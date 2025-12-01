# Developer Console - Guía de Uso

La Developer Console es la interfaz web para gestionar plugins, API keys y recursos de CodeflowX.

---

## 🚀 Acceso

**URL:** `https://console.codeflowx.ai`
**Autenticación:** Login con cuenta CodeflowX

---

## 📋 Pestañas

### Plugins
- **Registrar Plugin:** Sube archivo `.cfx-plugin`
- **Lista de Plugins:** Ver plugins registrados
- **Activar/Desactivar:** Gestionar estado
- **Logs:** Ver logs de ejecución

### API Keys
- **Generar Key:** Crear nueva API key
- **Lista de Keys:** Ver todas las keys
- **Revocar:** Eliminar key

### Logs
- **Filtrar por:** Plugin, fecha, nivel
- **Exportar:** Descargar logs

### Playground
- **Probar Hooks:** Ejecutar hooks manualmente
- **Test Endpoints:** Probar API
- **Simular Flujos:** Simular invocaciones

---

## 🔑 Generar API Key

1. Ve a pestaña **"API Keys"**
2. Haz clic en **"Generar API Key"**
3. Copia la key (solo se muestra una vez)
4. Usa la key en tu SDK

---

## 🔌 Registrar Plugin

1. Ve a pestaña **"Plugins"**
2. Haz clic en **"Registrar Plugin"**
3. Sube archivo `.cfx-plugin`
4. El sistema valida y registra automáticamente
5. Plugin aparece en lista

---

## 📊 Ver Logs

1. Ve a pestaña **"Logs"**
2. Filtra por plugin, fecha, nivel
3. Revisa logs de ejecución
4. Exporta si es necesario

---

## 🎮 Playground

1. Ve a pestaña **"Playground"**
2. Selecciona hook a probar
3. Ingresa payload
4. Ejecuta y ve resultado

---

**Más:** [Guía de Plugins](PLUGINS/GUIA_PLUGINS.md)
