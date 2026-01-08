// app/(app)/bpmn/forms/initiate-conformity-assessment-form/page.tsx
// Migrado de: initiate-conformity-assessment-form.zul
// Formulario para iniciar evaluación de conformidad según Art. 43 / Anexo VI del EU AI Act
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { ArrowLeft, Play, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function InitiateConformityAssessmentFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [assessmentType, setAssessmentType] = useState<string>('') // SELF, NOTIFIED_BODY
  const [justification, setJustification] = useState<string>('')

  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Cargar datos del workflow si están disponibles
    if (taskId) {
      fetch(`/api/bpmn/tasks/${taskId}`)
        .then(res => res.json())
        .then(data => {
          if (data.variables) {
            setProjectId(data.variables.projectId || projectId)
            setAssessmentType(data.variables.assessmentType || '')
            setJustification(data.variables.justification || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!projectId || projectId.trim() === '') {
      newErrors.projectId = t('bpmnForms.initiateConformityAssessment.errors.projectIdRequired', 'El ID del proyecto es requerido')
    }

    if (!assessmentType) {
      newErrors.assessmentType = t('bpmnForms.initiateConformityAssessment.errors.assessmentTypeRequired', 'El tipo de evaluación es requerido')
    }

    if (!justification || justification.trim() === '') {
      newErrors.justification = t('bpmnForms.initiateConformityAssessment.errors.justificationRequired', 'La justificación es requerida')
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
        assessmentType: assessmentType, // SELF, NOTIFIED_BODY
        justification: justification,
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
        alert(error.error || t('bpmnForms.initiateConformityAssessment.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.initiateConformityAssessment.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.initiateConformityAssessment.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Play className="w-6 h-6 text-primary" />
            {t('bpmnForms.initiateConformityAssessment.title', 'Iniciar Evaluación de Conformidad')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.initiateConformityAssessment.subtitle', 'Inicie la evaluación de conformidad según Art. 43 / Anexo VI del EU AI Act')}
          </p>
        </div>
      </div>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.initiateConformityAssessment.formTitle', 'Datos de la Evaluación')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Project ID */}
            <div className="space-y-2">
              <Label htmlFor="projectId">
                {t('bpmnForms.initiateConformityAssessment.projectId', 'ID del Proyecto')} *
              </Label>
              <Input
                id="projectId"
                value={projectId}
                onChange={(e) => setProjectId(e.target.value)}
                placeholder={t('bpmnForms.initiateConformityAssessment.projectIdPlaceholder', 'Ingrese el ID del proyecto')}
                className={errors.projectId ? 'border-red-500' : ''}
              />
              {errors.projectId && (
                <p className="text-sm text-red-500">{errors.projectId}</p>
              )}
            </div>

            {/* Assessment Type */}
            <div className="space-y-2">
              <Label htmlFor="assessmentType">
                {t('bpmnForms.initiateConformityAssessment.assessmentType', 'Tipo de Evaluación')} *
              </Label>
              <Select value={assessmentType} onValueChange={setAssessmentType}>
                <SelectTrigger className={errors.assessmentType ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('bpmnForms.initiateConformityAssessment.assessmentTypePlaceholder', 'Seleccione el tipo de evaluación')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="SELF">
                    {t('bpmnForms.initiateConformityAssessment.typeSelf', 'Autoevaluación (Self-Assessment)')}
                  </SelectItem>
                  <SelectItem value="NOTIFIED_BODY">
                    {t('bpmnForms.initiateConformityAssessment.typeNotifiedBody', 'Organismo Notificado (Notified Body)')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.assessmentType && (
                <p className="text-sm text-red-500">{errors.assessmentType}</p>
              )}
              <p className="text-sm text-muted-foreground">
                {t('bpmnForms.initiateConformityAssessment.assessmentTypeHelp', 'Seleccione si la evaluación será realizada internamente (SELF) o por un organismo notificado (NOTIFIED_BODY)')}
              </p>
            </div>

            {/* Justification */}
            <div className="space-y-2">
              <Label htmlFor="justification">
                {t('bpmnForms.initiateConformityAssessment.justification', 'Justificación')} *
              </Label>
              <Textarea
                id="justification"
                value={justification}
                onChange={(e) => setJustification(e.target.value)}
                placeholder={t('bpmnForms.initiateConformityAssessment.justificationPlaceholder', 'Explique la razón para iniciar esta evaluación de conformidad')}
                rows={5}
                className={errors.justification ? 'border-red-500' : ''}
              />
              {errors.justification && (
                <p className="text-sm text-red-500">{errors.justification}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.initiateConformityAssessment.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.initiateConformityAssessment.saving', 'Iniciando...')
                  : t('bpmnForms.initiateConformityAssessment.submit', 'Iniciar Evaluación')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function InitiateConformityAssessmentFormPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <InitiateConformityAssessmentFormContent />
    </Suspense>
  )
}



