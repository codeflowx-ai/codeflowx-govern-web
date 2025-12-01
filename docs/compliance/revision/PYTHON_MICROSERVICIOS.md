# GUÍA PYTHON - MICROSERVICIOS Y SERVICIOS EXTERNOS

**Equipo:** Python/MLOps Team
**Duración:** Paralelo con trabajo Java
**Objetivo:** Crear/actualizar microservicios Python requeridos por incidencias Java

---

## 📋 MICROSERVICIOS REQUERIDOS

**⚠️ NOTA:** La generación de documentos PDF se hace con **Java** (iText + FreeMarker), NO requiere microservicio Python.

| ID Incidencia | Microservicio | Endpoint | Estado | Prioridad |
|---------------|---------------|----------|--------|-----------|
| ~~**INC-010-002**~~ | ~~Generación Documentos/Reportes PDF~~ | ~~`/api/documentation/generate-report`~~ | ✅ **JAVA** | ~~🔴 CRÍTICA~~ |
| ~~**INC-010-001**~~ | ~~Documentación Técnica (Anexo IV)~~ | ~~`/api/documentation/generate-annex-iv`~~ | ✅ **JAVA** | ~~🔴 CRÍTICA~~ |
| **INC-005-002** | Detección Alucinaciones | `/api/rag/detect-hallucinations` | 🔴 PENDIENTE | 🔴 CRÍTICA |
| **INC-007** | Validación FRIA vs Métricas | `/api/fria/validate-against-metrics` | 🔴 PENDIENTE | 🔴 CRÍTICA |
| **INC-020** | API Autoridades (Mock) | `/api/registrations` | 🟡 MOCK | 🔴 CRÍTICA |

---

## 📚 DOCUMENTOS DE REFERENCIA

### Prompts Específicos:
1. **INC-005-002:**
   - `/docs/compliance/gaps/prompts/python/INC-005-002_deteccion_alucinaciones.md`
   - **Tecnologías:** SelfCheckGPT, FactScore, Entailment

2. **INC-007:**
   - Verificar prompt específico en `/docs/compliance/gaps/prompts/python/`
   - **Funcionalidad:** Validación cruzada FRIA vs métricas de performance

### Documentación General:
- **Arquitectura Microservicios:** Ver documentación del proyecto Python
- **Integración Java-Python:** Ver `AuthorityNotificationService.java` como ejemplo

---

## 🏗️ ARQUITECTURA DE MICROSERVICIOS

### **Stack Tecnológico:**
- **Framework:** FastAPI o Flask (según estándar del proyecto)
- **Formato:** REST API JSON
- **Autenticación:** Según estándar del proyecto
- **Base de Datos:** Si es necesaria, usar PostgreSQL

### **Convenciones:**
- **Endpoints:** `/api/{module}/{action}`
- **Métodos:** POST para operaciones, GET para consultas
- **Response:** JSON con estructura estándar
- **Error Handling:** Códigos HTTP estándar (200, 400, 500)

---

## 📁 ESTRUCTURA DE DIRECTORIOS

### **Microservicio Detección Alucinaciones:**
```
[proyecto-python]/
├── services/
│   └── hallucination_detection/
│       ├── __init__.py
│       ├── main.py              ← FastAPI app
│       ├── models.py             ← Pydantic models
│       ├── detection.py          ← Lógica de detección
│       └── requirements.txt
└── tests/
    └── test_hallucination_detection.py
```

### **Microservicio Validación FRIA:**
```
[proyecto-python]/
├── services/
│   └── fria_validation/
│       ├── __init__.py
│       ├── main.py              ← FastAPI app
│       ├── models.py             ← Pydantic models
│       ├── validation.py         ← Lógica de validación
│       └── requirements.txt
└── tests/
    └── test_fria_validation.py
```

---

## 🔧 IMPLEMENTACIÓN POR MICROSERVICIO

**⚠️ NOTA:** La generación de documentos (INC-010-002, INC-010-001) se hace con **Java** (iText + FreeMarker), NO requiere microservicio Python.

### **INC-005-002: Microservicio Detección Alucinaciones**

#### **Endpoint:**
```
POST /api/rag/detect-hallucinations
```

#### **Request:**
```json
{
  "text": "Texto generado por RAG a validar",
  "context": "Contexto del RAG (chunks, documentos fuente)",
  "model": "nombre-del-modelo" (opcional)
}
```

#### **Response:**
```json
{
  "isHallucination": true,
  "confidence": 0.85,
  "evidence": [
    {
      "sentence": "Fragmento detectado",
      "reason": "No está respaldado por el contexto",
      "confidence": 0.90
    }
  ],
  "methods": ["SelfCheckGPT", "FactScore"],
  "timestamp": "2025-11-25T18:00:00Z"
}
```

#### **Implementación:**
- [ ] Leer prompt completo: `INC-005-002_deteccion_alucinaciones.md`
- [ ] Crear servicio FastAPI/Flask
- [ ] Implementar SelfCheckGPT
- [ ] Implementar FactScore
- [ ] Implementar Entailment checking
- [ ] Crear tests
- [ ] Documentar API

---

### **INC-007: Microservicio Validación FRIA vs Métricas**

#### **Endpoint:**
```
POST /api/fria/validate-against-metrics
```

#### **Request:**
```json
{
  "friaId": 123,
  "metrics": {
    "accuracy": 0.95,
    "precision": 0.92,
    "recall": 0.88,
    "f1Score": 0.90
  },
  "projectId": 456
}
```

#### **Response:**
```json
{
  "isConsistent": true,
  "inconsistencies": [],
  "warnings": [
    "FRIA indica alto riesgo pero métricas son buenas"
  ],
  "recommendation": "REVIEW_FRIA",
  "confidence": 0.75
}
```

#### **Implementación:**
- [ ] Leer prompt específico
- [ ] Crear servicio FastAPI/Flask
- [ ] Implementar lógica de validación cruzada
- [ ] Crear tests
- [ ] Documentar API

---

### **INC-020: Mock API Autoridades**

#### **Endpoint:**
```
POST /api/registrations
```

#### **Request:**
```json
{
  "registrationId": 123,
  "uuid": "uuid-del-registro",
  "submissionData": {
    "section": "SECTION_A",
    "data": {...}
  }
}
```

#### **Response:**
```json
{
  "status": "SUBMITTED",
  "registrationId": "EU-2025-12345",
  "submittedAt": "2025-11-25T18:00:00Z",
  "message": "Registration submitted successfully"
}
```

#### **Implementación:**
- [ ] Crear mock/stub del servicio
- [ ] Documentar formato esperado de API oficial
- [ ] Dejar TODO para integración real

---

## 📝 PLANTILLA DE MICROSERVICIO FASTAPI

```python
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import logging

app = FastAPI(title="[Nombre Microservicio]")
logger = logging.getLogger(__name__)

class RequestModel(BaseModel):
    # Campos según endpoint
    pass

class ResponseModel(BaseModel):
    # Campos según endpoint
    pass

@app.post("/api/[module]/[action]", response_model=ResponseModel)
async def [action](request: RequestModel):
    """
    [Descripción según prompt]

    Artículo EU AI Act: Art. [número]
    """
    try:
        logger.info(f"Processing request: {request}")

        # Implementación según prompt
        result = process_request(request)

        return ResponseModel(**result)

    except Exception as e:
        logger.error(f"Error processing request: {e}", exc_info=True)
        raise HTTPException(status_code=500, detail=str(e))

def process_request(request: RequestModel) -> dict:
    # Lógica de negocio según prompt
    pass
```

---

## ✅ CHECKLIST FINAL

### **Antes de Empezar:**
- [ ] Leer prompts completos
- [ ] Verificar estándares del proyecto Python
- [ ] Revisar microservicios existentes como referencia

### **Durante Implementación:**
- [ ] Seguir estándares del proyecto
- [ ] Implementar tests
- [ ] Documentar API (OpenAPI/Swagger)

### **Después de Implementación:**
- [ ] Tests pasando
- [ ] Documentación actualizada
- [ ] Coordinar con equipo Java para integración

---

## 🔄 COORDINACIÓN CON EQUIPO JAVA

### **Comunicación:**
- **Endpoints esperados:** Documentar claramente
- **Formato de datos:** Especificar request/response
- **Errores:** Documentar códigos de error

### **Integración:**
- Los servicios Java tienen fallback si microservicio no está disponible
- Implementar mock/stub si es necesario para desarrollo

---

**Última Actualización:** 25 de noviembre de 2025
