import { parseDroolsFile } from './droolsParser'
import { DroolsRuleFile } from '../types'

/**
 * Extrae los campos esperados de las reglas para el probador
 */
export function extractExpectedFields(ruleContent: string): {
  factType: string
  fields: Array<{ name: string; type: 'string' | 'number' | 'boolean' | 'unknown'; examples?: string[] }>
} {
  try {
    const parsed = parseDroolsFile(ruleContent)

    if (parsed.rules.length === 0) {
      return { factType: 'Unknown', fields: [] }
    }

    // Obtener el tipo de fact más común
    const factTypes = new Map<string, number>()
    const fieldMap = new Map<string, Set<string>>() // fieldName -> tipos de valores encontrados

    parsed.rules.forEach((rule) => {
      rule.when.forEach((condition) => {
        factTypes.set(condition.factType, (factTypes.get(condition.factType) || 0) + 1)

        condition.conditions.forEach((expr) => {
          if (expr.field) {
            if (!fieldMap.has(expr.field)) {
              fieldMap.set(expr.field, new Set())
            }

            // Inferir tipo basado en el valor
            if (expr.value !== null && expr.value !== undefined) {
              const valueType = typeof expr.value
              if (valueType === 'string') {
                fieldMap.get(expr.field)!.add('string')
              } else if (valueType === 'number') {
                fieldMap.get(expr.field)!.add('number')
              } else if (valueType === 'boolean') {
                fieldMap.get(expr.field)!.add('boolean')
              }
            }

            // Si el operador es null/not null, puede ser cualquier tipo
            if (expr.operator === 'null' || expr.operator === 'not null') {
              fieldMap.get(expr.field)!.add('unknown')
            }
          }
        })
      })
    })

    // Obtener el fact type más común
    let mostCommonFactType = 'Unknown'
    let maxCount = 0
    factTypes.forEach((count, type) => {
      if (count > maxCount) {
        maxCount = count
        mostCommonFactType = type
      }
    })

    // Convertir el mapa de campos a array
    const fields = Array.from(fieldMap.entries()).map(([name, types]) => {
      const typeSet = Array.from(types)
      let inferredType: 'string' | 'number' | 'boolean' | 'unknown' = 'unknown'

      if (typeSet.includes('number')) {
        inferredType = 'number'
      } else if (typeSet.includes('boolean')) {
        inferredType = 'boolean'
      } else if (typeSet.includes('string')) {
        inferredType = 'string'
      }

      // Buscar ejemplos de valores en las condiciones
      const examples: string[] = []
      parsed.rules.forEach((rule) => {
        rule.when.forEach((condition) => {
          condition.conditions.forEach((expr) => {
            if (expr.field === name && expr.value !== null && expr.value !== undefined) {
              const example = String(expr.value)
              if (!examples.includes(example) && examples.length < 3) {
                examples.push(example)
              }
            }
          })
        })
      })

      return {
        name,
        type: inferredType,
        examples: examples.length > 0 ? examples : undefined,
      }
    })

    return {
      factType: mostCommonFactType,
      fields: fields.sort((a, b) => a.name.localeCompare(b.name)),
    }
  } catch (error) {
    console.error('Error extracting fields:', error)
    return { factType: 'Unknown', fields: [] }
  }
}
