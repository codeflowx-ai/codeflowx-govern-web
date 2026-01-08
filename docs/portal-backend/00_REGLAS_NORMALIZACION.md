# Reglas de Normalización y Arquitectura - Portal Backend

## Descripción General

Este documento contiene todas las reglas, estándares y convenciones que deben aplicarse en el desarrollo del portal backend de CodeFlowX. Sirve como referencia central para mantener consistencia y calidad en todos los módulos funcionales.

## 1. Reglas de Normalización de Base de Datos

### 1.1 Formas Normales Aplicadas

#### **1NF (Primera Forma Normal)**

- ✅ Eliminar grupos repetitivos
- ✅ Cada columna debe contener valores atómicos
- ✅ No arrays ni objetos JSON anidados (excepto cuando sea necesario para flexibilidad)

#### **2NF (Segunda Forma Normal)**

- ✅ Eliminar dependencias parciales
- ✅ Todas las columnas no-clave deben depender completamente de la clave primaria
- ✅ Crear tablas separadas para entidades independientes

#### **3NF (Tercera Forma Normal)**

- ✅ Eliminar dependencias transitivas
- ✅ Las columnas no-clave no deben depender de otras columnas no-clave
- ✅ Normalizar hasta el nivel de entidad

#### **BCNF (Boyce-Codd Normal Form)**

- ✅ Eliminar dependencias funcionales anómalas
- ✅ Cada determinante debe ser una clave candidata
- ✅ Aplicar en todas las tablas de relación

### 1.2 Reglas de Claves Primarias

#### **PK Única Autonumérica**

- ✅ Todas las tablas deben tener una PK única de tipo `BIGSERIAL`
- ✅ Nombrada siempre como `id`
- ✅ No usar UUIDs como PK (solo como identificadores externos si es necesario)

#### **Claves Foráneas**

- ✅ Siempre usar `BIGINT` para FK
- ✅ Referenciar siempre a la PK de la tabla padre
- ✅ Usar `ON DELETE CASCADE` solo cuando sea lógicamente correcto
- ✅ Usar `ON DELETE SET NULL` para relaciones opcionales

## 2. Prefijos de Objetos de Base de Datos

### 2.1 Prefijos de Tablas

| Módulo                | Prefijo | Descripción                            |
| --------------------- | ------- | -------------------------------------- |
| Core Modules          | `cor_`  | Usuarios, roles, menús, auditoría      |
| Project Management    | `prj_`  | Proyectos, clientes, licencias         |
| AI Training           | `trn_`  | Jobs, datasets, experimentos           |
| RAG System            | `rag_`  | Documentos, embeddings, chat           |
| Model Management      | `mdl_`  | Modelos, versiones, artefactos         |
| Technology Management | `tch_`  | Tecnologías, stacks, repositorios      |
| Domain Ingestion      | `dmn_`  | Dominios, jobs, procesamiento          |
| Model Evaluation      | `evl_`  | Evaluaciones, métricas, reportes       |
| Serving Module        | `srv_`  | Despliegues, endpoints, monitorización |
| Governance            | `gov_`  | Políticas, seguridad, compliance       |
| Plugins               | `plg_`  | Plugins, configuraciones, ejecuciones  |
| User Training         | `edu_`  | Cursos, lecciones, certificaciones     |
| Notifications         | `ntf_`  | Notificaciones, plantillas, entregas   |
| Roadmap               | `rdm_`  | Features, releases, milestones         |
| Generator             | `gen_`  | Templates, scaffolding, generación     |

### 2.2 Prefijos de Vistas

| Tipo                  | Prefijo | Ejemplo                |
| --------------------- | ------- | ---------------------- |
| Vistas de consulta    | `vw_`   | `vw_cor_user_roles`    |
| Vistas materializadas | `mv_`   | `mv_cor_user_activity` |
| Vistas de reportes    | `rp_`   | `rp_cor_audit_summary` |

### 2.3 Prefijos de Secuencias

| Tipo                  | Prefijo | Ejemplo                |
| --------------------- | ------- | ---------------------- |
| Secuencias de tablas  | `seq_`  | `seq_cor_users_id`     |
| Secuencias de negocio | `bus_`  | `bus_prj_project_code` |

### 2.4 Prefijos de Funciones y Procedimientos

| Tipo                | Prefijo | Ejemplo                        |
| ------------------- | ------- | ------------------------------ |
| Funciones simples   | `fn_`   | `fn_cor_get_user_permissions`  |
| Funciones complejas | `fnc_`  | `fnc_cor_calculate_user_stats` |
| Stored Procedures   | `sp_`   | `sp_cor_bulk_user_import`      |
| Triggers            | `trg_`  | `trg_cor_users_audit`          |

## 3. Reglas de Optimización de Base de Datos

### 3.1 Uso de Vistas

#### **Cuándo Usar Vistas**

- ✅ **Consultas complejas frecuentes** - Para evitar repetir JOINs complejos
- ✅ **Agregaciones comunes** - Sumas, promedios, conteos frecuentes
- ✅ **Filtros de seguridad** - Para control de acceso a nivel de fila
- ✅ **Consultas de reportes** - Para dashboards y análisis

#### **Ejemplos de Vistas Recomendadas**

```sql
-- Vista para roles de usuario con permisos
CREATE VIEW vw_cor_user_roles_permissions AS
SELECT u.id, u.username, r.name as role_name, p.resource, p.action
FROM cor_users u
JOIN cor_user_roles ur ON u.id = ur.user_id
JOIN cor_roles r ON ur.role_id = r.id
JOIN cor_role_permissions rp ON r.id = rp.role_id
JOIN cor_permissions p ON rp.permission_id = p.id
WHERE u.is_active = true AND r.is_active = true;

-- Vista para estadísticas de usuarios por departamento
CREATE VIEW vw_cor_user_department_stats AS
SELECT d.name as department_name,
       COUNT(u.id) as user_count,
       COUNT(CASE WHEN u.is_active THEN 1 END) as active_users
FROM cor_departments d
LEFT JOIN cor_users u ON d.id = u.department_id
GROUP BY d.id, d.name;
```

### 3.2 Uso de Funciones de Base de Datos

#### **Cuándo Usar Funciones**

- ✅ **Cálculos complejos** - Lógica matemática o de negocio compleja
- ✅ **Validaciones** - Reglas de negocio que deben aplicarse en BD
- ✅ **Transformaciones de datos** - Conversiones, formateo, etc.
- ✅ **Lógica reutilizable** - Operaciones que se usan en múltiples lugares

#### **Ejemplos de Funciones Recomendadas**

```sql
-- Función para calcular edad del usuario
CREATE OR REPLACE FUNCTION fn_cor_calculate_user_age(birth_date DATE)
RETURNS INTEGER AS $$
BEGIN
    RETURN EXTRACT(YEAR FROM AGE(birth_date));
END;
$$ LANGUAGE plpgsql;

-- Función para validar email
CREATE OR REPLACE FUNCTION fn_cor_validate_email(email TEXT)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$';
END;
$$ LANGUAGE plpgsql;
```

### 3.3 Uso de Stored Procedures

#### **Cuándo Usar Stored Procedures**

- ✅ **Operaciones transaccionales complejas** - Múltiples operaciones en una transacción
- ✅ **Importación masiva de datos** - Operaciones ETL complejas
- ✅ **Mantenimiento de datos** - Limpieza, archivo, etc.
- ✅ **Operaciones de auditoría** - Logging complejo de operaciones

#### **Ejemplo de Stored Procedure**

```sql
-- Procedimiento para crear usuario con roles
CREATE OR REPLACE PROCEDURE sp_cor_create_user_with_roles(
    p_username VARCHAR(50),
    p_email VARCHAR(100),
    p_password VARCHAR(255),
    p_first_name VARCHAR(50),
    p_last_name VARCHAR(50),
    p_department_id BIGINT,
    p_role_ids BIGINT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
    v_role_id BIGINT;
BEGIN
    -- Crear usuario
    INSERT INTO cor_users (username, email, password, first_name, last_name, department_id)
    VALUES (p_username, p_email, p_password, p_first_name, p_last_name, p_department_id)
    RETURNING id INTO v_user_id;

    -- Asignar roles
    FOREACH v_role_id IN ARRAY p_role_ids
    LOOP
        INSERT INTO cor_user_roles (user_id, role_id) VALUES (v_user_id, v_role_id);
    END LOOP;

    -- Log de auditoría
    INSERT INTO cor_audit_logs (user_id, action, resource, resource_id)
    VALUES (v_user_id, 'CREATE_USER', 'USER', v_user_id::TEXT);

    COMMIT;
END;
$$;
```

## 4. Reglas de Arquitectura de Software

### 4.1 Principio KISS (Keep It Simple, Stupid)

#### **Aplicación en Código**

- ✅ **Métodos simples** - Una función, una responsabilidad
- ✅ **Nombres descriptivos** - Evitar abreviaciones confusas
- ✅ **Lógica clara** - Evitar complejidad innecesaria
- ✅ **Comentarios cuando sea necesario** - Solo para lógica compleja

#### **Aplicación en Base de Datos**

- ✅ **Tablas normalizadas** - Evitar desnormalización innecesaria
- ✅ **Índices simples** - Solo los necesarios para rendimiento
- ✅ **Consultas legibles** - Evitar subconsultas anidadas excesivas

### 4.2 Principios SOLID

#### **S - Single Responsibility Principle**

- ✅ Cada clase/entidad tiene una sola responsabilidad
- ✅ Cada tabla almacena datos de una sola entidad
- ✅ Cada servicio maneja un solo dominio de negocio

#### **O - Open/Closed Principle**

- ✅ Extensible sin modificar código existente
- ✅ Usar herencia y composición apropiadamente
- ✅ Diseñar esquemas de BD para futuras extensiones

#### **L - Liskov Substitution Principle**

- ✅ Las subclases pueden sustituir a las clases base
- ✅ Mantener consistencia en contratos de interfaz
- ✅ Polimorfismo en servicios y repositorios

#### **I - Interface Segregation Principle**

- ✅ Interfaces pequeñas y específicas
- ✅ Evitar interfaces "gordas" con muchos métodos
- ✅ Separar responsabilidades en interfaces distintas

#### **D - Dependency Inversion Principle**

- ✅ Depender de abstracciones, no de implementaciones
- ✅ Inyección de dependencias en servicios
- ✅ Uso de interfaces para repositorios

### 4.3 Test Driven Development (TDD)

#### **Ciclo Red-Green-Refactor**

- ✅ **Red** - Escribir test que falle
- ✅ **Green** - Implementar código que pase el test
- ✅ **Refactor** - Mejorar código manteniendo tests pasando

#### **Aplicación en Backend**

- ✅ **Tests unitarios** para todos los servicios
- ✅ **Tests de integración** para repositorios
- ✅ **Tests de base de datos** para funciones y procedimientos
- ✅ **Cobertura mínima del 80%**

### 4.4 Arquitectura Hexagonal (Ports & Adapters)

#### **Estructura de Capas**

```
┌─────────────────────────────────────┐
│           Presentation Layer        │  ← Controllers, DTOs
├─────────────────────────────────────┤
│           Application Layer         │  ← Services, Use Cases
├─────────────────────────────────────┤
│           Domain Layer              │  ← Entities, Value Objects
├─────────────────────────────────────┤
│           Infrastructure Layer      │  ← Repositories, External APIs
└─────────────────────────────────────┘
```

#### **Implementación**

- ✅ **Domain Layer** - Entidades y lógica de negocio pura
- ✅ **Application Layer** - Orquestación de casos de uso
- ✅ **Infrastructure Layer** - Implementación de repositorios
- ✅ **Presentation Layer** - Controllers y DTOs

## 5. Reglas de Nomenclatura

### 5.1 Base de Datos

#### **Tablas**

- ✅ **Singular** para entidades principales: `cor_user`, `prj_project`
- ✅ **Plural** para tablas de relación: `cor_user_roles`, `prj_project_members`
- ✅ **Snake_case** para nombres de columnas: `first_name`, `created_at`

#### **Columnas**

- ✅ **id** - Clave primaria (siempre)
- ✅ **created_at** - Timestamp de creación (siempre)
- ✅ **updated_at** - Timestamp de actualización (siempre)
- ✅ **is_active** - Flag de activación (siempre)
- ✅ **deleted_at** - Soft delete (cuando sea necesario)

### 5.2 Código Java

#### **Clases**

- ✅ **PascalCase** para nombres de clase: `UserService`, `ProjectRepository`
- ✅ **Sufijos descriptivos**: `Service`, `Repository`, `Controller`, `Entity`

#### **Métodos**

- ✅ **camelCase** para nombres de método: `createUser()`, `findByDepartmentId()`
- ✅ **Verbos descriptivos**: `create`, `update`, `delete`, `find`, `get`

#### **Variables**

- ✅ **camelCase** para variables: `userName`, `departmentId`
- ✅ **Nombres descriptivos**: Evitar `i`, `temp`, `data`

## 6. Reglas de Rendimiento

### 6.1 Índices

#### **Índices Obligatorios**

- ✅ **Claves primarias** - Automático en PostgreSQL
- ✅ **Claves foráneas** - Para optimizar JOINs
- ✅ **Columnas de búsqueda** - `username`, `email`, `code`
- ✅ **Columnas de ordenamiento** - `created_at`, `updated_at`

#### **Índices Compuestos**

- ✅ **Consultas frecuentes** - Combinaciones de filtros comunes
- ✅ **Orden de columnas** - Más selectivas primero
- ✅ **Evitar exceso** - Solo los necesarios para rendimiento

### 6.2 Consultas

#### **Optimizaciones**

- ✅ **SELECT específico** - No usar `SELECT *`
- ✅ **JOINs eficientes** - Usar índices apropiados
- ✅ **Paginación** - `LIMIT` y `OFFSET` para grandes conjuntos
- ✅ **Evitar N+1** - Usar JOINs o consultas en lote

## 7. Reglas de Seguridad

### 7.1 Autenticación y Autorización

#### **Implementación**

- ✅ **JWT tokens** para autenticación
- ✅ **Roles y permisos** granulares
- ✅ **Validación de entrada** en todas las APIs
- ✅ **Auditoría completa** de todas las operaciones

#### **Base de Datos**

- ✅ **Usuarios de BD limitados** - Solo permisos necesarios
- ✅ **Encriptación** de datos sensibles
- ✅ **Backup regular** de datos
- ✅ **Logs de auditoría** en tablas separadas

### 7.2 Validación de Datos

#### **Niveles de Validación**

- ✅ **Frontend** - Validación de UI para experiencia de usuario
- ✅ **Backend** - Validación de API para seguridad
- ✅ **Base de Datos** - Constraints para integridad

## 8. Reglas de Mantenimiento

### 8.1 Documentación

#### **Requerida**

- ✅ **README** para cada módulo
- ✅ **Comentarios SQL** en todas las tablas y columnas
- ✅ **JavaDoc** en todas las clases públicas
- ✅ **Diagramas ER** para esquemas complejos

### 8.2 Versionado

#### **Base de Datos**

- ✅ **Scripts de migración** para cada cambio
- ✅ **Versionado de esquemas** en metadatos
- ✅ **Rollback scripts** para cambios críticos

#### **Código**

- ✅ **Semantic Versioning** para releases
- ✅ **Changelog** detallado de cambios
- ✅ **Tags de Git** para versiones estables

## 9. Reglas de Testing

### 9.1 Cobertura de Tests

#### **Mínimos Requeridos**

- ✅ **Unit Tests**: 80% de cobertura
- ✅ **Integration Tests**: 70% de cobertura
- ✅ **Database Tests**: 90% de cobertura de funciones y procedimientos

### 9.2 Tipos de Tests

#### **Unit Tests**

- ✅ **Services** - Lógica de negocio
- ✅ **Repositories** - Acceso a datos
- ✅ **Validators** - Validación de entrada

#### **Integration Tests**

- ✅ **API endpoints** - Flujos completos
- ✅ **Database operations** - Operaciones CRUD
- ✅ **External services** - Mocks apropiados

## 10. Reglas de Deployment

### 10.1 Entornos

#### **Configuración**

- ✅ **Development** - Configuración local
- ✅ **Staging** - Configuración de pruebas
- ✅ **Production** - Configuración optimizada

### 10.2 Base de Datos

#### **Migraciones**

- ✅ **Automáticas** en deployment
- ✅ **Rollback** en caso de fallo
- ✅ **Backup** antes de migraciones críticas

## Conclusión

Estas reglas proporcionan un marco sólido para el desarrollo del portal backend de CodeFlowX. Su aplicación garantiza:

- **Consistencia** en todos los módulos
- **Calidad** del código y base de datos
- **Mantenibilidad** a largo plazo
- **Escalabilidad** del sistema
- **Seguridad** de la aplicación

**Nota**: Este documento debe actualizarse regularmente conforme se añadan nuevas reglas o se modifiquen las existentes.
