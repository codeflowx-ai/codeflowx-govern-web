-- ============================================================================
-- MOCK DATA - PROMPTS MODULE
-- ============================================================================

-- Insertar prompts de ejemplo
INSERT INTO PRMPROMPTS (
    PRMNAME, PRMDESCRIPTION, PRMTYPE, PRMCATEGORY, PRMVERSION, PRMSTATUS,
    PRMCONTENT, PRMPARAMETERS, PRMMETADATA, PRMAPPROVALSTATUS,
    PRMAPPROVEDBY, PRMAPPROVEDAT, PRMCREATEDBY, PRMUPDATEDBY, PRMCREATEDAT, PRMUPDATEDAT
) VALUES 
-- System Prompts
('System - Code Assistant', 'Prompt de sistema para asistente de código profesional', ARRAY['SYSTEM'], 'CODING', 'v1.0', ARRAY['ACTIVE'], 'You are a professional code assistant. Help users write clean, efficient, and well-documented code. Always explain your reasoning and suggest best practices.', '{"temperature": 0.7, "max_tokens": 2000, "top_p": 0.9}', '{"language": "multi", "expertise": "senior", "style": "professional"}', ARRAY['APPROVED'], 'tech_lead', NOW() - INTERVAL '1 week', 'admin', 'admin', NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '1 week'),

('System - Data Analyst', 'Prompt de sistema para análisis de datos', ARRAY['SYSTEM'], 'ANALYSIS', 'v1.0', ARRAY['ACTIVE'], 'You are an expert data analyst. Analyze datasets, identify patterns, and provide actionable insights. Use statistical methods and visualization recommendations.', '{"temperature": 0.3, "max_tokens": 1500, "top_p": 0.95}', '{"domain": "analytics", "output_format": "structured"}', ARRAY['APPROVED'], 'data_lead', NOW() - INTERVAL '5 days', 'admin', NULL, NOW() - INTERVAL '10 days', NOW() - INTERVAL '5 days'),

('System - Creative Writer', 'Prompt de sistema para escritura creativa', ARRAY['SYSTEM'], 'CREATIVE', 'v2.1', ARRAY['ACTIVE'], 'You are a creative writing assistant. Help users craft engaging stories, articles, and content. Focus on narrative structure, character development, and vivid descriptions.', '{"temperature": 0.9, "max_tokens": 3000, "top_p": 0.85, "frequency_penalty": 0.3}', '{"tone": "engaging", "audience": "general", "genres": ["fiction", "non-fiction"]}', ARRAY['APPROVED'], 'content_lead', NOW() - INTERVAL '3 days', 'admin', 'admin', NOW() - INTERVAL '1 month', NOW() - INTERVAL '3 days'),

-- User Prompts
('User - Bug Report Generator', 'Genera reportes estructurados de bugs a partir de descripciones informales', ARRAY['USER'], 'CODING', 'v1.0', ARRAY['ACTIVE'], 'Based on the following bug description, create a structured bug report with: Title, Description, Steps to Reproduce, Expected Behavior, Actual Behavior, and Severity.', '{"temperature": 0.5, "max_tokens": 800}', '{"template": "bug_report", "fields_required": ["title", "description", "steps", "expected", "actual", "severity"]}', ARRAY['APPROVED'], 'qa_lead', NOW() - INTERVAL '2 days', 'dev_team', NULL, NOW() - INTERVAL '1 week', NOW() - INTERVAL '2 days'),

('User - SQL Query Optimizer', 'Optimiza consultas SQL y sugiere mejoras de performance', ARRAY['USER'], 'CODING', 'v1.2', ARRAY['ACTIVE'], 'Analyze the following SQL query and suggest optimizations. Consider indexes, query structure, and best practices. Explain each suggestion.', '{"temperature": 0.4, "max_tokens": 1200}', '{"database": "postgresql", "focus": "performance"}', ARRAY['APPROVED'], 'db_admin', NOW() - INTERVAL '6 days', 'dev_team', 'dev_team', NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '6 days'),

('User - Meeting Summarizer', 'Genera resúmenes ejecutivos de transcripciones de reuniones', ARRAY['USER'], 'ANALYSIS', 'v1.0', ARRAY['ACTIVE'], 'Summarize the following meeting transcript. Include: Key Discussion Points, Decisions Made, Action Items, and Follow-up Required.', '{"temperature": 0.5, "max_tokens": 1000}', '{"format": "executive_summary", "sections": ["discussion", "decisions", "actions", "followup"]}', ARRAY['PENDING'], NULL, NULL, 'ops_team', NULL, NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days'),

-- Assistant Prompts
('Assistant - Code Reviewer', 'Asistente para revisión de código con enfoque en calidad', ARRAY['ASSISTANT'], 'CODING', 'v1.0', ARRAY['ACTIVE'], 'Review the provided code for: 1) Code quality and readability, 2) Potential bugs, 3) Performance issues, 4) Security vulnerabilities, 5) Best practices adherence.',
'{"temperature": 0.3, "max_tokens": 1500}',
'{"review_depth": "thorough", "languages": ["python", "javascript", "java"], "focus_areas": ["security", "performance"]}',
'APPROVED', 'senior_dev', NOW() - INTERVAL '1 week', 'dev_team', NULL, NOW() - INTERVAL '3 weeks', NOW() - INTERVAL '1 week'),

('Assistant - Technical Documentation', 'Asistente para generar documentación técnica', ARRAY['ASSISTANT'], 'GENERAL', 'v1.5', ARRAY['ACTIVE'], 'Generate comprehensive technical documentation for the provided code/API. Include: Overview, Usage Examples, Parameters, Return Values, and Edge Cases.', '{"temperature": 0.4, "max_tokens": 2000}', '{"doc_style": "detailed", "include_examples": true, "api_format": "openapi"}', ARRAY['APPROVED'], 'tech_writer', NOW() - INTERVAL '8 days', 'dev_team', 'dev_team', NOW() - INTERVAL '1 month', NOW() - INTERVAL '8 days'),

-- Function Prompts
('Function - Extract Entities', 'Extrae entidades nombradas de texto no estructurado', ARRAY['FUNCTION'], 'ANALYSIS', 'v1.0', ARRAY['ACTIVE'], 'Extract named entities from the text: persons, organizations, locations, dates, and amounts. Return as JSON with entity type and confidence score.', '{"temperature": 0.2, "max_tokens": 500, "response_format": "json"}', '{"entity_types": ["PERSON", "ORG", "LOC", "DATE", "MONEY"], "min_confidence": 0.7}', ARRAY['APPROVED'], 'ml_lead', NOW() - INTERVAL '5 days', 'ml_team', NULL, NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '5 days'),

('Function - Sentiment Analysis', 'Analiza el sentimiento de texto y clasifica en positivo/negativo/neutro', ARRAY['FUNCTION'], 'ANALYSIS', 'v2.0', ARRAY['ACTIVE'], 'Analyze the sentiment of the provided text. Return JSON with: sentiment (positive/negative/neutral), confidence score, and key phrases.', '{"temperature": 0.1, "max_tokens": 300, "response_format": "json"}', '{"granularity": "sentence", "include_aspects": true}', ARRAY['APPROVED'], 'ml_lead', NOW() - INTERVAL '3 days', 'ml_team', 'ml_team', NOW() - INTERVAL '3 weeks', NOW() - INTERVAL '3 days'),

-- Prompts en Draft
('User - Report Generator V2', 'Nueva versión del generador de reportes (en desarrollo)', ARRAY['USER'], 'ANALYSIS', 'v2.0', ARRAY['DRAFT'], 'Generate comprehensive business reports with executive summary, detailed analysis, visualizations, and recommendations.', '{"temperature": 0.6, "max_tokens": 2500}', '{"report_types": ["quarterly", "annual", "project"], "include_charts": true}', ARRAY['PENDING'], NULL, NULL, 'analyst_team', 'analyst_team', NOW() - INTERVAL '2 days', NOW()),

('System - Multi-language Support', 'Prompt de sistema con soporte multi-idioma (testing)', ARRAY['SYSTEM'], 'GENERAL', 'v1.0', ARRAY['TESTING'], 'You are a multilingual assistant. Automatically detect the user language and respond accordingly. Support: English, Spanish, French, German, and Portuguese.', '{"temperature": 0.7, "max_tokens": 1500}', '{"languages": ["en", "es", "fr", "de", "pt"], "auto_detect": true}', ARRAY['PENDING'], NULL, NULL, 'i18n_team', NULL, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),

-- Prompts Deprecated
('User - Legacy SQL Generator', 'Generador SQL antiguo (deprecado, usar v2)', ARRAY['USER'], 'CODING', 'v0.9', ARRAY['DEPRECATED'], 'Generate SQL queries based on natural language descriptions. (DEPRECATED: Use SQL Query Optimizer v1.2 instead)', '{"temperature": 0.5, "max_tokens": 800}', '{"sql_standard": "SQL92", "deprecated": true, "replacement": "SQL Query Optimizer v1.2"}', ARRAY['REJECTED'], NULL, NULL, 'dev_team', NULL, NOW() - INTERVAL '6 months', NOW() - INTERVAL '3 months'),

-- Prompts adicionales para variety
('Function - Data Validation', 'Valida datos de entrada contra esquemas JSON', ARRAY['FUNCTION'], 'CODING', 'v1.0', ARRAY['ACTIVE'], 'Validate the provided data against the JSON schema. Return validation result with detailed errors if any.', '{"temperature": 0.1, "max_tokens": 500, "response_format": "json"}', '{"strict_mode": true, "return_errors": true}', ARRAY['APPROVED'], 'backend_lead', NOW() - INTERVAL '10 days', 'dev_team', NULL, NOW() - INTERVAL '3 weeks', NOW() - INTERVAL '10 days'),

('Assistant - API Designer', 'Asistente para diseño de APIs RESTful', ARRAY['ASSISTANT'], 'CODING', 'v1.0', ARRAY['ACTIVE'], 'Design a RESTful API for the described use case. Include: Endpoints, HTTP methods, Request/Response formats, Status codes, and Error handling.', '{"temperature": 0.4, "max_tokens": 1800}', '{"api_style": "REST", "include_auth": true, "versioning": "semantic"}', ARRAY['APPROVED'], 'api_architect', NOW() - INTERVAL '12 days', 'dev_team', NULL, NOW() - INTERVAL '4 weeks', NOW() - INTERVAL '12 days')

ON CONFLICT (PRMNAME) DO NOTHING;

-- Insertar versiones de prompts
INSERT INTO PRMPROMPTVERSIONS (
    PRMVERSION, PRMDESCRIPTION, PRMCONTENT, PRMPARAMETERS, PRMCHANGES, PRMSTATUS,
    PRMCREATEDBY, PRMUPDATEDBY, PRMCREATEDAT, PRMUPDATEDAT, IDPRMPROMPTS0
) VALUES 
-- Versiones de 'System - Creative Writer'
('v1.0', 'Versión inicial', 
'You are a creative writing assistant. Help users write engaging content.',
'{"temperature": 0.8, "max_tokens": 2000}',
'Initial release',
'DEPRECATED', 'admin', NULL, NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months', 3),

('v2.0', 'Mejoras en estructura narrativa',
'You are a creative writing assistant. Help users craft engaging stories, articles, and content. Focus on narrative structure and character development.',
'{"temperature": 0.85, "max_tokens": 2500}',
'Added focus on narrative structure',
'DEPRECATED', 'admin', NULL, NOW() - INTERVAL '1 month', NOW() - INTERVAL '1 month', 3),

('v2.1', 'Versión actual con descripciones vívidas',
'You are a creative writing assistant. Help users craft engaging stories, articles, and content. Focus on narrative structure, character development, and vivid descriptions.',
'{"temperature": 0.9, "max_tokens": 3000, "frequency_penalty": 0.3}',
'Added vivid descriptions and adjusted parameters',
'ACTIVE', 'admin', NULL, NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', 3),

-- Versiones de 'User - SQL Query Optimizer'
('v1.0', 'Versión inicial',
'Analyze the following SQL query and suggest optimizations.',
'{"temperature": 0.4, "max_tokens": 1000}',
'Initial release',
'DEPRECATED', 'dev_team', NULL, NOW() - INTERVAL '1 month', NOW() - INTERVAL '1 month', 5),

('v1.2', 'Versión actual con explicaciones detalladas',
'Analyze the following SQL query and suggest optimizations. Consider indexes, query structure, and best practices. Explain each suggestion.',
'{"temperature": 0.4, "max_tokens": 1200}',
'Added detailed explanations and best practices',
'ACTIVE', 'dev_team', NULL, NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days', 5)

ON CONFLICT DO NOTHING;

-- Insertar validaciones de prompts
INSERT INTO PRMPROMPTVALIDATIONS (
    PRMVALIDATIONTYPE, PRMVALIDATIONRESULT, PRMVALIDATIONSCORE, PRMVALIDATIONDETAILS,
    PRMISSUESFOUND, PRMRECOMMENDATIONS, PRMSTATUS, PRMVALIDATEDBY, PRMVALIDATEDAT,
    PRMCREATEDBY, PRMUPDATEDBY, PRMCREATEDAT, PRMUPDATEDAT, IDPRMPROMPTS0
) VALUES 
-- Validaciones para 'System - Code Assistant'
('CONTENT_QUALITY', 'PASSED', 95.5, 
'{"clarity": 98, "specificity": 93, "completeness": 95}',
'[]',
'["Consider adding examples for edge cases"]',
'COMPLETED', 'qa_team', NOW() - INTERVAL '1 week', 'qa_team', NULL, NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week', 1),

('PARAMETER_VALIDATION', 'PASSED', 100.0,
'{"temperature_valid": true, "max_tokens_valid": true, "top_p_valid": true}',
'[]',
'[]',
'COMPLETED', 'qa_team', NOW() - INTERVAL '1 week', 'qa_team', NULL, NOW() - INTERVAL '1 week', NOW() - INTERVAL '1 week', 1),

-- Validaciones para 'System - Data Analyst'
('CONTENT_QUALITY', 'PASSED', 92.3,
'{"clarity": 95, "specificity": 90, "completeness": 92}',
'[]',
'["Add more details about statistical methods"]',
'COMPLETED', 'qa_team', NOW() - INTERVAL '5 days', 'qa_team', NULL, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', 2),

-- Validaciones para 'User - Bug Report Generator'
('CONTENT_QUALITY', 'PASSED', 88.7,
'{"clarity": 90, "specificity": 88, "completeness": 88}',
'["Missing severity levels definition"]',
'["Define severity levels (Critical, High, Medium, Low)", "Add example output"]',
'COMPLETED', 'qa_team', NOW() - INTERVAL '2 days', 'qa_team', NULL, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 4),

('SECURITY_CHECK', 'PASSED', 100.0,
'{"no_pii_exposure": true, "no_injection_risk": true, "safe_parameters": true}',
'[]',
'[]',
'COMPLETED', 'security_team', NOW() - INTERVAL '2 days', 'security_team', NULL, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', 4),

-- Validación fallida para 'User - Meeting Summarizer' (pendiente de aprobación)
('CONTENT_QUALITY', 'FAILED', 65.2,
'{"clarity": 70, "specificity": 60, "completeness": 66}',
'["Unclear section definitions", "Missing example format"]',
'["Provide clear definitions for each section", "Add example summary output", "Specify handling of confidential information"]',
'COMPLETED', 'qa_team', NOW() - INTERVAL '3 days', 'qa_team', NULL, NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', 6),

-- Validaciones para 'Function - Extract Entities'
('ACCURACY_TEST', 'PASSED', 91.5,
'{"test_dataset": "standard_ner", "precision": 92, "recall": 91, "f1_score": 91.5}',
'[]',
'["Test with domain-specific data"]',
'COMPLETED', 'ml_team', NOW() - INTERVAL '5 days', 'ml_team', NULL, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', 9),

('PARAMETER_VALIDATION', 'PASSED', 98.0,
'{"temperature_optimal": true, "max_tokens_sufficient": true, "json_format_valid": true}',
'[]',
'[]',
'COMPLETED', 'ml_team', NOW() - INTERVAL '5 days', 'ml_team', NULL, NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', 9)

ON CONFLICT DO NOTHING;

-- Comentarios
COMMENT ON TABLE PRMPROMPTS IS 'Tabla principal de prompts para sistemas de IA';
COMMENT ON TABLE PRMPROMPTVERSIONS IS 'Versiones históricas de prompts';
COMMENT ON TABLE PRMPROMPTVALIDATIONS IS 'Validaciones de calidad y seguridad de prompts';

-- Mensaje de confirmación
DO $$
BEGIN
    RAISE NOTICE 'Datos mock para Prompts insertados exitosamente:';
    RAISE NOTICE '- % prompts', (SELECT COUNT(*) FROM PRMPROMPTS);
    RAISE NOTICE '- % versiones de prompts', (SELECT COUNT(*) FROM PRMPROMPTVERSIONS);
    RAISE NOTICE '- % validaciones de prompts', (SELECT COUNT(*) FROM PRMPROMPTVALIDATIONS);
END $$;

