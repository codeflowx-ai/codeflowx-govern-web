#!/usr/bin/env python3
"""
Script para generar prompts de agentes para cada guía del Reglamento de IA
"""
from pathlib import Path
import re

# Información de cada guía
GUIAS_INFO = {
    "01-guia-introductoria-al-reglamento-de-ia": {
        "titulo": "Guía Introductoria al Reglamento de IA",
        "articulos": ["Art. 1-4 (General)", "Art. 5 (Prohibidos)", "Art. 6 (Clasificación)"],
        "modulos_plataforma": ["Classification", "Prohibited Systems"],
        "cobertura": 0.40,
        "prioridad": "Media",
        "objetivo": "Comprender los conceptos fundamentales del Reglamento de IA y su aplicación",
        "tareas": [
            "Revisar implementación de clasificación de sistemas de IA",
            "Verificar cobertura de sistemas prohibidos",
            "Validar que la documentación introductoria esté disponible para usuarios"
        ]
    },
    "02-guia-practica-y-ejemplos-para-entender-el-reglamento-de-ia": {
        "titulo": "Guía Práctica y Ejemplos para Entender el Reglamento de IA",
        "articulos": ["Art. 1-4 (General)", "Art. 5 (Prohibidos)", "Art. 6 (Clasificación)"],
        "modulos_plataforma": ["Classification", "Prohibited Systems"],
        "cobertura": 0.35,
        "prioridad": "Media",
        "objetivo": "Proporcionar ejemplos prácticos y casos de uso del Reglamento de IA",
        "tareas": [
            "Crear ejemplos prácticos en la documentación",
            "Implementar casos de uso de ejemplo en la plataforma",
            "Validar que los ejemplos cubran los principales escenarios"
        ]
    },
    "03-guia-evaluacion-de-conformidad": {
        "titulo": "Guía de Evaluación de Conformidad",
        "articulos": ["Art. 43", "Anexo VI", "Anexo VII"],
        "modulos_plataforma": ["Conformity Assessment", "QMS", "Technical Docs"],
        "cobertura": 0.85,
        "prioridad": "Alta",
        "objetivo": "Implementar el proceso completo de evaluación de conformidad según Art. 43",
        "tareas": [
            "Revisar y completar módulo de Conformity Assessment",
            "Validar integración con QMS y Technical Docs",
            "Verificar cumplimiento de Anexos VI y VII",
            "Implementar declaración UE de conformidad"
        ]
    },
    "04-guia-del-sistema-de-gestion-de-la-calidad": {
        "titulo": "Guía del Sistema de Gestión de la Calidad",
        "articulos": ["Art. 17"],
        "modulos_plataforma": ["QMS"],
        "cobertura": 0.90,
        "prioridad": "Alta",
        "objetivo": "Implementar sistema de gestión de calidad completo según Art. 17",
        "tareas": [
            "Validar que los 13 módulos QMS estén implementados",
            "Verificar cálculo de scores de compliance",
            "Revisar detección de gaps",
            "Validar integración con otros módulos"
        ]
    },
    "05-guia-de-gestion-de-riesgos": {
        "titulo": "Guía de Gestión de Riesgos",
        "articulos": ["Art. 9", "Art. 27 (FRIA)"],
        "modulos_plataforma": ["FRIA", "QMS"],
        "cobertura": 0.75,
        "prioridad": "Alta",
        "objetivo": "Implementar gestión de riesgos completa según Art. 9 y Art. 27",
        "tareas": [
            "Revisar módulo FRIA y su integración con gestión de riesgos",
            "Validar que QMS incluya gestión de riesgos",
            "Verificar cálculo de riesgos y medidas de mitigación",
            "Implementar seguimiento de riesgos residuales"
        ]
    },
    "06-guia-vigilancia-humana": {
        "titulo": "Guía de Vigilancia Humana",
        "articulos": ["Art. 14"],
        "modulos_plataforma": ["HITL Supervision"],
        "cobertura": 0.80,
        "prioridad": "Alta",
        "objetivo": "Implementar supervisión humana efectiva según Art. 14",
        "tareas": [
            "Revisar módulo HITL y su funcionalidad completa",
            "Validar que se cubran todos los requisitos del Art. 14",
            "Verificar integración con otros módulos de compliance",
            "Implementar métricas de supervisión humana"
        ]
    },
    "07-guia-de-datos-y-gobernanza-de-datos": {
        "titulo": "Guía de Datos y Gobernanza de Datos",
        "articulos": ["Art. 10"],
        "modulos_plataforma": ["Traceability", "Immutable Logs"],
        "cobertura": 0.70,
        "prioridad": "Media",
        "objetivo": "Implementar gobernanza de datos según Art. 10",
        "tareas": [
            "Revisar módulos de trazabilidad y logs inmutables",
            "Validar gestión de calidad de datos de entrenamiento",
            "Implementar módulo específico de gobernanza de datos si es necesario",
            "Verificar cumplimiento de requisitos de datos según Art. 10"
        ]
    },
    "08-guia-transparencia": {
        "titulo": "Guía de Transparencia",
        "articulos": ["Art. 13"],
        "modulos_plataforma": ["Technical Docs", "Traceability"],
        "cobertura": 0.65,
        "prioridad": "Media",
        "objetivo": "Implementar requisitos de transparencia según Art. 13",
        "tareas": [
            "Ampliar módulo de documentación técnica con sección de transparencia",
            "Implementar información para usuarios según Art. 13",
            "Validar que la documentación sea clara y accesible",
            "Revisar integración con trazabilidad"
        ]
    },
    "09-guia-de-precision": {
        "titulo": "Guía de Precisión",
        "articulos": ["Art. 15 (parcial)"],
        "modulos_plataforma": ["Post-Market Monitoring", "Technical Docs"],
        "cobertura": 0.60,
        "prioridad": "Media",
        "objetivo": "Implementar métricas y validaciones de precisión",
        "tareas": [
            "Ampliar PMM con métricas específicas de precisión",
            "Implementar validaciones de precisión en documentación técnica",
            "Crear dashboard de métricas de precisión",
            "Validar seguimiento continuo de precisión"
        ]
    },
    "10-guia-solidez": {
        "titulo": "Guía de Solidez",
        "articulos": ["Art. 15 (parcial)"],
        "modulos_plataforma": ["Post-Market Monitoring", "QMS"],
        "cobertura": 0.65,
        "prioridad": "Media",
        "objetivo": "Implementar validaciones de solidez y robustez del sistema",
        "tareas": [
            "Ampliar QMS con validaciones específicas de solidez",
            "Implementar pruebas de robustez en PMM",
            "Validar resistencia a ataques y errores",
            "Crear métricas de solidez del sistema"
        ]
    },
    "11-guia-ciberseguridad": {
        "titulo": "Guía de Ciberseguridad",
        "articulos": ["Art. 15"],
        "modulos_plataforma": ["Immutable Logs", "Traceability"],
        "cobertura": 0.50,
        "prioridad": "Alta",
        "objetivo": "Implementar módulo específico de ciberseguridad según Art. 15",
        "tareas": [
            "Diseñar arquitectura del módulo de ciberseguridad",
            "Implementar requisitos específicos del Art. 15",
            "Integrar con logs inmutables y trazabilidad",
            "Crear dashboard de seguridad y métricas",
            "Validar cumplimiento de estándares de seguridad"
        ]
    },
    "12-guia-de-registros": {
        "titulo": "Guía de Registros",
        "articulos": ["Art. 49", "Anexo VIII"],
        "modulos_plataforma": ["EU Registration", "Traceability", "Immutable Logs"],
        "cobertura": 0.80,
        "prioridad": "Alta",
        "objetivo": "Implementar registro en base de datos EU según Art. 49",
        "tareas": [
            "Revisar módulo EU Registration completo",
            "Validar integración con trazabilidad y logs",
            "Verificar cumplimiento de Anexo VIII",
            "Implementar sincronización con base de datos EU"
        ]
    },
    "13-guia-vigilancia-poscomercializacion": {
        "titulo": "Guía de Vigilancia Poscomercialización",
        "articulos": ["Art. 20", "Art. 72"],
        "modulos_plataforma": ["Post-Market Monitoring"],
        "cobertura": 0.85,
        "prioridad": "Alta",
        "objetivo": "Implementar monitoreo post-mercado completo según Art. 20 y 72",
        "tareas": [
            "Revisar módulo PMM completo",
            "Validar detección de anomalías y drift",
            "Verificar notificación a autoridades según Art. 20",
            "Implementar métricas continuas de monitoreo"
        ]
    },
    "14-guia-gestion-de-incidentes": {
        "titulo": "Guía de Gestión de Incidentes",
        "articulos": ["Art. 20", "Art. 72"],
        "modulos_plataforma": ["Post-Market Monitoring"],
        "cobertura": 0.75,
        "prioridad": "Alta",
        "objetivo": "Implementar flujo completo de gestión de incidentes",
        "tareas": [
            "Crear módulo específico de gestión de incidentes",
            "Implementar workflow completo: reporte, investigación, resolución",
            "Validar notificación a autoridades según severidad",
            "Integrar con PMM para detección automática",
            "Crear dashboard de incidentes"
        ]
    },
    "15-guia-documentacion-tecnica": {
        "titulo": "Guía de Documentación Técnica",
        "articulos": ["Art. 11", "Anexo IV"],
        "modulos_plataforma": ["Technical Docs"],
        "cobertura": 0.85,
        "prioridad": "Alta",
        "objetivo": "Implementar documentación técnica completa según Art. 11 y Anexo IV",
        "tareas": [
            "Revisar módulo de documentación técnica completo",
            "Validar cumplimiento de Anexo IV",
            "Verificar que toda la documentación requerida esté disponible",
            "Implementar generación automática de documentación"
        ]
    },
    "16-manual-de-checklist-de-guias-de-requisitos": {
        "titulo": "Manual de Checklist de Guías de Requisitos",
        "articulos": ["Múltiples (checklist general)"],
        "modulos_plataforma": ["Conformity Assessment", "QMS"],
        "cobertura": 0.70,
        "prioridad": "Media",
        "objetivo": "Crear checklist completo de verificación de requisitos",
        "tareas": [
            "Implementar checklist interactivo de requisitos",
            "Validar que cubra todos los requisitos del Reglamento",
            "Integrar con Conformity Assessment y QMS",
            "Crear reporte de cumplimiento automático"
        ]
    }
}

def extraer_conceptos_clave(texto, max_conceptos=10):
    """Extrae conceptos clave del texto de la guía"""
    conceptos = []

    # Buscar términos importantes
    terminos_importantes = [
        r'artículo\s+\d+',
        r'anexo\s+[IVX]+',
        r'requisito',
        r'obligación',
        r'evaluación',
        r'conformidad',
        r'riesgo',
        r'vigilancia',
        r'documentación',
        r'transparencia',
        r'ciberseguridad',
        r'precisión',
        r'solidez',
        r'gobernanza',
        r'datos'
    ]

    texto_lower = texto.lower()
    for termino in terminos_importantes:
        if re.search(termino, texto_lower):
            conceptos.append(termino.title())

    return list(set(conceptos))[:max_conceptos]

def generar_prompt_agente(guia_id, info_guia, texto_guia):
    """Genera un prompt completo para un agente"""

    conceptos = extraer_conceptos_clave(texto_guia[:5000])  # Primeros 5000 caracteres

    nivel_cobertura = "🟢 Excelente" if info_guia["cobertura"] >= 0.80 else \
                      "🟡 Buena" if info_guia["cobertura"] >= 0.60 else \
                      "🟠 Regular" if info_guia["cobertura"] >= 0.40 else \
                      "🔴 Insuficiente"

    prompt = f"""# 🤖 PROMPT PARA AGENTE - {info_guia['titulo']}

**Guía:** {info_guia['titulo']}
**Archivo Guía:** `{guia_id}.pdf`
**Texto Extraído:** `textos_extraidos/{guia_id}.txt`
**Fecha:** Diciembre 2025
**Prioridad:** {info_guia['prioridad']}
**Cobertura Actual:** {info_guia['cobertura']*100:.0f}% ({nivel_cobertura})

---

## 📋 CONTEXTO Y OBJETIVO

### **Objetivo Principal**
{info_guia['objetivo']}

### **Artículos del Reglamento Relacionados**
{chr(10).join(f"- {art}" for art in info_guia['articulos'])}

### **Módulos de la Plataforma Relacionados**
{chr(10).join(f"- **{mod}**" for mod in info_guia['modulos_plataforma'])}

### **Estado de Cobertura Actual**
- **Cobertura:** {info_guia['cobertura']*100:.0f}%
- **Nivel:** {nivel_cobertura}
- **Estado:** {'✅ Bien cubierto' if info_guia['cobertura'] >= 0.80 else '⚠️ Requiere mejoras' if info_guia['cobertura'] >= 0.60 else '❌ Cobertura insuficiente'}

---

## 🎯 TAREAS ESPECÍFICAS PARA EL AGENTE

### **Tareas Principales**
"""

    for i, tarea in enumerate(info_guia['tareas'], 1):
        prompt += f"{i}. {tarea}\n"

    prompt += f"""
---

## 📚 INFORMACIÓN DE LA GUÍA

### **Conceptos Clave Identificados**
{chr(10).join(f"- {concepto}" for concepto in conceptos[:10])}

### **Ubicación de la Guía**
- **PDF Original:** `docs/compliance/guias/{guia_id}.pdf`
- **Texto Extraído:** `docs/compliance/guias/textos_extraidos/{guia_id}.txt`
- **Análisis Completo:** Ver `analisis_guias.txt` para detalles específicos

### **Referencias Relacionadas**
- **Reporte de Cumplimiento:** `REPORTE_CUMPLIMIENTO_GUIAS.md`
- **Análisis Detallado:** `analisis_guias.txt`
- **Comparativa:** `comparativa_cumplimiento.py`

---

## 🔍 ANÁLISIS DE REQUISITOS

### **Requisitos Principales de la Guía**
El agente debe revisar el texto completo de la guía en `textos_extraidos/{guia_id}.txt` para identificar:

1. **Requisitos Obligatorios**
   - Identificar todos los requisitos mandatorios mencionados en la guía
   - Verificar si están implementados en los módulos relacionados
   - Documentar gaps encontrados

2. **Procesos y Procedimientos**
   - Identificar procesos descritos en la guía
   - Validar que los procesos de la plataforma los cubran
   - Proponer mejoras si es necesario

3. **Documentación Requerida**
   - Identificar documentación que debe generarse según la guía
   - Verificar que esté disponible en la plataforma
   - Crear documentación faltante si es necesario

4. **Métricas y Validaciones**
   - Identificar métricas mencionadas en la guía
   - Verificar que se calculen y muestren en la plataforma
   - Implementar métricas faltantes

---

## 🏗️ ARQUITECTURA Y MÓDULOS

### **Módulos Relacionados en la Plataforma**
"""

    for modulo in info_guia['modulos_plataforma']:
        prompt += f"""
#### **{modulo}**
- **Ubicación Documentación:** `codeflowx-studio/docs/prompts/compliance/PROMPT_COMPLIANCE_*.md`
- **Estado:** Verificar en documentación del módulo
- **Integración:** Validar integración con otros módulos relacionados
"""

    prompt += f"""
---

## ✅ CHECKLIST DE VERIFICACIÓN

### **Verificación de Cumplimiento**
- [ ] Revisar texto completo de la guía
- [ ] Identificar todos los requisitos mencionados
- [ ] Verificar implementación en módulos relacionados
- [ ] Documentar gaps encontrados
- [ ] Proponer mejoras específicas
- [ ] Validar integración entre módulos
- [ ] Crear documentación faltante
- [ ] Implementar funcionalidades faltantes (si aplica)

### **Validación de Integración**
- [ ] Verificar que los módulos relacionados funcionen correctamente
- [ ] Validar flujos de trabajo entre módulos
- [ ] Probar casos de uso descritos en la guía
- [ ] Verificar que las métricas se calculen correctamente

### **Documentación**
- [ ] Actualizar documentación técnica con requisitos de la guía
- [ ] Crear guías de usuario si es necesario
- [ ] Documentar gaps y mejoras propuestas
- [ ] Actualizar reporte de cumplimiento

---

## 📊 MÉTRICAS DE ÉXITO

### **Criterios de Completitud**
- ✅ Todos los requisitos obligatorios identificados
- ✅ Gaps documentados y priorizados
- ✅ Mejoras propuestas con estimación de esfuerzo
- ✅ Documentación actualizada
- ✅ Validación de integración completada

### **Entregables Esperados**
1. **Análisis de Cumplimiento**
   - Documento con requisitos identificados
   - Mapeo de requisitos con módulos implementados
   - Lista de gaps con priorización

2. **Propuesta de Mejoras**
   - Mejoras específicas con estimación de esfuerzo
   - Plan de implementación si aplica
   - Impacto esperado en cobertura

3. **Documentación Actualizada**
   - Documentación técnica actualizada
   - Guías de usuario si es necesario
   - Reporte de cumplimiento actualizado

---

## 🔗 REFERENCIAS Y RECURSOS

### **Documentación de la Plataforma**
- **Módulos Compliance:** `codeflowx-studio/docs/prompts/compliance/`
- **README Compliance:** `codeflowx-studio/docs/prompts/compliance/README.md`
- **Arquitectura Backend:** Ver documentación específica de cada módulo

### **Guías del Reglamento**
- **Guía Original:** `docs/compliance/guias/{guia_id}.pdf`
- **Texto Extraído:** `docs/compliance/guias/textos_extraidos/{guia_id}.txt`
- **Análisis:** `docs/compliance/guias/analisis_guias.txt`

### **Herramientas de Análisis**
- **Script Comparativa:** `comparativa_cumplimiento.py`
- **Reporte Cumplimiento:** `REPORTE_CUMPLIMIENTO_GUIAS.md`

---

## 💡 NOTAS IMPORTANTES

1. **Prioridad:** Esta guía tiene prioridad **{info_guia['prioridad']}** según el análisis de cumplimiento
2. **Cobertura Actual:** {info_guia['cobertura']*100:.0f}% - {'Excelente cobertura, validar y optimizar' if info_guia['cobertura'] >= 0.80 else 'Buena cobertura, identificar mejoras' if info_guia['cobertura'] >= 0.60 else 'Cobertura limitada, requiere trabajo significativo'}
3. **Enfoque:** {'Validar y optimizar implementación existente' if info_guia['cobertura'] >= 0.80 else 'Identificar gaps y proponer mejoras específicas' if info_guia['cobertura'] >= 0.60 else 'Diseñar e implementar funcionalidades faltantes'}

---

**Última actualización:** Diciembre 2025
**Estado:** ⏳ Pendiente de análisis por agente
"""

    return prompt

def main():
    """Genera prompts para todas las guías"""

    guias_dir = Path(__file__).parent
    textos_dir = guias_dir / "textos_extraidos"
    prompts_dir = guias_dir / "prompts_agentes"
    prompts_dir.mkdir(exist_ok=True)

    print("="*80)
    print("GENERACIÓN DE PROMPTS PARA AGENTES - GUIAS REGLAMENTO DE IA")
    print("="*80)

    prompts_generados = []

    for guia_id, info_guia in GUIAS_INFO.items():
        print(f"\nProcesando: {info_guia['titulo']}")

        # Leer texto de la guía
        txt_file = textos_dir / f"{guia_id}.txt"
        if txt_file.exists():
            with open(txt_file, 'r', encoding='utf-8') as f:
                texto_guia = f.read()
        else:
            print(f"  ⚠️  Archivo de texto no encontrado: {txt_file}")
            texto_guia = ""

        # Generar prompt
        prompt = generar_prompt_agente(guia_id, info_guia, texto_guia)

        # Guardar prompt
        prompt_file = prompts_dir / f"PROMPT_AGENTE_{guia_id.upper().replace('-', '_')}.md"
        with open(prompt_file, 'w', encoding='utf-8') as f:
            f.write(prompt)

        prompts_generados.append({
            "guia": info_guia['titulo'],
            "archivo": prompt_file.name,
            "cobertura": info_guia['cobertura'],
            "prioridad": info_guia['prioridad']
        })

        print(f"  ✅ Prompt generado: {prompt_file.name}")

    # Generar índice
    indice_file = prompts_dir / "README_PROMPTS_AGENTES.md"
    with open(indice_file, 'w', encoding='utf-8') as f:
        f.write("# 📚 ÍNDICE DE PROMPTS PARA AGENTES - GUIAS REGLAMENTO DE IA\n\n")
        f.write("**Fecha:** Diciembre 2025\n")
        f.write("**Total de Prompts:** {}\n\n".format(len(prompts_generados)))
        f.write("---\n\n")
        f.write("## 📋 LISTA DE PROMPTS\n\n")
        f.write("| # | Guía | Archivo | Cobertura | Prioridad |\n")
        f.write("|---|------|---------|-----------|-----------|\n")

        for i, prompt_info in enumerate(prompts_generados, 1):
            nivel = "🟢" if prompt_info['cobertura'] >= 0.80 else \
                   "🟡" if prompt_info['cobertura'] >= 0.60 else \
                   "🟠" if prompt_info['cobertura'] >= 0.40 else \
                   "🔴"
            f.write(f"| {i} | {prompt_info['guia']} | `{prompt_info['archivo']}` | {nivel} {prompt_info['cobertura']*100:.0f}% | {prompt_info['prioridad']} |\n")

        f.write("\n---\n\n")
        f.write("## 🎯 CÓMO USAR ESTOS PROMPTS\n\n")
        f.write("1. **Seleccionar una guía** de la lista\n")
        f.write("2. **Leer el prompt completo** del archivo correspondiente\n")
        f.write("3. **Revisar el texto de la guía** en `textos_extraidos/`\n")
        f.write("4. **Analizar requisitos** y comparar con implementación actual\n")
        f.write("5. **Documentar gaps** y proponer mejoras\n")
        f.write("6. **Actualizar reporte de cumplimiento** con resultados\n\n")
        f.write("---\n\n")
        f.write("## 📊 ESTADÍSTICAS\n\n")

        excelentes = sum(1 for p in prompts_generados if p['cobertura'] >= 0.80)
        buenas = sum(1 for p in prompts_generados if 0.60 <= p['cobertura'] < 0.80)
        regulares = sum(1 for p in prompts_generados if 0.40 <= p['cobertura'] < 0.60)
        insuficientes = sum(1 for p in prompts_generados if p['cobertura'] < 0.40)

        f.write(f"- 🟢 Excelente (≥80%): {excelentes} guías\n")
        f.write(f"- 🟡 Buena (60-79%): {buenas} guías\n")
        f.write(f"- 🟠 Regular (40-59%): {regulares} guías\n")
        f.write(f"- 🔴 Insuficiente (<40%): {insuficientes} guías\n")

    print(f"\n✅ Índice generado: {indice_file.name}")
    print(f"\n{'='*80}")
    print(f"✅ Proceso completado. {len(prompts_generados)} prompts generados")
    print(f"📁 Ubicación: {prompts_dir}")
    print(f"{'='*80}")

if __name__ == "__main__":
    main()
