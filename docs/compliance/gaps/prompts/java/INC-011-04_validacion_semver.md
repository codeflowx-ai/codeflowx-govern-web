# PROMPT: INC-011-04 - Validación Automática de Formato SemVer en Versiones

**Incidencia:** INC-011-04  
**Prioridad:** 🟡 ALTA  
**Artículo EU AI Act:** Art. 11 (Documentación Técnica)  
**Esfuerzo Estimado:** 2.5 días  
**Tipo:** Java - Backend + DBA

---

## CONTEXTO

Los campos de versión (`MODVERSION`, `PRMVERSION`, `RAGVVERSIONNUMBER`, `DATAVERSIONNUMBER`, `EVALVERSIONNUMBER`) son VARCHAR sin validación de formato. No se valida que sigan formato SemVer (X.Y.Z) ni se calculan versiones automáticas.

**Ubicación Actual:**
- `ModelVersion.java` - campo `MODVERSION` (VARCHAR sin constraint)
- `PromptVersion.java` - campo `PRMVERSION` (VARCHAR sin constraint)
- `RagVersion.java` - campo `RAGVVERSIONNUMBER` (VARCHAR sin constraint)
- `DatasetVersion.java` - campo `DATAVERSIONNUMBER` (VARCHAR sin constraint)
- `ModelEvaluation.java` - campo `EVALVERSIONNUMBER` (VARCHAR sin constraint)

**Problemas:**
- Inconsistencias en numeración de versiones (ej: "v1", "1.0", "version-1")
- Dificulta comparación y ordenamiento de versiones
- No hay validación a nivel de base de datos
- No hay cálculo automático de siguiente versión

---

## REQUISITOS

1. **Agregar constraints en BD** para validar formato SemVer con regex
2. **Validación en BusinessService** antes de guardar
3. **Cálculo automático de siguiente versión** si no se proporciona
4. **Utilidad compartida** para validación y cálculo de versiones SemVer
5. **Migración de datos existentes** para normalizar versiones sin formato

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Agregar Constraints en Base de Datos (DBA)

**Archivo:** `nocode.service.entitys/src/main/resources/sql/validacion_semver_constraints.sql`

```sql
-- ============================================================================
-- PATRÓN SEMVER: X.Y.Z[-pre-release][+build-metadata]
-- Ejemplos válidos: 1.0.0, 1.2.3, 1.0.0-alpha, 1.0.0+build.1
-- ============================================================================

-- Constraint para MODMODELVERSIONS
ALTER TABLE MODMODELVERSIONS 
ADD CONSTRAINT chk_mod_version_format 
CHECK (MODVERSION ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?(\+[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$');

-- Constraint para PRMPROMPTVERSIONS
ALTER TABLE PRMPROMPTVERSIONS 
ADD CONSTRAINT chk_prm_version_format 
CHECK (PRMVERSION ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?(\+[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$');

-- Constraint para RAGRAGVERSIONS
ALTER TABLE RAGRAGVERSIONS 
ADD CONSTRAINT chk_ragv_version_format 
CHECK (RAGVVERSIONNUMBER ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?(\+[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$');

-- Constraint para DATADATASETVERSIONS
ALTER TABLE DATADATASETVERSIONS 
ADD CONSTRAINT chk_data_version_format 
CHECK (DATAVERSIONNUMBER ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?(\+[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$');

-- Constraint para MODEVALUATIONS
ALTER TABLE MODEVALUATIONS 
ADD CONSTRAINT chk_eval_version_format 
CHECK (EVALVERSIONNUMBER IS NULL OR EVALVERSIONNUMBER ~ '^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?(\+[a-zA-Z0-9]+(\.[a-zA-Z0-9]+)*)?$');

-- ============================================================================
-- MIGRACIÓN DE DATOS EXISTENTES
-- ============================================================================

-- Función para normalizar versiones a formato SemVer
CREATE OR REPLACE FUNCTION normalize_version(version_text TEXT)
RETURNS TEXT AS $$
DECLARE
    normalized TEXT;
    parts TEXT[];
    major INT;
    minor INT;
    patch INT;
BEGIN
    -- Si ya está en formato SemVer, retornar tal cual
    IF version_text ~ '^[0-9]+\.[0-9]+\.[0-9]+' THEN
        RETURN version_text;
    END IF;
    
    -- Intentar extraer números de la versión
    -- Ejemplos: "v1" -> "1.0.0", "1.0" -> "1.0.0", "version-2" -> "2.0.0"
    normalized := regexp_replace(version_text, '[^0-9]', '', 'g');
    
    IF normalized = '' OR length(normalized) = 0 THEN
        RETURN '1.0.0'; -- Versión por defecto
    END IF;
    
    -- Tomar primer número como major
    major := CAST(substring(normalized from 1 for 1) AS INT);
    
    -- Si hay más dígitos, usar como minor, sino 0
    IF length(normalized) > 1 THEN
        minor := CAST(substring(normalized from 2 for 1) AS INT);
    ELSE
        minor := 0;
    END IF;
    
    -- Patch siempre 0 para migración
    patch := 0;
    
    RETURN major || '.' || minor || '.' || patch;
END;
$$ LANGUAGE plpgsql;

-- Migrar MODMODELVERSIONS
UPDATE MODMODELVERSIONS 
SET MODVERSION = normalize_version(MODVERSION)
WHERE MODVERSION !~ '^[0-9]+\.[0-9]+\.[0-9]+';

-- Migrar PRMPROMPTVERSIONS
UPDATE PRMPROMPTVERSIONS 
SET PRMVERSION = normalize_version(PRMVERSION)
WHERE PRMVERSION !~ '^[0-9]+\.[0-9]+\.[0-9]+';

-- Migrar RAGRAGVERSIONS
UPDATE RAGRAGVERSIONS 
SET RAGVVERSIONNUMBER = normalize_version(RAGVVERSIONNUMBER)
WHERE RAGVVERSIONNUMBER !~ '^[0-9]+\.[0-9]+\.[0-9]+';

-- Migrar DATADATASETVERSIONS (si existe)
UPDATE DATADATASETVERSIONS 
SET DATAVERSIONNUMBER = normalize_version(DATAVERSIONNUMBER)
WHERE DATAVERSIONNUMBER !~ '^[0-9]+\.[0-9]+\.[0-9]+';

-- Migrar MODEVALUATIONS (si existe y no es NULL)
UPDATE MODEVALUATIONS 
SET EVALVERSIONNUMBER = normalize_version(EVALVERSIONNUMBER)
WHERE EVALVERSIONNUMBER IS NOT NULL 
  AND EVALVERSIONNUMBER !~ '^[0-9]+\.[0-9]+\.[0-9]+';

-- Eliminar función temporal después de migración
-- DROP FUNCTION normalize_version(TEXT);
```

### 2. Crear Utilidad SemVer Compartida

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/util/SemVerUtil.java`

```java
package com.codeflowx.govern.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Utilidad para validación y manipulación de versiones SemVer
 * Formato: X.Y.Z[-pre-release][+build-metadata]
 * 
 * Requisito: Art. 11 (Documentación Técnica) - Versionado consistente
 */
public class SemVerUtil {
    
    // Patrón SemVer: X.Y.Z[-pre-release][+build-metadata]
    private static final Pattern SEMVER_PATTERN = Pattern.compile(
        "^([0-9]+)\\.([0-9]+)\\.([0-9]+)(-([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*))?(\\+([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*))?$"
    );
    
    /**
     * Valida que una versión siga formato SemVer
     * @param version Versión a validar
     * @return true si es válida
     */
    public static boolean isValid(String version) {
        if (version == null || version.trim().isEmpty()) {
            return false;
        }
        return SEMVER_PATTERN.matcher(version.trim()).matches();
    }
    
    /**
     * Valida y lanza excepción si no es válida
     * @param version Versión a validar
     * @throws IllegalArgumentException si no es válida
     */
    public static void validate(String version) {
        if (!isValid(version)) {
            throw new IllegalArgumentException(
                "Versión debe seguir formato SemVer (X.Y.Z[-pre-release][+build-metadata]). " +
                "Recibido: " + version
            );
        }
    }
    
    /**
     * Parsea una versión SemVer
     * @param version Versión a parsear
     * @return SemVer object o null si no es válida
     */
    public static SemVer parse(String version) {
        if (!isValid(version)) {
            return null;
        }
        
        Matcher matcher = SEMVER_PATTERN.matcher(version.trim());
        if (!matcher.matches()) {
            return null;
        }
        
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = Integer.parseInt(matcher.group(3));
        String preRelease = matcher.group(5);
        String buildMetadata = matcher.group(8);
        
        return new SemVer(major, minor, patch, preRelease, buildMetadata);
    }
    
    /**
     * Calcula siguiente versión incrementando patch
     * @param currentVersion Versión actual
     * @return Siguiente versión (incrementa patch)
     */
    public static String nextPatch(String currentVersion) {
        SemVer semver = parse(currentVersion);
        if (semver == null) {
            return "1.0.0";
        }
        return new SemVer(semver.major, semver.minor, semver.patch + 1, null, null).toString();
    }
    
    /**
     * Calcula siguiente versión incrementando minor
     * @param currentVersion Versión actual
     * @return Siguiente versión (incrementa minor, reset patch)
     */
    public static String nextMinor(String currentVersion) {
        SemVer semver = parse(currentVersion);
        if (semver == null) {
            return "1.0.0";
        }
        return new SemVer(semver.major, semver.minor + 1, 0, null, null).toString();
    }
    
    /**
     * Calcula siguiente versión incrementando major
     * @param currentVersion Versión actual
     * @return Siguiente versión (incrementa major, reset minor y patch)
     */
    public static String nextMajor(String currentVersion) {
        SemVer semver = parse(currentVersion);
        if (semver == null) {
            return "1.0.0";
        }
        return new SemVer(semver.major + 1, 0, 0, null, null).toString();
    }
    
    /**
     * Compara dos versiones SemVer
     * @param v1 Primera versión
     * @param v2 Segunda versión
     * @return negativo si v1 < v2, 0 si iguales, positivo si v1 > v2
     */
    public static int compare(String v1, String v2) {
        SemVer semver1 = parse(v1);
        SemVer semver2 = parse(v2);
        
        if (semver1 == null || semver2 == null) {
            throw new IllegalArgumentException("Versiones no válidas para comparar: " + v1 + " vs " + v2);
        }
        
        // Comparar major
        int cmp = Integer.compare(semver1.major, semver2.major);
        if (cmp != 0) return cmp;
        
        // Comparar minor
        cmp = Integer.compare(semver1.minor, semver2.minor);
        if (cmp != 0) return cmp;
        
        // Comparar patch
        cmp = Integer.compare(semver1.patch, semver2.patch);
        if (cmp != 0) return cmp;
        
        // Si hay pre-release, la versión con pre-release es menor
        if (semver1.preRelease != null && semver2.preRelease == null) {
            return -1;
        }
        if (semver1.preRelease == null && semver2.preRelease != null) {
            return 1;
        }
        
        return 0;
    }
    
    /**
     * Clase interna para representar versión SemVer
     */
    public static class SemVer {
        public final int major;
        public final int minor;
        public final int patch;
        public final String preRelease;
        public final String buildMetadata;
        
        public SemVer(int major, int minor, int patch, String preRelease, String buildMetadata) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
            this.preRelease = preRelease;
            this.buildMetadata = buildMetadata;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(major).append(".").append(minor).append(".").append(patch);
            if (preRelease != null) {
                sb.append("-").append(preRelease);
            }
            if (buildMetadata != null) {
                sb.append("+").append(buildMetadata);
            }
            return sb.toString();
        }
    }
}
```

### 3. Modificar BusinessServices para Usar Validación

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/business/models/ModelVersionBusinessService.java`

```java
// Agregar import
import com.codeflowx.govern.util.SemVerUtil;

// Modificar método createVersion()
public ModelVersion createVersion(ModelVersion version, Long userId) {
    // Validar formato SemVer
    if (version.getModversion() != null && !version.getModversion().trim().isEmpty()) {
        SemVerUtil.validate(version.getModversion());
    } else {
        // Auto-calcular si no se proporciona
        String nextVersion = calculateNextVersion(version.getModel().getIdxmodel());
        version.setModversion(nextVersion);
    }
    
    // ... resto del código existente ...
}

/**
 * Calcula siguiente versión automáticamente
 */
private String calculateNextVersion(Long modelId) {
    List<ModelVersion> versions = modelVersionDao.findByModelIdOrderByVersionDesc(modelId);
    if (versions.isEmpty()) {
        return "1.0.0";
    }
    
    String lastVersion = versions.get(0).getModversion();
    return SemVerUtil.nextPatch(lastVersion);
}
```

**Aplicar misma lógica a:**
- `PromptVersionBusinessService.java`
- `RagVersionBusinessService.java`
- `DatasetVersionBusinessService.java`
- `ModelEvaluationBusinessService.java`

### 4. Crear Exception Personalizada

**Archivo:** `nocode.service.business/src/main/java/com/codeflowx/govern/exception/InvalidVersionFormatException.java`

```java
package com.codeflowx.govern.exception;

public class InvalidVersionFormatException extends IllegalArgumentException {
    public InvalidVersionFormatException(String message) {
        super(message);
    }
    
    public InvalidVersionFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### 5. Modificar SemVerUtil para Usar Exception Personalizada

```java
// En SemVerUtil.validate()
public static void validate(String version) {
    if (!isValid(version)) {
        throw new InvalidVersionFormatException(
            "Versión debe seguir formato SemVer (X.Y.Z[-pre-release][+build-metadata]). " +
            "Recibido: " + version
        );
    }
}
```

---

## VALIDACIONES ADICIONALES RECOMENDADAS

1. **Validación en frontend** antes de enviar al backend
2. **Ordenamiento automático** de versiones usando comparación SemVer
3. **API endpoint** para calcular siguiente versión automáticamente
4. **Dashboard de versiones** ordenado por SemVer

---

## PRUEBAS REQUERIDAS

1. **Test 1:** Validar versión válida "1.2.3" → Debe pasar
2. **Test 2:** Validar versión inválida "v1" → Debe lanzar excepción
3. **Test 3:** Calcular siguiente patch "1.2.3" → Debe retornar "1.2.4"
4. **Test 4:** Calcular siguiente minor "1.2.3" → Debe retornar "1.3.0"
5. **Test 5:** Calcular siguiente major "1.2.3" → Debe retornar "2.0.0"
6. **Test 6:** Comparar versiones "1.2.3" vs "1.2.4" → Debe retornar negativo
7. **Test 7:** Crear versión sin especificar → Debe auto-calcular "1.0.0"
8. **Test 8:** Intentar guardar versión inválida en BD → Debe fallar constraint

---

## LOGS INMUTABLES

Añadir logs cuando se detecta versión inválida:

```java
// En createVersion() cuando falla validación
loggingService.logChange(
    "MODEL",
    modelId,
    "VALIDATION_FAILED_INVALID_VERSION",
    userId,
    getUserName(userId),
    Map.of(
        "invalid_version", version.getModversion(),
        "reason", "Versión no sigue formato SemVer"
    )
);
```

---

## REFERENCIAS

- **SemVer Specification:** https://semver.org/
- **Art. 11 EU AI Act:** Documentación Técnica
- **Incidencia Original:** `/docs/compliance/auditoria/INCIDENCIAS_RECOMENDACIONES_VERSIONADO.md#inc-011-04`
- **Auditoría:** `/docs/compliance/auditoria/AUDITORIA_011_VERSIONADO_CONTROL_CAMBIOS.md`

---

## NOTAS DE IMPLEMENTACIÓN

- La función `normalize_version()` en PostgreSQL es temporal y debe ejecutarse solo durante migración
- Considerar usar librería externa de SemVer (ej: `com.github.zafarkhaja:java-semver`) en lugar de implementación propia
- Los constraints en BD pueden ser estrictos; considerar hacerlos más permisivos si hay casos especiales
- La migración de datos puede requerir revisión manual de versiones complejas
- Considerar agregar campo `VERSIONSOURCE` (MANUAL, AUTO_CALCULATED) para tracking

---

**Estado:** ✅ COMPLETADO  
**Esfuerzo Estimado:** 2.5 días  
**Responsable:** Backend Team + DBA Team

