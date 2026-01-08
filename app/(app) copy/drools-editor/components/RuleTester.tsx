'use client'

import { useState, useEffect, useMemo } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Play, Loader2, CheckCircle2, XCircle, RefreshCw } from 'lucide-react'
import { useToast } from '@/components/ui/toast'
import { extractExpectedFields } from '../utils/fieldExtractor'

interface RuleTesterProps {
  ruleContent: string
}

export function RuleTester({ ruleContent }: RuleTesterProps) {
  const { toast } = useToast()
  const [factData, setFactData] = useState<Record<string, any>>({})
  const [testResult, setTestResult] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  // Extraer campos esperados de las reglas
  const expectedFields = useMemo(() => {
    return extractExpectedFields(ruleContent)
  }, [ruleContent])

  // Inicializar campos cuando cambian las reglas
  useEffect(() => {
    if (expectedFields.fields.length > 0) {
      const initialData: Record<string, any> = {}
      expectedFields.fields.forEach((field) => {
        if (!(field.name in factData)) {
          // Inicializar con valor por defecto según el tipo
          if (field.type === 'number') {
            initialData[field.name] = field.examples?.[0] || '0'
          } else if (field.type === 'boolean') {
            initialData[field.name] = 'true'
          } else {
            initialData[field.name] = field.examples?.[0] || ''
          }
        } else {
          initialData[field.name] = factData[field.name]
        }
      })
      setFactData(initialData)
    }
  }, [expectedFields.fields])

  const updateField = (fieldName: string, value: string) => {
    setFactData((prev) => ({
      ...prev,
      [fieldName]: value,
    }))
  }

  const parseValue = (value: string, type: string): any => {
    if (type === 'number') {
      const num = parseFloat(value)
      return isNaN(num) ? value : num
    } else if (type === 'boolean') {
      return value === 'true' || value === 'True' || value === '1'
    }
    return value
  }

  const runTest = async () => {
    if (!ruleContent) {
      toast({
        title: 'Error',
        description: 'No hay contenido de regla para probar',
        variant: 'error',
      })
      return
    }

    setLoading(true)
    setTestResult(null)

    try {
      // Convertir valores según su tipo
      const processedFactData: Record<string, any> = {}
      Object.entries(factData).forEach(([key, value]) => {
        const field = expectedFields.fields.find((f) => f.name === key)
        processedFactData[key] = parseValue(String(value), field?.type || 'string')
      })

      const response = await fetch('/api/drools/rules/test', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ruleContent,
          factData: processedFactData,
        }),
      })

      const result = await response.json()
      setTestResult(result)

      if (result.success) {
        toast({
          title: 'Prueba exitosa',
          description: `${result.firedRules} regla(s) ejecutada(s)`,
          variant: 'success',
        })
      } else {
        toast({
          title: 'Error en la prueba',
          description: result.error || 'Error desconocido',
          variant: 'error',
        })
      }
    } catch (error) {
      console.error('Error testing rule:', error)
      const errorMessage = 'Error al probar la regla: ' + (error as Error).message
      setTestResult({
        success: false,
        error: errorMessage,
      })
      toast({
        title: 'Error',
        description: errorMessage,
        variant: 'error',
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>Probar Regla</CardTitle>
          <Button
            variant="ghost"
            size="sm"
            onClick={() => {
              const initialData: Record<string, any> = {}
              expectedFields.fields.forEach((field) => {
                if (field.type === 'number') {
                  initialData[field.name] = field.examples?.[0] || '0'
                } else if (field.type === 'boolean') {
                  initialData[field.name] = 'true'
                } else {
                  initialData[field.name] = field.examples?.[0] || ''
                }
              })
              setFactData(initialData)
            }}
            title="Resetear valores"
          >
            <RefreshCw className="w-4 h-4" />
          </Button>
        </div>
        {expectedFields.factType !== 'Unknown' && (
          <p className="text-sm text-gray-600 mt-1">
            Fact Type: <span className="font-mono font-semibold">{expectedFields.factType}</span>
          </p>
        )}
      </CardHeader>
      <CardContent className="space-y-4">
        {/* Formulario para ingresar datos del Fact */}
        <div className="space-y-3">
          {expectedFields.fields.length > 0 ? (
            <>
              <Label>Campos del Fact ({expectedFields.factType})</Label>
              <div className="space-y-3">
                {expectedFields.fields.map((field) => (
                  <div key={field.name} className="space-y-1">
                    <div className="flex items-center justify-between">
                      <Label htmlFor={field.name} className="text-sm font-medium">
                        {field.name}
                        <span className="ml-2 text-xs text-gray-500">({field.type})</span>
                      </Label>
                      {field.examples && field.examples.length > 0 && (
                        <span className="text-xs text-gray-400">
                          Ej: {field.examples.slice(0, 2).join(', ')}
                        </span>
                      )}
                    </div>
                    <Input
                      id={field.name}
                      type={field.type === 'number' ? 'number' : field.type === 'boolean' ? 'text' : 'text'}
                      placeholder={
                        field.examples?.[0]
                          ? `Ej: ${field.examples[0]}`
                          : field.type === 'number'
                            ? '0'
                            : field.type === 'boolean'
                              ? 'true o false'
                              : 'Valor'
                      }
                      value={factData[field.name] || ''}
                      onChange={(e) => updateField(field.name, e.target.value)}
                      className="font-mono"
                    />
                  </div>
                ))}
              </div>
            </>
          ) : (
            <div className="text-sm text-gray-500 p-4 bg-gray-50 rounded">
              No se pudieron detectar campos automáticamente. Asegúrate de que las reglas tengan condiciones con campos.
            </div>
          )}
        </div>

        {/* Botón Ejecutar Prueba */}
        <Button
          variant="primary"
          onClick={runTest}
          disabled={loading || !ruleContent}
          className="w-full"
        >
          {loading ? (
            <>
              <Loader2 className="w-4 h-4 mr-2 animate-spin" />
              Ejecutando...
            </>
          ) : (
            <>
              <Play className="w-4 h-4 mr-2" />
              Ejecutar Prueba
            </>
          )}
        </Button>

        {/* Mostrar resultados */}
        {testResult && (
          <div className="mt-4">
            <div
              className={`p-4 rounded ${
                testResult.success
                  ? 'bg-green-50 border border-green-200'
                  : 'bg-red-50 border border-red-200'
              }`}
            >
              <div className="flex items-center gap-2 mb-2">
                {testResult.success ? (
                  <CheckCircle2 className="w-5 h-5 text-green-600" />
                ) : (
                  <XCircle className="w-5 h-5 text-red-600" />
                )}
                <strong
                  className={testResult.success ? 'text-green-800' : 'text-red-800'}
                >
                  {testResult.success ? 'Prueba Exitosa' : 'Error en la Prueba'}
                </strong>
              </div>

              {testResult.success ? (
                <div className="space-y-2 text-sm">
                  <div>
                    <strong>Reglas Ejecutadas:</strong> {testResult.firedRules}
                  </div>
                  {testResult.result && (
                    <div>
                      <strong>Resultado:</strong>
                      <pre className="mt-2 p-2 bg-white rounded text-xs overflow-auto">
                        {JSON.stringify(testResult.result, null, 2)}
                      </pre>
                    </div>
                  )}
                </div>
              ) : (
                <div className="text-sm text-red-800">
                  <strong>Error:</strong> {testResult.error}
                </div>
              )}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  )
}


