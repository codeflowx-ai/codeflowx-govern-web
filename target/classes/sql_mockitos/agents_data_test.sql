-- Función para generar timestamps aleatorios en un rango
CREATE OR REPLACE FUNCTION random_timestamp(start_date timestamp, end_date timestamp) 
RETURNS timestamp AS $$
BEGIN
    RETURN start_date + random() * (end_date - start_date);
END;
$$ LANGUAGE plpgsql;

-- Función para generar datos de prueba de agentes
CREATE OR REPLACE FUNCTION generate_test_agents() 
RETURNS void AS $$
DECLARE
    agent_types text[] := ARRAY['ASSISTANT', 'AUTONOMOUS', 'COLLABORATIVE'];
    i integer;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESAGENT CASCADE;
    
    -- Generar agentes de prueba
    FOR i IN 1..10 LOOP
        INSERT INTO SLESAGENT (
            idxslesagent,
            agentname,
            agenttype,
            description,
            configuration,
            goals,
            constraints,
            active
        ) VALUES (
            i,
            'Agent-' || i,
            agent_types[1 + mod(i, 3)],
            'Description for Agent ' || i,
            jsonb_build_object(
                'max_tokens', 1000 * i,
                'temperature', 0.7,
                'capabilities', array['task_' || i, 'skill_' || (i+1)]
            ),
            jsonb_build_object(
                'primary', 'Goal ' || i,
                'secondary', array['SubGoal1', 'SubGoal2']
            ),
            jsonb_build_object(
                'max_runtime', 60 * i,
                'max_memory', 1024 * i
            ),
            true
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar tareas de agentes
CREATE OR REPLACE FUNCTION generate_agent_tasks() 
RETURNS void AS $$
DECLARE
    task_types text[] := ARRAY['ANALYSIS', 'PROCESSING', 'COORDINATION', 'LEARNING'];
    task_statuses text[] := ARRAY['PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED'];
    i integer;
    agent_id integer;
    task_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESAGENTTASK CASCADE;
    
    -- Generar tareas para las últimas 24 horas
    FOR i IN 1..100 LOOP
        agent_id := 1 + mod(i, 10);
        task_date := random_timestamp(
            current_timestamp - interval '24 hours',
            current_timestamp
        );
        
        INSERT INTO SLESAGENTTASK (
            idxslesagenttask,
            idslesagent,
            tasktype,
            status,
            taskdate,
            taskdata,
            result
        ) VALUES (
            i,
            agent_id,
            task_types[1 + mod(i, 4)],
            task_statuses[1 + mod(i, 4)],
            task_date,
            jsonb_build_object(
                'execution_time', random() * 100,
                'priority', mod(i, 3) + 1,
                'parameters', jsonb_build_object(
                    'param1', 'value1',
                    'param2', 'value2'
                )
            ),
            jsonb_build_object(
                'success', mod(i, 5) != 0,
                'accuracy', random() * 100,
                'response_time', random() * 1000,
                'user_satisfaction', random() * 5
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar interacciones entre agentes
CREATE OR REPLACE FUNCTION generate_agent_interactions() 
RETURNS void AS $$
DECLARE
    interaction_types text[] := ARRAY['REQUEST', 'RESPONSE', 'COLLABORATION', 'DELEGATION'];
    i integer;
    source_agent integer;
    target_agent integer;
    interaction_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESAGENTINTERACTION CASCADE;
    
    -- Generar interacciones
    FOR i IN 1..200 LOOP
        source_agent := 1 + mod(i, 10);
        -- Asegurar que target_agent sea diferente de source_agent
        target_agent := 1 + mod(i + 1, 10);
        
        interaction_date := random_timestamp(
            current_timestamp - interval '24 hours',
            current_timestamp
        );
        
        INSERT INTO SLESAGENTINTERACTION (
            idxslesagentinteraction,
            source_agent,
            target_agent,
            interactiontype,
            interactiondate,
            performance
        ) VALUES (
            i,
            source_agent,
            target_agent,
            interaction_types[1 + mod(i, 4)],
            interaction_date,
            jsonb_build_object(
                'latency', random() * 100,
                'success_rate', random() * 100,
                'data_transferred', random() * 1000
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función para generar métricas de recursos
CREATE OR REPLACE FUNCTION generate_resource_metrics() 
RETURNS void AS $$
DECLARE
    i integer;
    agent_id integer;
    metric_date timestamp;
BEGIN
    -- Limpiar datos existentes
    TRUNCATE TABLE SLESCHATCOGNITIVE CASCADE;
    
    -- Generar métricas de recursos
    FOR i IN 1..240 LOOP -- Una entrada cada 6 minutos para 24 horas
        agent_id := 1 + mod(i, 10);
        metric_date := current_timestamp - interval '24 hours' + (i * interval '6 minutes');
        
        INSERT INTO SLESCHATCOGNITIVE (
            idxsleschatcognitive,
            idslesagent,
            optimizationdate,
            resourceusage,
            cost
        ) VALUES (
            i,
            agent_id,
            metric_date,
            jsonb_build_object(
                'cpu_usage', 20 + random() * 60,
                'memory_usage', 30 + random() * 40,
                'token_count', floor(random() * 1000)
            ),
            random() * 100
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Función principal para generar todos los datos de prueba
CREATE OR REPLACE FUNCTION generate_all_agent_test_data() 
RETURNS void AS $$
BEGIN
    PERFORM generate_test_agents();
    PERFORM generate_agent_tasks();
    PERFORM generate_agent_interactions();
    PERFORM generate_resource_metrics();
END;
$$ LANGUAGE plpgsql;