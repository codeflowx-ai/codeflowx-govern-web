-- Funciones para Cost Optimization Dashboard

-- Función para generar datos de costes por modelo
CREATE OR REPLACE FUNCTION generate_model_cost_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    cost_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOSTOPTIMIZATION CASCADE;
    
    -- Generar datos de costes para los últimos 30 días
    FOR i IN 1..300 LOOP
        model_id := 1 + mod(i, 20); -- Asumiendo 20 modelos
        cost_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
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
            i,
            model_id,
            cost_date,
            random() * 1000, -- Ahorro en costes
            jsonb_build_object(
                'cpu_usage', random() * 100,
                'memory_usage', random() * 100,
                'token_count', floor(random() * 10000),
                'api_calls', floor(random() * 1000)
            ),
            jsonb_build_object(
                'token_optimization', true,
                'cache_enabled', true,
                'batch_processing', mod(i, 2) = 0
            ),
            jsonb_build_object(
                'latency_reduction', random() * 50,
                'throughput_increase', random() * 30,
                'error_rate_reduction', random() * 20
            ),
            jsonb_build_object(
                'suggestions', array[
                    'Optimize prompt length',
                    'Enable caching',
                    'Adjust batch size'
                ],
                'priority', mod(i, 3) + 1
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de uso de tokens
CREATE OR REPLACE FUNCTION generate_token_usage_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    usage_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESTOKENUSAGE CASCADE;
    
    -- Generar datos de uso de tokens para los últimos 30 días
    FOR i IN 1..600 LOOP -- Datos cada hora
        model_id := 1 + mod(i, 20);
        usage_date := current_timestamp - interval '30 days' + (i * interval '1 hour');
        
        INSERT INTO SLESTOKENUSAGE (
            idxslestokenusage,
            idslesmodel,
            usagedate,
            inputtokens,
            outputtokens,
            totalcost,
            usagemetrics
        ) VALUES (
            i,
            model_id,
            usage_date,
            floor(random() * 5000), -- tokens de entrada
            floor(random() * 3000), -- tokens de salida
            (random() * 50)::numeric(10,4), -- coste total
            jsonb_build_object(
                'prompt_efficiency', random() * 100,
                'completion_ratio', random() * 100,
                'cost_per_token', (random() * 0.001)::numeric(10,6),
                'optimization_score', random() * 100
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar predicciones de costes
CREATE OR REPLACE FUNCTION generate_cost_predictions() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    prediction_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCOSTPREDICTION CASCADE;
    
    -- Generar predicciones para los próximos 30 días
    FOR i IN 1..30 LOOP
        prediction_date := current_timestamp + (i * interval '1 day');
        
        FOR model_id IN 1..20 LOOP
            INSERT INTO SLESCOSTPREDICTION (
                idxslescostprediction,
                idslesmodel,
                predictiondate,
                predictedcost,
                confidenceinterval,
                factors
            ) VALUES (
                ((i-1) * 20) + model_id,
                model_id,
                prediction_date,
                (random() * 1000)::numeric(10,2),
                jsonb_build_object(
                    'lower_bound', (random() * 800)::numeric(10,2),
                    'upper_bound', (random() * 1200)::numeric(10,2)
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
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de optimización de recursos
CREATE OR REPLACE FUNCTION generate_resource_optimization_data() 
RETURNS void AS $$
DECLARE
    i integer;
    model_id integer;
    opt_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESRESOURCEOPTIMIZATION CASCADE;
    
    -- Generar datos de optimización para los últimos 30 días
    FOR i IN 1..300 LOOP
        model_id := 1 + mod(i, 20);
        opt_date := random_timestamp(
            current_timestamp - interval '30 days',
            current_timestamp
        );
        
        INSERT INTO SLESRESOURCEOPTIMIZATION (
            idxslesresourceoptimization,
            idslesmodel,
            optimizationdate,
            resourcetype,
            optimizationconfig,
            savings,
            performance
        ) VALUES (
            i,
            model_id,
            opt_date,
            CASE mod(i, 3)
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
            (random() * 500)::numeric(10,2),
            jsonb_build_object(
                'efficiency_gain', random() * 100,
                'response_time_impact', random() * 100,
                'quality_impact', random() * 100
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba de costes
CREATE OR REPLACE FUNCTION generate_all_cost_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_model_cost_data();
    PERFORM generate_token_usage_data();
    PERFORM generate_cost_predictions();
    PERFORM generate_resource_optimization_data();
END;
$$ LANGUAGE plpgsql;