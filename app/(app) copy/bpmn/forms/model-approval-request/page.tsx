// app/(app)/bpmn/forms/model-approval-request/page.tsx
// Migrado de: plataforma/workflow/model-approval-request-form.zul
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
import { ArrowLeft, FileCheck, Save } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function ModelApprovalRequestFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const modelIdParam = searchParams.get('modelId')
  const versionIdParam = searchParams.get('versionId')

  // Estado del formulario
  const [modelId, setModelId] = useState<string>(modelIdParam || '')
  const [versionId, setVersionId] = useState<string>(versionIdParam || '')
  const [approvalType, setApprovalType] = useState<string>('')
  const [targetEnvironment, setTargetEnvironment] = useState<string>('')
  const [businessJustification, setBusinessJustification] = useState<string>('')
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // TODO: Cargar datos del modelo si están disponibles
    // Cargar versiones disponibles del modelo si modelId está disponible
  }, [modelId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!modelId || modelId.trim() === '') {
      newErrors.modelId = t('modelApprovalRequest.errors.modelIdRequired', 'El ID del modelo es requerido')
    }

    if (!versionId || versionId.trim() === '') {
      newErrors.versionId = t('modelApprovalRequest.errors.versionIdRequired', 'El ID de la versión es requerido')
    }

    if (!approvalType) {
      newErrors.approvalType = t('modelApprovalRequest.errors.approvalTypeRequired', 'El tipo de aprobación es requerido')
    }

    if (!targetEnvironment) {
      newErrors.targetEnvironment = t('modelApprovalRequest.errors.targetEnvironmentRequired', 'El ambiente objetivo es requerido')
    }

    if (!businessJustification || businessJustification.trim() === '') {
      newErrors.businessJustification = t('modelApprovalRequest.errors.businessJustificationRequired', 'La justificación del negocio es requerida')
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
        modelId: modelId,
        versionId: versionId,
        approvalType: approvalType, // NEW_MODEL, VERSION_UPDATE, REDEPLOYMENT
        targetEnvironment: targetEnvironment, // STAGING, PRODUCTION
        businessJustification: businessJustification,
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
        alert(error.error || t('modelApprovalRequest.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('modelApprovalRequest.errors.submitError', 'Error al procesar el formulario'))
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
          {t('modelApprovalRequest.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileCheck className="w-6 h-6 text-primary" />
            {t('modelApprovalRequest.title', 'Solicitar Aprobación de Modelo')}
          </h1>
          <p className="text-muted-foreground">
            {t('modelApprovalRequest.subtitle', 'Complete el formulario para solicitar la aprobación del modelo')}
          </p>
        </div>
      </div>

      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('modelApprovalRequest.formTitle', 'Información de la Solicitud')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Model ID */}
            <div className="space-y-2">
              <Label htmlFor="modelId">
                {t('modelApprovalRequest.modelId', 'ID del Modelo')} *
              </Label>
              <Input
                id="modelId"
                value={modelId}
                onChange={(e) => setModelId(e.target.value)}
                placeholder={t('modelApprovalRequest.modelIdPlaceholder', 'Ingrese el ID del modelo')}
                className={errors.modelId ? 'border-red-500' : ''}
              />
              {errors.modelId && (
                <p className="text-sm text-red-500">{errors.modelId}</p>
              )}
            </div>

            {/* Version ID */}
            <div className="space-y-2">
              <Label htmlFor="versionId">
                {t('modelApprovalRequest.versionId', 'ID de la Versión')} *
              </Label>
              <Input
                id="versionId"
                value={versionId}
                onChange={(e) => setVersionId(e.target.value)}
                placeholder={t('modelApprovalRequest.versionIdPlaceholder', 'Ingrese el ID de la versión')}
                className={errors.versionId ? 'border-red-500' : ''}
              />
              {errors.versionId && (
                <p className="text-sm text-red-500">{errors.versionId}</p>
              )}
            </div>

            {/* Approval Type */}
            <div className="space-y-2">
              <Label htmlFor="approvalType">
                {t('modelApprovalRequest.approvalType', 'Tipo de Aprobación')} *
              </Label>
              <Select value={approvalType} onValueChange={setApprovalType}>
                <SelectTrigger className={errors.approvalType ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('modelApprovalRequest.approvalTypePlaceholder', 'Seleccione el tipo de aprobación')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="NEW_MODEL">
                    {t('modelApprovalRequest.approvalTypeNewModel', 'Nuevo Modelo')}
                  </SelectItem>
                  <SelectItem value="VERSION_UPDATE">
                    {t('modelApprovalRequest.approvalTypeVersionUpdate', 'Actualización de Versión')}
                  </SelectItem>
                  <SelectItem value="REDEPLOYMENT">
                    {t('modelApprovalRequest.approvalTypeRedeployment', 'Redespliegue')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.approvalType && (
                <p className="text-sm text-red-500">{errors.approvalType}</p>
              )}
            </div>

            {/* Target Environment */}
            <div className="space-y-2">
              <Label htmlFor="targetEnvironment">
                {t('modelApprovalRequest.targetEnvironment', 'Ambiente Objetivo')} *
              </Label>
              <Select value={targetEnvironment} onValueChange={setTargetEnvironment}>
                <SelectTrigger className={errors.targetEnvironment ? 'border-red-500' : ''}>
                  <SelectValue placeholder={t('modelApprovalRequest.targetEnvironmentPlaceholder', 'Seleccione el ambiente objetivo')} />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="STAGING">
                    {t('modelApprovalRequest.environmentStaging', 'Staging')}
                  </SelectItem>
                  <SelectItem value="PRODUCTION">
                    {t('modelApprovalRequest.environmentProduction', 'Producción')}
                  </SelectItem>
                </SelectContent>
              </Select>
              {errors.targetEnvironment && (
                <p className="text-sm text-red-500">{errors.targetEnvironment}</p>
              )}
            </div>

            {/* Business Justification */}
            <div className="space-y-2">
              <Label htmlFor="businessJustification">
                {t('modelApprovalRequest.businessJustification', 'Justificación del Negocio')} *
              </Label>
              <Textarea
                id="businessJustification"
                value={businessJustification}
                onChange={(e) => setBusinessJustification(e.target.value)}
                placeholder={t('modelApprovalRequest.businessJustificationPlaceholder', 'Explique la justificación del negocio para esta aprobación')}
                rows={5}
                className={errors.businessJustification ? 'border-red-500' : ''}
              />
              {errors.businessJustification && (
                <p className="text-sm text-red-500">{errors.businessJustification}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('modelApprovalRequest.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('modelApprovalRequest.saving', 'Enviando...')
                  : t('modelApprovalRequest.submit', 'Enviar Solicitud')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function ModelApprovalRequestPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <ModelApprovalRequestFormContent />
    </Suspense>
  )
}



