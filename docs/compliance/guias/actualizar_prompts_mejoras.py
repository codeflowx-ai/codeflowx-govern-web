#!/usr/bin/env python3
"""
Script para actualizar prompts de agentes enfocándolos solo en mejoras necesarias
basado en datos reales de auditoría
"""
from pathlib import Path
import re

# Datos reales de auditoría y mejoras necesarias
MEJORAS_NECESARIAS = {
    "01-guia-introductoria-al-reglamento-de-ia": {
        "cobertura_actual": 0.50,
        "mejoras": [
            "Ampliar documentación introductoria para usuarios",
            "Crear guías de usuario más detalladas",
            "Mejorar ejemplos prácticos de clasificación"
        ],
        "prioridad": "Baja"
    },
    "02-guia-practica-y-ejemplos-para-entender-el-reglamento-de-ia": {
        "cobertura_actual": 0.50,
        "mejoras": [
            "Crear más casos de uso prácticos",
            "Ampliar ejemplos por sector",
            "Mejorar documentación de ejemplos"
        ],
        "prioridad": "Baja"
    },
    "03-guia-evaluacion-de-conformidad": {
        "cobertura_actual": 0.90,
        "mejoras": [
            "Completar integración con declaración UE de conformidad",
            "Validar cumplimiento completo de Anexos VI y VII",
            "Mejorar reportes de evaluación"
        ],
        "prioridad": "Media"
    },
    "04-guia-del-sistema-de-gestion-de-la-calidad": {
        "cobertura_actual": 0.95,
        "mejoras": [
            "Validar que los 13 módulos QMS estén al 100%",
            "Completar documentación de procesos QMS",
            "Mejorar métricas de seguimiento"
        ],
        "prioridad": "Baja"
    },
    "05-guia-de-gestion-de-riesgos": {
        "cobertura_actual": 0.90,
        "mejoras": [
            "Completar integración FRIA al 100%",
            "Mejorar seguimiento de riesgos residuales",
            "Ampliar métricas de mitigación"
        ],
        "prioridad": "Media"
    },
    "06-guia-vigilancia-humana": {
        "cobertura_actual": 1.00,
        "mejoras": [
            "Validar que todos los workflows BPMN tengan HITL",
            "Mejorar métricas de supervisión humana",
            "Documentar casos de uso de HITL"
        ],
        "prioridad": "Baja"
    },
    "07-guia-de-datos-y-gobernanza-de-datos": {
        "cobertura_actual": 0.90,
        "mejoras": [
            "Completar módulo específico de gobernanza de datos (Art. 10)",
            "Mejorar gestión de calidad de datos de entrenamiento",
            "Ampliar validaciones de datasets"
        ],
        "prioridad": "Media"
    },
    "08-guia-transparencia": {
        "cobertura_actual": 0.75,
        "mejoras": [
            "Ampliar sección de transparencia en documentación técnica",
            "Implementar información para usuarios según Art. 13",
            "Mejorar claridad y accesibilidad de documentación"
        ],
        "prioridad": "Media"
    },
    "09-guia-de-precision": {
        "cobertura_actual": 0.80,
        "mejoras": [
            "Ampliar métricas específicas de precisión en PMM",
            "Implementar validaciones de precisión continuas",
            "Crear dashboard específico de métricas de precisión"
        ],
        "prioridad": "Media"
    },
    "10-guia-solidez": {
        "cobertura_actual": 0.80,
        "mejoras": [
            "Ampliar cobertura de tests adversariales (80% → 95%)",
            "Implementar validaciones de robustez en QMS",
            "Mejorar métricas de solidez del sistema"
        ],
        "prioridad": "Media"
    },
    "11-guia-ciberseguridad": {
        "cobertura_actual": 0.90,
        "mejoras": [
            "Completar hardening específico de seguridad",
            "Ampliar controles de seguridad según Art. 15",
            "Mejorar documentación de controles de seguridad"
        ],
        "prioridad": "Media"
    },
    "12-guia-de-registros": {
        "cobertura_actual": 1.00,
        "mejoras": [
            "Validar sincronización completa con base de datos EU",
            "Mejorar reportes de registro",
            "Documentar procesos de registro"
        ],
        "prioridad": "Baja"
    },
    "13-guia-vigilancia-poscomercializacion": {
        "cobertura_actual": 0.85,
        "mejoras": [
            "Formalizar documentación completa de PMM",
            "Completar integración con todos los microservicios",
            "Mejorar generación automática de informes"
        ],
        "prioridad": "Media"
    },
    "14-guia-gestion-de-incidentes": {
        "cobertura_actual": 0.85,
        "mejoras": [
            "Crear flujo específico de gestión de incidentes",
            "Mejorar workflow completo: reporte, investigación, resolución",
            "Ampliar dashboard de incidentes"
        ],
        "prioridad": "Alta"
    },
    "15-guia-documentacion-tecnica": {
        "cobertura_actual": 0.70,
        "mejoras": [
            "Completar estructura Anexo IV completa",
            "Mejorar generación automática de documentación",
            "Validar que toda documentación requerida esté disponible",
            "Implementar validación de completitud de documentación"
        ],
        "prioridad": "Alta"
    },
    "16-manual-de-checklist-de-guias-de-requisitos": {
        "cobertura_actual": 0.90,
        "mejoras": [
            "Implementar checklist interactivo completo",
            "Validar que cubra todos los requisitos del Reglamento",
            "Mejorar reporte de cumplimiento automático"
        ],
        "prioridad": "Baja"
    }
}

def generar_prompt_mejoras(guia_id, info_guia, mejoras_info):
    """Genera prompt enfocado solo en mejoras"""

    nivel_cobertura = "🟢 Excelente" if mejoras_info["cobertura_actual"] >= 0.80 else \
                      "🟡 Buena" if mejoras_info["cobertura_actual"] >= 0.60 else \
                      "🟠 Regular" if mejoras_info["cobertura_actual"] >= 0.40 else \
                      "🔴 Insuficiente"

    # Determinar si necesita mejoras significativas
    necesita_mejoras_significativas = mejoras_info["cobertura_actual"] < 0.80 or mejoras_info["prioridad"] in ["Alta", "Media"]

    prompt = f"""# 🔧 PROMPT PARA AGENTE - MEJORAS: {info_guia['titulo']}

**Guía:** {info_guia['titulo']}
**Archivo Guía:** `{guia_id}.pdf`
**Texto Extraído:** `textos_extraidos/{guia_id}.txt`
**Fecha:** Diciembre 2025
**Prioridad de Mejoras:** {mejoras_info['prioridad']}
**Cobertura Actual:** {mejoras_info['cobertura_actual']*100:.0f}% ({nivel_cobertura})

---

## 📋 CONTEXTO

### **Estado Actual**
La plataforma tiene una cobertura del **{mejoras_info['cobertura_actual']*100:.0f}%** para esta guía según los documentos de auditoría real.

### **Artículos del Reglamento Relacionados**
{chr(10).join(f"- {art}" for art in info_guia['articulos'])}

### **Módulos de la Plataforma Relacionados**
{chr(10).join(f"- **{mod}**" for mod in info_guia['modulos_plataforma'])}

### **Objetivo de Mejora**
{'🚨 **MEJORAS PRIORITARIAS REQUERIDAS**' if necesita_mejoras_significativas else '✅ **OPTIMIZACIÓN Y VALIDACIÓN**'}

{'Esta guía requiere mejoras significativas para alcanzar el objetivo de 90%+ de cobertura.' if necesita_mejoras_significativas else 'Esta guía está bien implementada, se requiere validación y optimización.'}

---

## 🎯 MEJORAS ESPECÍFICAS A IMPLEMENTAR

### **Mejoras Identificadas**
"""

    for i, mejora in enumerate(mejoras_info['mejoras'], 1):
        prompt += f"{i}. **{mejora}**\n"

    prompt += f"""
---

## 📚 INFORMACIÓN DE LA GUÍA

### **Ubicación de la Guía**
- **PDF Original:** `docs/compliance/guias/{guia_id}.pdf`
- **Texto Extraído:** `docs/compliance/guias/textos_extraidos/{guia_id}.txt`
- **Análisis Completo:** Ver `analisis_guias.txt` para detalles específicos

### **Referencias de Auditoría**
- **Auditoría Real:** Ver documentos en `docs/compliance/auditoria/`
- **Reporte Actualizado:** `REPORTE_CUMPLIMIENTO_GUIAS_ACTUALIZADO.md`
- **Estado Implementación:** Verificar en documentos de auditoría específicos

---

## 🔍 ANÁLISIS DE MEJORAS

### **Revisión de Requisitos**
El agente debe:

1. **Revisar el texto completo de la guía** en `textos_extraidos/{guia_id}.txt`
2. **Identificar requisitos específicos** que no están completamente cubiertos
3. **Comparar con implementación actual** según documentos de auditoría
4. **Documentar gaps específicos** para cada mejora identificada
5. **Proponer solución técnica** para cada mejora

### **Validación de Implementación Actual**
- Revisar documentos de auditoría relacionados
- Verificar estado actual de módulos implementados
- Identificar qué funcionalidades ya existen
- Documentar qué falta para completar

---

## 🏗️ ARQUITECTURA Y MÓDULOS

### **Módulos Relacionados en la Plataforma**
"""

    for modulo in info_guia['modulos_plataforma']:
        prompt += f"""
#### **{modulo}**
- **Ubicación Documentación:** `codeflowx-studio/docs/prompts/compliance/PROMPT_COMPLIANCE_*.md`
- **Estado según Auditoría:** Verificar en documentos de auditoría
- **Mejoras Necesarias:** Ver lista de mejoras específicas arriba
"""

    prompt += f"""
---

## ✅ CHECKLIST DE MEJORAS

### **Tareas de Mejora**
"""

    for i, mejora in enumerate(mejoras_info['mejoras'], 1):
        prompt += f"- [ ] **Mejora {i}:** {mejora}\n"
        prompt += f"  - [ ] Analizar requisito específico\n"
        prompt += f"  - [ ] Diseñar solución técnica\n"
        prompt += f"  - [ ] Implementar mejora\n"
        prompt += f"  - [ ] Validar funcionamiento\n"
        prompt += f"  - [ ] Documentar cambios\n\n"

    prompt += f"""
### **Validación Final**
- [ ] Todas las mejoras implementadas
- [ ] Cobertura mejorada al objetivo (90%+)
- [ ] Documentación actualizada
- [ ] Tests de validación pasados
- [ ] Reporte de cumplimiento actualizado

---

## 📊 MÉTRICAS DE ÉXITO

### **Criterios de Completitud**
- ✅ Todas las mejoras identificadas implementadas
- ✅ Cobertura mejorada: {mejoras_info['cobertura_actual']*100:.0f}% → **90%+**
- ✅ Documentación actualizada
- ✅ Validación de funcionamiento completada
- ✅ Reporte de auditoría actualizado

### **Entregables Esperados**
1. **Implementación de Mejoras**
   - Código implementado para cada mejora
   - Tests de validación
   - Documentación técnica actualizada

2. **Reporte de Mejoras**
   - Lista de mejoras implementadas
   - Métricas de cobertura antes/después
   - Validación de cumplimiento

3. **Documentación Actualizada**
   - Documentación técnica actualizada
   - Guías de usuario si aplica
   - Reporte de cumplimiento actualizado

---

## 🔗 REFERENCIAS Y RECURSOS

### **Documentación de la Plataforma**
- **Módulos Compliance:** `codeflowx-studio/docs/prompts/compliance/`
- **Auditorías:** `docs/compliance/auditoria/`
- **Arquitectura Backend:** Ver documentación específica de cada módulo

### **Guías del Reglamento**
- **Guía Original:** `docs/compliance/guias/{guia_id}.pdf`
- **Texto Extraído:** `docs/compliance/guias/textos_extraidos/{guia_id}.txt`
- **Análisis:** `docs/compliance/guias/analisis_guias.txt`

### **Herramientas de Análisis**
- **Reporte Actualizado:** `REPORTE_CUMPLIMIENTO_GUIAS_ACTUALIZADO.md`
- **Script Comparativa:** `comparativa_cumplimiento.py`

---

## 💡 NOTAS IMPORTANTES

1. **Prioridad:** Esta guía tiene prioridad de mejoras **{mejoras_info['prioridad']}**
2. **Cobertura Actual:** {mejoras_info['cobertura_actual']*100:.0f}% - {'Requiere mejoras significativas' if necesita_mejoras_significativas else 'Bien implementada, optimización necesaria'}
3. **Objetivo:** Alcanzar **90%+ de cobertura** mediante las mejoras identificadas
4. **Enfoque:** {'Implementar mejoras prioritarias para cumplimiento completo' if necesita_mejoras_significativas else 'Validar y optimizar implementación existente'}

---

**Última actualización:** Diciembre 2025
**Estado:** ⏳ Pendiente de mejoras por agente
**Objetivo de Cobertura:** 90%+
"""

    return prompt

def main():
    """Actualiza todos los prompts enfocándolos en mejoras"""

    guias_dir = Path(__file__).parent
    prompts_dir = guias_dir / "prompts_agentes"
    prompts_mejoras_dir = guias_dir / "prompts_agentes_mejoras"
    prompts_mejoras_dir.mkdir(exist_ok=True)

    # Información de guías (del script anterior)
    GUIAS_INFO = {
        "01-guia-introductoria-al-reglamento-de-ia": {
            "titulo": "Guía Introductoria al Reglamento de IA",
            "articulos": ["Art. 1-4 (General)", "Art. 5 (Prohibidos)", "Art. 6 (Clasificación)"],
            "modulos_plataforma": ["Classification", "Prohibited Systems"]
        },
        "02-guia-practica-y-ejemplos-para-entender-el-reglamento-de-ia": {
            "titulo": "Guía Práctica y Ejemplos para Entender el Reglamento de IA",
            "articulos": ["Art. 1-4 (General)", "Art. 5 (Prohibidos)", "Art. 6 (Clasificación)"],
            "modulos_plataforma": ["Classification", "Prohibited Systems"]
        },
        "03-guia-evaluacion-de-conformidad": {
            "titulo": "Guía de Evaluación de Conformidad",
            "articulos": ["Art. 43", "Anexo VI", "Anexo VII"],
            "modulos_plataforma": ["Conformity Assessment", "QMS", "Technical Docs"]
        },
        "04-guia-del-sistema-de-gestion-de-la-calidad": {
            "titulo": "Guía del Sistema de Gestión de la Calidad",
            "articulos": ["Art. 17"],
            "modulos_plataforma": ["QMS"]
        },
        "05-guia-de-gestion-de-riesgos": {
            "titulo": "Guía de Gestión de Riesgos",
            "articulos": ["Art. 9", "Art. 27 (FRIA)"],
            "modulos_plataforma": ["FRIA", "QMS"]
        },
        "06-guia-vigilancia-humana": {
            "titulo": "Guía de Vigilancia Humana",
            "articulos": ["Art. 14"],
            "modulos_plataforma": ["HITL Supervision"]
        },
        "07-guia-de-datos-y-gobernanza-de-datos": {
            "titulo": "Guía de Datos y Gobernanza de Datos",
            "articulos": ["Art. 10"],
            "modulos_plataforma": ["Traceability", "Immutable Logs"]
        },
        "08-guia-transparencia": {
            "titulo": "Guía de Transparencia",
            "articulos": ["Art. 13"],
            "modulos_plataforma": ["Technical Docs", "Traceability"]
        },
        "09-guia-de-precision": {
            "titulo": "Guía de Precisión",
            "articulos": ["Art. 15 (parcial)"],
            "modulos_plataforma": ["Post-Market Monitoring", "Technical Docs"]
        },
        "10-guia-solidez": {
            "titulo": "Guía de Solidez",
            "articulos": ["Art. 15 (parcial)"],
            "modulos_plataforma": ["Post-Market Monitoring", "QMS"]
        },
        "11-guia-ciberseguridad": {
            "titulo": "Guía de Ciberseguridad",
            "articulos": ["Art. 15"],
            "modulos_plataforma": ["Immutable Logs", "Traceability"]
        },
        "12-guia-de-registros": {
            "titulo": "Guía de Registros",
            "articulos": ["Art. 49", "Anexo VIII"],
            "modulos_plataforma": ["EU Registration", "Traceability", "Immutable Logs"]
        },
        "13-guia-vigilancia-poscomercializacion": {
            "titulo": "Guía de Vigilancia Poscomercialización",
            "articulos": ["Art. 20", "Art. 72"],
            "modulos_plataforma": ["Post-Market Monitoring"]
        },
        "14-guia-gestion-de-incidentes": {
            "titulo": "Guía de Gestión de Incidentes",
            "articulos": ["Art. 20", "Art. 72"],
            "modulos_plataforma": ["Post-Market Monitoring"]
        },
        "15-guia-documentacion-tecnica": {
            "titulo": "Guía de Documentación Técnica",
            "articulos": ["Art. 11", "Anexo IV"],
            "modulos_plataforma": ["Technical Docs"]
        },
        "16-manual-de-checklist-de-guias-de-requisitos": {
            "titulo": "Manual de Checklist de Guías de Requisitos",
            "articulos": ["Múltiples (checklist general)"],
            "modulos_plataforma": ["Conformity Assessment", "QMS"]
        }
    }

    print("="*80)
    print("ACTUALIZACIÓN DE PROMPTS - ENFOQUE EN MEJORAS")
    print("="*80)

    prompts_actualizados = []

    for guia_id, info_guia in GUIAS_INFO.items():
        if guia_id not in MEJORAS_NECESARIAS:
            continue

        mejoras_info = MEJORAS_NECESARIAS[guia_id]

        print(f"\nProcesando: {info_guia['titulo']}")
        print(f"  Cobertura actual: {mejoras_info['cobertura_actual']*100:.0f}%")
        print(f"  Prioridad: {mejoras_info['prioridad']}")
        print(f"  Mejoras: {len(mejoras_info['mejoras'])}")

        # Generar prompt enfocado en mejoras
        prompt = generar_prompt_mejoras(guia_id, info_guia, mejoras_info)

        # Guardar prompt
        prompt_file = prompts_mejoras_dir / f"PROMPT_MEJORAS_{guia_id.upper().replace('-', '_')}.md"
        with open(prompt_file, 'w', encoding='utf-8') as f:
            f.write(prompt)

        prompts_actualizados.append({
            "guia": info_guia['titulo'],
            "archivo": prompt_file.name,
            "cobertura": mejoras_info['cobertura_actual'],
            "prioridad": mejoras_info['prioridad'],
            "mejoras": len(mejoras_info['mejoras'])
        })

        print(f"  ✅ Prompt generado: {prompt_file.name}")

    # Generar índice
    indice_file = prompts_mejoras_dir / "README_PROMPTS_MEJORAS.md"
    with open(indice_file, 'w', encoding='utf-8') as f:
        f.write("# 🔧 ÍNDICE DE PROMPTS DE MEJORAS - GUIAS REGLAMENTO DE IA\n\n")
        f.write("**Fecha:** Diciembre 2025\n")
        f.write("**Total de Prompts:** {}\n".format(len(prompts_actualizados)))
        f.write("**Enfoque:** Solo mejoras necesarias basadas en auditoría real\n\n")
        f.write("---\n\n")
        f.write("## 📋 LISTA DE PROMPTS DE MEJORAS\n\n")
        f.write("| # | Guía | Archivo | Cobertura | Prioridad | Mejoras |\n")
        f.write("|---|------|---------|-----------|-----------|----------|\n")

        # Ordenar por prioridad (Alta, Media, Baja)
        orden_prioridad = {"Alta": 1, "Media": 2, "Baja": 3}
        prompts_ordenados = sorted(prompts_actualizados, key=lambda x: (orden_prioridad.get(x['prioridad'], 4), -x['cobertura']))

        for i, prompt_info in enumerate(prompts_ordenados, 1):
            nivel = "🟢" if prompt_info['cobertura'] >= 0.80 else \
                   "🟡" if prompt_info['cobertura'] >= 0.60 else \
                   "🟠" if prompt_info['cobertura'] >= 0.40 else \
                   "🔴"
            prioridad_emoji = "🔴" if prompt_info['prioridad'] == "Alta" else \
                             "🟡" if prompt_info['prioridad'] == "Media" else \
                             "🟢"
            f.write(f"| {i} | {prompt_info['guia']} | `{prompt_info['archivo']}` | {nivel} {prompt_info['cobertura']*100:.0f}% | {prioridad_emoji} {prompt_info['prioridad']} | {prompt_info['mejoras']} |\n")

        f.write("\n---\n\n")
        f.write("## 🎯 CÓMO USAR ESTOS PROMPTS\n\n")
        f.write("1. **Seleccionar una guía** de la lista (priorizar Alta y Media)\n")
        f.write("2. **Leer el prompt completo** del archivo correspondiente\n")
        f.write("3. **Revisar las mejoras específicas** identificadas\n")
        f.write("4. **Implementar mejoras** una por una\n")
        f.write("5. **Validar cobertura mejorada** (objetivo 90%+)\n")
        f.write("6. **Actualizar reporte de cumplimiento** con resultados\n\n")
        f.write("---\n\n")
        f.write("## 📊 ESTADÍSTICAS\n\n")

        altas = sum(1 for p in prompts_actualizados if p['prioridad'] == "Alta")
        medias = sum(1 for p in prompts_actualizados if p['prioridad'] == "Media")
        bajas = sum(1 for p in prompts_actualizados if p['prioridad'] == "Baja")

        f.write(f"- 🔴 Prioridad Alta: {altas} guías\n")
        f.write(f"- 🟡 Prioridad Media: {medias} guías\n")
        f.write(f"- 🟢 Prioridad Baja: {bajas} guías\n\n")

        total_mejoras = sum(p['mejoras'] for p in prompts_actualizados)
        f.write(f"- **Total de mejoras identificadas:** {total_mejoras}\n")

    print(f"\n✅ Índice generado: {indice_file.name}")
    print(f"\n{'='*80}")
    print(f"✅ Proceso completado. {len(prompts_actualizados)} prompts de mejoras generados")
    print(f"📁 Ubicación: {prompts_mejoras_dir}")
    print(f"{'='*80}")

if __name__ == "__main__":
    main()
