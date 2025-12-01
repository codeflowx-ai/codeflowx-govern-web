# QualityManagementSystemBusinessService

**Ubicación:** `com.codeflowx.govern.business.compliance.QualityManagementSystemBusinessService`
**Módulo:** `codeflowx.govern.business`
**Fecha:** 25 de noviembre de 2025

---

## 📋 Descripción Funcional

Implementa el sistema de gestión de calidad (QMS) obligatorio para sistemas de IA de alto riesgo según Art. 17 del EU AI Act.

---

## 🎯 Responsabilidades

- Gestionar los 13 módulos mandatorios del QMS (Art. 17)
- Calcular score de compliance del QMS
- Verificar cumplimiento de cada módulo

---

## 📚 Módulos del QMS (Art. 17)

1. **a)** Estrategia cumplimiento normativo
2. **b)** Control y verificación diseño
3. **c)** Desarrollo y aseguramiento calidad
4. **d)** Examen, prueba, validación
5. **e)** Especificaciones técnicas/normas
6. **f)** Sistemas gestión de datos
7. **g)** Sistema gestión riesgos (Art. 9)
8. **h)** Vigilancia poscomercialización (Art. 72)
9. **i)** Notificación incidentes graves (Art. 73)
10. **j)** Comunicación autoridades
11. **k)** Registro documentación
12. **l)** Gestión recursos
13. **m)** Marco rendición cuentas

---

## 📚 API Pública

### `calculateQmsComplianceScore(Long projectId)`

Calcula score de compliance del QMS (0.00 - 1.00).

**Parámetros:**
- `projectId`: ID del proyecto

**Retorna:** `BigDecimal` - Score basado en cumplimiento de los 13 módulos

**Uso:** Llamado por `ComplianceAssessmentBusinessService` en Step 2.

---

## 📖 Referencias

- **Art. 17 EU AI Act:** Quality Management System
- **Prompt:** INC-017
- **Integración:** Usado por ComplianceAssessmentBusinessService

---

**Nota:** Algunos métodos tienen implementación parcial (marcados con TODO).

---

**Última actualización:** 25 de noviembre de 2025
