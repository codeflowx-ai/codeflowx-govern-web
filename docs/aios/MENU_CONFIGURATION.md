# Configuración de Menú para AI OS

**Fecha:** 2024-12-19

---

## 📋 Entradas de Menú Requeridas

Añadir las siguientes 3 entradas al menú de la aplicación (archivo `root.json` o configuración de menú):

### 1. Agentes AI OS

```json
{
  "id": "aios-agents-list",
  "name": "Agentes AI OS",
  "iconClass": "fa-robot",
  "pageUrl": "/console/aios/agents/agent-list.zul",
  "roles": ["AIOS_ADMIN", "AIOS_OPERATOR"],
  "items": []
}
```

### 2. Dashboard Runtime

```json
{
  "id": "aios-dashboard",
  "name": "Dashboard Runtime",
  "iconClass": "fa-chart-line",
  "pageUrl": "/console/aios/dashboard/ai-runtime-dashboard.zul",
  "roles": ["AIOS_ADMIN", "AIOS_OPERATOR", "AIOS_CONSUMER"],
  "items": []
}
```

### 3. Monitor Supervisor

```json
{
  "id": "aios-supervisor-monitor",
  "name": "Monitor Supervisor",
  "iconClass": "fa-shield-halved",
  "pageUrl": "/console/aios/agents/supervisor-monitor.zul",
  "roles": ["AIOS_ADMIN"],
  "items": []
}
```

---

## 🔧 Ubicación del Archivo

El archivo `root.json` se encuentra en:
- `application-source/apps/[app-name]/webapp/menu/root.json`

O según la variable de entorno `SUINSIT_DEPLOY`:
- `${SUINSIT_DEPLOY}/[app-name]/menu/root.json`

---

## 📝 Instrucciones

1. Abrir el archivo `root.json` de la aplicación correspondiente
2. Añadir las 3 entradas en el array `items` del objeto raíz
3. Asegurar que los roles estén configurados correctamente
4. Reiniciar la aplicación para que los cambios surtan efecto

---

## 🎯 Estructura Recomendada

Si se desea agrupar bajo un menú padre "AI OS":

```json
{
  "id": "aios-menu",
  "name": "AI OS",
  "iconClass": "fa-robot",
  "items": [
    {
      "id": "aios-agents-list",
      "name": "Agentes",
      "pageUrl": "/console/aios/agents/agent-list.zul",
      "iconClass": "fa-robot"
    },
    {
      "id": "aios-dashboard",
      "name": "Dashboard Runtime",
      "pageUrl": "/console/aios/dashboard/ai-runtime-dashboard.zul",
      "iconClass": "fa-chart-line"
    },
    {
      "id": "aios-supervisor-monitor",
      "name": "Monitor Supervisor",
      "pageUrl": "/console/aios/agents/supervisor-monitor.zul",
      "iconClass": "fa-shield-halved"
    }
  ]
}
```

---

**Nota:** Los roles deben existir en el sistema antes de asignarlos al menú.
