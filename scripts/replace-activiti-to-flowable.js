// Script para reemplazar namespace activiti por flowable en todos los archivos BPMN
const fs = require('fs')
const path = require('path')
const { readdir, readFile, writeFile } = require('fs/promises')

const PROCESSES_DIR = path.join(process.cwd(), 'data', 'bpmn-processes')

async function replaceActivitiToFlowable(filePath) {
  try {
    const content = await readFile(filePath, 'utf-8')

    // Reemplazos necesarios
    let newContent = content

    // Reemplazar namespace xmlns:activiti por xmlns:flowable
    newContent = newContent.replace(/xmlns:activiti=/g, 'xmlns:flowable=')

    // Reemplazar referencias activiti: por flowable:
    newContent = newContent.replace(/activiti:/g, 'flowable:')

    // Reemplazar URLs de namespace
    newContent = newContent.replace(
      /http:\/\/activiti\.org\/bpmn/g,
      'http://flowable.org/bpmn'
    )

    // Reemplazar en atributos y elementos
    newContent = newContent.replace(/activiti/g, 'flowable')

    // Si el contenido cambió, escribir el archivo
    if (content !== newContent) {
      await writeFile(filePath, newContent, 'utf-8')
      console.log(`✓ Actualizado: ${filePath}`)
      return true
    }

    return false
  } catch (error) {
    console.error(`✗ Error procesando ${filePath}:`, error.message)
    return false
  }
}

async function processDirectory(dirPath) {
  const categories = ['aios', 'audit', 'compliance', 'metrics']
  let totalProcessed = 0
  let totalUpdated = 0

  for (const category of categories) {
    const categoryDir = path.join(dirPath, category)

    if (!fs.existsSync(categoryDir)) {
      console.log(`⚠ Categoría ${category} no encontrada en ${categoryDir}`)
      continue
    }

    try {
      const files = await readdir(categoryDir)
      const bpmnFiles = files.filter(file =>
        file.endsWith('.bpmn') || file.endsWith('.bpmn20.xml')
      )

      console.log(`\n📁 Procesando categoría: ${category} (${bpmnFiles.length} archivos)`)

      for (const file of bpmnFiles) {
        const filePath = path.join(categoryDir, file)
        totalProcessed++

        const updated = await replaceActivitiToFlowable(filePath)
        if (updated) {
          totalUpdated++
        }
      }
    } catch (error) {
      console.error(`✗ Error procesando categoría ${category}:`, error.message)
    }
  }

  return { totalProcessed, totalUpdated }
}

async function main() {
  console.log('🔄 Reemplazando namespace activiti por flowable en archivos BPMN...\n')

  // Modo demo: SIEMPRE operar sobre data/bpmn-processes
  if (!fs.existsSync(PROCESSES_DIR)) {
    console.error(`✗ Directorio no encontrado: ${PROCESSES_DIR}`)
    console.error('   Ejecuta "npm run copy:bpmn" primero para copiar los procesos.')
    process.exit(1)
  }

  console.log(`📂 Directorio: ${PROCESSES_DIR}\n`)

  const { totalProcessed, totalUpdated } = await processDirectory(PROCESSES_DIR)

  console.log(`\n✅ Proceso completado:`)
  console.log(`   - Archivos procesados: ${totalProcessed}`)
  console.log(`   - Archivos actualizados: ${totalUpdated}`)
  console.log(`   - Archivos sin cambios: ${totalProcessed - totalUpdated}`)
}

main().catch(error => {
  console.error('✗ Error fatal:', error)
  process.exit(1)
})
