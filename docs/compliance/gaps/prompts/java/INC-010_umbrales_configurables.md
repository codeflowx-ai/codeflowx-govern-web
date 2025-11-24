# PROMPT: INC-010 - Umbrales Configurables Métricas

**Incidencia:** INC-010  
**Prioridad:** 🟡 MEDIA  
**Artículo EU AI Act:** Art. 10, Art. 15  
**Esfuerzo Estimado:** 2 días  
**Tipo:** Java - Backend

---

## CONTEXTO

Umbrales de validación están hardcodeados. No hay configuración por sector o tipo de sistema.

---

## IMPLEMENTACIÓN

### 1. Crear tabla de configuración

```sql
CREATE TABLE GOVMETRICTHRESHOLDS (
    IDXMETRICTHRESHOLD BIGSERIAL PRIMARY KEY,
    METRICNAME VARCHAR(100) NOT NULL,
    SECTORCODE VARCHAR(50), -- NULL = global
    SYSTEMTYPE VARCHAR(50), -- NULL = todos
    THRESHOLDVALUE NUMERIC(5,2) NOT NULL,
    THRESHOLDTYPE VARCHAR(20) NOT NULL, -- MIN, MAX
    CREATEDAT TIMESTAMP NOT NULL,
    UPDATEDAT TIMESTAMP
);
```

### 2. Crear servicio

```java
@Service
public class MetricThresholdService {
    public boolean validateMetric(String metricName, BigDecimal value, 
                                 String sectorCode, String systemType) {
        MetricThreshold threshold = getThreshold(metricName, sectorCode, systemType);
        if (threshold.getThresholdType().equals("MIN")) {
            return value.compareTo(threshold.getThresholdValue()) >= 0;
        } else {
            return value.compareTo(threshold.getThresholdValue()) <= 0;
        }
    }
}
```

---

## REFERENCIAS

- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_Y_RECOMENDACIONES_AUDITORIA.md#inc-010`

---

**Estado:** ✅ COMPLETADO

