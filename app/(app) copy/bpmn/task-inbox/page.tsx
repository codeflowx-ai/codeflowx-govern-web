// app/(app)/bpmn/task-inbox/page.tsx - Bandeja de Tareas BPMN con Wow Factor
'use client'

import { useTranslation } from '@/app/config/i18n'
import { AssignTaskDialog } from '@/components/bpmn/AssignTaskDialog'
import { TaskFiltersBar } from '@/components/bpmn/TaskFiltersBar'
import { TaskInboxTable } from '@/components/bpmn/TaskInboxTable'
import { TaskStatisticsCard } from '@/components/bpmn/TaskStatisticsCard'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader } from '@/components/ui/card'
import { BPMNTask, BPMNUser, TaskFilters, TaskPriority, TaskStatistics } from '@/types/bpmn'
import {
  Inbox,
  RefreshCw
} from 'lucide-react'
import { useEffect, useState } from 'react'

export default function TaskInboxPage() {
  const { t } = useTranslation()
  const [tasks, setTasks] = useState<BPMNTask[]>([])
  const [filteredTasks, setFilteredTasks] = useState<BPMNTask[]>([])
  const [statistics, setStatistics] = useState<TaskStatistics>({
    totalTasks: 0,
    assignedToMeCount: 0,
    groupTasksCount: 0
  })
  const [loading, setLoading] = useState(true)
  const [filters, setFilters] = useState<TaskFilters>({})
  const [processDefinitions, setProcessDefinitions] = useState<string[]>([])
  const [showAssignDialog, setShowAssignDialog] = useState(false)
  const [selectedTask, setSelectedTask] = useState<BPMNTask | null>(null)
  const [availableUsers, setAvailableUsers] = useState<BPMNUser[]>([])

  // Cargar tareas
  const loadTasks = async () => {
    setLoading(true)
    try {
      const response = await fetch('/api/bpmn/tasks')
      if (response.ok) {
        const data = await response.json()
        setTasks(data.tasks || [])
        setStatistics(data.statistics || statistics)
        setProcessDefinitions(data.processDefinitions || [])
      }
    } catch (error) {
      console.error('Error cargando tareas:', error)
    } finally {
      setLoading(false)
    }
  }

  // Filtrar tareas
  useEffect(() => {
    let filtered = [...tasks]

    if (filters.process) {
      filtered = filtered.filter(t =>
        t.processDefinitionId === filters.process ||
        t.processDefinitionKey === filters.process
      )
    }

    if (filters.priority) {
      filtered = filtered.filter(t => {
        const taskPriority = getPriorityFromNumber(t.priority)
        return taskPriority === filters.priority
      })
    }

    if (filters.state === 'assigned') {
      filtered = filtered.filter(t => t.assignee)
    } else if (filters.state === 'unassigned') {
      filtered = filtered.filter(t => !t.assignee)
    } else if (filters.state === 'candidate') {
      filtered = filtered.filter(t => !t.assignee && t.candidateGroups && t.candidateGroups.length > 0)
    }

    setFilteredTasks(filtered)
  }, [tasks, filters])

  // Cargar tareas al montar
  useEffect(() => {
    loadTasks()
  }, [])

  // Limpiar filtros
  const clearFilters = () => {
    setFilters({})
  }

  // Reclamar tarea
  const handleClaimTask = async (task: BPMNTask) => {
    try {
      const response = await fetch(`/api/bpmn/tasks/${task.id}/claim`, {
        method: 'POST'
      })
      if (response.ok) {
        await loadTasks()
      }
    } catch (error) {
      console.error('Error reclamando tarea:', error)
    }
  }

  // Abrir formulario de tarea - Navegación en la misma ventana (estándar BPMN)
  const handleOpenTaskForm = (task: BPMNTask) => {
    if (task.formKey) {
      // Navegar en la misma ventana (comportamiento estándar en bandejas BPMN)
      // Usar globalThis para compatibilidad
      if (globalThis.window !== undefined) {
        globalThis.window.location.href = `/bpmn/tasks/${task.id}/form?formKey=${encodeURIComponent(task.formKey)}`
      }
    }
  }

  // Abrir tarea haciendo click en la fila
  const handleRowClick = (task: BPMNTask) => {
    if (task.formKey) {
      handleOpenTaskForm(task)
    }
  }

  // Mostrar diálogo de asignación
  const handleShowAssignDialog = async (task: BPMNTask) => {
    setSelectedTask(task)
    try {
      const response = await fetch('/api/bpmn/users')
      if (response.ok) {
        const data = await response.json()
        setAvailableUsers(data.users || [])
      }
    } catch (error) {
      console.error('Error cargando usuarios:', error)
    }
    setShowAssignDialog(true)
  }

  // Asignar tarea
  const handleAssignTask = async (userId: string) => {
    if (!selectedTask) return

    try {
      const response = await fetch(`/api/bpmn/tasks/${selectedTask.id}/assign`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId })
      })
      if (response.ok) {
        setShowAssignDialog(false)
        setSelectedTask(null)
        await loadTasks()
      }
    } catch (error) {
      console.error('Error asignando tarea:', error)
    }
  }

  // Obtener prioridad como texto
  const getPriorityFromNumber = (priority: number): TaskPriority => {
    if (priority >= 100) return 'urgent'
    if (priority >= 75) return 'high'
    if (priority >= 50) return 'normal'
    return 'low'
  }

  return (
    <div className="space-y-6">
      {/* Banner de desarrollo */}
      {/* Header con título y subtítulo */}
      <div className="text-center space-y-2">
        <div className="flex items-center justify-center gap-3 mb-4">
          <Inbox className="w-8 h-8 text-primary" />
          <h1 className="text-3xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {t('taskInbox.title', 'Bandeja de Tareas BPMN')}
          </h1>
        </div>
        <p className="text-muted-foreground text-lg">
          {t('taskInbox.subtitle', 'Gestiona tus tareas pendientes')}
        </p>
      </div>

        {/* Estadísticas */}
        <TaskStatisticsCard statistics={statistics} />

        {/* Filtros */}
        <TaskFiltersBar
          filters={filters}
          onFiltersChange={setFilters}
          onClearFilters={clearFilters}
          processDefinitions={processDefinitions}
        />

      {/* Tabla de tareas con efectos avanzados */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg overflow-hidden relative hover:bg-card/90">
        {/* Efecto de brillo en hover */}
        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

        <CardHeader className="pb-3 relative z-10">
          <div className="flex items-center justify-between">
            <h2 className="text-lg font-semibold">{t('taskInbox.title', 'Bandeja de Tareas BPMN')}</h2>
            <Button
              variant="outline"
              size="sm"
              onClick={loadTasks}
              disabled={loading}
              className="hover:scale-105 transition-transform duration-200"
            >
              <RefreshCw size={16} className={`mr-1 ${loading ? 'animate-spin' : ''}`} />
              {t('taskInbox.actions.refresh', 'Refrescar')}
            </Button>
          </div>
        </CardHeader>

        <CardContent className="pt-0 relative z-10">
          <TaskInboxTable
            tasks={filteredTasks}
            loading={loading}
            onClaimTask={handleClaimTask}
            onOpenTaskForm={handleOpenTaskForm}
            onAssignTask={handleShowAssignDialog}
            onRowClick={handleRowClick}
            getPriorityFromNumber={getPriorityFromNumber}
          />
        </CardContent>
      </Card>

      {/* Modal de asignación */}
      <AssignTaskDialog
        open={showAssignDialog}
        onOpenChange={setShowAssignDialog}
        task={selectedTask}
        users={availableUsers}
        onAssign={handleAssignTask}
      />
    </div>
  )
}


