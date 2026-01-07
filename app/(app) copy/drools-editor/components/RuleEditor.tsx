'use client'

import { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { DroolsRule, DroolsCondition, DroolsAction, ConditionExpression } from '../types'
import { Plus, Trash2 } from 'lucide-react'
import { v4 as uuidv4 } from 'uuid'

interface RuleEditorProps {
  rule: DroolsRule
  onChange: (rule: DroolsRule) => void
}

export function RuleEditor({ rule, onChange }: RuleEditorProps) {
  const [editedRule, setEditedRule] = useState<DroolsRule>(rule)

  const updateRule = (updates: Partial<DroolsRule>) => {
    const updated = { ...editedRule, ...updates }
    setEditedRule(updated)
    onChange(updated)
  }

  const addCondition = () => {
    const newCondition: DroolsCondition = {
      id: uuidv4(),
      factVariable: '$fact',
      factType: 'FactClass',
      conditions: [],
    }
    updateRule({
      when: [...editedRule.when, newCondition],
    })
  }

  const removeCondition = (conditionId: string) => {
    updateRule({
      when: editedRule.when.filter(c => c.id !== conditionId),
    })
  }

  const updateCondition = (conditionId: string, updates: Partial<DroolsCondition>) => {
    updateRule({
      when: editedRule.when.map(c =>
        c.id === conditionId ? { ...c, ...updates } : c
      ),
    })
  }

  const addConditionExpression = (conditionId: string) => {
    const newExpr: ConditionExpression = {
      id: uuidv4(),
      field: 'field',
      operator: '==',
      value: '',
    }
    updateCondition(conditionId, {
      conditions: [
        ...editedRule.when.find(c => c.id === conditionId)?.conditions || [],
        newExpr,
      ],
    })
  }

  const removeConditionExpression = (conditionId: string, exprId: string) => {
    const condition = editedRule.when.find(c => c.id === conditionId)
    if (condition) {
      updateCondition(conditionId, {
        conditions: condition.conditions.filter(e => e.id !== exprId),
      })
    }
  }

  const updateConditionExpression = (
    conditionId: string,
    exprId: string,
    updates: Partial<ConditionExpression>
  ) => {
    const condition = editedRule.when.find(c => c.id === conditionId)
    if (condition) {
      updateCondition(conditionId, {
        conditions: condition.conditions.map(e =>
          e.id === exprId ? { ...e, ...updates } : e
        ),
      })
    }
  }

  const addAction = () => {
    const newAction: DroolsAction = {
      id: uuidv4(),
      type: 'log',
      target: '$fact',
      message: '',
    }
    updateRule({
      then: [...editedRule.then, newAction],
    })
  }

  const removeAction = (actionId: string) => {
    updateRule({
      then: editedRule.then.filter(a => a.id !== actionId),
    })
  }

  const updateAction = (actionId: string, updates: Partial<DroolsAction>) => {
    updateRule({
      then: editedRule.then.map(a =>
        a.id === actionId ? { ...a, ...updates } : a
      ),
    })
  }

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader>
          <CardTitle>Editar Regla: {editedRule.name}</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {/* Información básica */}
          <div className="space-y-2">
            <Label>Nombre de la Regla</Label>
            <Input
              value={editedRule.name}
              onChange={(e) => updateRule({ name: e.target.value })}
            />
          </div>

          <div className="space-y-2">
            <Label>Salience</Label>
            <Input
              type="number"
              value={editedRule.salience}
              onChange={(e) => updateRule({ salience: parseInt(e.target.value) || 0 })}
            />
          </div>

          <div className="space-y-2">
            <Label>Documentación</Label>
            <Textarea
              value={editedRule.documentation || ''}
              onChange={(e) => updateRule({ documentation: e.target.value })}
              rows={3}
            />
          </div>
        </CardContent>
      </Card>

      {/* Condiciones (When) */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Condiciones (When)</CardTitle>
            <Button variant="outline" size="sm" onClick={addCondition}>
              <Plus className="w-4 h-4 mr-2" />
              Agregar Condición
            </Button>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          {editedRule.when.map((condition) => (
            <Card key={condition.id} className="bg-gray-50">
              <CardContent className="pt-4 space-y-3">
                <div className="flex items-center justify-between">
                  <div className="flex gap-2 flex-1">
                    <div className="flex-1">
                      <Label>Variable</Label>
                      <Input
                        value={condition.factVariable}
                        onChange={(e) =>
                          updateCondition(condition.id, { factVariable: e.target.value })
                        }
                      />
                    </div>
                    <div className="flex-1">
                      <Label>Tipo de Fact</Label>
                      <Input
                        value={condition.factType}
                        onChange={(e) =>
                          updateCondition(condition.id, { factType: e.target.value })
                        }
                      />
                    </div>
                  </div>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => removeCondition(condition.id)}
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>

                <div>
                  <div className="flex items-center justify-between mb-2">
                    <Label>Expresiones</Label>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => addConditionExpression(condition.id)}
                    >
                      <Plus className="w-4 h-4 mr-2" />
                      Agregar
                    </Button>
                  </div>
                  {condition.conditions.map((expr) => (
                    <div key={expr.id} className="flex gap-2 mb-2">
                      <Input
                        placeholder="Campo"
                        value={expr.field}
                        onChange={(e) =>
                          updateConditionExpression(condition.id, expr.id, {
                            field: e.target.value,
                          })
                        }
                      />
                      <select
                        className="px-3 py-2 border rounded"
                        value={expr.operator}
                        onChange={(e) =>
                          updateConditionExpression(condition.id, expr.id, {
                            operator: e.target.value as ConditionExpression['operator'],
                          })
                        }
                      >
                        <option value="==">==</option>
                        <option value="!=">!=</option>
                        <option value=">">&gt;</option>
                        <option value="<">&lt;</option>
                        <option value=">=">&gt;=</option>
                        <option value="<=">&lt;=</option>
                        <option value="null">null</option>
                        <option value="not null">not null</option>
                      </select>
                      <Input
                        placeholder="Valor"
                        value={expr.value || ''}
                        onChange={(e) =>
                          updateConditionExpression(condition.id, expr.id, {
                            value: e.target.value,
                          })
                        }
                        disabled={expr.operator === 'null' || expr.operator === 'not null'}
                      />
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => removeConditionExpression(condition.id, expr.id)}
                      >
                        <Trash2 className="w-4 h-4" />
                      </Button>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          ))}
        </CardContent>
      </Card>

      {/* Acciones (Then) */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Acciones (Then)</CardTitle>
            <Button variant="outline" size="sm" onClick={addAction}>
              <Plus className="w-4 h-4 mr-2" />
              Agregar Acción
            </Button>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          {editedRule.then.map((action) => (
            <Card key={action.id} className="bg-gray-50">
              <CardContent className="pt-4">
                <div className="flex items-center gap-2">
                  <select
                    className="px-3 py-2 border rounded flex-1"
                    value={action.type}
                    onChange={(e) =>
                      updateAction(action.id, {
                        type: e.target.value as DroolsAction['type'],
                      })
                    }
                  >
                    <option value="log">Log</option>
                    <option value="setField">Set Field</option>
                    <option value="callMethod">Call Method</option>
                    <option value="update">Update</option>
                    <option value="insert">Insert</option>
                    <option value="retract">Retract</option>
                  </select>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => removeAction(action.id)}
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>

                {action.type === 'log' && (
                  <div className="mt-2">
                    <Input
                      placeholder="Mensaje"
                      value={action.message || ''}
                      onChange={(e) =>
                        updateAction(action.id, { message: e.target.value })
                      }
                    />
                  </div>
                )}

                {(action.type === 'setField' || action.type === 'callMethod') && (
                  <div className="mt-2 space-y-2">
                    <Input
                      placeholder="Target (ej: $fact)"
                      value={action.target}
                      onChange={(e) =>
                        updateAction(action.id, { target: e.target.value })
                      }
                    />
                    <Input
                      placeholder="Método (ej: setDecision)"
                      value={action.method || ''}
                      onChange={(e) =>
                        updateAction(action.id, { method: e.target.value })
                      }
                    />
                    <Input
                      placeholder="Valor"
                      value={action.value || ''}
                      onChange={(e) =>
                        updateAction(action.id, { value: e.target.value })
                      }
                    />
                  </div>
                )}

                {(action.type === 'update' || action.type === 'insert' || action.type === 'retract') && (
                  <div className="mt-2">
                    <Input
                      placeholder="Target (ej: $fact)"
                      value={action.target}
                      onChange={(e) =>
                        updateAction(action.id, { target: e.target.value })
                      }
                    />
                  </div>
                )}
              </CardContent>
            </Card>
          ))}
        </CardContent>
      </Card>
    </div>
  )
}


