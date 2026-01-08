-- Función para generar datos de prueba del Control Center

-- Función auxiliar para generar timestamps aleatorios
CREATE OR REPLACE FUNCTION random_timestamp_between(start_date timestamp, end_date timestamp) 
RETURNS timestamp AS $$
BEGIN
    RETURN start_date + random() * (end_date - start_date);
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de monitorización en tiempo real
CREATE OR REPLACE FUNCTION generate_control_center_monitoring() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    monitor_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMODELMETRICS CASCADE;
    
    -- Generar datos de monitorización para las últimas 24 horas
    FOR i IN 1..1440 LOOP -- Un registro por minuto
        FOR model_id IN 1..10 LOOP -- Para cada modelo
            monitor_date := current_timestamp - interval '24 hours' + (i * interval '1 minute');
            
            INSERT INTO SLESMODELMETRICS (
                idxslesmodelmetrics,
                idslesmodel,
                metricdate,
                requestcount,
                errorcount,
                avgresponsetime,
                avgtokensused,
                cost,
                performancemetrics
            ) VALUES (
                ((i-1) * 10) + model_id,
                model_id,
                monitor_date,
                floor(random() * 100), -- requests por minuto
                floor(random() * 5),   -- errores
                random() * 1000,       -- tiempo de respuesta en ms
                floor(random() * 1000), -- tokens usados
                (random() * 10)::numeric(10,4), -- costo
                jsonb_build_object(
                    'cpu_usage', random() * 100,
                    'memory_usage', random() * 100,
                    'latency_p95', random() * 2000,
                    'success_rate', 90 + (random() * 10)
                )
            );
        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar incidentes
CREATE OR REPLACE FUNCTION generate_control_center_incidents() 
RETURNS void AS $$
DECLARE
    i integer;
    incident_types text[] := ARRAY['PERFORMANCE', 'SECURITY', 'COMPLIANCE', 'AVAILABILITY'];
    severity_levels text[] := ARRAY['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOMPLIANCEINCIDENT CASCADE;
    
    -- Generar incidentes para las últimas 24 horas
    FOR i IN 1..50 LOOP -- 50 incidentes de ejemplo
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
            floor(random() * 10) + 1, -- modelo aleatorio
            random_timestamp_between(current_timestamp - interval '24 hours', current_timestamp),
            severity_levels[1 + floor(random() * 4)],
            'Incident ' || i || ' of type ' || incident_types[1 + floor(random() * 4)],
            jsonb_build_object(
                'count', floor(random() * 1000),
                'affected_regions', array['EU', 'US', 'ASIA']
            ),
            jsonb_build_object(
                'actions_taken', array['action1', 'action2'],
                'resolution_time', floor(random() * 120)
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de auditoría
CREATE OR REPLACE FUNCTION generate_control_center_audit() 
RETURNS void AS $$
DECLARE
    i integer;
    audit_types text[] := ARRAY['MODEL_ACCESS', 'CONFIG_CHANGE', 'SECURITY_EVENT', 'COMPLIANCE_CHECK'];
    users text[] := ARRAY['admin', 'operator', 'auditor', 'system'];
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESDATAAUDIT CASCADE;
    
    -- Generar registros de auditoría para las últimas 24 horas
    FOR i IN 1..200 LOOP -- 200 registros de auditoría
        INSERT INTO SLESDATAAUDIT (
            idxslesdataaudit,
            auditdate,
            actiontype,
            userid,
            actiondetails,
            complianceimpact
        ) VALUES (
            i,
            random_timestamp_between(current_timestamp - interval '24 hours', current_timestamp),
            audit_types[1 + floor(random() * 4)],
            users[1 + floor(random() * 4)],
            jsonb_build_object(
                'action', 'Action ' || i,
                'resource', 'Resource ' || (floor(random() * 10) + 1),
                'details', 'Details for audit record ' || i
            ),
            jsonb_build_object(
                'impact_level', floor(random() * 3) + 1,
                'compliance_rules', array['rule1', 'rule2'],
                'risk_score', random() * 100
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de SLA
CREATE OR REPLACE FUNCTION generate_control_center_sla() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESSLAMONITORING CASCADE;
    
    -- Generar datos de SLA para las últimas 24 horas
    FOR i IN 1..288 LOOP -- Un registro cada 5 minutos
        FOR model_id IN 1..10 LOOP
            INSERT INTO SLESSLAMONITORING (
                idxslesslamonitoring,
                idslesmodel,
                monitoringdate,
                availability,
                responsetime,
                errorrate,
                metricsdata,
                alerttriggered
            ) VALUES (
                ((i-1) * 10) + model_id,
                model_id,
                current_timestamp - interval '24 hours' + (i * interval '5 minutes'),
                98 + (random() * 2), -- availability percentage
                50 + (random() * 200), -- response time in ms
                random() * 2, -- error rate percentage
                jsonb_build_object(
                    'throughput', floor(random() * 1000),
                    'concurrent_requests', floor(random() * 100),
                    'resource_utilization', random() * 100
                ),
                random() < 0.1 -- 10% chance of alert
            );
        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba del Control Center
CREATE OR REPLACE FUNCTION generate_all_control_center_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_control_center_monitoring();
    PERFORM generate_control_center_incidents();
    PERFORM generate_control_center_audit();
    PERFORM generate_control_center_sla();
END;
$$ LANGUAGE plpgsql;

-- Función para limpiar todos los datos de prueba
CREATE OR REPLACE FUNCTION clean_all_control_center_data() 
RETURNS void AS $$
BEGIN
    TRUNCATE TABLE SLESMODELMETRICS CASCADE;
    TRUNCATE TABLE SLESCOMPLIANCEINCIDENT CASCADE;
    TRUNCATE TABLE SLESDATAAUDIT CASCADE;
    TRUNCATE TABLE SLESSLAMONITORING CASCADE;
END;
$$ LANGUAGE plpgsql;

-- Generar todos los datos de prueba
SELECT generate_all_control_center_data();

-- O generar datos específicos
SELECT generate_control_center_monitoring();
SELECT generate_control_center_incidents();
SELECT generate_control_center_audit();
SELECT generate_control_center_sla();

-- Para limpiar todos los datos
SELECT clean_all_control_center_data();

/**
Este script generará:

Métricas de monitorización por minuto para las últimas 24 horas
50 incidentes aleatorios
200 registros de auditoría
Datos de SLA cada 5 minutos para las últimas 24 horas

Los datos incluyen:

Métricas de rendimiento
Estadísticas de uso
Incidentes y alertas
Registros de auditoría
Monitorización de SLA
Datos de cumplimiento */