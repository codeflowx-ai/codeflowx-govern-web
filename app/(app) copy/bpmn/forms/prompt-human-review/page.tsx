// app/(app)/bpmn/forms/prompt-human-review/page.tsx
// Migrado de: prompt-human-review-form.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, FileText, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function PromptHumanReviewFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [promptName, setPromptName] = useState<string>('')
  const [promptCategory, setPromptCategory] = useState<string>('')
  const [promptVersion, setPromptVersion] = useState<string>('')
  const [approvalType, setApprovalType] = useState<string>('')
  const [requestReason, setRequestReason] = useState<string>('')
  const [promptContent, setPromptContent] = useState<string>('')
  const [safetyScore, setSafetyScore] = useState<number>(0)
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [jailbreakDetected, setJailbreakDetected] = useState<boolean>(false)
  const [injectionDetected, setInjectionDetected] = useState<boolean>(false)
  const [maliciousContentDetected, setMaliciousContentDetected] = useState<boolean>(false)
  const [safetyRisks, setSafetyRisks] = useState<string[]>([])
  const [complianceRecommendation, setComplianceRecommendation] = useState<string>('')
  const [approved, setApproved] = useState<boolean | null>(null)
  const [approvalNotes, setApprovalNotes] = useState<string>('')
  const [rejectionReason, setRejectionReason] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  const getScoreClass = (score: number): string => {
    if (score >= 90) return 'bg-green-500 text-white'
    if (score >= 70) return 'bg-orange-500 text-white'
    return 'bg-red-500 text-white'
  }

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (approved === null) {
      newErrors.approved = t('promptHumanReview.errors.decisionRequired', 'Debe seleccionar APROBAR o RECHAZAR')
    }

    if (approved === false && (!rejectionReason || rejectionReason.trim() === '')) {
      newErrors.rejectionReason = t('promptHumanReview.errors.rejectionReasonRequired', 'Debe proporcionar una razón del rechazo')
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
        approved: approved,
        approvalNotes: approvalNotes.trim(),
        rejectionReason: approved === false ? rejectionReason.trim() : null,
        reviewerId: 'current-user', // TODO: obtener del contexto cuando esté disponible
        reviewerName: 'current-user',
        reviewedAt: Date.now(),
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
        alert(error.error || t('promptHumanReview.errors.submitError', 'Error al procesar la decisión'))
      }
    } catch (error) {
      console.error('Error procesando decisión:', error)
      alert(t('promptHumanReview.errors.submitError', 'Error al procesar la decisión'))
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
          {t('promptHumanReview.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileText className="w-6 h-6 text-primary" />
            {t('promptHumanReview.title', 'Revisión Humana de Prompt')}
          </h1>
          <p className="text-muted-foreground">
            {t('promptHumanReview.subtitle', 'Revisar y aprobar o rechazar el prompt')}
          </p>
        </div>
      </div>

      {/* Información del Prompt */}
      <Card className="bg-blue-50 dark:bg-blue-950">
        <CardHeader>
          <CardTitle>{t('promptHumanReview.promptInfo', 'Información del Prompt')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="font-semibold">{t('promptHumanReview.promptName', 'Nombre')}</Label>
              <Input value={promptName} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('promptHumanReview.promptCategory', 'Categoría')}</Label>
              <Input value={promptCategory} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('promptHumanReview.promptVersion', 'Versión')}</Label>
              <Input value={promptVersion} readOnly className="bg-muted" />
            </div>
            <div>
              <Label className="font-semibold">{t('promptHumanReview.approvalType', 'Tipo de Aprobación')}</Label>
              <Input value={approvalType} readOnly className="bg-muted" />
            </div>
          </div>
          <div>
            <Label className="font-semibold">{t('promptHumanReview.requestReason', 'Razón de Solicitud')}</Label>
            <textarea
              value={requestReason}
              readOnly
              rows={3}
              className="w-full px-3 py-2 border rounded-md bg-muted text-gray-900 dark:text-gray-100"
            />
          </div>
        </CardContent>
      </Card>

      {/* Contenido del Prompt */}
      <Card>
        <CardHeader>
          <CardTitle>{t('promptHumanReview.promptContent', 'Contenido del Prompt')}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="bg-gray-50 dark:bg-gray-900 p-4 rounded-md font-mono text-sm max-h-40 overflow-y-auto">
            <pre className="whitespace-pre-wrap">{promptContent}</pre>
          </div>
        </CardContent>
      </Card>

      {/* Resultado del Safety Check */}
      <Card>
        <CardHeader>
          <CardTitle>{t('promptHumanReview.safetyCheck', 'Resultado del Safety Check')}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-4 gap-4">
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.safetyScore', 'Puntuación de Seguridad')}</Label>
              <div className={`inline-block px-5 py-2 rounded-md text-lg font-bold text-center min-w-[80px] ${getScoreClass(safetyScore)}`}>
                {safetyScore}
              </div>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.jailbreakDetected', 'Jailbreak Detectado')}</Label>
              <div className={jailbreakDetected ? 'text-red-600 font-bold' : 'text-green-600 font-bold'}>
                {jailbreakDetected ? 'SÍ ⚠️' : 'NO ✓'}
              </div>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.injectionDetected', 'Injection Detectado')}</Label>
              <div className={injectionDetected ? 'text-red-600 font-bold' : 'text-green-600 font-bold'}>
                {injectionDetected ? 'SÍ ⚠️' : 'NO ✓'}
              </div>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.maliciousContent', 'Contenido Malicioso')}</Label>
              <div className={maliciousContentDetected ? 'text-red-600 font-bold' : 'text-green-600 font-bold'}>
                {maliciousContentDetected ? 'SÍ ⚠️' : 'NO ✓'}
              </div>
            </div>
          </div>
          {safetyRisks.length > 0 && (
            <div>
              <Label className="font-semibold text-orange-600">{t('promptHumanReview.risksIdentified', 'Riesgos Identificados')}</Label>
              <div className="mt-2 space-y-2">
                {safetyRisks.map((risk, index) => (
                  <div key={index} className="bg-orange-50 dark:bg-orange-950 p-3 border-l-4 border-orange-500 rounded">
                    {risk}
                  </div>
                ))}
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Resultado del Compliance Check */}
      <Card>
        <CardHeader>
          <CardTitle>{t('promptHumanReview.complianceCheck', 'Resultado del Compliance Check')}</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.complianceScore', 'Puntuación de Compliance')}</Label>
              <div className={`inline-block px-5 py-2 rounded-md text-lg font-bold text-center min-w-[80px] ${getScoreClass(complianceScore)}`}>
                {complianceScore}
              </div>
            </div>
            <div>
              <Label className="text-sm text-muted-foreground">{t('promptHumanReview.recommendation', 'Recomendación')}</Label>
              <div className="font-bold">{complianceRecommendation}</div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('promptHumanReview.decisionTitle', 'Decisión')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Decisión */}
            <div className="space-y-2">
              <Label className="font-semibold">
                {t('promptHumanReview.approvePrompt', '¿Aprobar el prompt?')} *
              </Label>
              <div className="flex gap-6">
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="approved"
                    value="true"
                    checked={approved === true}
                    onChange={() => setApproved(true)}
                    className="w-4 h-4"
                  />
                  <span className="text-green-600 font-semibold">
                    {t('promptHumanReview.approve', 'APROBAR')}
                  </span>
                </label>
                <label className="flex items-center gap-2 cursor-pointer">
                  <input
                    type="radio"
                    name="approved"
                    value="false"
                    checked={approved === false}
                    onChange={() => setApproved(false)}
                    className="w-4 h-4"
                  />
                  <span className="text-red-600 font-semibold">
                    {t('promptHumanReview.reject', 'RECHAZAR')}
                  </span>
                </label>
              </div>
              {errors.approved && (
                <p className="text-sm text-destructive">{errors.approved}</p>
              )}
            </div>

            {/* Notas de Aprobación */}
            <div className="space-y-2">
              <Label htmlFor="approvalNotes" className="font-semibold">
                {t('promptHumanReview.approvalNotes', 'Notas de Aprobación')}
              </Label>
              <textarea
                id="approvalNotes"
                value={approvalNotes}
                onChange={(e) => setApprovalNotes(e.target.value)}
                rows={3}
                placeholder={t('promptHumanReview.approvalNotesPlaceholder', 'Añada notas o comentarios sobre su decisión...')}
                className="w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 border-gray-300 dark:border-gray-700"
              />
            </div>

            {/* Razón del Rechazo */}
            {approved === false && (
              <div className="space-y-2">
                <Label htmlFor="rejectionReason" className="font-semibold">
                  {t('promptHumanReview.rejectionReason', 'Razón del Rechazo')} *
                </Label>
                <textarea
                  id="rejectionReason"
                  value={rejectionReason}
                  onChange={(e) => setRejectionReason(e.target.value)}
                  rows={3}
                  placeholder={t('promptHumanReview.rejectionReasonPlaceholder', 'Explique la razón del rechazo del prompt...')}
                  className={`w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 ${errors.rejectionReason ? 'border-destructive' : 'border-gray-300 dark:border-gray-700'}`}
                />
                {errors.rejectionReason && (
                  <p className="text-sm text-destructive">{errors.rejectionReason}</p>
                )}
              </div>
            )}

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('promptHumanReview.cancel', 'Cancelar')}
              </Button>
              <Button
                type="submit"
                disabled={loading || approved === null || (approved === false && !rejectionReason.trim())}
              >
                <Save size={16} className="mr-2" />
                {loading
                  ? t('promptHumanReview.saving', 'Guardando...')
                  : t('promptHumanReview.submit', 'Confirmar Decisión')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function PromptHumanReviewPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <PromptHumanReviewFormContent />
    </Suspense>
  )
}



