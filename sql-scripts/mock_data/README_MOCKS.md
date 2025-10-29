# 📊 Datos Mock - CodeFlowX Govern Demo

**Generado:** 2025-10-14 13:05:50  
**Propósito:** Datos de demostración con lógica de negocio coherente

---

## 🎯 Descripción

Estos scripts generan datos mock realistas para la versión demo del sistema.

**Características:**
- ✅ Datos coherentes con lógica de negocio
- ✅ Relaciones entre tablas respetadas
- ✅ Foreign Keys válidas
- ✅ Convención EnArt (UPPERCASE)
- ✅ Casos de uso realistas

---

## 🚀 Ejecución Rápida

```bash
# Método 1: Script maestro (recomendado)
./ejecutar_todos_mocks.sh

# Método 2: Manual
psql -d codeflowx_govern -f 01_core_mock_data.sql
psql -d codeflowx_govern -f 02_projects_mock_data.sql
psql -d codeflowx_govern -f 03_prompts_mock_data.sql
# ... etc
```

---

## 📋 Módulos Generados

1. **CORE** - 26 statements
2. **PROJECTS** - 8 statements
3. **PROMPTS** - 11 statements
4. **AGENTS** - 11 statements
5. **MODELS** - 9 statements
6. **TRAINING** - 25 statements

---

## 📊 Datos Generados

- **Usuarios:** 10
- **Proyectos:** 5
- **Agentes:** 8
- **Modelos:** 6
- **Experimentos:** 15
- **Runs:** 45 (aprox.)
- **Prompts:** 12

---

## ⚠️ IMPORTANTE

- Estos datos son para **DEMO/DESARROLLO** únicamente
- **NO usar en producción**
- Ejecutar **después** de crear las tablas (01_create_tables.sql)

---

**Generado por:** CodeFlowX NoCode Framework v1.1.0
