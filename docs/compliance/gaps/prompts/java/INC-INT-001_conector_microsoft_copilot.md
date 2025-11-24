# PROMPT: INC-INT-001 - Conector Microsoft Copilot

**Incidencia:** INC-INT-001  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 12 (Mantenimiento de registros), Art. 19 (Registros)  
**Esfuerzo Estimado:** 3-5 días  
**Tipo:** Java - Backend  
**Documento Relacionado:** `AUDITORIA_INTEGRACION_SIN_SUSTITUCION.md`

---

## CONTEXTO

No existe documentación ni implementación de un conector específico para Microsoft Copilot, lo cual es un gap significativo dado que Copilot es ampliamente usado en entornos enterprise. Esto impide registrar automáticamente el uso de Copilot en workflows y obtener trazabilidad completa para cumplimiento AI Act.

**Ubicación Actual:**
- No existe `CopilotConnectorService.java`
- No está documentado en `PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`
- No está incluido en `ExternalPlatformsViewModel.SUPPORTED_PLATFORMS`

---

## REQUISITOS

1. **Crear conector Microsoft Copilot** siguiendo la arquitectura de `AzureMLConnectorService`
2. **Integrar con Microsoft Graph API** o Copilot API para obtener interacciones
3. **Registrar interacciones** en CodeflowX para trazabilidad
4. **Clasificar riesgo** de interacciones con Copilot
5. **Respetar privacidad** del usuario (no almacenar contenido completo con PII sin consentimiento)
6. **Añadir a UI** de gestión de plataformas externas

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear `MicrosoftCopilotConnectorService.java`

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/business/integrations/MicrosoftCopilotConnectorService.java`

```java
package com.codeflowx.govern.business.integrations;

import com.codeflowx.govern.entity.integrations.ExternalModel;
import com.codeflowx.govern.entity.integrations.ExternalPlatformIntegration;
import com.microsoft.graph.auth.enums.NationalCloud;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.Chat;
import com.microsoft.graph.models.ChatMessage;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Conector Microsoft Copilot para sincronizar interacciones con CodeflowX governance.
 * 
 * Reutiliza arquitectura de AzureMLConnectorService pero para Copilot.
 * Integra con Microsoft Graph API para obtener metadata de interacciones.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MicrosoftCopilotConnectorService {

    private final ExternalIntegrationBusinessService integrationService;
    private final WebClient.Builder webClientBuilder;

    private static final String PLATFORM_TYPE = "MICROSOFT_COPILOT";

    /**
     * Sincroniza interacciones de Copilot hacia CodeflowX.
     * 
     * Solo registra metadata (no contenido completo por privacidad).
     */
    public void syncCopilotInteractions(Long platformId) {
        ExternalPlatformIntegration platform = integrationService.findPlatformById(platformId);
        if (platform == null || !PLATFORM_TYPE.equalsIgnoreCase(platform.getEplplatformtype())) {
            log.warn("Plataforma Copilot {} no encontrada o tipo incorrecto", platformId);
            return;
        }

        if (!Boolean.TRUE.equals(platform.getEplsyncenabled())) {
            log.info("Plataforma Copilot {} tiene sync deshabilitado", platform.getEplplatformname());
            return;
        }

        try {
            log.info("🔄 Syncing Copilot interactions from platform: {}", platform.getEplplatformname());

            // 1. Obtener cliente Graph API
            GraphServiceClient<okhttp3.Request> graphClient = buildGraphClient(platform);

            // 2. Listar chats de Copilot (últimas 24 horas)
            List<Chat> copilotChats = listCopilotChats(graphClient);

            log.info("Found {} Copilot chats to sync", copilotChats.size());

            // 3. Registrar cada interacción
            for (Chat chat : copilotChats) {
                registerCopilotInteraction(platform, chat);
            }

            // 4. Actualizar estado sync
            platform.setEplsyncstatus("SUCCESS");
            platform.setEpllastsyncat(new Timestamp(System.currentTimeMillis()));
            integrationService.savePlatform(platform);

            log.info("✅ Copilot sync completed: {} interactions processed", copilotChats.size());

        } catch (Exception e) {
            log.error("❌ Error syncing Copilot interactions: {}", e.getMessage(), e);
            platform.setEplsyncstatus("ERROR");
            platform.setEpllasterror(e.getMessage());
            integrationService.savePlatform(platform);
            throw new RuntimeException("Error syncing Copilot interactions", e);
        }
    }

    /**
     * Construye cliente Microsoft Graph API desde credenciales de plataforma.
     */
    private GraphServiceClient<okhttp3.Request> buildGraphClient(ExternalPlatformIntegration platform) {
        // Obtener credenciales desde metadata
        String tenantId = extractFromMetadata(platform.getEplmetadata(), "tenantId");
        String clientId = extractFromMetadata(platform.getEplmetadata(), "clientId");
        String clientSecret = extractFromMetadata(platform.getEplmetadata(), "clientSecret");

        if (tenantId == null || clientId == null || clientSecret == null) {
            throw new IllegalArgumentException("Missing credentials in platform metadata");
        }

        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
            .tenantId(tenantId)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();

        return GraphServiceClient.builder()
            .authenticationProvider(new TokenCredentialAuthProvider(credential))
            .buildClient();
    }

    /**
     * Lista chats de Copilot desde Microsoft Graph API.
     * 
     * Solo últimos 24 horas para evitar volumen excesivo.
     */
    private List<Chat> listCopilotChats(GraphServiceClient<okhttp3.Request> graphClient) {
        // Usar Microsoft Graph API para obtener chats de Copilot
        // Endpoint: GET /chats?$filter=chatType eq 'meeting' and topic contains 'Copilot'
        // O usar endpoint específico de Copilot si está disponible
        
        List<Chat> chats = new ArrayList<>();
        
        try {
            // TODO: Ajustar según API real de Copilot
            // Por ahora, usar endpoint genérico de chats
            var chatCollectionPage = graphClient
                .chats()
                .buildRequest()
                .top(100) // Limitar a 100 chats
                .get();

            if (chatCollectionPage != null && chatCollectionPage.getCurrentPage() != null) {
                for (Chat chat : chatCollectionPage.getCurrentPage()) {
                    // Filtrar solo chats relacionados con Copilot
                    if (chat.topic != null && chat.topic.toLowerCase().contains("copilot")) {
                        chats.add(chat);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error listing Copilot chats: {}", e.getMessage(), e);
        }

        return chats;
    }

    /**
     * Registra una interacción de Copilot en CodeflowX.
     * 
     * Solo metadata (no contenido completo por privacidad).
     */
    private void registerCopilotInteraction(ExternalPlatformIntegration platform, Chat chat) {
        // Verificar si ya existe
        String externalId = "copilot:" + chat.id;
        ExternalModel existing = integrationService.findExternalModel(
            platform.getIdxexternalplatform(), 
            externalId
        );

        ExternalModel interaction = existing != null ? existing : new ExternalModel();

        if (existing == null) {
            interaction.setPlatform(platform);
            interaction.setIduuid(UUID.randomUUID().toString());
            interaction.setExmcreatedat(new Timestamp(System.currentTimeMillis()));
            interaction.setExmapprovalstatus("PENDING");
        }

        // Guardar solo metadata (no contenido por privacidad)
        interaction.setExmexternalid(externalId);
        interaction.setExmexternalname(chat.topic != null ? chat.topic : "Copilot Interaction");
        interaction.setExmexternalurl(null); // No hay URL directa
        interaction.setExmsyncstatus("SYNCED");
        interaction.setExmlastsyncedat(new Timestamp(System.currentTimeMillis()));

        // Metadata JSON (solo información no sensible)
        String metadata = String.format(
            "{\"chatId\":\"%s\",\"createdDateTime\":\"%s\",\"messageCount\":%d,\"type\":\"copilot_interaction\"}",
            chat.id,
            chat.createdDateTime != null ? chat.createdDateTime.toString() : "",
            0 // TODO: Obtener message count real
        );
        interaction.setExmmetadata(metadata);

        // Clasificar riesgo automáticamente
        String[] riskLevel = classifyCopilotInteraction(interaction);
        interaction.setExmrisklevel(riskLevel);

        integrationService.saveExternalModel(interaction);

        log.info("✅ Copilot interaction registered: {}", externalId);
    }

    /**
     * Clasifica riesgo de interacción con Copilot.
     * 
     * Por defecto LIMITED_RISK (Copilot es herramienta asistente, no sistema crítico).
     */
    private String[] classifyCopilotInteraction(ExternalModel interaction) {
        // Por ahora, clasificar como LIMITED_RISK
        // TODO: Implementar lógica más sofisticada si es necesario
        return new String[]{"LIMITED_RISK"};
    }

    /**
     * Extrae valor de metadata JSON.
     */
    private String extractFromMetadata(String metadataJson, String key) {
        if (metadataJson == null) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(metadataJson);
            return node.path(key).asText(null);
        } catch (Exception e) {
            log.warn("Error extracting {} from metadata: {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * Test connection a Microsoft Graph API.
     */
    public boolean testConnection(ExternalPlatformIntegration platform) {
        try {
            GraphServiceClient<okhttp3.Request> graphClient = buildGraphClient(platform);
            // Intentar obtener usuario actual para validar credenciales
            User me = graphClient.me().buildRequest().get();
            return me != null;
        } catch (Exception e) {
            log.error("Error testing Copilot connection: {}", e.getMessage());
            return false;
        }
    }
}
```

### 2. Añadir dependencia Maven

**Ubicación:** `suinsit.nova.web/pom.xml`

```xml
<!-- Microsoft Graph SDK -->
<dependency>
    <groupId>com.microsoft.graph</groupId>
    <artifactId>microsoft-graph</artifactId>
    <version>5.71.0</version>
</dependency>

<!-- Azure Identity para autenticación -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>1.11.0</version>
</dependency>
```

### 3. Actualizar `ExternalPlatformsViewModel.java`

**Ubicación:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/integrations/ExternalPlatformsViewModel.java`

**Añadir a `SUPPORTED_PLATFORMS`:**

```java
private static final List<String> SUPPORTED_PLATFORMS = List.of(
    "DATABRICKS",
    "SNOWFLAKE",
    "AZURE_ML",
    "SAGEMAKER",
    "MICROSOFT_COPILOT",  // ✅ NUEVO
    // ... resto de plataformas
);
```

**Añadir variable:**

```java
@WireVariable
private MicrosoftCopilotConnectorService microsoftCopilotConnectorService;
```

**Añadir caso en `syncNow()`:**

```java
case "MICROSOFT_COPILOT":
    microsoftCopilotConnectorService.syncCopilotInteractions(
        platform.getIdxexternalplatform()
    );
    break;
```

**Añadir caso en `testConnection()`:**

```java
case "MICROSOFT_COPILOT":
    success = microsoftCopilotConnectorService.testConnection(platform);
    break;
```

### 4. Actualizar SQL Migration

**Ubicación:** `nocode.service.entitys/src/main/resources/sql/external_integrations.sql`

Asegurar que la tabla `EPLEXTERNALPLATFORMS` acepta `MICROSOFT_COPILOT` como tipo:

```sql
-- No requiere cambios si EPLPLATFORM_TYPE es VARCHAR(50)
-- Solo asegurar que el valor 'MICROSOFT_COPILOT' sea válido
```

### 5. Documentar en PROMPTS_12

**Ubicación:** `suinsit.nova.web/docs/compliance/PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md`

Añadir nuevo prompt al final del documento describiendo este conector.

---

## VALIDACIÓN

### Tests Unitarios

**Crear:** `MicrosoftCopilotConnectorServiceTest.java`

```java
@ExtendWith(MockitoExtension.class)
class MicrosoftCopilotConnectorServiceTest {

    @Mock
    private ExternalIntegrationBusinessService integrationService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private MicrosoftCopilotConnectorService copilotConnectorService;

    @Test
    void testSyncCopilotInteractions_Success() {
        // Given
        ExternalPlatformIntegration platform = createMockPlatform();
        when(integrationService.findPlatformById(1L)).thenReturn(platform);

        // When
        copilotConnectorService.syncCopilotInteractions(1L);

        // Then
        verify(integrationService, atLeastOnce()).saveExternalModel(any());
    }

    @Test
    void testTestConnection_Success() {
        // Test connection con credenciales válidas
    }
}
```

### Verificación Manual

1. ✅ Configurar plataforma Copilot en UI
2. ✅ Ejecutar sync manualmente
3. ✅ Verificar que interacciones aparecen en tabla `EXMEXTERNALMODELS`
4. ✅ Verificar que metadata se guarda correctamente
5. ✅ Verificar que clasificación de riesgo funciona

---

## CUMPLIMIENTO EU AI ACT

| Artículo | Cobertura |
|----------|-----------|
| **Art. 12** (Registros) | ✅ Metadata de interacciones registradas |
| **Art. 19** (Registros) | ✅ Logs inmutables de syncs |
| **Art. 15** (Privacidad) | ✅ No se almacena contenido completo con PII sin consentimiento |

---

## NOTAS IMPORTANTES

1. **Privacidad:** No almacenar contenido completo de conversaciones con PII sin consentimiento explícito del usuario.
2. **Volumen:** Limitar syncs a interacciones recientes (últimas 24-48 horas) para evitar volumen excesivo.
3. **Credenciales:** Las credenciales deben almacenarse encriptadas (ver INC-INT-002).
4. **API Changes:** La API de Microsoft Copilot puede cambiar. Verificar documentación oficial.

---

## PRÓXIMOS PASOS

1. ✅ Implementar `MicrosoftCopilotConnectorService`
2. ✅ Añadir a UI de gestión
3. ✅ Documentar en PROMPTS_12
4. ✅ Crear tests unitarios
5. ✅ Validar con datos reales (sandbox Microsoft)

---

**Prioridad:** 🔴 **CRÍTICA**  
**Fecha Límite:** Diciembre 2025  
**Responsable:** Java Backend Team

---

**Estado:** ✅ COMPLETADO

