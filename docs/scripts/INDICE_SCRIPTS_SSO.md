# 📑 ÍNDICE COMPLETO - SCRIPTS SSO

**Fecha:** Octubre 29, 2025  
**Versión:** 1.0  
**Total de Scripts:** 11 archivos SQL + 3 documentos MD

---

## 🗂️ ARCHIVOS GENERADOS

### 📘 DOCUMENTACIÓN

| Archivo | Descripción | Líneas |
|---------|-------------|--------|
| `README_SSO_SCRIPTS.md` | Guía principal de uso y ejecución | ~200 |
| `RESUMEN_EJECUCION_SSO.md` | Resumen ejecutivo y métricas | ~300 |
| `INDICE_SCRIPTS_SSO.md` | Este documento - Índice general | ~100 |

### 📜 SCRIPTS SQL

| # | Archivo | Registros | Descripción |
|---|---------|-----------|-------------|
| 00 | `00_MASTER_SSO_INSERTS.sql` | - | **Script maestro** - Ejecuta todos en orden |
| 01 | `01_SSO_APPLICATIONS_MENUS.sql` | 83 | 9 Applications + 74 Menus |
| 02 | `02_SSO_MENUITEMS_AGENTS.sql` | 52 | Menu Items - Agents |
| 03 | `03_SSO_MENUITEMS_PROVIDERS.sql` | 4 | Menu Items - Providers |
| 04 | `04_SSO_MENUITEMS_PROMPTS.sql` | 16 | Menu Items - Prompts |
| 05 | `05_SSO_MENUITEMS_RAG.sql` | 17 | Menu Items - RAG |
| 06 | `06_SSO_MENUITEMS_MODELS.sql` | 30 | Menu Items - Models |
| 07 | `07_SSO_MENUITEMS_SERVING.sql` | 26 | Menu Items - Serving |
| 08 | `08_SSO_MENUITEMS_CORE.sql` | 29 | Menu Items - Core |
| 09 | `09_SSO_MENUITEMS_GOVERNANCE.sql` | 47 | Menu Items - Governance |
| 10 | `10_SSO_MENUITEMS_PROJECTS.sql` | 44 | Menu Items - Projects |

**TOTAL:** 11 scripts SQL | 348 registros

---

## 📊 DISTRIBUCIÓN DE REGISTROS

### Por Tabla

| Tabla | Registros | Descripción |
|-------|-----------|-------------|
| `SSOAPLICACION` | 9 | Applications principales |
| `SSOMENU` | 74 | Menús funcionales |
| `SSOMENUITEM` | 265 | Pantallas/Items de menú |
| **TOTAL** | **348** | |

### Por Módulo

| Módulo | Menus | Items | Total |
|--------|-------|-------|-------|
| Agents | 11 | 52 | 63 |
| Models | 8 | 30 | 38 |
| Prompts | 6 | 16 | 22 |
| RAG | 7 | 17 | 24 |
| Providers | 2 | 4 | 6 |
| Core | 9 | 29 | 38 |
| Governance | 12 | 47 | 59 |
| Serving | 8 | 26 | 34 |
| Projects | 11 | 44 | 55 |
| **TOTAL** | **74** | **265** | **339** |

---

## 🚀 EJECUCIÓN RÁPIDA

### Comando único para ejecutar todo:

```bash
cd /mnt/c/Users/ManuelGonzalez/git/suinsit.nova.web/docs/scripts
psql -U postgres -d codeflowx_govern -f 00_MASTER_SSO_INSERTS.sql
```

### Verificación post-ejecución:

```bash
psql -U postgres -d codeflowx_govern -c "SELECT COUNT(*) FROM SSOAPLICACION;"    # Debe: 9
psql -U postgres -d codeflowx_govern -c "SELECT COUNT(*) FROM SSOMENU;"          # Debe: 74
psql -U postgres -d codeflowx_govern -c "SELECT COUNT(*) FROM SSOMENUITEM;"      # Debe: 265
```

---

## 📐 ESTRUCTURA TÉCNICA

### Campos en SSOAPLICACION

```sql
APLICACION    VARCHAR(100)  NOT NULL  -- Nombre de la aplicación
DASHBOARD     VARCHAR(100)            -- Ruta al dashboard principal (sin / inicial)
ICONCLASS     VARCHAR(100)            -- Clase de icono Font Awesome
NAMESPACE     VARCHAR(100)            -- platform o gobierno
TITULO        VARCHAR(100)            -- Título descriptivo
```

**Ejemplo:**
```sql
('Agents', 'platform/agents/monitoring/dashboard.zul', 'fa-robot', 'platform', 'Gestión de Agentes de IA')
```

### Campos en SSOMENU

```sql
MENU              VARCHAR(100)  NOT NULL  -- Nombre del menú
DASHBOARDPAGE     VARCHAR(100)            -- Ruta al dashboard del menú (sin / inicial)
ICONO             VARCHAR(100)            -- Clase de icono Font Awesome
DESCRIPCION       VARCHAR(100)            -- Descripción del menú
TITLE             VARCHAR(100)            -- Título corto
NAMESPACE         VARCHAR(100)            -- platform o gobierno
IDSSOAPLICACION0  LONG                    -- FK a SSOAPLICACION
```

**Ejemplo:**
```sql
('Gestión de Agentes', 'platform/agents/overview/page.zul', 'fa-list-check', 
 'Gestión CRUD de agentes y sus dominios', 'Gestión de Agentes', 'platform', 
 (SELECT IDXAPLICACION FROM SSOAPLICACION WHERE APLICACION = 'Agents'))
```

### Campos en SSOMENUITEM

```sql
ITEM        VARCHAR(100)  NOT NULL  -- Nombre del menu item
PAGE        VARCHAR(100)            -- Ruta al archivo ZUL (sin / inicial)
URL         VARCHAR(100)            -- Ruta de navegación (CON / inicial)
DASHBOARD   BOOLEAN                 -- true si es dashboard principal
NAMESPACE   VARCHAR(100)            -- platform o gobierno
IDSSOMENU0  LONG                    -- FK a SSOMENU
```

**Ejemplo:**
```sql
('Listado de Agentes', 'platform/agents/overview/page.zul', 
 '/platform/agents/overview/page', false, 'platform',
 (SELECT IDXSSOMENU FROM SSOMENU WHERE MENU = 'Gestión de Agentes' ...))
```

---

## 🎨 CONVENCIONES DE NOMENCLATURA

### Rutas en PAGE/DASHBOARD/DASHBOARDPAGE
- ✅ Correcto: `platform/agents/overview/page.zul`
- ❌ Incorrecto: `/platform/agents/overview/page.zul`
- ❌ Incorrecto: `console/platform/agents/overview/page.zul`

### Rutas en URL
- ✅ Correcto: `/platform/agents/overview/page`
- ❌ Incorrecto: `platform/agents/overview/page`

### Nombres de Menu Items
- Pattern Overview: `Listado de XXXX`
- Pattern Detail: `Gestionar XXXX`, `Detalle de XXXX`, `Nueva/Editar XXXX`
- Pattern Config: `Configurar XXXX`
- Pattern Dashboard: `Dashboard de XXXX`, `Dashboard XXXX`

---

## 📊 ESTADÍSTICAS

```
Total de archivos generados:     14
Total de scripts SQL:            11
Total de documentos MD:           3
Total de registros a insertar:  348
Total de líneas de código SQL: ~3,500

Tiempo estimado de ejecución:   < 5 segundos
Tamaño aproximado total:        ~250 KB
```

---

## 🔗 DEPENDENCIAS DE EJECUCIÓN

```
01_SSO_APPLICATIONS_MENUS.sql
    ├── Inserta SSOAPLICACION (9 registros)
    └── Inserta SSOMENU (74 registros)
         ↓
02_SSO_MENUITEMS_AGENTS.sql → Depende de SSOMENU
03_SSO_MENUITEMS_PROVIDERS.sql → Depende de SSOMENU
04_SSO_MENUITEMS_PROMPTS.sql → Depende de SSOMENU
05_SSO_MENUITEMS_RAG.sql → Depende de SSOMENU
06_SSO_MENUITEMS_MODELS.sql → Depende de SSOMENU
07_SSO_MENUITEMS_SERVING.sql → Depende de SSOMENU
08_SSO_MENUITEMS_CORE.sql → Depende de SSOMENU
09_SSO_MENUITEMS_GOVERNANCE.sql → Depende de SSOMENU
10_SSO_MENUITEMS_PROJECTS.sql → Depende de SSOMENU
```

**⚠️ IMPORTANTE:** Ejecutar siempre en orden secuencial (01 → 02 → ... → 10)

---

## 🎯 PRÓXIMOS PASOS

### Inmediatos
1. ✅ Scripts generados
2. ⏳ Ejecutar scripts en PostgreSQL
3. ⏳ Verificar conteos de registros
4. ⏳ Probar navegación en la aplicación

### Futuros
1. ⏳ Generar scripts para módulos pendientes (Training, Monitoring, Analytics, Playground)
2. ⏳ Configurar tabla SSOROLESMENU (permisos por rol)
3. ⏳ Agregar iconos a menu items individuales
4. ⏳ Documentar procesos BPMN asociados

---

## 📚 REFERENCIAS

- **Documento fuente:** `docs/funcional/ARQUITECTURA_NAVEGACION_SSO.md`
- **Conversación origen:** `docs/cursor_read_context_for_generating_view.md`
- **Entidades JPA:** `application-source/data/sources/org/suinsit/apps/admin/`
  - `Aplicacion.java`
  - `Ssomenu.java`
  - `Ssomenuitem.java`

---

**Estado:** ✅ SCRIPTS COMPLETOS Y LISTOS PARA EJECUCIÓN  
**Última actualización:** Octubre 29, 2025  
**Autor:** CodeflowX Development Team

