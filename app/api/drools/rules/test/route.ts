import { NextRequest, NextResponse } from 'next/server'
import { USE_MOCK } from '@/app/config/mock'

const WORKFLOW_ENGINE_URL = process.env.WORKFLOW_ENGINE_URL || 'http://localhost:8080'

export async function POST(request: NextRequest) {
  if (USE_MOCK) {
    // En modo mock, simular prueba exitosa
    const body = await request.json()
    const factData = body.factData || {}

    return NextResponse.json({
      success: true,
      firedRules: 2,
      result: {
        ...factData,
        decision: 'APPROVED',
        processed: true,
        timestamp: new Date().toISOString(),
      },
      error: null,
    })
  }

  try {
    const body = await request.json()

    const response = await fetch(`${WORKFLOW_ENGINE_URL}/api/drools/rules/test`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      throw new Error(errorData.error || response.statusText)
    }

    const data = await response.json()
    return NextResponse.json(data)
  } catch (error: any) {
    console.error('Error testing Drools rule:', error)
    return NextResponse.json(
      { error: error.message || 'Error al probar regla Drools' },
      { status: 500 }
    )
  }
}
