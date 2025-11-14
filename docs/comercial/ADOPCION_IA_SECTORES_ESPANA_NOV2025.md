---
title: "Adopción de soluciones de IA por sector en España"
author: "Equipo Strategy & Pricing"
date: "Noviembre 2025"
confidencialidad: "Uso interno"
---

# Adopción de soluciones de IA por sector en España

## 0. Resumen ejecutivo

- **Penetración media nacional (empresas ≥10 empleados): 13,9 % en 2023**, **15,8 % en 2024 (estimación ONTSI/Eurostat)** y **18,5 % proyectado para 2025** si se mantienen los planes de inversión vigentes.
- **Sectores con adopción líder (2023-2024):** Información y comunicaciones (29 → 32 %), Finanzas y seguros (25 → 27 %), Energía y utilities (23 → 25 %).
- **Sectores con mayor crecimiento YoY (2024):** Transporte (+6 pp vs 2022, +2 pp adicionales 2024e), Comercio (+5 pp, +2 pp 2024e) y Construcción (+4 pp, +2 pp 2024e) por ayudas públicas y casos de eficiencia operativa.
- **Drivers:** disponibilidad de datos operativos, presión regulatoria (finanzas, energía, administración pública), necesidad de automatizar atención al cliente (comercio, hostelería) y captación de talento.
- **Barreras comunes:** falta de talento, costes de implementación, dudas sobre cumplimiento EU AI Act/ENS y dependencia de proveedores.
- **Oportunidad CodeflowX:** ofrecer gobierno, evaluación y monitorización con despliegues ≤6 semanas en sectores que alcanzarán 15-20 % de adopción en 2025 pero carecen de soluciones de compliance integradas.

## 1. Fuentes estadísticas principales

| Fuente | Cobertura | Año datos | Observaciones |
|--------|-----------|-----------|----------------|
| INE – Encuesta sobre el uso de TIC y comercio electrónico en las empresas | Empresas españolas ≥10 empleados, por CNAE | 2023 (publicado oct-2024) | Incluye módulo experimental sobre IA (aprendizaje automático, NLP, reconocimiento imagen, etc.). |
| ONTSI – Indicadores de la Sociedad Digital en España 2024 | Panel empresas España | 2023 | Segmenta adopción por sector y tamaño; ofrece tendencias y testimonios. |
| Eurostat – isoc_e_aisec | UE-27 vs España por NACE | 2023 | Permite comparación con media UE y evolución 2021-2023. |
| MITMA – Observatorio Transporte y Logística 2024 | Transporte y logística | 2024 | Incluye indicadores de digitalización y proyectos IA. |
| Banco de España – Informe innovación financiera 2024 | Sector financiero | 2024 | Detalla proyectos de IA en scoring, fraude y cumplimiento. |
| Segittur – Informe Smart Tourist 2024 | Turismo y hostelería | 2024 | Casos de uso IA generativa en destinos turísticos. |

## 2. Tabla comparativa de adopción de IA por sector (España, 2023-2025)

| Sector CNAE | 2023 | 2024 (est.) | 2025 (proy.) | Principales usos declarados | Brecha UE 2023 | Notas |
|-------------|------|--------------|---------------|----------------------------|---------------|-------|
| Información y comunicaciones | **29 %** | 32 % | 35 % | Automatización soporte, analítica predictiva, IA generativa contenidos | +6 pp | Crecimiento sostenido; startups IA empujan adopción.
| Actividades financieras y de seguros | **25 %** | 27 % | 30 % | Scoring, AML, gestión documental, cumplimiento | +4 pp | DORA y EU AI Act aceleran proyectos gobierno.
| Suministro energía, agua y residuos | **23 %** | 25 % | 28 % | Mantenimiento predictivo, smart grids, forecasting demanda | +5 pp | PERTE y transición energética impulsan.
| Administración pública | **8 %** | 10 % | 13 % | Analítica expedientes, chatbots ciudadanos | −5 pp | Foco en comunidades autónomas y grandes ciudades.
| Educación (privada/concertada, formación) | **9 %** | 11 % | 14 % | Analítica aprendizaje, tutores IA, proctoring | −3 pp | Pilotos IA generativa en universidades privadas.
| Industria manufacturera | **13 %** | 15 % | 18 % | QA visión, mantenimiento predictivo, planificación | −1 pp | Subvenciones Industria 4.0 aceleran pymes.
| Actividades profesionales, científicas y técnicas | **12 %** | 14 % | 16 % | Marketing avanzado, consultoría, legaltech | +2 pp | Legaltech supera 12 % adopción 2024e.
| Comercio mayorista y minorista | **11 %** | 13 % | 16 % | Recomendadores, pricing dinámico, chatbots postventa | ±0 pp | Kit Digital II y marketplaces.
| Transporte y almacenamiento | **10 %** | 12 % | 15 % | Optimización rutas, visión hubs, asistentes flota | −2 pp | Logística 3PL escala pilotos IA.
| Actividades administrativas y servicios auxiliares | **7 %** | 9 % | 11 % | RPA + IA, documentación, workforce | −3 pp | BPO demandan compliance multi-cliente.
| Construcción | **6 %** | 8 % | 10 % | Gemelos digitales, seguridad obra, planificación | −2 pp | Fondos MRR + requisitos BIM.
| Hostelería y restauración | **5 %** | 7 % | 9 % | Chatbots reservas, marketing personalizado, forecasting | −1 pp | Destinos turísticos inteligentes.

> **Notas metodológicas:** 2023 = dato oficial INE/Eurostat. 2024 (est.) = interpolación ONTSI 2024 + panel sectorial (Q2 2024). 2025 (proy.) = proyección Strategy & Pricing basada en CAGR histórico (2021-2024) y planes públicos (Kit Digital II, PERTE, inversión sectorial). “Brecha UE 2023” = diferencia frente a media UE-27.

## 3. Detalle sectorial

### 3.1 Información y comunicaciones

- **Casos dominantes:** automatización L1 soporte (chatbots multicanal), clasificación de tickets, generación de contenido. Adopción de RAG sobre bases documentales de soporte.
- **Motivadores:** presión competitiva de agencias digitales, necesidad de diferenciar servicios gestionados, acuerdos MSP.
- **Riesgos/Barreras:** seguridad de datos de clientes, cumplimiento Art. 50 EU AI Act, gestión de prompts y versiones.
- **Relevancia CodeflowX:** posicionar governance overlay para agencias que revenden servicios AI a terceros. Integración con `leka-llm-interpreter` para optimizar costes BYOM.

### 3.2 Actividades financieras y de seguros

- **Casos dominantes:** scoring, KYC/AML, detección de fraude en pagos, automatización documental (OCR+NLP) para underwriting.
- **Motivadores:** exigencias Banco de España, EBA, DORA; regulación de modelos críticos (Art. 6 y Anexo III EU AI Act).
- **Riesgos/Barreras:** gobernanza de modelos heredados, transparencia y explicabilidad, auditorías de terceros.
- **Relevancia CodeflowX:** módulos de evaluación (`leka-llm-evaluation`, `leka-bias-detection`) y logging inmutable (TimescaleDB + hash chain) para auditorías.

### 3.3 Energía, agua y residuos

- **Casos dominantes:** mantenimiento predictivo en redes eléctricas, optimización de carga, predicción de demanda, monitorización de activos críticos.
- **Motivadores:** eficiencia energética, PERTE ERHA, objetivos de sostenibilidad, exigencias CNMC.
- **Riesgos/Barreras:** integración OT/IT, ciberseguridad industrial, requisitos ENS Alto.
- **Relevancia CodeflowX:** supervisión continua de agentes y modelos híbridos (RAG + ML tradicional), generación de FRIA automatizada.

### 3.4 Industria manufacturera

- **Casos dominantes:** visión artificial QA, mantenimiento predictivo, planificación producción, simulación digital.
- **Motivadores:** Industria 4.0, subvenciones MRR y programas acelerapyme.
- **Riesgos/Barreras:** legacy MES/SCADA, falta de talento IA, ROI incierto en pymes.
- **Relevancia CodeflowX:** gobierno modular para múltiples líneas (proyectos gobernados), evaluación RAG técnica y control de drift.

### 3.5 Servicios profesionales, científicas y técnicas
- **Casos dominantes:** automatización de propuestas, marketing predictivo, IA generativa aplicada a consultoría y legaltech (adopción legaltech estimada 11-12 %).
- **Motivadores:** diferenciación frente a competidores, demanda de cumplimiento por parte de clientes corporativos.
- **Riesgos/Barreras:** protección de propiedad intelectual, gestión de datos sensibles de clientes.
- **Relevancia CodeflowX:** permitir governance overlay multi-cliente (Cloud Enterprise), control de prompts y reporting Art. 12-14.

### 3.6 Administración pública
- **Casos dominantes:** analítica de expedientes, asistentes virtuales ciudadanos, priorización y triaje de trámites, detección de fraude en subvenciones.
- **Motivadores:** Plan de Digitalización AP 2021-2025, fondos NextGen, obligaciones ENS y EU AI Act (Art. 53 y 71).
- **Riesgos/Barreras:** heterogeneidad tecnológica, dependencia de proveedores legacy, procesos de contratación largos.
- **Relevancia CodeflowX:** reporting automatizado FRIA/ENS, multi-proyecto por concejalías, despliegue híbrido (cloud dedicado + on-prem) y logging inmutable para auditorías.

### 3.7 Educación (privada/concertada, empresas formación)
- **Casos dominantes:** analítica de aprendizaje, tutores inteligentes, proctoring, automatización administrativa.
- **Motivadores:** competencia en educación superior, programas UNIDIGITAL, necesidad de personalización y retención estudiantil.
- **Riesgos/Barreras:** protección de datos de menores, aceptación docente, presupuestos limitados.
- **Relevancia CodeflowX:** control de agentes educativos, evaluación de prompts/RAG académicos, gestión de consentimiento y reporting ético.

### 3.8 Comercio mayorista y minorista
- **Casos dominantes:** motores de recomendación, segmentación, chatbots de postventa, analítica de fraude en eCommerce.
- **Motivadores:** crecimiento del canal digital, open banking para medios de pago, ayudas Kit Digital II.
- **Riesgos/Barreras:** integraciones con ERPs legacy, cumplimiento RGPD/PSD2.
- **Relevancia CodeflowX:** monitorización de modelos recomendadores, evaluación sesgos, despliegue rápido en bundles cloud.

### 3.9 Transporte y almacenamiento
- **Casos dominantes:** optimización de rutas, visión en hubs logísticos, asistentes de flota, predicción de demanda.
- **Motivadores:** costes fuel/CO₂, exigencia de SLA en eCommerce, competitividad 3PL.
- **Riesgos/Barreras:** conectividad en tiempo real, integración telemática, cumplimiento ENS para operadores públicos.
- **Relevancia CodeflowX:** capacidad de orquestar agentes distribuidos, logging inmutable, alertas drift.

### 3.10 Actividades administrativas y servicios auxiliares
- **Casos dominantes:** automatización BPO, RPA con IA, clasificación documental, workforce scheduling.
- **Motivadores:** presión por márgenes, contratos SLA, externalización de compliance.
- **Riesgos/Barreras:** multi-clientes con datos sensibles, reticencia a inversión CAPEX.
- **Relevancia CodeflowX:** modelo por proyectos gobernados, multi-tenant, monetización cumplimiento.

### 3.11 Construcción
- **Casos dominantes:** gemelos digitales, control de riesgos laborales, previsión de materiales, licitaciones.
- **Motivadores:** normativa BIM, fondos Next Generation, contratos públicos exigentes.
- **Riesgos/Barreras:** baja madurez digital, entornos de obra desconectados.
- **Relevancia CodeflowX:** reporting ENS, control de datos sensibles, gestión de agentes en entornos desconectados mediante MCP.

### 3.12 Hostelería y restauración
- **Casos dominantes:** chatbots de reservas, asistentes de upselling, analítica de reviews, forecasting ocupación.
- **Motivadores:** recuperación post-pandemia, dependencia de marketplaces, promoción destinos inteligentes.
- **Riesgos/Barreras:** rotación personal, presupuesto limitado, fragmentación proveedores.
- **Relevancia CodeflowX:** paquetes Cloud Compliance, automatización FRIA para cadenas hoteleras y operadores turísticos.

## 4. Perspectiva por tamaño empresarial

| Tamaño (empleados) | Adopción IA (%) | Comentarios |
|--------------------|-----------------|-------------|
| 10-49 | 8 % | Foco en soluciones plug-and-play; escasez de talento interno. |
| 50-249 | 16 % | Creciente interés por gobierno y monitorización ante aumento de proyectos. |
| ≥250 | 29 % | Programas estructurados de IA, comités de gobernanza y exigencias regulatorias. |

Fuente: INE 2023, módulo IA. Complementar con ONTSI 2024 para tendencias.

## 4.1 Mapa de cumplimiento y gobierno por sector

| Sector | Normativas/obligaciones clave | Certificaciones/ISO relevantes | ¿Documentación sectorial CodeflowX? | Notas para versión adaptada |
|--------|-------------------------------|--------------------------------|------------------------------------|-----------------------------|
| Información y comunicaciones | EU AI Act (Art. 12-15, 71), RGPD, DSA | ISO/IEC 27001, 23894, ENS (si servicios públicos) | Sí (deck agencias + prompts MCP) | Añadir plantillas de FRIA para agentes multi-cliente y gestión BYOM. |
| Finanzas y seguros | EU AI Act (Art. 6, Anexo III 5), DORA, EBA GL, RGPD, PSD2 | ISO 27001, ISO/IEC 27701, ISO 20000-1 | Sí (pack FinReg en desarrollo) | Incluir anexos para scoring explicable, matrices AML y reporting Banco de España. |
| Energía, agua y residuos | EU AI Act (Anexo III 2), NIS2, CNMC, ENS Alto | ISO 27001, ISO 55001, ISO 14001 | Parcial (plantillas FRIA + logging) | Ampliar con protocolos de resiliencia OT, integración SCADA y plan continuidad. |
| Administración pública | EU AI Act (Art. 53-71), ENS, LPACAP, RGPD | ENS (Alto), ISO 27001, ISO 22301 | Sí (paquete AP en roadmap) | Incluir guías FRIA sector público, modelos de informe Art. 71 y anexos ENS. |
| Educación | EU AI Act (Art. 10-14), LOPDGDD, RGPD | ISO 27701, UNE 71362 (calidad e-learning) | Parcial (casos pilotos) | Añadir políticas protección menores, checklist consentimiento y gobernanza agentes educativos. |
| Industria manufacturera | EU AI Act (Anexo III 1), NIS2, normativa seguridad industrial | ISO 9001, ISO 27001, ISO 45001 | Sí (pack Industria 4.0) | Expandir con requisitos trazabilidad cadena suministro y mantenimiento predictivo. |
| Servicios profesionales (legaltech, consultoría) | EU AI Act (Art. 12-15), RGPD, LOPDGDD | ISO 27001, ISO 27701 | Sí (legaltech docs base) | Añadir cláusulas contrato, matrices de responsabilidad proveedor/deployer y guías de auditoría. |
| Comercio | EU AI Act (prácticas limitadas), RGPD, PSD2 (pagos), LSSI | ISO 27001, ISO 27701 | Parcial (plantillas commerce) | Crear checklists omnicanal, controles sesgos recomendadores y plantillas auditoría marketing. |
| Transporte y logística | EU AI Act (Anexo III 1, 4), NIS2, ADR (según caso) | ISO 27001, ISO 39001, ISO 28000 | Parcial (casos piloto logística) | Incorporar guías para tracking flotas, integraciones IoT y alertas ENS medio. |
| Actividades administrativas/BPO | EU AI Act (Art. 10-15), RGPD, ENS (cuando servicio público) | ISO 27001, ISO 27701, ISO 22301 | Sí (modelo multi-tenant) | Añadir SLAs de gobernanza, segregación de datos, plantillas de reporte clientes. |
| Construcción | EU AI Act (Anexo III 1), PRL (Ley 31/1995), normativa BIM | ISO 9001, ISO 45001, ISO 19650 | Parcial (pack construcción) | Extender guías para gemelos digitales, compliance obra pública y gestión subcontratas. |
| Hostelería y turismo | EU AI Act (prácticas limitadas), RGPD, ENS (turismo inteligente) | ISO 27001, UNE 178501 | Parcial (pack turismo inteligente) | Añadir plantillas de FRIA turística, controles PII huéspedes y reporting destinos inteligentes. |

> **Leyenda disponibilidad:** “Sí” = documentación/procesos listos; “Parcial” = material base disponible, requiere ajustes sectoriales. Próximos sprints: completar módulos ENS/sector público y plantillas comercio/turismo.

## 5. Implicaciones comerciales para CodeflowX

1. **Segmentar discurso:** enfatizar ROI y cumplimiento en sectores regulados (finanzas, energía) y eficiencia operativa en sectores con adopción incipiente (construcción, hostelería).
2. **Bundles específicos:**
   - *Pack FinReg:* governance + bias detection + evaluaciones AML (integración `leka-bias-detection` + `leka-llm-evaluation`).
   - *Pack Industria 4.0:* monitoreo de modelos visión + registros inmutables (TimescaleDB + hash chain).
   - *Pack Turismo Inteligente:* Cloud Governance + integración con n8n/ChatGPT vía `leka-llm-interpreter` y webhooks MCP.
3. **Apalancar ayudas públicas:** incluir en propuestas argumentos Kit Digital II, programas CIBERresiliencia (finanzas) y PERTE.
4. **Refuerzo en compliance:** destacar preparación EU AI Act (Art. 9-15, 71), ENS y obligaciones sectoriales (EBA, CNMC, MITMA).
5. **Servicios profesionales asociados:** auditorías FRIA, consultoría de evaluación RAG, soporte multi-tenant para partners.

## 6. Próximos pasos

- Actualizar `OPORTUNIDAD_NEGOCIO_ESPANA_NOVIEMBRE_2025.md` con esta tabla sectorial.
- Generar infografías y deck comercial basado en esta información.
- Coordinar con equipo de prompts (`PROMPTS_18`+) para incluir requisitos por sector (p.ej. triggers ENS, reporting Art. 71).
- Monitorizar publicación INE 2024 (prevista oct-2025) para refrescar cifras.

## 7. Bibliografía y enlaces

- Instituto Nacional de Estadística (2024). *Encuesta sobre el uso de TIC y comercio electrónico en las empresas 2023*. Módulo Inteligencia Artificial. https://www.ine.es/dyngs/INEbase/es/operacion.htm?c=Estadistica_C&cid=1254736176749
- Observatorio Nacional de Tecnología y Sociedad – ONTSI (2024). *Indicadores de la Sociedad Digital en España 2024*.
- Eurostat (2024). Dataset *isoc_e_aisec* – *Use of artificial intelligence in enterprises*.
- Banco de España (2024). *Informe sobre innovación financiera y RegTech*.
- MITMA (2024). *Observatorio del Transporte y la Logística en España*.
- Segittur (2024). *Informe Smart Tourist Destinations 2024*.
- Comisión Europea (2024). *Digital Economy and Society Index – DESI* (capítulo España).

---

**Preparado por:** Equipo Strategy & Pricing  
**Contacto:** strategy@codeflowx.com  
**Confidencialidad:** Uso interno – no distribuir externamente sin aprobación de Dirección.

