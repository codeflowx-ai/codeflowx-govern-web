// components/bpmn/TaskFiltersBar.tsx - Filtros con Wow Factor
'use client'

import { TaskFilters, TaskPriority, TaskState } from '@/types/bpmn'
import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { X } from 'lucide-react'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Card, CardContent } from '@/components/ui/card'

interface TaskFiltersBarProps {
  filters: TaskFilters
  onFiltersChange: (filters: TaskFilters) => void
  onClearFilters: () => void
  processDefinitions: string[]
}

export function TaskFiltersBar({
  filters,
  onFiltersChange,
  onClearFilters,
  processDefinitions
}: TaskFiltersBarProps) {
  const { t } = useTranslation()
  const priorities: TaskPriority[] = ['low', 'normal', 'high', 'urgent']
  const states: TaskState[] = ['all', 'assigned', 'unassigned', 'candidate']

  const handleProcessChange = (value: string) => {
    onFiltersChange({ ...filters, process: value === 'all' ? undefined : value })
  }

  const handlePriorityChange = (value: string) => {
    onFiltersChange({ ...filters, priority: value === 'all' ? undefined : value as TaskPriority })
  }

  const handleStateChange = (value: string) => {
    onFiltersChange({ ...filters, state: value === 'all' ? undefined : value as TaskState })
  }

  const hasActiveFilters = filters.process || filters.priority || filters.state

  return (
    <Card className="backdrop-blur-md bg-background/60 border border-border/50">
      <CardContent className="p-4">
        <div className="flex flex-col sm:flex-row gap-4 items-center">
          {/* Filtro de Proceso */}
          <Select
            value={filters.process || 'all'}
            onValueChange={handleProcessChange}
          >
            <SelectTrigger className="w-auto min-w-[200px]">
              <SelectValue placeholder={t('taskInbox.filters.process', 'Proceso...')} />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">{t('taskInbox.filters.allProcesses', 'Todos los procesos')}</SelectItem>
              {processDefinitions.map(proc => (
                <SelectItem key={proc} value={proc}>{proc}</SelectItem>
              ))}
            </SelectContent>
          </Select>

          {/* Filtro de Prioridad */}
          <Select
            value={filters.priority || 'all'}
            onValueChange={handlePriorityChange}
          >
            <SelectTrigger className="w-auto min-w-[150px]">
              <SelectValue placeholder={t('taskInbox.filters.priority', 'Prioridad...')} />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">{t('taskInbox.filters.allPriorities', 'Todas las prioridades')}</SelectItem>
              {priorities.map(priority => (
                <SelectItem key={priority} value={priority}>
                  {t(`taskInbox.filters.${priority}`, priority === 'low' ? 'Baja' : priority === 'normal' ? 'Normal' : priority === 'high' ? 'Alta' : 'Urgente')}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>

          {/* Filtro de Estado */}
          <Select
            value={filters.state || 'all'}
            onValueChange={handleStateChange}
          >
            <SelectTrigger className="w-auto min-w-[150px]">
              <SelectValue placeholder={t('taskInbox.filters.state', 'Estado...')} />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">{t('taskInbox.filters.allStates', 'Todos los estados')}</SelectItem>
              {states.filter(s => s !== 'all').map(state => (
                <SelectItem key={state} value={state}>
                  {t(`taskInbox.filters.${state}`, state === 'assigned' ? 'Asignadas' : state === 'unassigned' ? 'Sin asignar' : 'Candidatas')}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>

          {/* Botón limpiar filtros */}
          {hasActiveFilters && (
            <Button
              variant="outline"
              size="sm"
              onClick={onClearFilters}
              className="hover:scale-105 transition-transform duration-200"
            >
              <X size={16} className="mr-1" />
              {t('taskInbox.actions.clearFilters', 'Limpiar')}
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
