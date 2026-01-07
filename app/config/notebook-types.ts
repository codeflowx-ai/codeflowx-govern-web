export interface NotebookCell {
    id: string;
    type: 'code' | 'config' | 'tool' | 'test' | 'markdown';
    content: string;
    language: 'python' | 'json' | 'yaml' | 'markdown';
    output?: string;
    status: 'idle' | 'running' | 'success' | 'error';
    metadata?: Record<string, any>;
}

export interface AgentTool {
    name: string;
    description: string;
    parameters: ToolParameter[];
    code: string;
    category: 'api' | 'database' | 'file' | 'custom';
}

export interface ToolParameter {
    name: string;
    type: 'string' | 'number' | 'boolean' | 'array' | 'object';
    required: boolean;
    description: string;
    default?: any;
}

export interface AgentConfig {
    name: string;
    description: string;
    model: string;
    temperature: number;
    max_tokens: number;
    tools: string[];
    system_prompt: string;
}

export interface NotebookSession {
    id: string;
    name: string;
    cells: NotebookCell[];
    agentConfig: AgentConfig;
    tools: AgentTool[];
    createdAt: Date;
    updatedAt: Date;
}

export interface MockExecutionResult {
    success: boolean;
    output: string;
    executionTime: number;
    mockData?: any;
}
