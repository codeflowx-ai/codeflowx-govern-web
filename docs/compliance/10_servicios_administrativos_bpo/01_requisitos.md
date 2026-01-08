# Requisitos Normativos Sector BPO Services

## 1. Contexto y Alcance
Este documento consolida las referencias regulatorias aplicables a servicios de Business Process Outsourcing (BPO) con operaciones multicanal (contact center, backoffice documental, gestión de reclamaciones) para jurisdicciones **UE** y **LATAM**. Sirve como insumo para el metamodelo `BPO_SERVICES_v1.0.0` y deberá revisarse trimestralmente por el Comité de Cumplimiento.

## 2. Referencias Normativas Principales
- **EU AI Act** (Parlamento Europeo, texto de compromiso 2024) – Art. 9-15 para sistemas de IA de alto riesgo en servicios críticos.
- **Reglamento (UE) 2016/679 (RGPD)** y **LOPDGDD 3/2018** – Protección de datos personales en operaciones con clientes europeos.
- **ISO/IEC 27001:2022** – Sistema de Gestión de Seguridad de la Información obligatorio para centros de servicio que mantienen datos sensibles.
- **ISO 18295-1:2017** – Requisitos de calidad para centros de contacto orientados al cliente.
- **ENS (Esquema Nacional de Seguridad) – Nivel Medio** (España) – Aplicable a BPO que gestionen información para Administraciones Públicas.
- **Ley Federal de Protección de Datos Personales en Posesión de los Particulares (México)** – Obligaciones LATAM para servicios nearshore/offshore.

## 3. Controles Clave Derivados
| Control | Referencia | Evidencia requerida |
|---------|------------|---------------------|
| Gestión de riesgos de IA (inventario, evaluación de impacto) | EU AI Act Art. 9-11 | Registro FRIA, ImmutableLog, planes de mitigación |
| Gobernanza de datos personales | RGPD Art. 30, 32 | Registro de actividades, análisis de riesgo de tratamiento, logs de acceso |
| Continuidad del servicio y SLA | ISO 18295, ISO 27001 A.17 | Planes BCM, métricas de SLA, reportes de contingencia |
| Seguridad y segregación de datos | ENS, ISO 27001 A.8 | Clasificación de activos, cifrado en tránsito/reposo |
| Gestión de terceros/subcontratistas | EU AI Act Art. 28, ISO 27001 A.5 | Evaluación de proveedores, acuerdos de desempeño, auditorías |

## 4. Requisitos de Evidencia
- **FRIA BPO**: Evaluación formal de riesgos de IA incluyendo bias operativo, governance score, controles compensatorios.
- **ImmutableLog**: Registro inmutable de decisiones automatizadas e intervenciones humanas (HITL).
- **Service Level Governance**: Reportes mensuales de cumplimiento de SLA (AHT, FCR, NPS), con justificación de desvíos.
- **Personal Data Incident Response**: Procedimientos documentados y métricas MTTR/MTTD para filtraciones o uso indebido de datos.

## 5. Responsabilidades
- **Oficina IA**: Custodia del FRIA y validación de políticas AI OS.
- **Responsable de Protección de Datos (DPO)**: Seguimiento RGPD/LOPDGDD, coordinación con clientes.
- **Service Delivery Manager**: Evidencias de SLA, coordinación de planes de mejora.
- **Proveedor Tecnológico**: Mantenimiento de telemetría y publicación al marketplace AI OS.

## 6. Próximas Acciones
1. Validar catálogo de controles con auditores externos (Q1 2026).
2. Integrar métricas ENS/ISO a dashboards (`governance_metrics_bpo_services`).
3. Revisar implicaciones del AI Act una vez aprobado definitivamente (monitoring continuo).

---
Última revisión: 2025-11-11 · Responsable: Oficina IA CodeflowX · Versión: 1.0.0





