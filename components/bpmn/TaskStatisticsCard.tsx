// components/bpmn/TaskStatisticsCard.tsx - Estadísticas con Wow Factor
'use client'

import { TaskStatistics } from '@/types/bpmn'
import { useTranslation } from '@/app/config/i18n'
import { Card, CardContent } from '@/components/ui/card'
import { Inbox, User, Users } from 'lucide-react'

interface TaskStatisticsCardProps {
  statistics: TaskStatistics
}

export function TaskStatisticsCard({ statistics }: TaskStatisticsCardProps) {
  const { t } = useTranslation()

  const stats = [
    {
      id: 'total',
      titleKey: 'taskInbox.stats.totalTasks',
      titleFallback: 'Tareas Pendientes',
      value: statistics.totalTasks,
      icon: Inbox,
      color: '#3b82f6',
    },
    {
      id: 'assigned',
      titleKey: 'taskInbox.stats.assignedToMe',
      titleFallback: 'Asignadas a Mí',
      value: statistics.assignedToMeCount,
      icon: User,
      color: '#10b981',
    },
    {
      id: 'group',
      titleKey: 'taskInbox.stats.groupTasks',
      titleFallback: 'De Mis Roles',
      value: statistics.groupTasksCount,
      icon: Users,
      color: '#f59e0b',
    },
  ]

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      {stats.map((stat, index) => (
        <div
          key={stat.id}
          style={{
            animationDelay: `${index * 100}ms`,
            animation: 'fadeInUp 0.8s ease-out forwards',
          }}
          className="group relative overflow-hidden rounded-lg border border-border/50 bg-card/80 backdrop-blur-sm hover:bg-card/90 transition-all duration-500 hover:scale-105 hover:shadow-2xl"
        >
          {/* Efecto de brillo en hover */}
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-700 transform -skew-x-12 translate-x-[-100%] group-hover:translate-x-[100%]" />

          {/* Efecto de borde brillante */}
          <div
            className="absolute inset-0 rounded-lg opacity-0 group-hover:opacity-100 transition-opacity duration-500"
            style={{
              background: `linear-gradient(45deg, ${stat.color}40, transparent, ${stat.color}40)`,
              backgroundSize: '200% 200%',
              animation: 'shimmer 2s ease-in-out infinite',
            }}
          />

          <div className="relative z-10 p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-muted-foreground">
                  {t(stat.titleKey, stat.titleFallback)}
                </p>
                <p className="text-2xl font-bold text-foreground">
                  {stat.value}
                </p>
              </div>
              <div className="p-2 rounded-lg" style={{ backgroundColor: `${stat.color}20` }}>
                <stat.icon className="w-6 h-6" style={{ color: stat.color }} />
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  )
}
