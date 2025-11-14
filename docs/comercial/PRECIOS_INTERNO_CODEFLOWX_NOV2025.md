# TARIFA INTERNA CODEFLOWX - NOVIEMBRE 2025
## Precios Referencia España (EUR, sin IVA)

**Confidencialidad:** Uso interno CodeflowX  
**Fecha:** Noviembre 2025  
**Coste operativo base cloud:** 35 €/mes por cliente (infraestructura SaaS)

---

## 📦 Definición clave: Proyecto gobernado

- Un **proyecto** representa un caso de uso gobernado end-to-end (ej. "chatbot soporte RR.HH.", "agente scoring crédito", "RAG documentación legal").
- Cada proyecto puede incluir, como máximo, los siguientes activos:
  - **2 chatbots** (interfaces conversacionales)
  - **2 agentes** autónomos/flows de decisión
  - **2 RAG** (vector store + orquestación)
- El cliente puede activar solo los activos que necesite dentro de cada proyecto, pero no exceder el límite. Esto evita usos fraudulentos (p.ej. un plan básico gobernando decenas de bots bajo un único proyecto).
- Para aumentar la capacidad se pueden contratar **proyectos adicionales** (ver add-ons).

---

## 🟢 CLOUD (SaaS gestionado por CodeflowX)

| Plan | Precio (EUR/mes) | Proyectos incluidos | Coste Infra (EUR) | Margen Bruto | Enfoque | Cobertura |
|------|------------------|---------------------|-------------------|--------------|---------|-----------|
| **CodeflowX Compliance Cloud – Plug & Protect** | **149 €** | **1 proyecto** (cumplimiento esencial) | 35 € | 76% | Freelance, microagencias (≤10 personas) que necesitan **cumplimiento** para un caso de uso puntual | 1 proyecto con hasta 2 chatbots + 2 agentes + 2 RAG, checklists Art. 9-15, documentación Anexo IV básica, políticas Art. 50, FRIA templates, 2 usuarios, soporte email 48h |
| **Cloud Governance (Professional)** | **699 €** | **3 proyectos** (gobierno completo) | 70 € (2 instancias) | 90% | Agencias/consultoras 10-50 personas y software factories que gestionan varios casos de uso | 3 proyectos (cada uno hasta 2 chatbots + 2 agentes + 2 RAG), workflows BPMN, evaluación métricas, monitorización 24/7, alertas desviaciones, reporting Art. 71, 12 usuarios, soporte prioritario 12h |
| **Cloud Enterprise** | **1.199 €** | **5 proyectos** compartidos por hasta 5 clientes externos (multi-tenant) | 105 € (3 instancias) | 91% | Partners SaaS y organizaciones multi-cliente, incluye **solución completa** | Multi-tenant (5 clientes x 1 proyecto cada uno; proyectos adicionales vía add-on), marca blanca, API configurables, dashboards agregados, SLA 99,5 %, soporte 4h, formación comercial, controles centralizados |

**Notas Cloud:**
- Facturación mensual; pago anual anticipado (‑10 %).
- Proyectos adicionales se contratan vía add-on (ver abajo). 
- Los proyectos pueden reasignarse (p.ej. cerrar uno y abrir nuevo) previa baja documentada.
- CodeflowX se integra con la infraestructura de modelos del cliente. El módulo de servidores de inferencia es opcional.

---

## 🧩 ADD-ONS CLOUD

| Add-on | Precio | Disponible en | Descripción |
|--------|--------|----------------|-------------|
| **Onboarding guiado (2 semanas)** | 900 € único | Basic / Professional | Configuración inicial, mapeo procesos, primera auditoría |
| **Integración SSO (Azure AD/Okta)** | 600 € único | Professional / Enterprise | Autenticación corporativa |
| **Pack proyectos adicionales (1 proyecto)** | 120 € / mes | Todos | Añade 1 proyecto (con sus límites de activos) |
| **Pack clientes adicionales (5 tenants)** | 150 € / mes | Enterprise | Amplía el número de organizaciones servidas (de 5 en 5) |
| **Entorno regulado (datos UE dedicados)** | +90 € / mes | Professional / Enterprise | Workspace aislado multi-organización |
| **Soporte 24/7** | +150 € / mes | Professional / Enterprise | Línea dedicada incidentes críticos |
| **Activación módulo inferencia (gestión)** | 90 € / mes | Professional / Enterprise | Despliegue y gestión de `leka-llm-interpreter` (routing, wrappers, monitorización). Requiere elegir modalidad compute. |
| **Compute GPU dedicada** | Coste repercutido (desde 250 € / mes por nodo) | Professional / Enterprise | Facturación 1:1 del coste de GPU (cloud o on-prem). Se puede ajustar según proveedor (AWS, Azure, OVH, etc.). |
| **Compute serverless (tokens/consulta)** | Pago por uso | Professional / Enterprise | Uso de APIs comerciales (OpenAI, Anthropic, etc.). Se factura directamente al cliente según proveedor; CodeflowX no intermedia. |

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
| Freelance/microagencia | Cloud Compliance | 149 €/mes | 76% | Directo | 1 proyecto gobernado (compliance rápido) |
| Agencia/consultora mediana | Cloud Governance | 699 €/mes | 90% | Directo/Partner | 3 proyectos simultáneos, workflows avanzados |
| Partner SaaS (white-label) | Cloud Enterprise | 1.199 €/mes | 91% | Directo/Partner | Multi-tenant, marca blanca, 5 clientes incluidos |
| Enterprise (self-host) | Corporate Standard | 24k €/año | 80% | Partner | 5 sistemas IA, soporte L3, despliegue rápido |
| Enterprise regulado | Corporate Enterprise | 48k €/año | 80% | Partner Elite | HA + SIEM + roadmap dedicado |
| Ayuntamiento pequeño | Public Local | 12k €/año | 72% | Partner público | Cloud dedicado UE, FRIA incluida |
| Organismo regional | Public Regional | 24k €/año | 80% | Partner | 5 sistemas, reporting Art. 71 |
| Organismo nacional | Public National | 60k €/año | 82% | Partner Elite | ENS Alto + auditoría anual |

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
