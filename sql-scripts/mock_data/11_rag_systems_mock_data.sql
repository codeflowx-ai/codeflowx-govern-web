-- ============================================================================
-- MOCK DATA - RAG SYSTEMS MODULE
-- ============================================================================

-- Inserción de sistemas RAG de prueba
INSERT INTO RAGRAGSYSTEMS (
    RAGNAME, RAGDESCRIPTION, RAGTYPE, RAGVERSION, RAGSTATUS,
    RAGCONFIGURATION, RAGEMBEDDINGMODEL, RAGRETRIEVALCONFIG, RAGGENERATIONCONFIG,
    RAGMETADATA, RAGAPPROVALSTATUS, RAGCREATEDBY, RAGUPDATEDBY, RAGCREATEDAT, RAGUPDATEDAT
) VALUES 
('Customer Support Knowledge Base', 'Sistema RAG para base de conocimiento de soporte al cliente con búsqueda semántica avanzada', ARRAY['KNOWLEDGE_BASE'], '1.0.0', ARRAY['ACTIVE'], '{"chunk_size": 512, "chunk_overlap": 50, "max_results": 10}', 'text-embedding-ada-002', '{"top_k": 5, "similarity_threshold": 0.75, "reranking": true}', '{"model": "gpt-4", "temperature": 0.3, "max_tokens": 500}', '{"department": "Support", "priority": "High"}', ARRAY['APPROVED'], 'admin', 'admin', NOW() - INTERVAL '30 days', NOW() - INTERVAL '5 days'),
('Product Documentation Search', 'Búsqueda semántica en documentación de productos para desarrolladores', ARRAY['SEMANTIC_SEARCH'], '2.1.0', ARRAY['ACTIVE'], '{"chunk_size": 1024, "chunk_overlap": 100, "max_results": 15}', 'text-embedding-3-large', '{"top_k": 10, "similarity_threshold": 0.7, "metadata_filtering": true}', '{"model": "gpt-4-turbo", "temperature": 0.2, "max_tokens": 800}', '{"department": "Engineering", "priority": "High"}', ARRAY['APPROVED'], 'john.doe', 'john.doe', NOW() - INTERVAL '60 days', NOW() - INTERVAL '10 days'),
('Legal Documents Q&A', 'Sistema de preguntas y respuestas sobre documentos legales corporativos', ARRAY['QA_SYSTEM'], '1.5.2', ARRAY['ACTIVE'], '{"chunk_size": 768, "chunk_overlap": 80, "max_results": 8}', 'text-embedding-ada-002', '{"top_k": 8, "similarity_threshold": 0.8, "context_window": 3}', '{"model": "gpt-4", "temperature": 0.1, "max_tokens": 600}', '{"department": "Legal", "priority": "Critical", "compliance": true}', ARRAY['APPROVED'], 'jane.smith', 'jane.smith', NOW() - INTERVAL '90 days', NOW() - INTERVAL '15 days'),
('Technical Support Chatbot', 'Chatbot híbrido para soporte técnico con base de conocimiento y búsqueda', ARRAY['HYBRID'], '3.0.0', ARRAY['ACTIVE'], '{"chunk_size": 512, "chunk_overlap": 60, "max_results": 12}', 'text-embedding-3-small', '{"top_k": 7, "similarity_threshold": 0.72, "hybrid_search": true}', '{"model": "gpt-3.5-turbo", "temperature": 0.4, "max_tokens": 400}', '{"department": "IT", "priority": "High"}', ARRAY['APPROVED'], 'admin', 'admin', NOW() - INTERVAL '45 days', NOW() - INTERVAL '8 days'),
('Sales Intelligence System', 'Sistema de inteligencia de ventas con análisis de documentos comerciales', ARRAY['KNOWLEDGE_BASE'], '1.2.0', ARRAY['DRAFT'], '{"chunk_size": 640, "chunk_overlap": 70, "max_results": 10}', 'text-embedding-ada-002', '{"top_k": 6, "similarity_threshold": 0.73, "metadata_filtering": true}', '{"model": "gpt-4", "temperature": 0.35, "max_tokens": 550}', '{"department": "Sales", "priority": "Medium"}', ARRAY['PENDING'], 'mike.wilson', 'mike.wilson', NOW() - INTERVAL '15 days', NOW() - INTERVAL '2 days'),
('HR Policy Assistant', 'Asistente para consultas sobre políticas y procedimientos de recursos humanos', ARRAY['QA_SYSTEM'], '1.0.0', ARRAY['ACTIVE'], '{"chunk_size": 512, "chunk_overlap": 50, "max_results": 8}', 'text-embedding-3-small', '{"top_k": 5, "similarity_threshold": 0.78, "context_window": 2}', '{"model": "gpt-3.5-turbo", "temperature": 0.25, "max_tokens": 450}', '{"department": "HR", "priority": "Medium"}', ARRAY['APPROVED'], 'sarah.jones', 'sarah.jones', NOW() - INTERVAL '75 days', NOW() - INTERVAL '20 days'),
('Research Papers Search', 'Búsqueda semántica en repositorio de papers de investigación científica', ARRAY['SEMANTIC_SEARCH'], '2.0.1', ARRAY['ACTIVE'], '{"chunk_size": 1024, "chunk_overlap": 120, "max_results": 20}', 'text-embedding-3-large', '{"top_k": 15, "similarity_threshold": 0.68, "citation_tracking": true}', '{"model": "gpt-4-turbo", "temperature": 0.3, "max_tokens": 1000}', '{"department": "Research", "priority": "High"}', ARRAY['APPROVED'], 'dr.martinez', 'dr.martinez', NOW() - INTERVAL '120 days', NOW() - INTERVAL '30 days'),
('Financial Reports Analyzer', 'Análisis de reportes financieros con capacidades de Q&A', ARRAY['HYBRID'], '1.8.0', ARRAY['ACTIVE'], '{"chunk_size": 768, "chunk_overlap": 90, "max_results": 12}', 'text-embedding-ada-002', '{"top_k": 10, "similarity_threshold": 0.75, "table_parsing": true}', '{"model": "gpt-4", "temperature": 0.15, "max_tokens": 700}', '{"department": "Finance", "priority": "Critical"}', ARRAY['APPROVED'], 'cfo.office', 'cfo.office', NOW() - INTERVAL '100 days', NOW() - INTERVAL '12 days'),
('Marketing Content Library', 'Biblioteca de contenido de marketing con búsqueda inteligente', ARRAY['KNOWLEDGE_BASE'], '1.1.0', ARRAY['DRAFT'], '{"chunk_size": 512, "chunk_overlap": 55, "max_results": 15}', 'text-embedding-3-small', '{"top_k": 8, "similarity_threshold": 0.7, "tag_filtering": true}', '{"model": "gpt-3.5-turbo", "temperature": 0.5, "max_tokens": 400}', '{"department": "Marketing", "priority": "Low"}', ARRAY['PENDING'], 'marketing.team', 'marketing.team', NOW() - INTERVAL '10 days', NOW() - INTERVAL '3 days'),
('Code Documentation Assistant', 'Asistente para documentación de código y guías de desarrollo', ARRAY['QA_SYSTEM'], '2.2.0', ARRAY['ACTIVE'], '{"chunk_size": 896, "chunk_overlap": 100, "max_results": 10}', 'text-embedding-3-large', '{"top_k": 7, "similarity_threshold": 0.76, "code_aware": true}', '{"model": "gpt-4", "temperature": 0.2, "max_tokens": 650}', '{"department": "Engineering", "priority": "High"}', ARRAY['APPROVED'], 'dev.lead', 'dev.lead', NOW() - INTERVAL '80 days', NOW() - INTERVAL '18 days'),
('Medical Knowledge Base', 'Base de conocimiento médico para consultas clínicas', ARRAY['KNOWLEDGE_BASE'], '1.0.0', ARRAY['ARCHIVED'], '{"chunk_size": 1024, "chunk_overlap": 120, "max_results": 8}', 'text-embedding-ada-002', '{"top_k": 5, "similarity_threshold": 0.82, "medical_taxonomy": true}', '{"model": "gpt-4", "temperature": 0.1, "max_tokens": 800}', '{"department": "Healthcare", "priority": "Critical", "hipaa_compliant": true}', ARRAY['REJECTED'], 'dr.anderson', 'dr.anderson', NOW() - INTERVAL '200 days', NOW() - INTERVAL '150 days'),
('Contract Review Assistant', 'Asistente para revisión de contratos con capacidades de análisis', ARRAY['HYBRID'], '1.3.0', ARRAY['ACTIVE'], '{"chunk_size": 768, "chunk_overlap": 85, "max_results": 10}', 'text-embedding-3-large', '{"top_k": 8, "similarity_threshold": 0.77, "clause_detection": true}', '{"model": "gpt-4-turbo", "temperature": 0.15, "max_tokens": 750}', '{"department": "Legal", "priority": "High"}', ARRAY['APPROVED'], 'legal.team', 'legal.team', NOW() - INTERVAL '50 days', NOW() - INTERVAL '7 days');

-- Versiones de sistemas RAG
INSERT INTO RAGRAGVERSIONS (
    IDRAGRAGSYSTEMS0, RAGVERSIONUMBER, RAGCHANGELOG, RAGRELEASENOTES,
    RAGCONFIGURATION, RAGDEPLOYMENTSTATUS, RAGRELEASEDBY, RAGRELEASEDAT,
    RAGCREATEDBY, RAGCREATEDAT
) VALUES
-- Versiones para Customer Support Knowledge Base (ID 1)
(1, '0.9.0', 'Initial beta version', 'First working version with basic search', 
 '{"chunk_size": 512}', 'DEPRECATED', 'admin', NOW() - INTERVAL '60 days', 'admin', NOW() - INTERVAL '60 days'),
(1, '1.0.0', 'Production release', 'Stable production version with improved accuracy', 
 '{"chunk_size": 512, "chunk_overlap": 50}', 'DEPLOYED', 'admin', NOW() - INTERVAL '30 days', 'admin', NOW() - INTERVAL '30 days'),

-- Versiones para Product Documentation Search (ID 2)
(2, '1.0.0', 'Initial release', 'First production version', 
 '{"chunk_size": 1024}', 'DEPRECATED', 'john.doe', NOW() - INTERVAL '120 days', 'john.doe', NOW() - INTERVAL '120 days'),
(2, '2.0.0', 'Major upgrade', 'Improved embeddings and larger context', 
 '{"chunk_size": 1024, "chunk_overlap": 80}', 'DEPRECATED', 'john.doe', NOW() - INTERVAL '90 days', 'john.doe', NOW() - INTERVAL '90 days'),
(2, '2.1.0', 'Performance improvements', 'Optimized retrieval and metadata filtering', 
 '{"chunk_size": 1024, "chunk_overlap": 100}', 'DEPLOYED', 'john.doe', NOW() - INTERVAL '60 days', 'john.doe', NOW() - INTERVAL '60 days'),

-- Versiones para Legal Documents Q&A (ID 3)
(3, '1.0.0', 'Initial version', 'Basic Q&A functionality', 
 '{"chunk_size": 768}', 'DEPRECATED', 'jane.smith', NOW() - INTERVAL '150 days', 'jane.smith', NOW() - INTERVAL '150 days'),
(3, '1.5.2', 'Compliance update', 'Enhanced security and compliance features', 
 '{"chunk_size": 768, "chunk_overlap": 80}', 'DEPLOYED', 'jane.smith', NOW() - INTERVAL '90 days', 'jane.smith', NOW() - INTERVAL '90 days');

-- Fuentes de datos para sistemas RAG
INSERT INTO RAGRAGDATASOURCES (
    IDRAGRAGSYSTEMS0, RAGNAME, RAGTYPE, RAGCONNECTIONCONFIG, RAGSTATUS,
    RAGSYNCFREQUENCY, RAGLASTSYNCAT, RAGSYNCSTATUS, RAGDOCUMENTCOUNT,
    RAGINDEXNAME, RAGMETADATA, RAGCREATEDBY, RAGCREATEDAT
) VALUES
-- Fuentes para Customer Support Knowledge Base (ID 1)
(1, 'Zendesk Articles', 'API', '{"api_key": "***", "base_url": "https://support.company.com"}', ARRAY['ACTIVE'], 'HOURLY', NOW() - INTERVAL '2 hours', ARRAY['SUCCESS'], 1245, 'kb_zendesk_v1', '{"category": "support"}', 'admin', NOW() - INTERVAL '30 days'),
(1, 'Internal Wiki', 'WEB_SCRAPER', '{"base_url": "https://wiki.company.com", "depth": 3}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '6 hours', ARRAY['SUCCESS'], 567, 'kb_wiki_v1', '{"category": "internal"}', 'admin', NOW() - INTERVAL '25 days'),

-- Fuentes para Product Documentation Search (ID 2)
(2, 'GitHub Docs Repository', 'GIT', '{"repo_url": "github.com/company/docs", "branch": "main"}', ARRAY['ACTIVE'], 'HOURLY', NOW() - INTERVAL '1 hour', ARRAY['SUCCESS'], 2340, 'docs_github_v2', '{"format": "markdown"}', 'john.doe', NOW() - INTERVAL '60 days'),
(2, 'API Reference', 'FILE_SYSTEM', '{"path": "/docs/api", "extensions": ["json", "yaml"]}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '12 hours', ARRAY['SUCCESS'], 890, 'docs_api_v2', '{"format": "openapi"}', 'john.doe', NOW() - INTERVAL '55 days'),
(2, 'Video Tutorials', 'FILE_SYSTEM', '{"path": "/docs/videos", "transcripts": true}', ARRAY['INACTIVE'], 'WEEKLY', NOW() - INTERVAL '8 days', ARRAY['FAILED'], 145, 'docs_videos_v1', '{"format": "transcript"}', 'john.doe', NOW() - INTERVAL '50 days'),

-- Fuentes para Legal Documents Q&A (ID 3)
(3, 'SharePoint Legal', 'SHAREPOINT', '{"site_url": "sharepoint.com/legal", "folder": "Contracts"}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '8 hours', ARRAY['SUCCESS'], 3456, 'legal_sharepoint_v1', '{"confidential": true}', 'jane.smith', NOW() - INTERVAL '90 days'),

-- Fuentes para Technical Support Chatbot (ID 4)
(4, 'Confluence Tech Docs', 'CONFLUENCE', '{"space_key": "TECH", "api_token": "***"}', ARRAY['ACTIVE'], 'HOURLY', NOW() - INTERVAL '3 hours', ARRAY['SUCCESS'], 1890, 'tech_confluence_v3', '{"category": "technical"}', 'admin', NOW() - INTERVAL '45 days'),
(4, 'JIRA Tickets Archive', 'API', '{"project_key": "SUPPORT", "issue_types": ["Bug", "Task"]}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '10 hours', ARRAY['SUCCESS'], 8765, 'tech_jira_v3', '{"resolved_only": true}', 'admin', NOW() - INTERVAL '40 days'),

-- Fuentes para HR Policy Assistant (ID 6)
(6, 'HR Documents Library', 'FILE_SYSTEM', '{"path": "/hr/policies", "extensions": ["pdf", "docx"]}', ARRAY['ACTIVE'], 'WEEKLY', NOW() - INTERVAL '2 days', ARRAY['SUCCESS'], 234, 'hr_docs_v1', '{"department": "HR"}', 'sarah.jones', NOW() - INTERVAL '75 days'),

-- Fuentes para Research Papers Search (ID 7)
(7, 'ArXiv Papers', 'API', '{"categories": ["cs.AI", "cs.LG"], "date_range": "2020-2024"}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '5 hours', ARRAY['SUCCESS'], 15678, 'research_arxiv_v2', '{"source": "arxiv"}', 'dr.martinez', NOW() - INTERVAL '120 days'),
(7, 'Internal Research DB', 'DATABASE', '{"connection_string": "postgresql://...", "table": "papers"}', ARRAY['ACTIVE'], 'HOURLY', NOW() - INTERVAL '1 hour', ARRAY['SUCCESS'], 4567, 'research_internal_v2', '{"proprietary": true}', 'dr.martinez', NOW() - INTERVAL '115 days'),

-- Fuentes para Financial Reports Analyzer (ID 8)
(8, 'Financial Reports Folder', 'FILE_SYSTEM', '{"path": "/finance/reports", "extensions": ["xlsx", "pdf"]}', ARRAY['ACTIVE'], 'DAILY', NOW() - INTERVAL '14 hours', ARRAY['SUCCESS'], 1234, 'finance_reports_v1', '{"confidential": true}', 'cfo.office', NOW() - INTERVAL '100 days'),

-- Fuentes para Code Documentation Assistant (ID 10)
(10, 'GitHub Repositories', 'GIT', '{"org": "company", "include_readme": true}', ARRAY['ACTIVE'], 'HOURLY', NOW() - INTERVAL '2 hours', ARRAY['SUCCESS'], 5678, 'code_github_v2', '{"language": "all"}', 'dev.lead', NOW() - INTERVAL '80 days'),

-- Fuentes para Contract Review Assistant (ID 12)
(12, 'Contracts Archive', 'FILE_SYSTEM', '{"path": "/legal/contracts", "extensions": ["pdf", "docx"]}', ARRAY['ACTIVE'], 'WEEKLY', NOW() - INTERVAL '3 days', ARRAY['SUCCESS'], 2345, 'contracts_archive_v1', '{"confidential": true}', 'legal.team', NOW() - INTERVAL '50 days');

-- Comentarios
COMMENT ON TABLE RAGRAGSYSTEMS IS 'Sistemas RAG de prueba con diferentes tipos y estados';
COMMENT ON TABLE RAGRAGVERSIONS IS 'Versiones de sistemas RAG para tracking de cambios';
COMMENT ON TABLE RAGRAGDATASOURCES IS 'Fuentes de datos diversas para sistemas RAG';

