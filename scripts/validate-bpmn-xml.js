// scripts/validate-bpmn-xml.js
// Script para validar y parsear todos los archivos BPMN XML

const fs = require('fs');
const path = require('path');
const { parseString } = require('xml2js');

const PROCESSES_DIR = path.join(__dirname, '..', 'data', 'bpmn-processes');

const categories = ['aios', 'audit', 'compliance', 'metrics'];

function parseXML(xmlContent) {
  return new Promise((resolve, reject) => {
    parseString(xmlContent, { explicitArray: true, mergeAttrs: true }, (err, result) => {
      if (err) {
        reject(err);
        return;
      }
      resolve(result);
    });
  });
}

function extractProcessInfo(xmlContent, result) {
  try {
    const definitions = result.definitions || result['bpmn:definitions'] || {};
    const process = definitions.process?.[0] || {};
    const bpmnDiagram = definitions['bpmndi:BPMNDiagram']?.[0]?.['bpmndi:BPMNPlane']?.[0] || {};

    const processId = process.$?.id || 'unknown';
    const processName = process.$?.name || processId;

    // Contar elementos
    const startEvents = process.startEvent || [];
    const endEvents = process.endEvent || [];
    const tasks = [
      ...(process.userTask || []),
      ...(process.serviceTask || []),
      ...(process.businessRuleTask || []),
      ...(process.scriptTask || []),
      ...(process.sendTask || []),
      ...(process.receiveTask || []),
      ...(process.manualTask || [])
    ];
    const gateways = [
      ...(process.exclusiveGateway || []),
      ...(process.inclusiveGateway || []),
      ...(process.parallelGateway || []),
      ...(process.eventBasedGateway || [])
    ];
    const sequences = process.sequenceFlow || [];
    const shapes = bpmnDiagram['bpmndi:BPMNShape'] || [];
    const edges = bpmnDiagram['bpmndi:BPMNEdge'] || [];

    return {
      processId,
      processName,
      elements: {
        startEvents: startEvents.length,
        endEvents: endEvents.length,
        tasks: tasks.length,
        gateways: gateways.length,
        sequences: sequences.length
      },
      diagram: {
        shapes: shapes.length,
        edges: edges.length
      }
    };
  } catch (error) {
    return {
      processId: 'unknown',
      processName: 'unknown',
      error: error.message
    };
  }
}

async function validateFile(filePath, category, file) {
  try {
    const xmlContent = fs.readFileSync(filePath, 'utf-8');

    if (!xmlContent || xmlContent.trim().length === 0) {
      return {
        file,
        category,
        status: 'ERROR',
        error: 'Archivo vacío'
      };
    }

    // Validar que sea XML válido
    if (!xmlContent.trim().startsWith('<?xml') && !xmlContent.trim().startsWith('<')) {
      return {
        file,
        category,
        status: 'ERROR',
        error: 'No es un archivo XML válido'
      };
    }

    // Parsear XML
    const result = await parseXML(xmlContent);

    // Extraer información del proceso
    const processInfo = extractProcessInfo(xmlContent, result);

    return {
      file,
      category,
      status: 'OK',
      ...processInfo
    };
  } catch (error) {
    return {
      file,
      category,
      status: 'ERROR',
      error: error.message,
      stack: error.stack
    };
  }
}

async function validateCategory(category, baseDir) {
  const categoryDir = path.join(baseDir, category);

  if (!fs.existsSync(categoryDir)) {
    return [];
  }

  const files = fs.readdirSync(categoryDir);
  const bpmnFiles = files.filter(f => f.endsWith('.bpmn') || f.endsWith('.bpmn20.xml'));

  const results = [];
  for (const file of bpmnFiles) {
    const filePath = path.join(categoryDir, file);
    const result = await validateFile(filePath, category, file);
    results.push(result);
  }

  return results;
}

async function main() {
  console.log('🔍 Validando archivos BPMN XML...\n');

  // Modo demo: SIEMPRE validar desde data/bpmn-processes
  if (!fs.existsSync(PROCESSES_DIR)) {
    console.error('❌ No se encontró directorio de procesos (data/bpmn-processes).');
    console.error(`   Buscado en: ${PROCESSES_DIR}`);
    process.exit(1);
  }

  console.log(`📁 Directorio: ${PROCESSES_DIR}\n`);

  const allResults = [];

  for (const category of categories) {
    console.log(`📂 Validando categoría: ${category}`);
    const results = await validateCategory(category, PROCESSES_DIR);
    allResults.push(...results);

    const ok = results.filter(r => r.status === 'OK').length;
    const errors = results.filter(r => r.status === 'ERROR').length;
    console.log(`   ✓ OK: ${ok}, ✗ ERROR: ${errors}\n`);
  }

  // Resumen
  console.log('\n' + '='.repeat(80));
  console.log('📊 RESUMEN');
  console.log('='.repeat(80));

  const totalOk = allResults.filter(r => r.status === 'OK').length;
  const totalErrors = allResults.filter(r => r.status === 'ERROR').length;

  console.log(`Total archivos: ${allResults.length}`);
  console.log(`✓ Válidos: ${totalOk}`);
  console.log(`✗ Con errores: ${totalErrors}\n`);

  // Mostrar errores detallados
  if (totalErrors > 0) {
    console.log('='.repeat(80));
    console.log('❌ ERRORES DETALLADOS');
    console.log('='.repeat(80));

    allResults
      .filter(r => r.status === 'ERROR')
      .forEach(result => {
        console.log(`\n📄 ${result.category}/${result.file}`);
        console.log(`   Error: ${result.error}`);
        if (result.stack) {
          console.log(`   Stack: ${result.stack.split('\n')[0]}`);
        }
      });
  }

  // Mostrar información de archivos válidos
  if (totalOk > 0) {
    console.log('\n' + '='.repeat(80));
    console.log('✓ ARCHIVOS VÁLIDOS');
    console.log('='.repeat(80));

    allResults
      .filter(r => r.status === 'OK')
      .forEach(result => {
        console.log(`\n📄 ${result.category}/${result.file}`);
        console.log(`   ID: ${result.processId}`);
        console.log(`   Nombre: ${result.processName}`);
        if (result.elements) {
          console.log(`   Elementos: ${result.elements.startEvents} start, ${result.elements.endEvents} end, ${result.elements.tasks} tasks, ${result.elements.gateways} gateways`);
          console.log(`   Conexiones: ${result.elements.sequences} sequence flows`);
        }
        if (result.diagram) {
          console.log(`   Diagrama: ${result.diagram.shapes} shapes, ${result.diagram.edges} edges`);
        }
      });
  }

  console.log('\n' + '='.repeat(80));

  // Exit code
  process.exit(totalErrors > 0 ? 1 : 0);
}

main().catch(error => {
  console.error('❌ Error fatal:', error);
  process.exit(1);
});
