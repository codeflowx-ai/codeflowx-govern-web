// app/(app)/bpmn/forms/model-ml-review/page.tsx
// Migrado de: plataforma/workflow/model-ml-review-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { ArrowLeft, Code, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function ModelMlReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const modelIdParam = searchParams.get('modelId')

  // Estado del formulario
  const [mlApproval, setMlApproval] = useState<string>('')
  const [reviewNotes, setReviewNotes] = useState<string>('')
  const [modelName, setModelName] = useState<string>('')
  const [performanceScore, setPerformanceScore] = useState<number>(0)
  const [biasScore, setBiasScore] = useState<number>(0)
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // TODO: Cargar información del modelo y resultados de validaciones automáticas
    // Cargar performanceScore, biasScore, complianceScore desde las variables del workflow
  }, [taskId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!mlApproval) {
      newErrors.mlApproval = t('modelMlReview.errors.mlApprovalRequired', 'La decisión de aprobación es requerida')
    }

    if (mlApproval && mlApproval !== 'APPROVED' && (!reviewNotes || reviewNotes.trim() === '')) {
      newErrors.reviewNotes = t('modelMlReview.errors.reviewNotesRequired', 'Las notas de revisión son requeridas cuando no se aprueba')
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
        mlApproval: mlApproval, // APPROVED, REJECTED, NEEDS_CHANGES
        reviewNotes: reviewNotes,
        reviewedBy: 'current-user' // TODO: obtener del contexto cuando esté disponible
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
        alert(error.error || t('modelMlReview.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('modelMlReview.errors.submitError', 'Error al procesar el formulario'))
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
          {t('modelMlReview.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Code className="w-6 h-6 text-primary" />
            {t('modelMlReview.title', 'Revisión Técnica ML Engineer')}
          </h1>
          <p className="text-muted-foreground">
            {t('modelMlReview.subtitle', 'Revise técnicamente el modelo y proporcione su evaluación')}
          </p>
        </div>
      </div>

      {/* Información del Modelo y Métricas */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>
            {t('modelMlReview.modelInfo', 'Información del Modelo')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelMlReview.modelName', 'Nombre del Modelo')}
              </Label>
              <p className="font-medium">{modelName || '-'}</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelMlReview.modelId', 'ID del Modelo')}
              </Label>
              <p className="font-medium">{modelIdParam || '-'}</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-4 border-t">
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelMlReview.performanceScore', 'Score de Rendimiento')}
              </Label>
              <p className="text-2xl font-bold">{performanceScore}/100</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelMlReview.biasScore', 'Score de Sesgo')}
              </Label>
              <p className="text-2xl font-bold">{biasScore}/100</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelMlReview.complianceScore', 'Score de Cumplimiento')}
              </Label>
              <p className="text-2xl font-bold">{complianceScore}/100</p>
            </div>
          </div>
        </CardContent>
      </Card>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('modelMlReview.formTitle', 'Evaluación Técnica')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* ML Approval Decision */}
            <div className="space-y-2">
              <Label htmlFor="mlApproval">
                {t('modelMlReview.mlApproval', 'Decisión de Aprobación')} *
              </Label>
              <Select value={mlApproval} onValueChange={setMlApproval}>
                <SelectTrigger className={errors.mlApproval ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('modelMlReview.mlApprovalPlaceholder', 'Seleccione su decisión')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="APPROVED">
                    {t('modelMlReview.approved', 'Aprobar')}
                  </SelectItem>
                  <SelectItem value="REJECTED">
                    {t('modelMlReview.rejected', 'Rechazar')}
                  </SelectItem>
                  <SelectItem value="NEEDS_CHANGES">
                    {t('modelMlReview.needsChanges', 'Requiere Cambios')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.mlApproval && (
                <p className="text-sm text-red-500">{errors.mlApproval}</p>
              )}
            </div>

            {/* Review Notes */}
            <div className="space-y-2">
              <Label htmlFor="reviewNotes">
                {t('modelMlReview.reviewNotes', 'Notas de Revisión')}
                {mlApproval && mlApproval !== 'APPROVED' && ' *'}
              </Label>
              <Textarea
                id="reviewNotes"
                value={reviewNotes}
                onChange={(e) => setReviewNotes(e.target.value)}
                placeholder={t('modelMlReview.reviewNotesPlaceholder', 'Proporcione detalles técnicos sobre su evaluación del modelo, métricas observadas, consideraciones de rendimiento, etc.')}
                rows={8}
                className={errors.reviewNotes ? 'border-red-500' : ''}
              />
              {errors.reviewNotes && (
                <p className="text-sm text-red-500">{errors.reviewNotes}</p>
              )}
              <p className="text-sm text-muted-foreground">
                {t('modelMlReview.reviewNotesHint', 'Las notas son obligatorias cuando se rechaza o se requieren cambios')}
              </p>
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('modelMlReview.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('modelMlReview.saving', 'Guardando...')
                  : t('modelMlReview.submit', 'Enviar Revisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ModelMlReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ModelMlReviewFormContent />
    </Suspense>
  )
}
