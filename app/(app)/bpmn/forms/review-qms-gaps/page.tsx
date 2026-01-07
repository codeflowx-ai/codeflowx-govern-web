// app/(app)/bpmn/forms/review-qms-gaps/page.tsx
// Migrado de: review-qms-gaps-form.zul
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
import { ArrowLeft, FileCheck, Save, AlertTriangle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function ReviewQmsGapsFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [qmsScore, setQmsScore] = useState<number>(0)
  const [qmsGaps, setQmsGaps] = useState<string[]>([])
  const [action, setAction] = useState<string>('') // RETRY, CANCEL
  const [gapResolution, setGapResolution] = useState<string>('')
  const [comments, setComments] = useState<string>('')

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
            setQmsGaps(Array.isArray(data.variables.qmsGaps) ? data.variables.qmsGaps : [])
            setGapResolution(data.variables.gapResolution || '')
            setComments(data.variables.comments || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!action) {
      newErrors.action = t('bpmnForms.reviewQmsGaps.errors.actionRequired', 'Debe seleccionar una acción')
    }

    if (action === 'RETRY' && (!gapResolution || gapResolution.trim() === '')) {
      newErrors.gapResolution = t('bpmnForms.reviewQmsGaps.errors.gapResolutionRequired', 'La resolución de gaps es requerida cuando se reintenta')
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
        action: action, // RETRY, CANCEL
        gapResolution: action === 'RETRY' ? gapResolution : null,
        comments: comments,
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
        alert(error.error || t('bpmnForms.reviewQmsGaps.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.reviewQmsGaps.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.reviewQmsGaps.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileCheck className="w-6 h-6 text-primary" />
            {t('bpmnForms.reviewQmsGaps.title', 'Revisar Gaps QMS')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.reviewQmsGaps.subtitle', 'Revise los gaps del Sistema de Gestión de Calidad (QMS) y decida la acción a tomar')}
          </p>
        </div>
      </div>

      {/* Resultados QMS */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.reviewQmsGaps.resultsTitle', 'Resultados de la Evaluación QMS')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* QMS Score */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.reviewQmsGaps.qmsScore', 'Score QMS')}
            </Label>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 rounded-full h-2.5">
                <div
                  className={`h-2.5 rounded-full ${
                    qmsScore >= 0.90 ? 'bg-green-500' : 'bg-red-500'
                  }`}
                  style={{ width: `${qmsScore * 100}%` }}
                />
              </div>
              <span className="text-sm font-medium">
                {(qmsScore * 100).toFixed(1)}%
              </span>
            </div>
            {qmsScore < 0.90 && (
              <div className="flex items-center gap-2 text-amber-600 text-sm">
                <AlertTriangle size={16} />
                <span>
                  {t('bpmnForms.reviewQmsGaps.qmsScoreWarning', 'El score QMS es menor a 0.90. Se requiere revisión de gaps.')}
                </span>
              </div>
            )}
          </div>

          {/* QMS Gaps */}
          {qmsGaps.length > 0 && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.reviewQmsGaps.qmsGaps', 'Gaps QMS Detectados')}
              </Label>
              <ul className="list-disc list-inside space-y-1 text-sm">
                {qmsGaps.map((gap, index) => (
                  <li key={index} className="text-amber-600">
                    {gap}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Formulario de acción */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.reviewQmsGaps.formTitle', 'Acción a Tomar')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Action */}
            <div className="space-y-2">
              <Label htmlFor="action">
                {t('bpmnForms.reviewQmsGaps.action', 'Acción')} *
              </Label>
              <Select value={action} onValueChange={setAction}>
                <SelectTrigger className={errors.action ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('bpmnForms.reviewQmsGaps.actionPlaceholder', 'Seleccione una acción')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="RETRY">
                    {t('bpmnForms.reviewQmsGaps.actionRetry', 'Reintentar (después de resolver gaps)')}
                  </SelectItem>
                  <SelectItem value="CANCEL">
                    {t('bpmnForms.reviewQmsGaps.actionCancel', 'Cancelar evaluación')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.action && (
                <p className="text-sm text-red-500">{errors.action}</p>
              )}
            </div>

            {/* Gap Resolution (solo si action es RETRY) */}
            {action === 'RETRY' && (
              <div className="space-y-2">
                <Label htmlFor="gapResolution">
                  {t('bpmnForms.reviewQmsGaps.gapResolution', 'Resolución de Gaps')} *
                </Label>
                <Textarea
                  id="gapResolution"
                  value={gapResolution}
                  onChange={(e) => setGapResolution(e.target.value)}
                  placeholder={t('bpmnForms.reviewQmsGaps.gapResolutionPlaceholder', 'Describa cómo se resolverán los gaps identificados')}
                  rows={5}
                  className={errors.gapResolution ? 'border-red-500' : ''}
                />
                {errors.gapResolution && (
                  <p className="text-sm text-red-500">{errors.gapResolution}</p>
                )}
              </div>
            )}

            {/* Comments */}
            <div className="space-y-2">
              <Label htmlFor="comments">
                {t('bpmnForms.reviewQmsGaps.comments', 'Comentarios')}
              </Label>
              <Textarea
                id="comments"
                value={comments}
                onChange={(e) => setComments(e.target.value)}
                placeholder={t('bpmnForms.reviewQmsGaps.commentsPlaceholder', 'Comentarios adicionales sobre la revisión')}
                rows={4}
              />
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.reviewQmsGaps.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.reviewQmsGaps.saving', 'Enviando...')
                  : t('bpmnForms.reviewQmsGaps.submit', 'Confirmar Acción')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ReviewQmsGapsPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ReviewQmsGapsFormContent />
    </Suspense>
  )
}
