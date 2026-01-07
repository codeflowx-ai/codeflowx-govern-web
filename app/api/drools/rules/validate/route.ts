import { NextRequest, NextResponse } from 'next/server'
import { USE_MOCK } from '@/app/config/mock'

const WORKFLOW_ENGINE_URL = process.env.WORKFLOW_ENGINE_URL || 'http://localhost:8080'

export async function POST(request: NextRequest) {
  if (USE_MOCK) {
    // En modo mock, simular validación exitosa
    const body = await request.json()
    const content = body.content || ''

    // Validación básica mock: verificar que tenga "rule" y "when" y "then"
    const hasRule = content.includes('rule')
    const hasWhen = content.includes('when')
    const hasThen = content.includes('then')

    if (hasRule && hasWhen && hasThen) {
      return NextResponse.json({
        valid: true,
        errors: [],
      })
    } else {
      return NextResponse.json({
        valid: false,
        errors: ['La regla debe contener: rule, when y then'],
      })
    }
  }

  try {
    const body = await request.json()

    const response = await fetch(`${WORKFLOW_ENGINE_URL}/api/drools/rules/validate`, {
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
    console.error('Error validating Drools rule:', error)
    return NextResponse.json(
      { error: error.message || 'Error al validar regla Drools' },
      { status: 500 }
    )
  }
}
