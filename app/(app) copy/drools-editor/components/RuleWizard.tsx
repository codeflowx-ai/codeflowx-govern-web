'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { DroolsRule, DroolsAction, ConditionExpression } from '../types'
import { v4 as uuidv4 } from 'uuid'
import { ArrowLeft, ArrowRight, Check } from 'lucide-react'

interface RuleWizardProps {
  onComplete: (rule: DroolsRule) => void
  onCancel: () => void
}

const AVAILABLE_FACTS = [
  'AgentApprovalFact',
  'BiasDetectionFact',
  'AlertClassificationFact',
  'DatasetQualityFact',
  'ModelApprovalFact',
  'DriftDetectionFact',
  'LlmEvaluationFact',
]

export function RuleWizard({ onComplete, onCancel }: RuleWizardProps) {
  const [step, setStep] = useState(1)
  const [rule, setRule] = useState<Partial<DroolsRule>>({
    name: '',
    salience: 100,
    when: [],
    then: [],
    enabled: true,
  })

  const handleNext = () => {
    if (step < 5) {
      setStep(step + 1)
    }
  }

  const handlePrevious = () => {
    if (step > 1) {
      setStep(step - 1)
    }
  }

  const handleFinish = () => {
    if (rule.name && rule.when && rule.when.length > 0 && rule.then && rule.then.length > 0) {
      onComplete(rule as DroolsRule)
    }
  }

  // Paso 1: Información básica
  const renderStep1 = () => (
    <div className="space-y-4">
      <div className="space-y-2">
        <Label>Nombre de la Regla *</Label>
        <Input
          value={rule.name || ''}
          onChange={(e) => setRule({ ...rule, name: e.target.value })}
          placeholder="Ej: No Bias Detected - Auto-Approve"
        />
      </div>

      <div className="space-y-2">
        <Label>Salience (Prioridad)</Label>
        <Input
          type="number"
          value={rule.salience || 100}
          onChange={(e) => setRule({ ...rule, salience: parseInt(e.target.value) || 100 })}
        />
        <p className="text-xs text-gray-500">
          Mayor número = mayor prioridad. Por defecto: 100
        </p>
      </div>

      <div className="space-y-2">
        <Label>Documentación (Opcional)</Label>
        <Textarea
          value={rule.documentation || ''}
          onChange={(e) => setRule({ ...rule, documentation: e.target.value })}
          rows={3}
          placeholder="Descripción de la regla..."
        />
      </div>
    </div>
  )

  // Paso 2: Seleccionar Fact
  const renderStep2 = () => {
    const [factVariable, setFactVariable] = useState('$fact')
    const [factType, setFactType] = useState('')

    const handleAddFact = () => {
      if (factType) {
        setRule({
          ...rule,
          when: [
            ...(rule.when || []),
            {
              id: uuidv4(),
              factVariable,
              factType,
              conditions: [],
            },
          ],
        })
        setFactType('')
      }
    }

    return (
      <div className="space-y-4">
        <div className="space-y-2">
          <Label>Variable del Fact</Label>
          <Input
            value={factVariable}
            onChange={(e) => setFactVariable(e.target.value)}
            placeholder="$fact"
          />
        </div>

        <div className="space-y-2">
          <Label>Tipo de Fact *</Label>
          <select
            className="w-full px-3 py-2 border rounded"
            value={factType}
            onChange={(e) => setFactType(e.target.value)}
          >
            <option value="">Seleccione un Fact...</option>
            {AVAILABLE_FACTS.map(fact => (
              <option key={fact} value={fact}>{fact}</option>
            ))}
          </select>
        </div>

        <Button variant="outline" onClick={handleAddFact} disabled={!factType}>
          Agregar Fact
        </Button>

        {rule.when && rule.when.length > 0 && (
          <div className="mt-4">
            <Label>Facts Agregados:</Label>
            <div className="mt-2 space-y-2">
              {rule.when.map((condition) => (
                <div key={condition.id} className="p-2 bg-gray-50 rounded text-sm">
                  {condition.factVariable} : {condition.factType}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    )
  }

  // Paso 3: Agregar Condiciones
  const renderStep3 = () => {
    const selectedCondition = rule.when?.[0] // Simplificado: solo primera condición
    const [field, setField] = useState('')
    const [operator, setOperator] = useState<ConditionExpression['operator']>('==')
    const [value, setValue] = useState('')

    const handleAddCondition = () => {
      if (field && selectedCondition) {
        const updatedWhen = rule.when?.map(cond => {
          if (cond.id === selectedCondition.id) {
            return {
              ...cond,
              conditions: [
                ...cond.conditions,
                {
                  id: uuidv4(),
                  field,
                  operator,
                  value: operator === 'null' || operator === 'not null' ? null : value,
                },
              ],
            }
          }
          return cond
        })
        setRule({ ...rule, when: updatedWhen })
        setField('')
        setValue('')
      }
    }

    if (!selectedCondition) {
      return <p className="text-gray-500">Primero debe agregar un Fact en el paso anterior</p>
    }

    return (
      <div className="space-y-4">
        <div className="p-3 bg-blue-50 rounded">
          <p className="text-sm">
            <strong>Fact seleccionado:</strong> {selectedCondition.factVariable} : {selectedCondition.factType}
          </p>
        </div>

        <div className="space-y-2">
          <Label>Campo del Fact</Label>
          <Input
            value={field}
            onChange={(e) => setField(e.target.value)}
            placeholder="Ej: demographicParity"
          />
        </div>

        <div className="space-y-2">
          <Label>Operador</Label>
          <select
            className="w-full px-3 py-2 border rounded"
            value={operator}
            onChange={(e) => setOperator(e.target.value as ConditionExpression['operator'])}
          >
            <option value="==">== (Igual)</option>
            <option value="!=">!= (Diferente)</option>
            <option value=">">&gt; (Mayor que)</option>
            <option value="<">&lt; (Menor que)</option>
            <option value=">=">&gt;= (Mayor o igual)</option>
            <option value="<=">&lt;= (Menor o igual)</option>
            <option value="null">== null</option>
            <option value="not null">!= null</option>
          </select>
        </div>

        <div className="space-y-2">
          <Label>Valor</Label>
          <Input
            value={value}
            onChange={(e) => setValue(e.target.value)}
            placeholder="Ej: 0.80"
            disabled={operator === 'null' || operator === 'not null'}
          />
        </div>

        <Button variant="outline" onClick={handleAddCondition} disabled={!field}>
          Agregar Condición
        </Button>

        {selectedCondition.conditions.length > 0 && (
          <div className="mt-4">
            <Label>Condiciones Agregadas:</Label>
            <div className="mt-2 space-y-2">
              {selectedCondition.conditions.map((cond) => (
                <div key={cond.id} className="p-2 bg-gray-50 rounded text-sm font-mono">
                  {cond.field} {cond.operator} {cond.value !== null ? cond.value : ''}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    )
  }

  // Paso 4: Agregar Acciones
  const renderStep4 = () => {
    const [actionType, setActionType] = useState<DroolsAction['type']>('log')
    const [target, setTarget] = useState('$fact')
    const [method, setMethod] = useState('')
    const [actionValue, setActionValue] = useState('')
    const [message, setMessage] = useState('')

    const handleAddAction = () => {
      const newAction: DroolsAction = {
        id: uuidv4(),
        type: actionType,
        target: actionType !== 'log' ? target : '',
        method: actionType === 'setField' || actionType === 'callMethod' ? method : undefined,
        value: actionType === 'setField' || actionType === 'callMethod' ? actionValue : undefined,
        message: actionType === 'log' ? message : undefined,
      }
      setRule({
        ...rule,
        then: [...(rule.then || []), newAction],
      })
      setMethod('')
      setActionValue('')
      setMessage('')
    }

    return (
      <div className="space-y-4">
        <div className="space-y-2">
          <Label>Tipo de Acción</Label>
          <select
            className="w-full px-3 py-2 border rounded"
            value={actionType}
            onChange={(e) => setActionType(e.target.value as DroolsAction['type'])}
          >
            <option value="log">Log (Registrar mensaje)</option>
            <option value="setField">Set Field (Establecer campo)</option>
            <option value="callMethod">Call Method (Llamar método)</option>
            <option value="update">Update (Actualizar fact)</option>
            <option value="insert">Insert (Insertar fact)</option>
            <option value="retract">Retract (Eliminar fact)</option>
          </select>
        </div>

        {actionType === 'log' && (
          <div className="space-y-2">
            <Label>Mensaje</Label>
            <Input
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              placeholder="Ej: Regla ejecutada correctamente"
            />
          </div>
        )}

        {(actionType === 'setField' || actionType === 'callMethod') && (
          <>
            <div className="space-y-2">
              <Label>Target (Variable del Fact)</Label>
              <Input
                value={target}
                onChange={(e) => setTarget(e.target.value)}
                placeholder="$fact"
              />
            </div>
            <div className="space-y-2">
              <Label>Método</Label>
              <Input
                value={method}
                onChange={(e) => setMethod(e.target.value)}
                placeholder="Ej: setDecision"
              />
            </div>
            <div className="space-y-2">
              <Label>Valor</Label>
              <Input
                value={actionValue}
                onChange={(e) => setActionValue(e.target.value)}
                placeholder="Ej: APPROVED"
              />
            </div>
          </>
        )}

        {(actionType === 'update' || actionType === 'insert' || actionType === 'retract') && (
          <div className="space-y-2">
            <Label>Target (Variable del Fact)</Label>
            <Input
              value={target}
              onChange={(e) => setTarget(e.target.value)}
              placeholder="$fact"
            />
          </div>
        )}

        <Button variant="outline" onClick={handleAddAction}>
          Agregar Acción
        </Button>

        {rule.then && rule.then.length > 0 && (
          <div className="mt-4">
            <Label>Acciones Agregadas:</Label>
            <div className="mt-2 space-y-2">
              {rule.then.map((action) => (
                <div key={action.id} className="p-2 bg-gray-50 rounded text-sm">
                  {action.type}: {action.target}
                  {action.method && `.${action.method}(${action.value})`}
                  {action.message && ` - "${action.message}"`}
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    )
  }

  // Paso 5: Revisar y Crear
  const renderStep5 = () => {
    return (
      <div className="space-y-4">
        <Card>
          <CardHeader>
            <CardTitle>Resumen de la Regla</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <div>
              <strong>Nombre:</strong> {rule.name}
            </div>
            <div>
              <strong>Salience:</strong> {rule.salience}
            </div>
            {rule.documentation && (
              <div>
                <strong>Documentación:</strong> {rule.documentation}
              </div>
            )}
            <div>
              <strong>Condiciones:</strong> {rule.when?.length || 0}
            </div>
            <div>
              <strong>Acciones:</strong> {rule.then?.length || 0}
            </div>
          </CardContent>
        </Card>

        <div className="p-4 bg-yellow-50 border border-yellow-200 rounded">
          <p className="text-sm text-yellow-800">
            Revise la información antes de crear la regla. Una vez creada, podrá editarla desde el editor principal.
          </p>
        </div>
      </div>
    )
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <Card className="w-full max-w-2xl max-h-[90vh] overflow-auto">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Asistente de Creación de Reglas - Paso {step} de 5</CardTitle>
            <Button variant="ghost" size="sm" onClick={onCancel}>
              Cancelar
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          {step === 1 && renderStep1()}
          {step === 2 && renderStep2()}
          {step === 3 && renderStep3()}
          {step === 4 && renderStep4()}
          {step === 5 && renderStep5()}

          <div className="flex justify-between mt-6">
            <Button
              variant="outline"
              onClick={handlePrevious}
              disabled={step === 1}
            >
              <ArrowLeft className="w-4 h-4 mr-2" />
              Anterior
            </Button>
            {step < 5 ? (
              <Button variant="primary" onClick={handleNext}>
                Siguiente
                <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            ) : (
              <Button variant="success" onClick={handleFinish}>
                <Check className="w-4 h-4 mr-2" />
                Crear Regla
              </Button>
            )}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
