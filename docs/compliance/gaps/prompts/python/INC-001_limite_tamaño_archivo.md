# PROMPT: INC-001 - Límite de Tamaño de Archivo Insuficiente

**Incidencia:** INC-001  
**Prioridad:** 🔴 CRITICAL  
**Artículo EU AI Act:** Art. 10.1.a (datos representativos)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Python - Microservicio

---

## CONTEXTO

El límite actual de 100 MB por archivo CSV es insuficiente para datasets enterprise que requieren mayor volumen de datos para mantener representatividad estadística.

**Ubicación Actual:**
- `bias-detection-service/main.py:311` - Límite hardcodeado a 100 MB
- `bias-detection-service/README.md:318` - Variable `MAX_FILE_SIZE_MB=100`
- `bias-detection-service/docker-compose.yml:13` - Variable de entorno

---

## REQUISITOS

1. Aumentar límite configurable a 500 MB - 1 GB para casos enterprise
2. Validar memoria disponible antes de procesar
3. Mantener límite de 100 MB como default para compatibilidad
4. Añadir validación de memoria del sistema
5. Actualizar documentación y variables de entorno

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Modificar `bias-detection-service/main.py`

**Ubicación:** `bias-detection-service/main.py`

**Cambios:**

```python
# Línea ~307-315
# ANTES:
if file_size_mb > 100:
    raise HTTPException(
        status_code=413,
        detail=f"File too large ({file_size_mb:.1f}MB). Maximum size is 100MB"
    )

# DESPUÉS:
import os
import psutil

# Obtener límite configurable (default 100 MB, max 1024 MB)
MAX_FILE_SIZE_MB = int(os.getenv('MAX_FILE_SIZE_MB', '100'))
MAX_FILE_SIZE_MB = min(MAX_FILE_SIZE_MB, 1024)  # Cap a 1 GB

# Validar memoria disponible
available_memory_gb = psutil.virtual_memory().available / (1024**3)
required_memory_gb = (file_size_mb / 1024) * 3  # 3x el tamaño del archivo

if file_size_mb > MAX_FILE_SIZE_MB:
    raise HTTPException(
        status_code=413,
        detail=f"File too large ({file_size_mb:.1f}MB). Maximum size is {MAX_FILE_SIZE_MB}MB. "
               f"Configure MAX_FILE_SIZE_MB environment variable to increase limit."
    )

if required_memory_gb > available_memory_gb:
    raise HTTPException(
        status_code=507,  # Insufficient Storage
        detail=f"Insufficient memory. Required: {required_memory_gb:.2f}GB, "
               f"Available: {available_memory_gb:.2f}GB. "
               f"File size: {file_size_mb:.1f}MB"
    )
```

### 2. Actualizar `bias-detection-service/README.md`

**Sección Environment Variables:**

```markdown
| Variable | Description | Default | Max |
|----------|-------------|---------|-----|
| `MAX_FILE_SIZE_MB` | Max CSV size | 100 | 1024 |
| `TIMEOUT_SECONDS` | Analysis timeout | 30 | - |
```

**Añadir nota:**
```markdown
**Nota:** Para datasets enterprise grandes, aumentar `MAX_FILE_SIZE_MB` hasta 1024 MB (1 GB).
El sistema validará automáticamente que hay suficiente memoria disponible antes de procesar.
```

### 3. Actualizar `bias-detection-service/docker-compose.yml`

```yaml
environment:
  - MAX_FILE_SIZE_MB=500  # Aumentado para casos enterprise
  - TIMEOUT_SECONDS=30
```

### 4. Añadir dependencia `psutil` en `requirements.txt`

```txt
psutil>=5.9.0  # Para validación de memoria
```

### 5. Actualizar endpoint `/api/data-quality/validate`

Aplicar los mismos cambios al endpoint de validación de calidad de datos.

---

## VALIDACIONES

1. ✅ Archivo de 100 MB debe procesarse correctamente (compatibilidad)
2. ✅ Archivo de 500 MB debe procesarse si `MAX_FILE_SIZE_MB=500`
3. ✅ Archivo de 1500 MB debe rechazarse incluso con `MAX_FILE_SIZE_MB=2000` (cap a 1 GB)
4. ✅ Archivo que requiere más memoria de la disponible debe rechazarse con error 507
5. ✅ Mensaje de error debe indicar cómo configurar el límite

---

## TESTING

**Tests a crear:**

```python
# tests/test_file_size_limits.py

def test_default_limit_100mb():
    """Test que el límite por defecto es 100 MB"""
    # Subir archivo de 101 MB debe fallar
    pass

def test_configurable_limit_500mb():
    """Test que se puede configurar hasta 500 MB"""
    os.environ['MAX_FILE_SIZE_MB'] = '500'
    # Subir archivo de 450 MB debe funcionar
    pass

def test_max_cap_1gb():
    """Test que no se puede configurar más de 1 GB"""
    os.environ['MAX_FILE_SIZE_MB'] = '2000'
    # Debe usar 1024 MB como máximo
    pass

def test_insufficient_memory():
    """Test que rechaza si no hay memoria suficiente"""
    # Mock psutil para simular memoria insuficiente
    pass
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Variables de entorno
- `bias-detection-service/INTEGRATION_GUIDE.md` - Configuración enterprise
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Actualizar límite máximo

---

## CUMPLIMIENTO EU AI ACT

**Art. 10.1.a:** Datos relevantes, representativos y libres de errores
- ✅ Permite evaluar datasets enterprise grandes que mantienen representatividad
- ✅ Valida memoria disponible para evitar errores de procesamiento
- ✅ Mantiene calidad de evaluación incluso con datasets grandes

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** MLOps Team

