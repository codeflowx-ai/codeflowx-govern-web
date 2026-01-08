// app/(app)/bpmn/forms/fria-wizard-form/page.tsx
// Migrado de: fria-wizard-form.zul
// Formulario para recoger datos Art. 27.1 a-f del EU AI Act
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
import { ArrowLeft, FileText, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function FriaWizardFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')
  const componentIdParam = searchParams.get('componentId')

  // Estado del formulario - Art. 27.1 a-f
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [componentId, setComponentId] = useState<string>(componentIdParam || '')

  // Art. 27.1 a) - Descripción del sistema
  const [systemDescription, setSystemDescription] = useState<string>('')

  // Art. 27.1 b) - Datos de entrenamiento
  const [trainingDataDescription, setTrainingDataDescription] = useState<string>('')
  const [trainingDataSources, setTrainingDataSources] = useState<string>('')

  // Art. 27.1 c) - Funcionalidad y capacidades
  const [functionality, setFunctionality] = useState<string>('')
  const [capabilities, setCapabilities] = useState<string>('')

  // Art. 27.1 d) - Nivel de precisión, robustez y ciberseguridad
  const [accuracyLevel, setAccuracyLevel] = useState<string>('')
  const [robustnessLevel, setRobustnessLevel] = useState<string>('')
  const [cybersecurityLevel, setCybersecurityLevel] = useState<string>('')

  // Art. 27.1 e) - Medidas de mitigación de riesgos
  const [riskMitigationMeasures, setRiskMitigationMeasures] = useState<string>('')

  // Art. 27.1 f) - Impacto en derechos fundamentales
  const [fundamentalRightsImpact, setFundamentalRightsImpact] = useState<string>('')
  const [affectedRights, setAffectedRights] = useState<string[]>([])

  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Cargar datos del workflow si están disponibles
    if (taskId) {
      fetch(`/api/bpmn/tasks/${taskId}`)
        .then(res => res.json())
        .then(data => {
          if (data.variables) {
            setProjectId(data.variables.projectId || projectId)
            setComponentId(data.variables.componentId || componentId)
            setSystemDescription(data.variables.systemDescription || '')
            setTrainingDataDescription(data.variables.trainingDataDescription || '')
            setTrainingDataSources(data.variables.trainingDataSources || '')
            setFunctionality(data.variables.functionality || '')
            setCapabilities(data.variables.capabilities || '')
            setAccuracyLevel(data.variables.accuracyLevel || '')
            setRobustnessLevel(data.variables.robustnessLevel || '')
            setCybersecurityLevel(data.variables.cybersecurityLevel || '')
            setRiskMitigationMeasures(data.variables.riskMitigationMeasures || '')
            setFundamentalRightsImpact(data.variables.fundamentalRightsImpact || '')
            if (data.variables.affectedRights) {
              setAffectedRights(Array.isArray(data.variables.affectedRights)
                ? data.variables.affectedRights
                : [])
            }
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId, componentId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!projectId || projectId.trim() === '') {
      newErrors.projectId = t('bpmnForms.friaWizardForm.errors.projectIdRequired', 'El ID del proyecto es requerido')
    }

    if (!systemDescription || systemDescription.trim() === '') {
      newErrors.systemDescription = t('bpmnForms.friaWizardForm.errors.systemDescriptionRequired', 'La descripción del sistema es requerida')
    }

    if (!trainingDataDescription || trainingDataDescription.trim() === '') {
      newErrors.trainingDataDescription = t('bpmnForms.friaWizardForm.errors.trainingDataDescriptionRequired', 'La descripción de los datos de entrenamiento es requerida')
    }

    if (!functionality || functionality.trim() === '') {
      newErrors.functionality = t('bpmnForms.friaWizardForm.errors.functionalityRequired', 'La funcionalidad es requerida')
    }

    if (!riskMitigationMeasures || riskMitigationMeasures.trim() === '') {
      newErrors.riskMitigationMeasures = t('bpmnForms.friaWizardForm.errors.riskMitigationMeasuresRequired', 'Las medidas de mitigación de riesgos son requeridas')
    }

    if (!fundamentalRightsImpact || fundamentalRightsImpact.trim() === '') {
      newErrors.fundamentalRightsImpact = t('bpmnForms.friaWizardForm.errors.fundamentalRightsImpactRequired', 'El impacto en derechos fundamentales es requerido')
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
        componentId: componentId || null,
        // Art. 27.1 a-f
        systemDescription: systemDescription,
        trainingDataDescription: trainingDataDescription,
        trainingDataSources: trainingDataSources,
        functionality: functionality,
        capabilities: capabilities,
        accuracyLevel: accuracyLevel,
        robustnessLevel: robustnessLevel,
        cybersecurityLevel: cybersecurityLevel,
        riskMitigationMeasures: riskMitigationMeasures,
        fundamentalRightsImpact: fundamentalRightsImpact,
        affectedRights: affectedRights,
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
        alert(error.error || t('bpmnForms.friaWizardForm.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.friaWizardForm.errors.submitError', 'Error al procesar el formulario'))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  const toggleAffectedRight = (right: string) => {
    setAffectedRights(prev =>
      prev.includes(right)
        ? prev.filter(r => r !== right)
        : [...prev, right]
    )
  }

  const fundamentalRightsOptions = [
    'DIGNITY',
    'FREEDOM',
    'DEMOCRACY',
    'EQUALITY',
    'RULE_OF_LAW',
    'HUMAN_RIGHTS',
    'PRIVACY',
    'DATA_PROTECTION',
    'NON_DISCRIMINATION',
    'CONSUMER_PROTECTION',
    'WORKERS_RIGHTS'
  ]

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('bpmnForms.bpmnForms.friaWizardForm.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileText className="w-6 h-6 text-primary" />
            {t('bpmnForms.bpmnForms.friaWizardForm.title', 'Wizard FRIA - Evaluación de Impacto en Derechos Fundamentales')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.friaWizardForm.subtitle', 'Complete el formulario según Art. 27.1 a-f del EU AI Act')}
          </p>
        </div>
      </div>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.friaWizardForm.formTitle', 'Datos del Formulario FRIA')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Project ID */}
            <div className="space-y-2">
              <Label htmlFor="projectId">
                {t('bpmnForms.friaWizardForm.projectId', 'ID del Proyecto')} *
              </Label>
              <Input
                id="projectId"
                value={projectId}
                onChange={(e) => setProjectId(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.projectIdPlaceholder', 'Ingrese el ID del proyecto')}
                className={errors.projectId ? 'border-red-500' : ''}
              />
              {errors.projectId && (
                <p className="text-sm text-red-500">{errors.projectId}</p>
              )}
            </div>

            {/* Component ID */}
            <div className="space-y-2">
              <Label htmlFor="componentId">
                {t('bpmnForms.friaWizardForm.componentId', 'ID del Componente')}
              </Label>
              <Input
                id="componentId"
                value={componentId}
                onChange={(e) => setComponentId(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.componentIdPlaceholder', 'Ingrese el ID del componente')}
              />
            </div>

            {/* Art. 27.1 a) - Descripción del sistema */}
            <div className="space-y-2">
              <Label htmlFor="systemDescription">
                {t('bpmnForms.friaWizardForm.systemDescription', 'Art. 27.1 a) - Descripción del Sistema')} *
              </Label>
              <Textarea
                id="systemDescription"
                value={systemDescription}
                onChange={(e) => setSystemDescription(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.systemDescriptionPlaceholder', 'Describa el sistema de IA y su propósito')}
                rows={4}
                className={errors.systemDescription ? 'border-red-500' : ''}
              />
              {errors.systemDescription && (
                <p className="text-sm text-red-500">{errors.systemDescription}</p>
              )}
            </div>

            {/* Art. 27.1 b) - Datos de entrenamiento */}
            <div className="space-y-2">
              <Label htmlFor="trainingDataDescription">
                {t('bpmnForms.friaWizardForm.trainingDataDescription', 'Art. 27.1 b) - Descripción de Datos de Entrenamiento')} *
              </Label>
              <Textarea
                id="trainingDataDescription"
                value={trainingDataDescription}
                onChange={(e) => setTrainingDataDescription(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.trainingDataDescriptionPlaceholder', 'Describa los datos utilizados para entrenar el sistema')}
                rows={4}
                className={errors.trainingDataDescription ? 'border-red-500' : ''}
              />
              {errors.trainingDataDescription && (
                <p className="text-sm text-red-500">{errors.trainingDataDescription}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="trainingDataSources">
                {t('bpmnForms.friaWizardForm.trainingDataSources', 'Fuentes de Datos de Entrenamiento')}
              </Label>
              <Textarea
                id="trainingDataSources"
                value={trainingDataSources}
                onChange={(e) => setTrainingDataSources(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.trainingDataSourcesPlaceholder', 'Liste las fuentes de datos utilizadas')}
                rows={3}
              />
            </div>

            {/* Art. 27.1 c) - Funcionalidad y capacidades */}
            <div className="space-y-2">
              <Label htmlFor="functionality">
                {t('bpmnForms.friaWizardForm.functionality', 'Art. 27.1 c) - Funcionalidad del Sistema')} *
              </Label>
              <Textarea
                id="functionality"
                value={functionality}
                onChange={(e) => setFunctionality(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.functionalityPlaceholder', 'Describa la funcionalidad y capacidades del sistema')}
                rows={4}
                className={errors.functionality ? 'border-red-500' : ''}
              />
              {errors.functionality && (
                <p className="text-sm text-red-500">{errors.functionality}</p>
              )}
            </div>

            <div className="space-y-2">
              <Label htmlFor="capabilities">
                {t('bpmnForms.friaWizardForm.capabilities', 'Capacidades Específicas')}
              </Label>
              <Textarea
                id="capabilities"
                value={capabilities}
                onChange={(e) => setCapabilities(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.capabilitiesPlaceholder', 'Describa las capacidades específicas del sistema')}
                rows={3}
              />
            </div>

            {/* Art. 27.1 d) - Nivel de precisión, robustez y ciberseguridad */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="space-y-2">
                <Label htmlFor="accuracyLevel">
                  {t('bpmnForms.friaWizardForm.accuracyLevel', 'Nivel de Precisión')}
                </Label>
                <Select value={accuracyLevel} onValueChange={setAccuracyLevel}>
                  <SelectTrigger>
                    <SelectValue placeholder={t('bpmnForms.friaWizardForm.accuracyLevelPlaceholder', 'Seleccione nivel')} />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="HIGH">{t('bpmnForms.friaWizardForm.levelHigh', 'Alto')}</SelectItem>
                    <SelectItem value="MEDIUM">{t('bpmnForms.friaWizardForm.levelMedium', 'Medio')}</SelectItem>
                    <SelectItem value="LOW">{t('bpmnForms.friaWizardForm.levelLow', 'Bajo')}</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="robustnessLevel">
                  {t('bpmnForms.friaWizardForm.robustnessLevel', 'Nivel de Robustez')}
                </Label>
                <Select value={robustnessLevel} onValueChange={setRobustnessLevel}>
                  <SelectTrigger>
                    <SelectValue placeholder={t('bpmnForms.friaWizardForm.robustnessLevelPlaceholder', 'Seleccione nivel')} />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="HIGH">{t('bpmnForms.friaWizardForm.levelHigh', 'Alto')}</SelectItem>
                    <SelectItem value="MEDIUM">{t('bpmnForms.friaWizardForm.levelMedium', 'Medio')}</SelectItem>
                    <SelectItem value="LOW">{t('bpmnForms.friaWizardForm.levelLow', 'Bajo')}</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="space-y-2">
                <Label htmlFor="cybersecurityLevel">
                  {t('bpmnForms.friaWizardForm.cybersecurityLevel', 'Nivel de Ciberseguridad')}
                </Label>
                <Select value={cybersecurityLevel} onValueChange={setCybersecurityLevel}>
                  <SelectTrigger>
                    <SelectValue placeholder={t('bpmnForms.friaWizardForm.cybersecurityLevelPlaceholder', 'Seleccione nivel')} />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="HIGH">{t('bpmnForms.friaWizardForm.levelHigh', 'Alto')}</SelectItem>
                    <SelectItem value="MEDIUM">{t('bpmnForms.friaWizardForm.levelMedium', 'Medio')}</SelectItem>
                    <SelectItem value="LOW">{t('bpmnForms.friaWizardForm.levelLow', 'Bajo')}</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </div>

            {/* Art. 27.1 e) - Medidas de mitigación de riesgos */}
            <div className="space-y-2">
              <Label htmlFor="riskMitigationMeasures">
                {t('bpmnForms.friaWizardForm.riskMitigationMeasures', 'Art. 27.1 e) - Medidas de Mitigación de Riesgos')} *
              </Label>
              <Textarea
                id="riskMitigationMeasures"
                value={riskMitigationMeasures}
                onChange={(e) => setRiskMitigationMeasures(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.riskMitigationMeasuresPlaceholder', 'Describa las medidas implementadas para mitigar riesgos')}
                rows={5}
                className={errors.riskMitigationMeasures ? 'border-red-500' : ''}
              />
              {errors.riskMitigationMeasures && (
                <p className="text-sm text-red-500">{errors.riskMitigationMeasures}</p>
              )}
            </div>

            {/* Art. 27.1 f) - Impacto en derechos fundamentales */}
            <div className="space-y-2">
              <Label htmlFor="fundamentalRightsImpact">
                {t('bpmnForms.friaWizardForm.fundamentalRightsImpact', 'Art. 27.1 f) - Impacto en Derechos Fundamentales')} *
              </Label>
              <Textarea
                id="fundamentalRightsImpact"
                value={fundamentalRightsImpact}
                onChange={(e) => setFundamentalRightsImpact(e.target.value)}
                placeholder={t('bpmnForms.friaWizardForm.fundamentalRightsImpactPlaceholder', 'Describa el impacto potencial en derechos fundamentales')}
                rows={5}
                className={errors.fundamentalRightsImpact ? 'border-red-500' : ''}
              />
              {errors.fundamentalRightsImpact && (
                <p className="text-sm text-red-500">{errors.fundamentalRightsImpact}</p>
              )}
            </div>

            {/* Derechos afectados */}
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.friaWizardForm.affectedRights', 'Derechos Fundamentales Afectados')}
              </Label>
              <div className="grid grid-cols-2 md:grid-cols-3 gap-2">
                {fundamentalRightsOptions.map((right) => (
                  <div key={right} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      id={`right-${right}`}
                      checked={affectedRights.includes(right)}
                      onChange={() => toggleAffectedRight(right)}
                      className="rounded border-gray-300"
                    />
                    <Label htmlFor={`right-${right}`} className="text-sm font-normal cursor-pointer">
                      {t(`bpmnForms.friaWizardForm.rights.${right}`, right)}
                    </Label>
                  </div>
                ))}
              </div>
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.friaWizardForm.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.friaWizardForm.saving', 'Enviando...')
                  : t('bpmnForms.friaWizardForm.submit', 'Enviar Formulario FRIA')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function FriaWizardFormPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <FriaWizardFormContent />
    </Suspense>
  )
}
