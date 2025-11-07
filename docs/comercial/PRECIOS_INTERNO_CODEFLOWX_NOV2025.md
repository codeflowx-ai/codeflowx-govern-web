# TARIFA INTERNA CODEFLOWX - NOVIEMBRE 2025
## Precios Referencia España (EUR, sin IVA)

**Confidencialidad:** Uso interno CodeflowX  
**Fecha:** Noviembre 2025  
**Coste operativo base cloud:** 35 €/mes por cliente (infraestructura SaaS)

---

## 🎯 Objetivo

Definir una estructura de precios interna que cubra:
- **Cloud Compliance** para empresas pequeñas/medianas (desarrolladores, agencias, consultoras)
- **Planes OEM** para partners que quieren reempaquetar CodeflowX como parte de su oferta
- **Licenciamiento self-hosting (Corporate)** vendido a través de partners
- **Planes Administraciones Públicas** diferenciando pequeños ayuntamientos y organismos grandes

Los precios aseguran margen >70% sobre el coste operativo cloud (35 €/mes) y establecen coherencia entre valor entregado y complejidad de soporte.

---

## 🟢 CLOUD (SaaS gestionado por CodeflowX)

| Plan | Precio (EUR/mes) | Coste Infra (EUR) | Margen Bruto | Enfoque | Cobertura |
|------|------------------|-------------------|--------------|---------|-----------|
| **Cloud Compliance (Basic)** | **149 €** | 35 € | 76% | Freelance, microagencias (≤10 personas) que necesitan **cumplimiento esencial** | Hasta **2 sistemas IA** (chatbots/agentes/RAG), checklists Art. 9-15, documentación Anexo IV básica, políticas Art. 50, FRIA templates, 2 usuarios, soporte email 48h |
| **Cloud Governance (Professional)** | **399 €** | 70 € (2 instancias) | 82% | Agencias/consultoras 10-50 personas y software factories que necesitan **gobierno completo** | Hasta **7 sistemas IA**, workflows BPMN, evaluación métricas, monitorización 24/7, alertas desviaciones, reporting Art. 71, 12 usuarios, soporte prioritario 12h |
| **Cloud Enterprise** | **799 €** | 105 € (3 instancias) | 87% | Partners SaaS y organizaciones con múltiples clientes internos. Incluye **solución completa** (cumplimiento + gobierno + evaluación + monitorización) multi-tenant | Multi-tenant, marca blanca opcional, API configurables, dashboards agregados, SLA 99,5 %, soporte 4h, formación comercial, controles centralizados |

**Notas Cloud:**
- Precios sin inferencia incluida. CodeflowX se integra con la infraestructura de modelos existente del cliente. Se ofrece un módulo opcional de servidores de inferencia (ver add-ons).
- Facturación mensual; pago anual anticipado (‑10 %).
- Número de sistemas IA = combinaciones de chatbots, agentes, RAG, scoring u otros flows gobernados. Packs adicionales disponibles.
- Upgrade/downgrade prorrateado en el mes corriente.

---

## 🧩 ADD-ONS CLOUD

| Add-on | Precio | Disponible en | Descripción |
|--------|--------|----------------|-------------|
| **Onboarding guiado (2 semanas)** | 900 € único | Basic / Professional | Configuración inicial, mapeo procesos, primera auditoría |
| **Integración SSO (Azure AD/Okta)** | 600 € único | Professional / Enterprise | Autenticación corporativa |
| **Pack sistemas adicionales (3 sistemas)** | 90 € / mes | Todos | Amplía cobertura a más chatbots/agentes/RAG |
| **Entorno regulado (datos UE dedicados)** | +90 € / mes | Professional / Enterprise | Workspace aislado multi-organización |
| **Soporte 24/7** | +150 € / mes | Professional / Enterprise | Línea dedicada incidentes críticos |
| **Módulo servidores de inferencia** | 120 € / mes por instancia | Professional / Enterprise | Despliegue gestionado de `leka-llm-interpreter`, incluye modelos open-source, wrappers comerciales opcionales, routing inteligente. Costes API externos se facturan al cliente. |

---

## 🏢 CORPORATE SELF-HOST (Licencia anual vía partners)

| Plan | Precio Licencia Anual | Mantenimiento & Soporte | Requisitos | Canal | Características |
|------|-----------------------|-------------------------|------------|-------|------------------|
| **Corporate Standard** | **24.000 €/año** | 4.800 €/año (20%) | 2 nodos, 100 usuarios | Partner certificado (25% margen) | Kubernetes o bare-metal, 5 sistemas IA incluidos, workflows personalizables, soporte L3 (SLA 8h), actualizaciones trimestrales |
| **Corporate Enterprise** | **48.000 €/año** | 9.600 €/año (20%) | 5 nodos, 500 usuarios | Partner Elite (30% margen) | Multi-entorno (dev/pre/prod), auditoría avanzada, integración SIEM/SOC, soporte L3 (SLA 4h), roadmap dedicado |

**Servicios adicionales Corporate:**
- Implantación onsite (consultoría partner) recomendada 10-20k€
- Formación técnica certificada partner: 2.000 € (3 días)
- Auditoría anual cumplimiento: 3.500 € (opcional)

---

## 🏛️ ADMINISTRACIONES PÚBLICAS

| Plan | Precio Licencia Anual | Tipo Administración | Modelo Implantación | Observaciones |
|------|-----------------------|---------------------|----------------------|---------------|
| **Public Local** | **12.000 €** | Ayuntamientos <100.000 habitantes, organismos municipales | Cloud dedicado (SaaS región UE) o self-host básico | 2 sistemas IA, FRIA integrada, soporte 12h, formación 1 día, **incluye cumplimiento + gobierno + evaluación + monitorización** |
| **Public Regional** | **24.000 €** | Diputaciones, Comunidades Autónomas, hospitales regionales | Self-host híbrido o cloud dedicado | 5 sistemas IA, reporting Art. 71 automatizado, soporte 8h, formación 2 días, **solución integral (cumplimiento + gobierno + evaluación + monitorización)** |
| **Public National** | **60.000 €** | Ministerios, agencias estatales, fuerzas seguridad | Self-host clusters + alta disponibilidad | 10 sistemas IA, cumplimiento ENS Alto, soporte 4h, formación 3 días, auditoría anual incluida, **solución completa CodeflowX** |

**Partners sector público:**
- Venta obligatoria vía partner homologado (20-25% margen)
- Procesos RFP: incluir soporte técnico CodeflowX en preventa
- Descuento early adopter 10% si firma antes Abr 2026

---

## 🔄 TABLA COMPARATIVA RÁPIDA

| Segmento | Plan | Precio | Márgenes | Canal | Comentarios |
|----------|------|--------|----------|-------|-------------|
| Freelance/microagencia | Cloud Basic | 149 €/mes | 76% | Directo | 1 sistema IA alto riesgo, compliance rápido |
| Agencia/consultora mediana | Cloud Professional | 399 €/mes | 82% | Directo/Partner | Hasta 5 sistemas, workflows avanzados |
| Partner SaaS (white-label) | Cloud OEM | 799 €/mes + variable | 87% | Directo/Partner | Marca blanca, multi-tenant |
| Enterprise | Corporate Standard | 24k €/año | 80% | Partner | Self-host, soporte L3 |
| Enterprise avanzadas | Corporate Enterprise | 48k €/año | 80% | Partner Elite | Roadmap dedicado, HA |
| Ayuntamiento pequeño | Public Local | 12k €/año | 72% | Partner público | Cloud dedicado, FRIA |
| Organismo regional | Public Regional | 24k €/año | 80% | Partner | Self-host híbrido |
| Organismo nacional | Public National | 60k €/año | 82% | Partner Elite | ENS Alto, auditoría incluida |

---

## 📌 SUPUESTOS FINANCIEROS

- Coste operativo cloud fijo: 35 €/mes por instancia básica (Compute + almacenamiento + monitorización)
- Coste soporte medio: 1h/mes técnico N2 (valorado 35 €) → incluido en margen
- Descuentos máximos sin aprobación dirección:
  - Cloud Basic/Professional: hasta 10% (anualidad)
  - Cloud OEM: negociación caso a caso (mínimo 650 €/mes)
  - Corporate/Public: hasta 15% si volumen ≥3 licencias
- Partners reciben margen estándar:
  - OEM: comisión 15%
  - Corporate Standard: 25%
  - Corporate Enterprise: 30%
  - Public sector: 20-25% según homologación

---

## 🚀 ESTRATEGIA COMERCIAL RESUMIDA

1. **Cloud Basic** → Captar volumen (meta 200 clientes año 1). Upsell a Professional.
2. **Cloud Professional** → Enfocado en agencias/consultoras mid-market (meta 120 clientes año 1).
3. **Cloud OEM** → 20 partners SaaS en 12 meses (metas 800€/mes + variable).
4. **Corporate Self-host** → 30 licencias Standard + 10 Enterprise vía partners (objetivo 1,44M€/año).
5. **Public Sector** → 40 licencias Local + 10 Regional + 3 National (objetivo 1,32M€/año).

---

**Documento preparado por:** Equipo Strategy & Pricing  
**Revisado por:** Dirección Comercial  
**Última actualización:** Noviembre 2025
