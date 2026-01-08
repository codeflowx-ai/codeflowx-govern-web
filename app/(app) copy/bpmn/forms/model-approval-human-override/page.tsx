// app/(app)/bpmn/forms/model-approval-human-override/page.tsx
// Migrado de: model-approval-human-override-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, Brain, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function ModelApprovalHumanOverrideFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [modelName, setModelName] = useState<string>('')
  const [modelVersion, setModelVersion] = useState<string>('')
  const [performanceScore, setPerformanceScore] = useState<number>(0)
  const [biasScore, setBiasScore] = useState<number>(0)
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [droolsDecision, setDroolsDecision] = useState<string>('')
  const [confidenceLevel, setConfidenceLevel] = useState<number>(0)
  const [droolsJustification, setDroolsJustification] = useState<string>('')
  const [selectedDecision, setSelectedDecision] = useState<string>('')
  const [humanJustification, setHumanJustification] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!selectedDecision || selectedDecision.trim() === '') {
      newErrors.selectedDecision = t('modelApprovalHumanOverride.errors.decisionRequired', 'Debe seleccionar una decisión')
    }

    if (!humanJustification || humanJustification.trim() === '') {
      newErrors.humanJustification = t('modelApprovalHumanOverride.errors.justificationRequired', 'Debe proporcionar una justificación')
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
        human_decision: selectedDecision,
        human_justification: humanJustification.trim(),
        override_occurred: selectedDecision !== droolsDecision,
        approved_by: 'current-user', // TODO: obtener del contexto cuando esté disponible
        approval_date: new Date().toISOString(),
        projectId: projectIdParam ? Number.parseInt(projectIdParam, 10) : null,
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
        alert(error.error || t('modelApprovalHumanOverride.errors.submitError', 'Error al procesar la decisión'))
      }
    } catch (error) {
      console.error('Error procesando decisión:', error)
      alert(t('modelApprovalHumanOverride.errors.submitError', 'Error al procesar la decisión'))
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
          {t('modelApprovalHumanOverride.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Brain className="w-6 h-6 text-primary" />
            {t('modelApprovalHumanOverride.title', 'Human Override - Model Approval')}
          </h1>
          <p className="text-muted-foreground">
            {t('modelApprovalHumanOverride.subtitle', 'Revisión humana de aprobación de modelo')}
          </p>
        </div>
      </div>

      {/* Información del Modelo */}
      <Card className="bg-blue-50 dark:bg-blue-950">
        <CardHeader>
          <CardTitle>{t('modelApprovalHumanOverride.modelInfo', 'Información del Modelo')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.modelName', 'Nombre del Modelo')}</Label>
              <Input value={modelName} readOnly className="bg-muted font-bold" />
            </div>
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.modelVersion', 'Versión')}</Label>
              <Input value={modelVersion} readOnly className="bg-muted" />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Scores de Evaluación */}
      <Card>
        <CardHeader>
          <CardTitle>{t('modelApprovalHumanOverride.scores', 'Scores de Evaluación')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-3 gap-4">
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.performanceScore', 'Performance Score')}</Label>
              <Input value={performanceScore.toString()} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.biasScore', 'Bias Score')}</Label>
              <Input value={biasScore.toString()} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.complianceScore', 'Compliance Score')}</Label>
              <Input value={complianceScore.toString()} readOnly className="bg-muted" />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Decisión de Drools */}
      <Card className="bg-gray-50 dark:bg-gray-900">
        <CardHeader>
          <CardTitle>{t('modelApprovalHumanOverride.droolsDecision', 'Decisión de Drools')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.decision', 'Decisión Recomendada')}</Label>
              <Input value={droolsDecision} readOnly className="bg-muted font-bold" />
            </div>
            <div>
              <Label className="font-semibold">{t('modelApprovalHumanOverride.confidence', 'Nivel de Confianza')}</Label>
              <Input value={confidenceLevel.toString()} readOnly className="bg-muted" />
            </div>
          </div>
          <div>
            <Label className="font-semibold">{t('modelApprovalHumanOverride.justification', 'Justificación')}</Label>
            <textarea
              value={droolsJustification}
              readOnly
              rows={4}
              className="w-full px-3 py-2 border rounded-md bg-muted text-gray-900 dark:text-gray-100"
            />
          </div>
        </CardContent>
      </Card>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('modelApprovalHumanOverride.decisionTitle', 'Tu Decisión')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Decisión */}
            <div className="space-y-2">
              <Label className="font-semibold">
                {t('modelApprovalHumanOverride.decision', 'Decisión')} *
              </Label>
              <div className="flex gap-6">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="decision"
                    value="APPROVE"
                    checked={selectedDecision === 'APPROVE'}
                    onChange={(e) => setSelectedDecision(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="text-green-600 font-semibold">
                    {t('modelApprovalHumanOverride.approve', 'Aprobar')}
                  </span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="decision"
                    value="REJECT"
                    checked={selectedDecision === 'REJECT'}
                    onChange={(e) => setSelectedDecision(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="text-red-600 font-semibold">
                    {t('modelApprovalHumanOverride.reject', 'Rechazar')}
                  </span>
                </label>
              </div>
              {errors.selectedDecision && (
                <p className="text-sm text-destructive">{errors.selectedDecision}</p>
              )}
            </div>

            {/* Justificación */}
            <div className="space-y-2">
              <Label htmlFor="humanJustification" className="font-semibold">
                {t('modelApprovalHumanOverride.justification', 'Justificación')} *
              </Label>
              <textarea
                id="humanJustification"
                value={humanJustification}
                onChange={(e) => setHumanJustification(e.target.value)}
                rows={4}
                placeholder={t('modelApprovalHumanOverride.justificationPlaceholder', 'Explica tu decisión...')}
                className={`w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 ${errors.humanJustification ? 'border-destructive' : 'border-gray-300 dark:border-gray-700'}`}
              />
              {errors.humanJustification && (
                <p className="text-sm text-destructive">{errors.humanJustification}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('modelApprovalHumanOverride.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading || !selectedDecision || !humanJustification.trim()}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('modelApprovalHumanOverride.saving', 'Guardando...')
                  : t('modelApprovalHumanOverride.submit', 'Confirmar Decisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ModelApprovalHumanOverridePage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ModelApprovalHumanOverrideFormContent />
    </Suspense>
  )
}



