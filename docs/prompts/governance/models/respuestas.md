# Respuestas a las Preguntas sobre Análisis de Sesgos, Explicabilidad y Testing de Modelos

**Fecha:** Enero 2025
**Estado:** Basado en código verificado del sistema

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

2. **Endpoint:** `POST /api/v1/bff/compliance/models/{modelId}/bias-analysis/execute`
   - Acepta `multipart/form-data` con el archivo CSV
   - Parámetros: `protectedAttribute` (ej: "gender"), `favorableOutcome`, `threshold`

3. **Flujo de ejecución:**
   ```
   Frontend → BFF → Microservicio Models → ModelBiasAnalysisBusinessService.executeBiasAnalysis()
                                                    ↓
                                          AIGovernanceClient.biasDetection().analyzeBias()
                                                    ↓
                                          Microservicio Python bias-detection (puerto 8001)
                                                    ↓
                                          Análisis de sesgo (demographic parity, equal opportunity)
                                                    ↓
                                          Mapear BiasAnalysisResponse → ModelBiasAnalysis
                                                    ↓
                                          Guardar en BD (tabla MODBIASANALYSIS)
   ```

4. **Resultados almacenados:**
   - `modbiasscore`: Score calculado (0-1, donde 0 = sin sesgo, 1 = máximo sesgo)
   - `modseverity`: LOW, MODERATE, HIGH
   - `modaffectedgroups`: Grupos afectados (JSONB con lista de grupos)
   - `modmitigationstrategies`: Recomendaciones de mitigación
   - `modanalysisdata`: Datos completos del análisis (JSONB)

5. **Implementación:**
   - `ModelBiasAnalysisBusinessService.executeBiasAnalysis()` - Ejecuta el análisis
   - Usa `AIGovernanceClient.biasDetection().analyzeBias()` del microservicio Python
   - Mapea `BiasAnalysisResponse` → `ModelBiasAnalysis` automáticamente

**Nota importante:** El CSV debe contener las predicciones ya generadas. El sistema NO invoca el modelo para generar predicciones automáticamente (ver pregunta 10).

---

## 2. ¿Cómo analizamos los sesgos en modelos locales LLMs?

**RESPUESTA:** ✅ **MISMO PROCESO QUE MODELOS EXTERNOS**

**Cómo funciona:**

1. **Para modelos locales LLMs:**
   - El proceso es idéntico al de modelos externos
   - Solo cambia cómo se genera el CSV con predicciones

2. **Opciones para generar predicciones:**
   - **Opción A:** Usuario genera CSV manualmente desde las predicciones del modelo local
   - **Opción B:** Usar `ModelWrapperClient` para invocar el modelo y generar predicciones (pendiente de integrar)
   - **Opción C:** Si el modelo está desplegado, usar `ModelDeployment.baseUrl` para invocarlo (pendiente de integrar)

3. **Ejecución:**
   - Mismo endpoint: `POST /api/v1/bff/compliance/models/{modelId}/bias-analysis/execute`
   - Mismo microservicio Python: `bias-detection` (puerto 8001)
   - Mismo formato CSV requerido

**Estado actual:** El CSV debe generarse externamente. La integración con `ModelWrapperClient` o endpoints de serving para generar predicciones automáticamente está pendiente.

---

## 3. ¿Cómo analizamos los sesgos en modelos MLOps?

**RESPUESTA:** ✅ **MISMO PROCESO, CON INTEGRACIÓN MLOps**

**Cómo funciona:**

1. **Para modelos en plataformas MLOps:**
   - Los modelos están registrados en `Model` entity
   - Pueden tener `ModelDeployment` con `baseUrl` si están desplegados
   - Pueden tener `ModelMlopsIntegration` con configuración específica de la plataforma

2. **Obtener predicciones desde MLOps:**
   - **Opción A:** Descargar predicciones desde la plataforma MLOps (Databricks, SageMaker, etc.) manualmente
   - **Opción B:** Invocar endpoint de serving si está configurado en `ModelDeployment.baseUrl` (pendiente de integrar)
   - **Opción C:** Usar cliente específico de la plataforma MLOps para obtener predicciones (pendiente)

3. **Generar CSV y ejecutar:**
   - Mismo endpoint: `POST /api/v1/bff/compliance/models/{modelId}/bias-analysis/execute`
   - Formato CSV igual que modelos externos/locales
   - Mismo microservicio Python: `bias-detection`

**Plataformas MLOps soportadas:**
- Databricks (`DATABRICKS`)
- SageMaker (`SAGEMAKER`)
- Vertex AI (`VERTEX_AI`)
- Azure ML (`AZURE_ML`)
- MLflow (`MLFLOW`)
- Hugging Face (`HUGGINGFACE`)
- Kubeflow (`KUBEFLOW`)
- Seldon (`SELDON`)
- Weights & Biases (`WANDB`)

**Estado:** Las integraciones MLOps se registran en `ModelMlopsIntegration`, pero la descarga automática de predicciones desde plataformas MLOps está pendiente.

---

## 4. ¿Qué es la explicabilidad y cómo funciona en CodeflowX?

**RESPUESTA:** ✅ **INTEGRADO Y FUNCIONAL**

**Qué es la explicabilidad:**
La explicabilidad es la capacidad de entender por qué un modelo toma ciertas decisiones. Muestra qué características (features) son más importantes para las predicciones y permite entender la contribución de cada feature a la decisión final.

**Cómo funciona en CodeflowX:**

1. **Usuario sube archivo CSV** con predicciones y características:
   ```
   feature1,feature2,feature3,prediction
   0.5,0.3,0.2,1
   0.7,0.1,0.4,0
   ```

2. **Endpoint:** `POST /api/v1/bff/compliance/models/{modelId}/explainability/execute`
   - Acepta `multipart/form-data` con el archivo CSV
   - Parámetros: `method` (SHAP, LIME, etc.), `predictionColumn` (default: "prediction")

3. **Flujo de ejecución:**
   ```
   Frontend → BFF → Microservicio Models → ModelExplainabilityBusinessService.executeExplainabilityAnalysis()
                                                      ↓
                                        AIGovernanceClient.biasDetection().explainPredictions()
                                                      ↓
                                        Microservicio Python bias-detection (puerto 8001)
                                                      ↓
                                        Cálculo de importancia de características (SHAP/LIME)
                                                      ↓
                                        Mapear ExplainabilityResponse → ModelExplainability
                                                      ↓
                                        Guardar en BD (tabla MODEXPLAINABILITY)
   ```

4. **Resultados almacenados:**
   - `modexplainabilityscore`: Score 0-100 (calculado desde importancia de características)
   - `modisexplainable`: Boolean (true si score > 50)
   - `modfeatureimportance`: Importancia de cada característica (JSONB con mapa feature → importancia)
   - `modexplanationquality`: EXCELLENT (≥80), GOOD (≥60), FAIR (≥40), POOR (<40)
   - `modrecommendations`: Recomendaciones del análisis
   - `modexplainabilitymethod`: Método usado (SHAP, LIME, etc.)

5. **Métodos soportados:**
   - **SHAP** (SHapley Additive exPlanations) - Método basado en teoría de juegos
   - **LIME** (Local Interpretable Model-agnostic Explanations) - Aproximación local
   - **Integrated Gradients** - Para redes neuronales

6. **Tipo de modelo detectado automáticamente:**
   - **Tree** - Árboles de decisión, Random Forest, XGBoost, LightGBM
   - **Linear** - Regresiones lineales, SVM, modelos lineales
   - **Neural** - Redes neuronales, LLMs, transformers

**Nota:** El tipo de modelo se determina desde `Model.modmodeltype` en la entidad `Model`.

---

## 5. ¿Cómo probamos los modelos MLOps?

**RESPUESTA:** ⚠️ **PARCIALMENTE IMPLEMENTADO - FALTA AUTOMATIZACIÓN**

**Lo que SÍ funciona:**

1. **Registro de modelos MLOps:**
   - ✅ Modelos se registran en `Model` entity
   - ✅ Configuración de plataforma MLOps en `ModelMlopsIntegration`
   - ✅ Asociación modelo-plataforma con configuración JSONB

2. **Configuración de endpoints:**
   - ✅ `ModelDeployment.baseUrl` para URLs base de serving
   - ✅ `ModelDeployment.entrypointPath` para rutas específicas
   - ✅ Estado de despliegue (`status`: ACTIVE, INACTIVE, ERROR, PENDING)

3. **Análisis disponibles:**
   - ✅ Análisis de sesgo (bias analysis)
   - ✅ Análisis de explicabilidad (explainability)
   - ✅ Métricas de rendimiento (performance metrics)

**Lo que NO está automatizado (pendiente):**

1. **Ejecución automática de tests:**
   - ❌ No hay servicio que ejecute datasets de prueba automáticamente
   - ❌ No hay comparación automática de versiones
   - ❌ No hay evaluación automática de métricas contra benchmarks

2. **Obtención de predicciones desde MLOps:**
   - ⚠️ Las integraciones MLOps están configuradas pero no hay código que:
     - Descargue modelos desde Databricks/SageMaker/Vertex AI
     - Invoke endpoints de serving automáticamente
     - Genere CSV con predicciones desde datasets de prueba

**Cómo funciona actualmente:**

1. Modelo MLOps se registra manualmente en CodeflowX
2. Usuario descarga predicciones manualmente desde la plataforma MLOps (o las genera manualmente)
3. Usuario sube CSV a CodeflowX para análisis (sesgo/explicabilidad)
4. CodeflowX ejecuta análisis usando microservicio Python `bias-detection`
5. Resultados se almacenan en BD

**Próximos pasos (pendiente):**
- Integrar con clientes MLOps para descargar predicciones automáticamente
- Agregar servicio de testing que ejecute datasets y compare resultados
- Implementar comparación automática de versiones

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

- ✅ Entidad `ModelMlopsIntegration` para asociación modelo-plataforma
- ✅ Campo `modmlopsplatform` con valor de la plataforma
- ✅ Campo `modmlopsconfig` (JSONB) para configuración específica de cada plataforma
- ✅ Estado de integración (`modmlopsstatus`: ACTIVE, INACTIVE, ERROR, PENDING)
- ✅ Sincronización última (`modmlopslastsyncat`) y errores (`modmlopslasterror`)

**Capacidades actuales:**

- **Catalogación:** ✅ Todas las plataformas se pueden registrar
- **Configuración:** ✅ JSONB permite configuración flexible por plataforma
- **Sincronización:** ⚠️ Pendiente implementar sincronización automática
- **Descarga de predicciones:** ⚠️ Pendiente para todas las plataformas
- **Testing automático:** ⚠️ Pendiente

**Estado:** Principalmente para **registro y catálogo**. La sincronización automática de modelos, descarga de predicciones y testing está pendiente.

---

## 7. ¿Solo los registramos?

**RESPUESTA:** ⚠️ **PRINCIPALMENTE REGISTRO, PERO CON ANÁLISIS INTEGRADO**

**Lo que SÍ hacemos:**

1. **Registro completo:**
   - ✅ Registro de modelos (internos, externos, MLOps)
   - ✅ Versiones y despliegues
   - ✅ Costes y métricas
   - ✅ Configuración de integraciones MLOps
   - ✅ Proveedores y credenciales

2. **Análisis integrado:**
   - ✅ Análisis de sesgo (ejecuta microservicio Python `bias-detection`)
   - ✅ Análisis de explicabilidad (ejecuta microservicio Python `bias-detection`)
   - ✅ Almacenamiento automático de resultados en BD
   - ✅ Métricas de rendimiento

**Lo que NO hacemos (pendiente):**

1. **Testing automatizado:**
   - ❌ No ejecutamos datasets de prueba automáticamente
   - ❌ No comparamos versiones automáticamente
   - ❌ No evaluamos modelos automáticamente contra benchmarks

2. **Sincronización MLOps:**
   - ❌ No descargamos modelos desde plataformas MLOps
   - ❌ No sincronizamos versiones automáticamente
   - ❌ No obtenemos predicciones automáticamente desde plataformas

3. **Generación automática de predicciones:**
   - ❌ No invocamos modelos automáticamente para generar predicciones
   - ❌ No ejecutamos prompts automáticamente

**Resumen:**
- ✅ Registramos TODO (modelos, versiones, despliegues, integraciones)
- ✅ Ejecutamos análisis de sesgo/explicabilidad cuando el usuario sube CSV
- ❌ Falta automatización de testing, sincronización MLOps y generación automática de predicciones

---

## 8. En modelos externos y/o locales, ¿dónde ejecutamos el prompt para analizar o el dataset de pruebas?

**RESPUESTA:** ⚠️ **ACTUALMENTE NO SE EJECUTAN AUTOMÁTICAMENTE**

**Dónde se ejecuta el análisis (una vez tenemos el CSV):**

1. **Microservicio Python `bias-detection` (puerto 8001):**
   - Recibe el CSV con predicciones (o predicciones + características para explicabilidad)
   - Ejecuta el análisis de sesgo (algoritmos de fairness: demographic parity, equal opportunity)
   - Ejecuta análisis de explicabilidad (SHAP/LIME/Integrated Gradients)

2. **Flujo actual:**
   ```
   Usuario genera CSV manualmente (fuera de CodeflowX)
         ↓
   Usuario sube CSV → Frontend → BFF → Microservicio Models
                                         ↓
                          ModelBiasAnalysisBusinessService.executeBiasAnalysis()
                                         ↓
                          AIGovernanceClient.biasDetection().analyzeBias(csvFile, ...)
                                         ↓
                          HTTP POST → Gateway → Microservicio Python bias-detection
                                         ↓
                          Ejecuta análisis en Python (bibliotecas: scikit-learn, pandas, shap, lime)
                                         ↓
                          Retorna JSON con resultados (BiasAnalysisResponse)
                                         ↓
                          Mapeo a ModelBiasAnalysis → Guarda en BD
   ```

**Dónde NO ejecutamos actualmente:**

- ❌ **NO ejecutamos prompts directamente** - El usuario debe tener las predicciones ya generadas
- ❌ **NO ejecutamos datasets automáticamente** - El usuario debe subir CSV con predicciones
- ❌ **NO invocamos modelos para generar predicciones** - Falta integración con `ModelWrapperClient` o endpoints de serving

**Cómo DEBERÍA funcionar (pendiente de implementar):**

```java
// 1. Obtener modelo y prompts/dataset
Model model = modelRepository.findById(modelId);
List<String> prompts = dataset.getPrompts();

// 2. Invocar modelo para generar predicciones
// Opción A: Modelo externo (OpenAI, Anthropic, etc.)
ModelWrapperClient modelWrapper = ...;
ModelInvokeResponse response = modelWrapper.invoke(modelId, prompts);

// Opción B: Modelo local (endpoint de serving)
ServingWrapperClient servingClient = ...;
ServingResponse response = servingClient.chat(deployment.getBaseUrl(), prompts);

// 3. Generar CSV con predicciones
CSV csv = generateCSV(prompts, response.getPredictions(), protectedAttributes);

// 4. Ejecutar análisis
ModelBiasAnalysis analysis = biasAnalysisService.executeBiasAnalysis(modelId, csv, ...);
```

**Estado:** Actualmente el CSV debe generarse manualmente. La integración con `ModelWrapperClient` o `ServingWrapperClient` para generar predicciones automáticamente está pendiente.

---

## 9. ¿Tenemos un wrapper con LangChain? ¿Usamos el wrapper?

**RESPUESTA:** ⚠️ **WRAPPER EXISTE PERO NO ESTÁ CLARO SI USA LANGCHAIN, Y NO SE USA PARA ANÁLISIS**

**Lo que SÍ existe:**

1. **`ModelWrapperClient` (Java):**
   - ✅ Cliente Java completamente funcional
   - ✅ Ubicación: `codeflowx.govern.nocode.client.ModelWrapperClient`
   - ✅ Métodos: `invoke()`, `batchInvoke()`, `compareResponses()`
   - ✅ Soporta múltiples proveedores: OpenAI, Anthropic, Cohere, Groq

2. **Microservicio Python `leka-model-wrapper` (puerto 8006):**
   - ✅ Funciona y responde a las peticiones HTTP
   - ✅ Endpoints disponibles para invocación de modelos
   - ⚠️ **No está claro** si usa LangChain internamente (no visible desde el código Java)

**Lo que NO se hace:**

1. **NO se usa para análisis:**
   - ❌ `ModelBiasAnalysisBusinessService` NO usa `ModelWrapperClient`
   - ❌ `ModelExplainabilityBusinessService` NO usa `ModelWrapperClient`
   - ❌ No hay integración para generar predicciones antes de análisis

2. **NO está integrado en el flujo:**
   - ❌ No se invoca para generar predicciones desde prompts
   - ❌ No se usa para ejecutar datasets de prueba

**Cómo DEBERÍA usarse (pendiente):**

```java
// 1. Invocar modelo para generar predicciones
ModelWrapperClient modelWrapper = aiGovernanceClient.modelWrapper();
ModelInvokeRequest request = ModelInvokeRequest.builder()
    .modelId("gpt-4")
    .messages(List.of(prompts))
    .build();
ModelInvokeResponse response = modelWrapper.invoke(request);

// 2. Generar CSV con predicciones
File csvFile = generateCSVFromPredictions(prompts, response.getPredictions(), protectedAttributes);

// 3. Ejecutar análisis de sesgo/explicabilidad
ModelBiasAnalysis analysis = biasAnalysisService.executeBiasAnalysis(modelId, csvFile, ...);
```

**Estado:**
- ✅ Wrapper existe y funciona
- ❌ NO está integrado en el flujo de análisis
- ⚠️ No está claro si usa LangChain (habría que revisar código Python del microservicio)
- ⚠️ Actualmente el usuario debe generar el CSV manualmente

---

## 10. ¿Tenemos servidores de inferencia propios? ¿Nos descargamos el modelo y lo probamos en el servidor de inferencia?

**RESPUESTA:** ⚠️ **INFRAESTRUCTURA EXISTE PERO NO INTEGRADA PARA TESTING**

**Lo que SÍ existe:**

1. **`ServingWrapperClient` (Java):**
   - ✅ Cliente Java para microservicio de serving
   - ✅ Ubicación: `codeflowx.govern.nocode.client.ServingWrapperClient`
   - ✅ Endpoints: chat, generación de texto, embeddings
   - ✅ Puerto: 8000 (microservicio `serving-wrapper`)

2. **`ModelDeployment` entity:**
   - ✅ Representa despliegues (Kubernetes, Cloud, etc.)
   - ✅ Campos: `baseUrl`, `entrypointPath`, `status`, `namespace`, `environment`
   - ✅ Relación con `Model` (un modelo puede tener múltiples despliegues)

3. **`Model` entity:**
   - ✅ Relación `@OneToMany` con `ModelDeployment` (despliegues internos)
   - ✅ Relación `@ManyToOne` con `ModelProvider` (proveedores externos)

**Lo que NO se hace:**

1. **NO descargamos modelos:**
   - ❌ No hay código que descargue modelos de Hugging Face u otras fuentes
   - ❌ No hay código que despliegue modelos en servidores propios automáticamente
   - ❌ No hay sincronización automática de modelos

2. **NO probamos en servidores propios:**
   - ❌ No hay código que use `ModelDeployment.baseUrl` para testing
   - ❌ No hay código que invoque endpoints de serving para análisis
   - ❌ No hay integración entre `ServingWrapperClient` y servicios de análisis

**Cómo funciona actualmente:**
- Modelos se registran manualmente
- Si hay `ModelDeployment`, el usuario conoce el endpoint (`baseUrl` + `entrypointPath`) pero debe invocarlo manualmente
- No hay automatización para testing en servidores propios

**Cómo DEBERÍA funcionar (pendiente):**

```java
// 1. Obtener deployment del modelo
ModelDeployment deployment = model.getSubsrvdeployments().get(0);
String servingUrl = deployment.getBaseUrl() + deployment.getEntrypointPath();

// 2. Invocar endpoint con dataset de prueba usando ServingWrapperClient
ServingWrapperClient servingClient = aiGovernanceClient.servingWrapper();
List<String> prompts = testDataset.getPrompts();
for (String prompt : prompts) {
    ServingResponse response = servingClient.chat(servingUrl, prompt);
    predictions.add(response.getPrediction());
}

// 3. Generar CSV con predicciones
File csvFile = generateCSV(prompts, predictions, protectedAttributes);

// 4. Ejecutar análisis
ModelBiasAnalysis analysis = biasAnalysisService.executeBiasAnalysis(modelId, csvFile, ...);
```

**Estado:**
- ✅ Infraestructura existe (clientes, entidades, despliegues)
- ❌ NO está integrada para testing automático
- ⚠️ Falta código que conecte `ServingWrapperClient` con servicios de análisis

---

## 11. ¿Nos proporcionan el endpoint de serving y ejecutamos el prompt y el dataset? ¿Cómo sabemos cuál es su estructura?

**RESPUESTA:** ⚠️ **ENDPOINT SE CONFIGURA PERO NO SE EJECUTA AUTOMÁTICAMENTE Y NO SE DETECTA LA ESTRUCTURA**

**Lo que SÍ existe:**

1. **Configuración de endpoints:**
   - ✅ `ModelDeployment.baseUrl` - URL base del endpoint (ej: "https://model.example.com")
   - ✅ `ModelDeployment.entrypointPath` - Ruta del endpoint (ej: "/predict" o "/v1/chat")
   - ✅ `ModelDeployment.status` - Estado (ACTIVE, INACTIVE, ERROR, PENDING)

2. **Clientes disponibles:**
   - ✅ `ServingWrapperClient` - Para endpoints de serving (puerto 8000)
   - ✅ `ModelWrapperClient` - Para modelos externos (puerto 8006)

**Lo que NO se hace:**

1. **NO ejecutamos automáticamente:**
   - ❌ No hay código que invoque el endpoint con prompts/datasets
   - ❌ No hay generación automática de predicciones desde endpoints
   - ❌ No hay integración entre endpoints y servicios de análisis

2. **NO detectamos estructura:**
   - ❌ No hay discovery de API del endpoint (OpenAPI/Swagger)
   - ❌ No hay validación de formato de request/response
   - ❌ No hay detección automática de estructura de datos
   - ❌ El usuario debe conocer la estructura manualmente

**Cómo funciona actualmente:**

1. Usuario configura endpoint en `ModelDeployment.baseUrl` y `ModelDeployment.entrypointPath`
2. Usuario invoca endpoint manualmente (fuera de CodeflowX) con prompts/dataset
3. Usuario genera CSV con predicciones
4. Usuario sube CSV a CodeflowX
5. CodeflowX ejecuta análisis

**Cómo DEBERÍA funcionar (pendiente):**

```java
// 1. Obtener endpoint desde ModelDeployment
String endpoint = deployment.getBaseUrl() + deployment.getEntrypointPath();

// 2. Detectar estructura (OpenAPI/Swagger, o intentar diferentes formatos)
APISpec spec = discoverAPISpec(endpoint); // Pendiente
// o
RequestFormat format = tryDetectFormat(endpoint); // Pendiente

// 3. Ejecutar dataset con prompts
List<String> prompts = testDataset.getPrompts();
List<String> predictions = new ArrayList<>();
for (String prompt : prompts) {
    ServingRequest request = buildRequest(prompt, format); // Pendiente
    ServingResponse response = servingClient.invoke(endpoint, request);
    predictions.add(response.getPrediction());
}

// 4. Generar CSV con predicciones
File csvFile = generateCSV(prompts, predictions, protectedAttributes);

// 5. Ejecutar análisis automáticamente
ModelBiasAnalysis analysis = biasAnalysisService.executeBiasAnalysis(modelId, csvFile, ...);
```

**Formatos de endpoints que deberían soportarse (pendiente implementar):**

- **REST API (JSON)** - Estándar, más común
- **gRPC** - Para servicios de alto rendimiento
- **TensorFlow Serving** - Formato específico de TensorFlow
- **TorchServe** - Formato específico de PyTorch
- **Seldon Core** - Formato estándar de Seldon
- **KServe** - Formato estándar de Kubernetes
- **OpenAI-compatible API** - Para modelos que siguen el formato OpenAI

**Detección de estructura (pendiente):**

1. **OpenAPI/Swagger:** Si el endpoint expone `/openapi.json` o `/swagger.json`, leer especificación
2. **Intentar formatos comunes:** Probar diferentes formatos de request/response hasta que uno funcione
3. **Configuración manual:** Permitir al usuario especificar el formato si la detección falla

**Estado:**
- ✅ Endpoints se pueden configurar
- ❌ NO se ejecutan automáticamente
- ❌ NO se detecta la estructura
- ⚠️ Falta implementar discovery y ejecución automática

---

## 📋 RESUMEN EJECUTIVO

### ✅ LO QUE FUNCIONA (INTEGRADO):

1. **Análisis de sesgo:**
   - ✅ Ejecuta microservicio Python `bias-detection` (puerto 8001)
   - ✅ Acepta CSV con predicciones via endpoint `POST /{modelId}/bias-analysis/execute`
   - ✅ Guarda resultados en BD (tabla `MODBIASANALYSIS`)
   - ✅ Calcula métricas: demographic parity, equal opportunity, bias score

2. **Análisis de explicabilidad:**
   - ✅ Ejecuta microservicio Python `bias-detection` (puerto 8001)
   - ✅ Acepta CSV con predicciones y características via endpoint `POST /{modelId}/explainability/execute`
   - ✅ Guarda resultados en BD (tabla `MODEXPLAINABILITY`)
   - ✅ Métodos: SHAP, LIME, Integrated Gradients

3. **Registro de modelos:**
   - ✅ Modelos internos, externos, MLOps
   - ✅ Versiones, despliegues, costes, métricas
   - ✅ Integraciones con 9 plataformas MLOps configurables

### ⚠️ LO QUE FALTA (PENDIENTE):

1. **Generación automática de predicciones:**
   - ❌ Integrar `ModelWrapperClient` para invocar modelos externos
   - ❌ Integrar `ServingWrapperClient` para endpoints de serving
   - ❌ Generar CSV automáticamente desde prompts/datasets

2. **Testing automatizado:**
   - ❌ Ejecutar datasets de prueba automáticamente
   - ❌ Comparar versiones automáticamente
   - ❌ Evaluar modelos automáticamente contra benchmarks

3. **Sincronización MLOps:**
   - ❌ Descargar modelos/predicciones desde plataformas MLOps
   - ❌ Sincronizar versiones automáticamente

4. **Discovery de endpoints:**
   - ❌ Detectar estructura de API automáticamente (OpenAPI/Swagger)
   - ❌ Validar formato de request/response
   - ❌ Detección automática de formatos (REST, gRPC, TensorFlow Serving, etc.)

---

## 🎯 CONCLUSIÓN

**Estado actual:**
- ✅ **Análisis están integrados** - Sesgo y explicabilidad funcionan correctamente
- ✅ **Microservicios Python conectados** - `bias-detection` integrado via `AIGovernanceClient`
- ⚠️ **Generación de predicciones manual** - Usuario debe crear CSV manualmente
- ⚠️ **Testing no automatizado** - Falta ejecutar datasets automáticamente

**Flujo completo actual:**
1. Usuario registra modelo en CodeflowX
2. Usuario genera CSV con predicciones manualmente (fuera de CodeflowX)
3. Usuario sube CSV a CodeflowX via endpoint `/bias-analysis/execute` o `/explainability/execute`
4. CodeflowX ejecuta análisis usando microservicio Python
5. CodeflowX guarda resultados en BD

**Flujo ideal (pendiente):**
1. Usuario registra modelo en CodeflowX
2. Usuario selecciona dataset de prueba o proporciona prompts
3. CodeflowX invoca modelo/endpoint automáticamente usando `ModelWrapperClient` o `ServingWrapperClient`
4. CodeflowX genera predicciones automáticamente
5. CodeflowX genera CSV automáticamente
6. CodeflowX ejecuta análisis automáticamente
7. CodeflowX guarda resultados en BD

**Estimación de trabajo para completar flujo ideal:**
- Integración `ModelWrapperClient` con servicios de análisis: 1-2 días
- Integración `ServingWrapperClient` con servicios de análisis: 1-2 días
- Discovery de estructura de endpoints (OpenAPI/Swagger): 2-3 días
- Generación automática de CSV desde predicciones: 1 día
- Testing y validación: 2 días
- **Total estimado: 7-10 días de desarrollo**
