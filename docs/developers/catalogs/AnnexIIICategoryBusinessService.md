# AnnexIIICategoryBusinessService

**Ubicación:** `com.codeflowx.govern.business.catalogs.AnnexIIICategoryBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Gestiona categorías del Anexo III del EU AI Act que definen sistemas de IA de alto riesgo.

---

## 🎯 Responsabilidades

- Consultar categorías del Anexo III
- Verificar si un sistema pertenece a una categoría de alto riesgo
- Gestionar mapeo de sistemas a categorías

---

## 📚 API Pública

### `findAllCategories()`

Obtiene todas las categorías del Anexo III.

**Retorna:** `List<AnnexIIICategory>`

---

### `findCategoryByCode(String categoryCode)`

Obtiene categoría por código.

---

### `isHighRiskCategory(String categoryCode)`

Verifica si una categoría es de alto riesgo.

**Retorna:** `Boolean`

---

## 📖 Referencias

- **Anexo III EU AI Act:** Categorías de sistemas de IA de alto riesgo
- **ViewModels:** HighRiskClassifierViewModel

---

**Última actualización:** 25 de noviembre de 2025
