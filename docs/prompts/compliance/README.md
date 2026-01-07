# 📚 PROMPTS DE IMPLEMENTACIÓN - MÓDULO COMPLIANCE

**Fecha:** Diciembre 2025
**Versión:** 1.0
**Objetivo:** Prompts individuales para implementación en paralelo de pantallas de compliance

---

## 📋 ÍNDICE DE PROMPTS

### **Módulos Principales (11 módulos)**

| # | Módulo | Artículo EU AI Act | Prompt | Estado |
|---|--------|-------------------|--------|--------|
| 1 | **Prohibited Systems** | Art. 5 | `PROMPT_COMPLIANCE_PROHIBITED_SYSTEMS.md` | ✅ Creado |
| 2 | **Classification** | Art. 6 + Anexo III | `PROMPT_COMPLIANCE_CLASSIFICATION.md` | ✅ Creado |
| 3 | **FRIA** | Art. 27 + Anexo IX | `PROMPT_COMPLIANCE_FRIA.md` | ✅ Creado |
| 4 | **Conformity Assessment** | Art. 43 + Anexo VI | `PROMPT_COMPLIANCE_CONFORMITY.md` | ✅ Creado |
| 5 | **EU Registration** | Art. 49 + Anexo VIII | `PROMPT_COMPLIANCE_EU_REGISTRATION.md` | ✅ Creado |
| 6 | **Post-Market Monitoring** | Art. 20, 72 | `PROMPT_COMPLIANCE_PMM.md` | ✅ Creado |
| 7 | **Immutable Logs** | Art. 12, 19 | `PROMPT_COMPLIANCE_IMMUTABLE_LOGS.md` | ✅ Creado |
| 8 | **QMS** | Art. 17 | `PROMPT_COMPLIANCE_QMS.md` | ✅ Creado |
| 9 | **Technical Docs** | Art. 11 + Anexo IV | `PROMPT_COMPLIANCE_TECHNICAL_DOCS.md` | ✅ Creado |
| 10 | **HITL Supervision** | Art. 14 | `PROMPT_COMPLIANCE_HITL.md` | ✅ Creado |
| 11 | **Traceability** | Art. 12, 19 | `PROMPT_COMPLIANCE_TRACEABILITY.md` | ✅ Creado |

---

## 🎯 ESTRUCTURA DE CADA PROMPT

Cada prompt incluye:

### **1. Información General**
- Módulo y artículo EU AI Act
- Estado y esfuerzo estimado
- Resumen del módulo

### **2. Pantallas Requeridas**
- Tabla con todas las pantallas necesarias
- Estado de cada pantalla (✅ Existe / ❌ No existe)
- Prioridad (🔴 Alta / 🟡 Media / 🟢 Baja)

### **3. Arquitectura y Dependencias**
- **Pantallas ZUL Originales:** Ruta y ViewModel asociado
- **ViewModels Java:** Paquete, archivo, servicios usados, métodos principales
- **Entidades JPA:** Tabla, ubicación, campos principales con tipos
- **Business Services:** Ubicación, métodos con consultas BBDD
- **Servicios CRUD:** Ubicación y métodos disponibles
- **Lógica de Negocio Detallada:** Pseudocódigo y fórmulas

### **4. Funcionalidades por Pantalla**
- Descripción detallada de cada funcionalidad
- Validaciones requeridas
- Mock data completo
- API routes mock

### **5. Estilos "Wow Factor"**
- Estructura base con partículas flotantes
- Componentes UI disponibles
- Ejemplos de código

### **6. Traducciones**
- Estructura de traducciones requeridas
- Ejemplos en español/inglés

### **7. Checklist de Implementación**
- Tareas por pantalla
- Tareas generales
- Referencias a documentación

---

## 🚀 CÓMO USAR ESTOS PROMPTS

### **Para Agentes Independientes:**

1. **Seleccionar un módulo** de la lista
2. **Leer el prompt completo** del módulo
3. **Revisar las pantallas requeridas:**
   - Si existe (✅): Revisar y completar funcionalidades faltantes
   - Si no existe (❌): Crear nueva pantalla desde cero
4. **Implementar con datos mock:**
   - Usar mock data proporcionado en el prompt
   - Crear API routes mock
   - Configurar variable `NEXT_PUBLIC_USE_MOCK=true`
5. **Seguir checklist de implementación**
6. **Verificar estilos "Wow Factor"**

### **Para Coordinación:**

- Cada prompt es **independiente** y puede trabajarse en **paralelo**
- Los prompts incluyen **toda la información necesaria** (ViewModels, Entidades, Business Services, ZUL)
- Al finalizar, solo cambiar `NEXT_PUBLIC_USE_MOCK=false` para activar backend real

---

## 📖 REFERENCIAS PRINCIPALES

### **Documentación Base:**
- **Inventario Pantallas:** `docs/PANTALLAS_GOVERNANCE_COMPLIANCE_AI_ACT.md`
- **Lógica de Negocio:** `docs/prompts/BUSINESS_LOGIC_COMPLIANCE.md`
- **Prompts Migración:** `docs/prompts/MIGRACION_COMPLIANCE_*.md`

### **Código Fuente:**
- **ViewModels:** `suinsit.nova.web/src/main/java/com/codeflowx/govern/viewmodel/compliance/`
- **Entidades JPA:** `nocode.service.entitys/src/main/java/com/codeflowx/govern/entity/compliance/`
- **Business Services:** `codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/`
- **Servicios CRUD:** `codeflowx.govern.services/src/main/java/com/codeflowx/govern/service/compliance/`
- **Pantallas ZUL:** `src/main/webapp/console/gobierno/compliance/`

### **Pantallas Next.js:**
- **Ubicación:** `app/(app)/governance/compliance/`
- **Ejemplos:** Ver pantallas existentes para referencia de estilos

---

## ✅ CHECKLIST GENERAL

Antes de empezar a implementar:

- [ ] Leer el prompt completo del módulo
- [ ] Revisar ViewModels Java asociados
- [ ] Revisar Entidades JPA y sus campos
- [ ] Revisar Business Services y sus métodos
- [ ] Revisar pantallas ZUL originales (si existen)
- [ ] Identificar qué pantallas ya existen en Next.js
- [ ] Preparar mock data según especificaciones
- [ ] Configurar variable de entorno `NEXT_PUBLIC_USE_MOCK=true`

---

## 🔗 INTEGRACIÓN CON BACKEND

### **Cuando el Backend esté Disponible:**

1. **Desactivar Mock:**
   ```bash
   # .env.local
   NEXT_PUBLIC_USE_MOCK=false
   ```

2. **Actualizar API Routes:**
   - Reemplazar mock data con llamadas reales a backend
   - Usar servicios definidos en `app/(app)/governance/services/`

3. **Verificar Integración:**
   - Probar cada endpoint
   - Validar respuestas
   - Verificar manejo de errores

---

**Última actualización:** Diciembre 2025
**Estado:** ✅ **COMPLETADO** - 11/11 prompts creados y listos para implementación en paralelo
