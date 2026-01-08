export interface DemoUser {
    id: string
    name: string
    email: string
    password: string
    roles: string[]
    avatar?: string
    department?: string
    lastLogin?: string
}

export const demoUsers: DemoUser[] = [
    {
        id: '1',
        name: 'Admin User',
        email: 'admin@company.com',
        password: 'demo123',
        roles: ['admin'],
        department: 'Administración',
        lastLogin: '2 hours ago'
    },
    {
        id: '2',
        name: 'Developer User',
        email: 'dev@company.com',
        password: 'demo123',
        roles: ['developer'],
        department: 'Desarrollo',
        lastLogin: '1 day ago'
    },
    {
        id: '3',
        name: 'Viewer User',
        email: 'viewer@company.com',
        password: 'demo123',
        roles: ['viewer'],
        department: 'Consultoría',
        lastLogin: '1 week ago'
    },
    {
        id: '4',
        name: 'Business Analytics User',
        email: 'business@company.com',
        password: 'demo123',
        roles: ['business_analytics'],
        department: 'Analytics',
        lastLogin: '3 hours ago'
    },
    {
        id: '5',
        name: 'AI Analytics User',
        email: 'ai@company.com',
        password: 'demo123',
        roles: ['ai_analytics'],
        department: 'AI & ML',
        lastLogin: '5 hours ago'
    },
    {
        id: '6',
        name: 'DevOps User',
        email: 'devops@company.com',
        password: 'demo123',
        roles: ['devops'],
        department: 'DevOps',
        lastLogin: '1 hour ago'
    },
    {
        id: '7',
        name: 'Project Manager User',
        email: 'pm@company.com',
        password: 'demo123',
        roles: ['project_manager'],
        department: 'Gestión de Proyectos',
        lastLogin: '4 hours ago'
    }
]

export const authenticateUser = (email: string, password: string): DemoUser | null => {
    const user = demoUsers.find(u => u.email === email && u.password === password)
    return user || null
}

export const getUserById = (id: string): DemoUser | null => {
    return demoUsers.find(u => u.id === id) || null
}

export const getUserByEmail = (email: string): DemoUser | null => {
    return demoUsers.find(u => u.email === email) || null
} 