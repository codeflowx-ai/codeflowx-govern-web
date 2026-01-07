// components/bpmn/TaskInboxTable.tsx - Tabla con Wow Factor
'use client'

import { BPMNTask, TaskPriority } from '@/types/bpmn'
import { useTranslation } from '@/app/config/i18n'
import { Table, TableHeader, TableBody, TableRow, TableCell } from '@/components/ui/table'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { ExternalLink, Hand, UserPlus } from 'lucide-react'

interface TaskInboxTableProps {
  tasks: BPMNTask[]
  loading: boolean
  onClaimTask: (task: BPMNTask) => void
  onOpenTaskForm: (task: BPMNTask) => void
  onAssignTask: (task: BPMNTask) => void
  onRowClick?: (task: BPMNTask) => void
  getPriorityFromNumber: (priority: number) => TaskPriority
}

export function TaskInboxTable({
  tasks,
  loading,
  onClaimTask,
  onOpenTaskForm,
  onAssignTask,
  onRowClick,
  getPriorityFromNumber
}: TaskInboxTableProps) {
  const { t } = useTranslation()

  const getPriorityBadge = (priority: number) => {
    const priorityType = getPriorityFromNumber(priority)
    const variants: Record<TaskPriority, 'primary' | 'secondary' | 'danger' | 'outline'> = {
      low: 'outline',
      normal: 'primary',
      high: 'secondary',
      urgent: 'danger'
    }
    const labels: Record<TaskPriority, string> = {
      low: t('taskInbox.filters.low', 'Baja'),
      normal: t('taskInbox.filters.normal', 'Normal'),
      high: t('taskInbox.filters.high', 'Alta'),
      urgent: t('taskInbox.filters.urgent', 'Urgente')
    }
    return (
      <Badge variant={variants[priorityType]}>
        {labels[priorityType]}
      </Badge>
    )
  }

  const formatDate = (dateString?: string) => {
    if (!dateString) return '-'
    const date = new Date(dateString)
    return date.toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  const formatCandidateGroups = (groups?: string[]) => {
    if (!groups || groups.length === 0) return '-'
    return groups.join(', ')
  }

  const getDueDateStyle = (dueDate?: string) => {
    if (!dueDate) return ''
    const due = new Date(dueDate)
    const now = new Date()
    if (due < now) return 'text-destructive font-bold'
    const diffDays = (due.getTime() - now.getTime()) / (1000 * 60 * 60 * 24)
    if (diffDays <= 1) return 'text-warning font-bold'
    return ''
  }

  if (loading) {
    return (
      <div className="text-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">{t('common.loading', 'Cargando...')}</span>
        </div>
      </div>
    )
  }

  if (tasks.length === 0) {
    return (
      <div className="text-center py-5 text-muted-foreground">
        <p className="mb-0">{t('taskInbox.table.noTasks', 'No hay tareas pendientes')}</p>
      </div>
    )
  }

  return (
    <div className="overflow-x-auto">
      <Table hover>
        <TableHeader>
          <TableRow>
            <TableCell header scope="col" style={{ width: '80px', textAlign: 'center' }}>
              {t('taskInbox.table.priority', 'Prioridad')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '250px' }}>
              {t('taskInbox.table.task', 'Tarea')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '180px' }}>
              {t('taskInbox.table.process', 'Proceso')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '120px' }}>
              {t('taskInbox.table.assigned', 'Asignado')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '150px' }}>
              {t('taskInbox.table.roles', 'Roles')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '130px', textAlign: 'center' }}>
              {t('taskInbox.table.creation', 'Creación')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '130px', textAlign: 'center' }}>
              {t('taskInbox.table.dueDate', 'Vencimiento')}
            </TableCell>
            <TableCell header scope="col" style={{ width: '150px', textAlign: 'center' }}>
              {t('taskInbox.table.actions', 'Acciones')}
            </TableCell>
          </TableRow>
        </TableHeader>
        <TableBody>
          {tasks.map((task, index) => (
            <TableRow
              key={task.id}
              style={{
                cursor: task.formKey ? 'pointer' : 'default',
                animationDelay: `${index * 50}ms`,
                animation: 'fadeInUp 0.5s ease-out forwards'
              }}
              onClick={() => {
                if (task.formKey && onRowClick) {
                  onRowClick(task)
                }
              }}
              className={task.formKey ? 'hover:bg-hover transition-colors' : ''}
            >
              <TableCell style={{ textAlign: 'center' }}>
                {getPriorityBadge(task.priority)}
              </TableCell>
              <TableCell>
                <div>
                  <div className="font-medium">{task.name}</div>
                  {task.description && (
                    <div className="text-sm text-muted-foreground">{task.description}</div>
                  )}
                </div>
              </TableCell>
              <TableCell>
                {task.processDefinitionKey || task.processDefinitionId}
              </TableCell>
              <TableCell>
                <span className={task.assignee ? 'font-medium' : 'text-muted-foreground italic'}>
                  {task.assignee || t('taskInbox.table.unassigned', 'Sin asignar')}
                </span>
              </TableCell>
              <TableCell>
                <span className="text-sm text-muted-foreground">
                  {formatCandidateGroups(task.candidateGroups)}
                </span>
              </TableCell>
              <TableCell style={{ textAlign: 'center' }}>
                {formatDate(task.createTime)}
              </TableCell>
              <TableCell style={{ textAlign: 'center' }}>
                <span className={getDueDateStyle(task.dueDate)}>
                  {formatDate(task.dueDate)}
                </span>
              </TableCell>
              <TableCell style={{ textAlign: 'center' }}>
                <div className="flex gap-1 justify-center">
                  {task.formKey && (
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={(e) => {
                        e.stopPropagation()
                        onOpenTaskForm(task)
                      }}
                      title={t('taskInbox.table.openTask', 'Abrir tarea')}
                      className="hover:scale-105 transition-transform duration-200"
                    >
                      <ExternalLink size={14} />
                    </Button>
                  )}
                  {!task.assignee && (
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={(e) => {
                        e.stopPropagation()
                        onClaimTask(task)
                      }}
                      title={t('taskInbox.table.claim', 'Reclamar')}
                      className="hover:scale-105 transition-transform duration-200 hover:bg-green-50 hover:text-green-700"
                    >
                      <Hand size={14} />
                    </Button>
                  )}
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={(e) => {
                      e.stopPropagation()
                      onAssignTask(task)
                    }}
                    title={t('taskInbox.table.assign', 'Asignar')}
                    className="hover:scale-105 transition-transform duration-200"
                  >
                    <UserPlus size={14} />
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}
