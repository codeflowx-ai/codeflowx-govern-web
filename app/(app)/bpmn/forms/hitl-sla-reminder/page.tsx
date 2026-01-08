// app/(app)/bpmn/forms/hitl-sla-reminder/page.tsx
// Migrado de: hitl-sla-reminder-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, AlertCircle, Save, Clock, ExternalLink, Calendar } from 'lucide-react'
import { useParams, useSearchParams, useRouter } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'
import { mockHitlSlaReminder } from '@/app/(app)/governance/data/mockHitl'
import { Badge } from '@/components/ui/badge'

function HitlSlaReminderFormContent() {
  const { t } = useTranslation()
  const router = useRouter()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [interventionData, setInterventionData] = useState(mockHitlSlaReminder)
  const [agentName, setAgentName] = useState<string>(mockHitlSlaReminder.entityName)
  const [elapsedTime, setElapsedTime] = useState<string>('')
  const [slaAction, setSlaAction] = useState<string>('')
  const [slaNotes, setSlaNotes] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Calcular tiempo transcurrido
    const createdAt = new Date(interventionData.createdAt)
    const now = new Date()
    const hours = Math.floor((now.getTime() - createdAt.getTime()) / (1000 * 60 * 60))
    const minutes = Math.floor(((now.getTime() - createdAt.getTime()) % (1000 * 60 * 60)) / (1000 * 60))
    setElapsedTime(`${hours}h ${minutes}m`)
  }, [interventionData])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!slaAction || slaAction.trim() === '') {
      newErrors.slaAction = t('hitlSlaReminder.errors.actionRequired', 'Debe seleccionar una acción')
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!validateForm()) {
      return
    }

    setLoading(true)
    try {
      const variables = {
        sla_action: slaAction,
        sla_notes: slaNotes.trim(),
        sla_handler: 'current-user', // TODO: obtener del contexto cuando esté disponible
        sla_timestamp: new Date().toISOString(),
        projectId: projectIdParam ? Number.parseInt(projectIdParam, 10) : null,
      }

      const response = await fetch(`/api/bpmn/tasks/${taskId}/complete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ variables })
      })

      if (response.ok) {
        if (globalThis.window !== undefined) {
          globalThis.window.location.href = '/bpmn/task-inbox'
        }
      } else {
        const error = await response.json()
        alert(error.error || t('hitlSlaReminder.errors.submitError', 'Error al procesar la acción'))
      }
    } catch (error) {
      console.error('Error procesando acción:', error)
      alert(t('hitlSlaReminder.errors.submitError', 'Error al procesar la acción'))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  const getSlaActionLabel = (action: string): string => {
    switch (action) {
      case 'escalate':
        return t('hitlSlaReminder.escalate', 'Escalar a superior inmediato')
      case 'extend':
        return t('hitlSlaReminder.extend', 'Extender plazo (24h más)')
      case 'complete_now':
        return t('hitlSlaReminder.completeNow', 'Completar ahora (asumir tarea)')
      default:
        return action
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('hitlSlaReminder.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <AlertCircle className="w-6 h-6 text-orange-600" />
            {t('hitlSlaReminder.title', 'SLA Excedido - Revisión Humana Pendiente')}
          </h1>
          <p className="text-muted-foreground">
            {t('hitlSlaReminder.subtitle', 'La tarea de revisión humana lleva más de 24 horas sin completarse')}
          </p>
        </div>
      </div>

      {/* Alert Section */}
      <Card
        style={{
          backgroundColor: "var(--theme-warning)",
          borderColor: "var(--theme-warning)",
        }}
      >
        <CardContent className="pt-6">
          <div className="flex items-center gap-4">
            <AlertCircle
              className="w-8 h-8"
              style={{ color: "var(--theme-warning)" }}
            />
            <div>
              <h3
                className="text-lg font-bold"
                style={{ color: "var(--theme-warning)" }}
              >
                {t('hitlSlaReminder.alertTitle', 'SLA Excedido')}
              </h3>
              <p
                className="text-sm"
                style={{ color: "var(--theme-text-secondary)" }}
              >
                {t('hitlSlaReminder.alertMessage', 'Se requiere acción inmediata')}
              </p>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Información de la Intervención */}
      <Card
        style={{
          background: "linear-gradient(to bottom right, var(--theme-warning), var(--theme-error))",
          borderColor: "var(--theme-warning)",
        }}
      >
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <AlertCircle className="w-5 h-5 text-orange-600" />
            {t('hitlSlaReminder.interventionInfo', 'Información de la Intervención')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.entityType', 'Tipo de Entidad')}</Label>
              <Input value={interventionData.entityType} readOnly className="bg-muted font-bold" />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.entityName', 'Nombre de Entidad')}</Label>
              <Input value={interventionData.entityName} readOnly className="bg-muted font-bold" />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.supervisionType', 'Tipo de Supervisión')}</Label>
              <Input value={interventionData.supervisionType} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.slaHours', 'SLA (horas)')}</Label>
              <Input value={`${interventionData.slaHours}h`} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.createdAt', 'Creado')}</Label>
              <Input
                value={new Date(interventionData.createdAt).toLocaleString()}
                readOnly
                className="bg-muted"
              />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.slaDeadline', 'SLA Deadline')}</Label>
              <Input
                value={new Date(interventionData.slaDeadline).toLocaleString()}
                readOnly
                className="bg-muted text-orange-600 font-bold"
              />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.timeRemaining', 'Tiempo Restante')}</Label>
              <Input
                value={`${interventionData.timeRemaining.toFixed(1)}h`}
                readOnly
                className={`bg-muted font-bold ${
                  interventionData.timeRemaining < 1
                    ? 'text-red-600'
                    : interventionData.timeRemaining < 2
                    ? 'text-orange-600'
                    : 'text-green-600'
                }`}
              />
            </div>
            <div>
              <Label className="font-semibold">{t('hitlSlaReminder.urgency', 'Urgencia')}</Label>
              <div className="mt-2">
                <Badge
                  variant={
                    interventionData.urgency === 'CRITICAL'
                      ? 'danger'
                      : interventionData.urgency === 'HIGH'
                      ? 'primary'
                      : 'outline'
                  }
                  className={
                    interventionData.urgency === 'HIGH'
                      ? 'bg-orange-500'
                      : interventionData.urgency === 'MEDIUM'
                      ? 'bg-yellow-500'
                      : ''
                  }
                >
                  {interventionData.urgency}
                </Badge>
              </div>
            </div>
          </div>

          {/* Acciones rápidas */}
          <div className="flex gap-2 pt-4 border-t">
            <Button
              variant="outline"
              size="sm"
              onClick={() => {
                router.push(`/governance/compliance/hitl-supervision?interventionId=${interventionData.interventionId}`)
              }}
            >
              <ExternalLink className="h-4 w-4 mr-2" />
              {t('hitlSlaReminder.goToIntervention', 'Ir a Intervención')}
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => {
                // TODO: Implementar extensión de SLA
                alert(t('hitlSlaReminder.extendSla', 'Extender SLA'))
              }}
            >
              <Calendar className="h-4 w-4 mr-2" />
              {t('hitlSlaReminder.extendSla', 'Extender SLA')}
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('hitlSlaReminder.actionTitle', 'Acción a Tomar')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Acción SLA */}
            <div className="space-y-2">
              <Label className="font-semibold">
                {t('hitlSlaReminder.action', 'Acción')} *
              </Label>
              <div className="space-y-3">
                <label className="flex items-center gap-2 cursor-pointer p-3 border rounded-md hover:bg-gray-50 dark:hover:bg-gray-800">
                  <input
                    type="radio"
                    name="slaAction"
                    value="escalate"
                    checked={slaAction === 'escalate'}
                    onChange={(e) => setSlaAction(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span>{t('hitlSlaReminder.escalate', 'Escalar a superior inmediato')}</span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer p-3 border rounded-md hover:bg-gray-50 dark:hover:bg-gray-800">
                  <input
                    type="radio"
                    name="slaAction"
                    value="extend"
                    checked={slaAction === 'extend'}
                    onChange={(e) => setSlaAction(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span>{t('hitlSlaReminder.extend', 'Extender plazo (24h más)')}</span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer p-3 border rounded-md hover:bg-gray-50 dark:hover:bg-gray-800">
                  <input
                    type="radio"
                    name="slaAction"
                    value="complete_now"
                    checked={slaAction === 'complete_now'}
                    onChange={(e) => setSlaAction(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span>{t('hitlSlaReminder.completeNow', 'Completar ahora (asumir tarea)')}</span>
                </label>
              </div>
              {errors.slaAction && (
                <p className="text-sm text-destructive">{errors.slaAction}</p>
              )}
            </div>

            {/* Notas */}
            <div className="space-y-2">
              <Label htmlFor="slaNotes" className="font-semibold">
                {t('hitlSlaReminder.notes', 'Notas')}
              </Label>
              <textarea
                id="slaNotes"
                value={slaNotes}
                onChange={(e) => setSlaNotes(e.target.value)}
                rows={3}
                placeholder={t('hitlSlaReminder.notesPlaceholder', 'Explica la acción tomada...')}
                className="w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
              />
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('hitlSlaReminder.cancel', 'Cerrar')}
              </Button>
              <Button type="submit" disabled={loading || !slaAction} className="bg-orange-600 hover:bg-orange-700">
                <Save size={16} className="mr-2" />
                {loading
                  ? t('hitlSlaReminder.saving', 'Guardando...')
                  : t('hitlSlaReminder.submit', 'Confirmar Acción')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function HitlSlaReminderPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <HitlSlaReminderFormContent />
    </Suspense>
  )
}
