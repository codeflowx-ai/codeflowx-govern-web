package com.codeflowx.govern.business.developer;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para cargar reglas Drools desde plugins
 */
@Slf4j
@Service
public class PluginDroolsLoaderService {

    private final KieServices kieServices;
    private final Map<String, KieContainer> pluginKieContainers = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private org.kie.api.runtime.KieContainer mainKieContainer;

    public PluginDroolsLoaderService() {
        this.kieServices = KieServices.Factory.get();
    }

    /**
     * Carga reglas Drools desde plugin
     */
    public Map<String, Object> loadDroolsRules(
            Path drlPath,
            Map<String, Object> ruleDef,
            Path pluginPath
    ) throws IOException {
        String packageName = (String) ruleDef.getOrDefault("package", "com.codeflowx.plugin.rules");
        String kbaseName = (String) ruleDef.getOrDefault("kbase", "plugin-rules");
        String pluginName = pluginPath.getFileName().toString();

        try {
            // Crear KieFileSystem
            KieFileSystem kfs = kieServices.newKieFileSystem();

            // Leer archivo DRL
            byte[] drlContent = Files.readAllBytes(drlPath);
            String drlResourcePath = "src/main/resources/" + drlPath.getFileName().toString();
            kfs.write(drlResourcePath, new String(drlContent));

            // Cargar Facts si existen
            Path factsDir = pluginPath.resolve("facts");
            if (Files.exists(factsDir)) {
                Files.walk(factsDir)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .forEach(factFile -> {
                            try {
                                byte[] factContent = Files.readAllBytes(factFile);
                                String factResourcePath = "src/main/java/" +
                                        factFile.getFileName().toString();
                                kfs.write(factResourcePath, new String(factContent));
                            } catch (IOException e) {
                                log.error("Error cargando fact: " + factFile, e);
                            }
                        });
            }

            // Construir KieModule
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
                throw new RuntimeException("Error compilando reglas Drools: " +
                        kieBuilder.getResults().getMessages());
            }

            KieModule kieModule = kieBuilder.getKieModule();
            ReleaseId releaseId = kieModule.getReleaseId();
            KieContainer kieContainer = kieServices.newKieContainer(releaseId);

            // Registrar KieContainer
            String containerKey = pluginName + ":" + kbaseName;
            pluginKieContainers.put(containerKey, kieContainer);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "loaded");
            result.put("package", packageName);
            result.put("kbase", kbaseName);
            result.put("container_key", containerKey);
            result.put("file", drlPath.toString());

            log.info("Reglas Drools cargadas: {} (kbase: {})", drlPath.getFileName(), kbaseName);
            return result;
        } catch (Exception e) {
            log.error("Error cargando reglas Drools: " + drlPath, e);
            throw new RuntimeException("Error cargando reglas Drools: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene KieContainer de plugin
     */
    public KieContainer getPluginKieContainer(String containerKey) {
        return pluginKieContainers.get(containerKey);
    }

    /**
     * Ejecuta reglas de plugin
     */
    public void executePluginRules(String containerKey, String sessionName, Object fact) {
        KieContainer container = pluginKieContainers.get(containerKey);
        if (container == null) {
            throw new IllegalArgumentException("KieContainer no encontrado: " + containerKey);
        }

        try {
            org.kie.api.runtime.StatelessKieSession session =
                    container.newStatelessKieSession(sessionName);
            session.execute(fact);
            log.info("Reglas de plugin ejecutadas: {} / {}", containerKey, sessionName);
        } catch (Exception e) {
            log.error("Error ejecutando reglas de plugin: " + containerKey, e);
            throw new RuntimeException("Error ejecutando reglas: " + e.getMessage(), e);
        }
    }
}
