# EU AI ACT COMPLIANCE – PLAYBOOK ACTUALIZADO

Esta carpeta contiene los prompts y requisitos vigentes para generar la plataforma de gobernanza CodeflowX. Desde noviembre 2025 la estructura se apoya en el metamodelo (`PROMPTS_26`) y en documentos sectoriales numerados. Todo lo anterior queda archivado en `_deprecated`.

---

## 🚀 Inicio rápido para agentes Cursor
1. **Lee `PROMPTS_00_PLAYBOOK.md`**: guía maestra de ejecución, orden de prompts y política de saneamiento.
2. **Consulta `TRACKING_PROMPTS_IMPLEMENTACION.md`**: estado por prompt y porcentaje de avance.
3. **Configura el metamodelo**: usa `PROMPTS_26_METAMODELO_SECTORIAL.md` antes de cualquier sector.
4. **Selecciona sector**: abre la carpeta correspondiente (`NN_sector/`), revisa `01_requisitos.md` y ejecuta el `PROMPTS_3X` asociado.
5. **Desarrolla artefactos**: cada prompt indica entidades JPA, servicios REST, ViewModels/ZUL y políticas a generar.

---

## 📚 Documentos vigentes

| Categoría | Archivo | Descripción |
|-----------|---------|-------------|
| Playbook | `PROMPTS_00_PLAYBOOK.md` | Flujo maestro para agentes. |
| Metamodelo | `PROMPTS_26_METAMODELO_SECTORIAL.md` | Catálogos `SECTOR_GOVERNANCE` y `FAAS_FRAMEWORKS`, políticas y validaciones API. |
| Integraciones core | `PROMPTS_15_GOVERNANCE_API_REST.md` | Endpoints REST para ingestión y reporting. |
| | `PROMPTS_16_WEBHOOKS_Y_DELIVERIES.md` | Gestión de webhooks y entregas. |
| | `PROMPTS_17_ADAPTADOR_MCP.md` | Integración MCP para agentes locales. |
| | `PROMPTS_18_POSTGRES_GOVERNANCE.md` | Tablas y extensiones Postgres (Timescale, pgcrypto, pgvector). |
| Sectores | `PROMPTS_27` … `PROMPTS_36` | Frameworks FaaS y subperfiles por industria. |
| Seguimiento | `TRACKING_PROMPTS_IMPLEMENTACION.md` | Avance por prompt. |

Cada sector cuenta con `01_requisitos.md` (resumen funcional/técnico) y un `PROMPTS_3X_*.md` numerado.

---

## 🗂️ Política de limpieza
- mover los siguientes archivos a `docs/compliance/_deprecated/` para evitar instrucciones obsoletas:
  - `PROMPTS_01` a `PROMPTS_14_CORRECCION_NOMBRES_TABLAS.md` y derivados históricos.
  - `COMPLIANCE_VERIFICATION_MATRIX_EXTERNAL*.md`, `GOBIERNO_IA_CICLO_VIDA_COMPLETO_GUIA.md`, `GUIA_EJECUCION_PROMPTS_PENDIENTES.md`, `IMPLEMENTACION_100_COMPLETA_PROMPTS_03.md`, `INVENTARIO_COMPLETO_PROMPTS.md`, `ORGANIZACION_10_CHATS_PARALELOS.md`, otros indicados en `PROMPTS_00`.
- si necesitas consultar alguno, muévelo temporalmente fuera de `_deprecated` y devuélvelo al finalizar.

---

## ✅ Buenas prácticas (recordatorio)
- Tablas con prefijo de tres letras por dominio (`gov_`, `prj_`, `cor_`…), PK autonumérica, 3FN.
- Aplicar KISS, SOLID y arquitectura hexagonal en servicios Java (EnArt).
- Python expuesto solo para IA/MLOps/RAG; nada de lógica de negocio pública.
- Guardar cualquier nuevo prompt ejecutado en `TRACKING_PROMPTS_IMPLEMENTACION.md`.

---

## 📞 Contacto interno
- Equipo: Arquitectura de Cumplimiento CodeflowX
- Canal: `governance@codeflowx.internal`
- Última actualización: Noviembre 2025





---

### 📄 PROMPTS_11_INTEGRACION_QDRANT_MINIO_OPENSEARCH.md ⭐🔥

**Objetivo:** Integrar componentes especializados enterprise (Qdrant, MinIO, OpenSearch)  
**Total Prompts:** 15 prompts  
**Implementados:** 0  
**Pendientes:** 15  
**% Completo:** 0% ⏳

**Prompts:**
- **GRUPO A - Qdrant (5):** Setup + collections, cliente Java, RAG Python, integración prompts, reranking
- **GRUPO B - MinIO (5):** Setup + buckets, cliente Java, docs Anexo IV, pipeline RAG, lifecycle
- **GRUPO C - OpenSearch (5):** Setup + índices, cliente Java logs Art. 19, analytics Python, ILM, dashboards

**Arquitectura resultante:**
```
PostgreSQL/TimescaleDB  (50-100 GB metadata)
+ Qdrant                (18-35 GB embeddings, 5.75M vectors)
+ MinIO                 (4 TB docs/datasets/modelos)
+ OpenSearch            (650 GB logs inmutables Art. 19)
```

**Beneficios clave:**
- ✅ RAG potente (hybrid search vector+keyword + reranking +30-50% accuracy)
- ✅ Compliance robusto (logs inmutables OpenSearch Art. 19)
- ✅ Escalabilidad (componentes especializados optimizados)
- ✅ Multi-tenant (collections/buckets/índices por cliente)
- ✅ Lifecycle automático (archivado MinIO, retention ILM OpenSearch)

**Prioridad:** 🟡 ALTA (RAG enterprise + infraestructura compliance)

**Estimación:** 18-26 días secuencial | 9-12 días paralelo (3 chats)

**Prompts críticos (🔴):** 6 de 15
- PROMPT 3 (RAG Python FastAPI)
- PROMPT 8 (Anexo IV MinIO Art. 11)
- PROMPT 9 (Pipeline RAG MinIO → Qdrant)
- PROMPT 11 (OpenSearch setup)
- PROMPT 12 (OpenSearch LogService Art. 19)
- PROMPT 14 (ILM retention GDPR)


---

### 📄 PROMPTS_12_CONECTORES_PLATAFORMAS_ENTERPRISE.md ⭐🔥🔥

**Objetivo:** Integrar CodeflowX como governance overlay sobre Databricks, Snowflake, Azure ML, SageMaker  
**Total Prompts:** 12 prompts  
**Implementados:** 0  
**Pendientes:** 12  
**% Completo:** 0% ⏳  
**Prioridad:** 🔴 **CRÍTICA COMERCIAL**

**Prompts:**
- **Databricks (3):** Conector + sync bidireccional, webhooks receiver, UI gestión
- **Snowflake (2):** Catalogación datasets (metadata only), data quality (sample)
- **Azure ML / SageMaker (2):** Deployment monitoring Art. 72
- **Data Lakes (1):** S3/Azure Blob/GCS catalogación
- **Spark (1):** Submit evaluation jobs big data
- **Framework (3):** Orquestación sync, BPMN approval, dashboard

**Arquitectura governance overlay:**
```
Cliente mantiene: Databricks + Snowflake + Azure ML + Spark
CodeflowX añade: Governance + Compliance + Aprobaciones
Datos: NO se mueven (solo metadata/métricas)
```

**Impacto comercial:** 🔴 **MÁXIMO**
- Sin esto: Solo clientes greenfield (20% mercado, €3B TAM)
- Con esto: Clientes greenfield + enterprise (80% mercado, **€12B TAM**)
- **4x más mercado accesible**

**Mensaje venta:**
> "CodeflowX se integra con su Databricks existente, añadiendo compliance AI Act sin migración."

**Beneficios cliente:**
- ✅ Mantienen inversión existente ($300K/año Databricks+Snowflake)
- ✅ Datos NO se mueven (permanecen en Snowflake)
- ✅ Workflow ML Engineers NO cambia
- ✅ Compliance AI Act automático
- ✅ ROI: $500K ahorro vs desarrollo interno

**Prioridad:** 🔴 **MÁS CRÍTICA QUE GPAI** (comercial urgente)

**Estimación:** 26-32 días secuencial | 12-16 días paralelo (4 chats)

**Prompts críticos (🔴):** 6 de 12
- PROMPT 1 (Databricks conector)
- PROMPT 2 (Webhooks)
- PROMPT 4 (Snowflake catalog)
- PROMPT 10 (Orquestación)
- PROMPT 11 (BPMN approval workflow)


---

### 📄 PROMPTS_14_INTEGRACION_EXTENSIONES_POSTGRESQL.md 🌟💾

**Objetivo:** Integrar las 17 extensiones PostgreSQL instaladas en tablas, vistas y queries existentes  
**Total Prompts:** 12 prompts  
**Implementados:** 0  
**Pendientes:** 12  
**% Completo:** 0% ⏳  
**Prioridad:** 🟡 **ALTA (Compliance + Performance)**

**Extensiones críticas:**
- **uuid-ossp** → UUIDs automáticos (TODAS las tablas)
- **pgcrypto** → Hash chains logs inmutables (Art. 19 AI Act)
- **timescaledb** → Series temporales (compresión 10x)
- **vector** → Embeddings RAG (búsqueda semántica)
- **ltree** → Linaje modelos GPAI (Art. 53)
- **pg_trgm** → Búsqueda difusa (tolerante typos)
- **hstore** → Metadatos flexibles
- **btree_gin/gist** → Índices optimizados

**Prompts grupos:**
- **Grupo 1 (3):** uuid-ossp + pgcrypto → Audit logs inmutables
- **Grupo 2 (2):** ltree → Linaje modelos GPAI Art. 53
- **Grupo 3 (2):** timescaledb → Hypertables + continuous aggregates
- **Grupo 4 (1):** pgvector → Embeddings RAG
- **Grupo 5 (1):** pg_trgm → Búsqueda fuzzy
- **Grupo 6 (1):** hstore → Metadatos dinámicos
- **Grupo 7 (1):** btree_gin/gist → Índices compuestos
- **Grupo 8 (1):** Funciones utilidad

**Compliance AI Act:**
- ✅ **Art. 19:** Hash chains criptográficos (pgcrypto) → logs inmutables
- ✅ **Art. 53:** Linaje GPAI (ltree) → documentar modificaciones sustanciales
- ✅ **Art. 15:** Retención logs 10 años comprimido (timescaledb)
- ✅ **Art. 13:** RAG instrucciones de uso (pgvector)

**Rendimiento:**
- 📈 **10x compresión** datos > 90 días (timescaledb)
- 📈 **100x queries** más rápidas (continuous aggregates)
- 📈 **Búsqueda semántica** sub-segundo (pgvector HNSW índices)
- 📈 **Búsqueda fuzzy** tolerante typos (pg_trgm)

**Documentación arquitectura:**
- 📖 `/docs/arquitectura/POSTGRESQL_EXTENSIONES_COMPLETAS.md` (80 KB) → Explicación extensiones
- 📖 `/docs/arquitectura/POSTGRESQL_QUERIES_VISTAS_OPTIMIZADAS.md` (65 KB) → Queries y vistas

**Tablas críticas afectadas:**
```sql
cor_auditlog           → Hash chain + hypertable (Art. 19)
cor_model_lineage      → ltree path (Art. 53 GPAI)
eval_modelmetrics      → Hypertable (series temporales)
rag_chunks             → vector(1536) embeddings
gov_prompt             → pg_trgm índice búsqueda
cor_model              → hstore hyperparameters
```

**Validación incluida:**
- ✅ Script verificación integridad hash chain
- ✅ Función `verify_audit_chain()` → detectar manipulación
- ✅ Queries linaje completo (ancestros + descendientes)
- ✅ Continuous aggregates (métricas diarias auto-refresh)
- ✅ Test RAG búsqueda semántica

**Estimación:** 4-6 horas  
**Impacto:** 🟢 **Compliance** (Art. 19, 53) + 🟢 **Performance** (10-100x mejora)

**Prompts críticos (🔴):**
- PROMPT 14.2: Audit logs hash chain (Art. 19 - **CRÍTICO LEGAL**)
- PROMPT 14.4: Linaje modelos ltree (Art. 53 GPAI - **CRÍTICO LEGAL**)
- PROMPT 14.6: Hypertables (performance - **CRÍTICO PROD**)

**Referencias:**
- Documentación completa: `docs/arquitectura/POSTGRESQL_*`
- Ejemplos SQL: Todas las vistas, funciones y triggers incluidos
- ViewModel Java: Ejemplos integración ZKoss

---

## 📊 RESUMEN INVENTARIO PROMPTS

**Total documentos:** 14  
**Total prompts:** 185  
**Implementados:** 17 (PROMPTS_01 base + PROMPTS_03 completo)  
**Pendientes:** 168  
**% Progreso global:** ~9%

**Distribución por tecnología:**
- **Python:** 45 prompts (PROMPTS_01, 02, 10)
- **Java:** 34 prompts (PROMPTS_03 ✅, 05, 08)
- **BPMN:** 12 prompts (PROMPTS_09)
- **MLOps:** 6 prompts (PROMPTS_06)
- **Infraestructura:** 27 prompts (PROMPTS_11, 12)
- **Data/Mock:** 8 prompts (PROMPTS_13)
- **Database:** 12 prompts (PROMPTS_14) 🌟 **NUEVO**
- **Deprecated:** 47 prompts (PROMPTS_07 → reemplazado)

**Prioridades críticas:**
1. 🔴 **PROMPTS_14** (Database) → Compliance Art. 19/53 + Performance
2. 🔴 **PROMPTS_12** (Conectores) → 4x mercado accesible
3. 🔴 **PROMPTS_11** (Infraestructura) → Stack completo operativo
4. 🟡 **PROMPTS_08** (Java Multi-Framework) → ISO/OECD compliance
5. 🟡 **PROMPTS_10** (Python Multi-Framework) → Extensión microservicios

**Roadmap Q1 2025:**
- Semana 1-2: PROMPTS_14 + PROMPTS_11 (Database + Infra)
- Semana 3-4: PROMPTS_12 (Conectores enterprise)
- Semana 5-8: PROMPTS_08 + PROMPTS_09 + PROMPTS_10 (Multi-Framework)
- Semana 9-10: PROMPTS_13 (Mock data + Ollama)
- Semana 11-12: PROMPTS_05 + PROMPTS_06 (Nuevas entidades + GPAI)

