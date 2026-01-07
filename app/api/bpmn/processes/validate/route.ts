// app/api/bpmn/processes/validate/route.ts
import { NextRequest, NextResponse } from 'next/server'

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080'

/**
 * POST /api/bpmn/processes/validate
 * Valida sintaxis BPMN sin guardar
 */
export async function POST(request: NextRequest) {
  try {
    // En modo demo, retornar validación básica sin backend
    // Validación completa requiere Flowable en el backend
    const body = await request.json()
    const xml = body.xml || ''

    // Validación básica: verificar que sea XML válido
    if (!xml || !xml.trim().startsWith('<?xml') && !xml.trim().startsWith('<')) {
      return NextResponse.json({
        valid: false,
        errors: ['El contenido no es un XML válido']
      })
    }

    // Validación básica: verificar que tenga un proceso
    if (!xml.includes('<process')) {
      return NextResponse.json({
        valid: false,
        errors: ['El XML no contiene un elemento <process>']
      })
    }

    // En modo demo, retornar como válido (validación completa requiere backend)
    return NextResponse.json({
      valid: true,
      errors: [],
      warning: 'Validación básica completada. Validación completa requiere backend con Flowable.'
    })
  } catch (error) {
    console.error('Error validating BPMN process:', error)
    return NextResponse.json(
      {
        valid: false,
        errors: [error instanceof Error ? error.message : 'Error desconocido al validar']
      },
      { status: 500 }
    )
  }
}
