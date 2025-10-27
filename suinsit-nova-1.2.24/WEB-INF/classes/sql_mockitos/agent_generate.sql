-- Generar todos los datos de prueba
SELECT generate_all_agent_test_data();

-- O generar datos específicos
SELECT generate_test_agents();
SELECT generate_agent_tasks();
SELECT generate_agent_interactions();
SELECT generate_resource_metrics();

/**
Estos procedimientos generarán:

10 agentes con diferentes tipos y configuraciones
100 tareas distribuidas en las últimas 24 horas
200 interacciones entre agentes
Métricas de recursos cada 6 minutos durante 24 horas

¿Quieres que continúe con los procedimientos para generar datos de prueba para:

Cost Optimization Dashboard
Compliance & Audit Dashboard **/