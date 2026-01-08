// scripts/add-di-to-bpmn.js
// Inyecta BPMNDI (shapes + edges) en BPMN que tenga BPMNPlane vacío.
// KISS: string-based, namespace-agnostic, pensado para modo demo/desarrollo.

const fs = require('fs')
const path = require('path')

const ELEMENT_DIMENSIONS = {
  startEvent: { width: 36, height: 36 },
  endEvent: { width: 36, height: 36 },
  task: { width: 100, height: 80 },
  userTask: { width: 100, height: 80 },
  serviceTask: { width: 100, height: 80 },
  scriptTask: { width: 100, height: 80 },
  businessRuleTask: { width: 110, height: 80 },
  sendTask: { width: 100, height: 80 },
  receiveTask: { width: 100, height: 80 },
  manualTask: { width: 100, height: 80 },
  callActivity: { width: 120, height: 90 },
  exclusiveGateway: { width: 50, height: 50 },
  inclusiveGateway: { width: 50, height: 50 },
  parallelGateway: { width: 50, height: 50 },
  eventBasedGateway: { width: 50, height: 50 },
  boundaryEvent: { width: 36, height: 36 },
  intermediateCatchEvent: { width: 36, height: 36 },
  intermediateThrowEvent: { width: 36, height: 36 }
}

const SPACING = { horizontal: 200, vertical: 120, level: 180 }
const INITIAL_OFFSET = { x: 100, y: 100 }

function extractProcessId(xml) {
  const m = xml.match(/<\s*(?:[\w-]+:)?process\b[^>]*\bid="([^"]+)"/i)
  return m ? m[1] : null
}

function extractSequenceFlows(xml) {
  const flows = []
  const re =
    /<\s*(?:[\w-]+:)?sequenceFlow\b[^>]*\bid="([^"]+)"[^>]*\bsourceRef="([^"]+)"[^>]*\btargetRef="([^"]+)"[^>]*>/gi
  let m
  while ((m = re.exec(xml)) !== null) {
    flows.push({ id: m[1], source: m[2], target: m[3] })
  }
  return flows
}

function extractElements(xml) {
  const tags = [
    'startEvent',
    'endEvent',
    'task',
    'userTask',
    'serviceTask',
    'scriptTask',
    'businessRuleTask',
    'sendTask',
    'receiveTask',
    'manualTask',
    'callActivity',
    'exclusiveGateway',
    'inclusiveGateway',
    'parallelGateway',
    'eventBasedGateway',
    'boundaryEvent',
    'intermediateCatchEvent',
    'intermediateThrowEvent'
  ]

  const re = new RegExp(`<\\s*(?:[\\w-]+:)?(${tags.join('|')})\\b[^>]*\\bid="([^"]+)"[^>]*>`, 'gi')
  const elements = []
  const seen = new Set()
  let m
  while ((m = re.exec(xml)) !== null) {
    const type = m[1]
    const id = m[2]
    if (!id || seen.has(id)) continue
    seen.add(id)
    elements.push({ id, type })
  }

  const flows = extractSequenceFlows(xml)
  for (const f of flows) {
    if (f.source && !seen.has(f.source)) {
      elements.push({ id: f.source, type: 'task' })
      seen.add(f.source)
    }
    if (f.target && !seen.has(f.target)) {
      elements.push({ id: f.target, type: 'task' })
      seen.add(f.target)
    }
  }

  // construir incoming/outgoing solo por flows (para BFS)
  const byId = new Map(elements.map(e => [e.id, e]))
  for (const f of flows) {
    const s = byId.get(f.source)
    const t = byId.get(f.target)
    if (s) {
      s.outgoing = s.outgoing || []
      s.outgoing.push(f.id)
    }
    if (t) {
      t.incoming = t.incoming || []
      t.incoming.push(f.id)
    }
  }

  return { elements, flows }
}

function calculateLevels(elements) {
  const levels = new Map()
  const visited = new Set()
  const queue = []

  const startEvents = elements.filter(e => e.type === 'startEvent')
  for (const s of startEvents) {
    queue.push({ id: s.id, level: 0 })
    levels.set(s.id, 0)
  }
  if (startEvents.length === 0 && elements.length > 0) {
    queue.push({ id: elements[0].id, level: 0 })
    levels.set(elements[0].id, 0)
  }

  while (queue.length) {
    const cur = queue.shift()
    if (visited.has(cur.id)) continue
    visited.add(cur.id)

    const el = elements.find(e => e.id === cur.id)
    if (!el || !el.outgoing) continue

    for (const flowId of el.outgoing) {
      const target = elements.find(e => e.incoming && e.incoming.includes(flowId))
      if (target && !levels.has(target.id)) {
        const next = cur.level + 1
        levels.set(target.id, next)
        queue.push({ id: target.id, level: next })
      }
    }
  }

  return levels
}

function calculatePositions(elements) {
  const positions = new Map()
  const levels = calculateLevels(elements)
  const elementsByLevel = new Map()

  for (const [id, level] of levels.entries()) {
    const el = elements.find(e => e.id === id)
    if (!el) continue
    if (!elementsByLevel.has(level)) elementsByLevel.set(level, [])
    elementsByLevel.get(level).push(el)
  }

  for (const [level, levelElements] of elementsByLevel.entries()) {
    const y = level * SPACING.level + INITIAL_OFFSET.y
    const totalWidth = levelElements.length * SPACING.horizontal
    const startX = INITIAL_OFFSET.x + (elementsByLevel.size > 1 ? totalWidth / 2 - (levelElements.length - 1) * SPACING.horizontal / 2 : INITIAL_OFFSET.x)

    levelElements.forEach((el, idx) => {
      const x = startX + idx * SPACING.horizontal
      const dims = ELEMENT_DIMENSIONS[el.type] || ELEMENT_DIMENSIONS.task
      positions.set(el.id, {
        x: Math.max(INITIAL_OFFSET.x, x - dims.width / 2),
        y: Math.max(INITIAL_OFFSET.y, y - dims.height / 2)
      })
    })
  }

  let unconnected = 0
  for (const el of elements) {
    if (!positions.has(el.id)) {
      const dims = ELEMENT_DIMENSIONS[el.type] || ELEMENT_DIMENSIONS.task
      positions.set(el.id, {
        x: INITIAL_OFFSET.x + unconnected * SPACING.horizontal - dims.width / 2,
        y: INITIAL_OFFSET.y - dims.height / 2
      })
      unconnected++
    }
  }

  return positions
}

function hasShapesOrEdges(xml) {
  return /bpmndi:BPMNShape|bpmndi:BPMNEdge|BPMNShape|BPMNEdge/.test(xml)
}

function injectDi(xml) {
  const processId = extractProcessId(xml) || 'process'
  const { elements, flows } = extractElements(xml)
  const positions = calculatePositions(elements)

  // Asegurar que existe un BPMNPlane NO self-closing y que referencia al process correcto
  // - Caso A: <bpmndi:BPMNPlane ... />  -> convertir a <bpmndi:BPMNPlane ...></bpmndi:BPMNPlane>
  // - Caso B: <bpmndi:BPMNPlane ...>...</bpmndi:BPMNPlane> -> asegurar bpmnElement correcto

  // 1) Convertir self-closing plane si existe
  const selfClosingPlaneRe = /<bpmndi:BPMNPlane\b([^>]*)\/>/i
  if (selfClosingPlaneRe.test(xml)) {
    xml = xml.replace(selfClosingPlaneRe, (_m, attrs) => {
      // asegurar bpmnElement
      let nextAttrs = String(attrs || '')
      if (/\bbpmnElement=/.test(nextAttrs)) {
        nextAttrs = nextAttrs.replace(/\bbpmnElement="[^"]*"/i, `bpmnElement="${processId}"`)
      } else {
        nextAttrs = `${nextAttrs} bpmnElement="${processId}"`
      }
      return `<bpmndi:BPMNPlane${nextAttrs}></bpmndi:BPMNPlane>`
    })
  }

  // 2) Asegurar bpmnElement correcto en plane no self-closing
  xml = xml.replace(/<bpmndi:BPMNPlane\b([^>]*)>/i, (match, attrs) => {
    const a = String(attrs || '')
    if (/\bbpmnElement=/.test(a)) {
      return `<bpmndi:BPMNPlane${a.replace(/\bbpmnElement="[^"]*"/i, `bpmnElement="${processId}"`)}>`
    }
    return `<bpmndi:BPMNPlane${a} bpmnElement="${processId}">`
  })

  // 3) Reemplazar contenido interno del plane
  const planeBlockRe = /(<bpmndi:BPMNPlane\b[^>]*>)([\s\S]*?)(<\/bpmndi:BPMNPlane>)/i
  const shapes = []

  for (const el of elements) {
    const pos = positions.get(el.id)
    if (!pos) continue
    const dims = ELEMENT_DIMENSIONS[el.type] || ELEMENT_DIMENSIONS.task
    const x = Math.max(INITIAL_OFFSET.x / 2, pos.x)
    const y = Math.max(INITIAL_OFFSET.y / 2, pos.y)
    shapes.push(
      `      <bpmndi:BPMNShape id="BPMNShape_${el.id}" bpmnElement="${el.id}">\n` +
      `        <omgdc:Bounds x="${x}" y="${y}" width="${Math.max(36, dims.width)}" height="${Math.max(36, dims.height)}" />\n` +
      `      </bpmndi:BPMNShape>`
    )
  }

  const edges = []
  for (const f of flows) {
    const sourcePos = positions.get(f.source)
    const targetPos = positions.get(f.target)
    if (!sourcePos || !targetPos) continue
    const sourceEl = elements.find(e => e.id === f.source) || { type: 'task' }
    const targetEl = elements.find(e => e.id === f.target) || { type: 'task' }
    const sd = ELEMENT_DIMENSIONS[sourceEl.type] || ELEMENT_DIMENSIONS.task
    const td = ELEMENT_DIMENSIONS[targetEl.type] || ELEMENT_DIMENSIONS.task

    edges.push(
      `      <bpmndi:BPMNEdge id="BPMNEdge_${f.id}" bpmnElement="${f.id}">\n` +
      `        <omgdi:waypoint x="${sourcePos.x + sd.width}" y="${sourcePos.y + sd.height / 2}" />\n` +
      `        <omgdi:waypoint x="${targetPos.x}" y="${targetPos.y + td.height / 2}" />\n` +
      `      </bpmndi:BPMNEdge>`
    )
  }

  const body = '\n' + [...shapes, ...edges].join('\n') + '\n'

  const updated = xml.replace(planeBlockRe, `$1${body}    $3`)
  if (updated === xml) throw new Error('No se pudo reemplazar el contenido del BPMNPlane')
  return updated
}

function main() {
  const target = process.argv[2]
  if (!target) {
    console.error('Uso: node scripts/add-di-to-bpmn.js <ruta-al-bpmn>')
    process.exit(1)
  }

  const filePath = path.isAbsolute(target) ? target : path.join(process.cwd(), target)
  const xml = fs.readFileSync(filePath, 'utf8')

  if (hasShapesOrEdges(xml)) {
    console.log(`= Saltado (ya tiene shapes/edges): ${filePath}`)
    process.exit(0)
  }

  const out = injectDi(xml)
  fs.writeFileSync(filePath, out, 'utf8')
  console.log(`✓ BPMNDI generado: ${filePath}`)
}

main()
