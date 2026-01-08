// types/bpmn-designer.ts - Tipos para el Diseñador BPMN

export interface BPMNElement {
  id: string
  type: 'start' | 'end' | 'task' | 'gateway' | 'event' | 'subprocess'
  subtype?: string
  name: string
  description?: string
  x: number
  y: number
  width: number
  height: number
  properties: {
    formKey?: string
    delegateClass?: string
    candidateGroups?: string[]
    candidateUsers?: string[]
    priority?: number
    assignee?: string
    dueDate?: string
    documentation?: string
    script?: string
    scriptFormat?: string
    conditionExpression?: string
    ruleVariablesInput?: string
    rules?: string
    resultVariable?: string
    type?: string
    isExecutable?: boolean
    [key: string]: any
  }
  connections: {
    incoming: string[]
    outgoing: string[]
  }
}

export interface BPMNConnection {
  id: string
  type: 'sequence' | 'message' | 'association'
  source: string
  target: string
  label?: string
  conditionExpression?: string
  properties: Record<string, any>
  waypoints?: Array<{ x: number; y: number }>
}

export interface BPMNProcess {
  id: string
  name: string
  elements: BPMNElement[]
  connections: BPMNConnection[]
  namespace: string
  isExecutable: boolean
  documentation?: string
  candidateStarterGroups?: string[]
}

export interface BPMNProcessInfo {
  id: string
  name: string
  category: string
  path: string
  version?: string
}

export interface BPMNSaveRequest {
  xml: string
  comment?: string
}

export interface BPMNSaveResponse {
  success: boolean
  processId?: string
  errors?: string[]
}

export interface ValidationResult {
  valid: boolean
  errors: string[]
}
