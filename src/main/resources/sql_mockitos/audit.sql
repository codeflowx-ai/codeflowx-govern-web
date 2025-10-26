-- Funciones para generar datos de prueba de Compliance y Audit

-- Función para generar datos de monitorización de compliance
CREATE OR REPLACE FUNCTION generate_compliance_monitoring() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    monitoring_date timestamp;
    risk_levels text[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEMONITORING CASCADE;
    
    -- Generar datos de monitorización para los últimos 30 días
    FOR i IN 1..300 LOOP
        model_id := 1 + mod(i, 20); -- Asumiendo 20 modelos
        monitoring_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESCOMPLIANCEMONITORING (
            idxslescompliancemonitoring,
            idslesmodel,
            monitoringdate,
            compliancemetrics,
            violations,
            correctiveactions
        ) VALUES (
            i,
            model_id,
            monitoring_date,
            jsonb_build_object(
                'compliance_score', 60 + random() * 40,
                'risk_level', risk_levels[1 + mod(i, 4)],
                'gdpr_compliance', random() > 0.1,
                'hipaa_compliance', random() > 0.1,
                'ccpa_compliance', random() > 0.1
            ),
            CASE WHEN random() < 0.3 THEN
                jsonb_build_object(
                    'count', floor(random() * 3),
                    'details', array[
                        'Data retention policy violation',
                        'Unauthorized access attempt',
                        'Missing consent record'
                    ]
                )
            ELSE NULL END,
            CASE WHEN random() < 0.3 THEN
                jsonb_build_object(
                    'actions', array[
                        'Update data retention settings',
                        'Implement additional access controls',
                        'Review consent management process'
                    ],
                    'status', 'IN_PROGRESS',
                    'deadline', current_date + interval '7 days'
                )
            ELSE NULL END
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar incidentes de compliance
CREATE OR REPLACE FUNCTION generate_compliance_incidents() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    incident_date timestamp;
    severity_levels text[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEINCIDENT CASCADE;
    
    -- Generar incidentes para los últimos 30 días
    FOR i IN 1..50 LOOP
        model_id := 1 + mod(i, 20);
        incident_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESCOMPLIANCEINCIDENT (
            idxsleschatincident,
            idslesmodel,
            incidentdate,
            severity,
            description,
            affectedusers,
            mitigationactions
        ) VALUES (
            i,
            model_id,
            incident_date,
            severity_levels[1 + mod(i, 4)],
            'Compliance incident #' || i || ': ' || CASE mod(i, 3)
                WHEN 0 THEN 'Data privacy violation detected'
                WHEN 1 THEN 'Unauthorized model access'
                ELSE 'Regulatory requirement breach'
            END,
            jsonb_build_object(
                'count', floor(random() * 100),
                'impact_level', severity_levels[1 + mod(i, 4)],
                'regions', array['EU', 'US', 'ASIA']
            ),
            jsonb_build_object(
                'immediate_actions', array[
                    'System lockdown',
                    'User notification',
                    'Regulatory report'
                ],
                'long_term_actions', array[
                    'Policy review',
                    'Training update',
                    'System enhancement'
                ]
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de auditoría
CREATE OR REPLACE FUNCTION generate_audit_data() 
RETURNS void AS $$
DECLARE
    i integer;
    audit_date timestamp;
    action_types text[] := ARRAY['MODEL_ACCESS', 'DATA_MODIFICATION', 'CONFIGURATION_CHANGE', 'SECURITY_EVENT'];
    user_roles text[] := ARRAY['ADMIN', 'DEVELOPER', 'ANALYST', 'AUDITOR'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESDATAAUDIT CASCADE;
    
    -- Generar registros de auditoría para los últimos 30 días
    FOR i IN 1..500 LOOP
        audit_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESDATAAUDIT (
            idxslesdataaudit,
            auditdate,
            actiontype,
            userid,
            actiondetails,
            complianceimpact
        ) VALUES (
            i,
            audit_date,
            action_types[1 + mod(i, 4)],
            user_roles[1 + mod(i, 4)] || '_USER_' || mod(i, 10),
            jsonb_build_object(
                'action', action_types[1 + mod(i, 4)],
                'resource', 'Model_' || mod(i, 20),
                'details', 'Action performed by ' || user_roles[1 + mod(i, 4)],
                'ip_address', '192.168.1.' || mod(i, 255)::text
            ),
            jsonb_build_object(
                'impact_level', CASE 
                    WHEN mod(i, 4) = 0 THEN 'HIGH'
                    WHEN mod(i, 4) = 1 THEN 'MEDIUM'
                    ELSE 'LOW'
                END,
                'compliance_rules', array['GDPR', 'HIPAA', 'CCPA'],
                'risk_assessment', floor(random() * 100)
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar certificaciones
CREATE OR REPLACE FUNCTION generate_certifications() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    cert_types text[] := ARRAY['ISO27001', 'SOC2', 'HIPAA', 'GDPR', 'AI_CERT'];
    valid_from timestamp;
    valid_until timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCERTIFICATION CASCADE;
    
    -- Generar certificaciones para cada modelo
    FOR i IN 1..60 LOOP
        model_id := 1 + mod(i, 20);
        valid_from := current_timestamp - interval '6 months';
        valid_until := valid_from + interval '1 year';
        
        INSERT INTO SLESCERTIFICATION (
            idxslescertification,
            idslesmodel,
            certificationname,
            certificationtype,
            certificationlevel,
            validfrom,
            validuntil,
            criteria,
            evaluationresults
        ) VALUES (
            i,
            model_id,
            cert_types[1 + mod(i, 5)] || ' Certification',
            cert_types[1 + mod(i, 5)],
            CASE mod(i, 3)
                WHEN 0 THEN 'BASIC'
                WHEN 1 THEN 'ADVANCED'
                ELSE 'EXPERT'
            END,
            valid_from,
            valid_until,
            jsonb_build_object(
                'requirements', array[
                    'Security controls',
                    'Data protection',
                    'Process documentation'
                ],
                'standards', array[
                    'ISO27001:2013',
                    'NIST SP 800-53',
                    'AI Act requirements'
                ]
            ),
            jsonb_build_object(
                'score', 80 + random() * 20,
                'findings', array[
                    'Minor documentation gaps',
                    'Recommended improvements'
                ],
                'auditor_notes', 'Certification audit completed successfully'
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de privacidad
CREATE OR REPLACE FUNCTION generate_privacy_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESDATAPRIVACY CASCADE;
    
    -- Generar datos de privacidad para cada modelo
    FOR i IN 1..20 LOOP
        model_id := i;
        
        INSERT INTO SLESDATAPRIVACY (
            idxslesdataprivacy,
            idslesmodel,
            consenttype,
            dataprivacyconfig,
            maskingrules,
            retentionpolicy
        ) VALUES (
            i,
            model_id,
            CASE mod(i, 3)
                WHEN 0 THEN 'EXPLICIT'
                WHEN 1 THEN 'IMPLICIT'
                ELSE 'MANDATORY'
            END,
            jsonb_build_object(
                'data_classification', array['PII', 'SENSITIVE', 'PUBLIC'],
                'encryption_level', 'AES-256',
                'access_controls', jsonb_build_object(
                    'role_based', true,
                    'mfa_required', true,
                    'ip_restrictions', array['10.0.0.0/24']
                )
            ),
            jsonb_build_object(
                'pii_fields', array['email', 'phone', 'address'],
                'masking_patterns', jsonb_build_object(
                    'email', '*****@domain.com',
                    'phone', '***-***-1234',
                    'address', '*** Main St'
                )
            ),
            jsonb_build_object(
                'retention_period', '90 days',
                'deletion_policy', 'HARD_DELETE',
                'archival_rules', array[
                    'Archive after 30 days',
                    'Delete after 90 days'
                ]
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba de compliance
CREATE OR REPLACE FUNCTION generate_all_compliance_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_compliance_monitoring();
    PERFORM generate_compliance_incidents();
    PERFORM generate_audit_data();
    PERFORM generate_certifications();
    PERFORM generate_privacy_data();
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar la generación completa de datos:
SELECT generate_all_compliance_test_data();

-- Para ejecutar funciones individuales:
-- SELECT generate_compliance_monitoring();
-- SELECT generate_compliance_incidents();
-- SELECT generate_audit_data();
-- SELECT generate_certifications();
-- SELECT generate_privacy_data();