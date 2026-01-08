// types/bpmn.ts - Tipos para BPMN Tasks

export interface BPMNTask {
  id: string
  name: string
  description?: string
  assignee?: string
  owner?: string
  processInstanceId: string
  processDefinitionId: string
  processDefinitionKey?: string
  taskDefinitionKey: string
  priority: number
  dueDate?: string
  createTime: string
  candidateGroups?: string[]
  candidateUsers?: string[]
  formKey?: string
  executionId?: string
  parentTaskId?: string
  tenantId?: string
  category?: string
}

export interface BPMNProcessDefinition {
  id: string
  key: string
  name: string
  version: number
  category?: string
  deploymentId?: string
}

export interface BPMNUser {
  username: string
  fullname: string
  email: string
  id?: string
}

export interface TaskStatistics {
  totalTasks: number
  assignedToMeCount: number
  groupTasksCount: number
}

export type TaskPriority = 'low' | 'normal' | 'high' | 'urgent'
export type TaskState = 'all' | 'assigned' | 'unassigned' | 'candidate'

export interface TaskFilters {
  process?: string
  priority?: TaskPriority
  state?: TaskState
}
