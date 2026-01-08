/**
 * Plantilla Prompty - Metodología estructurada para prompts de IA
 *
 * Prompty es un formato estándar que organiza los prompts en secciones claras:
 * - Name: Nombre del prompt
 * - Description: Descripción del propósito
 * - Instructions: Instrucciones detalladas
 * - Input Variables: Variables de entrada
 * - Output Format: Formato de salida esperado
 * - Examples: Ejemplos de uso
 * - Constraints: Restricciones y limitaciones
 */

export const PROMPTY_TEMPLATE = `---
name: "Nombre del Prompt"
description: "Descripción breve del propósito del prompt"
version: "1.0.0"
author: ""
tags: []
---

# Instructions

Escribe aquí las instrucciones principales del prompt. Estas instrucciones deben ser claras, específicas y orientadas a la tarea que el modelo debe realizar.

## Context

Proporciona contexto adicional que ayude al modelo a entender mejor la tarea.

## Input Variables

Define las variables de entrada que el prompt espera:

- \`variable1\`: Descripción de la variable 1
- \`variable2\`: Descripción de la variable 2

## Output Format

Especifica el formato esperado de la salida:

- Formato JSON
- Formato de texto estructurado
- Formato de lista
- etc.

## Examples

### Example 1
**Input:**
\`\`\`
Ejemplo de entrada 1
\`\`\`

**Output:**
\`\`\`
Ejemplo de salida esperada 1
\`\`\`

### Example 2
**Input:**
\`\`\`
Ejemplo de entrada 2
\`\`\`

**Output:**
\`\`\`
Ejemplo de salida esperada 2
\`\`\`

## Constraints

- Restricción 1
- Restricción 2
- Restricción 3

## Notes

Notas adicionales sobre el uso del prompt, consideraciones especiales, etc.
`

export const getPromptyTemplate = (name?: string, description?: string): string => {
  return PROMPTY_TEMPLATE
    .replace('"Nombre del Prompt"', name ? `"${name}"` : '"Nombre del Prompt"')
    .replace('"Descripción breve del propósito del prompt"', description ? `"${description}"` : '"Descripción breve del propósito del prompt"')
}
