// app/(app)/bpmn/forms/complete-documentation/page.tsx
// Migrado de: complete-documentation-form.zul
// Formulario para completar documentación técnica (Anexo IV)
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { ArrowLeft, FileText, Save, AlertTriangle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState, useEffect } from 'react'

function CompleteDocumentationFormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  const taskId = params.id as string
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario
  const [projectId, setProjectId] = useState<string>(projectIdParam || '')
  const [docScore, setDocScore] = useState<number>(0)
  const [docGaps, setDocGaps] = useState<string[]>([])
  const [documentationUpdates, setDocumentationUpdates] = useState<string>('')
  const [annexIvSections, setAnnexIvSections] = useState<Record<string, boolean>>({})
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
            setDocScore(data.variables.docScore || 0)
            setDocGaps(Array.isArray(data.variables.docGaps) ? data.variables.docGaps : [])
            setDocumentationUpdates(data.variables.documentationUpdates || '')
            setComments(data.variables.comments || '')
          }
        })
        .catch(err => console.error('Error cargando datos del workflow:', err))
    }
  }, [taskId, projectId])

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    if (!documentationUpdates || documentationUpdates.trim() === '') {
      newErrors.documentationUpdates = t('bpmnForms.completeDocumentation.errors.documentationUpdatesRequired', 'Debe describir las actualizaciones realizadas a la documentación')
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
        documentationUpdates: documentationUpdates,
        annexIvSections: annexIvSections,
        comments: comments,
        docComplete: true,
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
        alert(error.error || t('bpmnForms.completeDocumentation.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('bpmnForms.completeDocumentation.errors.submitError', 'Error al procesar el formulario'))
    } finally {
      setLoading(false)
    }
  }

  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  const annexIvSectionList = [
    'SECTION_1',
    'SECTION_2',
    'SECTION_3',
    'SECTION_4',
    'SECTION_5',
    'SECTION_6',
    'SECTION_7',
    'SECTION_8'
  ]

  const toggleAnnexSection = (section: string) => {
    setAnnexIvSections(prev => ({
      ...prev,
      [section]: !prev[section]
    }))
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('bpmnForms.completeDocumentation.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <FileText className="w-6 h-6 text-primary" />
            {t('bpmnForms.completeDocumentation.title', 'Completar Documentación Técnica')}
          </h1>
          <p className="text-muted-foreground">
            {t('bpmnForms.completeDocumentation.subtitle', 'Complete la documentación técnica según Anexo IV del EU AI Act')}
          </p>
        </div>
      </div>

      {/* Resultados de documentación */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.completeDocumentation.resultsTitle', 'Estado de la Documentación')}
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Doc Score */}
          <div className="space-y-2">
            <Label>
              {t('bpmnForms.completeDocumentation.docScore', 'Score de Documentación')}
            </Label>
            <div className="flex items-center gap-2">
              <div className="flex-1 bg-gray-200 rounded-full h-2.5">
                <div
                  className={`h-2.5 rounded-full ${
                    docScore >= 0.90 ? 'bg-green-500' : 'bg-red-500'
                  }`}
                  style={{ width: `${docScore * 100}%` }}
                />
              </div>
              <span className="text-sm font-medium">
                {(docScore * 100).toFixed(1)}%
              </span>
            </div>
            {docScore < 0.90 && (
              <div className="flex items-center gap-2 text-amber-600 text-sm">
                <AlertTriangle size={16} />
                <span>
                  {t('bpmnForms.completeDocumentation.docScoreWarning', 'El score de documentación es menor a 0.90. Se requiere completar documentación.')}
                </span>
              </div>
            )}
          </div>

          {/* Doc Gaps */}
          {docGaps.length > 0 && (
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.completeDocumentation.docGaps', 'Gaps de Documentación Detectados')}
              </Label>
              <ul className="list-disc list-inside space-y-1 text-sm">
                {docGaps.map((gap, index) => (
                  <li key={index} className="text-amber-600">
                    {gap}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Formulario de completar documentación */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('bpmnForms.completeDocumentation.formTitle', 'Actualizar Documentación')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Secciones Anexo IV */}
            <div className="space-y-2">
              <Label>
                {t('bpmnForms.completeDocumentation.annexIvSections', 'Secciones del Anexo IV Completadas')}
              </Label>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-2">
                {annexIvSectionList.map((section) => (
                  <div key={section} className="flex items-center space-x-2">
                    <input
                      type="checkbox"
                      id={`section-${section}`}
                      checked={annexIvSections[section] || false}
                      onChange={() => toggleAnnexSection(section)}
                      className="rounded border-gray-300"
                    />
                    <Label htmlFor={`section-${section}`} className="text-sm font-normal cursor-pointer">
                      {t(`bpmnForms.completeDocumentation.sections.${section}`, section)}
                    </Label>
                  </div>
                ))}
              </div>
            </div>

            {/* Documentation Updates */}
            <div className="space-y-2">
              <Label htmlFor="documentationUpdates">
                {t('bpmnForms.completeDocumentation.documentationUpdates', 'Actualizaciones Realizadas')} *
              </Label>
              <Textarea
                id="documentationUpdates"
                value={documentationUpdates}
                onChange={(e) => setDocumentationUpdates(e.target.value)}
                placeholder={t('bpmnForms.completeDocumentation.documentationUpdatesPlaceholder', 'Describa las actualizaciones realizadas a la documentación técnica para resolver los gaps identificados')}
                rows={6}
                className={errors.documentationUpdates ? 'border-red-500' : ''}
              />
              {errors.documentationUpdates && (
                <p className="text-sm text-red-500">{errors.documentationUpdates}</p>
              )}
            </div>

            {/* Comments */}
            <div className="space-y-2">
              <Label htmlFor="comments">
                {t('bpmnForms.completeDocumentation.comments', 'Comentarios')}
              </Label>
              <Textarea
                id="comments"
                value={comments}
                onChange={(e) => setComments(e.target.value)}
                placeholder={t('bpmnForms.completeDocumentation.commentsPlaceholder', 'Comentarios adicionales sobre la documentación')}
                rows={4}
              />
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('bpmnForms.completeDocumentation.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('bpmnForms.completeDocumentation.saving', 'Enviando...')
                  : t('bpmnForms.completeDocumentation.submit', 'Completar Documentación')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function CompleteDocumentationPage() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="spinner-border text-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <CompleteDocumentationFormContent />
    </Suspense>
  )
}
