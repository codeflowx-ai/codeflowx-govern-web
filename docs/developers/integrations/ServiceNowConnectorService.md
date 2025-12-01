# ServiceNowConnectorService

**Ubicación:** `com.codeflowx.govern.business.integrations.ServiceNowConnectorService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Conector ServiceNow ITSM para sincronizar change requests / incidentes como datasets gobernados.

---

## 🎯 Responsabilidades

- Sincronizar tickets de ServiceNow (change requests, incidentes)
- Mapear tickets a datasets externos
- Integrar con workflows de gobernanza

---

## 📚 API Pública

### `syncTickets(Long platformId)`

Sincroniza tickets de ServiceNow según configuración.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como SERVICENOW

**Retorna:** `List<ExternalDataset>` - Tickets sincronizados como datasets

**Ejemplo:**
```java
List<ExternalDataset> tickets = serviceNowService.syncTickets(platformId);
```

---

## ⚙️ Configuración

**Property:** `govern.integrations.servicenow.timeout-ms` (default: 30000)

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `instanceUrl`: URL de la instancia ServiceNow
- `username`: Usuario
- `password`: Password
- `table`: Tabla a consultar (ej: "change_request", "incident")
- `query`: Query de filtrado (sysparm_query)
- `limit`: Límite de resultados

---

## 📖 Referencias

- **ServiceNow API:** https://developer.servicenow.com/
- **Integración:** Usado por ExternalIntegrationBusinessService

---

**Última actualización:** 25 de noviembre de 2025
