import { NotebookSession, AgentTool, AgentConfig } from './notebook-types-simple';

export const mockTools: AgentTool[] = [
    {
        name: 'web_search',
        description: 'Realiza búsquedas en la web',
        parameters: [
            { name: 'query', type: 'string', required: true, description: 'Término de búsqueda' },
            { name: 'max_results', type: 'number', required: false, description: 'Número máximo de resultados', default: 5 }
        ],
        code: `def web_search(query: str, max_results: int = 5):
    """Simula búsqueda web"""
    return f"Resultados para '{query}': {max_results} enlaces encontrados"`,
        category: 'api'
    },
    {
        name: 'database_query',
        description: 'Ejecuta consultas en base de datos',
        parameters: [
            { name: 'sql', type: 'string', required: true, description: 'Consulta SQL' },
            { name: 'database', type: 'string', required: false, description: 'Nombre de la base de datos', default: 'default' }
        ],
        code: `def database_query(sql: str, database: str = 'default'):
    """Simula consulta a base de datos"""
    return f"Ejecutando '{sql}' en {database}: 42 filas retornadas"`,
        category: 'database'
    },
    {
        name: 'file_processor',
        description: 'Procesa archivos del sistema',
        parameters: [
            { name: 'file_path', type: 'string', required: true, description: 'Ruta del archivo' },
            { name: 'operation', type: 'string', required: false, description: 'Operación a realizar', default: 'read' }
        ],
        code: `def file_processor(file_path: str, operation: str = 'read'):
    """Simula procesamiento de archivos"""
    return f"Operación '{operation}' en '{file_path}': archivo procesado exitosamente"`,
        category: 'file'
    }
];

export const mockAgentConfig: AgentConfig = {
    name: 'Mi Agente IA',
    description: 'Agente inteligente para automatización',
    model: 'gpt-4',
    temperature: 0.7,
    max_tokens: 2000,
    tools: ['web_search', 'database_query'],
    system_prompt: 'Eres un asistente inteligente que ayuda a los usuarios con tareas complejas.'
};

export const mockNotebookSession: NotebookSession = {
    id: '1',
    name: 'Agente de Automatización',
    cells: [
        {
            id: '1',
            type: 'config',
            content: JSON.stringify(mockAgentConfig, null, 2),
            language: 'json',
            status: 'success',
            output: 'Configuración del agente cargada exitosamente'
        },
        {
            id: '2',
            type: 'tool',
            content: mockTools[0].code,
            language: 'python',
            status: 'success',
            output: 'Herramienta web_search creada'
        },
        {
            id: '3',
            type: 'code',
            content: `# Crear instancia del agente
from agent_framework import Agent

agent = Agent(
    name="${mockAgentConfig.name}",
    model="${mockAgentConfig.model}",
    temperature=${mockAgentConfig.temperature}
)

# Agregar herramientas
agent.add_tool(web_search)
agent.add_tool(database_query)

print("Agente configurado exitosamente!")`,
            language: 'python',
            status: 'idle'
        },
        {
            id: '4',
            type: 'test',
            content: `# Probar el agente
response = agent.run("Busca información sobre inteligencia artificial y guarda los resultados")

print("Respuesta del agente:")
print(response)`,
            language: 'python',
            status: 'idle'
        }
    ],
    agentConfig: mockAgentConfig,
    tools: mockTools,
    createdAt: new Date(),
    updatedAt: new Date()
};

export const mockExecutionResults = {
    'web_search': {
        success: true,
        output: "Resultados para 'inteligencia artificial': 5 enlaces encontrados\n- OpenAI: https://openai.com\n- Google AI: https://ai.google\n- Microsoft AI: https://microsoft.com/ai\n- DeepMind: https://deepmind.com\n- Stanford AI: https://ai.stanford.edu",
        executionTime: 1.2,
        mockData: { query: 'inteligencia artificial', max_results: 5 }
    },
    'database_query': {
        success: true,
        output: "Ejecutando 'SELECT * FROM ai_models' en default: 42 filas retornadas\n\nModelos disponibles:\n- GPT-4\n- Claude-3\n- Llama-2\n- Gemini Pro",
        executionTime: 0.8,
        mockData: { sql: 'SELECT * FROM ai_models', database: 'default' }
    },
    'file_processor': {
        success: true,
        output: "Operación 'read' en '/data/ai_research.pdf': archivo procesado exitosamente\n\nContenido extraído: Documento sobre tendencias en IA para 2024",
        executionTime: 2.1,
        mockData: { file_path: '/data/ai_research.pdf', operation: 'read' }
    }
};
