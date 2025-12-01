package com.codeflowx.govern.viewmodels.developer;

import com.codeflowx.govern.business.developer.DeveloperConsoleService;
import com.codeflowx.govern.entity.aios.AioComponent;
import com.codeflowx.govern.entity.aios.AioMarketplaceEntry;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox;

import java.util.List;
import java.util.Map;

/**
 * ViewModel principal para Developer Console
 */
@Slf4j
@Getter
@Setter
public class DeveloperConsoleViewModel {

    @Wire
    private Window developerConsoleWin;

    private DeveloperConsoleService developerConsoleService;

    // Datos para tabs
    private List<AioMarketplaceEntry> plugins;
    private List<AioComponent> mcpComponents;
    private List<Map<String, Object>> apiKeys;
    private List<Map<String, Object>> logs;

    // Selección actual
    private AioMarketplaceEntry selectedPlugin;
    private AioComponent selectedMcp;
    private Map<String, Object> selectedApiKey;

    // Formularios
    private String newPluginName;
    private String newPluginVersion;
    private String newPluginDescription;
    private String newPluginManifest;

    // Playground
    private String playgroundEndpoint;
    private String playgroundMethod;
    private String playgroundPayload;
    private String playgroundResponse;

    @Init
    public void init() {
        log.info("Inicializando Developer Console");
        loadPlugins();
        loadMcpComponents();
        loadApiKeys();
    }

    @Command
    @NotifyChange({"plugins"})
    public void loadPlugins() {
        try {
            plugins = developerConsoleService.listPlugins();
        } catch (Exception e) {
            log.error("Error cargando plugins", e);
            Messagebox.show("Error cargando plugins: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"mcpComponents"})
    public void loadMcpComponents() {
        try {
            mcpComponents = developerConsoleService.listMcpComponents();
        } catch (Exception e) {
            log.error("Error cargando MCP components", e);
        }
    }

    @Command
    @NotifyChange({"apiKeys"})
    public void loadApiKeys() {
        try {
            apiKeys = developerConsoleService.listApiKeys();
        } catch (Exception e) {
            log.error("Error cargando API keys", e);
        }
    }

    @Command
    @NotifyChange({"plugins", "newPluginName", "newPluginVersion", "newPluginDescription", "newPluginManifest"})
    public void registerPlugin() {
        try {
            if (newPluginName == null || newPluginName.trim().isEmpty()) {
                Messagebox.show("El nombre del plugin es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            developerConsoleService.registerPlugin(
                    newPluginName,
                    newPluginVersion,
                    newPluginDescription,
                    newPluginManifest
            );

            Messagebox.show("Plugin registrado exitosamente", "Éxito", Messagebox.OK, Messagebox.INFORMATION);

            // Limpiar formulario
            newPluginName = null;
            newPluginVersion = null;
            newPluginDescription = null;
            newPluginManifest = null;

            loadPlugins();
        } catch (Exception e) {
            log.error("Error registrando plugin", e);
            Messagebox.show("Error registrando plugin: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"apiKeys"})
    public void generateApiKey() {
        try {
            Map<String, Object> newKey = developerConsoleService.generateApiKey();
            Messagebox.show(
                    "API Key generada: " + newKey.get("api_key"),
                    "API Key Generada",
                    Messagebox.OK,
                    Messagebox.INFORMATION
            );
            loadApiKeys();
        } catch (Exception e) {
            log.error("Error generando API key", e);
            Messagebox.show("Error generando API key: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    @NotifyChange({"playgroundResponse"})
    public void testEndpoint() {
        try {
            if (playgroundEndpoint == null || playgroundEndpoint.trim().isEmpty()) {
                Messagebox.show("El endpoint es requerido", "Validación", Messagebox.OK, Messagebox.EXCLAMATION);
                return;
            }

            playgroundResponse = developerConsoleService.testEndpoint(
                    playgroundEndpoint,
                    playgroundMethod != null ? playgroundMethod : "GET",
                    playgroundPayload
            );
        } catch (Exception e) {
            log.error("Error probando endpoint", e);
            playgroundResponse = "Error: " + e.getMessage();
        }
    }

    @Command
    public void viewPluginDetails(@BindingParam("plugin") AioMarketplaceEntry plugin) {
        selectedPlugin = plugin;
        // Abrir ventana de detalles
    }

    @Command
    public void deleteApiKey(@BindingParam("apiKey") Map<String, Object> apiKey) {
        try {
            int result = Messagebox.show(
                    "¿Está seguro de eliminar esta API key?",
                    "Confirmar",
                    Messagebox.YES | Messagebox.NO,
                    Messagebox.QUESTION
            );

            if (result == Messagebox.YES) {
                developerConsoleService.deleteApiKey((String) apiKey.get("id"));
                loadApiKeys();
                Messagebox.show("API key eliminada", "Éxito", Messagebox.OK, Messagebox.INFORMATION);
            }
        } catch (Exception e) {
            log.error("Error eliminando API key", e);
            Messagebox.show("Error eliminando API key: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @Command
    public void refreshLogs() {
        try {
            logs = developerConsoleService.getRecentLogs(100);
        } catch (Exception e) {
            log.error("Error cargando logs", e);
        }
    }
}
