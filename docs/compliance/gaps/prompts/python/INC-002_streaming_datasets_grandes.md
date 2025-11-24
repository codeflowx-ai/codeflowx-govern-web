# PROMPT: INC-002 - Streaming para Datasets Muy Grandes

**Incidencia:** INC-002  
**Prioridad:** 🔴 CRITICAL  
**Artículo EU AI Act:** Art. 10.1.a (datos representativos)  
**Esfuerzo Estimado:** 5-7 días  
**Tipo:** Python - Microservicio

---

## CONTEXTO

El sistema actual carga el dataset completo en memoria antes de procesarlo, lo que limita el tamaño máximo procesable y puede causar OOM (Out of Memory) errors.

**Ubicación Actual:**
- `bias-detection-service/main.py:319` - `pd.read_csv(StringIO(contents.decode('utf-8')))` carga todo en memoria
- `bias-detection-service/services/data_quality_service.py` - Procesa DataFrame completo

---

## REQUISITOS

1. Implementar procesamiento por chunks/streaming para análisis incremental
2. Mantener precisión estadística con muestreo estratificado si es necesario
3. Procesar datasets > 1 GB sin cargar todo en memoria
4. Mostrar progreso de evaluación en tiempo real
5. Mantener compatibilidad con datasets pequeños (< 100 MB)

---

## IMPLEMENTACIÓN REQUERIDA

### 1. Crear nuevo servicio `StreamingDataQualityService`

**Archivo:** `bias-detection-service/services/streaming_data_quality_service.py`

```python
"""
Streaming Data Quality Service
Procesa datasets grandes por chunks manteniendo precisión estadística
"""
import pandas as pd
import numpy as np
from typing import Dict, List, Optional, Iterator
from io import StringIO
import logging
from collections import defaultdict

logger = logging.getLogger(__name__)


class StreamingDataQualityService:
    """
    Service para evaluar calidad de datos usando procesamiento por chunks
    """
    
    def __init__(self, chunk_size: int = 10000):
        """
        Args:
            chunk_size: Número de filas por chunk (default: 10,000)
        """
        self.chunk_size = chunk_size
        self.quality_thresholds = {
            "completeness": 0.95,
            "duplicate_threshold": 0.05,
            "outlier_threshold": 0.05
        }
    
    def evaluate_data_quality_streaming(
        self,
        file_content: bytes,
        target_column: Optional[str] = None,
        numerical_features: Optional[List[str]] = None,
        categorical_features: Optional[List[str]] = None
    ) -> Dict:
        """
        Evalúa calidad de datos procesando por chunks
        
        Args:
            file_content: Contenido del archivo CSV en bytes
            target_column: Columna target (si aplica)
            numerical_features: Lista de features numéricos
            categorical_features: Lista de features categóricos
            
        Returns:
            Dictionary con métricas de calidad agregadas
        """
        logger.info("Starting streaming data quality evaluation")
        
        # Inicializar acumuladores
        accumulators = self._initialize_accumulators()
        total_rows = 0
        
        # Procesar por chunks
        chunk_iterator = self._read_csv_chunks(file_content)
        
        for chunk_num, chunk_df in enumerate(chunk_iterator):
            total_rows += len(chunk_df)
            logger.info(f"Processing chunk {chunk_num + 1}: {len(chunk_df)} rows (total: {total_rows})")
            
            # Auto-detect feature types en primer chunk
            if chunk_num == 0:
                if numerical_features is None:
                    numerical_features = chunk_df.select_dtypes(include=[np.number]).columns.tolist()
                if categorical_features is None:
                    categorical_features = chunk_df.select_dtypes(include=['object', 'category']).columns.tolist()
            
            # Procesar chunk
            chunk_results = self._process_chunk(
                chunk_df,
                numerical_features,
                categorical_features,
                target_column
            )
            
            # Acumular resultados
            self._accumulate_results(accumulators, chunk_results, len(chunk_df))
        
        # Calcular métricas finales agregadas
        final_results = self._calculate_final_metrics(accumulators, total_rows)
        
        logger.info(f"Streaming evaluation completed: {total_rows} rows processed")
        return final_results
    
    def _read_csv_chunks(self, file_content: bytes) -> Iterator[pd.DataFrame]:
        """
        Lee CSV por chunks usando pandas chunking
        """
        string_content = file_content.decode('utf-8')
        string_io = StringIO(string_content)
        
        # Leer header primero
        header = string_io.readline()
        
        # Leer por chunks
        chunk_num = 0
        while True:
            chunk_lines = []
            for _ in range(self.chunk_size):
                line = string_io.readline()
                if not line:
                    break
                chunk_lines.append(line)
            
            if not chunk_lines:
                break
            
            # Crear DataFrame del chunk
            chunk_df = pd.read_csv(
                StringIO(header + ''.join(chunk_lines)),
                dtype=str  # Leer todo como string inicialmente para eficiencia
            )
            
            # Convertir tipos numéricos
            chunk_df = self._convert_types(chunk_df)
            
            yield chunk_df
            chunk_num += 1
    
    def _convert_types(self, df: pd.DataFrame) -> pd.DataFrame:
        """Convierte tipos de datos automáticamente"""
        for col in df.columns:
            try:
                # Intentar convertir a numérico
                df[col] = pd.to_numeric(df[col], errors='ignore')
            except:
                pass
        return df
    
    def _initialize_accumulators(self) -> Dict:
        """Inicializa acumuladores para métricas"""
        return {
            "missing_counts": defaultdict(int),
            "duplicate_count": 0,
            "outlier_counts": defaultdict(int),
            "value_counts": defaultdict(lambda: defaultdict(int)),
            "statistics": defaultdict(list),
            "total_rows": 0
        }
    
    def _process_chunk(
        self,
        chunk_df: pd.DataFrame,
        numerical_features: List[str],
        categorical_features: List[str],
        target_column: Optional[str]
    ) -> Dict:
        """Procesa un chunk y retorna resultados parciales"""
        results = {
            "missing_counts": {},
            "duplicate_count": chunk_df.duplicated().sum(),
            "outlier_counts": {},
            "value_counts": {},
            "statistics": {}
        }
        
        # Missing values
        for col in chunk_df.columns:
            missing_count = chunk_df[col].isna().sum()
            if missing_count > 0:
                results["missing_counts"][col] = missing_count
        
        # Outliers (solo para numéricos)
        for col in numerical_features:
            if col in chunk_df.columns:
                Q1 = chunk_df[col].quantile(0.25)
                Q3 = chunk_df[col].quantile(0.75)
                IQR = Q3 - Q1
                if IQR > 0:
                    lower_bound = Q1 - 1.5 * IQR
                    upper_bound = Q3 + 1.5 * IQR
                    outliers = ((chunk_df[col] < lower_bound) | (chunk_df[col] > upper_bound)).sum()
                    if outliers > 0:
                        results["outlier_counts"][col] = outliers
        
        # Estadísticas (para numéricos)
        for col in numerical_features:
            if col in chunk_df.columns:
                results["statistics"][col] = {
                    "mean": chunk_df[col].mean(),
                    "std": chunk_df[col].std(),
                    "min": chunk_df[col].min(),
                    "max": chunk_df[col].max()
                }
        
        # Value counts (para categóricos) - muestrear si hay muchos valores únicos
        for col in categorical_features:
            if col in chunk_df.columns:
                value_counts = chunk_df[col].value_counts().to_dict()
                results["value_counts"][col] = value_counts
        
        return results
    
    def _accumulate_results(self, accumulators: Dict, chunk_results: Dict, chunk_size: int):
        """Acumula resultados de un chunk"""
        accumulators["total_rows"] += chunk_size
        accumulators["duplicate_count"] += chunk_results["duplicate_count"]
        
        # Missing counts
        for col, count in chunk_results["missing_counts"].items():
            accumulators["missing_counts"][col] += count
        
        # Outlier counts
        for col, count in chunk_results["outlier_counts"].items():
            accumulators["outlier_counts"][col] += count
        
        # Statistics (acumular para cálculo final)
        for col, stats in chunk_results["statistics"].items():
            if col not in accumulators["statistics"]:
                accumulators["statistics"][col] = []
            accumulators["statistics"][col].append(stats)
        
        # Value counts
        for col, value_counts in chunk_results["value_counts"].items():
            for value, count in value_counts.items():
                accumulators["value_counts"][col][value] += count
    
    def _calculate_final_metrics(self, accumulators: Dict, total_rows: int) -> Dict:
        """Calcula métricas finales agregadas"""
        results = {
            "validation_date": pd.Timestamp.now().isoformat(),
            "n_samples": total_rows,
            "issues": [],
            "quality_score": 0
        }
        
        # Completitud
        missing_analysis = {}
        completeness_scores = []
        
        for col, missing_count in accumulators["missing_counts"].items():
            missing_pct = (missing_count / total_rows) * 100
            completeness = 1 - (missing_count / total_rows)
            missing_analysis[col] = {
                "missing_count": missing_count,
                "missing_percentage": missing_pct,
                "completeness": completeness
            }
            completeness_scores.append(completeness)
            
            if missing_pct > 5:
                results["issues"].append({
                    "severity": "HIGH",
                    "category": "Missing Values",
                    "message": f"{col} has {missing_pct:.2f}% missing values"
                })
        
        results["missing_values"] = missing_analysis
        results["completeness_score"] = np.mean(completeness_scores) if completeness_scores else 1.0
        
        # Duplicados
        duplicate_pct = (accumulators["duplicate_count"] / total_rows) * 100
        results["duplicates"] = {
            "n_duplicates": accumulators["duplicate_count"],
            "duplicate_percentage": duplicate_pct
        }
        
        if duplicate_pct > self.quality_thresholds["duplicate_threshold"] * 100:
            results["issues"].append({
                "severity": "MEDIUM",
                "category": "Duplicates",
                "message": f"{accumulators['duplicate_count']} duplicate records found ({duplicate_pct:.2f}%)"
            })
        
        # Outliers
        outlier_analysis = {}
        for col, outlier_count in accumulators["outlier_counts"].items():
            outlier_pct = (outlier_count / total_rows) * 100
            outlier_analysis[col] = {
                "outlier_count": outlier_count,
                "outlier_percentage": outlier_pct
            }
        
        results["outliers"] = outlier_analysis
        
        # Calcular score global
        results["quality_score"] = self._calculate_quality_score(results)
        
        return results
    
    def _calculate_quality_score(self, results: Dict) -> float:
        """Calcula score global de calidad (0-100)"""
        scores = []
        
        # Completitud (peso 30%)
        if "completeness_score" in results:
            scores.append(results["completeness_score"] * 0.30)
        
        # Duplicados (peso 20%)
        if "duplicates" in results:
            duplicate_score = max(0, 1 - (results["duplicates"]["duplicate_percentage"] / 100))
            scores.append(duplicate_score * 0.20)
        
        # Outliers (peso 20%)
        if "outliers" in results and results["outliers"]:
            outlier_scores = []
            for col_data in results["outliers"].values():
                outlier_score = max(0, 1 - (col_data["outlier_percentage"] / 100))
                outlier_scores.append(outlier_score)
            if outlier_scores:
                scores.append(np.mean(outlier_scores) * 0.20)
        
        # Sin issues críticos (peso 30%)
        critical_issues = sum(1 for issue in results.get("issues", []) if issue["severity"] == "HIGH")
        issue_score = max(0, 1 - (critical_issues * 0.1))
        scores.append(issue_score * 0.30)
        
        return sum(scores) * 100
```

### 2. Modificar `bias-detection-service/main.py`

**Añadir endpoint con streaming:**

```python
from services.streaming_data_quality_service import StreamingDataQualityService

# Inicializar servicio
streaming_service = StreamingDataQualityService(chunk_size=10000)

@app.post("/api/data-quality/validate-streaming")
async def validate_data_quality_streaming(
    file: UploadFile = File(...),
    target_column: Optional[str] = Form(None),
    numerical_features: Optional[str] = Form(None),
    categorical_features: Optional[str] = Form(None),
    chunk_size: int = Form(10000)
):
    """
    Validación de calidad de datos usando procesamiento por chunks (streaming)
    Para datasets grandes (> 100 MB)
    """
    try:
        # Leer archivo
        contents = await file.read()
        file_size_mb = len(contents) / (1024 * 1024)
        
        logger.info(f"Starting streaming validation for {file_size_mb:.1f}MB file")
        
        # Parse feature lists
        num_feats = [f.strip() for f in numerical_features.split(',')] if numerical_features else None
        cat_feats = [f.strip() for f in categorical_features.split(',')] if categorical_features else None
        
        # Procesar con streaming
        streaming_service.chunk_size = chunk_size
        result = streaming_service.evaluate_data_quality_streaming(
            file_content=contents,
            target_column=target_column,
            numerical_features=num_feats,
            categorical_features=cat_feats
        )
        
        return result
        
    except Exception as e:
        logger.error(f"Error in streaming validation: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Streaming validation failed: {str(e)}")
```

### 3. Modificar endpoint existente para usar streaming automáticamente

```python
@app.post("/api/data-quality/validate")
async def validate_data_quality(
    file: UploadFile = File(...),
    ...
):
    """
    Validación de calidad - usa streaming automáticamente si archivo > 100 MB
    """
    contents = await file.read()
    file_size_mb = len(contents) / (1024 * 1024)
    
    # Usar streaming si archivo es grande
    if file_size_mb > 100:
        logger.info(f"File size {file_size_mb:.1f}MB > 100MB, using streaming mode")
        return await validate_data_quality_streaming(
            file=file,
            target_column=target_column,
            ...
        )
    else:
        # Usar método tradicional para archivos pequeños
        return await validate_data_quality_traditional(file, ...)
```

---

## VALIDACIONES

1. ✅ Dataset de 50 MB debe procesarse con método tradicional (compatibilidad)
2. ✅ Dataset de 500 MB debe procesarse con streaming sin OOM
3. ✅ Dataset de 2 GB debe procesarse con streaming
4. ✅ Métricas agregadas deben ser estadísticamente equivalentes a procesamiento completo
5. ✅ Progreso debe mostrarse en logs

---

## TESTING

```python
# tests/test_streaming_quality.py

def test_streaming_small_file():
    """Test que archivos pequeños funcionan igual"""
    pass

def test_streaming_large_file():
    """Test que archivos grandes se procesan sin OOM"""
    pass

def test_streaming_metrics_accuracy():
    """Test que métricas streaming son equivalentes a procesamiento completo"""
    pass

def test_streaming_chunk_size_configurable():
    """Test que chunk_size es configurable"""
    pass
```

---

## DOCUMENTACIÓN

Actualizar:
- `bias-detection-service/README.md` - Nuevo endpoint streaming
- `bias-detection-service/INTEGRATION_GUIDE.md` - Cuándo usar streaming
- `docs/compliance/auditoria/AUDITORIA_EVALUACION_DATASETS.md` - Capacidad streaming

---

## CUMPLIMIENTO EU AI ACT

**Art. 10.1.a:** Datos relevantes, representativos y libres de errores
- ✅ Permite evaluar datasets enterprise muy grandes
- ✅ Mantiene precisión estadística con procesamiento incremental
- ✅ Evita errores de memoria que afectarían calidad de evaluación

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0  
**Responsable:** MLOps Team

