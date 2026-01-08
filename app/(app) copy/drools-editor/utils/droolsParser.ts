import { DroolsRuleFile, DroolsRule, DroolsCondition, DroolsAction, ConditionExpression } from '../types'
import { v4 as uuidv4 } from 'uuid'

/**
 * Parsea archivo .drl a modelo interno
 */
export function parseDroolsFile(content: string): DroolsRuleFile {
  const lines = content.split('\n')
  let packageName = ''
  const imports: string[] = []
  const globals: { type: string; name: string }[] = []
  const rules: DroolsRule[] = []

  let i = 0
  while (i < lines.length) {
    const line = lines[i].trim()

    // Parsear package
    if (line.startsWith('package ')) {
      packageName = line.replace('package ', '').replace(';', '').trim()
    }
    // Parsear imports
    else if (line.startsWith('import ')) {
      const importLine = line.replace('import ', '').replace(';', '').trim()
      if (importLine && !importLine.startsWith('static')) {
        imports.push(importLine)
      }
    }
    // Parsear globals
    else if (line.startsWith('global ')) {
      const globalLine = line.replace('global ', '').replace(';', '').trim()
      const parts = globalLine.split(/\s+/)
      if (parts.length >= 2) {
        globals.push({
          type: parts[0],
          name: parts[1],
        })
      }
    }
    // Parsear reglas
    else if (line.startsWith('rule ')) {
      const ruleResult = parseRule(lines, i)
      if (ruleResult.rule) {
        rules.push(ruleResult.rule)
        console.log(`✓ Regla parseada: "${ruleResult.rule.name}" (línea ${i + 1} -> ${ruleResult.nextIndex + 1})`)
      } else {
        console.warn(`⚠ No se pudo parsear regla en línea ${i + 1}`)
      }

      // Asegurarse de avanzar correctamente
      if (ruleResult.nextIndex <= i) {
        console.warn(`⚠ nextIndex (${ruleResult.nextIndex}) <= i (${i}), avanzando manualmente`)
        i++
      } else {
        i = ruleResult.nextIndex
      }
      continue
    }

    i++
  }

  console.log(`📊 Total de reglas parseadas: ${rules.length} de ${content.split(/rule\s+"/).length - 1} encontradas`)

  return {
    package: packageName,
    imports,
    globals,
    rules,
    category: '',
    fileName: '',
  }
}

/**
 * Parsea una regla completa
 */
function parseRule(lines: string[], startIndex: number): { rule: DroolsRule | null; nextIndex: number } {
  let i = startIndex
  let ruleName = ''
  let salience: number | undefined
  let documentation = ''
  const whenConditions: DroolsCondition[] = []
  const thenActions: DroolsAction[] = []

  // Parsear nombre de la regla
  const ruleLine = lines[i].trim()
  const ruleMatch = ruleLine.match(/rule\s+"([^"]+)"/)
  if (ruleMatch) {
    ruleName = ruleMatch[1]
  } else {
    // Si no se puede parsear el nombre, retornar null
    return { rule: null, nextIndex: i + 1 }
  }

  i++

  // Parsear salience, ruleflow-group y documentación hasta encontrar "when"
  while (i < lines.length) {
    const line = lines[i].trim()

    // Si encontramos otra regla, significa que la anterior terminó sin "end" explícito
    if (line.startsWith('rule ')) {
      // Retroceder para que el parser principal la detecte
      i--
      break
    }

    if (line.startsWith('salience ')) {
      const salienceMatch = line.match(/salience\s+(\d+)/)
      if (salienceMatch) {
        salience = parseInt(salienceMatch[1], 10)
      }
    } else if (line.startsWith('ruleflow-group ')) {
      // Ignorar ruleflow-group por ahora
    } else if (line.startsWith('//')) {
      documentation += line.replace('//', '').trim() + '\n'
    } else if (line === 'when' || line.startsWith('when')) {
      break
    } else if (line === 'end' || /^\s*end\s*$/.test(line)) {
      // Encontramos el end antes del when, algo está mal estructurado
      // Pero retornamos la regla de todos modos
      break
    }

    i++
  }

  // Parsear bloque when
  if (i < lines.length && lines[i].trim() === 'when') {
    i++
    const whenResult = parseWhenBlock(lines, i)
    whenConditions.push(...whenResult.conditions)
    i = whenResult.nextIndex
  }

  // Parsear bloque then
  if (i < lines.length && lines[i].trim() === 'then') {
    i++
    const thenResult = parseThenBlock(lines, i)
    thenActions.push(...thenResult.actions)
    i = thenResult.nextIndex

    // parseThenBlock retorna el índice que apunta al "end" (sin avanzar)
    // Verificar que estamos en el "end" y avanzar después
    if (i < lines.length) {
      const trimmedLine = lines[i].trim()
      if (trimmedLine === 'end' || /^\s*end\s*$/.test(trimmedLine)) {
        i++ // Avanzar después del "end"
      } else {
        // No estamos en "end", buscar más adelante
        while (i < lines.length) {
          const trimmed = lines[i].trim()
          if (trimmed === 'end' || /^\s*end\s*$/.test(trimmed)) {
            i++ // Avanzar después del "end"
            break
          }
          if (trimmed.startsWith('rule ')) {
            // Encontramos otra regla antes del "end"
            i--
            break
          }
          i++
        }
      }
    }
  } else {
    // No hay bloque "then", buscar "end" directamente
    while (i < lines.length) {
      const trimmedLine = lines[i].trim()

      // Si encontramos otra regla antes del "end", la regla anterior no tiene "end" explícito
      if (trimmedLine.startsWith('rule ')) {
        // La regla anterior no tiene "end" explícito, pero encontramos otra regla
        // Retroceder para que el parser principal la detecte
        i--
        break
      }

      // Buscar "end" como palabra completa (no "endpoint", "endless", etc.)
      // Puede tener espacios antes/después pero debe ser solo "end"
      if (trimmedLine === 'end' || /^\s*end\s*$/.test(trimmedLine)) {
        i++ // Avanzar después del "end"
        break
      }

      i++
    }
  }

  // Verificar que encontramos el "end" o estamos al final
  if (i >= lines.length) {
    // No se encontró "end", pero tenemos una regla válida
    // Retornar la regla de todos modos
    console.warn(`⚠ Regla "${ruleName}" no tiene "end" explícito, pero se parseó correctamente`)
    return {
      rule: {
        id: uuidv4(),
        name: ruleName,
        salience: salience ?? 0,
        when: whenConditions,
        then: thenActions,
        documentation: documentation.trim() || undefined,
        enabled: true,
      },
      nextIndex: i,
    }
  }

  // Verificar que realmente estamos en el "end"
  const endLine = lines[i].trim()
  if (endLine === 'end' || /^\s*end\s*$/.test(endLine)) {
    // Estamos en el "end", avanzar una línea más para pasar el "end"
    return {
      rule: {
        id: uuidv4(),
        name: ruleName,
        salience: salience ?? 0,
        when: whenConditions,
        then: thenActions,
        documentation: documentation.trim() || undefined,
        enabled: true,
      },
      nextIndex: i + 1, // Avanzar después del "end"
    }
  }

  // Si llegamos aquí, el índice no apunta a "end"
  // Esto puede pasar si parseThenBlock no encontró el "end" correctamente
  // Buscar el "end" más adelante
  let searchIndex = i
  while (searchIndex < lines.length) {
    const trimmed = lines[searchIndex].trim()
    if (trimmed === 'end' || /^\s*end\s*$/.test(trimmed)) {
      // Encontramos el "end", avanzar después
      return {
        rule: {
          id: uuidv4(),
          name: ruleName,
          salience: salience ?? 0,
          when: whenConditions,
          then: thenActions,
          documentation: documentation.trim() || undefined,
          enabled: true,
        },
        nextIndex: searchIndex + 1,
      }
    }
    if (trimmed.startsWith('rule ')) {
      // Encontramos otra regla antes del "end"
      break
    }
    searchIndex++
  }

  // Si no encontramos "end", retornar la regla de todos modos
  console.warn(`⚠ Regla "${ruleName}" - no se encontró "end" explícito, usando índice ${searchIndex}`)
  return {
    rule: {
      id: uuidv4(),
      name: ruleName,
      salience: salience ?? 0,
      when: whenConditions,
      then: thenActions,
      documentation: documentation.trim() || undefined,
      enabled: true,
    },
    nextIndex: searchIndex,
  }
}

/**
 * Parsea bloque when de una regla
 */
function parseWhenBlock(lines: string[], startIndex: number): { conditions: DroolsCondition[]; nextIndex: number } {
  const conditions: DroolsCondition[] = []
  let i = startIndex
  let currentCondition: DroolsCondition | null = null
  const conditionExpressions: ConditionExpression[] = []

  while (i < lines.length) {
    const line = lines[i].trim()

    // Si encontramos otra regla, significa que la anterior terminó sin "end" explícito
    if (line.startsWith('rule ')) {
      // Retroceder para que el parser principal la detecte
      i--
      break
    }

    if (line === 'then' || line === 'end' || /^\s*end\s*$/.test(line)) {
      break
    }

    // Detectar patrón: $fact : FactClass(
    const factMatch = line.match(/\$(\w+)\s*:\s*(\w+)\s*\(/)
    if (factMatch) {
      // Guardar condición anterior si existe
      if (currentCondition) {
        currentCondition.conditions = conditionExpressions
        conditions.push(currentCondition)
      }

      currentCondition = {
        id: uuidv4(),
        factVariable: '$' + factMatch[1],
        factType: factMatch[2],
        conditions: [],
      }
      conditionExpressions.length = 0

      // Parsear condiciones dentro de los paréntesis
      let conditionContent = line
      let parenCount = (line.match(/\(/g) || []).length - (line.match(/\)/g) || []).length

      while (parenCount > 0 && i + 1 < lines.length) {
        i++
        conditionContent += ' ' + lines[i].trim()
        parenCount += (lines[i].match(/\(/g) || []).length - (lines[i].match(/\)/g) || []).length
      }

      // Parsear expresiones de condición
      const expressions = parseConditionExpressions(conditionContent)
      conditionExpressions.push(...expressions)
    } else if (currentCondition && line) {
      // Continuar parseando condiciones en líneas siguientes
      const expressions = parseConditionExpressions(line)
      conditionExpressions.push(...expressions)
    }

    i++
  }

  // Agregar última condición
  if (currentCondition) {
    currentCondition.conditions = conditionExpressions
    conditions.push(currentCondition)
  }

  return { conditions, nextIndex: i }
}

/**
 * Parsea expresiones de condición individuales
 */
function parseConditionExpressions(content: string): ConditionExpression[] {
  const expressions: ConditionExpression[] = []

  // Remover paréntesis externos
  let cleanContent = content.replace(/^\$?\w+\s*:\s*\w+\s*\(/, '').replace(/\)\s*$/, '').trim()

  // Dividir por comas, pero respetar paréntesis anidados
  const parts: string[] = []
  let currentPart = ''
  let depth = 0

  for (let i = 0; i < cleanContent.length; i++) {
    const char = cleanContent[i]
    if (char === '(') depth++
    else if (char === ')') depth--
    else if (char === ',' && depth === 0) {
      if (currentPart.trim()) {
        parts.push(currentPart.trim())
      }
      currentPart = ''
      continue
    }
    currentPart += char
  }

  if (currentPart.trim()) {
    parts.push(currentPart.trim())
  }

  for (const part of parts) {
    const trimmed = part.trim()
    if (!trimmed || trimmed === '||' || trimmed === '&&') continue

    // Parsear operadores: >=, <=, ==, !=, >, <
    let operator: ConditionExpression['operator'] = '=='
    let field = ''
    let value: any = null

    const operators = ['>=', '<=', '!=', '==', '>', '<']
    for (const op of operators) {
      if (trimmed.includes(op)) {
        operator = op as ConditionExpression['operator']
        const [fieldPart, valuePart] = trimmed.split(op).map(s => s.trim())
        field = fieldPart
        value = parseValue(valuePart)
        break
      }
    }

    // Manejar null/not null
    if (trimmed.includes('== null') || trimmed.includes('!= null')) {
      const nullMatch = trimmed.match(/(\w+)\s*(==|!=)\s*null/)
      if (nullMatch) {
        field = nullMatch[1]
        operator = nullMatch[2] === '==' ? 'null' : 'not null'
        value = null
      }
    }

    if (field) {
      expressions.push({
        id: uuidv4(),
        field,
        operator,
        value,
      })
    }
  }

  return expressions
}

/**
 * Parsea un valor (string, número, boolean, etc.)
 */
function parseValue(valueStr: string): any {
  const trimmed = valueStr.trim()

  // String con comillas
  if ((trimmed.startsWith('"') && trimmed.endsWith('"')) ||
      (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
    return trimmed.slice(1, -1)
  }

  // Número
  if (/^-?\d+$/.test(trimmed)) {
    return parseInt(trimmed, 10)
  }

  // Decimal
  if (/^-?\d+\.\d+$/.test(trimmed)) {
    return parseFloat(trimmed)
  }

  // Boolean
  if (trimmed === 'true' || trimmed === 'false') {
    return trimmed === 'true'
  }

  // Variable o expresión
  return trimmed
}

/**
 * Parsea bloque then de una regla
 */
function parseThenBlock(lines: string[], startIndex: number): { actions: DroolsAction[]; nextIndex: number } {
  const actions: DroolsAction[] = []
  let i = startIndex

  while (i < lines.length) {
    const line = lines[i].trim()

    // Si encontramos otra regla, significa que la anterior terminó sin "end" explícito
    if (line.startsWith('rule ')) {
      // Retroceder para que el parser principal la detecte
      i--
      break
    }

    // Buscar "end" como palabra completa (debe estar solo en la línea)
    // IMPORTANTE: "end" debe estar solo, no "endpoint", "endless", etc.
    if (line === 'end' || /^\s*end\s*$/.test(line)) {
      // Retornar el índice que apunta al "end" (sin avanzar)
      // El parser principal avanzará después del "end"
      break
    }

    // Ignorar líneas vacías y comentarios, pero seguir avanzando
    if (!line || line.startsWith('//')) {
      i++
      continue
    }

    // Parsear diferentes tipos de acciones
    const action = parseAction(line)
    if (action) {
      actions.push(action)
    }

    i++
  }

  // Retornar el índice actual (que apunta al "end" o a la siguiente regla)
  // Si no encontramos "end", retornar el índice actual de todos modos
  return { actions, nextIndex: i }
}

/**
 * Parsea una acción individual
 */
function parseAction(line: string): DroolsAction | null {
  // Ignorar líneas vacías o solo con espacios
  if (!line || line.trim().length === 0) {
    return null
  }

  // logger.info("message") o logger.warn("message") etc.
  const logMatch = line.match(/logger\.(info|warn|error|debug)\s*\(\s*"([^"]+)"\s*\)/)
  if (logMatch) {
    return {
      id: uuidv4(),
      type: 'log',
      target: '',
      message: logMatch[2],
    }
  }

  // $fact.setField(value) - más específico primero
  const setFieldMatch = line.match(/\$(\w+)\.set(\w+)\s*\(\s*(.+?)\s*\)/)
  if (setFieldMatch) {
    return {
      id: uuidv4(),
      type: 'setField',
      target: '$' + setFieldMatch[1],
      method: 'set' + setFieldMatch[2],
      value: parseValue(setFieldMatch[3]),
    }
  }

  // $fact.method(value) - método genérico (después de setField para evitar conflictos)
  const methodMatch = line.match(/\$(\w+)\.(\w+)\s*\(\s*(.+?)\s*\)/)
  if (methodMatch) {
    return {
      id: uuidv4(),
      type: 'callMethod',
      target: '$' + methodMatch[1],
      method: methodMatch[2],
      value: parseValue(methodMatch[3]),
    }
  }

  // update($fact)
  const updateMatch = line.match(/update\s*\(\s*\$(\w+)\s*\)/)
  if (updateMatch) {
    return {
      id: uuidv4(),
      type: 'update',
      target: '$' + updateMatch[1],
    }
  }

  // insert($fact)
  const insertMatch = line.match(/insert\s*\(\s*\$(\w+)\s*\)/)
  if (insertMatch) {
    return {
      id: uuidv4(),
      type: 'insert',
      target: '$' + insertMatch[1],
    }
  }

  // retract($fact)
  const retractMatch = line.match(/retract\s*\(\s*\$(\w+)\s*\)/)
  if (retractMatch) {
    return {
      id: uuidv4(),
      type: 'retract',
      target: '$' + retractMatch[1],
    }
  }

  // Si no coincide con ningún patrón conocido, retornar null
  // (puede ser código Java complejo que no parseamos)
  return null
}

/**
 * Genera código DRL desde el modelo interno
 */
export function generateDroolsFile(ruleFile: DroolsRuleFile): string {
  let content = ''

  // Package
  if (ruleFile.package) {
    content += `package ${ruleFile.package};\n\n`
  }

  // Imports
  for (const imp of ruleFile.imports) {
    content += `import ${imp};\n`
  }
  if (ruleFile.imports.length > 0) {
    content += '\n'
  }

  // Globals
  for (const global of ruleFile.globals) {
    content += `global ${global.type} ${global.name};\n`
  }
  if (ruleFile.globals.length > 0) {
    content += '\n'
  }

  // Rules
  for (const rule of ruleFile.rules) {
    if (!rule.enabled) continue

    // Documentación
    if (rule.documentation) {
      const docLines = rule.documentation.split('\n')
      for (const docLine of docLines) {
        if (docLine.trim()) {
          content += `// ${docLine.trim()}\n`
        }
      }
    }

    // Nombre de la regla
    content += `rule "${rule.name}"\n`

    // Salience
    if (rule.salience !== 0) {
      content += `    salience ${rule.salience}\n`
    }

    // When
    content += '    when\n'
    for (const condition of rule.when) {
      content += `        ${condition.factVariable} : ${condition.factType}(\n`

      for (let i = 0; i < condition.conditions.length; i++) {
        const expr = condition.conditions[i]
        const indent = '            '
        const operatorStr = expr.operator === 'null' ? '== null' :
                           expr.operator === 'not null' ? '!= null' :
                           expr.operator
        const valueStr = typeof expr.value === 'string' ? `"${expr.value}"` : String(expr.value)
        const comma = i < condition.conditions.length - 1 ? ',' : ''

        content += `${indent}${expr.field} ${operatorStr} ${valueStr}${comma}\n`
      }

      content += '        )\n'
    }

    // Then
    content += '    then\n'
    for (const action of rule.then) {
      const indent = '        '

      switch (action.type) {
        case 'log':
          if (action.message) {
            content += `${indent}logger.info("${action.message}");\n`
          }
          break
        case 'setField':
          if (action.method && action.value !== undefined) {
            const valueStr = typeof action.value === 'string' ? `"${action.value}"` : String(action.value)
            content += `${indent}${action.target}.${action.method}(${valueStr});\n`
          }
          break
        case 'callMethod':
          if (action.method) {
            const valueStr = action.value !== undefined
              ? (typeof action.value === 'string' ? `"${action.value}"` : String(action.value))
              : ''
            content += `${indent}${action.target}.${action.method}(${valueStr});\n`
          }
          break
        case 'update':
          content += `${indent}update(${action.target});\n`
          break
        case 'insert':
          content += `${indent}insert(${action.target});\n`
          break
        case 'retract':
          content += `${indent}retract(${action.target});\n`
          break
      }
    }

    content += 'end\n\n'
  }

  return content
}
