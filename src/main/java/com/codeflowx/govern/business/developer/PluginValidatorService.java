package com.codeflowx.govern.business.developer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

/**
 * Servicio para validar plugins .cfx-plugin
 */
@Slf4j
@Service
public class PluginValidatorService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> validHooks = Arrays.asList(
            "onOnboard", "preInvoke", "postInvoke", "onPolicyViolation", "onTelemetry"
    );

    /**
     * Valida manifest.json
     */
    public void validateManifest(Map<String, Object> manifest) throws IllegalArgumentException {
        // Campos requeridos
        if (!manifest.containsKey("name")) {
            throw new IllegalArgumentException("Manifest debe contener 'name'");
        }
        if (!manifest.containsKey("version")) {
            throw new IllegalArgumentException("Manifest debe contener 'version'");
        }
        if (!manifest.containsKey("author")) {
            throw new IllegalArgumentException("Manifest debe contener 'author'");
        }
        if (!manifest.containsKey("description")) {
            throw new IllegalArgumentException("Manifest debe contener 'description'");
        }
        if (!manifest.containsKey("hooks")) {
            throw new IllegalArgumentException("Manifest debe contener 'hooks'");
        }
        if (!manifest.containsKey("capabilities")) {
            throw new IllegalArgumentException("Manifest debe contener 'capabilities'");
        }

        // Validar tipo de plugin
        String pluginType = (String) manifest.getOrDefault("type", "HOOKS_ONLY");
        if (!"HOOKS_ONLY".equals(pluginType) && !"FULL_EXTENSION".equals(pluginType)) {
            throw new IllegalArgumentException("Tipo de plugin inválido: " + pluginType +
                    ". Debe ser 'HOOKS_ONLY' o 'FULL_EXTENSION'");
        }

        // Si es FULL_EXTENSION, validar estructura
        if ("FULL_EXTENSION".equals(pluginType)) {
            validateFullExtensionManifest(manifest);
        }

        // Validar hooks
        Object hooksObj = manifest.get("hooks");
        if (!(hooksObj instanceof List)) {
            throw new IllegalArgumentException("'hooks' debe ser un array");
        }

        @SuppressWarnings("unchecked")
        List<String> hooks = (List<String>) hooksObj;
        for (String hook : hooks) {
            if (!validHooks.contains(hook)) {
                throw new IllegalArgumentException("Hook inválido: " + hook + ". Hooks válidos: " + validHooks);
            }
        }

        // Validar capabilities
        Object capabilitiesObj = manifest.get("capabilities");
        if (!(capabilitiesObj instanceof Map)) {
            throw new IllegalArgumentException("'capabilities' debe ser un objeto");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> capabilities = (Map<String, Object>) capabilitiesObj;
        if (!capabilities.containsKey("language")) {
            throw new IllegalArgumentException("'capabilities' debe contener 'language'");
        }

        String language = (String) capabilities.get("language");
        List<String> validLanguages = Arrays.asList("python", "typescript", "javascript", "java");
        if (!validLanguages.contains(language.toLowerCase())) {
            throw new IllegalArgumentException("Lenguaje no soportado: " + language);
        }

        // Validar versión (semver)
        String version = (String) manifest.get("version");
        if (!version.matches("^\\d+\\.\\d+\\.\\d+(-.*)?$")) {
            throw new IllegalArgumentException("Versión debe seguir formato semver: " + version);
        }

        log.info("Manifest validado correctamente: {} v{}", manifest.get("name"), version);
    }

    /**
     * Valida manifest de plugin completo (FULL_EXTENSION)
     */
    private void validateFullExtensionManifest(Map<String, Object> manifest) {
        // Validar procesos BPMN
        if (manifest.containsKey("processes")) {
            Object processesObj = manifest.get("processes");
            if (!(processesObj instanceof List)) {
                throw new IllegalArgumentException("'processes' debe ser un array");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> processes = (List<Map<String, Object>>) processesObj;
            for (Map<String, Object> process : processes) {
                if (!process.containsKey("file")) {
                    throw new IllegalArgumentException("Cada proceso debe tener 'file'");
                }
                if (!process.containsKey("process_id")) {
                    throw new IllegalArgumentException("Cada proceso debe tener 'process_id'");
                }
            }
        }

        // Validar pantallas ZKoss
        if (manifest.containsKey("screens")) {
            Object screensObj = manifest.get("screens");
            if (!(screensObj instanceof List)) {
                throw new IllegalArgumentException("'screens' debe ser un array");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> screens = (List<Map<String, Object>>) screensObj;
            for (Map<String, Object> screen : screens) {
                if (!screen.containsKey("file")) {
                    throw new IllegalArgumentException("Cada pantalla debe tener 'file'");
                }
                if (!screen.containsKey("route")) {
                    throw new IllegalArgumentException("Cada pantalla debe tener 'route'");
                }
            }
        }

        // Validar reglas Drools
        if (manifest.containsKey("rules")) {
            Object rulesObj = manifest.get("rules");
            if (!(rulesObj instanceof List)) {
                throw new IllegalArgumentException("'rules' debe ser un array");
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rules = (List<Map<String, Object>>) rulesObj;
            for (Map<String, Object> rule : rules) {
                if (!rule.containsKey("file")) {
                    throw new IllegalArgumentException("Cada regla debe tener 'file'");
                }
                if (!rule.containsKey("package")) {
                    throw new IllegalArgumentException("Cada regla debe tener 'package'");
                }
            }
        }
    }

    /**
     * Valida firma del plugin
     */
    public void validateSignature(Map<String, Object> manifest, Path pluginPath) throws Exception {
        if (!manifest.containsKey("signature")) {
            log.warn("Plugin sin firma: {}", manifest.get("name"));
            return;
        }

        String signature = (String) manifest.get("signature");
        String signatureAlgorithm = (String) manifest.getOrDefault("signature_algorithm", "HMAC-SHA256");

        // Obtener clave pública del autor (en producción, desde base de datos o keystore)
        String authorPublicKey = getAuthorPublicKey((String) manifest.get("author"));
        if (authorPublicKey == null) {
            throw new SecurityException("No se encontró clave pública para autor: " + manifest.get("author"));
        }

        // Calcular hash del código del plugin
        String codeHash = calculateCodeHash(pluginPath);

        // Verificar firma
        boolean isValid = verifySignature(codeHash, signature, authorPublicKey, signatureAlgorithm);
        if (!isValid) {
            throw new SecurityException("Firma del plugin inválida");
        }

        log.info("Firma validada correctamente para plugin: {}", manifest.get("name"));
    }

    /**
     * Calcula hash del código del plugin
     */
    private String calculateCodeHash(Path pluginPath) throws Exception {
        Path codeDir = pluginPath.resolve("code");
        if (!Files.exists(codeDir)) {
            throw new IllegalArgumentException("Directorio 'code' no encontrado en plugin");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        Files.walk(codeDir)
                .filter(Files::isRegularFile)
                .sorted()
                .forEach(path -> {
                    try {
                        byte[] fileBytes = Files.readAllBytes(path);
                        digest.update(fileBytes);
                    } catch (Exception e) {
                        log.error("Error calculando hash de " + path, e);
                    }
                });

        return bytesToHex(digest.digest());
    }

    /**
     * Verifica firma HMAC
     */
    private boolean verifySignature(String data, String signature, String key, String algorithm) throws Exception {
        Mac mac = Mac.getInstance(algorithm.replace("HMAC-", "Hmac"));
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String calculatedSignature = bytesToHex(hash);
        return calculatedSignature.equals(signature);
    }

    /**
     * Obtiene clave pública del autor (mock - en producción desde BD)
     */
    private String getAuthorPublicKey(String author) {
        // TODO: Implementar consulta a base de datos
        // Por ahora, retornar clave mock
        return "mock-public-key-for-" + author;
    }

    /**
     * Convierte bytes a hex
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
