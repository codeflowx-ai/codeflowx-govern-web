# 📘 CODEFLOWX - DOCUMENTACIÓN TÉCNICA DE PROCESOS BPMN

**Versión:** 1.0  
**Fecha:** Octubre 2025  
**Audiencia:** Desarrolladores, Analistas de Sistemas, Arquitectos  
**Proyecto:** CodeflowX AI Governance Platform

---

## 📑 TABLA DE CONTENIDOS

1. [Arquitectura General](#arquitectura-general)
2. [Procesos de Aprobación](#procesos-de-aprobacion)
3. [Procesos de Detección](#procesos-de-deteccion)
4. [Procesos de Evaluación](#procesos-de-evaluacion)
5. [Procesos de Governance](#procesos-de-governance)
6. [Procesos de Automatización](#procesos-de-automatizacion)
7. [Anexos Técnicos](#anexos-tecnicos)

---

## 🏗️ ARQUITECTURA GENERAL

### Stack Tecnológico

```
┌─────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN                               │
│  • ZKoss (ZUL + MVVM)                              │
│  • Bandeja de Tareas Unificada                     │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE ORQUESTACIÓN                               │
│  • Flowable BPMN Engine                            │
│  • 17 Procesos BPMN 2.0 Avanzados                  │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE REGLAS DE NEGOCIO                          │
│  • Drools Rules Engine                             │
│  • 11 Fact Objects + 11 DRL (120+ reglas)          │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE SERVICIOS                                  │
│  • Spring Boot Microservices                       │
│  • JavaDelegates (80+ delegates)                   │
│  • Integration con Python ML Services              │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│  CAPA DE PERSISTENCIA                               │
│  • PostgreSQL (JPA Entities)                       │
│  • MinIO (Object Storage)                          │
│  • Qdrant (Vector DB)                              │
└─────────────────────────────────────────────────────┘
```

### Elementos BPMN Avanzados Utilizados

| Elemento | Uso | Beneficio |
|----------|-----|-----------|
| **ParallelGateway** | Ejecución paralela de validaciones | 2-3x más rápido |
| **BusinessRuleTask** | Integración nativa con Drools | Reglas modificables sin redeploy |
| **Timer Boundary Event** | SLAs automáticos con recordatorios | Cumplimiento garantizado |
| **Error Boundary Event** | Resiliencia ante fallos externos | Alta disponibilidad |
| **Message Start Event** | Comunicación inter-proceso | Event-driven architecture |
| **MailTask** | Notificaciones automáticas | Sin código custom |

---

[... contenido completo del archivo ...]

**FIN DOCUMENTACIÓN TÉCNICA**

---

**Para dudas técnicas:**
- Revisar código en: `src/main/java/com/codeflowx/govern/workflow/`
- Logs Flowable: `flowable.engine` level DEBUG
- Drools logs: `org.drools` level DEBUG
