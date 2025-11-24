# INCIDENCIAS Y RECOMENDACIONES - SUPERVISIÓN HUMANA (HITL)
**Fecha:** Diciembre 2025  
**Auditor:** Sistema de Gobierno de IA - CodeflowX  
**Documento Relacionado:** `AUDITORIA_SUPERVISION_HUMANA_HITL.md`  
**Base Legal:** EU AI Act Art. 14, ISO 42001

---

## 1. INCIDENCIAS DETECTADAS

### 1.1 Incidencias Críticas (CRITICAL)

#### **INC-HITL-001: Falta de Protección Robusta Contra Desactivación de Controles Críticos**

**Severidad:** 🔴 CRITICAL  
**Categoría:** Seguridad y Cumplimiento  
**Artículo Afectado:** EU AI Act Art. 14.1 (Human oversight)

**Descripción:**
Un partner con rol `GOVERNANCE_ADMIN` puede desactivar HITL para procesos de alto riesgo sin protección suficiente. La validación solo existe a nivel de aplicación y puede ser bypassed.

**Evidencia:**
- Validación solo en código Java (`@NotNull`, `@NotBlank`)
- No hay triggers de PostgreSQL que validen cambios críticos
- No hay CHECK constraints en tablas de configuración
- No hay registro de intentos de modificación de controles críticos
- No hay alertas automáticas cuando se detecta intento de desactivación

**Impacto:**
- 🔴 Violación directa de EU AI Act Art. 14.1
- 🔴 Riesgo legal y regulatorio significativo
- 🔴 Pérdida de trazabilidad y evidencia para auditorías
- 🔴 Posible pérdida de certificaciones (ISO 42001)

**Recomendación:**
Implementar protección multicapa:
1. **Nivel BD:** Triggers de PostgreSQL que validen cambios críticos
2. **Nivel Aplicación:** Reglas de negocio que impidan desactivar HITL para alto riesgo
3. **Nivel Auditoría:** Registro de todos los intentos de modificación
4. **Nivel Alertas:** Notificaciones automáticas a CISO/Compliance

**Prioridad:** ALTA  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Governance Team + DBA  
**Fecha Límite:** 2 semanas

---

#### **INC-HITL-002: Falta de Validación Obligatoria de HITL para Sistemas de Alto Riesgo**

**Severidad:** 🔴 CRITICAL  
**Categoría:** Cumplimiento Normativo  
**Artículo Afectado:** EU AI Act Art. 14.1, Art. 6 (Risk classification)

**Descripción:**
El sistema no garantiza automáticamente que HITL esté siempre activa para sistemas clasificados como de alto riesgo. La activación de HITL depende de configuración manual y puede omitirse.

**Evidencia:**
- No hay validación automática contra matriz de riesgo al desplegar
- Campo `AGTRISKASSESSMENT` es opcional (`nullable = true`)
- No hay constraint que relacione nivel de riesgo con requerimiento de HITL
- Procesos BPMN pueden ejecutarse sin validar nivel de riesgo

**Impacto:**
- 🔴 Incumplimiento de EU AI Act Art. 14.1 (supervisión humana obligatoria)
- 🔴 Sistemas de alto riesgo pueden operar sin supervisión
- 🔴 Riesgo regulatorio y legal

**Recomendación:**
1. Implementar validación automática en punto de entrada (deployment, configuración)
2. Hacer obligatorio campo `AGTRISKASSESSMENT` para sistemas de alto riesgo
3. Crear constraint de BD que relacione `riskLevel = HIGH|CRITICAL` con `requiresHitl = true`
4. Integrar validación en BPMN Engine antes de ejecutar procesos

**Prioridad:** ALTA  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Governance Team + Backend Team  
**Fecha Límite:** 2 semanas

---

#### **INC-HITL-003: Ausencia de Registro de Intentos de Modificación de Controles Críticos**

**Severidad:** 🔴 CRITICAL  
**Categoría:** Auditoría y Trazabilidad  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency), ISO 42001 (Audit trails)

**Descripción:**
No se registran los intentos de modificación o desactivación de controles críticos de HITL, lo que impide detectar y prevenir violaciones de cumplimiento.

**Evidencia:**
- No hay tabla de auditoría de cambios de configuración de HITL
- No hay registro de intentos fallidos de modificación
- No hay campo de "intento de bypass" en entidades de aprobación
- Logs de aplicación no capturan estos eventos de forma estructurada

**Impacto:**
- 🔴 Imposibilidad de detectar intentos de violación de controles
- 🔴 Falta de evidencia para auditorías externas
- 🔴 Incumplimiento de ISO 42001 (requisitos de auditoría)

**Recomendación:**
1. Crear tabla `GOVAUDITHITLCHANGES` para registrar todos los intentos de modificación
2. Implementar interceptor/auditoría a nivel de JPA para capturar cambios
3. Registrar: usuario, timestamp, cambio intentado, resultado (permitido/rechazado), razón
4. Generar alertas automáticas para intentos de modificación de controles críticos

**Prioridad:** ALTA  
**Esfuerzo Estimado:** 4-6 días  
**Responsable:** Governance Team + Backend Team  
**Fecha Límite:** 2 semanas

---

### 1.2 Incidencias Altas (HIGH)

#### **INC-HITL-004: Falta de Exportación Automática de Evidencia para Auditorías**

**Severidad:** 🟠 HIGH  
**Categoría:** Auditoría y Cumplimiento  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency)

**Descripción:**
No existe funcionalidad para exportar automáticamente evidencia de aprobaciones humanas en formatos estándar para auditorías externas (PDF, XML, JSON estructurado).

**Evidencia:**
- Datos almacenados en JSONB requieren parseo manual
- No hay endpoints REST para exportación de evidencia
- No hay generación automática de reportes de auditoría
- Consultas SQL deben ejecutarse manualmente

**Impacto:**
- 🟠 Dificulta auditorías externas
- 🟠 Mayor tiempo y esfuerzo para preparar evidencia
- 🟠 Riesgo de omisión de información relevante

**Recomendación:**
1. Crear endpoint `GET /api/v1/governance/approvals/export` con formatos PDF/XML/JSON
2. Implementar generación de reportes estructurados con toda la información de aprobación
3. Incluir metadatos de auditoría (hash, firma, timestamp)
4. Permitir filtrado por período, tipo, estado, aprobador

**Prioridad:** MEDIA  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Backend Team + Frontend Team  
**Fecha Límite:** 1 mes

---

#### **INC-HITL-005: Ausencia de Firma Digital en Aprobaciones**

**Severidad:** 🟠 HIGH  
**Categoría:** Integridad y No Repudio  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency), ISO 42001 (Integrity)

**Descripción:**
Las aprobaciones humanas no incluyen firma digital o certificado que garantice la integridad y no repudio de las decisiones.

**Evidencia:**
- No hay campo de hash/certificado en entidades de aprobación
- No hay integración con sistemas de firma electrónica
- No hay validación de integridad de registros históricos

**Impacto:**
- 🟠 Dificulta probar autenticidad de aprobaciones en auditorías
- 🟠 Riesgo de modificación no detectada de registros
- 🟠 Falta de garantía de no repudio

**Recomendación:**
1. Agregar campo `AGTHASHSIGNATURE` en entidades de aprobación
2. Calcular hash SHA-256 de registro completo al aprobar
3. Integrar con servicio de firma electrónica (opcional, para alta seguridad)
4. Implementar validación de integridad en consultas de auditoría

**Prioridad:** MEDIA  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Backend Team + Security Team  
**Fecha Límite:** 1 mes

---

#### **INC-HITL-006: Configuración de HITL No Personalizable por Partner/Tenant**

**Severidad:** 🟠 HIGH  
**Categoría:** Funcionalidad y Flexibilidad  
**Artículo Afectado:** EU AI Act Art. 14 (Human oversight)

**Descripción:**
Todos los partners comparten la misma política de HITL. No hay capacidad de personalización por tenant, lo que limita la flexibilidad para diferentes sectores o casos de uso.

**Evidencia:**
- No existe tabla de configuración por tenant
- Políticas hardcodeadas en código
- No hay override de configuración global (con restricciones)

**Impacto:**
- 🟠 Limitación para partners con requisitos específicos
- 🟠 Dificulta adopción en sectores con regulaciones específicas
- 🟠 Falta de flexibilidad operativa

**Recomendación:**
1. Crear tabla `GOVHITLCONFIG` con configuración por tenant
2. Permitir override de configuración global con validación de restricciones
3. Mantener controles críticos como no modificables (incluso con override)
4. Implementar UI de configuración con validación de políticas

**Prioridad:** MEDIA  
**Esfuerzo Estimado:** 7-10 días  
**Responsable:** Backend Team + Frontend Team  
**Fecha Límite:** 1.5 meses

---

### 1.3 Incidencias Medias (MEDIUM)

#### **INC-HITL-007: Campos JSONB Sin Validación de Esquema**

**Severidad:** 🟡 MEDIUM  
**Categoría:** Calidad de Datos  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency)

**Descripción:**
Los campos JSONB (`AGTRISKASSESSMENT`, `AGTCOMPLIANCECHECK`, etc.) no tienen validación de esquema, lo que puede llevar a datos inconsistentes o incompletos.

**Evidencia:**
- Campos JSONB definidos como `String` sin validación
- No hay constraints de JSON Schema en PostgreSQL
- No hay validación a nivel de aplicación antes de persistir

**Impacto:**
- 🟡 Datos inconsistentes pueden afectar análisis y reportes
- 🟡 Dificulta consultas estructuradas sobre JSONB
- 🟡 Riesgo de pérdida de información crítica

**Recomendación:**
1. Definir JSON Schema para cada campo JSONB
2. Implementar validación a nivel de aplicación usando Jackson/JSON Schema
3. Considerar usar CHECK constraints con validación JSON en PostgreSQL 12+
4. Documentar estructura esperada de cada campo JSONB

**Prioridad:** BAJA  
**Esfuerzo Estimado:** 3-4 días  
**Responsable:** Backend Team  
**Fecha Límite:** 2 meses

---

#### **INC-HITL-008: Falta de Campo de IP/Origen en Solicitudes de Aprobación**

**Severidad:** 🟡 MEDIUM  
**Categoría:** Auditoría y Trazabilidad  
**Artículo Afectado:** ISO 42001 (Audit trails)

**Descripción:**
Las solicitudes de aprobación no registran la dirección IP u origen de la solicitud, lo que limita la capacidad de auditoría y detección de anomalías.

**Evidencia:**
- No hay campo `AGTSOURCEIP` o similar en entidades de aprobación
- No hay registro de user agent o información de sesión
- Logs de aplicación no están estructurados para capturar esta información

**Impacto:**
- 🟡 Limitación en capacidad de auditoría forense
- 🟡 Dificulta detección de accesos no autorizados
- 🟡 Falta de contexto completo para investigaciones

**Recomendación:**
1. Agregar campos `AGTSOURCEIP`, `AGTUSERAGENT` en entidades de aprobación
2. Capturar información de origen en interceptores/auditores de JPA
3. Registrar información de sesión (si aplica)
4. Incluir en reportes de auditoría

**Prioridad:** BAJA  
**Esfuerzo Estimado:** 2-3 días  
**Responsable:** Backend Team  
**Fecha Límite:** 2 meses

---

#### **INC-HITL-009: Ausencia de Dashboard de Métricas de HITL**

**Severidad:** 🟡 MEDIUM  
**Categoría:** Visibilidad y Monitoreo  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency)

**Descripción:**
No existe dashboard o vista consolidada de métricas de HITL (tiempos de aprobación, tasas de aprobación/rechazo, distribución por tipo, etc.), lo que limita la visibilidad operativa.

**Evidencia:**
- Existe entidad `AutoApprovalAnalytics` pero no hay UI asociada
- Métricas deben calcularse mediante consultas SQL manuales
- No hay alertas proactivas sobre métricas anómalas

**Impacto:**
- 🟡 Limitada visibilidad operativa
- 🟡 Dificulta identificación de problemas o tendencias
- 🟡 No hay alertas proactivas

**Recomendación:**
1. Crear dashboard en frontend con métricas clave de HITL
2. Implementar gráficos de tendencias (tiempo de aprobación, tasas)
3. Configurar alertas para métricas anómalas (ej: tiempo de aprobación > threshold)
4. Incluir comparativas por período, tipo, aprobador

**Prioridad:** BAJA  
**Esfuerzo Estimado:** 5-7 días  
**Responsable:** Frontend Team + Backend Team  
**Fecha Límite:** 2 meses

---

#### **INC-HITL-010: Falta de Documentación de Criterios de Supervisión**

**Severidad:** 🟡 MEDIUM  
**Categoría:** Documentación y Transparencia  
**Artículo Afectado:** EU AI Act Art. 14.4 (Transparency)

**Descripción:**
No hay documentación clara y accesible sobre los criterios que determinan cuándo se requiere supervisión humana, qué nivel de aprobación se necesita, y cómo se evalúan las solicitudes.

**Evidencia:**
- Criterios están implícitos en código
- No hay documentación de usuario para aprobadores
- No hay guía de criterios de evaluación
- Matriz de riesgo no está documentada públicamente

**Impacto:**
- 🟡 Falta de transparencia (requisito EU AI Act Art. 14.4)
- 🟡 Dificulta capacitación de aprobadores
- 🟡 Puede llevar a inconsistencias en decisiones

**Recomendación:**
1. Crear documentación de criterios de supervisión humana
2. Documentar matriz de riesgo y niveles de aprobación
3. Crear guía para aprobadores con ejemplos
4. Publicar criterios en portal de gobierno de IA

**Prioridad:** BAJA  
**Esfuerzo Estimado:** 3-5 días  
**Responsable:** Technical Writing + Governance Team  
**Fecha Límite:** 1 mes

---

## 2. RECOMENDACIONES ESTRATÉGICAS

### 2.1 Mejoras Arquitecturales

#### **REC-HITL-001: Implementar Capa de Protección Multicapa**

**Descripción:**
Implementar protección a múltiples niveles (BD, aplicación, auditoría, alertas) para garantizar que controles críticos no puedan ser desactivados.

**Componentes:**
1. **Triggers de PostgreSQL:** Validar cambios críticos antes de commit
2. **Reglas de Negocio:** Validación en capa de servicio
3. **Auditoría:** Registro de todos los intentos
4. **Alertas:** Notificaciones automáticas

**Beneficios:**
- Protección robusta contra violaciones
- Cumplimiento con EU AI Act Art. 14
- Mejora de seguridad y trazabilidad

**Prioridad:** ALTA  
**Esfuerzo:** 5-7 días

---

#### **REC-HITL-002: Implementar Validación Automática de HITL por Nivel de Riesgo**

**Descripción:**
Sistema debe validar automáticamente que HITL esté activa para sistemas de alto riesgo, sin depender de configuración manual.

**Componentes:**
1. Validación en punto de entrada (deployment, configuración)
2. Constraints de BD que relacionen riesgo con HITL
3. Integración en BPMN Engine
4. Validación continua en runtime

**Beneficios:**
- Garantía de cumplimiento automático
- Reducción de errores humanos
- Mejora de seguridad

**Prioridad:** ALTA  
**Esfuerzo:** 3-5 días

---

### 2.2 Mejoras de Funcionalidad

#### **REC-HITL-003: Sistema de Exportación de Evidencia para Auditorías**

**Descripción:**
Implementar funcionalidad completa para exportar evidencia de aprobaciones en formatos estándar (PDF, XML, JSON) con toda la información necesaria para auditorías externas.

**Componentes:**
1. Endpoints REST para exportación
2. Generación de reportes estructurados
3. Inclusión de metadatos (hash, firma, timestamp)
4. Filtrado y búsqueda avanzada

**Beneficios:**
- Facilita auditorías externas
- Reduce tiempo de preparación de evidencia
- Mejora cumplimiento

**Prioridad:** MEDIA  
**Esfuerzo:** 5-7 días

---

#### **REC-HITL-004: Integración con Sistemas de Firma Electrónica**

**Descripción:**
Integrar con sistemas de firma electrónica para garantizar integridad y no repudio de aprobaciones.

**Componentes:**
1. Integración con servicio de firma (ej: DocuSign, Adobe Sign)
2. Almacenamiento de certificados/hashes
3. Validación de integridad
4. UI para firma desde portal

**Beneficios:**
- Garantía de integridad
- No repudio de decisiones
- Mejora de seguridad

**Prioridad:** MEDIA  
**Esfuerzo:** 7-10 días

---

### 2.3 Mejoras de Configuración

#### **REC-HITL-005: Configuración de HITL por Partner/Tenant**

**Descripción:**
Permitir configuración personalizada de HITL por tenant, manteniendo restricciones para controles críticos.

**Componentes:**
1. Tabla `GOVHITLCONFIG` con configuración por tenant
2. UI de configuración con validación
3. Override de configuración global con restricciones
4. Documentación de políticas

**Beneficios:**
- Flexibilidad para diferentes sectores
- Mejora de adopción
- Mantiene cumplimiento

**Prioridad:** MEDIA  
**Esfuerzo:** 7-10 días

---

## 3. PLAN DE ACCIÓN PRIORIZADO

### Fase 1: Protección Crítica (2 semanas)

1. ✅ **INC-HITL-001:** Implementar protección multicapa contra desactivación
2. ✅ **INC-HITL-002:** Validación obligatoria de HITL para alto riesgo
3. ✅ **INC-HITL-003:** Registro de intentos de modificación

### Fase 2: Mejoras de Auditoría (1 mes)

4. ✅ **INC-HITL-004:** Exportación automática de evidencia
5. ✅ **INC-HITL-005:** Firma digital en aprobaciones

### Fase 3: Funcionalidad y Configuración (1.5 meses)

6. ✅ **INC-HITL-006:** Configuración por partner/tenant
7. ✅ **INC-HITL-009:** Dashboard de métricas

### Fase 4: Mejoras Adicionales (2 meses)

8. ✅ **INC-HITL-007:** Validación de esquema JSONB
9. ✅ **INC-HITL-008:** Campo de IP/origen
10. ✅ **INC-HITL-010:** Documentación de criterios

---

## 4. MÉTRICAS DE ÉXITO

### 4.1 Métricas de Cumplimiento

- **100%** de sistemas de alto riesgo con HITL activa
- **0** intentos exitosos de desactivación de controles críticos
- **100%** de aprobaciones con registro completo

### 4.2 Métricas de Calidad

- Tiempo promedio de aprobación < 48 horas (alto riesgo)
- Tasa de aprobaciones automáticas < 20% (solo bajo riesgo)
- 0 aprobaciones sin justificación

### 4.3 Métricas de Auditoría

- 100% de evidencia exportable en < 5 minutos
- 0 gaps en trazabilidad
- 100% de aprobaciones con firma/hash

---

**Fin del Documento de Incidencias y Recomendaciones**







