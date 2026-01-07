# Backend - Gestión de Riesgos y Métricas de Calidad

**Fecha:** 2025-01-14
**Estado:** ✅ COMPLETADO

---

## 📋 Resumen

Se ha implementado el backend completo para las **Mejoras 1 y 2** del plan de 72 horas:

1. ✅ **Gestión de Riesgos de Datos** (DTGDATASETRISKS)
2. ✅ **Métricas de Calidad Detalladas ISO 8000** (DTGDATAQUALITYMETRICS)

---

## 🗄️ Tablas SQL Creadas

### 1. DTGDATASETRISKS

```sql
CREATE TABLE IF NOT EXISTS DTGDATASETRISKS (
    IDXRISK BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET) ON DELETE CASCADE,

    -- Tipo y Descripción
    DTGRISKTYPE VARCHAR(50) NOT NULL, -- QUALITY, BIAS, SECURITY, PRIVACITY, COMPLIANCE, LEGAL
    DTGRISKNAME VARCHAR(255) NOT NULL,
    DTGRISKDESCRIPTION TEXT,

    -- Evaluación de Riesgo
    DTGRISKPROBABILITY VARCHAR(20) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
    DTGRISKIMPACT VARCHAR(20) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
    DTGRISKSCORE DECIMAL(5,2), -- Calculado: probability * impact

    -- Estado y Mitigación
    DTGRISKSTATUS VARCHAR(20) NOT NULL DEFAULT 'IDENTIFIED',
    DTGMITIGATIONPLAN TEXT,
    DTGMITIGATIONSTATUS VARCHAR(20),
    DTGRESPONSIBLE BIGINT,
    DTGTARGETDATE TIMESTAMP,

    -- Auditoría
    DTGCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGCREATEDBY BIGINT,
    DTGUPDATEDAT TIMESTAMP,
    DTGUPDATEDBY BIGINT
);
```

### 2. DTGDATAQUALITYMETRICS

```sql
CREATE TABLE IF NOT EXISTS DTGDATAQUALITYMETRICS (
    IDXMETRIC BIGSERIAL PRIMARY KEY,
    IDXDATASET BIGINT NOT NULL REFERENCES DTGDATASETS(IDXDATASET) ON DELETE CASCADE,

    -- Dimensión de Calidad (ISO 8000)
    DTGQUALITYDIMENSION VARCHAR(50) NOT NULL, -- COMPLETENESS, ACCURACY, CONSISTENCY, VALIDITY, TIMELINESS, UNIQUENESS

    -- Métricas
    DTGQUALITYSCORE DECIMAL(5,2), -- 0.00 - 1.00
    DTGQUALITYTHRESHOLD DECIMAL(5,2) DEFAULT 0.80,
    DTGQUALITYSTATUS VARCHAR(20) NOT NULL, -- PASS, WARNING, FAIL

    -- Detalles Específicos
    DTGQUALITYDETAILS JSONB,

    -- Versión y Fecha
    DTGMETRICDATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGMETRICVERSION VARCHAR(50),

    -- Auditoría
    DTGCREATEDAT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    DTGCREATEDBY BIGINT
);
```

---

## 📦 Componentes Implementados

### **Entidades JPA**

1. ✅ `DataGovernanceDatasetRisk.java`
   - Entidad completa con lifecycle hooks
   - Cálculo automático de score de riesgo
   - Validaciones y anotaciones

2. ✅ `DataGovernanceQualityMetric.java`
   - Entidad completa con lifecycle hooks
   - Cálculo automático de estado (PASS/WARNING/FAIL)
   - Validaciones y anotaciones

### **Repositorios**

1. ✅ `DataGovernanceDatasetRiskRepository.java`
   - Métodos de búsqueda por dataset, tipo, estado, probabilidad, impacto
   - Búsqueda de riesgos críticos (score >= 0.8)
   - Búsqueda de riesgos pendientes de mitigación

2. ✅ `DataGovernanceQualityMetricRepository.java`
   - Métodos de búsqueda por dataset, dimensión, estado
   - Cálculo de score promedio
   - Búsqueda de métricas bajo umbral

### **DTOs**

1. ✅ `DataGovernanceDatasetRiskDto.java`
   - DTO completo con validaciones
   - Anotaciones Swagger/OpenAPI
   - Mapeo completo de campos

2. ✅ `DataGovernanceQualityMetricDto.java`
   - DTO completo con validaciones
   - Anotaciones Swagger/OpenAPI
   - Mapeo completo de campos

### **Servicios (Interfaces)**

1. ✅ `DataGovernanceDatasetRiskService.java`
   - Interface reactiva con todos los métodos necesarios
   - CRUD completo
   - Métodos especiales (críticos, pendientes)

2. ✅ `DataGovernanceQualityMetricService.java`
   - Interface reactiva con todos los métodos necesarios
   - CRUD completo
   - Métodos especiales (promedio, bajo umbral)

### **Controladores REST**

1. ✅ `DataGovernanceDatasetRiskController.java`
   - Endpoints completos para gestión de riesgos
   - Documentación Swagger/OpenAPI
   - Métricas de tiempo
   - Manejo de errores

2. ✅ `DataGovernanceQualityMetricController.java`
   - Endpoints completos para métricas de calidad
   - Documentación Swagger/OpenAPI
   - Métricas de tiempo
   - Manejo de errores

---

## 🔌 Endpoints REST Implementados

### **Riesgos**

- `POST /api/v1/governance/data/datasets/{datasetId}/risks` - Crear riesgo
- `GET /api/v1/governance/data/datasets/{datasetId}/risks` - Listar riesgos de dataset
- `GET /api/v1/governance/data/risks/{riskId}` - Obtener riesgo por ID
- `GET /api/v1/governance/data/risks` - Listar todos los riesgos (con filtros)
- `PUT /api/v1/governance/data/risks/{riskId}` - Actualizar riesgo
- `DELETE /api/v1/governance/data/risks/{riskId}` - Eliminar riesgo
- `GET /api/v1/governance/data/risks/critical` - Obtener riesgos críticos
- `GET /api/v1/governance/data/risks/pending-mitigation` - Obtener riesgos pendientes

### **Métricas de Calidad**

- `POST /api/v1/governance/data/datasets/{datasetId}/quality-metrics` - Crear/actualizar métrica
- `GET /api/v1/governance/data/datasets/{datasetId}/quality-metrics` - Listar métricas de dataset
- `GET /api/v1/governance/data/datasets/{datasetId}/quality-metrics/{dimension}` - Obtener métrica por dimensión
- `GET /api/v1/governance/data/quality-metrics/{metricId}` - Obtener métrica por ID
- `GET /api/v1/governance/data/quality-metrics` - Listar métricas (con filtros)
- `GET /api/v1/governance/data/datasets/{datasetId}/quality-metrics/average` - Calcular score promedio
- `GET /api/v1/governance/data/datasets/{datasetId}/quality-metrics/below-threshold` - Obtener métricas bajo umbral
- `DELETE /api/v1/governance/data/quality-metrics/{metricId}` - Eliminar métrica

---

## ✨ Características Especiales

### **Cálculo Automático de Score de Riesgo**

La entidad `DataGovernanceDatasetRisk` calcula automáticamente el score de riesgo basado en probabilidad e impacto:

- **LOW** = 0.25
- **MEDIUM** = 0.50
- **HIGH** = 0.75
- **CRITICAL** = 1.0

Score = Probabilidad × Impacto (0.00 - 1.00)

### **Cálculo Automático de Estado de Calidad**

La entidad `DataGovernanceQualityMetric` calcula automáticamente el estado basado en score y umbral:

- **PASS**: score >= umbral
- **WARNING**: score >= umbral × 0.8
- **FAIL**: score < umbral × 0.8

---

## 📝 Próximos Pasos

1. **Implementar Servicios (Implementaciones)**
   - Crear implementaciones de las interfaces de servicio
   - Integrar con repositorios
   - Manejo de errores y validaciones

2. **Testing**
   - Tests unitarios de entidades
   - Tests de repositorios
   - Tests de servicios
   - Tests de controladores

3. **Integración Frontend ↔ Backend**
   - Actualizar API routes del frontend
   - Reemplazar mocks con llamadas reales
   - Manejo de errores

---

## ✅ Checklist

- [x] Tablas SQL creadas
- [x] Entidades JPA implementadas
- [x] Repositorios implementados
- [x] DTOs creados
- [x] Interfaces de servicio definidas
- [x] Controladores REST implementados
- [ ] Implementaciones de servicios (pendiente)
- [ ] Tests (pendiente)
- [ ] Integración frontend (pendiente)

---

**Última actualización:** 2025-01-14
