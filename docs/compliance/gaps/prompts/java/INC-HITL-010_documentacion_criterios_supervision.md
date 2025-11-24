# PROMPT: INC-HITL-010 - Documentación de Criterios de Supervisión

**Incidencia:** INC-HITL-010  
**Prioridad:** 🟡 MEDIUM  
**Artículo EU AI Act:** Art. 14.4 (Transparency)  
**Esfuerzo Estimado:** 3-5 días  
**Tipo:** Documentación  
**Estado:** ✅ COMPLETADO

---

## DESCRIPCIÓN

No hay documentación clara y accesible sobre los criterios que determinan cuándo se requiere supervisión humana, qué nivel de aprobación se necesita, y cómo se evalúan las solicitudes. Se requiere crear documentación completa y publicarla en portal de gobierno de IA.

---

## REQUISITOS

1. Crear documentación de criterios de supervisión humana
2. Documentar matriz de riesgo y niveles de aprobación
3. Crear guía para aprobadores con ejemplos
4. Publicar criterios en portal de gobierno de IA

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Documento de Criterios de Supervisión

**Archivo:** `docs/compliance/governance/CRITERIOS_SUPERVISION_HUMANA_HITL.md`

```markdown
# CRITERIOS DE SUPERVISIÓN HUMANA (HITL)

## 1. MATRIZ DE RIESGO Y NIVELES DE APROBACIÓN

| Nivel de Riesgo | Aprobación Requerida | Nivel | Timeout | Escalación |
|-----------------|----------------------|-------|---------|------------|
| **CRITICAL** | OBLIGATORIA | Committee | 24h | Automática a CISO |
| **HIGH** | OBLIGATORIA | Dual Approval | 48h | Automática a Director |
| **MEDIUM** | OBLIGATORIA | Single Approver | 72h | Manual a Supervisor |
| **LOW** | Opcional | Auto con notificación | N/A | Revisión mensual |

## 2. CRITERIOS DE EVALUACIÓN

### 2.1 Evaluación de Riesgo

- **CRITICAL:** Sistemas que pueden causar daño grave a personas o violar derechos fundamentales
- **HIGH:** Sistemas que procesan datos personales sensibles o toman decisiones críticas
- **MEDIUM:** Sistemas con impacto moderado en usuarios
- **LOW:** Sistemas con impacto mínimo

### 2.2 Criterios de Aprobación

- **Técnico:** Verificar que el sistema cumple requisitos técnicos
- **Cumplimiento:** Verificar que cumple normativas aplicables (EU AI Act, GDPR, etc.)
- **Ético:** Evaluar impacto ético y sesgos potenciales

## 3. GUÍA PARA APROBADORES

[Incluir ejemplos prácticos, checklist, etc.]
```

### 2. Guía para Aprobadores

**Archivo:** `docs/compliance/governance/GUIA_APROBADORES_HITL.md`

```markdown
# GUÍA PARA APROBADORES - SUPERVISIÓN HUMANA (HITL)

## 1. PROCESO DE APROBACIÓN

1. Revisar solicitud y contexto
2. Evaluar nivel de riesgo
3. Verificar cumplimiento normativo
4. Tomar decisión (aprobar/rechazar/condicional)
5. Registrar justificación

## 2. CHECKLIST DE EVALUACIÓN

- [ ] Evaluación de riesgo completada
- [ ] Verificación de cumplimiento realizada
- [ ] Revisión técnica realizada
- [ ] Revisión ética realizada (si aplica)
- [ ] Justificación documentada

## 3. EJEMPLOS PRÁCTICOS

[Incluir casos de uso reales]
```

### 3. Integración en Portal de Gobierno

**Archivo:** Crear página en frontend para mostrar documentación

```typescript
// Componente React/Vue para mostrar documentación
// Integrar en portal de gobierno de IA
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_SUPERVISION_HUMANA_HITL.md#inc-hitl-010`
- **EU AI Act Art. 14.4:** Transparency

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Technical Writing + Governance Team  
**Fecha Límite:** 1 mes

