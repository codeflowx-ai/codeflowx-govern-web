-- Funciones para generar datos de prueba de Risk Management

-- Función para generar evaluaciones de riesgo de IA
CREATE OR REPLACE FUNCTION generate_ai_risk_data() 
RETURNS void AS $$
DECLARE
    risk_levels text[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
    i integer;
    model_id integer;
    assessment_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESAIRISK CASCADE;
    
    -- Generar datos de riesgo para los últimos 30 días
    FOR i IN 1..200 LOOP
        model_id := 1 + mod(i, 20); -- Asumiendo 20 modelos
        assessment_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESAIRISK (
            idxslesairisk,
            idslesmodel,
            assessmentdate,
            risklevel,
            riskscore,
            autonomylevel,
            selflearning,
            mitigationmeasures,
            fundamentalrights
        ) VALUES (
            i,
            model_id,
            assessment_date,
            risk_levels[1 + mod(i, 4)],
            random() * 100,
            random() * 100,
            random() > 0.5,
            jsonb_build_object(
                'measures', array[
                    'Human oversight implementation',
                    'Regular model validation',
                    'Bias detection systems'
                ],
                'status', 'IN_PROGRESS',
                'effectiveness', random() * 100
            ),
            jsonb_build_object(
                'privacy_impact', random() * 100,
                'fairness_score', random() * 100,
                'transparency_level', random() * 100
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar evaluaciones de impacto
CREATE OR REPLACE FUNCTION generate_impact_assessment_data() 
RETURNS void AS $$
DECLARE
    i integer;
    risk_id integer;
    assessment_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESIMPACTASSESSMENT CASCADE;
    
    -- Generar evaluaciones de impacto
    FOR i IN 1..150 LOOP
        risk_id := 1 + mod(i, 200); -- Relacionado con los registros de riesgo
        assessment_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESIMPACTASSESSMENT (
            idxslesimpactassessment,
            idslesairisk,
            assessmentdate,
            fundamentalrights,
            discriminationrisks,
            environmentalimpact,
            recommendations
        ) VALUES (
            i,
            risk_id,
            assessment_date,
            jsonb_build_object(
                'privacy_score', random() * 100,
                'autonomy_impact', random() * 100,
                'dignity_preservation', random() * 100
            ),
            jsonb_build_object(
                'bias_detection', array[
                    'Gender bias detected',
                    'Age discrimination risk',
                    'Ethnic bias potential'
                ],
                'severity_score', random() * 100
            ),
            jsonb_build_object(
                'energy_consumption', random() * 1000,
                'carbon_footprint', random() * 500,
                'resource_usage', random() * 100
            ),
            jsonb_build_object(
                'actions', array[
                    'Implement bias mitigation',
                    'Enhance monitoring systems',
                    'Regular audit schedule'
                ],
                'priority_level', mod(i, 3) + 1
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar monitorización de compliance
CREATE OR REPLACE FUNCTION generate_compliance_monitoring_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    monitoring_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEMONITORING CASCADE;
    
    -- Generar datos de monitorización
    FOR i IN 1..300 LOOP
        model_id := 1 + mod(i, 20);
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
                'compliance_score', random() * 100,
                'risk_level', CASE 
                    WHEN random() < 0.1 THEN 'HIGH'
                    WHEN random() < 0.3 THEN 'MEDIUM'
                    ELSE 'LOW'
                END,
                'control_effectiveness', random() * 100
            ),
            jsonb_build_object(
                'count', floor(random() * 5),
                'details', array[
                    'Data retention violation',
                    'Access control breach',
                    'Documentation incomplete'
                ]
            ),
            jsonb_build_object(
                'actions', array[
                    'Update access controls',
                    'Implement monitoring',
                    'Enhance documentation'
                ],
                'status', 'IN_PROGRESS',
                'deadline', current_date + (random() * 30)::integer
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar incidentes de compliance
CREATE OR REPLACE FUNCTION generate_compliance_incident_data() 
RETURNS void AS $$
DECLARE
    severity_levels text[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
    i integer;
    model_id integer;
    incident_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEINCIDENT CASCADE;
    
    -- Generar incidentes
    FOR i IN 1..100 LOOP
        model_id := 1 + mod(i, 20);
        incident_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESCOMPLIANCEINCIDENT (
            idxslescomplianceincident,
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
            'Compliance incident #' || i || ' description',
            jsonb_build_object(
                'count', floor(random() * 1000),
                'regions', array['EU', 'NA', 'APAC'],
                'impact_level', mod(i, 3) + 1
            ),
            jsonb_build_object(
                'immediate_actions', array[
                    'System lockdown',
                    'User notification',
                    'Data audit'
                ],
                'long_term_actions', array[
                    'Policy update',
                    'Training program',
                    'System enhancement'
                ],
                'status', 'IN_PROGRESS'
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de auto-control
CREATE OR REPLACE FUNCTION generate_auto_control_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    control_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESAUTOCONTROL CASCADE;
    
    -- Generar datos de control automático
    FOR i IN 1..200 LOOP
        model_id := 1 + mod(i, 20);
        control_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESAUTOCONTROL (
            idxslesautocontrol,
            idslesmodel,
            controldate,
            controltype,
            controlrules,
            automaticactions,
            compliancecheck
        ) VALUES (
            i,
            model_id,
            control_date,
            CASE mod(i, 3)
                WHEN 0 THEN 'BIAS_CHECK'
                WHEN 1 THEN 'PERFORMANCE_CHECK'
                ELSE 'COMPLIANCE_CHECK'
            END,
            jsonb_build_object(
                'rules', array[
                    'Bias threshold check',
                    'Performance degradation detection',
                    'Compliance violation check'
                ],
                'thresholds', jsonb_build_object(
                    'bias_threshold', 0.1,
                    'performance_threshold', 0.8,
                    'compliance_threshold', 0.9
                )
            ),
            jsonb_build_object(
                'actions', array[
                    'Model retraining trigger',
                    'Alert generation',
                    'Automatic documentation'
                ],
                'execution_status', 'COMPLETED'
            ),
            jsonb_build_object(
                'compliance_score', random() * 100,
                'violations_detected', floor(random() * 3),
                'remediation_status', 'IN_PROGRESS'
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba de risk management
CREATE OR REPLACE FUNCTION generate_all_risk_management_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_ai_risk_data();
    PERFORM generate_impact_assessment_data();
    PERFORM generate_compliance_monitoring_data();
    PERFORM generate_compliance_incident_data();
    PERFORM generate_auto_control_data();
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar la generación completa de datos:
-- SELECT generate_all_risk_management_data();
/**
 * Este script:

Genera datos de evaluación de riesgos de IA
Crea evaluaciones de impacto
Genera datos de monitorización de compliance
Crea incidentes de compliance
Genera datos de auto-control
 * */
-- Generar todos los datos de prueba
SELECT generate_all_risk_management_data();

-- O generar datos específicos
SELECT generate_ai_risk_data();
SELECT generate_impact_assessment_data();
SELECT generate_compliance_monitoring_data();
SELECT generate_compliance_incident_data();
SELECT generate_auto_control_data();

/**
Los datos generados incluyen:

Niveles de riesgo variados
Métricas de cumplimiento
Incidentes y violaciones
Acciones de mitigación
Evaluaciones de impacto
Controles automáticos */