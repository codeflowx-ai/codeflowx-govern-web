// scripts/copy-bpmn-processes.js
// Script para copiar procesos BPMN desde la carpeta lib a la carpeta local

const fs = require('fs');
const path = require('path');

const sourceDir = path.join(__dirname, '..', '..', 'nocode-service', 'codeflowx.govern.workflow.lib', 'src', 'main', 'resources', 'processes');
const targetDir = path.join(__dirname, '..', 'mocks', 'bpmn');

const categories = ['aios', 'audit', 'compliance', 'metrics'];

function copyFile(source, target) {
  try {
    const targetDir = path.dirname(target);
    if (!fs.existsSync(targetDir)) {
      fs.mkdirSync(targetDir, { recursive: true });
    }
    fs.copyFileSync(source, target);
    console.log(`✓ Copiado: ${path.basename(source)}`);
    return true;
  } catch (error) {
    console.error(`✗ Error copiando ${source}:`, error.message);
    return false;
  }
}

function copyCategory(category) {
  const sourceCategoryDir = path.join(sourceDir, category);
  const targetCategoryDir = path.join(targetDir, category);

  if (!fs.existsSync(sourceCategoryDir)) {
    console.log(`⚠ Categoría ${category} no existe en origen`);
    return 0;
  }

  if (!fs.existsSync(targetCategoryDir)) {
    fs.mkdirSync(targetCategoryDir, { recursive: true });
  }

  const files = fs.readdirSync(sourceCategoryDir);
  let copied = 0;

  files.forEach(file => {
    if (file.endsWith('.bpmn') || file.endsWith('.bpmn20.xml')) {
      const sourceFile = path.join(sourceCategoryDir, file);
      const targetFile = path.join(targetCategoryDir, file);
      if (copyFile(sourceFile, targetFile)) {
        copied++;
      }
    }
  });

  return copied;
}

console.log('📋 Copiando procesos BPMN desde lib a carpeta local...\n');
console.log(`Origen: ${sourceDir}`);
console.log(`Destino: ${targetDir}\n`);

let totalCopied = 0;

categories.forEach(category => {
  console.log(`📁 Procesando categoría: ${category}`);
  const copied = copyCategory(category);
  totalCopied += copied;
  console.log(`   ${copied} archivos copiados\n`);
});

console.log(`✅ Total: ${totalCopied} procesos BPMN copiados exitosamente`);
