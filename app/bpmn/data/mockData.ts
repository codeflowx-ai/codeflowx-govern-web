// app/bpmn/data/mockData.ts - Mock data para BPMN Task Inbox
import { BPMNTask, BPMNUser, TaskStatistics } from '@/types/bpmn'

// Helper para simular delay de API
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms))

// Mock de usuarios disponibles
export const mockUsers: BPMNUser[] = [
  {
    username: 'admin',
    fullname: 'Admin User',
    email: 'admin@company.com',
    id: '1'
  },
  {
    username: 'dev',
    fullname: 'Developer User',
    email: 'dev@company.com',
    id: '2'
  },
  {
    username: 'pm',
    fullname: 'Project Manager User',
    email: 'pm@company.com',
    id: '3'
  },
  {
    username: 'analyst',
    fullname: 'Business Analyst',
    email: 'analyst@company.com',
    id: '4'
  },
  {
    username: 'reviewer',
    fullname: 'Reviewer User',
    email: 'reviewer@company.com',
    id: '5'
  }
]

// Mock de tareas BPMN
export const mockTasks: BPMNTask[] = [
  {
    id: 'task_001',
    name: 'Revisar Documentación de Incidente',
    description: 'Revisar y validar la documentación del incidente reportado',
    assignee: 'pm',
    owner: 'pm',
    processInstanceId: 'proc_001',
    processDefinitionId: 'incident-management:1:abc123',
    processDefinitionKey: 'incident-management',
    taskDefinitionKey: 'review-incident-documentation',
    priority: 75,
    dueDate: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000).toISOString(), // 2 días
    createTime: new Date(Date.now() - 5 * 60 * 60 * 1000).toISOString(), // 5 horas atrás
    candidateGroups: ['managers', 'reviewers'],
    formKey: 'review-incident-documentation-form'
  },
  {
    id: 'task_002',
    name: 'Aprobar Acción Correctiva',
    description: 'Revisar y aprobar la acción correctiva propuesta',
    assignee: undefined,
    owner: undefined,
    processInstanceId: 'proc_002',
    processDefinitionId: 'corrective-action:1:def456',
    processDefinitionKey: 'corrective-action',
    taskDefinitionKey: 'approve-corrective-action',
    priority: 100,
    dueDate: new Date(Date.now() + 1 * 24 * 60 * 60 * 1000).toISOString(), // 1 día
    createTime: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(), // 2 horas atrás
    candidateGroups: ['managers', 'approvers'],
    formKey: 'approve-corrective-action-form'
  },
  {
    id: 'task_003',
    name: 'Implementar Mejora de Competencia',
    description: 'Implementar las mejoras identificadas en el análisis de brechas',
    assignee: 'dev',
    owner: 'dev',
    processInstanceId: 'proc_003',
    processDefinitionId: 'competence-gap:1:ghi789',
    processDefinitionKey: 'competence-gap',
    taskDefinitionKey: 'implement-competence-improvement',
    priority: 50,
    dueDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString(), // 7 días
    createTime: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(), // 1 día atrás
    candidateGroups: ['developers', 'implementers'],
    formKey: 'implement-competence-improvement-form'
  },
  {
    id: 'task_004',
    name: 'Evaluar Modelo de IA',
    description: 'Realizar evaluación completa del modelo de IA propuesto',
    assignee: undefined,
    owner: undefined,
    processInstanceId: 'proc_004',
    processDefinitionId: 'ai-governance:1:jkl012',
    processDefinitionKey: 'ai-governance',
    taskDefinitionKey: 'evaluate-ai-model',
    priority: 100,
    dueDate: new Date(Date.now() - 1 * 24 * 60 * 60 * 1000).toISOString(), // Vencida hace 1 día
    createTime: new Date(Date.now() - 3 * 24 * 60 * 60 * 1000).toISOString(), // 3 días atrás
    candidateGroups: ['ai_analytics', 'reviewers'],
    formKey: 'evaluate-ai-model-form'
  },
  {
    id: 'task_005',
    name: 'Revisar Solicitud de Descomisión de IA',
    description: 'Revisar la solicitud de descomisión del sistema de IA',
    assignee: 'analyst',
    owner: 'analyst',
    processInstanceId: 'proc_005',
    processDefinitionId: 'ai-decommission:1:mno345',
    processDefinitionKey: 'ai-decommission',
    taskDefinitionKey: 'review-decommission-request',
    priority: 75,
    dueDate: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000).toISOString(), // 3 días
    createTime: new Date(Date.now() - 4 * 60 * 60 * 1000).toISOString(), // 4 horas atrás
    candidateGroups: ['analysts', 'reviewers'],
    formKey: 'review-decommission-request-form'
  },
  {
    id: 'task_006',
    name: 'Completar Auditoría Interna',
    description: 'Completar el proceso de auditoría interna programada',
    assignee: undefined,
    owner: undefined,
    processInstanceId: 'proc_006',
    processDefinitionId: 'internal-audit:1:pqr678',
    processDefinitionKey: 'internal-audit',
    taskDefinitionKey: 'complete-internal-audit',
    priority: 50,
    dueDate: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toISOString(), // 5 días
    createTime: new Date(Date.now() - 6 * 60 * 60 * 1000).toISOString(), // 6 horas atrás
    candidateGroups: ['auditors', 'reviewers'],
    formKey: 'complete-internal-audit-form'
  },
  {
    id: 'task_007',
    name: 'Revisar Reunión de Revisión de Gestión',
    description: 'Revisar y preparar la reunión de revisión de gestión',
    assignee: 'pm',
    owner: 'pm',
    processInstanceId: 'proc_007',
    processDefinitionId: 'management-review:1:stu901',
    processDefinitionKey: 'management-review',
    taskDefinitionKey: 'review-management-meeting',
    priority: 50,
    dueDate: new Date(Date.now() + 1 * 24 * 60 * 60 * 1000).toISOString(), // 1 día
    createTime: new Date(Date.now() - 3 * 60 * 60 * 1000).toISOString(), // 3 horas atrás
    candidateGroups: ['managers'],
    formKey: 'review-management-meeting-form'
  },
  {
    id: 'task_008',
    name: 'Aprobar Registro UE',
    description: 'Aprobar el paquete de registro para la Unión Europea',
    assignee: undefined,
    owner: undefined,
    processInstanceId: 'proc_008',
    processDefinitionId: 'eu-registration:1:vwx234',
    processDefinitionKey: 'eu-registration',
    taskDefinitionKey: 'approve-eu-registration',
    priority: 100,
    dueDate: new Date(Date.now() + 2 * 24 * 60 * 60 * 1000).toISOString(), // 2 días
    createTime: new Date(Date.now() - 1 * 60 * 60 * 1000).toISOString(), // 1 hora atrás
    candidateGroups: ['approvers', 'compliance'],
    formKey: 'approve-eu-registration-form'
  }
]

// Función para obtener tareas mock con estadísticas
export const getMockTasks = async (currentUser: string = 'pm'): Promise<{
  tasks: BPMNTask[]
  statistics: TaskStatistics
  processDefinitions: string[]
}> => {
  await delay(500) // Simular delay de red

  const tasks = [...mockTasks]

  const statistics: TaskStatistics = {
    totalTasks: tasks.length,
    assignedToMeCount: tasks.filter(t => t.assignee === currentUser).length,
    groupTasksCount: tasks.filter(t =>
      !t.assignee && t.candidateGroups && t.candidateGroups.length > 0
    ).length
  }

  const processDefinitions = [...new Set(
    tasks.map(t => t.processDefinitionKey || t.processDefinitionId)
  )]

  return {
    tasks,
    statistics,
    processDefinitions
  }
}

// Función para obtener usuarios mock
export const getMockUsers = async (): Promise<BPMNUser[]> => {
  await delay(300)
  return [...mockUsers]
}

// Función para obtener una tarea específica
export const getMockTask = async (taskId: string): Promise<BPMNTask | null> => {
  await delay(200)
  return mockTasks.find(t => t.id === taskId) || null
}

// Función para simular claim de tarea
export const mockClaimTask = async (taskId: string, userId: string): Promise<boolean> => {
  await delay(300)
  const task = mockTasks.find(t => t.id === taskId)
  if (task && !task.assignee) {
    task.assignee = userId
    task.owner = userId
    return true
  }
  return false
}

// Función para simular asignación de tarea
export const mockAssignTask = async (taskId: string, userId: string): Promise<boolean> => {
  await delay(300)
  const task = mockTasks.find(t => t.id === taskId)
  if (task) {
    task.assignee = userId
    task.owner = userId
    return true
  }
  return false
}

// Función para simular completar tarea
export const mockCompleteTask = async (taskId: string, variables: Record<string, any>): Promise<boolean> => {
  await delay(400)
  const taskIndex = mockTasks.findIndex(t => t.id === taskId)
  if (taskIndex !== -1) {
    mockTasks.splice(taskIndex, 1)
    return true
  }
  return false
}
