-- Función para generar datos de prueba del Executive Dashboard

CREATE OR REPLACE FUNCTION generate_executive_test_data() 
RETURNS void AS $$
DECLARE
    model_types text[] := ARRAY['GPT-4', 'LLAMA-2', 'CLAUDE-2', 'MISTRAL', 'FALCON'];
    providers text[] := ARRAY['OpenAI', 'Meta', 'Anthropic', 'Mistral AI', 'TII'];
    i integer;
    j integer;
    base_date timestamp;
    model_id integer;
    current_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESMODELMETRICS CASCADE;
    TRUNCATE TABLE SLESAIRISK CASCADE;
    TRUNCATE TABLE SLESCOMPLIANCEMONITORING CASCADE;
    TRUNCATE TABLE SLESCOSTOPTIMIZATION CASCADE;

    -- Generar datos para los últimos 30 días
    FOR i IN 1..30 LOOP
        base_date := CURRENT_TIMESTAMP - ((30 - i) || ' days')::interval;
        
        -- Generar datos para cada modelo
        FOR j IN 1..5 LOOP
            model_id := j;
            
            -- Métricas de modelo
            FOR k IN 1..24 LOOP -- 24 puntos de datos por día
                current_date := base_date + (k || ' hours')::interval;
                
                INSERT INTO SLESMODELMETRICS (
                    idxslesmodelmetrics,
                    idslesmodel,
                    metricdate,
                    requestcount,
                    errorcount,
                    avgresponsetime,
                    avgtokensused,
                    cost,
                    detailedmetrics
                ) VALUES (
                    (i * 1000) + (j * 100) + k,
                    model_id,
                    current_date,
                    floor(random() * 1000 + 100), -- requests
                    floor(random() * 20),          -- errors
                    random() * 500 + 100,          -- response time (ms)
                    floor(random() * 1000 + 500),  -- tokens
                    (random() * 50)::numeric(10,2),-- cost
                    jsonb_build_object(
                        'cpu_usage', random() * 100,
                        'memory_usage', random() * 100,
                        'cache_hits', floor(random() * 100),
                        'throughput', random() * 1000
                    )
                );
            END LOOP;

            -- Evaluación de riesgos
            INSERT INTO SLESAIRISK (
                idxslesairisk,
                idslesmodel,
                assessmentdate,
                risklevel,
                riskscore,
                mitigationmeasures,
                fundamentalrights
            ) VALUES (
                (i * 100) + j,
                model_id,
                base_date,
                CASE 
                    WHEN random() < 0.1 THEN 'HIGH'
                    WHEN random() < 0.3 THEN 'MEDIUM'
                    ELSE 'LOW'
                END,
                random() * 100,
                jsonb_build_object(
                    'measures', array['Measure 1', 'Measure 2'],
                    'status', 'IN_PROGRESS'
                ),
                jsonb_build_object(
                    'privacy_impact', random() * 100,
                    'fairness_score', random() * 100
                )
            );

            -- Monitorización de compliance
            INSERT INTO SLESCOMPLIANCEMONITORING (
                idxslescompliancemonitoring,
                idslesmodel,
                monitoringdate,
                compliancemetrics,
                violations,
                correctiveactions
            ) VALUES (
                (i * 100) + j,
                model_id,
                base_date,
                jsonb_build_object(
                    'compliance_score', random() * 100,
                    'gdpr_compliance', random() > 0.1,
                    'hipaa_compliance', random() > 0.1
                ),
                jsonb_build_object(
                    'count', floor(random() * 5),
                    'details', array['violation1', 'violation2']
                ),
                jsonb_build_object(
                    'actions', array['action1', 'action2'],
                    'status', 'IN_PROGRESS'
                )
            );

            -- Optimización de costes
            INSERT INTO SLESCOSTOPTIMIZATION (
                idxslescostoptimization,
                idslesmodel,
                optimizationdate,
                costsavings,
                resourceusage,
                optimizationrules,
                performanceimprovements
            ) VALUES (
                (i * 100) + j,
                model_id,
                base_date,
                (random() * 1000)::numeric(10,2),
                jsonb_build_object(
                    'cpu_usage', random() * 100,
                    'memory_usage', random() * 100,
                    'token_count', floor(random() * 10000)
                ),
                jsonb_build_object(
                    'token_optimization', true,
                    'cache_enabled', true,
                    'batch_processing', random() > 0.5
                ),
                jsonb_build_object(
                    'latency_reduction', random() * 50,
                    'throughput_increase', random() * 30
                )
            );
        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar KPIs específicos
CREATE OR REPLACE FUNCTION generate_executive_kpis() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    current_date timestamp;
BEGIN
    -- Limpiar datos existentes de KPIs
    TRUNCATE TABLE SLESEXECUTIVEKPI CASCADE;

    -- Generar KPIs para los últimos 30 días
    FOR i IN 1..30 LOOP
        current_date := CURRENT_TIMESTAMP - ((30 - i) || ' days')::interval;
        
        -- KPIs globales del día
        INSERT INTO SLESEXECUTIVEKPI (
            idxslesexecutivekpi,
            kpidate,
            kpitype,
            kpivalue,
            metadata
        ) VALUES (
            i,
            current_date,
            'DAILY_SUMMARY',
            jsonb_build_object(
                'total_requests', floor(random() * 100000),
                'total_cost', (random() * 5000)::numeric(10,2),
                'avg_response_time', random() * 1000,
                'success_rate', (random() * 10 + 90)::numeric(5,2),
                'active_models', floor(random() * 20 + 10)
            ),
            jsonb_build_object(
                'trends', array[
                    jsonb_build_object(
                        'metric', 'cost_efficiency',
                        'change', random() * 20 - 10
                    ),
                    jsonb_build_object(
                        'metric', 'performance',
                        'change', random() * 15 - 5
                    )
                ],
                'alerts', array[
                    jsonb_build_object(
                        'type', 'COST',
                        'severity', 'HIGH',
                        'message', 'Unusual cost spike detected'
                    ),
                    jsonb_build_object(
                        'type', 'PERFORMANCE',
                        'severity', 'MEDIUM',
                        'message', 'Performance degradation in Model X'
                    )
                ]
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba ejecutivos
CREATE OR REPLACE FUNCTION generate_all_executive_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_executive_test_data();
    PERFORM generate_executive_kpis();
END;
$$ LANGUAGE plpgsql;

-- Para ejecutar la generación de datos:
-- SELECT generate_all_executive_test_data();

-- Generar todos los datos de prueba
SELECT generate_all_executive_test_data();

-- O generar datos específicos
SELECT generate_executive_test_data();
SELECT generate_executive_kpis();
/**
 * Este procedimiento genera:

Métricas de Modelo:

Datos de uso por hora
Estadísticas de rendimiento
Costes asociados


Evaluación de Riesgos:

Niveles de riesgo
Medidas de mitigación
Impacto en derechos fundamentales


Monitorización de Compliance:

Métricas de cumplimiento
Violaciones detectadas
Acciones correctivas


Optimización de Costes:

Ahorros generados
Uso de recursos
Mejoras de rendimiento


KPIs Ejecutivos:

Resúmenes diarios
Tendencias
Alertas importantes
 */*/