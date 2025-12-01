package com.codeflowx.govern.business.developer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Servicio para cargar y validar plugins .cfx-plugin
 * Soporta plugins simples (hooks) y plugins completos (procesos, pantallas, reglas)
 */
@Slf4j
@Service
public class PluginLoaderService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PluginValidatorService validatorService;
    private final Path pluginStoragePath;

    @Autowired(required = false)
    private PluginBpmnLoaderService bpmnLoaderService;

    @Autowired(required = false)
    private PluginZkossLoaderService zkossLoaderService;

    @Autowired(required = false)
    private PluginDroolsLoaderService droolsLoaderService;

    public PluginLoaderService(PluginValidatorService validatorService) {
        this.validatorService = validatorService;
        this.pluginStoragePath = Paths.get(System.getProperty("user.home"), ".codeflowx", "plugins");
        try {
            Files.createDirectories(pluginStoragePath);
        } catch (IOException e) {
            log.error("Error creando directorio de plugins", e);
        }
    }

    /**
     * Carga plugin desde archivo .cfx-plugin
     */
    public Map<String, Object> loadPlugin(InputStream pluginStream, String pluginName) throws Exception {
        // Extraer ZIP
        Path extractPath = pluginStoragePath.resolve(pluginName);
        Files.createDirectories(extractPath);

        try (ZipInputStream zis = new ZipInputStream(pluginStream)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = extractPath.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zis, filePath);
                }
                zis.closeEntry();
            }
        }

        // Leer manifest.json
        Path manifestPath = extractPath.resolve("manifest.json");
        if (!Files.exists(manifestPath)) {
            throw new IllegalArgumentException("manifest.json no encontrado en plugin");
        }

        Map<String, Object> manifest = objectMapper.readValue(
                Files.readAllBytes(manifestPath),
                Map.class
        );

        // Validar manifest
        validatorService.validateManifest(manifest);

        // Validar firma si existe
        if (manifest.containsKey("signature")) {
            validatorService.validateSignature(manifest, extractPath);
        }

        // Determinar tipo de plugin
        String pluginType = (String) manifest.getOrDefault("type", "HOOKS_ONLY");

        // Cargar código del plugin (hooks)
        String codeLanguage = (String) ((Map<String, Object>) manifest.get("capabilities")).get("language");
        Path codePath = extractPath.resolve("code").resolve("plugin." + getFileExtension(codeLanguage));

        Map<String, Object> pluginData = new HashMap<>();
        pluginData.put("manifest", manifest);
        pluginData.put("code_path", codePath.toString());
        pluginData.put("extract_path", extractPath.toString());
        pluginData.put("name", manifest.get("name"));
        pluginData.put("version", manifest.get("version"));
        pluginData.put("type", pluginType);

        // Si es plugin completo, cargar procesos, pantallas y reglas
        if ("FULL_EXTENSION".equals(pluginType)) {
            loadFullExtensionPlugin(manifest, extractPath, pluginData);
        }

        log.info("Plugin cargado: {} v{} (tipo: {})", manifest.get("name"), manifest.get("version"), pluginType);
        return pluginData;
    }

    /**
     * Carga componentes de plugin completo (procesos, pantallas, reglas)
     */
    private void loadFullExtensionPlugin(
            Map<String, Object> manifest,
            Path extractPath,
            Map<String, Object> pluginData
    ) throws Exception {
        // Cargar procesos BPMN
        if (manifest.containsKey("processes") && bpmnLoaderService != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> processes = (List<Map<String, Object>>) manifest.get("processes");
            List<Map<String, Object>> loadedProcesses = new ArrayList<>();

            for (Map<String, Object> processDef : processes) {
                String bpmnFile = (String) processDef.get("file");
                Path bpmnPath = extractPath.resolve(bpmnFile);

                if (Files.exists(bpmnPath)) {
                    Map<String, Object> loaded = bpmnLoaderService.loadBpmnProcess(
                            bpmnPath,
                            processDef
                    );
                    loadedProcesses.add(loaded);
                    log.info("Proceso BPMN cargado: {}", bpmnFile);
                } else {
                    log.warn("Archivo BPMN no encontrado: {}", bpmnFile);
                }
            }
            pluginData.put("loaded_processes", loadedProcesses);
        }

        // Cargar pantallas ZKoss
        if (manifest.containsKey("screens") && zkossLoaderService != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> screens = (List<Map<String, Object>>) manifest.get("screens");
            List<Map<String, Object>> loadedScreens = new ArrayList<>();

            for (Map<String, Object> screenDef : screens) {
                String zulFile = (String) screenDef.get("file");
                Path zulPath = extractPath.resolve(zulFile);

                if (Files.exists(zulPath)) {
                    Map<String, Object> loaded = zkossLoaderService.loadZkossScreen(
                            zulPath,
                            screenDef,
                            extractPath
                    );
                    loadedScreens.add(loaded);
                    log.info("Pantalla ZKoss cargada: {}", zulFile);
                } else {
                    log.warn("Archivo ZUL no encontrado: {}", zulFile);
                }
            }
            pluginData.put("loaded_screens", loadedScreens);
        }

        // Cargar reglas Drools
        if (manifest.containsKey("rules") && droolsLoaderService != null) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rules = (List<Map<String, Object>>) manifest.get("rules");
            List<Map<String, Object>> loadedRules = new ArrayList<>();

            for (Map<String, Object> ruleDef : rules) {
                String drlFile = (String) ruleDef.get("file");
                Path drlPath = extractPath.resolve(drlFile);

                if (Files.exists(drlPath)) {
                    Map<String, Object> loaded = droolsLoaderService.loadDroolsRules(
                            drlPath,
                            ruleDef,
                            extractPath
                    );
                    loadedRules.add(loaded);
                    log.info("Reglas Drools cargadas: {}", drlFile);
                } else {
                    log.warn("Archivo DRL no encontrado: {}", drlFile);
                }
            }
            pluginData.put("loaded_rules", loadedRules);
        }
    }

    /**
     * Obtiene extensión de archivo según lenguaje
     */
    private String getFileExtension(String language) {
        switch (language.toLowerCase()) {
            case "python":
                return "py";
            case "typescript":
            case "javascript":
                return "js";
            case "java":
                return "java";
            default:
                return "txt";
        }
    }

    /**
     * Lista plugins cargados
     */
    public List<Map<String, Object>> listLoadedPlugins() {
        List<Map<String, Object>> plugins = new ArrayList<>();

        try {
            if (Files.exists(pluginStoragePath)) {
                Files.list(pluginStoragePath).forEach(pluginDir -> {
                    Path manifestPath = pluginDir.resolve("manifest.json");
                    if (Files.exists(manifestPath)) {
                        try {
                            Map<String, Object> manifest = objectMapper.readValue(
                                    Files.readAllBytes(manifestPath),
                                    Map.class
                            );
                            Map<String, Object> pluginInfo = new HashMap<>();
                            pluginInfo.put("name", manifest.get("name"));
                            pluginInfo.put("version", manifest.get("version"));
                            pluginInfo.put("path", pluginDir.toString());
                            plugins.add(pluginInfo);
                        } catch (Exception e) {
                            log.error("Error leyendo manifest de " + pluginDir, e);
                        }
                    }
                });
            }
        } catch (IOException e) {
            log.error("Error listando plugins", e);
        }

        return plugins;
    }

    /**
     * Elimina plugin cargado
     */
    public void unloadPlugin(String pluginName) throws IOException {
        Path pluginPath = pluginStoragePath.resolve(pluginName);
        if (Files.exists(pluginPath)) {
            deleteDirectory(pluginPath);
            log.info("Plugin eliminado: {}", pluginName);
        }
    }

    /**
     * Elimina directorio recursivamente
     */
    private void deleteDirectory(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.list(path).forEach(child -> {
                try {
                    deleteDirectory(child);
                } catch (IOException e) {
                    log.error("Error eliminando " + child, e);
                }
            });
        }
        Files.delete(path);
    }
}
