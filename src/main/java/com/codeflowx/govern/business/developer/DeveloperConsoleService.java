package com.codeflowx.govern.business.developer;

import com.codeflowx.govern.entity.aios.AioComponent;
import com.codeflowx.govern.entity.aios.AioMarketplaceEntry;
import com.codeflowx.govern.entity.aios.enums.AioComponentType;
import com.codeflowx.govern.repository.aios.AioComponentRepository;
import com.codeflowx.govern.repository.aios.AioMarketplaceEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para Developer Console
 */
@Slf4j
@Service
public class DeveloperConsoleService {

    @Autowired
    private AioMarketplaceEntryRepository marketplaceEntryRepository;

    @Autowired
    private AioComponentRepository componentRepository;

    @Autowired
    private PluginLoaderService pluginLoaderService;

    @Autowired
    private ApiKeyService apiKeyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lista todos los plugins registrados
     */
    public List<AioMarketplaceEntry> listPlugins() {
        return marketplaceEntryRepository.findAll()
                .stream()
                .filter(entry -> "PLUGIN".equals(entry.getEntryType()))
                .collect(Collectors.toList());
    }

    /**
     * Lista componentes MCP
     */
    public List<AioComponent> listMcpComponents() {
        return componentRepository.findByAioComponenttype(AioComponentType.MCP);
    }

    /**
     * Registra un nuevo plugin desde manifest.json
     */
    @Transactional
    public void registerPlugin(String name, String version, String description, String manifestJson) {
        try {
            // Parsear manifest
            Map<String, Object> manifest = objectMapper.readValue(manifestJson, Map.class);

            // Validar manifest
            validatePluginManifest(manifest);

            // Crear AioMarketplaceEntry
            AioMarketplaceEntry entry = new AioMarketplaceEntry();
            entry.setEntryName(name);
            entry.setEntryVersion(version);
            entry.setEntryDescription(description);
            entry.setEntryType("PLUGIN");
            entry.setEntryMetadata(manifestJson);
            entry.setEntryStatus("ACTIVE");
            entry.setEntryCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));

            // Crear AioComponent asociado (type=MCP)
            AioComponent component = new AioComponent();
            component.setAiocomponentname(name);
            component.setAiocomponentnamespace("mcp");
            component.setAiocomponentcode(name.toLowerCase().replaceAll("\\s+", "-"));
            component.setAiocomponenttype(AioComponentType.MCP);
            component.setAiocomponentversion(version);
            component.setAiocomponentstate(com.codeflowx.govern.entity.aios.enums.AioComponentState.ACTIVE);
            component.setAiocomponentmetadata(manifestJson);

            componentRepository.save(component);
            entry.setComponent(component);
            marketplaceEntryRepository.save(entry);

            log.info("Plugin registrado: {} v{}", name, version);
        } catch (Exception e) {
            log.error("Error registrando plugin", e);
            throw new RuntimeException("Error registrando plugin: " + e.getMessage(), e);
        }
    }

    /**
     * Valida manifest.json de plugin
     */
    private void validatePluginManifest(Map<String, Object> manifest) {
        if (!manifest.containsKey("name")) {
            throw new IllegalArgumentException("Manifest debe contener 'name'");
        }
        if (!manifest.containsKey("version")) {
            throw new IllegalArgumentException("Manifest debe contener 'version'");
        }
        if (!manifest.containsKey("hooks")) {
            throw new IllegalArgumentException("Manifest debe contener 'hooks'");
        }

        // Validar hooks
        Object hooksObj = manifest.get("hooks");
        if (!(hooksObj instanceof List)) {
            throw new IllegalArgumentException("'hooks' debe ser un array");
        }

        List<String> validHooks = Arrays.asList("onOnboard", "preInvoke", "postInvoke", "onPolicyViolation", "onTelemetry");
        @SuppressWarnings("unchecked")
        List<String> hooks = (List<String>) hooksObj;
        for (String hook : hooks) {
            if (!validHooks.contains(hook)) {
                throw new IllegalArgumentException("Hook inválido: " + hook);
            }
        }
    }

    /**
     * Lista API keys del usuario actual
     */
    public List<Map<String, Object>> listApiKeys() {
        // TODO: Implementar con autenticación real
        return apiKeyService.listApiKeys();
    }

    /**
     * Genera nueva API key
     */
    public Map<String, Object> generateApiKey() {
        return apiKeyService.generateApiKey();
    }

    /**
     * Elimina API key
     */
    @Transactional
    public void deleteApiKey(String apiKeyId) {
        apiKeyService.deleteApiKey(apiKeyId);
    }

    /**
     * Prueba endpoint del API
     */
    public String testEndpoint(String endpoint, String method, String payload) {
        try {
            // TODO: Implementar llamada HTTP real
            return "{\"status\": \"success\", \"message\": \"Endpoint test - implementar HTTP client\"}";
        } catch (Exception e) {
            log.error("Error probando endpoint", e);
            throw new RuntimeException("Error probando endpoint: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene logs recientes
     */
    public List<Map<String, Object>> getRecentLogs(int limit) {
        // TODO: Implementar consulta a ImmutableLog
        return new ArrayList<>();
    }
}
