// app/(app)/bpmn-designer/utils/bpmnParser.ts
import { parseString } from 'xml2js'
import { BPMNElement, BPMNConnection, BPMNProcess } from '@/types/bpmn-designer'

/**
 * Parsea XML BPMN 2.0 a modelo interno
 */
export function parseBPMNXML(xmlString: string): Promise<BPMNProcess> {
  return new Promise((resolve, reject) => {
    parseString(xmlString, { explicitArray: true, mergeAttrs: true }, (err, result) => {
      if (err) {
        reject(err)
        return
      }

      try {
        const definitions = result.definitions || result['bpmn:definitions'] || {}
        const process = definitions.process?.[0] || {}
        const bpmnDiagram = definitions['bpmndi:BPMNDiagram']?.[0]?.['bpmndi:BPMNPlane']?.[0] || {}

        // Extraer información del proceso
        const processId = process.$.id || 'process'
        const processName = process.$.name || processId
        const isExecutable = process.$.isExecutable === 'true'
        const namespace = definitions.$?.targetNamespace || 'http://www.activiti.org/test'
        const documentation = process.documentation?.[0]?._ || process.documentation?.[0] || ''

        // Extraer elementos
        const elements: BPMNElement[] = []
        const shapes = bpmnDiagram['bpmndi:BPMNShape'] || []

        // Mapear startEvent
        const startEvents = process.startEvent || []
        startEvents.forEach((event: any) => {
          const shape = findShape(shapes, event.$.id)
          const element = mapXMLElementToBPMNElement(
            event,
            shape,
            'start',
            event.$.id,
            event.$.name || 'Start Event'
          )
          if (element) elements.push(element)
        })

        // Mapear endEvent
        const endEvents = process.endEvent || []
        endEvents.forEach((event: any) => {
          const shape = findShape(shapes, event.$.id)
          const element = mapXMLElementToBPMNElement(
            event,
            shape,
            'end',
            event.$.id,
            event.$.name || 'End Event'
          )
          if (element) elements.push(element)
        })

        // Mapear userTask
        const userTasks = process.userTask || []
        userTasks.forEach((task: any) => {
          const shape = findShape(shapes, task.$.id)
          const element = mapXMLElementToBPMNElement(
            task,
            shape,
            'task',
            task.$.id,
            task.$.name || 'User Task',
            'user'
          )
          if (element) elements.push(element)
        })

        // Mapear serviceTask
        const serviceTasks = process.serviceTask || []
        serviceTasks.forEach((task: any) => {
          const shape = findShape(shapes, task.$.id)
          const element = mapXMLElementToBPMNElement(
            task,
            shape,
            'task',
            task.$.id,
            task.$.name || 'Service Task',
            'service'
          )
          if (element) elements.push(element)
        })

        // Mapear scriptTask
        const scriptTasks = process.scriptTask || []
        scriptTasks.forEach((task: any) => {
          const shape = findShape(shapes, task.$.id)
          const element = mapXMLElementToBPMNElement(
            task,
            shape,
            'task',
            task.$.id,
            task.$.name || 'Script Task',
            'script'
          )
          if (element) elements.push(element)
        })

        // Mapear businessRuleTask
        const businessRuleTasks = process.businessRuleTask || []
        businessRuleTasks.forEach((task: any) => {
          const shape = findShape(shapes, task.$.id)
          const element = mapXMLElementToBPMNElement(
            task,
            shape,
            'task',
            task.$.id,
            task.$.name || 'Business Rule Task',
            'businessRule'
          )
          if (element) elements.push(element)
        })

        // Mapear exclusiveGateway
        const exclusiveGateways = process.exclusiveGateway || []
        exclusiveGateways.forEach((gateway: any) => {
          const shape = findShape(shapes, gateway.$.id)
          const element = mapXMLElementToBPMNElement(
            gateway,
            shape,
            'gateway',
            gateway.$.id,
            gateway.$.name || 'Exclusive Gateway',
            'exclusive'
          )
          if (element) elements.push(element)
        })

        // Mapear parallelGateway
        const parallelGateways = process.parallelGateway || []
        parallelGateways.forEach((gateway: any) => {
          const shape = findShape(shapes, gateway.$.id)
          const element = mapXMLElementToBPMNElement(
            gateway,
            shape,
            'gateway',
            gateway.$.id,
            gateway.$.name || 'Parallel Gateway',
            'parallel'
          )
          if (element) elements.push(element)
        })

        // Mapear inclusiveGateway
        const inclusiveGateways = process.inclusiveGateway || []
        inclusiveGateways.forEach((gateway: any) => {
          const shape = findShape(shapes, gateway.$.id)
          const element = mapXMLElementToBPMNElement(
            gateway,
            shape,
            'gateway',
            gateway.$.id,
            gateway.$.name || 'Inclusive Gateway',
            'inclusive'
          )
          if (element) elements.push(element)
        })

        // Mapear boundaryEvent
        const boundaryEvents = process.boundaryEvent || []
        boundaryEvents.forEach((event: any) => {
          const shape = findShape(shapes, event.$.id)
          const element = mapXMLElementToBPMNElement(
            event,
            shape,
            'event',
            event.$.id,
            event.$.name || 'Boundary Event',
            'boundary'
          )
          if (element) elements.push(element)
        })

        // Extraer conexiones (sequenceFlow)
        const connections: BPMNConnection[] = []
        const sequenceFlows = process.sequenceFlow || []
        const edges = bpmnDiagram['bpmndi:BPMNEdge'] || []

        sequenceFlows.forEach((flow: any) => {
          const connection = mapXMLSequenceFlowToConnection(flow, edges)
          if (connection) {
            connections.push(connection)

            // Actualizar conexiones de elementos
            const sourceElement = elements.find(el => el.id === connection.source)
            const targetElement = elements.find(el => el.id === connection.target)

            if (sourceElement) {
              sourceElement.connections.outgoing.push(connection.id)
            }
            if (targetElement) {
              targetElement.connections.incoming.push(connection.id)
            }
          }
        })

        const bpmnProcess: BPMNProcess = {
          id: processId,
          name: processName,
          elements,
          connections,
          namespace,
          isExecutable,
          documentation,
          candidateStarterGroups: process.$?.['activiti:candidateStarterGroups']?.split(',') || []
        }

        resolve(bpmnProcess)
      } catch (error) {
        reject(error)
      }
    })
  })
}

/**
 * Mapea elemento XML BPMN a BPMNElement
 */
function mapXMLElementToBPMNElement(
  xmlElement: any,
  shape: any,
  type: BPMNElement['type'],
  id: string,
  name: string,
  subtype?: string
): BPMNElement | null {
  const bounds = shape?.['omgdc:Bounds']?.[0]?.$ || shape?.['dc:Bounds']?.[0]?.$ || {}
  const x = parseFloat(bounds.x || '0')
  const y = parseFloat(bounds.y || '0')
  const width = parseFloat(bounds.width || (type === 'gateway' ? '50' : type === 'start' || type === 'end' ? '36' : '100'))
  const height = parseFloat(bounds.height || (type === 'gateway' ? '50' : type === 'start' || type === 'end' ? '36' : '80'))

  const properties: BPMNElement['properties'] = {}

  // Extraer propiedades activiti:*
  const attrs = xmlElement.$ || {}
  if (attrs['activiti:formKey']) properties.formKey = attrs['activiti:formKey']
  if (attrs['activiti:class']) properties.delegateClass = attrs['activiti:class']
  if (attrs['activiti:candidateGroups']) {
    properties.candidateGroups = attrs['activiti:candidateGroups'].split(',').map((g: string) => g.trim())
  }
  if (attrs['activiti:candidateUsers']) {
    properties.candidateUsers = attrs['activiti:candidateUsers'].split(',').map((u: string) => u.trim())
  }
  if (attrs['activiti:priority']) properties.priority = parseInt(attrs['activiti:priority'])
  if (attrs['activiti:assignee']) properties.assignee = attrs['activiti:assignee']
  if (attrs['activiti:type']) properties.type = attrs['activiti:type']
  if (attrs['activiti:ruleVariablesInput']) properties.ruleVariablesInput = attrs['activiti:ruleVariablesInput']
  if (attrs['activiti:rules']) properties.rules = attrs['activiti:rules']
  if (attrs['activiti:resultVariable']) properties.resultVariable = attrs['activiti:resultVariable']
  if (attrs['activiti:scriptFormat']) properties.scriptFormat = attrs['activiti:scriptFormat']

  // Extraer script
  if (xmlElement.script) {
    const scriptContent = xmlElement.script[0]?._ || xmlElement.script[0] || ''
    properties.script = scriptContent
  }

  // Extraer documentation
  const documentation = xmlElement.documentation?.[0]?._ || xmlElement.documentation?.[0] || ''
  if (documentation) {
    properties.documentation = documentation
  }

  return {
    id,
    type,
    subtype,
    name,
    description: documentation || undefined,
    x,
    y,
    width,
    height,
    properties,
    connections: {
      incoming: [],
      outgoing: []
    }
  }
}

/**
 * Mapea sequenceFlow XML a BPMNConnection
 */
function mapXMLSequenceFlowToConnection(xmlFlow: any, edges: any[]): BPMNConnection | null {
  const attrs = xmlFlow.$ || {}
  const id = attrs.id
  const source = attrs.sourceRef
  const target = attrs.targetRef

  if (!id || !source || !target) return null

  // Buscar edge para obtener label
  const edge = edges.find((e: any) => e.$.bpmnElement === id)
  const label = edge?.['bpmndi:BPMNLabel']?.[0]?.['omgdc:Bounds']?.[0]?.$?.label || attrs.name

  // Extraer conditionExpression
  const conditionExpression = xmlFlow.conditionExpression?.[0]?._ ||
                              xmlFlow.conditionExpression?.[0]?.$?.expression ||
                              xmlFlow['conditionExpression']?.[0]?._ ||
                              undefined

  return {
    id,
    type: 'sequence',
    source,
    target,
    label,
    conditionExpression,
    properties: {}
  }
}

/**
 * Busca shape por bpmnElement
 */
function findShape(shapes: any[], bpmnElementId: string): any {
  return shapes.find((shape: any) => shape.$.bpmnElement === bpmnElementId)
}
