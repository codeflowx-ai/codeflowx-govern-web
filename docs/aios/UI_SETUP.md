# Configuración de UI para AI OS - Agent Supervisor

**Versión:** 1.0.0 Early Access
**Fecha:** 2024-12-19

---

## 📋 Componentes Creados

### ViewModels

1. **AgentListViewModel** (`com.codeflowx.govern.viewmodel.aios.AgentListViewModel`)
   - Listado de agentes desde `AioComponent` (type=AGENT)
   - Estado runtime, loops detectados, última actividad
   - Acciones: PAUSE, RESUME, TERMINATE, VIEW LOGS

2. **AgentDetailViewModel** (`com.codeflowx.govern.viewmodel.aios.AgentDetailViewModel`)
   - Detalle completo del agente
   - Estado runtime en tiempo real
   - Acciones de control

3. **AgentMemoryListViewModel** (`com.codeflowx.govern.viewmodel.aios.AgentMemoryListViewModel`)
   - Listado de memorias del agente
   - Filtros por namespace
   - Ver contenido decifrado (requiere AIOS_ADMIN)
   - Comparar versiones, borrar versión

4. **AiRuntimeDashboardViewModel** (`com.codeflowx.govern.viewmodel.aios.AiRuntimeDashboardViewModel`)
   - KPIs: agentes activos, loops, violaciones, memorias, alertas BPMN
   - Gráficas: actividad horaria, agentes por estado, distribución de riesgos
   - Últimos eventos

5. **SupervisorMonitorViewModel** (`com.codeflowx.govern.viewmodel.aios.SupervisorMonitorViewModel`)
   - Estado del Supervisor y Worker
   - Test de integridad (MinIO, TimescaleDB, RabbitMQ, Hash Chain)
   - Logs recientes

### Pantallas ZUL

1. **agent-list.zul** (`/console/aios/agents/agent-list.zul`)
   - Tabla de agentes con métricas KPIs
   - Filtros y búsqueda
   - Acciones por agente

2. **agent-detail.zul** (`/console/aios/agents/agent-detail.zul`)
   - Información general del agente
   - Estado runtime
   - Acciones de control

3. **memory-list.zul** (`/console/aios/agents/memory-list.zul`)
   - Tabla de memorias
   - Filtros por namespace
   - Acciones sobre memorias

4. **ai-runtime-dashboard.zul** (`/console/aios/dashboard/ai-runtime-dashboard.zul`)
   - Dashboard con KPIs y gráficas
   - Últimos eventos

5. **supervisor-monitor.zul** (`/console/aios/agents/supervisor-monitor.zul`)
   - Monitor del supervisor
   - Tests de integridad

### Cliente REST

- **AgentSupervisorClient** (`com.codeflowx.govern.viewmodel.aios.client.AgentSupervisorClient`)
  - Cliente para interactuar con `codeflowx-agent-supervisor` (puerto 8087)

---

## 🔧 Configuración Requerida

### 1. Application Properties

Añadir en `application.yml`:

```yaml
agent:
  supervisor:
    api:
      url: http://localhost:8087/api/v1/supervisor  # Local
      # url: http://codeflowx-agent-supervisor:8087/api/v1/supervisor  # K8s
```

### 2. Menú

Añadir 3 entradas al menú (archivo `root.json` o configuración de menú):

```json
{
  "name": "AI OS",
  "iconClass": "fa-robot",
  "items": [
    {
      "name": "Agentes",
      "pageUrl": "/console/aios/agents/agent-list.zul",
      "iconClass": "fa-robot"
    },
    {
      "name": "Dashboard Runtime",
      "pageUrl": "/console/aios/dashboard/ai-runtime-dashboard.zul",
      "iconClass": "fa-chart-line"
    },
    {
      "name": "Monitor Supervisor",
      "pageUrl": "/console/aios/agents/supervisor-monitor.zul",
      "iconClass": "fa-shield-halved"
    }
  ]
}
```

### 3. Dependencias

Verificar que están disponibles:
- `codeflowx.govern.services` (AioComponentService)
- `codeflowx.nocode.persist` (BusinessService)
- `codeflowx.govern.business` (ImmutableLoggingBusinessService)
- Spring Web (RestTemplate)

---

## 🎨 Estilo Visual

Los componentes siguen el estilo corporativo:
- **Azul oscuro:** `#0B2A4A` (fondo principal)
- **Azul claro:** `#007BFF` (botones primarios)
- **Gris claro:** `#F4F6F8` (fondos secundarios)
- **Cards simples** con sombras ligeras
- **Grid limpio** con espaciado consistente
- **Iconografía mínima** (Font Awesome)

---

## 📊 Gráficas

Las gráficas están preparadas para integrar **ZK Charts**:
- Línea: actividad por hora
- Barras: agentes por estado
- Pie: distribución de riesgos

**Nota:** La integración de ZK Charts requiere configuración adicional.

---

## 🔐 Seguridad

- **Autorización:** Las acciones (PAUSE/RESUME/TERMINATE) requieren rol `AIOS_ADMIN`
- **Ver contenido decifrado:** Requiere rol `AIOS_ADMIN`
- **REST Client:** Añadir autenticación (JWT, API Key) según configuración del microservicio

---

## 🚀 Despliegue

1. Compilar el proyecto
2. Verificar que `codeflowx-agent-supervisor` esté ejecutándose (puerto 8087)
3. Configurar menú con nuevas entradas
4. Acceder a `/console/aios/agents/agent-list.zul`

---

## 📝 Notas

- **Datos de ejemplo:** Algunas gráficas y métricas usan datos de ejemplo hasta que se integre con TimescaleDB
- **Gráficas:** Requieren integración con ZK Charts (pendiente)
- **Telemetría:** La vista de telemetría está preparada pero requiere integración con `codeflowx-aios-telemetry`
- **Logs:** La vista de logs requiere integración con ImmutableLog

---

**Última actualización:** 2024-12-19
