// app/api/bpmn/processes/local/route.ts
// API local para leer procesos BPMN desde la carpeta data/bpmn-processes (modo demo)

import { NextRequest, NextResponse } from 'next/server'
import { readdir, readFile } from 'fs/promises'
import { join } from 'path'
import { existsSync } from 'fs'

// Modo demo: SIEMPRE usar data/bpmn-processes (fuente editable)
const PROCESSES_DIR = join(process.cwd(), 'data', 'bpmn-processes')

interface BPMNProcessInfo {
  id: string
  name: string
  category?: string
  path?: string
}

/**
 * Extrae el nombre del proceso desde el XML
 */
function extractProcessName(xmlContent: string): string | null {
  const nameMatch = xmlContent.match(/<process[^>]*name="([^"]+)"/i)
  if (nameMatch) {
    return nameMatch[1]
  }

  // Intentar con process id como fallback
  const idMatch = xmlContent.match(/<process[^>]*id="([^"]+)"/i)
  if (idMatch) {
    return idMatch[1]
  }

  return null
}

/**
 * Extrae el ID del proceso desde el XML
 */
function extractProcessId(xmlContent: string, filename: string): string {
  const idMatch = xmlContent.match(/<process[^>]*id="([^"]+)"/i)
  if (idMatch) {
    return idMatch[1]
  }

  // Fallback: usar nombre del archivo sin extensión
  return filename.replace(/\.(bpmn|bpmn20\.xml)$/, '')
}

/**
 * GET /api/bpmn/processes/local
 * Lista todos los procesos BPMN disponibles en la carpeta local
 */
export async function GET(request: NextRequest) {
  try {
    if (!existsSync(PROCESSES_DIR)) {
      return NextResponse.json(
        { error: 'Directorio de procesos no encontrado. Ejecuta "npm run copy:bpmn" primero.' },
        { status: 404 }
      )
    }

    const processes: BPMNProcessInfo[] = []
    const categories = ['aios', 'audit', 'compliance', 'metrics']

    for (const category of categories) {
      const categoryDir = join(PROCESSES_DIR, category)

      if (!existsSync(categoryDir)) {
        continue
      }

      try {
        const files = await readdir(categoryDir)

        for (const file of files) {
          if (file.endsWith('.bpmn') || file.endsWith('.bpmn20.xml')) {
            try {
              const filePath = join(categoryDir, file)
              const xmlContent = await readFile(filePath, 'utf-8')

              const processId = extractProcessId(xmlContent, file)
              const processName = extractProcessName(xmlContent) || processId

              processes.push({
                id: processId,
                name: processName,
                category: category,
                path: `${category}/${file}`
              })
            } catch (error) {
              console.error(`Error procesando archivo ${file}:`, error)
            }
          }
        }
      } catch (error) {
        console.error(`Error leyendo categoría ${category}:`, error)
      }
    }

    return NextResponse.json(processes)
  } catch (error) {
    console.error('Error listing local BPMN processes:', error)
    return NextResponse.json(
      { error: 'Internal server error', details: error instanceof Error ? error.message : 'Unknown error' },
      { status: 500 }
    )
  }
}
