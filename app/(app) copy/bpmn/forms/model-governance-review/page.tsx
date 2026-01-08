// app/(app)/bpmn/forms/model-governance-review/page.tsx
// Migrado de: plataforma/workflow/model-governance-review-form.zul
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
import { ArrowLeft, Shield, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function ModelGovernanceReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const modelIdParam = searchParams.get('modelId')

  // Estado del formulario
  const [governanceApproval, setGovernanceApproval] = useState<string>('')
  const [riskAssessment, setRiskAssessment] = useState<string>('')
  const [reviewNotes, setReviewNotes] = useState<string>('')
  const [modelName, setModelName] = useState<string>('')
  const [performanceScore, setPerformanceScore] = useState<number>(0)
  const [biasScore, setBiasScore] = useState<number>(0)
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [mlEngineerApproval, setMlEngineerApproval] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // TODO: Cargar información del modelo, resultados de validaciones y decisión del ML Engineer
    // Cargar performanceScore, biasScore, complianceScore, mlEngineerApproval desde las variables del workflow
  }, [taskId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!governanceApproval) {
      newErrors.governanceApproval = t('modelGovernanceReview.errors.governanceApprovalRequired', 'La decisión de governance es requerida')
    }

    if (!riskAssessment) {
      newErrors.riskAssessment = t('modelGovernanceReview.errors.riskAssessmentRequired', 'La evaluación de riesgo es requerida')
    }

    if (governanceApproval && governanceApproval !== 'APPROVED' && (!reviewNotes || reviewNotes.trim() === '')) {
      newErrors.reviewNotes = t('modelGovernanceReview.errors.reviewNotesRequired', 'Las notas de revisión son requeridas cuando no se aprueba')
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
        governanceApproval: governanceApproval, // APPROVED, REJECTED, CONDITIONAL
        riskAssessment: riskAssessment, // LOW, MEDIUM, HIGH
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
        alert(error.error || t('modelGovernanceReview.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('modelGovernanceReview.errors.submitError', 'Error al procesar el formulario'))
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
          {t('modelGovernanceReview.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Shield className="w-6 h-6 text-primary" />
            {t('modelGovernanceReview.title', 'Revisión de Governance')}
          </h1>
          <p className="text-muted-foreground">
            {t('modelGovernanceReview.subtitle', 'Revise aspectos de governance: ética, riesgos y cumplimiento regulatorio')}
          </p>
        </div>
      </div>

      {/* Información del Modelo y Evaluaciones Previas */}
      <Card className="border-2">
        <CardHeader>
          <CardTitle>
            {t('modelGovernanceReview.modelInfo', 'Información del Modelo y Evaluaciones')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.modelName', 'Nombre del Modelo')}
              </Label>
              <p className="font-medium">{modelName || '-'}</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.modelId', 'ID del Modelo')}
              </Label>
              <p className="font-medium">{modelIdParam || '-'}</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4 pt-4 border-t">
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.performanceScore', 'Score de Rendimiento')}
              </Label>
              <p className="text-2xl font-bold">{performanceScore}/100</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.biasScore', 'Score de Sesgo')}
              </Label>
              <p className="text-2xl font-bold">{biasScore}/100</p>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.complianceScore', 'Score de Cumplimiento')}
              </Label>
              <p className="text-2xl font-bold">{complianceScore}/100</p>
            </div>
          </div>

          <div className="pt-4 border-t">
            <Label className="text-sm text-muted-foreground">
              {t('modelGovernanceReview.mlEngineerDecision', 'Decisión del ML Engineer')}
            </Label>
            <p className="font-medium mt-1">
              {mlEngineerApproval ? t(`modelGovernanceReview.mlApproval.${mlEngineerApproval}`, mlEngineerApproval) : '-'}
            </p>
          </div>
        </CardContent>
      </Card>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('modelGovernanceReview.formTitle', 'Evaluación de Governance')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Governance Approval Decision */}
            <div className="space-y-2">
              <Label htmlFor="governanceApproval">
                {t('modelGovernanceReview.governanceApproval', 'Decisión de Aprobación')} *
              </Label>
              <Select value={governanceApproval} onValueChange={setGovernanceApproval}>
                <SelectTrigger className={errors.governanceApproval ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('modelGovernanceReview.governanceApprovalPlaceholder', 'Seleccione su decisión')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="APPROVED">
                    {t('modelGovernanceReview.approved', 'Aprobar')}
                  </SelectItem>
                  <SelectItem value="REJECTED">
                    {t('modelGovernanceReview.rejected', 'Rechazar')}
                  </SelectItem>
                  <SelectItem value="CONDITIONAL">
                    {t('modelGovernanceReview.conditional', 'Aprobación Condicional')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.governanceApproval && (
                <p className="text-sm text-red-500">{errors.governanceApproval}</p>
              )}
            </div>

            {/* Risk Assessment */}
            <div className="space-y-2">
              <Label htmlFor="riskAssessment">
                {t('modelGovernanceReview.riskAssessment', 'Evaluación de Riesgo')} *
              </Label>
              <Select value={riskAssessment} onValueChange={setRiskAssessment}>
                <SelectTrigger className={errors.riskAssessment ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('modelGovernanceReview.riskAssessmentPlaceholder', 'Seleccione el nivel de riesgo')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="LOW">
                    {t('modelGovernanceReview.riskLow', 'Bajo')}
                  </SelectItem>
                  <SelectItem value="MEDIUM">
                    {t('modelGovernanceReview.riskMedium', 'Medio')}
                  </SelectItem>
                  <SelectItem value="HIGH">
                    {t('modelGovernanceReview.riskHigh', 'Alto')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.riskAssessment && (
                <p className="text-sm text-red-500">{errors.riskAssessment}</p>
              )}
              <p className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.riskAssessmentHint', 'Evalúe el riesgo considerando aspectos éticos, regulatorios y de impacto')}
              </p>
            </div>

            {/* Review Notes */}
            <div className="space-y-2">
              <Label htmlFor="reviewNotes">
                {t('modelGovernanceReview.reviewNotes', 'Notas de Revisión')}
                {governanceApproval && governanceApproval !== 'APPROVED' && ' *'}
              </Label>
              <Textarea
                id="reviewNotes"
                value={reviewNotes}
                onChange={(e) => setReviewNotes(e.target.value)}
                placeholder={t('modelGovernanceReview.reviewNotesPlaceholder', 'Proporcione detalles sobre aspectos de governance: cumplimiento regulatorio, consideraciones éticas, evaluación de riesgos, recomendaciones de monitoreo, etc.')}
                rows={8}
                className={errors.reviewNotes ? 'border-red-500' : ''}
              />
              {errors.reviewNotes && (
                <p className="text-sm text-red-500">{errors.reviewNotes}</p>
              )}
              <p className="text-sm text-muted-foreground">
                {t('modelGovernanceReview.reviewNotesHint', 'Las notas son obligatorias cuando se rechaza o se requiere aprobación condicional')}
              </p>
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('modelGovernanceReview.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('modelGovernanceReview.saving', 'Guardando...')
                  : t('modelGovernanceReview.submit', 'Enviar Revisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ModelGovernanceReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ModelGovernanceReviewFormContent />
    </Suspense>
  )
}



