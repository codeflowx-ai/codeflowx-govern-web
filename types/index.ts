export interface TechStack {
    frontend: 'nextjs' | 'react' | 'angular' | 'vue';
    backend: 'springboot' | 'nodejs' | 'dotnet' | 'python';
    database: 'mysql' | 'postgresql' | 'mongodb' | 'oracle';
    architecture: 'monolith' | 'microservices' | 'serverless';
}

export interface ProjectConfig {
    id: string;
    name: string;
    description: string;
    techStack: TechStack;
    features: string[];
    businessDomain: 'healthcare' | 'finance' | 'retail' | 'manufacturing' | 'custom';
    complexity: 'simple' | 'medium' | 'enterprise';
}

export interface GenerationRequest {
    config: ProjectConfig;
    templates: string[];
    customRequirements?: string;
}

export interface GenerationResponse {
    projectId: string;
    status: 'generating' | 'completed' | 'error';
    files: GeneratedFile[];
    progress: number;
}

export interface GeneratedFile {
    path: string;
    content: string;
    language: string;
    type: 'component' | 'service' | 'model' | 'config' | 'test';
}