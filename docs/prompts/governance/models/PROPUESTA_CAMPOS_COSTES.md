# Propuesta: Campos de Costes en Entidad Model

## 📊 Análisis Actual

### Entidades Existentes con Costes

1. **ModelUsage** (registro individual de uso)
   - `modinputtokens`, `modoutputtokens`, `modtotaltokens`
   - `modinputcost`, `modoutputcost`, `modtotalcost`
   - Relación: `ManyToOne` con `ModelCatalog`

2. **ModelCatalog** (agregación de uso)
   - `modtotaltokensconsumed` (Long)
   - `modtotalcost` (BigDecimal)
   - `modusagecount` (Long)

3. **Model** (entidad principal)
   - ❌ **NO tiene campos de costes**
   - ❌ **NO tiene relación directa con ModelUsage o ModelCatalog**

---

## 💡 Propuesta: Agregar Campos de Costes en Model

### Campos a Agregar

```java
// ========================================
// COSTES Y CONSUMO DE TOKENS
// ========================================

@Column(name = "MODTOTALTOKENS", nullable = true)
@Field(criteria = true, auditar = true, filter = true, label = "Total Tokens Consumidos", type = "LONG")
private Long modtotaltokens;

@Column(name = "MODTOTALCOST", nullable = true)
@Field(criteria = true, auditar = true, filter = true, label = "Coste Total USD", type = "DECIMAL")
private BigDecimal modtotalcost;

@Column(name = "MODMONTHLYCOST", nullable = true)
@Field(criteria = true, auditar = true, filter = true, label = "Coste Mensual USD", type = "DECIMAL")
private BigDecimal modmonthlycost;

@Column(name = "MODDAILYCOST", nullable = true)
@Field(criteria = true, auditar = true, filter = true, label = "Coste Diario USD", type = "DECIMAL")
private BigDecimal moddailycost;

@Column(name = "MODCOSTPERTOKEN", nullable = true)
@Field(criteria = true, auditar = false, filter = false, label = "Coste por Token USD", type = "DECIMAL")
private BigDecimal modcostpertoken;

@Column(name = "MODUSAGECOUNT", nullable = true)
@Field(criteria = true, auditar = true, filter = true, label = "Total Usos", type = "LONG")
private Long modusagecount;

@Column(name = "MODLASTCOSTUPDATE", nullable = true)
@Field(criteria = false, auditar = false, filter = false, label = "Última Actualización Costes", type = "TIMESTAMP")
private Timestamp modlastcostupdate;
```

### Ventajas

1. ✅ **Consultas más rápidas**: No requiere joins con ModelUsage/ModelCatalog
2. ✅ **Simplicidad**: Acceso directo desde Model
3. ✅ **Agregación automática**: Se actualizan mediante triggers o jobs periódicos
4. ✅ **Compatibilidad**: No rompe código existente (campos nullable)

### Desventajas

1. ⚠️ **Redundancia**: Datos duplicados (también en ModelCatalog)
2. ⚠️ **Sincronización**: Requiere mantener sincronizados Model y ModelCatalog
3. ⚠️ **Migración**: Requiere script SQL para calcular valores iniciales

---

## 🔄 Alternativa: Consultar ModelUsage/ModelCatalog

### Opción 2: Sin modificar Model

Consultar costes agregando datos de:
- `ModelUsage` relacionados (a través de ModelCatalog o directamente)
- `ModelCatalog` si existe relación

**Ventajas:**
- ✅ No modifica entidad Model
- ✅ Fuente única de verdad

**Desventajas:**
- ❌ Consultas más complejas (joins)
- ❌ Requiere verificar relación Model ↔ ModelCatalog
- ❌ Menos eficiente para consultas frecuentes

---

## ✅ Recomendación

**Agregar campos en Model** por las siguientes razones:

1. **Performance**: Consultas más rápidas para el frontend
2. **Simplicidad**: Endpoints más simples de implementar
3. **Escalabilidad**: Model es la entidad principal consultada
4. **Mantenimiento**: Se actualizan mediante jobs/triggers automáticos

### Estrategia de Actualización

1. **Job periódico**: Actualizar campos agregados cada hora/día
2. **Trigger**: Actualizar al insertar/actualizar ModelUsage
3. **Evento**: Actualizar al finalizar requests de modelos

---

## 📝 Implementación

1. Agregar campos en `Model.java`
2. Crear migración SQL para agregar columnas
3. Crear job/trigger para actualizar valores
4. Implementar endpoints que usen estos campos
5. Mantener sincronización con ModelUsage/ModelCatalog

---

**¿Procedemos con esta propuesta?**
