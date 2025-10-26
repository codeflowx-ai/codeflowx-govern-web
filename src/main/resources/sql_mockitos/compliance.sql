-- Funciones para Compliance & Audit Dashboard

-- Función para generar registros de auditoría
CREATE OR REPLACE FUNCTION generate_audit_logs() 
RETURNS void AS $$
DECLARE
    i integer;
    audit_date timestamp;
    action_types text[] := ARRAY['MODEL_ACCESS', 'DATA_MODIFICATION', 'CONFIGURATION_CHANGE', 'SECURITY_EVENT'];
    user_ids text[] := ARRAY['user1', 'user2', 'user3', 'admin1', 'admin2'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESDATAAUDIT CASCADE;
    
    -- Generar registros de auditoría para los últimos 30 días
    FOR i IN 1..1000 LOOP
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
            user_ids[1 + mod(i, 5)],
            jsonb_build_object(
                'action', 'Action ' || i,
                'resource', 'Resource ' || mod(i, 10),
                'details', 'Details for action ' || i
            ),
            jsonb_build_object(
                'impact_level', mod(i, 3) + 1,
                'compliance_rules', array['rule1', 'rule2'],
                'risk_assessment', random() * 100
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de cumplimiento normativo
CREATE OR REPLACE FUNCTION generate_compliance_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    compliance_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEMONITORING CASCADE;
    
    -- Generar datos de cumplimiento para los últimos 30 días
    FOR i IN 1..300 LOOP
        model_id := 1 + mod(i, 20);
        compliance_date := random_timestamp(
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
            compliance_date,
            jsonb_build_object(
                'compliance_score', random() * 100,
                'risk_level', CASE 
                    WHEN random() < 0.1 THEN 'HIGH'
                    WHEN random() < 0.3 THEN 'MEDIUM'
                    ELSE 'LOW'
                END,
                'gdpr_compliance', random() > 0.1,
                'hipaa_compliance', random() > 0.1
            ),
            jsonb_build_object(
                'count', floor(random() * 5),
                'details', array[
                    'violation1',
                    'violation2'
                ]
            ),
            jsonb_build_object(
                'actions', array[
                    'action1',
                    'action2'
                ],
                'status', 'IN_PROGRESS'
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba de compliance
CREATE OR REPLACE FUNCTION generate_all_compliance_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_audit_logs();
    PERFORM generate_compliance_data();
END;
$$ LANGUAGE plpgsql;