// types/auth.ts
export interface User {
    id: string
    email: string
    name: string
    role: UserRole
    tenantId: string
    permissions: Permission[]
    isActive: boolean
}

export interface UserRole {
    id: string
    name: 'super_admin' | 'tenant_admin' | 'project_manager' | 'developer' | 'viewer'
    permissions: Permission[]
}

export interface Permission {
    id: string
    resource: 'users' | 'projects' | 'models' | 'git_config' | 'billing' | 'analytics'
    actions: ('create' | 'read' | 'update' | 'delete' | 'manage')[]
}

// Configuraciones asociadas al tenant (definiciones mínimas para tipado)
export interface DatabaseConfig {
    type?: 'mysql' | 'postgresql' | 'mongodb' | 'oracle' | string
    host?: string
    port?: number
    database?: string
    username?: string
    password?: string
    schema?: string
    ssl?: boolean
}

export interface GitConfig {
    provider?: 'github' | 'gitlab' | 'bitbucket' | string
    baseUrl?: string
    organization?: string
    repository?: string
    token?: string
    username?: string
    email?: string
}

export interface ModelConfig {
    defaultModelId?: string
    allowedModels?: string[]
    temperatureDefault?: number
    maxTokensDefault?: number
    ragEnabled?: boolean
}

export interface Tenant {
    id: string
    name: string
    domain: string
    dbConfig: DatabaseConfig
    gitConfig: GitConfig
    modelConfig: ModelConfig
    isActive: boolean
}
