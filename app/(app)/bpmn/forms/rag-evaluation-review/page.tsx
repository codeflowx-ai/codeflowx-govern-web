// app/(app)/bpmn/forms/rag-evaluation-review/page.tsx
// Migrado de: rag-evaluation-review-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, Database, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function RagEvaluationReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [decision, setDecision] = useState<string>('')
  const [notes, setNotes] = useState<string>('')
  const [ragSystemName, setRagSystemName] = useState<string>('')
  const [ragEvaluationId, setRagEvaluationId] = useState<number>(0)
  const [overallScore, setOverallScore] = useState<number>(0)
  const [contextRelevance, setContextRelevance] = useState<number>(0)
  const [answerAccuracy, setAnswerAccuracy] = useState<number>(0)
  const [retrievalPrecision, setRetrievalPrecision] = useState<number>(0)
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}
    // TODO: Agregar validaciones según el ViewModel
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
        // TODO: Mapear variables según el ViewModel (taskService.complete)
        projectId: projectIdParam ? Number.parseInt(projectIdParam, 10) : null,
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
        alert(error.error || t('ragEvaluationReview.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('ragEvaluationReview.errors.submitError', 'Error al procesar el formulario'))
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
          {t('ragEvaluationReview.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Database className="w-6 h-6 text-primary" />
            {t('ragEvaluationReview.title', 'Título del Formulario')}
          </h1>
          <p className="text-muted-foreground">
            {t('ragEvaluationReview.subtitle', 'Descripción del formulario')}
          </p>
        </div>
      </div>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('ragEvaluationReview.formTitle', 'Datos del Formulario')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* TODO: Agregar campos del formulario según el ZUL y ViewModel */}

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('ragEvaluationReview.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('ragEvaluationReview.saving', 'Guardando...')
                  : t('ragEvaluationReview.submit', 'Enviar')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function RagEvaluationReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <RagEvaluationReviewFormContent />
    </Suspense>
  )
}
