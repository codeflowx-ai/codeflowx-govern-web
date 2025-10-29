# 📊 SCRIPTS DE POBLACIÓN SSO - CODEFLOWX GOVERN

**Fecha:** Octubre 29, 2025  
**Versión:** 1.0  
**Propósito:** Scripts SQL para poblar las tablas SSO (Single Sign-On) del sistema

---

## 📋 CONTENIDO

Este conjunto de scripts SQL pobla las tablas del sistema SSO con la estructura completa de navegación de CodeflowX Govern:

- **SSOAPLICACION**: 9 aplicaciones principales
- **SSOMENU**: 74 menús funcionales
- **SSOMENUITEM**: 265 pantallas/items de menú

---

## 🗂️ ESTRUCTURA DE ARCHIVOS

```
docs/scripts/
├── 00_MASTER_SSO_INSERTS.sql              # Script maestro (ejecuta todos)
├── 01_SSO_APPLICATIONS_MENUS.sql          # Applications y Menus (9 + 74)
├── 02_SSO_MENUITEMS_AGENTS.sql            # Menu Items Agents (52)
├── 03_SSO_MENUITEMS_PROVIDERS.sql         # Menu Items Providers (4)
├── 04_SSO_MENUITEMS_PROMPTS.sql           # Menu Items Prompts (16)
├── 05_SSO_MENUITEMS_RAG.sql               # Menu Items RAG (17)
├── 06_SSO_MENUITEMS_MODELS.sql            # Menu Items Models (30)
├── 07_SSO_MENUITEMS_SERVING.sql           # Menu Items Serving (26)
├── 08_SSO_MENUITEMS_CORE.sql              # Menu Items Core (29)
├── 09_SSO_MENUITEMS_GOVERNANCE.sql        # Menu Items Governance (47)
└── 10_SSO_MENUITEMS_PROJECTS.sql          # Menu Items Projects (44)
```

---

## 🚀 EJECUCIÓN

### **Opción 1: Script Maestro (Recomendado)**

Ejecutar todo de una vez usando el script maestro:

```bash
cd docs/scripts
psql -U postgres -d codeflowx_govern -f 00_MASTER_SSO_INSERTS.sql
```

### **Opción 2: Ejecución Manual por Partes**

Si prefieres ejecutar por partes o verificar cada paso:

```bash
cd docs/scripts

# Paso 1: Applications y Menus
psql -U postgres -d codeflowx_govern -f 01_SSO_APPLICATIONS_MENUS.sql

# Paso 2-10: Menu Items por módulo
psql -U postgres -d codeflowx_govern -f 02_SSO_MENUITEMS_AGENTS.sql
psql -U postgres -d codeflowx_govern -f 03_SSO_MENUITEMS_PROVIDERS.sql
psql -U postgres -d codeflowx_govern -f 04_SSO_MENUITEMS_PROMPTS.sql
psql -U postgres -d codeflowx_govern -f 05_SSO_MENUITEMS_RAG.sql
psql -U postgres -d codeflowx_govern -f 06_SSO_MENUITEMS_MODELS.sql
psql -U postgres -d codeflowx_govern -f 07_SSO_MENUITEMS_SERVING.sql
psql -U postgres -d codeflowx_govern -f 08_SSO_MENUITEMS_CORE.sql
psql -U postgres -d codeflowx_govern -f 09_SSO_MENUITEMS_GOVERNANCE.sql
psql -U postgres -d codeflowx_govern -f 10_SSO_MENUITEMS_PROJECTS.sql
```

---

## 📊 DETALLE DE MÓDULOS

| Script | Módulo | Menus | Items | Descripción |
|--------|--------|-------|-------|-------------|
| 02 | **Agents** | 11 | 52 | Gestión de agentes de IA |
| 03 | **Providers** | 2 | 4 | Proveedores de IA |
| 04 | **Prompts** | 6 | 16 | Gestión de prompts |
| 05 | **RAG** | 7 | 17 | Sistemas RAG |
| 06 | **Models** | 8 | 30 | Gestión de modelos |
| 07 | **Serving** | 8 | 26 | Serving de modelos |
| 08 | **Core** | 9 | 29 | Administración del sistema |
| 09 | **Governance** | 12 | 47 | Gobernanza de IA |
| 10 | **Projects** | 11 | 44 | Gestión de proyectos |
| **TOTAL** | **9 Apps** | **74** | **265** | |

---

## ⚙️ CARACTERÍSTICAS TÉCNICAS

### **IDs Autonuméricos**
- Los campos `IDXAPLICACION`, `IDXSSOMENU` e `IDXSSOMENUITEM` son SERIAL (autonuméricos)
- No se incluyen en los INSERT statements
- PostgreSQL los genera automáticamente

### **Referencias FK**
Las Foreign Keys se resuelven mediante subconsultas SELECT:

```sql
-- Ejemplo: Referencia a Application
(SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents')

-- Ejemplo: Referencia a Menu
(SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' 
  AND IDSSOAPLICACION0 = (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'))
```

### **Formato de Rutas**

Todos los campos de rutas comienzan con `platform/` o `gobierno/` (sin `/` inicial):

- **DASHBOARD** (en SSOAPLICACION): `platform/agents/monitoring/dashboard.zul`
- **DASHBOARDPAGE** (en SSOMENU): `platform/agents/overview/page.zul`
- **PAGE** (en SSOMENUITEM): `platform/agents/overview/page.zul`
- **URL** (en SSOMENUITEM): `/platform/agents/overview/page` (esta SÍ lleva `/` inicial)

---

## ✅ VERIFICACIÓN POST-EJECUCIÓN

Después de ejecutar los scripts, verifica la cantidad de registros:

```sql
-- Verificar Applications
SELECT COUNT(*) FROM SSOAPLICACION;  -- Debe devolver: 9

-- Verificar Menus
SELECT COUNT(*) FROM SSOMENU;        -- Debe devolver: 74

-- Verificar Menu Items
SELECT COUNT(*) FROM SSOMENUITEM;    -- Debe devolver: 265

-- Ver Applications insertadas
SELECT IDXAPLICACION, APLICACION, NAMESPACE, TITULO 
FROM SSOAPLICACION 
ORDER BY IDXAPLICACION;

-- Ver Menus por Application
SELECT a.APLICACION, COUNT(m.IDXSSOMENU) as total_menus
FROM SSOAPLICACION a
LEFT JOIN SSOMENU m ON m.IDSSOAPLICACION0 = a.IDXAPLICACION
GROUP BY a.APLICACION
ORDER BY a.APLICACION;

-- Ver Menu Items por Menu
SELECT a.APLICACION, m.MENU, COUNT(mi.IDXSSOMENUITEM) as total_items
FROM SSOAPLICACION a
JOIN SSOMENU m ON m.IDSSOAPLICACION0 = a.IDXAPLICACION
LEFT JOIN SSOMENUITEM mi ON mi.IDSSOMENU0 = m.IDXSSOMENU
GROUP BY a.APLICACION, m.MENU
ORDER BY a.APLICACION, m.MENU;
```

---

## 🔄 ROLLBACK

Si necesitas eliminar los datos insertados:

```sql
-- CUIDADO: Esto eliminará TODOS los datos SSO
-- Ejecutar en orden inverso por las FK

DELETE FROM SSOMENUITEM;
DELETE FROM SSOMENU;
DELETE FROM SSOAPLICACION;

-- Resetear secuencias (opcional)
ALTER SEQUENCE ssoaplicacion_idxaplicacion_seq RESTART WITH 1;
ALTER SEQUENCE ssomenu_idxssomenu_seq RESTART WITH 1;
ALTER SEQUENCE ssomenuitem_idxssomenuitem_seq RESTART WITH 1;
```

---

## 📚 DOCUMENTACIÓN RELACIONADA

- **ARQUITECTURA_NAVEGACION_SSO.md**: Documento maestro con toda la estructura SSO
- **cursor_read_context_for_generating_view.md**: Contexto de generación de vistas

---

## 🏆 RESUMEN DE MÓDULOS DOCUMENTADOS

### ✅ Módulos Completados (9/14+)

| Application | Menus | Items | Dashboard Principal |
|-------------|-------|-------|---------------------|
| **Agents** | 11 | 52 | `platform/agents/monitoring/dashboard.zul` |
| **Models** | 8 | 30 | `platform/models/overview/page.zul` |
| **Prompts** | 6 | 16 | `platform/prompts/overview/page.zul` |
| **RAG** | 7 | 17 | `platform/rag/overview/page.zul` |
| **Providers** | 2 | 4 | `platform/providers/providers-overview-overview.zul` |
| **Core** | 9 | 29 | `gobierno/core/admin-dashboard.zul` |
| **Governance** | 12 | 47 | `platform/governance/dashboard/overview.zul` |
| **Serving** | 8 | 26 | `platform/serving/deployment-status-overview.zul` |
| **Projects** | 11 | 44 | `platform/projects/project-portfolio-dashboard-overview.zul` |

---

## 🎯 PRÓXIMOS PASOS

1. ✅ Ejecutar scripts en PostgreSQL
2. ⏳ Verificar inserción correcta
3. ⏳ Configurar permisos por rol (tabla SSOROLESMENU)
4. ⏳ Documentar módulos pendientes (Training, Monitoring, Analytics, Playground, etc.)
5. ⏳ Generar scripts para módulos adicionales

---

## 📞 CONTACTO

**Proyecto:** CodeflowX Govern  
**Autor:** Equipo de Desarrollo  
**Última actualización:** Octubre 29, 2025  
**Estado:** Scripts completos para 9 módulos (64% del sistema)

