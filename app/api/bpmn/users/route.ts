// app/api/bpmn/users/route.ts - API para obtener usuarios disponibles
import { NextRequest, NextResponse } from 'next/server'
import { config } from '@/app/config/environment'
import { getMockUsers } from '@/app/bpmn/data/mockData'

export async function GET(request: NextRequest) {
  try {
    // Si está en modo mock, usar datos mock
    if (config.useMock) {
      const users = await getMockUsers()
      return NextResponse.json({ users })
    }

    // Modo real: conectar al backend
    const backendUrl = config.backendUrl
    const authHeader = request.headers.get('authorization')

    const response = await fetch(`${backendUrl}/api/bpmn/users`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(authHeader && { 'Authorization': authHeader })
      }
    })

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Error obteniendo usuarios' },
        { status: response.status }
      )
    }

    const data = await response.json()

    return NextResponse.json({
      users: data.users || []
    })
  } catch (error) {
    console.error('Error en API de usuarios:', error)

    // En caso de error, si fallbackToMock está activado, usar mocks
    if (config.fallbackToMock) {
      const users = await getMockUsers()
      return NextResponse.json({ users })
    }

    return NextResponse.json(
      { error: 'Error interno del servidor' },
      { status: 500 }
    )
  }
}
