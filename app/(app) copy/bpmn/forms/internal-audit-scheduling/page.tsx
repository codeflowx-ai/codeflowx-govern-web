// app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx
// Migrado de: internal-audit-scheduling-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, Calendar, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function InternalAuditSchedulingFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [auditDate, setAuditDate] = useState<string>(() => {
    // Fecha por defecto: 14 días desde hoy a las 9:00
    const date = new Date()
    date.setDate(date.getDate() + 14)
    date.setHours(9, 0, 0, 0)
    return date.toISOString().slice(0, 16) // Formato YYYY-MM-DDTHH:mm
  })
  const [auditScope, setAuditScope] = useState<string>('')
  const [auditor, setAuditor] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!auditScope || auditScope.trim() === '') {
      newErrors.auditScope = t('internalAuditScheduling.errors.scopeRequired', 'Debe definir el alcance de la auditoría')
    }

    if (!auditor || auditor.trim() === '') {
      newErrors.auditor = t('internalAuditScheduling.errors.auditorRequired', 'Debe asignar un auditor responsable')
    }

    if (!auditDate) {
      newErrors.auditDate = t('internalAuditScheduling.errors.dateRequired', 'Debe seleccionar una fecha')
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
        auditScheduledDate: new Date(auditDate).toISOString(),
        auditScope: auditScope.trim(),
        auditAuditor: auditor.trim(),
        projectId: projectId ? Number.parseInt(projectId, 10) : null,
        scheduledBy: 'current-user' // Obtener del contexto de autenticación cuando esté disponible
      }

      const response = await fetch(`/api/bpmn/tasks/${taskId}/complete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ variables })
      })

      if (response.ok) {
        // Redirigir a la bandeja de tareas
        if (globalThis.window !== undefined) {
          globalThis.window.location.href = '/bpmn/task-inbox'
        }
      } else {
        const error = await response.json()
        alert(error.error || t('internalAuditScheduling.errors.submitError', 'Error al planificar la auditoría'))
      }
    } catch (error) {
      console.error('Error planificando auditoría:', error)
      alert(t('internalAuditScheduling.errors.submitError', 'Error al planificar la auditoría'))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('internalAuditScheduling.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Calendar className="w-6 h-6 text-primary" />
            {t('internalAuditScheduling.title', 'Programar auditoría interna ISO 42001')}
          </h1>
          <p className="text-muted-foreground">
            {t('internalAuditScheduling.subtitle', 'Complete los datos para programar la auditoría interna')}
          </p>
        </div>
      </div>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('internalAuditScheduling.formTitle', 'Datos de la auditoría')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Proyecto */}
            <div className="space-y-2">
              <Label htmlFor="projectId" className="font-semibold">
                {t('internalAuditScheduling.project', 'Proyecto')}
              </Label>
              <Input
                id="projectId"
                type="text"
                value={projectId}
                onChange={(e) => setProjectId(e.target.value)}
                readOnly
                placeholder={t('internalAuditScheduling.projectPlaceholder', 'Project ID')}
                className="bg-muted"
              />
            </div>

            {/* Fecha programada */}
            <div className="space-y-2">
              <Label htmlFor="auditDate" className="font-semibold">
                {t('internalAuditScheduling.date', 'Fecha programada')}
              </Label>
              <Input
                id="auditDate"
                type="datetime-local"
                value={auditDate}
                onChange={(e) => setAuditDate(e.target.value)}
                className={errors.auditDate ? 'border-destructive' : ''}
              />
              {errors.auditDate && (
                <p className="text-sm text-destructive">{errors.auditDate}</p>
              )}
            </div>

            {/* Alcance */}
            <div className="space-y-2">
              <Label htmlFor="auditScope" className="font-semibold">
                {t('internalAuditScheduling.scope', 'Alcance')}
              </Label>
              <textarea
                id="auditScope"
                value={auditScope}
                onChange={(e) => setAuditScope(e.target.value)}
                rows={4}
                placeholder={t('internalAuditScheduling.scopePlaceholder', 'Cláusulas, procesos y sistemas a auditar')}
                className={`w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 ${errors.auditScope ? 'border-destructive' : 'border-gray-300 dark:border-gray-700'}`}
              />
              {errors.auditScope && (
                <p className="text-sm text-destructive">{errors.auditScope}</p>
              )}
            </div>

            {/* Auditor responsable */}
            <div className="space-y-2">
              <Label htmlFor="auditor" className="font-semibold">
                {t('internalAuditScheduling.auditor', 'Auditor responsable')}
              </Label>
              <Input
                id="auditor"
                type="text"
                value={auditor}
                onChange={(e) => setAuditor(e.target.value)}
                placeholder={t('internalAuditScheduling.auditorPlaceholder', 'Usuario o rol auditor')}
                className={errors.auditor ? 'border-destructive' : ''}
              />
              {errors.auditor && (
                <p className="text-sm text-destructive">{errors.auditor}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('internalAuditScheduling.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('internalAuditScheduling.saving', 'Guardando...')
                  : t('internalAuditScheduling.submit', 'Planificar auditoría')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function InternalAuditSchedulingPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <InternalAuditSchedulingFormContent />
    </Suspense>
  )
}



