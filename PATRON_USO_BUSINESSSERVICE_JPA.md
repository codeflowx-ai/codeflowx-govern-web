# Patrón de Uso Correcto de BusinessService según Tipo JPA

## 🎯 Principio Fundamental

El framework está diseñado para usar **SIEMPRE las JPAs** (tablas, views, procedures, functions).  
`BusinessService` tiene métodos específicos para cada tipo de JPA anotada con `@Entidad(type="...")`.

---

## 📋 Métodos Disponibles por Tipo

### 1. TABLES (type="TABLE")

```java
// Sin paginación - retorna List (máximo 999)
List<MiTabla> list = businessService.findAllEntity(MiTabla.class);

// Con paginación - retorna PageResult
PageResult<MiTabla> result = businessService.findAllEntity(MiTabla.class, pageParams);

// Con paginación y criterios
PageResult<MiTabla> result = businessService.findAllEntity(MiTabla.class, pageParams, criterias);

// Con paginación y filtros Map (⚠️ IGNORA LOS FILTROS - usa findByParams en su lugar)
PageResult<MiTabla> result = businessService.findAllEntity(MiTabla.class, pageParams, filters);
```

### 2. VIEWS (type="VIEW")

```java
// Sin paginación - retorna List (máximo 999)
List<MiView> list = businessService.findAllView(MiView.class);

// Con paginación - retorna PageResult
PageResult<MiView> result = businessService.findAllView(MiView.class, pageParams);

// Con paginación y criterios
PageResult<MiView> result = businessService.findAllView(MiView.class, pageParams, criterias);
```

### 3. PROCEDURES (type="PROCEDURE")

```java
// El bean debe tener los campos IN/OUT mapeados
MiProcedure proc = new MiProcedure();
proc.setParam1(valor1);  // IN parameter
proc.setParam2(valor2);  // IN parameter

MiProcedure result = businessService.callProcedure(proc);

// Leer valores OUT
String output = result.getOutParam();
```

### 4. FUNCTIONS (type="FUNCTION")

```java
// El bean debe tener los campos IN y un campo OUT mapeado
MiFunction func = new MiFunction();
func.setParam1(valor1);  // IN parameter

MiFunction result = businessService.callFuction(func);  // Nota: typo en el método original

// Leer valor de retorno
String returnValue = result.getReturnValue();
```

### 5. Queries Personalizadas (cualquier tipo)

```java
// Query nativa con parámetros nombrados
String sql = "SELECT * FROM MITABLA WHERE CAMPO = :valor AND ESTADO = :estado";
Map<String, Object> params = new HashMap<>();
params.put("valor", "test");
params.put("estado", "ACTIVO");

List<MiTabla> result = businessService.findByParams(MiTabla.class, sql, params);
```

---

## 🔍 Cómo Identificar el Tipo de JPA

### Por Ubicación del Package:
- `com.codeflowx.govern.entity.views.*` → **VIEW**
- `com.codeflowx.govern.entity.*` (sin views) → **TABLE** (generalmente)

### Por Anotación en la Entidad:
```java
@Entidad (
    namespace = "core",
    type = "VIEW",  // <-- Aquí está el tipo
    name = "V_ADMIN_DASHBOARD_SUMMARY"
)
public class AdminDashboardSummary { ... }
```

---

## ✅ Ejemplos Correctos

### Ejemplo 1: Cargar una VIEW

```java
// AdminDashboardSummary está en entity.views.core → es VIEW
PageResult<AdminDashboardSummary> result = businessService.findAllView(
    AdminDashboardSummary.class,
    pageParams
);

if (result != null && result.getContent() != null && !result.getContent().isEmpty()) {
    dashboardSummary = result.getContent().get(0);
}
```

### Ejemplo 2: Cargar una TABLE

```java
// ModuleStats está en entity.dashboard → es TABLE
PageResult<ModuleStats> result = businessService.findAllEntity(
    ModuleStats.class,
    pageParams
);

if (result != null && result.getContent() != null) {
    moduleStats = result.getContent();
}
```

### Ejemplo 3: Ejecutar PROCEDURE

```java
// Crear instancia del procedure
MyStoredProcedure proc = new MyStoredProcedure();
proc.setInputParam1("valor1");
proc.setInputParam2(123);

// Ejecutar
MyStoredProcedure result = businessService.callProcedure(proc);

// Leer outputs
String output = result.getOutputParam();
```

### Ejemplo 4: Query Personalizada

```java
// Para queries complejas o con filtros dinámicos
String sql = "SELECT * FROM MDLMODELS WHERE MDLSTATUS = :status AND MDLCATEGORY = :category LIMIT 100";
Map<String, Object> params = new HashMap<>();
params.put("status", "DEPLOYED");
params.put("category", "ML");

List<Model> models = businessService.findByParams(Model.class, sql, params);
```

---

## ❌ Errores Comunes

### ❌ Error 1: Usar `findAllEntity()` con VIEWS

```java
// MAL - AdminDashboardSummary es VIEW
PageResult<AdminDashboardSummary> result = businessService.findAllEntity(
    AdminDashboardSummary.class, pageParams
);
```

```java
// BIEN
PageResult<AdminDashboardSummary> result = businessService.findAllView(
    AdminDashboardSummary.class, pageParams
);
```

### ❌ Error 2: Usar filtros Map (no funcionan)

```java
// MAL - Los filtros Map son ignorados internamente
Map<String, Object> filters = new HashMap<>();
filters.put("status", "ACTIVE");
PageResult<Model> result = businessService.findAllEntity(
    Model.class, pageParams, filters  // ← Filtros ignorados
);
```

```java
// BIEN - Usar findByParams con SQL
String sql = "SELECT * FROM MDLMODELS WHERE MDLSTATUS = :status";
Map<String, Object> params = new HashMap<>();
params.put("status", "ACTIVE");
List<Model> result = businessService.findByParams(Model.class, sql, params);
```

### ❌ Error 3: Esperar que los métodos retornen tipos incorrectos

```java
// MAL - findByParams retorna List, no PageResult
PageResult<Model> result = businessService.findByParams(Model.class, sql, params);
```

```java
// BIEN
List<Model> result = businessService.findByParams(Model.class, sql, params);
```

---

## 📊 Resumen Rápido

| Tipo JPA | Método | Retorno | Uso |
|----------|--------|---------|-----|
| **TABLE** | `findAllEntity()` | `List<T>` o `PageResult<T>` | Tablas normales |
| **VIEW** | `findAllView()` | `List<T>` o `PageResult<T>` | Vistas de BD |
| **PROCEDURE** | `callProcedure()` | `T` (bean con OUT) | Stored procedures |
| **FUNCTION** | `callFuction()` | `T` (bean con retorno) | Funciones de BD |
| **Custom Query** | `findByParams()` | `List<T>` | Queries SQL personalizadas |

---

## 🎯 Checklist de Corrección

Al revisar un ViewModel:

1. ✅ Identificar el tipo de cada JPA usada (TABLE/VIEW/PROCEDURE/FUNCTION)
2. ✅ Usar el método correcto de BusinessService según el tipo
3. ✅ Para VIEWS: cambiar `findAllEntity()` → `findAllView()`
4. ✅ Para filtros: cambiar `findAllEntity(Class, PageParams, Map)` → `findByParams(Class, sql, params)`
5. ✅ Eliminar HashMap vacíos como tercer parámetro
6. ✅ Asegurar que el tipo de retorno coincida (List vs PageResult)
7. ✅ Para PROCEDURES/FUNCTIONS: usar `callProcedure()` / `callFuction()`

---

**Fecha:** 26 de octubre de 2025  
**Framework:** Enart NoCode Framework  
**Versión:** Basado en BusinessService v1.0

