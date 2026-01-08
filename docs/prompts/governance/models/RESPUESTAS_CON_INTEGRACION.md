# Respuestas a las Preguntas - CON INTEGRACIÓN IMPLEMENTADA

**Fecha:** Enero 2025
**Estado:** Integración completada - Respuestas basadas en el código implementado

---

## 1. ¿Cómo analizamos los sesgos en modelos externos?

**RESPUESTA:** ✅ **INTEGRADO Y FUNCIONAL**

**Cómo funciona:**

1. **Usuario sube archivo CSV** con predicciones del modelo:
   ```
   y_true,y_pred,gender
   1,1,male
   0,0,female
   1,0,female
   0,1,male
   ```

2. **Endpoint:** `POST /api/v1/models/{modelId}/bias-analysis/execute`
   - Acepta `multipart/form-data` con el archivo CSV
   - Parámetros: `protectedAttribute` (ej: "gender"), `favorableOutcome`, `threshold`

3. **Flujo de ejecución:**
   ```
   Frontend → BFF → Microservicio Models → ModelBiasAnalysisBusinessService
                                                    ↓
                                          BiasDetectionClient (Puerto 8001)
                                                    ↓
                                          Microservicio Python
                                                    ↓
                                          Análisis de sesgo (demographic parity, equal opportunity)
                                                    ↓
                                          Mapear respuesta → ModelBiasAnalysis
                                                    ↓
                                          Guardar en BD
   ```

4. **Resultados:**
   - `modbiasscore`: Score calculado (0-1)
   - `modseverity`: LOW, MODERATE, HIGH
   - `modaffectedgroups`: Grupos afectados (JSONB)
   - `modmitigationstrategies`: Recomendaciones
   - `modanalysisdata`: Datos completos del análisis (JSONB)

**Implementación:**
- `ModelBiasAnalysisBusinessService.executeBiasAnalysis()` - Ejecuta el análisis
- Usa `BiasDetectionClient.analyzeBias()` del microservicio Python
- Mapea `BiasAnalysisResponse` → `ModelBiasAnalysis` automáticamente

---

## 2. ¿Cómo analizamos los sesgos en modelos locales LLMs?

**RESPUESTA:** ✅ **MISMO PROCESO QUE MODELOS EXTERNOS**

**Cómo funciona:**

1. **Para modelos locales:**
   - El proceso es idéntico
   - Solo cambia cómo se genera el CSV con predicciones

2. **Opciones para generar predicciones:**
   - **Opción A:** Usuario genera CSV manualmente desde las predicciones del modelo local
   - **Opción B:** Usar `ModelWrapperClient` para invocar el modelo y generar predicciones (futuro)
   - **Opción C:** Si el modelo está desplegado en Kubernetes, usar `ModelDeployment.baseUrl` para invocarlo

3. **Ejecución:**
   - Mismo endpoint: `POST /api/v1/models/{modelId}/bias-analysis/execute`
   - Mismo microservicio Python: `BiasDetectionClient`

**Nota:** Actualmente el CSV debe generarse externamente. La integración con `ModelWrapperClient` para generar predicciones automáticamente está pendiente.

---

## 3. ¿Cómo analizamos los sesgos en modelos MLOps?

**RESPUESTA:** ✅ **MISMO PROCESO, CON INTEGRACIÓN MLOPS**

**Cómo funciona:**

1. **Para modelos en plataformas MLOps:**
   - Los modelos están registrados en `Model` entity
   - Pueden tener `ModelDeployment` con `baseUrl` si están desplegados
   - Pueden tener `ModelMlopsIntegration` con configuración específica

2. **Obtener predicciones desde MLOps:**
   - **Opción A:** Descargar predicciones desde la plataforma MLOps (Databricks, SageMaker, etc.)
   - **Opción B:** Invocar endpoint de serving si está configurado en `ModelDeployment.baseUrl`
   - **Opción C:** Usar cliente de la plataforma MLOps (futuro)

3. **Generar CSV y ejecutar:**
   - Mismo endpoint: `POST /api/v1/models/{modelId}/bias-analysis/execute`
   - Formato CSV igual que modelos externos/locales

**Plataformas MLOps soportadas:**
- Databricks, SageMaker, Vertex AI, Azure ML, MLflow, Hugging Face, Kubeflow, Seldon, W&B
- Configuración en `ExternalPlatformIntegration`
- Credenciales en `ProviderCredential`

**Nota:** Actualmente se registran las integraciones, pero la descarga automática de predicciones desde plataformas MLOps está pendiente.

---

## 4. ¿Qué es la explicabilidad y cómo funciona en CodeflowX?

**RESPUESTA:** ✅ **INTEGRADO Y FUNCIONAL**

**Qué es:**
La explicabilidad es la capacidad de entender por qué un modelo toma ciertas decisiones. Muestra qué características (features) son más importantes para las predicciones.

**Cómo funciona en CodeflowX:**

1. **Usuario sube archivo CSV** con predicciones y características:
   ```
   feature1,feature2,feature3,prediction
   0.5,0.3,0.2,1
   0.7,0.1,0.4,0
   ```

2. **Endpoint:** `POST /api/v1/models/{modelId}/explainability/execute`
   - Acepta `multipart/form-data` con el archivo CSV
   - Parámetros: `method` (SHAP, LIME, etc.), `predictionColumn`

3. **Flujo de ejecución:**
   ```
   Frontend → BFF → Microservicio Models → ModelExplainabilityBusinessService
                                                      ↓
                                        BiasDetectionClient.explainPredictions() (Puerto 8001)
                                                      ↓
                                        Microservicio Python (SHAP/LIME)
                                                      ↓
                                        Cálculo de importancia de características
                                                      ↓
                                        Mapear respuesta → ModelExplainability
                                                      ↓
                                        Guardar en BD
   ```

4. **Resultados:**
   - `modexplainabilityscore`: Score 0-100
   - `modisexplainable`: Boolean
   - `modfeatureimportance`: Importancia de cada característica (JSONB)
   - `modexplanationquality`: EXCELLENT, GOOD, FAIR, POOR
   - `modrecommendations`: Recomendaciones

**Métodos soportados:**
- SHAP (SHapley Additive exPlanations)
- LIME (Local Interpretable Model-agnostic Explanations)
- Integrated Gradients (para redes neuronales)

**Tipo de modelo detectado automáticamente:**
- Tree (árboles de decisión, Random Forest, XGBoost)
- Linear (regresiones, SVM)
- Neural (redes neuronales, LLMs, transformers)

---

## 5. ¿Cómo probamos los modelos MLOps?

**RESPUESTA:** ⚠️ **PARCIALMENTE IMPLEMENTADO**

**Lo que SÍ funciona:**

1. **Registro de modelos MLOps:**
   - ✅ Modelos se registran en `Model` entity
   - ✅ Configuración de plataforma MLOps en `ExternalPlatformIntegration`
   - ✅ Integración específica por modelo en `ModelMlopsIntegration`

2. **Configuración de endpoints:**
   - ✅ `ModelDeployment.baseUrl` para endpoints de serving
   - ✅ `ModelDeployment.entrypointPath` para rutas específicas

3. **Clientes disponibles:**
   - ✅ `ModelWrapperClient` para invocar modelos (puerto 8006)
   - ✅ `ServingWrapperClient` para endpoints de serving (puerto 8000)

**Lo que NO está integrado (pendiente):**

1. **Ejecución automática de tests:**
   - ❌ No hay servicio que ejecute datasets de prueba automáticamente
   - ❌ No hay comparación automática de versiones
   - ❌ No hay evaluación automática de métricas

2. **Obtención de predicciones desde MLOps:**
   - ⚠️ Las integraciones MLOps están configuradas pero no hay código que:
     - Descargue modelos desde Databricks/SageMaker
     - Invoke endpoints de serving automáticamente
     - Genere CSV con predicciones desde datasets

**Cómo funciona actualmente:**
1. Modelo MLOps se registra manualmente en CodeflowX
2. Usuario descarga predicciones manualmente desde la plataforma MLOps
3. Usuario sube CSV a CodeflowX para análisis (sesgo/explicabilidad)
4. CodeflowX ejecuta análisis usando `BiasDetectionClient`

**Próximos pasos (pendiente):**
- Integrar con clientes MLOps para descargar predicciones automáticamente
- Agregar servicio de testing que ejecute datasets y compare resultados

---

## 6. ¿Qué formatos de MLOps soportamos?

**RESPUESTA:** ✅ **9 PLATAFORMAS CONFIGURABLES**

**Plataformas MLOps soportadas:**

1. **Databricks** - `DATABRICKS`
2. **SageMaker** - `SAGEMAKER`
3. **Vertex AI** - `VERTEX_AI`
4. **Azure ML** - `AZURE_ML`
5. **MLflow** - `MLFLOW`
6. **Hugging Face** - `HUGGINGFACE`
7. **Kubeflow** - `KUBEFLOW`
8. **Seldon** - `SELDON`
9. **Weights & Biases** - `WANDB`

**Implementación:**
- ✅ Entidad `ExternalPlatformIntegration` para configuración global
- ✅ Entidad `ModelMlopsIntegration` para asociación modelo-plataforma
- ✅ Integraciones Java (`*IntegrationProvider`) para cada plataforma
- ✅ Configuración de credenciales, endpoints, regiones

**Capacidades por plataforma:**
- Catalogación: ✅ Todas
- Quality: ⚠️ Pendiente para algunas
- PII: ⚠️ Pendiente para algunas
- CRUD: ⚠️ Pendiente para algunas
- Cleanup: ⚠️ Pendiente para algunas

**Estado:** Principalmente para **registro y catálogo**. La sincronización automática de modelos y descarga de predicciones está pendiente.

---

## 7. ¿Solo los registramos?

**RESPUESTA:** ⚠️ **PRINCIPALMENTE REGISTRO, PERO CON ANÁLISIS INTEGRADO**

**Lo que SÍ hacemos:**

1. **Registro completo:**
   - ✅ Registro de modelos (internos, externos, MLOps)
   - ✅ Versiones y despliegues
   - ✅ Costes y métricas
   - ✅ Configuración de integraciones MLOps

2. **Análisis integrado:**
   - ✅ Análisis de sesgo (ejecuta microservicio Python)
   - ✅ Análisis de explicabilidad (ejecuta microservicio Python)
   - ✅ Almacenamiento automático de resultados

**Lo que NO hacemos (pendiente):**

1. **Testing automatizado:**
   - ❌ No ejecutamos datasets de prueba automáticamente
   - ❌ No comparamos versiones automáticamente
   - ❌ No evaluamos modelos automáticamente

2. **Sincronización MLOps:**
   - ❌ No descargamos modelos desde plataformas MLOps
   - ❌ No sincronizamos versiones automáticamente
   - ❌ No obtenemos predicciones automáticamente

**Resumen:** Registramos TODO y ejecutamos análisis de sesgo/explicabilidad cuando el usuario sube CSV. Falta automatización de testing y sincronización MLOps.

---

## 8. En modelos externos y/o locales, ¿dónde ejecutamos el prompt para analizar o el dataset de pruebas?

**RESPUESTA:** ✅ **EJECUTAMOS EN EL MICROSERVICIO PYTHON**

**Dónde se ejecuta:**

1. **Microservicio Python `bias-detection` (puerto 8001):**
   - Recibe el CSV con predicciones
   - Ejecuta el análisis de sesgo (algoritmos de fairness)
   - Ejecuta análisis de explicabilidad (SHAP/LIME)

2. **Flujo:**
   ```
   Usuario → Frontend → BFF → Microservicio Models
                                         ↓
                          ModelBiasAnalysisBusinessService.executeBiasAnalysis()
                                         ↓
                          BiasDetectionClient.analyzeBias(csvFile, ...)
                                         ↓
                          HTTP POST → Gateway (puerto 8000)
                                         ↓
                          Microservicio Python bias-detection (puerto 8001)
                                         ↓
                          Ejecuta análisis en Python
                                         ↓
                          Retorna JSON con resultados
                                         ↓
                          Mapeo a ModelBiasAnalysis → Guarda en BD
   ```

**Dónde NO ejecutamos:**
- ❌ No ejecutamos prompts directamente (el usuario debe tener las predicciones)
- ❌ No ejecutamos datasets automáticamente (el usuario debe subir CSV)
- ❌ No invocamos modelos para generar predicciones (pendiente integración con `ModelWrapperClient`)

**Para generar predicciones (pendiente):**
- Usar `ModelWrapperClient.invoke()` o `batchInvoke()` para modelos externos
- Usar `ServingWrapperClient` o `ModelDeployment.baseUrl` para modelos locales
- Generar CSV automáticamente y luego ejecutar análisis

---

## 9. ¿Tenemos un wrapper con LangChain? ¿Usamos el wrapper?

**RESPUESTA:** ⚠️ **WRAPPER EXISTE PERO NO ESTÁ CLARO SI USA LANGCHAIN**

**Lo que SÍ existe:**

1. **`ModelWrapperClient` (puerto 8006):**
   - ✅ Cliente Java completamente funcional
   - ✅ Métodos: `invoke()`, `batchInvoke()`, `compareResponses()`
   - ✅ Soporta múltiples proveedores: OpenAI, Anthropic, Cohere, Groq

2. **Microservicio Python `leka-model-wrapper`:**
   - ✅ Funciona y responde a las peticiones
   - ⚠️ **No está claro** si usa LangChain internamente (no visible desde Java)

**Lo que NO se hace:**

1. **NO se usa para análisis:**
   - ❌ `ModelBiasAnalysisBusinessService` NO usa `ModelWrapperClient`
   - ❌ `ModelExplainabilityBusinessService` NO usa `ModelWrapperClient`
   - ❌ No hay integración para generar predicciones antes de análisis

**Cómo DEBERÍA usarse (pendiente):**
```java
// 1. Invocar modelo para generar predicciones
ModelInvokeRequest request = ModelInvokeRequest.builder()
    .modelId("gpt-4")
    .messages(List.of(prompts))
    .build();
ModelInvokeResponse response = modelWrapperClient.invoke(request);

// 2. Generar CSV con predicciones
// 3. Ejecutar análisis de sesgo/explicabilidad
```

**Estado:** Wrapper existe y funciona, pero NO está integrado en el flujo de análisis. Actualmente el usuario debe generar el CSV manualmente.

---

## 10. ¿Tenemos servidores de inferencia propios? ¿Nos descargamos el modelo y lo probamos en el servidor de inferencia?

**RESPUESTA:** ⚠️ **INFRAESTRUCTURA EXISTE PERO NO INTEGRADA**

**Lo que SÍ existe:**

1. **`ServingWrapperClient` (puerto 8000):**
   - ✅ Cliente Java para microservicio de serving
   - ✅ Endpoints: chat, generación de texto, embeddings

2. **`ModelDeployment` entity:**
   - ✅ Representa despliegues (Kubernetes, Cloud, etc.)
   - ✅ Campos: `baseUrl`, `entrypointPath`, `status`, `namespace`

3. **`Model` entity:**
   - ✅ Relación con `ModelDeployment` (despliegues internos)
   - ✅ Relación con `ModelProvider` (proveedores externos)

**Lo que NO se hace:**

1. **NO descargamos modelos:**
   - ❌ No hay código que descargue modelos de Hugging Face u otras fuentes
   - ❌ No hay código que despliegue modelos en servidores propios

2. **NO probamos en servidores propios:**
   - ❌ No hay código que use `ModelDeployment.baseUrl` para testing
   - ❌ No hay código que invoque endpoints de serving para análisis

**Cómo funciona actualmente:**
- Modelos se registran manualmente
- Si hay `ModelDeployment`, el usuario conoce el endpoint pero debe invocarlo manualmente
- No hay automatización para testing en servidores propios

**Cómo DEBERÍA funcionar (pendiente):**
```java
// 1. Obtener deployment del modelo
ModelDeployment deployment = model.getSubsrvdeployments().get(0);
String servingUrl = deployment.getBaseUrl() + deployment.getEntrypointPath();

// 2. Invocar endpoint con dataset de prueba
// 3. Obtener predicciones
// 4. Generar CSV
// 5. Ejecutar análisis
```

---

## 11. ¿Nos proporcionan el endpoint de serving y ejecutamos el prompt y el dataset? ¿Cómo sabemos cuál es su estructura?

**RESPUESTA:** ⚠️ **ENDPOINT SE CONFIGURA PERO NO SE EJECUTA AUTOMÁTICAMENTE**

**Lo que SÍ existe:**

1. **Configuración de endpoints:**
   - ✅ `ModelDeployment.baseUrl` - URL base del endpoint
   - ✅ `ModelDeployment.entrypointPath` - Ruta del endpoint
   - ✅ `ModelDeployment.status` - Estado (ACTIVE, INACTIVE, etc.)

2. **Estructura del endpoint:**
   - ⚠️ Se configura manualmente en `ModelDeployment`
   - ⚠️ No hay detección automática de estructura
   - ⚠️ No hay validación de formato

**Lo que NO se hace:**

1. **NO ejecutamos automáticamente:**
   - ❌ No hay código que invoque el endpoint con prompts/datasets
   - ❌ No hay generación automática de predicciones

2. **NO detectamos estructura:**
   - ❌ No hay discovery de API del endpoint
   - ❌ No hay validación de formato de request/response
   - ❌ El usuario debe conocer la estructura manualmente

**Cómo funciona actualmente:**
1. Usuario configura endpoint en `ModelDeployment.baseUrl`
2. Usuario invoca endpoint manualmente (fuera de CodeflowX)
3. Usuario genera CSV con predicciones
4. Usuario sube CSV a CodeflowX
5. CodeflowX ejecuta análisis

**Cómo DEBERÍA funcionar (pendiente):**
```java
// 1. Obtener endpoint desde ModelDeployment
String endpoint = deployment.getBaseUrl() + deployment.getEntrypointPath();

// 2. Detectar estructura (OpenAPI/Swagger, o intentar diferentes formatos)
// 3. Ejecutar dataset con prompts
// 4. Obtener predicciones
// 5. Ejecutar análisis automáticamente
```

**Formato de endpoints soportados (pendiente implementar):**
- REST API (JSON)
- gRPC
- TensorFlow Serving
- TorchServe
- Seldon Core
- KServe

---

## 📋 RESUMEN EJECUTIVO

### ✅ LO QUE FUNCIONA (INTEGRADO):

1. **Análisis de sesgo:**
   - ✅ Ejecuta microservicio Python
   - ✅ Acepta CSV con predicciones
   - ✅ Guarda resultados en BD

2. **Análisis de explicabilidad:**
   - ✅ Ejecuta microservicio Python (SHAP/LIME)
   - ✅ Acepta CSV con predicciones y características
   - ✅ Guarda resultados en BD

3. **Registro de modelos:**
   - ✅ Modelos internos, externos, MLOps
   - ✅ Versiones, despliegues, costes

### ⚠️ LO QUE FALTA (PENDIENTE):

1. **Generación automática de predicciones:**
   - ❌ Integrar `ModelWrapperClient` para invocar modelos
   - ❌ Integrar `ServingWrapperClient` para endpoints
   - ❌ Generar CSV automáticamente

2. **Testing automatizado:**
   - ❌ Ejecutar datasets de prueba
   - ❌ Comparar versiones
   - ❌ Evaluar métricas

3. **Sincronización MLOps:**
   - ❌ Descargar modelos/predicciones desde plataformas
   - ❌ Sincronizar versiones automáticamente

4. **Discovery de endpoints:**
   - ❌ Detectar estructura de API automáticamente
   - ❌ Validar formato de request/response

---

## 🎯 CONCLUSIÓN

**Estado actual:**
- ✅ **Análisis están integrados** - Sesgo y explicabilidad funcionan
- ✅ **Microservicios Python conectados** - BiasDetectionClient integrado
- ⚠️ **Generación de predicciones manual** - Usuario debe crear CSV
- ⚠️ **Testing no automatizado** - Falta ejecutar datasets automáticamente

**Flujo completo actual:**
1. Usuario registra modelo
2. Usuario genera CSV con predicciones (manualmente)
3. Usuario sube CSV a CodeflowX
4. CodeflowX ejecuta análisis (sesgo/explicabilidad)
5. CodeflowX guarda resultados

**Flujo ideal (pendiente):**
1. Usuario registra modelo
2. CodeflowX invoca modelo/dataset automáticamente
3. CodeflowX genera predicciones
4. CodeflowX ejecuta análisis automáticamente
5. CodeflowX guarda resultados
