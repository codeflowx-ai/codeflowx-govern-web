// app/(app)/bpmn/forms/final-review/page.tsx
// Migrado de: final-review-form.zul
// Formulario para revisión final antes de generar el reporte de conformidad
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { ArrowLeft, CheckCircle2, Save, BarChart3 } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function FinalReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [qmsScore, setQmsScore] = useState<number>(0)
  const [docScore, setDocScore] = useState<number>(0)
  const [consistencyScore, setConsistencyScore] = useState<number>(0)
  const [overallScore, setOverallScore] = useState<number>(0)
  const [qmsCompliant, setQmsCompliant] = useState<boolean>(false)
  const [docComplete, setDocComplete] = useState<boolean>(false)
  const [processConsistent, setProcessConsistent] = useState<boolean>(false)
  const [reviewComments, setReviewComments] = useState<string>('')
  const [approveForReport, setApproveForReport] = useState<boolean>(false)

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
            setQmsScore(data.variables.qmsScore || 0)
            setDocScore(data.variables.docScore || 0)
            setConsistencyScore(data.variables.consistencyScore || 0)
            setOverallScore(data.variables.overallScore || 0)
            setQmsCompliant(data.variables.qmsCompliant || false)
            setDocComplete(data.variables.docComplete || false)
            setProcessConsistent(data.variables.processConsistent || false)
            setReviewComments(data.variables.reviewComments || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!reviewComments || reviewComments.trim() === '') {
      newErrors.reviewComments = t('bpmnForms.finalReview.errors.reviewCommentsRequired', 'Los comentarios de revisión son requeridos')
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
        reviewComments: reviewComments,
        approveForReport: approveForReport,
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
        alert(error.error || t('bpmnForms.finalReview.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.finalReview.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.finalReview.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <CheckCircle2 className="w-6 h-6 text-primary" />
            {t('bpmnForms.finalReview.title', 'Revisión Final - Evaluación de Conformidad')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.finalReview.subtitle', 'Revise los resultados de la evaluación antes de generar el reporte de conformidad')}
          </p>
        </div>
      </div>

      {/* Resumen de Scores */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.finalReview.summaryTitle', 'Resumen de la Evaluación')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Overall Score */}
          <div className="space-y-2">
            <Label className="text-lg font-semibold">
              {t('bpmnForms.finalReview.overallScore', 'Score General')}
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
          </div>

          {/* Scores individuales */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.finalReview.qmsScore', 'Score QMS')}
              </Label>
              <div className="flex items-center gap-2">
                <div className="flex-1 bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${
                      qmsScore >= 0.90 ? 'bg-green-500' : 'bg-red-500'
                    }`}
                    style={{ width: `${qmsScore * 100}%` }}
                  />
                </div>
                <span className="text-sm font-medium">
                  {(qmsScore * 100).toFixed(1)}%
                </span>
              </div>
              <div className={`inline-flex items-center px-2 py-1 rounded text-xs ${
                qmsCompliant ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {qmsCompliant ? t('bpmnForms.finalReview.compliant', 'Cumple') : t('bpmnForms.finalReview.nonCompliant', 'No Cumple')}
              </div>
            </div>

            <div className="space-y-2">
              <Label>
                {t('bpmnForms.finalReview.docScore', 'Score Documentación')}
              </Label>
              <div className="flex items-center gap-2">
                <div className="flex-1 bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${
                      docScore >= 0.90 ? 'bg-green-500' : 'bg-red-500'
                    }`}
                    style={{ width: `${docScore * 100}%` }}
                  />
                </div>
                <span className="text-sm font-medium">
                  {(docScore * 100).toFixed(1)}%
                </span>
              </div>
              <div className={`inline-flex items-center px-2 py-1 rounded text-xs ${
                docComplete ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {docComplete ? t('bpmnForms.finalReview.complete', 'Completo') : t('bpmnForms.finalReview.incomplete', 'Incompleto')}
              </div>
            </div>

            <div className="space-y-2">
              <Label>
                {t('bpmnForms.finalReview.consistencyScore', 'Score Consistencia')}
              </Label>
              <div className="flex items-center gap-2">
                <div className="flex-1 bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${
                      consistencyScore >= 0.90 ? 'bg-green-500' : 'bg-red-500'
                    }`}
                    style={{ width: `${consistencyScore * 100}%` }}
                  />
                </div>
                <span className="text-sm font-medium">
                  {(consistencyScore * 100).toFixed(1)}%
                </span>
              </div>
              <div className={`inline-flex items-center px-2 py-1 rounded text-xs ${
                processConsistent ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
              }`}>
                {processConsistent ? t('bpmnForms.finalReview.consistent', 'Consistente') : t('bpmnForms.finalReview.inconsistent', 'Inconsistente')}
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Formulario de revisión */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.finalReview.formTitle', 'Comentarios de Revisión')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Review Comments */}
            <div className="space-y-2">
              <Label htmlFor="reviewComments">
                {t('bpmnForms.finalReview.reviewComments', 'Comentarios de Revisión')} *
              </Label>
              <Textarea
                id="reviewComments"
                value={reviewComments}
                onChange={(e) => setReviewComments(e.target.value)}
                placeholder={t('bpmnForms.finalReview.reviewCommentsPlaceholder', 'Proporcione comentarios sobre la evaluación y su decisión de proceder con la generación del reporte')}
                rows={6}
                className={errors.reviewComments ? 'border-red-500' : ''}
              />
              {errors.reviewComments && (
                <p className="text-sm text-red-500">{errors.reviewComments}</p>
              )}
            </div>

            {/* Approve for Report */}
            <div className="space-y-2">
              <div className="flex items-center space-x-2">
                <input
                  type="checkbox"
                  id="approveForReport"
                  checked={approveForReport}
                  onChange={(e) => setApproveForReport(e.target.checked)}
                  className="rounded border-gray-300"
                />
                <Label htmlFor="approveForReport" className="cursor-pointer">
                  {t('bpmnForms.finalReview.approveForReport', 'Aprobar para generar reporte de conformidad')}
                </Label>
              </div>
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.finalReview.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.finalReview.saving', 'Enviando...')
                  : t('bpmnForms.finalReview.submit', 'Confirmar Revisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function FinalReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <FinalReviewFormContent />
    </Suspense>
  )
}
