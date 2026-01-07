// app/(app)/bpmn/forms/fria-authority-notification-form/page.tsx
// Migrado de: fria-authority-notification-form.zul
// Formulario para revisar y confirmar notificación a autoridad competente (Art. 27.3)
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { ArrowLeft, Send, Save, CheckCircle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function FriaAuthorityNotificationFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [notificationId, setNotificationId] = useState<string>('')
  const [notificationDate, setNotificationDate] = useState<string>('')
  const [authorityName, setAuthorityName] = useState<string>('')
  const [authorityEndpoint, setAuthorityEndpoint] = useState<string>('')
  const [notificationStatus, setNotificationStatus] = useState<string>('') // PENDING, SENT, FAILED
  const [notificationSummary, setNotificationSummary] = useState<string>('')
  const [confirmation, setConfirmation] = useState<boolean>(false)

  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Cargar datos del workflow
    if (taskId) {
      fetch(`/api/bpmn/tasks/${taskId}`)
        .then(res => res.json())
        .then(data => {
          if (data.variables) {
            setProjectId(data.variables.projectId || projectId)
            setNotificationId(data.variables.notificationId || '')
            setNotificationDate(data.variables.notificationDate || '')
            setAuthorityName(data.variables.authorityName || '')
            setAuthorityEndpoint(data.variables.authorityEndpoint || '')
            setNotificationStatus(data.variables.notificationStatus || 'PENDING')
            setNotificationSummary(data.variables.notificationSummary || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!confirmation) {
      newErrors.confirmation = t('bpmnForms.friaAuthorityNotificationForm.errors.confirmationRequired', 'Debe confirmar la notificación antes de continuar')
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
        projectId: projectId ? Number.parseInt(projectId, 10) : null,
        notificationConfirmed: true,
        notificationDate: new Date().toISOString(),
        submittedBy: 'current-user' // TODO: obtener del contexto cuando esté disponible
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
        alert(error.error || t('bpmnForms.friaAuthorityNotificationForm.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.friaAuthorityNotificationForm.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.friaAuthorityNotificationForm.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Send className="w-6 h-6 text-primary" />
            {t('bpmnForms.friaAuthorityNotificationForm.title', 'Notificación a Autoridad Competente - Art. 27.3')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.friaAuthorityNotificationForm.subtitle', 'Revise y confirme la notificación del FRIA a la autoridad competente')}
          </p>
        </div>
      </div>

      {/* Información de la notificación */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaAuthorityNotificationForm.infoTitle', 'Información de la Notificación')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Notification ID */}
          {notificationId && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.notificationId', 'ID de Notificación')}
              </Label>
              <Input
                value={notificationId}
                readOnly
                className="bg-gray-100"
              />
            </div>
          )}

          {/* Notification Date */}
          {notificationDate && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.notificationDate', 'Fecha de Notificación')}
              </Label>
              <Input
                value={new Date(notificationDate).toLocaleString()}
                readOnly
                className="bg-gray-100"
              />
            </div>
          )}

          {/* Authority Name */}
          {authorityName && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.authorityName', 'Autoridad Competente')}
              </Label>
              <Input
                value={authorityName}
                readOnly
                className="bg-gray-100"
              />
            </div>
          )}

          {/* Authority Endpoint */}
          {authorityEndpoint && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.authorityEndpoint', 'Endpoint de la Autoridad')}
              </Label>
              <Input
                value={authorityEndpoint}
                readOnly
                className="bg-gray-100"
              />
            </div>
          )}

          {/* Notification Status */}
          {notificationStatus && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.notificationStatus', 'Estado de la Notificación')}
              </Label>
              <div className={`inline-flex items-center px-3 py-1 rounded-full text-sm ${
                notificationStatus === 'SENT' ? 'bg-green-100 text-green-800' :
                notificationStatus === 'FAILED' ? 'bg-red-100 text-red-800' :
                'bg-yellow-100 text-yellow-800'
              }`}>
                {notificationStatus === 'SENT' && <CheckCircle size={16} className="mr-2" />}
                {t(`bpmnForms.friaAuthorityNotificationForm.status.${notificationStatus}`, notificationStatus)}
              </div>
            </div>
          )}

          {/* Notification Summary */}
          {notificationSummary && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAuthorityNotificationForm.notificationSummary', 'Resumen de la Notificación')}
              </Label>
              <Textarea
                value={notificationSummary}
                readOnly
                rows={6}
                className="bg-gray-100"
              />
            </div>
          )}
        </CardContent>
      </Card>

      {/* Formulario de confirmación */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaAuthorityNotificationForm.formTitle', 'Confirmación de Notificación')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Confirmation */}
            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="confirmation"
                  checked={confirmation}
                  onChange={(e) => setConfirmation(e.target.checked)}
                  className="rounded border-gray-300"
                />
                <Label htmlFor="confirmation" className="cursor-pointer">
                  {t('bpmnForms.friaAuthorityNotificationForm.confirmation', 'Confirmo que he revisado la notificación y estoy de acuerdo con su envío a la autoridad competente según Art. 27.3 del EU AI Act')} *
                </Label>
              </div>
              {errors.confirmation && (
                <p className="text-sm text-red-500">{errors.confirmation}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.friaAuthorityNotificationForm.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading || !confirmation}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.friaAuthorityNotificationForm.saving', 'Enviando...')
                  : t('bpmnForms.friaAuthorityNotificationForm.submit', 'Confirmar Notificación')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function FriaAuthorityNotificationFormPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <FriaAuthorityNotificationFormContent />
    </Suspense>
  )
}
