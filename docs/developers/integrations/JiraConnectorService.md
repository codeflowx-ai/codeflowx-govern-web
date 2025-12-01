# JiraConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.JiraConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector Jira Cloud para sincronizar issues de gobernanza (riesgos, tareas de aprobación) como datasets externos.

---

## 🎯 Responsabilidades

- Sincronizar issues de Jira
- Mapear issues a datasets externos
- Integrar con workflows de gobernanza

---

## 📚 API Pública

### `syncIssues(Long platformId)`

Sincroniza issues de un proyecto Jira.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como JIRA

**Retorna:** `List<ExternalDataset>` - Issues sincronizados como datasets

**Ejemplo:**
```java
List<ExternalDataset> issues = jiraService.syncIssues(platformId);
```

---

## ⚙️ Configuración

**Property:** `govern.integrations.jira.timeout-ms` (default: 30000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `baseUrl`: URL base de Jira (ej: "https://company.atlassian.net")
- `username`: Usuario o email
- `apiToken`: API Token de Jira
- `projectKey`: Clave del proyecto a sincronizar
- `maxResults`: Número máximo de resultados

---

## 📖 Referencias

- **Jira API:** https://developer.atlassian.com/cloud/jira/platform/rest/v3/
- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
