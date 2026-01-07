# 📘 GUÍA FUNCIONAL - INTEGRACIONES DE DATOS Y MLOps

**Versión:** 1.0
**Fecha:** Diciembre 2025
**Audiencia:** Equipos de Datos, ML, Producto, Compliance, Tecnología

---

## 🎯 ¿Qué son las integraciones?
Es el módulo que conecta la plataforma con fuentes de datos, storages y herramientas MLOps externas, para descubrir y catalogar datasets y modelos en un único lugar, con trazabilidad y gobierno.

---

## 🌟 Beneficios principales
1. **Catálogo unificado:** datasets y modelos externos visibles en un mismo sitio.
2. **Onboarding rápido:** añadir un nuevo proveedor sin tocar el núcleo de la plataforma.
3. **Gobernanza y cumplimiento:** trazabilidad de orígenes, licencias y aprobaciones.
4. **Confianza y control:** visibilidad de qué se usa, dónde está y bajo qué credenciales.
5. **Experiencia consistente:** mismo flujo funcional para todos los proveedores.

---

## 🧩 Áreas que cubrimos (vista general)
- **Data Lakes / Storage:** S3, Azure Blob, GCS, ADLS Gen2, HDFS, IBM COS, OCI, MinIO.
- **Bases de datos:** Relacionales (Snowflake, PostgreSQL, MySQL, SQL Server, Oracle, DB2), Cloud DWH (BigQuery, Redshift), NoSQL (MongoDB, Cassandra, DynamoDB).
- **MLOps / Model Hubs:** Databricks, SageMaker, Vertex AI, Azure ML, MLflow, Hugging Face (modelos y datasets), Kubeflow (pipelines/runs), Seldon (endpoints), Weights & Biases (runs).
- **ITSM / Gestión de Proyectos:** Jira (proyectos e incidencias), ServiceNow (incidencias, tareas, proyectos, cambios), BMC Remedy/Helix (incidencias, cambios, problemas), Freshservice (tickets, problemas, cambios), Zendesk (tickets, problemas, cambios).

---

## 🔑 Qué puedes hacer
- **Descubrir y catalogar** datasets y modelos externos con su metadata clave.
- **Catalogar elementos ITSM** como incidencias, proyectos y tareas desde Jira, ServiceNow, BMC Remedy, Freshservice y Zendesk.
- **Filtrar y etiquetar** por organización, licencia o tarea (model hubs).
- **Ver trazabilidad** de orígenes y estados de sincronización.
- **Alinear aprobaciones** con los flujos de gobierno existentes.
- **Consultar endpoints** de modelos desplegados (ej. Seldon) de forma controlada.

---

## 👥 Roles y valor
- **Data / ML Engineers:** ven y mantienen el catálogo externo sin fricción.
- **Producto / Analítica:** identifican rápidamente fuentes y modelos disponibles.
- **IT / Project Managers:** tienen visibilidad de incidencias, proyectos y tareas desde múltiples sistemas ITSM (Jira, ServiceNow, BMC Remedy, Freshservice, Zendesk) en un solo lugar.
- **Compliance / Seguridad:** cuentan con trazabilidad de orígenes y licencias.
- **Tecnología / Plataforma:** habilitan proveedores autorizados de forma consistente.

---

## 🛡️ Enfoque de gobierno
- **Trazabilidad:** cada proveedor se registra con su origen y estado.
- **Licencias y uso responsable:** visibilidad de licencias en hubs de modelos/datasets.
- **Aprobaciones:** integración con los flujos de aprobación de la plataforma.
- **PII (cuando aplica):** soporte para marcar y revisar riesgos en datasets tabulares.

---

## 📚 Catálogo resumido
- **Storage / Data Lakes:** S3, Azure Blob, GCS, ADLS Gen2, HDFS, IBM COS, OCI, MinIO.
- **Bases de datos:** Snowflake, PostgreSQL, MySQL, SQL Server, Oracle, DB2, BigQuery, Redshift, MongoDB, Cassandra, DynamoDB.
- **MLOps / Model Hubs:** Databricks, SageMaker, Vertex AI, Azure ML, MLflow, Hugging Face, Kubeflow, Seldon, Weights & Biases.
- **ITSM / Gestión:** Jira (proyectos e incidencias), ServiceNow (incidencias, tareas, proyectos, cambios), BMC Remedy/Helix (incidencias, cambios, problemas), Freshservice (tickets, problemas, cambios), Zendesk (tickets, problemas, cambios).

---

## ❓ Preguntas frecuentes
**¿Puedo ver todo en un solo catálogo?**
Sí, datasets y modelos externos se muestran en una vista unificada con metadata básica.

**¿Puedo filtrar por licencias o tareas en hubs de modelos?**
Sí, puedes aplicar filtros funcionales (por ejemplo, licencias o tareas en Hugging Face).

**¿Necesito conocer detalles técnicos para usarlo?**
No. La experiencia es homogénea y guiada; la configuración técnica se gestiona aparte.

**¿Cómo se integra con las aprobaciones?**
Los activos externos pueden pasar por los flujos de aprobación ya existentes de la plataforma.

**¿Se controla el acceso?**
Los proveedores se habilitan según política interna; sólo se muestran los autorizados.

**¿Puedo catalogar incidencias y proyectos de sistemas ITSM?**
Sí, puedes catalogar proyectos, incidencias y tareas desde Jira, ServiceNow, BMC Remedy, Freshservice y Zendesk. Estos elementos aparecen en el catálogo con su metadata (estado, asignado, prioridad, etc.) y enlaces directos a las plataformas originales.

**¿Puedo ver incidencias de múltiples sistemas ITSM en un solo lugar?**
Sí, el catálogo unificado permite ver y gestionar elementos de todos los sistemas ITSM configurados, facilitando la visibilidad centralizada y la trazabilidad.
