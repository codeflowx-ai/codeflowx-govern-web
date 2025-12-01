# SparkEvaluationService

**Ubicación:** `com.codeflowx.govern.business.integrations.SparkEvaluationService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Servicio para enviar jobs de evaluación (bias/quality) a Spark vía Livy sin mover datos. Ejecuta evaluaciones distribuidas sobre datasets remotos (S3/HDFS).

---

## 🎯 Responsabilidades

- Enviar scripts PySpark a Livy para evaluación de sesgo
- Ejecutar evaluaciones de calidad sobre datasets remotos
- Obtener resultados sin copiar datos

---

## 📚 API Pública

### `submitBiasEvaluation(Long platformId, String datasetPath, List<String> protectedAttributes)`

Envía un script PySpark a Livy para evaluar sesgo sobre un dataset remoto.

**Parámetros:**
- `platformId`: ID de la plataforma externa configurada como SPARK
- `datasetPath`: Ruta del dataset (S3, HDFS, etc.)
- `protectedAttributes`: Lista de atributos protegidos a evaluar

**Retorna:** `SparkEvaluationResult` con:
- `outputPath`: Ruta donde se guardaron los resultados
- `results`: JSON con resultados de la evaluación

**Ejemplo:**
```java
List<String> protectedAttrs = List.of("gender", "age");
SparkEvaluationResult result = sparkService.submitBiasEvaluation(
    platformId, "s3://bucket/dataset", protectedAttrs);
```

---

## ⚙️ Configuración

**Credenciales:** Se obtienen de `ExternalPlatformIntegration.eplmetadata`:
- `livyUrl`: URL del servidor Livy
- `resultsPath`: Ruta donde guardar resultados (S3, HDFS)

---

## 📖 Referencias

- **Apache Livy:** https://livy.apache.org/
- **Integración:** Usado para evaluaciones distribuidas

---

**Última actualización:** 25 de noviembre de 2025
