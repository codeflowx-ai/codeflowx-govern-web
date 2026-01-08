// app/(app)/bpmn/forms/fria-analysis-form/page.tsx
// Migrado de: fria-analysis-form.zul
// Formulario para revisar el análisis automático de FRIA y justificar inconsistencias
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { ArrowLeft, FileSearch, Save, AlertTriangle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function FriaAnalysisFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [consistencyScore, setConsistencyScore] = useState<number>(0)
  const [isConsistent, setIsConsistent] = useState<boolean>(true)
  const [inconsistencies, setInconsistencies] = useState<string[]>([])
  const [justification, setJustification] = useState<string>('')
  const [friaDocumentUrl, setFriaDocumentUrl] = useState<string>('')
  const [complianceScore, setComplianceScore] = useState<number>(0)
  const [art27Compliant, setArt27Compliant] = useState<boolean>(false)

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
            setConsistencyScore(data.variables.consistencyScore || 0)
            setIsConsistent(data.variables.isConsistent !== undefined ? data.variables.isConsistent : true)
            setInconsistencies(Array.isArray(data.variables.inconsistencies) ? data.variables.inconsistencies : [])
            setJustification(data.variables.consistencyJustification || '')
            setFriaDocumentUrl(data.variables.friaDocumentUrl || '')
            setComplianceScore(data.variables.complianceScore || 0)
            setArt27Compliant(data.variables.art27Compliant || false)
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    // Si consistencyScore < 0.70, se requiere justificación
    if (consistencyScore < 0.70 && (!justification || justification.trim() === '')) {
      newErrors.justification = t('bpmnForms.friaAnalysisForm.errors.justificationRequired', 'La justificación es requerida cuando el score de consistencia es menor a 0.70')
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
        consistencyJustification: justification,
        consistencyAcknowledged: true,
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
        alert(error.error || t('bpmnForms.friaAnalysisForm.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.friaAnalysisForm.errors.submitError', 'Error al procesar el formulario'))
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
          {t('bpmnForms.friaAnalysisForm.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileSearch className="w-6 h-6 text-primary" />
            {t('bpmnForms.friaAnalysisForm.title', 'Análisis FRIA - Validación Cruzada')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.friaAnalysisForm.subtitle', 'Revise el análisis automático y justifique inconsistencias si es necesario')}
          </p>
        </div>
      </div>

      {/* Resultados del análisis */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaAnalysisForm.resultsTitle', 'Resultados del Análisis')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Score de consistencia */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.friaAnalysisForm.consistencyScore', 'Score de Consistencia')}
            </Label>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 rounded-full h-2.5">
                <div
                  className={`h-2.5 rounded-full ${
                    consistencyScore >= 0.70 ? 'bg-green-500' : 'bg-red-500'
                  }`}
                  style={{ width: `${consistencyScore * 100}%` }}
                />
              </div>
              <span className="text-sm font-medium">
                {(consistencyScore * 100).toFixed(1)}%
              </span>
            </div>
            {consistencyScore < 0.70 && (
              <div className="flex items-center gap-2 text-amber-600 text-sm">
                <AlertTriangle size={16} />
                <span>
                  {t('bpmnForms.friaAnalysisForm.consistencyWarning', 'El score de consistencia es menor a 0.70. Se requiere justificación.')}
                </span>
              </div>
            )}
          </div>

          {/* Estado de consistencia */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.friaAnalysisForm.isConsistent', 'Estado de Consistencia')}
            </Label>
            <div className={`inline-flex items-center px-3 py-1 rounded-full text-sm ${
              isConsistent ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
            }`}>
              {isConsistent
                ? t('bpmnForms.friaAnalysisForm.consistent', 'Consistente')
                : t('bpmnForms.friaAnalysisForm.inconsistent', 'Inconsistente')
              }
            </div>
          </div>

          {/* Inconsistencias detectadas */}
          {inconsistencies.length > 0 && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAnalysisForm.inconsistencies', 'Inconsistencias Detectadas')}
              </Label>
              <ul className="list-disc list-inside space-y-1 text-sm">
                {inconsistencies.map((inc, index) => (
                  <li key={index} className="text-amber-600">
                    {inc}
                  </li>
                ))}
              </ul>
            </div>
          )}

          {/* Score de compliance */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.friaAnalysisForm.complianceScore', 'Score de Compliance')}
            </Label>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 rounded-full h-2.5">
                <div
                  className={`h-2.5 rounded-full ${
                    complianceScore >= 0.90 ? 'bg-green-500' : 'bg-yellow-500'
                  }`}
                  style={{ width: `${complianceScore * 100}%` }}
                />
              </div>
              <span className="text-sm font-medium">
                {(complianceScore * 100).toFixed(1)}%
              </span>
            </div>
          </div>

          {/* Art. 27 Compliant */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.friaAnalysisForm.art27Compliant', 'Cumplimiento Art. 27')}
            </Label>
            <div className={`inline-flex items-center px-3 py-1 rounded-full text-sm ${
              art27Compliant ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
            }`}>
              {art27Compliant
                ? t('bpmnForms.friaAnalysisForm.compliant', 'Cumple')
                : t('bpmnForms.friaAnalysisForm.nonCompliant', 'No Cumple')
              }
            </div>
          </div>

          {/* URL del documento FRIA */}
          {friaDocumentUrl && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaAnalysisForm.friaDocumentUrl', 'Documento FRIA')}
              </Label>
              <a
                href={friaDocumentUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="text-primary hover:underline text-sm"
              >
                {t('bpmnForms.friaAnalysisForm.viewDocument', 'Ver documento')}
              </a>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Formulario de justificación */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaAnalysisForm.formTitle', 'Justificación de Inconsistencias')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Justificación (requerida si consistencyScore < 0.70) */}
            <div className="space-y-2">
              <Label htmlFor="justification">
                {t('bpmnForms.friaAnalysisForm.justification', 'Justificación')}
                {consistencyScore < 0.70 && ' *'}
              </Label>
              <Textarea
                id="justification"
                value={justification}
                onChange={(e) => setJustification(e.target.value)}
                placeholder={t('bpmnForms.friaAnalysisForm.justificationPlaceholder', 'Justifique las inconsistencias detectadas entre el FRIA documental y las métricas técnicas')}
                rows={6}
                className={errors.justification ? 'border-red-500' : ''}
              />
              {errors.justification && (
                <p className="text-sm text-red-500">{errors.justification}</p>
              )}
              {consistencyScore < 0.70 && (
                <p className="text-sm text-muted-foreground">
                  {t('bpmnForms.friaAnalysisForm.justificationHelp', 'Debe proporcionar una justificación para las inconsistencias detectadas antes de continuar.')}
                </p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.friaAnalysisForm.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.friaAnalysisForm.saving', 'Enviando...')
                  : t('bpmnForms.friaAnalysisForm.submit', 'Confirmar Análisis')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function FriaAnalysisFormPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <FriaAnalysisFormContent />
    </Suspense>
  )
}
