# PROMPT: INC-004 - Timeout Adaptativo

**Incidencia:** INC-004  
**Prioridad:** 🟠 HIGH  
**Artículo EU AI Act:** Art. 10.1.a (calidad de evaluación)  
**Esfuerzo Estimado:** 2-3 días  
**Tipo:** Python - Microservicio

---

## CONTEXTO

El timeout de 30 segundos es fijo y no se adapta al tamaño del dataset, lo que puede causar evaluaciones incompletas para datasets medianos-grandes.

**Ubicación Actual:**
- `bias-detection-service/README.md:319` - `TIMEOUT_SECONDS=30` (fijo)
- No hay cálculo dinámico basado en tamaño de dataset

---

## REQUISITOS

1. Calcular timeout basado en tamaño de dataset: `timeout = max(30, dataset_size_mb * 2)`
2. Implementar retry con backoff exponencial
3. Mostrar progreso de evaluación en tiempo real
4. Configurar timeout máximo (ej: 10 minutos)
5. Logging de tiempo de ejecución por etapa

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear utilidad `AdaptiveTimeout`

**Archivo:** `bias-detection-service/utils/adaptive_timeout.py`

```python
"""
Adaptive Timeout Utility
Calcula timeout dinámico basado en tamaño de dataset
"""
import os
import logging
from typing import Optional

logger = logging.getLogger(__name__)


class AdaptiveTimeout:
    """
    Calcula timeout adaptativo basado en tamaño de dataset
    """
    
    def __init__(
        self,
        base_timeout: int = 30,
        timeout_per_mb: float = 2.0,
        max_timeout: int = 600,  # 10 minutos
        min_timeout: int = 30
    ):
        """
        Args:
            base_timeout: Timeout base en segundos (default: 30)
            timeout_per_mb: Segundos adicionales por MB (default: 2.0)
            max_timeout: Timeout máximo en segundos (default: 600)
            min_timeout: Timeout mínimo en segundos (default: 30)
        """
        self.base_timeout = base_timeout
        self.timeout_per_mb = timeout_per_mb
        self.max_timeout = max_timeout
        self.min_timeout = min_timeout
    
    def calculate_timeout(self, file_size_mb: float) -> int:
        """
        Calcula timeout basado en tamaño de archivo
        
        Args:
            file_size_mb: Tamaño del archivo en MB
            
        Returns:
            Timeout en segundos
        """
        # Fórmula: base + (tamaño * factor) con límites
        calculated_timeout = int(self.base_timeout + (file_size_mb * self.timeout_per_mb))
        
        # Aplicar límites
        timeout = max(self.min_timeout, min(calculated_timeout, self.max_timeout))
        
        logger.info(
            f"Calculated timeout: {timeout}s for file size: {file_size_mb:.1f}MB "
            f"(base: {self.base_timeout}s, per MB: {self.timeout_per_mb}s)"
        )
        
        return timeout
    
    @classmethod
    def from_env(cls) -> 'AdaptiveTimeout':
        """Crea instancia desde variables de entorno"""
        base_timeout = int(os.getenv('BASE_TIMEOUT_SECONDS', '30'))
        timeout_per_mb = float(os.getenv('TIMEOUT_PER_MB', '2.0'))
        max_timeout = int(os.getenv('MAX_TIMEOUT_SECONDS', '600'))
        min_timeout = int(os.getenv('MIN_TIMEOUT_SECONDS', '30'))
        
        return cls(
            base_timeout=base_timeout,
            timeout_per_mb=timeout_per_mb,
            max_timeout=max_timeout,
            min_timeout=min_timeout
        )
```

### 2. Crear decorador de timeout con retry

**Archivo:** `bias-detection-service/utils/timeout_retry.py`

```python
"""
Timeout and Retry Decorator
Implementa timeout adaptativo con retry y backoff exponencial
"""
import asyncio
import logging
from functools import wraps
from typing import Callable, Any
import time

logger = logging.getLogger(__name__)


def adaptive_timeout_with_retry(
    timeout_calculator: Callable[[float], int],
    max_retries: int = 3,
    backoff_factor: float = 2.0
):
    """
    Decorador que aplica timeout adaptativo con retry y backoff exponencial
    
    Args:
        timeout_calculator: Función que calcula timeout basado en file_size_mb
        max_retries: Número máximo de reintentos
        backoff_factor: Factor de backoff exponencial
    """
    def decorator(func: Callable) -> Callable:
        @wraps(func)
        async def wrapper(*args, **kwargs):
            # Obtener file_size_mb de kwargs o args
            file_size_mb = kwargs.get('file_size_mb', 0)
            if not file_size_mb and args:
                # Intentar obtener de primer argumento si es UploadFile
                try:
                    file = args[0] if args else kwargs.get('file')
                    if hasattr(file, 'size'):
                        file_size_mb = file.size / (1024 * 1024)
                except:
                    pass
            
            # Calcular timeout
            timeout = timeout_calculator(file_size_mb)
            
            # Intentar con retry
            last_exception = None
            for attempt in range(max_retries + 1):
                try:
                    start_time = time.time()
                    
                    # Ejecutar función con timeout
                    result = await asyncio.wait_for(
                        func(*args, **kwargs),
                        timeout=timeout
                    )
                    
                    execution_time = time.time() - start_time
                    logger.info(
                        f"Function {func.__name__} completed in {execution_time:.2f}s "
                        f"(timeout: {timeout}s, attempt: {attempt + 1})"
                    )
                    
                    return result
                    
                except asyncio.TimeoutError:
                    execution_time = time.time() - start_time
                    last_exception = asyncio.TimeoutError(
                        f"Function {func.__name__} timed out after {execution_time:.2f}s "
                        f"(timeout: {timeout}s, attempt: {attempt + 1}/{max_retries + 1})"
                    )
                    
                    if attempt < max_retries:
                        # Calcular backoff
                        backoff_time = timeout * (backoff_factor ** attempt)
                        logger.warning(
                            f"Timeout on attempt {attempt + 1}, retrying in {backoff_time:.2f}s "
                            f"(new timeout: {timeout * backoff_factor:.0f}s)"
                        )
                        
                        # Aumentar timeout para siguiente intento
                        timeout = int(timeout * backoff_factor)
                        await asyncio.sleep(min(backoff_time, 60))  # Cap backoff a 60s
                    else:
                        logger.error(
                            f"Function {func.__name__} failed after {max_retries + 1} attempts"
                        )
                        raise last_exception
                        
                except Exception as e:
                    logger.error(f"Error in {func.__name__}: {str(e)}")
                    raise
            
            raise last_exception
        
        return wrapper
    return decorator
```

### 3. Modificar endpoints para usar timeout adaptativo

**Archivo:** `bias-detection-service/main.py`

```python
from utils.adaptive_timeout import AdaptiveTimeout
from utils.timeout_retry import adaptive_timeout_with_retry

# Inicializar timeout calculator
timeout_calculator = AdaptiveTimeout.from_env()

@app.post("/api/data-quality/validate")
@adaptive_timeout_with_retry(
    timeout_calculator=lambda file_size_mb: timeout_calculator.calculate_timeout(file_size_mb),
    max_retries=2,
    backoff_factor=1.5
)
async def validate_data_quality(
    file: UploadFile = File(...),
    target_column: Optional[str] = Form(None),
    ...
):
    """
    Validación de calidad con timeout adaptativo
    """
    try:
        # Leer archivo para calcular tamaño
        contents = await file.read()
        file_size_mb = len(contents) / (1024 * 1024)
        
        # Calcular timeout
        timeout = timeout_calculator.calculate_timeout(file_size_mb)
        logger.info(f"Starting validation with adaptive timeout: {timeout}s for {file_size_mb:.1f}MB file")
        
        # Procesar (el decorador maneja el timeout)
        result = await process_validation(contents, target_column, ...)
        
        return result
        
    except asyncio.TimeoutError as e:
        logger.error(f"Validation timed out: {str(e)}")
        raise HTTPException(
            status_code=504,
            detail=f"Validation timed out after {timeout}s. "
                   f"Try increasing MAX_TIMEOUT_SECONDS or reducing file size."
        )
    except Exception as e:
        logger.error(f"Error in validation: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Validation failed: {str(e)}")
```

### 4. Añadir progreso en tiempo real

**Archivo:** `bias-detection-service/utils/progress_tracker.py`

```python
"""
Progress Tracker
Rastrea progreso de evaluación en tiempo real
"""
import time
import logging
from typing import Dict, Optional

logger = logging.getLogger(__name__)


class ProgressTracker:
    """Rastrea progreso de evaluación"""
    
    def __init__(self, total_steps: int = 5):
        self.total_steps = total_steps
        self.current_step = 0
        self.start_time = time.time()
        self.step_times = {}
    
    def start_step(self, step_name: str):
        """Inicia un paso"""
        self.current_step += 1
        step_start = time.time()
        self.step_times[step_name] = {
            "start": step_start,
            "step_number": self.current_step
        }
        logger.info(f"[{self.current_step}/{self.total_steps}] Starting: {step_name}")
    
    def end_step(self, step_name: str):
        """Termina un paso"""
        if step_name in self.step_times:
            elapsed = time.time() - self.step_times[step_name]["start"]
            total_elapsed = time.time() - self.start_time
            self.step_times[step_name]["elapsed"] = elapsed
            logger.info(
                f"[{self.current_step}/{self.total_steps}] Completed: {step_name} "
                f"({elapsed:.2f}s, total: {total_elapsed:.2f}s)"
            )
    
    def get_progress(self) -> Dict:
        """Retorna progreso actual"""
        total_elapsed = time.time() - self.start_time
        progress_pct = (self.current_step / self.total_steps) * 100
        
        return {
            "current_step": self.current_step,
            "total_steps": self.total_steps,
            "progress_percentage": progress_pct,
            "elapsed_time": total_elapsed,
            "estimated_remaining": (total_elapsed / self.current_step * (self.total_steps - self.current_step)) if self.current_step > 0 else None
        }
```

**Uso en endpoints:**

```python
from utils.progress_tracker import ProgressTracker

async def validate_data_quality(...):
    progress = ProgressTracker(total_steps=5)
    
    progress.start_step("File validation")
    # Validar archivo
    progress.end_step("File validation")
    
    progress.start_step("Data profiling")
    # Profiling
    progress.end_step("Data profiling")
    
    # ... más pasos
    
    return {
        "result": result,
        "progress": progress.get_progress()
    }
```

### 5. Actualizar variables de entorno

**Archivo:** `bias-detection-service/docker-compose.yml`

```yaml
environment:
  - BASE_TIMEOUT_SECONDS=30
  - TIMEOUT_PER_MB=2.0
  - MAX_TIMEOUT_SECONDS=600
  - MIN_TIMEOUT_SECONDS=30
  - MAX_RETRIES=2
  - BACKOFF_FACTOR=1.5
```

---

## VALIDACIONES

1. ✅ Dataset de 10 MB debe usar timeout ~50s (30 + 10*2)
2. ✅ Dataset de 100 MB debe usar timeout ~230s (30 + 100*2)
3. ✅ Dataset de 500 MB debe usar timeout máximo 600s (cap)
4. ✅ Timeout debe ser mínimo 30s incluso para archivos muy pequeños
5. ✅ Retry debe aumentar timeout en cada intento
6. ✅ Progreso debe mostrarse en logs

---

## TESTING

```python
# tests/test_adaptive_timeout.py

def test_timeout_calculation_small_file():
    """Test timeout para archivo pequeño"""
    timeout_calc = AdaptiveTimeout()
    assert timeout_calc.calculate_timeout(10) == 50  # 30 + 10*2

def test_timeout_calculation_large_file():
    """Test timeout para archivo grande (debe cap a max)"""
    timeout_calc = AdaptiveTimeout(max_timeout=600)
    assert timeout_calc.calculate_timeout(500) == 600  # Cap a máximo

def test_retry_with_backoff():
    """Test que retry aumenta timeout"""
    pass

def test_progress_tracking():
    """Test que progreso se rastrea correctamente"""
    pass
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Variables de entorno de timeout
- `bias-detection-service/INTEGRATION_GUIDE.md` - Configuración de timeout
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Timeout adaptativo

---

## CUMPLIMIENTO EU AI ACT

**Art. 10.1.a:** Calidad de evaluación
- ✅ Timeout adaptativo evita evaluaciones incompletas
- ✅ Retry con backoff aumenta probabilidad de completar evaluación
- ✅ Progreso en tiempo real permite monitoreo de calidad

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** MLOps Team

