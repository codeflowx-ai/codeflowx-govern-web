// app/api/bpmn/tasks/route.ts - API para obtener tareas BPMN
import { NextRequest, NextResponse } from 'next/server'
import { config } from '@/app/config/environment'
import { getMockTasks } from '@/app/bpmn/data/mockData'

export async function GET(request: NextRequest) {
  try {
    // Si está en modo mock, usar datos mock
    if (config.useMock) {
      const currentUser = 'pm' // TODO: obtener del token de autenticación
      const data = await getMockTasks(currentUser)
      return NextResponse.json(data)
    }

    // Modo real: conectar al backend
    const backendUrl = config.backendUrl
    const searchParams = request.nextUrl.searchParams

    // Construir query params
    const params = new URLSearchParams()
    if (searchParams.get('assignee')) {
      params.append('assignee', searchParams.get('assignee')!)
    }
    if (searchParams.get('candidateUser')) {
      params.append('candidateUser', searchParams.get('candidateUser')!)
    }
    if (searchParams.get('candidateGroup')) {
      params.append('candidateGroup', searchParams.get('candidateGroup')!)
    }
    if (searchParams.get('processDefinitionKey')) {
      params.append('processDefinitionKey', searchParams.get('processDefinitionKey')!)
    }

    // Obtener token de autenticación del header
    const authHeader = request.headers.get('authorization')

    const response = await fetch(`${backendUrl}/api/bpmn/tasks?${params.toString()}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        ...(authHeader && { 'Authorization': authHeader })
      }
    })

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Error obteniendo tareas' },
        { status: response.status }
      )
    }

    const data = await response.json()

    // Calcular estadísticas
    const tasks = data.tasks || []
    const currentUser = data.currentUser || 'current-user' // TODO: obtener del token

    const statistics = {
      totalTasks: tasks.length,
      assignedToMeCount: tasks.filter((t: any) => t.assignee === currentUser).length,
      groupTasksCount: tasks.filter((t: any) =>
        !t.assignee && t.candidateGroups && t.candidateGroups.length > 0
      ).length
    }

    // Obtener definiciones de procesos únicas
    const processDefinitions = [...new Set(
      tasks.map((t: any) => t.processDefinitionKey || t.processDefinitionId)
    )]

    return NextResponse.json({
      tasks,
      statistics,
      processDefinitions
    })
  } catch (error) {
    console.error('Error en API de tareas:', error)

    // En caso de error, si fallbackToMock está activado, usar mocks
    if (config.fallbackToMock) {
      const currentUser = 'pm'
      const data = await getMockTasks(currentUser)
      return NextResponse.json(data)
    }

    return NextResponse.json(
      { error: 'Error interno del servidor' },
      { status: 500 }
    )
  }
}
