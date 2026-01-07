# 🚀 Backend Notebook de Agentes de IA - Especificaciones Técnicas

## 📋 Descripción General

Este documento describe las especificaciones técnicas para desarrollar el backend del **Notebook de Agentes de IA** que se conectará con el frontend Next.js. El sistema permitirá crear, ejecutar y gestionar agentes de inteligencia artificial con herramientas personalizadas.

### 🎯 Objetivo Principal: **Creación de Agentes Reutilizables**

El sistema está diseñado para permitir a los usuarios crear **agentes de IA reutilizables** que puedan ser:
- **Compartidos** entre diferentes notebooks y proyectos
- **Versionados** para control de cambios y mejoras
- **Importados/Exportados** para distribución y colaboración
- **Reutilizados** como componentes en workflows más complejos
- **Optimizados** iterativamente basándose en resultados previos

## 🏗️ Arquitectura del Sistema

### Frontend (Next.js)
- **Editor de Notebooks** con celdas de código Python
- **Workspace Tree** para gestión de archivos
- **Plantillas predefinidas** para diferentes tipos de agentes
- **Chatbot de asistencia** integrado
- **Modo Mock** para desarrollo y testing

### Backend (Python + FastAPI)
- **API REST** para operaciones CRUD
- **Jupyter Kernel** para ejecución de código Python
- **Sistema de autenticación** JWT
- **Base de datos** PostgreSQL para persistencia
- **WebSocket** para comunicación en tiempo real

## 🗄️ Base de Datos

### Motor: **PostgreSQL 15+**

### Tablas Principales

#### 1. `users`
```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    roles TEXT[] DEFAULT '{}',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 2. `notebook_sessions`
```sql
CREATE TABLE notebook_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_public BOOLEAN DEFAULT false,
    tags TEXT[] DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. `notebook_cells`
```sql
CREATE TABLE notebook_cells (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id UUID REFERENCES notebook_sessions(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('code', 'config', 'tool', 'test', 'markdown')),
    content TEXT NOT NULL,
    language VARCHAR(20) NOT NULL,
    order_index INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'idle' CHECK (status IN ('idle', 'running', 'success', 'error')),
    output TEXT,
    execution_time FLOAT,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 4. `agent_tools`
```sql
CREATE TABLE agent_tools (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    code TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    parameters JSONB NOT NULL,
    is_public BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 5. `agent_configs`
```sql
CREATE TABLE agent_configs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id UUID REFERENCES notebook_sessions(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    model VARCHAR(100) NOT NULL,
    temperature FLOAT DEFAULT 0.7,
    max_tokens INTEGER DEFAULT 2000,
    system_prompt TEXT,
    tools TEXT[] DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 6. `execution_logs`
```sql
CREATE TABLE execution_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cell_id UUID REFERENCES notebook_cells(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    execution_start TIMESTAMP NOT NULL,
    execution_end TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    output TEXT,
    error_message TEXT,
    execution_time FLOAT,
    memory_usage BIGINT,
    cpu_usage FLOAT
);
```

#### 7. `reusable_agents`
```sql
CREATE TABLE reusable_agents (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    version VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    agent_config JSONB NOT NULL,
    tools_config JSONB NOT NULL,
    system_prompt TEXT,
    category VARCHAR(100) NOT NULL,
    tags TEXT[] DEFAULT '{}',
    is_public BOOLEAN DEFAULT false,
    is_verified BOOLEAN DEFAULT false,
    download_count INTEGER DEFAULT 0,
    rating FLOAT DEFAULT 0.0,
    rating_count INTEGER DEFAULT 0,
    dependencies JSONB DEFAULT '{}',
    requirements TEXT[] DEFAULT '{}',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 8. `agent_versions`
```sql
CREATE TABLE agent_versions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agent_id UUID REFERENCES reusable_agents(id) ON DELETE CASCADE,
    version VARCHAR(20) NOT NULL,
    changelog TEXT,
    agent_config JSONB NOT NULL,
    tools_config JSONB NOT NULL,
    is_deprecated BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 9. `agent_dependencies`
```sql
CREATE TABLE agent_dependencies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agent_id UUID REFERENCES reusable_agents(id) ON DELETE CASCADE,
    dependency_id UUID REFERENCES reusable_agents(id) ON DELETE CASCADE,
    dependency_type VARCHAR(50) NOT NULL, -- 'required', 'optional', 'conflicts'
    version_constraint VARCHAR(100), -- '>=1.0.0', '~=2.0.0'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔌 API Endpoints

### Autenticación
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
POST /api/auth/logout
GET  /api/auth/me
```

### Sesiones de Notebook
```
GET    /api/notebooks                    # Listar notebooks del usuario
POST   /api/notebooks                    # Crear nuevo notebook
GET    /api/notebooks/{id}              # Obtener notebook por ID
PUT    /api/notebooks/{id}              # Actualizar notebook
DELETE /api/notebooks/{id}              # Eliminar notebook
POST   /api/notebooks/{id}/duplicate    # Duplicar notebook
POST   /api/notebooks/{id}/export      # Exportar notebook
POST   /api/notebooks/import           # Importar notebook
```

### Celdas del Notebook
```
GET    /api/notebooks/{id}/cells        # Listar celdas
POST   /api/notebooks/{id}/cells        # Crear nueva celda
PUT    /api/notebooks/{id}/cells/{cell_id}  # Actualizar celda
DELETE /api/notebooks/{id}/cells/{cell_id}  # Eliminar celda
POST   /api/notebooks/{id}/cells/reorder    # Reordenar celdas
```

### Ejecución de Código
```
POST   /api/notebooks/{id}/execute/cell/{cell_id}    # Ejecutar celda individual
POST   /api/notebooks/{id}/execute/all               # Ejecutar todas las celdas
POST   /api/notebooks/{id}/execute/stop              # Detener ejecución
GET    /api/notebooks/{id}/execute/status            # Estado de ejecución
```

### Herramientas del Agente
```
GET    /api/tools                       # Listar herramientas
POST   /api/tools                       # Crear herramienta
GET    /api/tools/{id}                  # Obtener herramienta
PUT    /api/tools/{id}                  # Actualizar herramienta
DELETE /api/tools/{id}                  # Eliminar herramienta
POST   /api/tools/{id}/test             # Probar herramienta
```

### Plantillas
```
GET    /api/templates                   # Listar plantillas disponibles
GET    /api/templates/{id}              # Obtener plantilla
POST   /api/templates                   # Crear plantilla personalizada
PUT    /api/templates/{id}              # Actualizar plantilla
DELETE /api/templates/{id}              # Eliminar plantilla
```

### WebSocket
```
WS /ws/notebooks/{id}                  # Conexión en tiempo real para outputs
```

## 🐍 Especificaciones Python

### Versión: **Python 3.11+**

### Dependencias Principales
```txt
fastapi==0.104.1
uvicorn[standard]==0.24.0
sqlalchemy==2.0.23
psycopg2-binary==2.9.9
alembic==1.12.1
jupyter-client==9.0.1
jupyter-kernel-gateway==2.6.1
ipykernel==6.27.1
pydantic==2.5.0
python-jose[cryptography]==3.3.0
passlib[bcrypt]==1.7.4
python-multipart==0.0.6
websockets==12.0
redis==5.0.1
celery==5.3.4
```

### Estructura del Proyecto
```
backend/
├── app/
│   ├── __init__.py
│   ├── main.py                 # FastAPI app
│   ├── config.py               # Configuración
│   ├── database.py             # Conexión DB
│   ├── models/                 # SQLAlchemy models
│   │   ├── __init__.py
│   │   ├── user.py
│   │   ├── notebook.py
│   │   ├── cell.py
│   │   └── tool.py
│   ├── schemas/                # Pydantic schemas
│   │   ├── __init__.py
│   │   ├── user.py
│   │   ├── notebook.py
│   │   ├── cell.py
│   │   └── tool.py
│   ├── api/                    # API routes
│   │   ├── __init__.py
│   │   ├── auth.py
│   │   ├── notebooks.py
│   │   ├── cells.py
│   │   └── tools.py
│   ├── core/                   # Lógica de negocio
│   │   ├── __init__.py
│   │   ├── jupyter_kernel.py   # Gestión de kernels
│   │   ├── code_executor.py    # Ejecutor de código
│   │   ├── agent_runner.py     # Ejecutor de agentes
│   │   └── security.py         # Autenticación y autorización
│   └── utils/                  # Utilidades
│       ├── __init__.py
│       └── helpers.py
├── alembic/                    # Migraciones DB
├── tests/                      # Tests
├── requirements.txt
├── Dockerfile
└── docker-compose.yml
```

## 🔧 Funcionalidades Core

### 1. **Jupyter Kernel Manager**
- Gestión de kernels Python aislados por usuario
- Soporte para múltiples kernels simultáneos
- Limpieza automática de recursos
- Monitoreo de uso de memoria y CPU

### 2. **Code Executor**
- Ejecución segura de código Python
- Sandboxing para operaciones peligrosas
- Timeout configurable por celda
- Captura de stdout/stderr/errors

### 3. **Agent Framework**
- Sistema de herramientas personalizables
- Gestión de estado y memoria del agente
- Integración con modelos de IA externos
- Pipeline de ejecución configurable

### 4. **Real-time Communication**
- WebSocket para outputs en tiempo real
- Streaming de logs de ejecución
- Notificaciones de estado de celdas
- Colaboración en tiempo real

### 5. **Agent Reusability System**
- Sistema de versionado de agentes
- Marketplace de agentes compartidos
- Importación/exportación de agentes completos
- Sistema de dependencias entre agentes
- Templates de agentes para casos de uso comunes
- Métricas de rendimiento y uso de agentes

## 🔒 Seguridad

### Autenticación
- JWT tokens con refresh
- Rate limiting por endpoint
- Validación de roles y permisos
- Logs de auditoría

### Ejecución de Código
- Sandboxing con Docker containers
- Whitelist de librerías permitidas
- Timeout y límites de memoria
- Escaneo de código malicioso

### Base de Datos
- Prepared statements
- Validación de entrada
- Encriptación de datos sensibles
- Backup automático

## 📊 Monitoreo y Logging

### Métricas
- Tiempo de ejecución por celda
- Uso de memoria y CPU
- Tasa de éxito/error
- Usuarios concurrentes

### Logs
- Logs estructurados en JSON
- Niveles: DEBUG, INFO, WARNING, ERROR
- Rotación automática de logs
- Integración con sistemas de monitoreo

## 🚀 Deployment

### Docker
```dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
```

### Variables de Entorno
```bash
DATABASE_URL=postgresql://user:pass@localhost/dbname
REDIS_URL=redis://localhost:6379
JWT_SECRET_KEY=your-secret-key
JWT_ALGORITHM=HS256
ACCESS_TOKEN_EXPIRE_MINUTES=30
REFRESH_TOKEN_EXPIRE_DAYS=7
```

### Docker Compose
```yaml
version: '3.8'
services:
  backend:
    build: .
    ports:
      - "8000:8000"
    environment:
      - DATABASE_URL=postgresql://user:pass@db/dbname
      - REDIS_URL=redis://redis:6379
    depends_on:
      - db
      - redis
  
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: notebook_db
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  postgres_data:
```

## 🧪 Testing

### Tests Unitarios
- pytest para testing
- Cobertura mínima del 80%
- Mocks para servicios externos
- Tests de integración con DB

### Tests de API
- TestClient de FastAPI
- Validación de schemas
- Tests de autenticación
- Tests de ejecución de código

## 📈 Escalabilidad

### Horizontal
- Múltiples instancias del backend
- Load balancer con nginx
- Redis para sesiones y cache
- Queue system con Celery

### Vertical
- Optimización de queries DB
- Indexación de tablas
- Cache de resultados frecuentes
- Compresión de respuestas

## 🔄 Integración con Frontend

### CORS
```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000", "https://yourdomain.com"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
```

### WebSocket Events
```typescript
// Frontend events
interface WebSocketEvents {
  'cell:start': { cellId: string; timestamp: string };
  'cell:output': { cellId: string; output: string; type: 'stdout' | 'stderr' };
  'cell:complete': { cellId: string; status: 'success' | 'error'; executionTime: number };
  'cell:error': { cellId: string; error: string; traceback: string };
}
```

## 📝 Ejemplo de Implementación

### Modelo de Celda
```python
from sqlalchemy import Column, String, Text, Integer, DateTime, JSON
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.sql import func
from app.database import Base

class NotebookCell(Base):
    __tablename__ = "notebook_cells"
    
    id = Column(UUID(as_uuid=True), primary_key=True, default=func.gen_random_uuid())
    session_id = Column(UUID(as_uuid=True), ForeignKey("notebook_sessions.id"))
    type = Column(String(20), nullable=False)
    content = Column(Text, nullable=False)
    language = Column(String(20), nullable=False)
    order_index = Column(Integer, nullable=False)
    status = Column(String(20), default="idle")
    output = Column(Text)
    execution_time = Column(Float)
    metadata = Column(JSON, default={})
    created_at = Column(DateTime, default=func.now())
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())
```

### Schema de Celda
```python
from pydantic import BaseModel, Field
from typing import Optional, Dict, Any
from datetime import datetime
from uuid import UUID

class NotebookCellBase(BaseModel):
    type: str = Field(..., regex="^(code|config|tool|test|markdown)$")
    content: str
    language: str = Field(..., regex="^(python|json|yaml|markdown)$")
    order_index: int = Field(..., ge=0)
    metadata: Optional[Dict[str, Any]] = {}

class NotebookCellCreate(NotebookCellBase):
    pass

class NotebookCellUpdate(BaseModel):
    type: Optional[str] = Field(None, regex="^(code|config|tool|test|markdown)$")
    content: Optional[str] = None
    language: Optional[str] = Field(None, regex="^(python|json|yaml|markdown)$")
    order_index: Optional[int] = Field(None, ge=0)
    metadata: Optional[Dict[str, Any]] = None

class NotebookCell(NotebookCellBase):
    id: UUID
    session_id: UUID
    status: str = "idle"
    output: Optional[str] = None
    execution_time: Optional[float] = None
    created_at: datetime
    updated_at: datetime
    
    class Config:
        from_attributes = True
```

## 🎯 Próximos Pasos

1. **Setup del proyecto** con FastAPI y SQLAlchemy
2. **Configuración de la base de datos** PostgreSQL
3. **Implementación de modelos** y schemas
4. **API endpoints** básicos (CRUD)
5. **Sistema de autenticación** JWT
6. **Integración con Jupyter Kernel**
7. **WebSocket para tiempo real**
8. **Tests y documentación**
9. **Deployment con Docker**

## 📞 Contacto

Para dudas técnicas o aclaraciones sobre las especificaciones, contactar al equipo de desarrollo del frontend.

---

**Nota**: Este documento debe ser actualizado conforme evolucione el sistema. Mantener sincronización entre frontend y backend.
