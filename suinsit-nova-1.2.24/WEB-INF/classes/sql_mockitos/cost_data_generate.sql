-- Función principal para generar datos de prueba de costes
CREATE OR REPLACE FUNCTION generate_cost_optimization_test_data() 
RETURNS void AS $$
DECLARE
    model_ids integer[];
    model_names text[];
    i integer;
    current_date timestamp;
    base_cost decimal;
    token_cost decimal;
BEGIN
    -- Configuración inicial
    model_ids := ARRAY[1, 2, 3, 4, 5];
    model_names := ARRAY['GPT-4', 'GPT-3.5', 'CLAUDE-2', 'LLAMA-2', 'MISTRAL-7B'];
    current_date := NOW();
    
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESTOKENUSAGE CASCADE;
    TRUNCATE TABLE SLESCOSTOPTIMIZATION CASCADE;
    TRUNCATE TABLE SLESCOSTPREDICTION CASCADE;
    TRUNCATE TABLE SLESRESOURCEOPTIMIZATION CASCADE;

    -- 1. Generar datos de uso de tokens
    FOR i IN 1..720 LOOP -- Datos cada hora para 30 días
        FOR j IN 1..array_length(model_ids, 1) LOOP
            base_cost := (random() * 50 + 10)::decimal(10,4);
            token_cost := (random() * 0.0001 + 0.00001)::decimal(10,6);
            
            INSERT INTO SLESTOKENUSAGE (
                idxslestokenusage,
                idslesmodel,
                usagedate,
                inputtokens,
                outputtokens,
                totalcost,
                usagemetrics
            ) VALUES (
                (i-1) * array_length(model_ids, 1) + j,
                model_ids[j],
                current_date - interval '30 days' + (i * interval '1 hour'),
                floor(random() * 100000 + 5000), -- tokens de entrada
                floor(random() * 50000 + 2000),  -- tokens de salida
                base_cost,
                jsonb_build_object(
                    'prompt_efficiency', (random() * 30 + 70), -- 70-100%
                    'completion_ratio', (random() * 40 + 60),  -- 60-100%
                    'cost_per_token', token_cost,
                    'optimization_score', (random() * 40 + 60)  -- 60-100%
                )
            );
        END LOOP;
    END LOOP;

    -- 2. Generar datos de optimización de costes
    FOR i IN 1..90 LOOP -- 3 registros por día durante 30 días
        FOR j IN 1..array_length(model_ids, 1) LOOP
            INSERT INTO SLESCOSTOPTIMIZATION (
                idxslescostoptimization,
                idslesmodel,
                optimizationdate,
                costsavings,
                resourceusage,
                optimizationrules,
                performanceimprovements,
                recommendations
            ) VALUES (
                (i-1) * array_length(model_ids, 1) + j,
                model_ids[j],
                current_date - interval '30 days' + (i * interval '8 hours'),
                (random() * 200 + 50)::decimal(10,2), -- ahorros entre 50 y 250
                jsonb_build_object(
                    'cpu_usage', random() * 100,
                    'memory_usage', random() * 100,
                    'token_count', floor(random() * 10000),
                    'api_calls', floor(random() * 1000)
                ),
                jsonb_build_object(
                    'token_optimization', true,
                    'cache_enabled', true,
                    'batch_processing', random() > 0.5,
                    'prompt_compression', random() > 0.3
                ),
                jsonb_build_object(
                    'latency_reduction', random() * 50,
                    'throughput_increase', random() * 30,
                    'error_rate_reduction', random() * 20
                ),
                jsonb_build_object(
                    'suggestions', array[
                        'Optimize prompt templates',
                        'Implement response caching',
                        'Adjust batch sizes',
                        'Use token compression'
                    ],
                    'priority', (random() * 2 + 1)::int
                )
            );
        END LOOP;
    END LOOP;

    -- 3. Generar predicciones de costes
    FOR i IN 1..30 LOOP -- Predicciones para los próximos 30 días
        FOR j IN 1..array_length(model_ids, 1) LOOP
            base_cost := (random() * 500 + 200)::decimal(10,2);
            
            INSERT INTO SLESCOSTPREDICTION (
                idxslescostprediction,
                idslesmodel,
                predictiondate,
                predictedcost,
                confidenceinterval,
                factors
            ) VALUES (
                (i-1) * array_length(model_ids, 1) + j,
                model_ids[j],
                current_date + (i * interval '1 day'),
                base_cost,
                jsonb_build_object(
                    'lower_bound', base_cost * 0.8,
                    'upper_bound', base_cost * 1.2
                ),
                jsonb_build_object(
                    'historical_trend', random() * 100,
                    'seasonality', random() * 100,
                    'usage_pattern', random() * 100,
                    'growth_factor', random() * 100
                )
            );
        END LOOP;
    END LOOP;

    -- 4. Generar datos de optimización de recursos
    FOR i IN 1..90 LOOP -- 3 registros por día durante 30 días
        FOR j IN 1..array_length(model_ids, 1) LOOP
            INSERT INTO SLESRESOURCEOPTIMIZATION (
                idxslesresourceoptimization,
                idslesmodel,
                optimizationdate,
                resourcetype,
                optimizationconfig,
                savings,
                performance
            ) VALUES (
                (i-1) * array_length(model_ids, 1) + j,
                model_ids[j],
                current_date - interval '30 days' + (i * interval '8 hours'),
                CASE mod(j, 3)
                    WHEN 0 THEN 'CPU'
                    WHEN 1 THEN 'MEMORY'
                    ELSE 'TOKENS'
                END,
                jsonb_build_object(
                    'threshold', random() * 100,
                    'auto_scale', true,
                    'optimization_rules', array[
                        'rule1',
                        'rule2',
                        'rule3'
                    ]
                ),
                (random() * 300 + 100)::decimal(10,2),
                jsonb_build_object(
                    'efficiency_gain', random() * 100,
                    'response_time_impact', random() * 100,
                    'quality_impact', random() * 100
                )
            );
        END LOOP;
    END LOOP;

    -- Registrar la generación de datos
    RAISE NOTICE 'Generated test data for cost optimization dashboard';
    RAISE NOTICE 'Token usage records: %', (SELECT count(*) FROM SLESTOKENUSAGE);
    RAISE NOTICE 'Cost optimization records: %', (SELECT count(*) FROM SLESCOSTOPTIMIZATION);
    RAISE NOTICE 'Cost prediction records: %', (SELECT count(*) FROM SLESCOSTPREDICTION);
    RAISE NOTICE 'Resource optimization records: %', (SELECT count(*) FROM SLESRESOURCEOPTIMIZATION);
END;
$$ LANGUAGE plpgsql;

-- Función para limpiar todos los datos de prueba
CREATE OR REPLACE FUNCTION clean_cost_optimization_test_data() 
RETURNS void AS $$
BEGIN
    TRUNCATE TABLE SLESTOKENUSAGE CASCADE;
    TRUNCATE TABLE SLESCOSTOPTIMIZATION CASCADE;
    TRUNCATE TABLE SLESCOSTPREDICTION CASCADE;
    TRUNCATE TABLE SLESRESOURCEOPTIMIZATION CASCADE;
    RAISE NOTICE 'Cleaned all cost optimization test data';
END;
$$ LANGUAGE plpgsql;


-- Generar datos de prueba
SELECT generate_cost_optimization_test_data();

-- Limpiar datos de prueba si es necesario
SELECT clean_cost_optimization_test_data();