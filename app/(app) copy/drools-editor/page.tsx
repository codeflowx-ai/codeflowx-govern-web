'use client'

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import { Editor } from '@monaco-editor/react'
import { parseDroolsFile, generateDroolsFile } from './utils/droolsParser'
import { DroolsRuleFile, DroolsRule } from './types'
import { usePageTitle } from '@/components/contexts/PageTitleContext'
import { Save, RefreshCw, Code, Eye, Split, AlertCircle, CheckCircle2, Plus, TestTube } from 'lucide-react'
import { RuleWizard } from './components/RuleWizard'
import { RuleTester } from './components/RuleTester'
import { useToast } from '@/components/ui/toast'

interface DroolsRuleInfo {
  category: string
  fileName: string
  packageName: string
  ruleCount: number
  lastModified: string
}

export default function DroolsEditorPage() {
  const { setPageTitle } = usePageTitle()
  const { toast } = useToast()
  const [categories, setCategories] = useState<Record<string, DroolsRuleInfo[]>>({})
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null)
  const [selectedFile, setSelectedFile] = useState<string | null>(null)
  const [ruleFile, setRuleFile] = useState<DroolsRuleFile | null>(null)
  const [rawContent, setRawContent] = useState<string>('')
  const [viewMode, setViewMode] = useState<'visual' | 'code' | 'split'>('split')
  const [selectedRule, setSelectedRule] = useState<DroolsRule | null>(null)
  const [validationErrors, setValidationErrors] = useState<string[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [isSaving, setIsSaving] = useState(false)
  const [hasChanges, setHasChanges] = useState(false)
  const [showWizard, setShowWizard] = useState(false)
  const [showTester, setShowTester] = useState(false)

  useEffect(() => {
    setPageTitle('Editor de Reglas Drools')
  }, [setPageTitle])

  // Cargar lista de categorías y archivos
  useEffect(() => {
    loadCategories()
  }, [])

  const loadCategories = async () => {
    try {
      setIsLoading(true)
      const response = await fetch('/api/drools/rules')
      if (response.ok) {
        const data = await response.json()
        setCategories(data)
      }
    } catch (error) {
      console.error('Error loading categories:', error)
    } finally {
      setIsLoading(false)
    }
  }

  // Cargar archivo seleccionado
  const loadFile = async (category: string, fileName: string) => {
    try {
      setIsLoading(true)
      const response = await fetch(`/api/drools/rules/${category}/${fileName}`)
      if (response.ok) {
        const content = await response.text()
        setRawContent(content)
        try {
          const parsed = parseDroolsFile(content)
          parsed.category = category
          parsed.fileName = fileName
          setRuleFile(parsed)
        } catch (error) {
          console.error('Error parsing file:', error)
        }
        setSelectedCategory(category)
        setSelectedFile(fileName)
        setHasChanges(false)
        setValidationErrors([])
      }
    } catch (error) {
      console.error('Error loading file:', error)
    } finally {
      setIsLoading(false)
    }
  }

  // Guardar archivo
  const saveFile = async () => {
    if (!selectedCategory || !selectedFile) return

    try {
      setIsSaving(true)

      // Validar antes de guardar
      const validationResponse = await fetch('/api/drools/rules/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: rawContent }),
      })

      const validation = await validationResponse.json()

      if (!validation.valid) {
        setValidationErrors(validation.errors || [])
        toast({
          title: 'Errores de validación',
          description: validation.errors.join(', '),
          variant: 'error',
        })
        return
      }

      // Guardar
      const saveResponse = await fetch(
        `/api/drools/rules/${selectedCategory}/${selectedFile}`,
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ content: rawContent }),
        }
      )

      const result = await saveResponse.json()

      if (result.success) {
        setHasChanges(false)
        setValidationErrors([])
        toast({
          title: 'Éxito',
          description: 'Reglas guardadas exitosamente',
          variant: 'success',
        })
      } else {
        setValidationErrors(result.errors || [])
        toast({
          title: 'Error al guardar',
          description: result.errors?.join(', ') || 'Error desconocido',
          variant: 'error',
        })
      }
    } catch (error) {
      console.error('Error saving file:', error)
      toast({
        title: 'Error',
        description: 'Error al guardar el archivo',
        variant: 'error',
      })
    } finally {
      setIsSaving(false)
    }
  }

  // Sincronizar código con modelo visual
  const syncCodeToVisual = () => {
    if (rawContent) {
      try {
        const parsed = parseDroolsFile(rawContent)
        if (selectedCategory && selectedFile) {
          parsed.category = selectedCategory
          parsed.fileName = selectedFile
        }
        setRuleFile(parsed)
        setHasChanges(true)
      } catch (error) {
        console.error('Error parsing:', error)
        toast({
          title: 'Error de sintaxis',
          description: 'Error al parsear el código. Verifique la sintaxis.',
          variant: 'error',
        })
      }
    }
  }

  // Sincronizar modelo visual con código
  const syncVisualToCode = () => {
    if (ruleFile) {
      const generated = generateDroolsFile(ruleFile)
      setRawContent(generated)
      setHasChanges(true)
    }
  }

  // Validar código
  const validateCode = async () => {
    if (!rawContent) return

    try {
      const validationResponse = await fetch('/api/drools/rules/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: rawContent }),
      })

      const validation = await validationResponse.json()
      setValidationErrors(validation.errors || [])

      if (validation.valid) {
        toast({
          title: 'Validación exitosa',
          description: 'El código es válido',
          variant: 'success',
        })
      } else {
        toast({
          title: 'Errores de validación',
          description: validation.errors.join(', '),
          variant: 'error',
        })
      }
    } catch (error) {
      console.error('Error validating:', error)
    }
  }

  const handleCodeChange = (value: string | undefined) => {
    setRawContent(value || '')
    setHasChanges(true)
  }

  const handleWizardComplete = (newRule: DroolsRule) => {
    if (ruleFile) {
      const updated = {
        ...ruleFile,
        rules: [...ruleFile.rules, { ...newRule, id: newRule.id || Date.now().toString() }],
      }
      setRuleFile(updated)
      const generated = generateDroolsFile(updated)
      setRawContent(generated)
      setHasChanges(true)
    }
    setShowWizard(false)
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white border-b p-4">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Editor de Reglas Drools</h1>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowWizard(true)}
            >
              <Plus className="w-4 h-4 mr-2" />
              Nueva Regla
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowTester(true)}
              disabled={!rawContent}
            >
              <TestTube className="w-4 h-4 mr-2" />
              Probar
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={syncCodeToVisual}
              disabled={!rawContent}
            >
              <RefreshCw className="w-4 h-4 mr-2" />
              Sincronizar → Visual
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={syncVisualToCode}
              disabled={!ruleFile}
            >
              <RefreshCw className="w-4 h-4 mr-2" />
              Sincronizar → Código
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={validateCode}
              disabled={!rawContent}
            >
              <CheckCircle2 className="w-4 h-4 mr-2" />
              Validar
            </Button>
            <Button
              variant="primary"
              size="sm"
              onClick={saveFile}
              disabled={!selectedFile || isSaving}
            >
              <Save className="w-4 h-4 mr-2" />
              {isSaving ? 'Guardando...' : 'Guardar'}
            </Button>
          </div>
        </div>

        {/* Modos de vista */}
        <div className="flex gap-2 mt-4">
          <Button
            variant={viewMode === 'code' ? 'primary' : 'outline'}
            size="sm"
            onClick={() => setViewMode('code')}
          >
            <Code className="w-4 h-4 mr-2" />
            Código
          </Button>
          <Button
            variant={viewMode === 'visual' ? 'primary' : 'outline'}
            size="sm"
            onClick={() => setViewMode('visual')}
          >
            <Eye className="w-4 h-4 mr-2" />
            Visual
          </Button>
          <Button
            variant={viewMode === 'split' ? 'primary' : 'outline'}
            size="sm"
            onClick={() => setViewMode('split')}
          >
            <Split className="w-4 h-4 mr-2" />
            Dividido
          </Button>
        </div>

        {/* Errores de validación */}
        {validationErrors.length > 0 && (
          <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded">
            <div className="flex items-center gap-2 text-red-800">
              <AlertCircle className="w-5 h-5" />
              <strong>Errores de validación:</strong>
            </div>
            <ul className="mt-2 list-disc list-inside text-sm text-red-700">
              {validationErrors.map((error, idx) => (
                <li key={idx}>{error}</li>
              ))}
            </ul>
          </div>
        )}
      </div>

      <div className="flex h-[calc(100vh-180px)]">
        {/* Sidebar - Lista de archivos */}
        <div className="w-64 bg-white border-r p-4 overflow-auto">
          <h2 className="font-semibold mb-4">Categorías</h2>
          {isLoading ? (
            <p className="text-sm text-gray-500">Cargando...</p>
          ) : (
            Object.keys(categories).map(category => (
              <div key={category} className="mb-4">
                <button
                  onClick={() => setSelectedCategory(category)}
                  className={`font-medium text-sm w-full text-left p-2 rounded ${
                    selectedCategory === category
                      ? 'bg-primary-100 text-primary-700'
                      : 'hover:bg-gray-100'
                  }`}
                >
                  {category}
                </button>
                {selectedCategory === category && categories[category] && (
                  <div className="mt-2 ml-4 space-y-1">
                    {categories[category].map(file => (
                      <button
                        key={file.fileName}
                        onClick={() => loadFile(category, file.fileName)}
                        className={`text-xs w-full text-left p-2 rounded ${
                          selectedFile === file.fileName
                            ? 'bg-primary-200 text-primary-800'
                            : 'hover:bg-gray-50'
                        }`}
                      >
                        {file.fileName}
                        <span className="block text-gray-500 text-xs">
                          {file.ruleCount} regla{file.ruleCount !== 1 ? 's' : ''}
                        </span>
                      </button>
                    ))}
                  </div>
                )}
              </div>
            ))
          )}
        </div>

        {/* Main Content */}
        <div className="flex-1 flex overflow-hidden">
          {viewMode === 'code' || viewMode === 'split' ? (
            <div className={viewMode === 'split' ? 'w-1/2 border-r flex flex-col' : 'w-full flex flex-col'}>
              <div className="p-2 bg-gray-100 border-b">
                <span className="text-sm font-medium">Editor de Código</span>
                {hasChanges && (
                  <span className="ml-2 text-xs text-yellow-600">● Sin guardar</span>
                )}
              </div>
              <div className="flex-1">
                <Editor
                  height="100%"
                  language="java"
                  value={rawContent}
                  onChange={handleCodeChange}
                  theme="vs-dark"
                  options={{
                    minimap: { enabled: false },
                    fontSize: 14,
                    wordWrap: 'on',
                    automaticLayout: true,
                  }}
                />
              </div>
            </div>
          ) : null}

          {viewMode === 'visual' || viewMode === 'split' ? (
            <div className={viewMode === 'split' ? 'w-1/2 p-4 overflow-auto' : 'w-full p-4 overflow-auto'}>
              {ruleFile ? (
                <div className="space-y-4">
                  {/* Información del archivo */}
                  <Card>
                    <CardHeader>
                      <CardTitle>Información del Archivo</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p className="text-sm">
                        <strong>Package:</strong> {ruleFile.package}
                      </p>
                      <p className="text-sm mt-2">
                        <strong>Imports:</strong> {ruleFile.imports.length > 0 ? ruleFile.imports.join(', ') : 'Ninguno'}
                      </p>
                      <p className="text-sm mt-2">
                        <strong>Reglas:</strong> {ruleFile.rules.length}
                      </p>
                      {ruleFile.globals.length > 0 && (
                        <p className="text-sm mt-2">
                          <strong>Globals:</strong> {ruleFile.globals.map(g => `${g.type} ${g.name}`).join(', ')}
                        </p>
                      )}
                    </CardContent>
                  </Card>

                  {/* Lista de reglas */}
                  {ruleFile.rules.map(rule => (
                    <div
                      onClick={() => setSelectedRule(rule)}
                      className="cursor-pointer"
                    >
                      <Card
                        key={rule.id}
                        className={`transition-all ${
                          selectedRule?.id === rule.id ? 'border-primary-500 border-2' : ''
                        }`}
                      >
                      <CardHeader>
                        <CardTitle className="text-lg">{rule.name}</CardTitle>
                        <p className="text-sm text-muted-foreground">
                          Salience: {rule.salience}
                        </p>
                        {rule.documentation && (
                          <p className="text-sm text-gray-600 mt-2">{rule.documentation}</p>
                        )}
                      </CardHeader>
                      <CardContent>
                        <div className="space-y-3">
                          <div>
                            <strong className="text-sm">When:</strong>
                            {rule.when.map((condition, idx) => (
                              <div key={idx} className="ml-4 mt-2 p-2 bg-gray-50 rounded">
                                <span className="text-sm font-mono text-primary-600">
                                  {condition.factVariable}
                                </span>
                                {' : '}
                                <span className="text-sm font-mono text-blue-600">
                                  {condition.factType}
                                </span>
                                {condition.conditions.length > 0 && (
                                  <div className="ml-4 mt-1 space-y-1">
                                    {condition.conditions.map((cond, cIdx) => (
                                      <div key={cIdx} className="text-xs font-mono">
                                        <span className="text-gray-700">{cond.field}</span>
                                        {' '}
                                        <span className="text-orange-600">{cond.operator}</span>
                                        {' '}
                                        <span className="text-green-600">
                                          {typeof cond.value === 'string' ? `"${cond.value}"` : String(cond.value)}
                                        </span>
                                      </div>
                                    ))}
                                  </div>
                                )}
                              </div>
                            ))}
                          </div>
                          <div>
                            <strong className="text-sm">Then:</strong>
                            {rule.then.map((action, idx) => (
                              <div key={idx} className="ml-4 mt-2 p-2 bg-gray-50 rounded">
                                <span className="text-xs font-mono">
                                  <span className="text-purple-600">{action.type}</span>
                                  {action.target && (
                                    <>
                                      {' '}
                                      <span className="text-primary-600">{action.target}</span>
                                    </>
                                  )}
                                  {action.method && (
                                    <>
                                      .<span className="text-blue-600">{action.method}</span>
                                    </>
                                  )}
                                  {action.value !== undefined && (
                                    <>
                                      (
                                      <span className="text-green-600">
                                        {typeof action.value === 'string' ? `"${action.value}"` : String(action.value)}
                                      </span>
                                      )
                                    </>
                                  )}
                                  {action.message && (
                                    <>
                                      {' '}
                                      <span className="text-gray-600">"{action.message}"</span>
                                    </>
                                  )}
                                </span>
                              </div>
                            ))}
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="flex items-center justify-center h-full text-gray-500">
                  <div className="text-center">
                    <p className="text-lg mb-2">Seleccione un archivo para editar</p>
                    <p className="text-sm">Elija una categoría y un archivo .drl del panel lateral</p>
                  </div>
                </div>
              )}
            </div>
          ) : null}
        </div>
      </div>

      {/* Wizard Modal */}
      {showWizard && (
        <RuleWizard
          onComplete={handleWizardComplete}
          onCancel={() => setShowWizard(false)}
        />
      )}

      {/* Tester Modal */}
      {showTester && (
        <div className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50">
          <Card className="w-full max-w-2xl max-h-[90vh] overflow-auto bg-white dark:bg-gray-800">
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>Probador de Reglas</CardTitle>
                <Button variant="ghost" size="sm" onClick={() => setShowTester(false)}>
                  Cerrar
                </Button>
              </div>
            </CardHeader>
            <CardContent className="bg-white dark:bg-gray-800">
              <RuleTester ruleContent={rawContent} />
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  )
}
