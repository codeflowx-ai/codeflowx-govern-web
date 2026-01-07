// app/(app)/bpmn/forms/approve-conformity-assessment/page.tsx
// Migrado de: approve-conformity-assessment-form.zul
// Formulario para aprobar o rechazar la evaluación de conformidad
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
import { ArrowLeft, Shield, Save, FileText, CheckCircle, XCircle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function ApproveConformityAssessmentFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [overallScore, setOverallScore] = useState<number>(0)
  const [reportUrl, setReportUrl] = useState<string>('')
  const [decision, setDecision] = useState<string>('') // APPROVE, REJECT
  const [approvalComments, setApprovalComments] = useState<string>('')
  const [rejectionReason, setRejectionReason] = useState<string>('')

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
            setOverallScore(data.variables.overallScore || 0)
            setReportUrl(data.variables.reportUrl || '')
            setDecision(data.variables.decision || '')
            setApprovalComments(data.variables.approvalComments || '')
            setRejectionReason(data.variables.rejectionReason || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!decision) {
      newErrors.decision = t('bpmnForms.approveConformityAssessment.errors.decisionRequired', 'La decisión es requerida')
    }

    if (decision === 'APPROVE' && (!approvalComments || approvalComments.trim() === '')) {
      newErrors.approvalComments = t('bpmnForms.approveConformityAssessment.errors.approvalCommentsRequired', 'Los comentarios de aprobación son requeridos')
    }

    if (decision === 'REJECT' && (!rejectionReason || rejectionReason.trim() === '')) {
      newErrors.rejectionReason = t('bpmnForms.approveConformityAssessment.errors.rejectionReasonRequired', 'El motivo de rechazo es requerido')
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
        decision: decision,
        approved: decision === 'APPROVE',
        approvalComments: decision === 'APPROVE' ? approvalComments : null,
        rejectionReason: decision === 'REJECT' ? rejectionReason : null,
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
        alert(error.error || t('bpmnForms.approveConformityAssessment.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.approveConformityAssessment.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.approveConformityAssessment.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Shield className="w-6 h-6 text-primary" />
            {t('bpmnForms.approveConformityAssessment.title', 'Aprobar Evaluación de Conformidad')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.approveConformityAssessment.subtitle', 'Revise el reporte y apruebe o rechace la evaluación de conformidad')}
          </p>
        </div>
      </div>

      {/* Resumen del Reporte */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.approveConformityAssessment.reportTitle', 'Resumen del Reporte de Conformidad')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Overall Score */}
          <div className="space-y-2">
            <Label className="text-lg font-semibold">
              {t('bpmnForms.approveConformityAssessment.overallScore', 'Score General')}
            </Label>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 rounded-full h-3">
                <div
                  className={`h-3 rounded-full ${
                    overallScore >= 0.90 ? 'bg-green-500' : overallScore >= 0.70 ? 'bg-yellow-500' : 'bg-red-500'
                  }`}
                  style={{ width: `${overallScore * 100}%` }}
                />
              </div>
              <span className="text-lg font-bold">
                {(overallScore * 100).toFixed(1)}%
              </span>
            </div>
            <p className="text-sm text-muted-foreground">
              {overallScore >= 0.90
                ? t('bpmnForms.approveConformityAssessment.scoreExcellent', 'Score excelente - Aprobación recomendada')
                : overallScore >= 0.70
                ? t('bpmnForms.approveConformityAssessment.scoreGood', 'Score bueno - Revisión recomendada')
                : t('bpmnForms.approveConformityAssessment.scoreLow', 'Score bajo - Rechazo recomendado')
              }
            </p>
          </div>

          {/* Report URL */}
          {reportUrl && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.approveConformityAssessment.reportUrl', 'Reporte de Conformidad')}
              </Label>
              <a
                href={reportUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center gap-2 text-primary hover:underline"
              >
                <FileText size={16} />
                {t('bpmnForms.approveConformityAssessment.viewReport', 'Ver reporte completo')}
              </a>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Formulario de decisión */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.approveConformityAssessment.formTitle', 'Decisión de Aprobación')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Decision */}
            <div className="space-y-2">
              <Label htmlFor="decision">
                {t('bpmnForms.approveConformityAssessment.decision', 'Decisión')} *
              </Label>
              <Select value={decision} onValueChange={setDecision}>
                <SelectTrigger className={errors.decision ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('bpmnForms.approveConformityAssessment.decisionPlaceholder', 'Seleccione una decisión')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="APPROVE">
                    <div className="flex items-center gap-2">
                      <CheckCircle size={16} className="text-green-600" />
                      {t('bpmnForms.approveConformityAssessment.decisionApprove', 'Aprobar')}
                    </div>
                  </SelectItem>
                  <SelectItem value="REJECT">
                    <div className="flex items-center gap-2">
                      <XCircle size={16} className="text-red-600" />
                      {t('bpmnForms.approveConformityAssessment.decisionReject', 'Rechazar')}
                    </div>
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.decision && (
                <p className="text-sm text-red-500">{errors.decision}</p>
              )}
            </div>

            {/* Approval Comments (solo si decision es APPROVE) */}
            {decision === 'APPROVE' && (
              <div className="space-y-2">
                <Label htmlFor="approvalComments">
                  {t('bpmnForms.approveConformityAssessment.approvalComments', 'Comentarios de Aprobación')} *
                </Label>
                <Textarea
                  id="approvalComments"
                  value={approvalComments}
                  onChange={(e) => setApprovalComments(e.target.value)}
                  placeholder={t('bpmnForms.approveConformityAssessment.approvalCommentsPlaceholder', 'Proporcione comentarios sobre la aprobación de la evaluación')}
                  rows={5}
                  className={errors.approvalComments ? 'border-red-500' : ''}
                />
                {errors.approvalComments && (
                  <p className="text-sm text-red-500">{errors.approvalComments}</p>
                )}
              </div>
            )}

            {/* Rejection Reason (solo si decision es REJECT) */}
            {decision === 'REJECT' && (
              <div className="space-y-2">
                <Label htmlFor="rejectionReason">
                  {t('bpmnForms.approveConformityAssessment.rejectionReason', 'Motivo de Rechazo')} *
                </Label>
                <Textarea
                  id="rejectionReason"
                  value={rejectionReason}
                  onChange={(e) => setRejectionReason(e.target.value)}
                  placeholder={t('bpmnForms.approveConformityAssessment.rejectionReasonPlaceholder', 'Explique el motivo del rechazo de la evaluación')}
                  rows={5}
                  className={errors.rejectionReason ? 'border-red-500' : ''}
                />
                {errors.rejectionReason && (
                  <p className="text-sm text-red-500">{errors.rejectionReason}</p>
                )}
              </div>
            )}

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.approveConformityAssessment.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.approveConformityAssessment.saving', 'Enviando...')
                  : t('bpmnForms.approveConformityAssessment.submit', 'Confirmar Decisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ApproveConformityAssessmentPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ApproveConformityAssessmentFormContent />
    </Suspense>
  )
}



