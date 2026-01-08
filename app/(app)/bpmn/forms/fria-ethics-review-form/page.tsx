// app/(app)/bpmn/forms/fria-ethics-review-form/page.tsx
// Migrado de: fria-ethics-review-form.zul
// Formulario para revisión ética de FRIA (Enhanced Review) cuando hay alto impacto
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
import { ArrowLeft, Shield, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function FriaEthicsReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [reviewDecision, setReviewDecision] = useState<string>('') // APPROVE, REJECT, MODIFY
  const [reviewComments, setReviewComments] = useState<string>('')
  const [impactSeverity, setImpactSeverity] = useState<string>('')
  const [charterArticlesAffected, setCharterArticlesAffected] = useState<string[]>([])
  const [modificationPlan, setModificationPlan] = useState<string>('')

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
            setReviewDecision(data.variables.reviewDecision || '')
            setReviewComments(data.variables.reviewComments || '')
            setImpactSeverity(data.variables.impactSeverity || '')
            if (data.variables.charterArticlesAffected) {
              setCharterArticlesAffected(Array.isArray(data.variables.charterArticlesAffected)
                ? data.variables.charterArticlesAffected
                : [])
            }
            setModificationPlan(data.variables.modificationPlan || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!reviewDecision) {
      newErrors.reviewDecision = t('bpmnForms.friaEthicsReviewForm.errors.reviewDecisionRequired', 'La decisión de revisión es requerida')
    }

    if (!reviewComments || reviewComments.trim() === '') {
      newErrors.reviewComments = t('bpmnForms.friaEthicsReviewForm.errors.reviewCommentsRequired', 'Los comentarios de revisión son requeridos')
    }

    // Si la decisión es MODIFY, se requiere plan de modificación
    if (reviewDecision === 'MODIFY' && (!modificationPlan || modificationPlan.trim() === '')) {
      newErrors.modificationPlan = t('bpmnForms.friaEthicsReviewForm.errors.modificationPlanRequired', 'El plan de modificación es requerido cuando la decisión es MODIFY')
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
        reviewDecision: reviewDecision,
        reviewComments: reviewComments,
        modificationPlan: reviewDecision === 'MODIFY' ? modificationPlan : null,
        friaApproved: reviewDecision === 'APPROVE',
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
        alert(error.error || t('bpmnForms.friaEthicsReviewForm.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.friaEthicsReviewForm.errors.submitError', 'Error al procesar el formulario'))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  const charterArticles = [
    'ARTICLE_1',
    'ARTICLE_2',
    'ARTICLE_3',
    'ARTICLE_4',
    'ARTICLE_5',
    'ARTICLE_6',
    'ARTICLE_7',
    'ARTICLE_8',
    'ARTICLE_9',
    'ARTICLE_10',
    'ARTICLE_11',
    'ARTICLE_12',
    'ARTICLE_13',
    'ARTICLE_14',
    'ARTICLE_15',
    'ARTICLE_16',
    'ARTICLE_17',
    'ARTICLE_18',
    'ARTICLE_19',
    'ARTICLE_20',
    'ARTICLE_21',
    'ARTICLE_22',
    'ARTICLE_23',
    'ARTICLE_24',
    'ARTICLE_25',
    'ARTICLE_26',
    'ARTICLE_27',
    'ARTICLE_28',
    'ARTICLE_29',
    'ARTICLE_30',
    'ARTICLE_31',
    'ARTICLE_32',
    'ARTICLE_33',
    'ARTICLE_34',
    'ARTICLE_35',
    'ARTICLE_36',
    'ARTICLE_37',
    'ARTICLE_38',
    'ARTICLE_39',
    'ARTICLE_40',
    'ARTICLE_41',
    'ARTICLE_42',
    'ARTICLE_43',
    'ARTICLE_44',
    'ARTICLE_45',
    'ARTICLE_46',
    'ARTICLE_47',
    'ARTICLE_48',
    'ARTICLE_49',
    'ARTICLE_50',
    'ARTICLE_51',
    'ARTICLE_52',
    'ARTICLE_53',
    'ARTICLE_54'
  ]

  const toggleCharterArticle = (article: string) => {
    setCharterArticlesAffected(prev =>
      prev.includes(article)
        ? prev.filter(a => a !== article)
        : [...prev, article]
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('bpmnForms.friaEthicsReviewForm.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Shield className="w-6 h-6 text-primary" />
            {t('bpmnForms.friaEthicsReviewForm.title', 'Revisión Ética FRIA - Enhanced Review')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.friaEthicsReviewForm.subtitle', 'Revise el impacto en derechos fundamentales y tome una decisión')}
          </p>
        </div>
      </div>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaEthicsReviewForm.formTitle', 'Formulario de Revisión Ética')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Impact Severity (read-only) */}
            {impactSeverity && (
              <div className="space-y-2">
                <Label>
                  {t('bpmnForms.friaEthicsReviewForm.impactSeverity', 'Severidad del Impacto')}
                </Label>
                <div className={`inline-flex items-center px-3 py-1 rounded-full text-sm ${
                  impactSeverity === 'HIGH' ? 'bg-red-100 text-red-800' :
                  impactSeverity === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-green-100 text-green-800'
                }`}>
                  {t(`bpmnForms.friaEthicsReviewForm.severity.${impactSeverity}`, impactSeverity)}
                </div>
              </div>
            )}

            {/* Charter Articles Affected */}
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaEthicsReviewForm.charterArticlesAffected', 'Artículos de la Carta de Derechos Fundamentales Afectados')}
              </Label>
              <div className="grid grid-cols-2 md:grid-cols-3 gap-2 max-h-48 overflow-y-auto border rounded p-2">
                {charterArticles.map((article) => (
                  <div key={article} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      id={`article-${article}`}
                      checked={charterArticlesAffected.includes(article)}
                      onChange={() => toggleCharterArticle(article)}
                      className="rounded border-gray-300"
                    />
                    <Label htmlFor={`article-${article}`} className="text-sm font-normal cursor-pointer">
                      {t(`bpmnForms.friaEthicsReviewForm.articles.${article}`, article)}
                    </Label>
                  </div>
                ))}
              </div>
            </div>

            {/* Review Decision */}
            <div className="space-y-2">
              <Label htmlFor="reviewDecision">
                {t('bpmnForms.friaEthicsReviewForm.reviewDecision', 'Decisión de Revisión')} *
              </Label>
              <Select value={reviewDecision} onValueChange={setReviewDecision}>
                <SelectTrigger className={errors.reviewDecision ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('bpmnForms.friaEthicsReviewForm.reviewDecisionPlaceholder', 'Seleccione una decisión')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="APPROVE">
                    {t('bpmnForms.friaEthicsReviewForm.decisionApprove', 'Aprobar')}
                  </SelectItem>
                  <SelectItem value="REJECT">
                    {t('bpmnForms.friaEthicsReviewForm.decisionReject', 'Rechazar')}
                  </SelectItem>
                  <SelectItem value="MODIFY">
                    {t('bpmnForms.friaEthicsReviewForm.decisionModify', 'Modificar')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.reviewDecision && (
                <p className="text-sm text-red-500">{errors.reviewDecision}</p>
              )}
            </div>

            {/* Review Comments */}
            <div className="space-y-2">
              <Label htmlFor="reviewComments">
                {t('bpmnForms.friaEthicsReviewForm.reviewComments', 'Comentarios de Revisión')} *
              </Label>
              <Textarea
                id="reviewComments"
                value={reviewComments}
                onChange={(e) => setReviewComments(e.target.value)}
                placeholder={t('bpmnForms.friaEthicsReviewForm.reviewCommentsPlaceholder', 'Proporcione comentarios detallados sobre su decisión')}
                rows={6}
                className={errors.reviewComments ? 'border-red-500' : ''}
              />
              {errors.reviewComments && (
                <p className="text-sm text-red-500">{errors.reviewComments}</p>
              )}
            </div>

            {/* Modification Plan (solo si decisión es MODIFY) */}
            {reviewDecision === 'MODIFY' && (
              <div className="space-y-2">
                <Label htmlFor="modificationPlan">
                  {t('bpmnForms.friaEthicsReviewForm.modificationPlan', 'Plan de Modificación')} *
                </Label>
                <Textarea
                  id="modificationPlan"
                  value={modificationPlan}
                  onChange={(e) => setModificationPlan(e.target.value)}
                  placeholder={t('bpmnForms.friaEthicsReviewForm.modificationPlanPlaceholder', 'Describa el plan de modificación necesario para aprobar el FRIA')}
                  rows={6}
                  className={errors.modificationPlan ? 'border-red-500' : ''}
                />
                {errors.modificationPlan && (
                  <p className="text-sm text-red-500">{errors.modificationPlan}</p>
                )}
              </div>
            )}

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.friaEthicsReviewForm.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.friaEthicsReviewForm.saving', 'Enviando...')
                  : t('bpmnForms.friaEthicsReviewForm.submit', 'Enviar Revisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function FriaEthicsReviewFormPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <FriaEthicsReviewFormContent />
    </Suspense>
  )
}
