// app/(app)/bpmn/forms/agent-approval-human-override/page.tsx
// Migrado de: agent-approval-human-override-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, AlertTriangle, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function AgentApprovalHumanOverrideFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [agentId, setAgentId] = useState<string>('')
  const [agentName, setAgentName] = useState<string>('')
  const [riskScore, setRiskScore] = useState<number>(0)
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [ethicsScore, setEthicsScore] = useState<number>(0)
  const [minScore, setMinScore] = useState<number>(0)
  const [confidenceLevel, setConfidenceLevel] = useState<number>(0)
  const [justification, setJustification] = useState<string>('')
  const [humanDecision, setHumanDecision] = useState<string>('')
  const [humanNotes, setHumanNotes] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const getScoreClass = (score: number): string => {
    if (score >= 85) return 'text-green-600 font-bold'
    if (score >= 60) return 'text-orange-600 font-bold'
    return 'text-red-600 font-bold'
  }

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!humanDecision || humanDecision.trim() === '') {
      newErrors.humanDecision = t('agentApprovalHumanOverride.errors.decisionRequired', 'Debe seleccionar una decisión')
    }

    if (!humanNotes || humanNotes.trim() === '') {
      newErrors.humanNotes = t('agentApprovalHumanOverride.errors.notesRequired', 'Debe proporcionar notas justificando su decisión')
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
        human_decision: humanDecision,
        human_notes: humanNotes.trim(),
        human_reviewer: 'current-user', // TODO: obtener del contexto cuando esté disponible
        human_review_timestamp: new Date().toISOString(),
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
        alert(error.error || t('agentApprovalHumanOverride.errors.submitError', 'Error al procesar la decisión'))
      }
    } catch (error) {
      console.error('Error procesando decisión:', error)
      alert(t('agentApprovalHumanOverride.errors.submitError', 'Error al procesar la decisión'))
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
          {t('agentApprovalHumanOverride.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <AlertTriangle className="w-6 h-6 text-primary" />
            {t('agentApprovalHumanOverride.title', 'Revisión Humana Requerida')}
          </h1>
          <p className="text-muted-foreground">
            {t('agentApprovalHumanOverride.subtitle', 'El Agent requiere aprobación manual debido a scores borderline')}
          </p>
        </div>
      </div>

      {/* Información del Agent */}
      <Card className="bg-blue-50 dark:bg-blue-950">
        <CardHeader>
          <CardTitle>{t('agentApprovalHumanOverride.agentInfo', 'Información del Agent')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.agentId', 'Agent ID')}</Label>
              <Input value={agentId} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.agentName', 'Agent Name')}</Label>
              <Input value={agentName} readOnly className="bg-muted font-bold" />
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Scores de Evaluación */}
      <Card>
        <CardHeader>
          <CardTitle>{t('agentApprovalHumanOverride.scores', 'Scores de Evaluación')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.riskScore', 'Risk Score')}</Label>
              <div className="flex items-center gap-2">
                <span className={getScoreClass(riskScore)}>{riskScore}</span>
                <span className="text-muted-foreground">/100</span>
              </div>
            </div>
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.complianceScore', 'Compliance Score')}</Label>
              <div className="flex items-center gap-2">
                <span className={getScoreClass(complianceScore)}>{complianceScore}</span>
                <span className="text-muted-foreground">/100</span>
              </div>
            </div>
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.ethicsScore', 'Ethics Score')}</Label>
              <div className="flex items-center gap-2">
                <span className={getScoreClass(ethicsScore)}>{ethicsScore}</span>
                <span className="text-muted-foreground">/100</span>
              </div>
            </div>
            <div>
              <Label className="font-semibold">{t('agentApprovalHumanOverride.minScore', 'Score Mínimo')}</Label>
              <div className="flex items-center gap-2">
                <span className={`${getScoreClass(minScore)} text-lg`}>{minScore}</span>
                <span className="text-muted-foreground">/100</span>
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Decisión de Drools */}
      <Card className="bg-gray-50 dark:bg-gray-900">
        <CardHeader>
          <CardTitle>{t('agentApprovalHumanOverride.droolsDecision', 'Decisión de Drools')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label className="font-semibold">{t('agentApprovalHumanOverride.confidence', 'Confianza')}</Label>
            <Input value={confidenceLevel.toString()} readOnly className="bg-muted" />
          </div>
          <div>
            <Label className="font-semibold">{t('agentApprovalHumanOverride.justification', 'Justificación')}</Label>
            <textarea
              value={justification}
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
            {t('agentApprovalHumanOverride.decisionTitle', 'Tu Decisión')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Decisión */}
            <div className="space-y-2">
              <Label className="font-semibold">
                {t('agentApprovalHumanOverride.decision', 'Decisión')} *
              </Label>
              <div className="flex gap-6">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="decision"
                    value="approve"
                    checked={humanDecision === 'approve'}
                    onChange={(e) => setHumanDecision(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="text-green-600 font-semibold">
                    {t('agentApprovalHumanOverride.approve', 'Aprobar Agent')}
                  </span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="decision"
                    value="reject"
                    checked={humanDecision === 'reject'}
                    onChange={(e) => setHumanDecision(e.target.value)}
                    className="w-4 h-4"
                  />
                  <span className="text-red-600 font-semibold">
                    {t('agentApprovalHumanOverride.reject', 'Rechazar Agent')}
                  </span>
                </label>
              </div>
              {errors.humanDecision && (
                <p className="text-sm text-destructive">{errors.humanDecision}</p>
              )}
            </div>

            {/* Notas */}
            <div className="space-y-2">
              <Label htmlFor="humanNotes" className="font-semibold">
                {t('agentApprovalHumanOverride.notes', 'Notas')} *
              </Label>
              <textarea
                id="humanNotes"
                value={humanNotes}
                onChange={(e) => setHumanNotes(e.target.value)}
                rows={4}
                placeholder={t('agentApprovalHumanOverride.notesPlaceholder', 'Explica tu decisión...')}
                className={`w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 ${errors.humanNotes ? 'border-destructive' : 'border-gray-300 dark:border-gray-700'}`}
              />
              {errors.humanNotes && (
                <p className="text-sm text-destructive">{errors.humanNotes}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('agentApprovalHumanOverride.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading || !humanDecision || !humanNotes.trim()}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('agentApprovalHumanOverride.saving', 'Guardando...')
                  : t('agentApprovalHumanOverride.submit', 'Confirmar Decisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function AgentApprovalHumanOverridePage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <AgentApprovalHumanOverrideFormContent />
    </Suspense>
  )
}
