package com.codeflowx.govern.business.developer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para cargar pantallas ZKoss desde plugins
 */
@Slf4j
@Service
public class PluginZkossLoaderService {

    private final Path webappPath;

    public PluginZkossLoaderService() {
        // Ruta al webapp de ZKoss (ajustar según tu estructura)
        this.webappPath = Paths.get("src/main/webapp/console/gobierno/plugins");
        try {
            Files.createDirectories(webappPath);
        } catch (IOException e) {
            log.error("Error creando directorio de plugins ZKoss", e);
        }
    }

    /**
     * Carga pantalla ZKoss desde plugin
     */
    public Map<String, Object> loadZkossScreen(
            Path zulPath,
            Map<String, Object> screenDef,
            Path pluginPath
    ) throws IOException {
        String route = (String) screenDef.getOrDefault("route",
                zulPath.getFileName().toString().replace(".zul", ""));
        String viewmodel = (String) screenDef.get("viewmodel");

        // Crear ruta en webapp
        String[] routeParts = route.split("/");
        Path targetDir = webappPath;
        for (int i = 0; i < routeParts.length - 1; i++) {
            targetDir = targetDir.resolve(routeParts[i]);
        }
        Files.createDirectories(targetDir);

        // Copiar ZUL
        String zulFileName = routeParts[routeParts.length - 1] + ".zul";
        Path targetZulPath = targetDir.resolve(zulFileName);
        Files.copy(zulPath, targetZulPath, StandardCopyOption.REPLACE_EXISTING);

        // Si hay ViewModel, copiar también (requiere compilación separada)
        if (viewmodel != null) {
            String viewmodelPath = viewmodel.replace(".", "/") + ".java";
            Path sourceViewModel = pluginPath.resolve("ui/viewmodels").resolve(viewmodelPath);

            if (Files.exists(sourceViewModel)) {
                // Nota: En producción, compilar ViewModel y cargar en classpath
                log.info("ViewModel encontrado: {} (requiere compilación manual)", viewmodel);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", "loaded");
        result.put("route", route);
        result.put("zul_path", targetZulPath.toString());
        result.put("viewmodel", viewmodel);
        result.put("file", zulPath.toString());

        log.info("Pantalla ZKoss cargada: {} -> {}", route, targetZulPath);
        return result;
    }
}
