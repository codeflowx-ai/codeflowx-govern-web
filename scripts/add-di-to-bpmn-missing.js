// scripts/add-di-to-bpmn-missing.js
// Recorre data/bpmn-processes y aplica add-di-to-bpmn.js SOLO a los BPMN que no tengan shapes/edges.

const fs = require('fs')
const path = require('path')

const ROOT = path.join(process.cwd(), 'data', 'bpmn-processes')

function listFiles(dir) {
  const out = []
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name)
    if (entry.isDirectory()) out.push(...listFiles(full))
    else out.push(full)
  }
  return out
}

function hasShapesOrEdges(xml) {
  return /bpmndi:BPMNShape|bpmndi:BPMNEdge|BPMNShape|BPMNEdge/.test(xml)
}

function hasBpmnPlane(xml) {
  return /<bpmndi:BPMNPlane\b/i.test(xml)
}

function main() {
  if (!fs.existsSync(ROOT)) {
    console.error(`✗ No existe: ${ROOT}`)
    process.exit(1)
  }

  const candidates = listFiles(ROOT).filter(f => f.endsWith('.bpmn') || f.endsWith('.bpmn20.xml'))
  const script = path.join(process.cwd(), 'scripts', 'add-di-to-bpmn.js')

  let processed = 0
  let updated = 0
  let skipped = 0

  for (const file of candidates) {
    const xml = fs.readFileSync(file, 'utf8')

    // Solo cuando hay plane pero no hay shapes/edges
    if (!hasBpmnPlane(xml)) {
      skipped++
      continue
    }
    if (hasShapesOrEdges(xml)) {
      skipped++
      continue
    }

    // Ejecutar script base (sync)
    const { spawnSync } = require('child_process')
    const r = spawnSync(process.execPath, [script, file], { stdio: 'inherit' })
    processed++
    if (r.status === 0) updated++
  }

  console.log('\n✅ add-di missing summary:', { processed, updated, skipped, total: candidates.length })
}

main()
