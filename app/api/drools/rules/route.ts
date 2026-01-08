import { NextRequest, NextResponse } from 'next/server'
import { USE_MOCK } from '@/app/config/mock'
import { readFile } from 'fs/promises'
import { join } from 'path'

const WORKFLOW_ENGINE_URL = process.env.WORKFLOW_ENGINE_URL || 'http://localhost:8080'

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const mockPath = join(process.cwd(), 'mocks', 'drools', 'rules.json')
      const mockData = await readFile(mockPath, 'utf-8')
      return NextResponse.json(JSON.parse(mockData))
    } catch (error) {
      console.error('Error loading mock rules.json:', error)
      return NextResponse.json(
        { error: 'Error al cargar reglas mock' },
        { status: 500 }
      )
    }
  }

  try {
    const response = await fetch(`${WORKFLOW_ENGINE_URL}/api/drools/rules`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })

    if (!response.ok) {
      throw new Error(`Workflow engine error: ${response.statusText}`)
    }

    const data = await response.json()
    return NextResponse.json(data)
  } catch (error: any) {
    console.error('Error fetching Drools rules:', error)
    return NextResponse.json(
      { error: error.message || 'Error al obtener reglas Drools' },
      { status: 500 }
    )
  }
}
