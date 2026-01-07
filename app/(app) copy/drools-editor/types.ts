export interface DroolsRule {
  id: string
  name: string
  salience: number
  when: DroolsCondition[]
  then: DroolsAction[]
  documentation?: string
  enabled: boolean
}

export interface DroolsCondition {
  id: string
  factVariable: string // $fact, $item, etc.
  factType: string // FactClass
  conditions: ConditionExpression[]
}

export interface ConditionExpression {
  id: string
  field: string
  operator: '>=' | '<=' | '==' | '!=' | '>' | '<' | 'in' | 'not in' | 'null' | 'not null'
  value: any
  logicalOperator?: '&&' | '||'
}

export interface DroolsAction {
  id: string
  type: 'setField' | 'callMethod' | 'log' | 'update' | 'insert' | 'retract'
  target: string // $fact
  method?: string
  value?: any
  message?: string
}

export interface DroolsRuleFile {
  package: string
  imports: string[]
  globals: { type: string; name: string }[]
  rules: DroolsRule[]
  category: string
  fileName: string
}
