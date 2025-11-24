# PROMPT: INC-020 - Integración APIs Autoridades

**Incidencia:** INC-020  
**Prioridad:** 🔴 CRÍTICA  
**Artículo EU AI Act:** Art. 27.3, Art. 49  
**Esfuerzo Estimado:** 2 días + TBD  
**Tipo:** Java - Backend

---

## CONTEXTO

Notificación a autoridades (Art. 27.3) y registro UE (Art. 49) no están integrados con APIs oficiales. Solo hay mocks o endpoints configurados manualmente.

**Ubicación Actual:**
- `NotifyAuthorityFriaDelegate.java` - notificación mock
- `EURegistrationService.java` - registro pendiente

---

## REQUISITOS

1. Preparar integración con API oficial cuando esté disponible
2. Mantener mocks para testing
3. Documentar proceso de integración
4. Validar formato de datos según Anexo VIII

---

## IMPLEMENTACIÓN

### Crear servicio de notificación a autoridades

**Archivo:** `com.codeflowx.govern.business.compliance.AuthorityNotificationService.java`

```java
@Service
@Slf4j
public class AuthorityNotificationService {
    
    @Value("${authority.api.enabled:false}")
    private boolean realApiEnabled;
    
    @Value("${authority.api.url:}")
    private String authorityApiUrl;
    
    /**
     * Notifica autoridad según Art. 27.3
     */
    public void notifyAuthority(FriaAssessment fria) {
        // 1. Formatear según especificación oficial (Anexo VIII)
        AuthorityNotificationPayload payload = formatNotificationPayload(fria);
        
        // 2. Validar formato
        validateNotificationPayload(payload);
        
        // 3. Enviar (mock o real según configuración)
        if (realApiEnabled && !authorityApiUrl.isEmpty()) {
            AuthorityResponse response = authorityApiClient.sendNotification(payload);
            fria.setFrianotificationid(response.getNotificationId());
        } else {
            // Mock para testing
            MockAuthorityResponse mockResponse = mockAuthorityService.sendNotification(payload);
            fria.setFrianotificationid(mockResponse.getNotificationId());
        }
        
        fria.setFrianotified(true);
        fria.setFrianotificationdate(new Timestamp(System.currentTimeMillis()));
        friaService.save(fria);
    }
    
    /**
     * Formatea payload según Anexo VIII
     */
    private AuthorityNotificationPayload formatNotificationPayload(FriaAssessment fria) {
        AuthorityNotificationPayload payload = new AuthorityNotificationPayload();
        payload.setFriaId(fria.getIdxfriaassessment());
        payload.setProjectId(fria.getProject().getIdxproject());
        payload.setImpactSeverity(fria.getFriaimpactseverity());
        payload.setCharterArticles(fria.getFriacharterarticles());
        // ... más campos según Anexo VIII
        return payload;
    }
}
```

---

## REFERENCIAS

- **Art. 27.3 EU AI Act:** Notificación Autoridades
- **Art. 49 EU AI Act:** Registro UE
- **Anexo VIII:** Formato Notificación
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-020`

---

**Estado:** ✅ COMPLETADO

