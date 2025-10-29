-- ============================================================================
-- MOCK DATA - GOVERNANCE MODULE
-- ============================================================================

-- Insertar políticas de governance de ejemplo
INSERT INTO GOVPOLICIES (
    NAME, DESCRIPTION, CATEGORY, ENFORCEMENTLEVEL, POLICYTYPE, STATUS, VERSION,
    CREATEDBY, CREATEDAT, UPDATEDAT, EFFECTIVEDATE, EXPIRATIONDATE, METADATA
) VALUES 
-- Políticas de Compliance
('GDPR Compliance Policy', 'Política de cumplimiento con el Reglamento General de Protección de Datos de la UE', 'COMPLIANCE', ARRAY['HIGH'], ARRAY['COMPLIANCE'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"framework": "GDPR", "jurisdiction": "EU"}'),

('SOC 2 Type II Policy', 'Política de cumplimiento con SOC 2 Type II para servicios de seguridad', 'COMPLIANCE', ARRAY['HIGH'], ARRAY['COMPLIANCE'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '6 months', '{"framework": "SOC2", "type": "Type II"}'),

('ISO 27001 Policy', 'Política de gestión de seguridad de la información según ISO 27001', 'COMPLIANCE', ARRAY['MEDIUM'], ARRAY['COMPLIANCE'], ARRAY['ACTIVE'], 2, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '2 years', '{"framework": "ISO27001", "certification": "required"}'),

-- Políticas de Seguridad
('Data Encryption Policy', 'Política de encriptación de datos en reposo y en tránsito', 'SECURITY', ARRAY['HIGH'], ARRAY['SECURITY'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"encryption_standard": "AES-256", "key_management": "HSM"}'),

('Access Control Policy', 'Política de control de acceso basado en roles y permisos', 'SECURITY', ARRAY['HIGH'], ARRAY['SECURITY'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"access_model": "RBAC", "mfa_required": true}'),

('Incident Response Policy', 'Política de respuesta a incidentes de seguridad', 'SECURITY', ARRAY['MEDIUM'], ARRAY['SECURITY'], ARRAY['ACTIVE'], 3, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"response_time": "4 hours", "escalation": "automated"}'),

-- Políticas de Protección de Datos
('Data Retention Policy', 'Política de retención y eliminación de datos personales', 'DATA_PROTECTION', ARRAY['HIGH'], ARRAY['DATA_PROTECTION'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"retention_period": "7 years", "auto_deletion": true}'),

('Data Anonymization Policy', 'Política de anonimización de datos para análisis y desarrollo', 'DATA_PROTECTION', ARRAY['MEDIUM'], ARRAY['DATA_PROTECTION'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"anonymization_method": "k-anonymity", "k_value": 5}'),

-- Políticas Éticas
('AI Ethics Policy', 'Política ética para el desarrollo y uso de sistemas de inteligencia artificial', 'ETHICS', ARRAY['HIGH'], ARRAY['ETHICS'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"principles": ["fairness", "transparency", "accountability"]}'),

('Algorithmic Bias Policy', 'Política para prevenir y mitigar sesgos algorítmicos', 'ETHICS', ARRAY['MEDIUM'], ARRAY['ETHICS'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"bias_testing": "mandatory", "review_frequency": "quarterly"}'),

-- Políticas Operacionales
('Change Management Policy', 'Política de gestión de cambios en sistemas de producción', 'OPERATIONAL', ARRAY['MEDIUM'], ARRAY['OPERATIONAL'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"approval_process": "multi-stage", "rollback_plan": "required"}'),

('Backup and Recovery Policy', 'Política de respaldo y recuperación de datos críticos', 'OPERATIONAL', ARRAY['HIGH'], ARRAY['OPERATIONAL'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"backup_frequency": "daily", "retention": "30 days"}'),

-- Políticas en borrador
('Cloud Security Policy', 'Política de seguridad para servicios en la nube', 'SECURITY', ARRAY['HIGH'], ARRAY['SECURITY'], ARRAY['DRAFT'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"cloud_providers": ["AWS", "Azure", "GCP"]}'),

('Remote Work Security Policy', 'Política de seguridad para trabajo remoto', 'SECURITY', ARRAY['MEDIUM'], ARRAY['SECURITY'], ARRAY['PENDING_APPROVAL'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"vpn_required": true, "device_management": "MDM"}'),

-- Políticas expiradas
('Legacy System Policy', 'Política para sistemas legacy (expirada)', 'OPERATIONAL', ARRAY['LOW'], ARRAY['OPERATIONAL'], ARRAY['INACTIVE'], 1, 1, NOW() - INTERVAL '2 years', NOW() - INTERVAL '1 year', NOW() - INTERVAL '2 years', NOW() - INTERVAL '1 year', '{"legacy_systems": ["System A", "System B"]}')
ON CONFLICT (NAME) DO NOTHING;

-- Insertar reglas de políticas
INSERT INTO GOVPOLICYRULES (
    NAME, CONDITIONTEXT, ACTIONTEXT, PRIORITY, ISACTIVE, CREATEDAT, UPDATEDAT, IDGOVPOLICIES0
) VALUES 
-- Reglas para GDPR Compliance Policy
('GDPR Data Processing Rule', 'data_type IN ("personal", "sensitive") AND processing_purpose IS NOT NULL', 'require_explicit_consent AND log_processing_activity', 1, true, NOW(), NOW(), 1),

('GDPR Data Subject Rights Rule', 'data_subject_request_type IN ("access", "rectification", "erasure")', 'process_within_30_days AND notify_data_subject', 2, true, NOW(), NOW(), 1),

-- Reglas para Data Encryption Policy
('Encryption at Rest Rule', 'data_classification IN ("confidential", "restricted") AND storage_type = "database"', 'encrypt_with_AES256 AND use_HSM_keys', 1, true, NOW(), NOW(), 4),

('Encryption in Transit Rule', 'communication_protocol NOT IN ("HTTPS", "TLS1.3")', 'block_communication AND log_security_violation', 1, true, NOW(), NOW(), 4),

-- Reglas para Access Control Policy
('Role-Based Access Rule', 'user_role NOT IN authorized_roles AND resource_sensitivity = "high"', 'deny_access AND log_access_attempt', 1, true, NOW(), NOW(), 5),

('MFA Requirement Rule', 'authentication_method = "password_only" AND user_privileges = "admin"', 'require_mfa_authentication', 2, true, NOW(), NOW(), 5),

-- Reglas para AI Ethics Policy
('Bias Detection Rule', 'model_training_data_contains_protected_attributes', 'run_bias_analysis AND require_approval', 1, true, NOW(), NOW(), 9),

('Transparency Rule', 'model_decision_affects_human_life AND explainability_score < 0.8', 'require_human_review AND document_reasoning', 2, true, NOW(), NOW(), 9)
ON CONFLICT DO NOTHING;

-- Insertar evaluaciones de políticas
INSERT INTO GOVPOLICYEVALUATIONS (
    ARTIFACTTYPE, ARTIFACTID, EVALUATIONTYPE, STATUS, RESULT, CONFIDENCESCORE, 
    EXECUTIONTIMEMS, EVALUATEDBY, EVALUATEDAT, IDGOVPOLICIES0
) VALUES 
-- Evaluaciones para GDPR Compliance Policy
(ARRAY['MODEL'], 'model_001', ARRAY['COMPLIANCE_CHECK'], ARRAY['SUCCESS'], '{"gdpr_compliant": true, "data_processing_lawful": true, "consent_mechanism": true}', 95.5, 1200, 1, NOW() - INTERVAL '2 days', 1),

(ARRAY['MODEL'], 'model_002', ARRAY['COMPLIANCE_CHECK'], ARRAY['WARNING'], '{"gdpr_compliant": false, "missing_consent": true, "data_retention_issue": false}', 75.2, 980, 1, NOW() - INTERVAL '5 days', 1),

-- Evaluaciones para Data Encryption Policy
(ARRAY['SYSTEM'], 'payment_system', ARRAY['SECURITY_AUDIT'], ARRAY['SUCCESS'], '{"encryption_at_rest": true, "encryption_in_transit": true, "key_management": true}', 98.1, 800, 1, NOW() - INTERVAL '1 day', 4),

(ARRAY['SYSTEM'], 'legacy_database', ARRAY['SECURITY_AUDIT'], ARRAY['ERROR'], '{"encryption_at_rest": false, "encryption_in_transit": false, "key_management": false}', 15.3, 1500, 1, NOW() - INTERVAL '3 days', 4),

-- Evaluaciones para AI Ethics Policy
(ARRAY['MODEL'], 'hiring_model', ARRAY['BIAS_ASSESSMENT'], ARRAY['SUCCESS'], '{"gender_bias": 0.02, "age_bias": 0.01, "ethnicity_bias": 0.03}', 92.7, 2100, 1, NOW() - INTERVAL '1 week', 9),

(ARRAY['MODEL'], 'credit_scoring_model', ARRAY['BIAS_ASSESSMENT'], ARRAY['WARNING'], '{"gender_bias": 0.15, "age_bias": 0.08, "ethnicity_bias": 0.12}', 68.4, 1800, 1, NOW() - INTERVAL '4 days', 9)
ON CONFLICT DO NOTHING;

-- Insertar violaciones de políticas
INSERT INTO GOVPOLICYVIOLATIONS (
    VIOLATIONTITLE, DESCRIPTION, SEVERITY, STATUS, POLICYNAME, POLICYVERSION,
    DETECTEDAT, DETECTEDBY, ASSIGNEDTO, RESOLUTIONPLAN, CREATEDAT, UPDATEDAT
) VALUES 
-- Violaciones críticas
('Unencrypted Personal Data Found', 'Personal data found stored in plain text in legacy database system', 'CRITICAL', ARRAY['OPEN'], 'Data Encryption Policy', '1.0', NOW() - INTERVAL '2 days', 'security_scanner', 'security_team_lead', 'Implement AES-256 encryption and migrate to secure storage', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),

('GDPR Consent Missing', 'User data processed without explicit consent for marketing purposes', 'CRITICAL', ARRAY['IN_PROGRESS'], 'GDPR Compliance Policy', '1.0', NOW() - INTERVAL '1 day', 'compliance_monitor', 'legal_team', 'Implement consent management platform and update privacy policy', NOW() - INTERVAL '1 day', NOW()),

-- Violaciones de alta severidad
('Admin Access Without MFA', 'Administrator account accessed using only password authentication', 'HIGH', ARRAY['RESOLVED'], 'Access Control Policy', '1.0', NOW() - INTERVAL '1 week', 'access_log_monitor', 'security_team', 'Enabled MFA for all admin accounts and reviewed access logs', NOW() - INTERVAL '1 week', NOW() - INTERVAL '3 days'),

('Algorithmic Bias Detected', 'Hiring model shows 15% gender bias in favor of male candidates', 'HIGH', ARRAY['OPEN'], 'AI Ethics Policy', '1.0', NOW() - INTERVAL '4 days', 'bias_detection_tool', 'ml_team_lead', 'Retrain model with balanced dataset and implement fairness constraints', NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days'),

-- Violaciones de severidad media
('Backup Policy Non-compliance', 'Daily backup not performed for critical system', 'MEDIUM', ARRAY['RESOLVED'], 'Backup and Recovery Policy', '1.0', NOW() - INTERVAL '2 weeks', 'backup_monitor', 'operations_team', 'Fixed backup script and implemented monitoring alerts', NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '1 week'),

('Change Management Bypass', 'Production deployment performed without proper change approval', 'MEDIUM', ARRAY['OPEN'], 'Change Management Policy', '1.0', NOW() - INTERVAL '6 days', 'deployment_monitor', 'devops_team', 'Implement deployment gates and approval workflow', NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days')
ON CONFLICT DO NOTHING;

-- Insertar evaluaciones de cumplimiento
INSERT INTO GOVCOMPLIANCEASSESSMENTS (
    ASSESSMENTNAME, DESCRIPTION, COMPLIANCEFRAMEWORK, STATUS, OVERALLSCORE,
    COMPLIANCEPERCENTAGE, ASSESSMENTDATE, VALIDUNTIL, ASSESSORNAME, ASSESSOREMAIL,
    CREATEDAT, UPDATEDAT
) VALUES 
-- Evaluaciones de cumplimiento
('Q1 2024 GDPR Assessment', 'Quarterly assessment of GDPR compliance across all data processing activities', ARRAY['GDPR'], ARRAY['COMPLETED'], 87.5, 87.5, NOW() - INTERVAL '1 month', NOW() + INTERVAL '2 months', 'Dr. Sarah Johnson', 'sarah.johnson@company.com', NOW() - INTERVAL '1 month', NOW() - INTERVAL '1 month'),

('SOC 2 Type II Annual Review', 'Annual SOC 2 Type II compliance review and audit preparation', ARRAY['SOC2'], ARRAY['IN_PROGRESS'], 92.1, 92.1, NOW() - INTERVAL '2 weeks', NOW() + INTERVAL '10 months', 'Michael Chen', 'michael.chen@audit.com', NOW() - INTERVAL '2 weeks', NOW() - INTERVAL '2 weeks'),

('ISO 27001 Implementation Review', 'Review of ISO 27001 security management system implementation', ARRAY['ISO27001'], ARRAY['COMPLETED'], 78.9, 78.9, NOW() - INTERVAL '3 months', NOW() + INTERVAL '9 months', 'Emma Rodriguez', 'emma.rodriguez@security.com', NOW() - INTERVAL '3 months', NOW() - INTERVAL '3 months'),

('AI Ethics Framework Assessment', 'Assessment of AI ethics policy implementation and algorithmic fairness', ARRAY['CUSTOM'], ARRAY['COMPLETED'], 84.2, 84.2, NOW() - INTERVAL '2 months', NOW() + INTERVAL '10 months', 'Dr. Alex Kim', 'alex.kim@ethics.com', NOW() - INTERVAL '2 months', NOW() - INTERVAL '2 months'),

('Data Protection Impact Assessment', 'DPIA for new customer data processing system', ARRAY['GDPR'], ARRAY['PENDING'], NULL, NULL, NOW(), NOW() + INTERVAL '6 months', 'Lisa Thompson', 'lisa.thompson@privacy.com', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Insertar más políticas para tener datos suficientes
INSERT INTO GOVPOLICIES (
    NAME, DESCRIPTION, CATEGORY, ENFORCEMENTLEVEL, POLICYTYPE, STATUS, VERSION,
    CREATEDBY, CREATEDAT, UPDATEDAT, EFFECTIVEDATE, EXPIRATIONDATE, METADATA
) VALUES 
('API Security Policy', 'Política de seguridad para APIs y servicios web', 'SECURITY', ARRAY['HIGH'], ARRAY['SECURITY'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"rate_limiting": true, "authentication": "OAuth2"}'),

('Code Review Policy', 'Política de revisión de código y estándares de desarrollo', 'OPERATIONAL', ARRAY['MEDIUM'], ARRAY['OPERATIONAL'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"review_required": true, "approvers": 2}'),

('Third Party Risk Policy', 'Política de evaluación y gestión de riesgos de terceros', 'COMPLIANCE', ARRAY['HIGH'], ARRAY['COMPLIANCE'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"assessment_frequency": "annual", "risk_threshold": "medium"}'),

('Business Continuity Policy', 'Política de continuidad del negocio y recuperación ante desastres', 'OPERATIONAL', ARRAY['HIGH'], ARRAY['OPERATIONAL'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"rto": "4 hours", "rpo": "1 hour"}'),

('Software License Compliance', 'Política de cumplimiento de licencias de software', 'COMPLIANCE', ARRAY['MEDIUM'], ARRAY['COMPLIANCE'], ARRAY['ACTIVE'], 1, 1, NOW(), NOW(), NOW(), NOW() + INTERVAL '1 year', '{"audit_frequency": "quarterly", "auto_detection": true}')
ON CONFLICT (NAME) DO NOTHING;

-- Comentarios
COMMENT ON TABLE GOVPOLICIES IS 'Tabla de políticas de governance organizacional';
COMMENT ON TABLE GOVPOLICYRULES IS 'Reglas asociadas a las políticas de governance';
COMMENT ON TABLE GOVPOLICYEVALUATIONS IS 'Evaluaciones automáticas de cumplimiento de políticas';
COMMENT ON TABLE GOVPOLICYVIOLATIONS IS 'Violaciones detectadas de las políticas de governance';
COMMENT ON TABLE GOVCOMPLIANCEASSESSMENTS IS 'Evaluaciones de cumplimiento con frameworks regulatorios';

-- Mensaje de confirmación
DO $$
BEGIN
    RAISE NOTICE 'Datos mock para Governance insertados exitosamente:';
    RAISE NOTICE '- % políticas de governance', (SELECT COUNT(*) FROM GOVPOLICIES);
    RAISE NOTICE '- % reglas de políticas', (SELECT COUNT(*) FROM GOVPOLICYRULES);
    RAISE NOTICE '- % evaluaciones de políticas', (SELECT COUNT(*) FROM GOVPOLICYEVALUATIONS);
    RAISE NOTICE '- % violaciones de políticas', (SELECT COUNT(*) FROM GOVPOLICYVIOLATIONS);
    RAISE NOTICE '- % evaluaciones de cumplimiento', (SELECT COUNT(*) FROM GOVCOMPLIANCEASSESSMENTS);
END $$;
