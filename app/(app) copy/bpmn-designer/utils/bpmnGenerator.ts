// app/(app)/bpmn-designer/utils/bpmnGenerator.ts
import { BPMNElement, BPMNConnection, BPMNProcess } from '@/types/bpmn-designer'

/**
 * Genera XML BPMN 2.0 desde modelo interno
 */
export function generateBPMNXML(process: BPMNProcess): string {
  const namespace = process.namespace || 'http://www.activiti.org/test'

  let xml = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
             xmlns:activiti="http://activiti.org/bpmn"
             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
             xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
             typeLanguage="http://www.w3.org/2001/XMLSchema"
             expressionLanguage="http://www.w3.org/1999/XPath"
             targetNamespace="${namespace}">
  <process id="${escapeXml(process.id)}" name="${escapeXml(process.name)}" isExecutable="${process.isExecutable ? 'true' : 'false'}"`

  if (process.candidateStarterGroups && process.candidateStarterGroups.length > 0) {
    xml += ` activiti:candidateStarterGroups="${process.candidateStarterGroups.join(',')}"`
  }

  xml += `>`

  // Agregar documentation del proceso
  if (process.documentation) {
    xml += `
    <documentation>${escapeXml(process.documentation)}</documentation>`
  }

  // Generar elementos
  process.elements.forEach(element => {
    xml += generateXMLElement(element)
  })

  // Generar conexiones
  process.connections.forEach(connection => {
    xml += generateXMLSequenceFlow(connection)
  })

  xml += `
  </process>`

  // Generar BPMNDiagram
  xml += generateBPMNDiagram(process)

  xml += `
</definitions>`

  return xml
}

/**
 * Genera elemento XML desde BPMNElement
 */
function generateXMLElement(element: BPMNElement): string {
  let xml = ''

  switch (element.type) {
    case 'start':
      xml = `\n    <startEvent id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      break

    case 'end':
      xml = `\n    <endEvent id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      break

    case 'task':
      if (element.subtype === 'user') {
        xml = `\n    <userTask id="${escapeXml(element.id)}" name="${escapeXml(element.name)}"`
        if (element.properties.formKey) {
          xml += ` activiti:formKey="${escapeXml(element.properties.formKey)}"`
        }
        if (element.properties.candidateGroups && element.properties.candidateGroups.length > 0) {
          xml += ` activiti:candidateGroups="${element.properties.candidateGroups.join(',')}"`
        }
        if (element.properties.candidateUsers && element.properties.candidateUsers.length > 0) {
          xml += ` activiti:candidateUsers="${element.properties.candidateUsers.join(',')}"`
        }
        if (element.properties.priority !== undefined) {
          xml += ` activiti:priority="${element.properties.priority}"`
        }
        if (element.properties.assignee) {
          xml += ` activiti:assignee="${escapeXml(element.properties.assignee)}"`
        }
        xml += `>`
      } else if (element.subtype === 'service') {
        xml = `\n    <serviceTask id="${escapeXml(element.id)}" name="${escapeXml(element.name)}"`
        if (element.properties.delegateClass) {
          xml += ` activiti:class="${escapeXml(element.properties.delegateClass)}"`
        }
        if (element.properties.type) {
          xml += ` activiti:type="${escapeXml(element.properties.type)}"`
        }
        xml += `>`
      } else if (element.subtype === 'script') {
        xml = `\n    <scriptTask id="${escapeXml(element.id)}" name="${escapeXml(element.name)}"`
        if (element.properties.scriptFormat) {
          xml += ` scriptFormat="${escapeXml(element.properties.scriptFormat)}"`
        }
        xml += `>`
        if (element.properties.script) {
          xml += `\n      <script><![CDATA[${element.properties.script}]]></script>`
        }
      } else if (element.subtype === 'businessRule') {
        xml = `\n    <businessRuleTask id="${escapeXml(element.id)}" name="${escapeXml(element.name)}"`
        if (element.properties.ruleVariablesInput) {
          xml += ` activiti:ruleVariablesInput="${escapeXml(element.properties.ruleVariablesInput)}"`
        }
        if (element.properties.rules) {
          xml += ` activiti:rules="${escapeXml(element.properties.rules)}"`
        }
        if (element.properties.resultVariable) {
          xml += ` activiti:resultVariable="${escapeXml(element.properties.resultVariable)}"`
        }
        xml += `>`
      } else {
        xml = `\n    <task id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      }
      break

    case 'gateway':
      if (element.subtype === 'exclusive') {
        xml = `\n    <exclusiveGateway id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      } else if (element.subtype === 'parallel') {
        xml = `\n    <parallelGateway id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      } else if (element.subtype === 'inclusive') {
        xml = `\n    <inclusiveGateway id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      } else {
        xml = `\n    <exclusiveGateway id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      }
      break

    case 'event':
      if (element.subtype === 'boundary') {
        xml = `\n    <boundaryEvent id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      } else {
        xml = `\n    <intermediateCatchEvent id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
      }
      break

    default:
      xml = `\n    <task id="${escapeXml(element.id)}" name="${escapeXml(element.name)}">`
  }

  // Agregar documentation si existe
  if (element.properties.documentation || element.description) {
    const doc = element.properties.documentation || element.description || ''
    xml += `\n      <documentation>${escapeXml(doc)}</documentation>`
  }

  // Cerrar elemento
  const tagName = xml.match(/<(\w+)/)?.[1] || 'task'
  xml += `\n    </${tagName}>`

  return xml
}

/**
 * Genera sequenceFlow XML desde BPMNConnection
 */
function generateXMLSequenceFlow(connection: BPMNConnection): string {
  let xml = `\n    <sequenceFlow id="${escapeXml(connection.id)}" sourceRef="${escapeXml(connection.source)}" targetRef="${escapeXml(connection.target)}"`

  if (connection.label) {
    xml += ` name="${escapeXml(connection.label)}"`
  }

  xml += `>`

  if (connection.conditionExpression) {
    xml += `\n      <conditionExpression xsi:type="tFormalExpression"><![CDATA[${connection.conditionExpression}]]></conditionExpression>`
  }

  xml += `\n    </sequenceFlow>`

  return xml
}

/**
 * Genera BPMNDiagram con posiciones
 */
function generateBPMNDiagram(process: BPMNProcess): string {
  const diagramId = `BPMNDiagram_${process.id}`
  const planeId = `BPMNPlane_${process.id}`

  let xml = `
  <bpmndi:BPMNDiagram id="${diagramId}">
    <bpmndi:BPMNPlane bpmnElement="${process.id}" id="${planeId}">`

  // Generar shapes para elementos
  process.elements.forEach(element => {
    xml += generateBPMNShape(element)
  })

  // Generar edges para conexiones
  process.connections.forEach(connection => {
    xml += generateBPMNEdge(connection, process.elements)
  })

  xml += `
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>`

  return xml
}

/**
 * Genera BPMNShape para un elemento
 */
function generateBPMNShape(element: BPMNElement): string {
  const shapeId = `BPMNShape_${element.id}`
  return `
      <bpmndi:BPMNShape bpmnElement="${escapeXml(element.id)}" id="${shapeId}">
        <omgdc:Bounds height="${element.height}" width="${element.width}" x="${element.x}" y="${element.y}"></omgdc:Bounds>
      </bpmndi:BPMNShape>`
}

/**
 * Genera BPMNEdge para una conexión
 */
function generateBPMNEdge(connection: BPMNConnection, elements: BPMNElement[]): string {
  const sourceElement = elements.find(el => el.id === connection.source)
  const targetElement = elements.find(el => el.id === connection.target)

  if (!sourceElement || !targetElement) {
    return ''
  }

  const startX = sourceElement.x + sourceElement.width / 2
  const startY = sourceElement.y + sourceElement.height / 2
  const endX = targetElement.x + targetElement.width / 2
  const endY = targetElement.y + targetElement.height / 2

  const edgeId = `BPMNEdge_${connection.id}`

  let xml = `
      <bpmndi:BPMNEdge bpmnElement="${escapeXml(connection.id)}" id="${edgeId}">
        <omgdi:waypoint x="${startX}" y="${startY}"></omgdi:waypoint>
        <omgdi:waypoint x="${endX}" y="${endY}"></omgdi:waypoint>`

  if (connection.label) {
    xml += `
        <bpmndi:BPMNLabel>
          <omgdc:Bounds height="16.0" width="100.0" x="${(startX + endX) / 2}" y="${(startY + endY) / 2 - 8}"></omgdc:Bounds>
        </bpmndi:BPMNLabel>`
  }

  xml += `
      </bpmndi:BPMNEdge>`

  return xml
}

/**
 * Escapa caracteres XML especiales
 */
function escapeXml(text: string): string {
  if (!text) return ''
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&apos;')
}
