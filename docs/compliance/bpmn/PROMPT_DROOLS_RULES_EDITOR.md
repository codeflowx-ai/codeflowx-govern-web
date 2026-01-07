# PROMPT: Editor de Reglas Drools - Crear y Editar Reglas de Negocio

**Fecha:** Enero 2025
**Objetivo:** Crear un editor visual para crear, editar y validar reglas Drools (.drl)
**Prioridad:** 🟡 MEDIA
**Estimación:** 4-6 días

---

## 📋 CONTEXTO

### Estado Actual

**Reglas Drools:**
- **Ubicación:** `nocode-service/codeflowx.govern.workflow.lib/src/main/resources/rules/`
- **Total:** 11 archivos `.drl` organizados por categorías
- **Categorías:**
  - `agent/` - 1 archivo
  - `aios/` - 5 archivos (external, marketplace, onboarding, policy, runtime)
  - `alert/` - 1 archivo
  - `bias/` - 1 archivo
  - `dataset/` - 1 archivo
  - `drift/` - 1 archivo
  - `evaluation/` - 1 archivo

**Estructura de Reglas Drools:**
```drools
package com.codeflowx.govern.workflow.drools.{category};

import com.codeflowx.govern.workflow.drools.facts.{FactClass};
import java.util.ArrayList;

global org.slf4j.Logger logger;

rule "Rule Name"
    salience 100
    when
        $fact : FactClass(
            field1 >= value1,
            field2 == value2,
            field3 != null
        )
    then
        logger.info("Rule fired");
        $fact.setDecision("APPROVED");
        update($fact);
end
```

**Facts Disponibles:**
- `AgentApprovalFact`
- `BiasDetectionFact`
- `AlertClassificationFact`
- `DatasetQualityFact`
- `ModelApprovalFact`
- `DriftDetectionFact`
- `LlmEvaluationFact`
- Y otros...

### Stack Tecnológico

- **Frontend:** Next.js 14, React, TypeScript
- **Backend:** Spring Boot, Drools 7.74.1
- **Formato:** Archivos `.drl` (Drools Rule Language)
- **Librería:** `codeflowx.govern.workflow.lib` (JAR compartido)

---

## 🎯 OBJETIVOS

1. **Visualizar reglas Drools existentes** desde archivos `.drl`
2. **Editar reglas** con editor visual o código
3. **Crear nuevas reglas** con asistente guiado
4. **Validar sintaxis Drools** antes de guardar
5. **Probar reglas** con datos de ejemplo
6. **Organizar reglas** por categorías

---

## 📐 ARQUITECTURA PROPUESTA

### Componentes Necesarios

```
Frontend (Next.js)
├── Drools Rules Editor (page.tsx)
│   ├── Lista de Reglas (sidebar)
│   ├── Editor de Regla (split view: visual + código)
│   ├── Asistente de Creación (wizard)
│   ├── Validador de Sintaxis
│   └── Probador de Reglas (test runner)
├── API Routes
│   ├── /api/drools/rules (listar)
│   ├── /api/drools/rules/[category]/[file] (cargar)
│   ├── /api/drools/rules/[category]/[file] (guardar)
│   ├── /api/drools/rules/validate (validar)
│   └── /api/drools/rules/test (probar)
│
Backend (Spring Boot)
├── DroolsRulesController
│   ├── GET /api/drools/rules
│   ├── GET /api/drools/rules/{category}/{file}
│   ├── POST /api/drools/rules/{category}/{file}
│   ├── POST /api/drools/rules/validate
│   └── POST /api/drools/rules/test
└── DroolsRulesService
    ├── listRules()
    ├── loadRule(category, file)
    ├── saveRule(category, file, content)
    ├── validateRule(content)
    └── testRule(content, factData)
```

---

## 🔧 IMPLEMENTACIÓN PASO A PASO

### FASE 1: Parser DRL → Modelo Interno (1.5 días)

#### 1.1 Instalar Librería de Parsing

**Opción A: Parser Manual (Recomendado para control)**
```bash
npm install @types/node
```

**Opción B: Monaco Editor (Editor de código avanzado)**
```bash
npm install @monaco-editor/react
```

#### 1.2 Crear Utilidades de Parsing

**Archivo:** `app/(app)/drools-editor/utils/droolsParser.ts`

```typescript
export interface DroolsRule {
  name: string
  salience?: number
  when: DroolsCondition[]
  then: DroolsAction[]
  documentation?: string
}

export interface DroolsCondition {
  factVariable?: string // $fact
  factType: string // FactClass
  conditions: ConditionExpression[]
}

export interface ConditionExpression {
  field: string
  operator: '>=' | '<=' | '==' | '!=' | '>' | '<' | 'in' | 'not in'
  value: any
  logicalOperator?: '&&' | '||'
}

export interface DroolsAction {
  type: 'setField' | 'callMethod' | 'log' | 'update' | 'insert' | 'retract'
  target?: string // $fact
  method?: string // setDecision
  value?: any
  message?: string
}

export interface DroolsRuleFile {
  package: string
  imports: string[]
  globals: { type: string; name: string }[]
  rules: DroolsRule[]
}

/**
 * Parsea archivo .drl a modelo interno
 */
export function parseDroolsFile(content: string): DroolsRuleFile {
  // 1. Extraer package
  // 2. Extraer imports
  // 3. Extraer globals
  // 4. Extraer rules (usar regex o parser manual)
  // 5. Parsear cada rule:
  //    - nombre
  //    - salience
  //    - when (condiciones)
  //    - then (acciones)
  // 6. Retornar DroolsRuleFile
}

/**
 * Parsea bloque when de una regla
 */
function parseWhenBlock(whenContent: string): DroolsCondition[] {
  // Parsear condiciones como:
  // $fact : FactClass(
  //   field1 >= value1,
  //   field2 == value2
  // )
}

/**
 * Parsea bloque then de una regla
 */
function parseThenBlock(thenContent: string): DroolsAction[] {
  // Parsear acciones como:
  // $fact.setDecision("APPROVED");
  // update($fact);
}
```

#### 1.3 Crear Tipos TypeScript

**Archivo:** `app/(app)/drools-editor/types.ts`

```typescript
export interface DroolsRule {
  id: string // Generado automáticamente
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
```

---

### FASE 2: API Backend para Cargar/Guardar (1 día)

#### 2.1 Crear Controller Spring Boot

**Archivo:** `codeflowx.govern.workflow.engine/src/main/java/.../DroolsRulesController.java`

```java
@RestController
@RequestMapping("/api/drools/rules")
@RequiredArgsConstructor
public class DroolsRulesController {

    private final DroolsRulesService droolsRulesService;

    /**
     * Lista todas las reglas disponibles por categoría
     */
    @GetMapping
    public ResponseEntity<Map<String, List<DroolsRuleInfo>>> listRules() {
        return ResponseEntity.ok(droolsRulesService.listRules());
    }

    /**
     * Carga un archivo de reglas
     */
    @GetMapping("/{category}/{fileName}")
    public ResponseEntity<String> loadRule(
        @PathVariable String category,
        @PathVariable String fileName
    ) {
        String content = droolsRulesService.loadRule(category, fileName);
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .body(content);
    }

    /**
     * Guarda un archivo de reglas
     */
    @PostMapping("/{category}/{fileName}")
    public ResponseEntity<DroolsSaveResponse> saveRule(
        @PathVariable String category,
        @PathVariable String fileName,
        @RequestBody DroolsSaveRequest request
    ) {
        // Validar antes de guardar
        ValidationResult validation = droolsRulesService.validateRule(request.getContent());
        if (!validation.isValid()) {
            return ResponseEntity.badRequest()
                .body(DroolsSaveResponse.error(validation.getErrors()));
        }

        // Guardar archivo
        droolsRulesService.saveRule(category, fileName, request.getContent());

        return ResponseEntity.ok(DroolsSaveResponse.success(category, fileName));
    }

    /**
     * Valida sintaxis Drools sin guardar
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateRule(
        @RequestBody DroolsValidateRequest request
    ) {
        ValidationResult result = droolsRulesService.validateRule(request.getContent());
        return ResponseEntity.ok(result);
    }

    /**
     * Prueba una regla con datos de ejemplo
     */
    @PostMapping("/test")
    public ResponseEntity<DroolsTestResponse> testRule(
        @RequestBody DroolsTestRequest request
    ) {
        DroolsTestResponse result = droolsRulesService.testRule(
            request.getRuleContent(),
            request.getFactData()
        );
        return ResponseEntity.ok(result);
    }
}
```

#### 2.2 Crear Service

**Archivo:** `DroolsRulesService.java`

```java
@Service
@RequiredArgsConstructor
public class DroolsRulesService {

    private final ResourceLoader resourceLoader;
    private final KieContainer kieContainer;

    /**
     * Lista reglas desde resources/rules/
     */
    public Map<String, List<DroolsRuleInfo>> listRules() {
        // Escanear src/main/resources/rules/
        // Organizar por categoría
        // Retornar: { "agent": [...], "bias": [...], ... }
    }

    /**
     * Carga archivo .drl
     */
    public String loadRule(String category, String fileName) {
        // Leer desde resources/rules/{category}/{fileName}
        // Retornar contenido como String
    }

    /**
     * Guarda archivo .drl
     */
    public void saveRule(String category, String fileName, String content) {
        // Validar contenido
        // Determinar ruta del archivo
        // Escribir contenido
        // Opcional: Recargar KieContainer
    }

    /**
     * Valida sintaxis Drools
     */
    public ValidationResult validateRule(String content) {
        try {
            // Crear KieHelper temporal
            KieHelper kieHelper = new KieHelper();
            kieHelper.addContent(content, ResourceType.DRL);

            // Intentar compilar
            KieContainer tempContainer = kieHelper.build();

            // Si llega aquí, es válido
            return ValidationResult.valid();
        } catch (Exception e) {
            return ValidationResult.error(e.getMessage());
        }
    }

    /**
     * Prueba regla con datos de ejemplo
     */
    public DroolsTestResponse testRule(String ruleContent, Map<String, Object> factData) {
        try {
            // Crear KieHelper con regla
            KieHelper kieHelper = new KieHelper();
            kieHelper.addContent(ruleContent, ResourceType.DRL);
            KieContainer tempContainer = kieHelper.build();

            // Crear sesión
            KieSession session = tempContainer.newKieSession();

            // Insertar facts desde factData
            Object fact = createFactFromData(factData);
            session.insert(fact);

            // Ejecutar reglas
            int firedRules = session.fireAllRules();

            // Obtener resultado
            Map<String, Object> result = extractFactData(fact);

            session.dispose();

            return DroolsTestResponse.success(firedRules, result);
        } catch (Exception e) {
            return DroolsTestResponse.error(e.getMessage());
        }
    }
}
```

#### 2.3 Crear DTOs

```java
@Data
public class DroolsRuleInfo {
    private String category;
    private String fileName;
    private String packageName;
    private int ruleCount;
    private LocalDateTime lastModified;
}

@Data
public class DroolsSaveRequest {
    private String content;
    private String comment; // Opcional
}

@Data
public class DroolsSaveResponse {
    private boolean success;
    private String category;
    private String fileName;
    private List<String> errors;
}

@Data
public class DroolsValidateRequest {
    private String content;
}

@Data
public class DroolsTestRequest {
    private String ruleContent;
    private Map<String, Object> factData; // Datos del fact para probar
}

@Data
public class DroolsTestResponse {
    private boolean success;
    private int firedRules;
    private Map<String, Object> result;
    private String error;
}
```

---

### FASE 3: Editor Visual de Reglas (2 días)

#### 3.1 Crear Componente Editor

**Archivo:** `app/(app)/drools-editor/page.tsx`

```typescript
'use client'

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'
import MonacoEditor from '@monaco-editor/react'
import { parseDroolsFile, generateDroolsFile } from './utils/droolsParser'
import { DroolsRuleFile, DroolsRule } from './types'

export default function DroolsEditorPage() {
  const [categories, setCategories] = useState<string[]>([])
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null)
  const [selectedFile, setSelectedFile] = useState<string | null>(null)
  const [ruleFile, setRuleFile] = useState<DroolsRuleFile | null>(null)
  const [rawContent, setRawContent] = useState<string>('')
  const [viewMode, setViewMode] = useState<'visual' | 'code' | 'split'>('split')
  const [selectedRule, setSelectedRule] = useState<DroolsRule | null>(null)
  const [validationErrors, setValidationErrors] = useState<string[]>([])

  // Cargar lista de categorías y archivos
  useEffect(() => {
    fetch('/api/drools/rules')
      .then(res => res.json())
      .then(data => {
        setCategories(Object.keys(data))
      })
  }, [])

  // Cargar archivo seleccionado
  const loadFile = async (category: string, fileName: string) => {
    try {
      const response = await fetch(`/api/drools/rules/${category}/${fileName}`)
      const content = await response.text()

      setRawContent(content)
      const parsed = parseDroolsFile(content)
      setRuleFile(parsed)
      setSelectedCategory(category)
      setSelectedFile(fileName)
    } catch (error) {
      console.error('Error loading file:', error)
    }
  }

  // Guardar archivo
  const saveFile = async () => {
    if (!selectedCategory || !selectedFile) return

    try {
      // Validar antes de guardar
      const validationResponse = await fetch('/api/drools/rules/validate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: rawContent }),
      })

      const validation = await validationResponse.json()

      if (!validation.valid) {
        setValidationErrors(validation.errors || [])
        alert(`Errores de validación:\n${validation.errors.join('\n')}`)
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
        alert('Reglas guardadas exitosamente')
        setValidationErrors([])
      } else {
        alert(`Error al guardar: ${result.errors.join('\n')}`)
      }
    } catch (error) {
      console.error('Error saving file:', error)
      alert('Error al guardar el archivo')
    }
  }

  // Sincronizar código con modelo visual
  const syncCodeToVisual = () => {
    if (rawContent) {
      try {
        const parsed = parseDroolsFile(rawContent)
        setRuleFile(parsed)
      } catch (error) {
        console.error('Error parsing:', error)
      }
    }
  }

  // Sincronizar modelo visual con código
  const syncVisualToCode = () => {
    if (ruleFile) {
      const generated = generateDroolsFile(ruleFile)
      setRawContent(generated)
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white border-b p-4">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Editor de Reglas Drools</h1>
          <div className="flex gap-2">
            <Button onClick={syncCodeToVisual}>Sincronizar → Visual</Button>
            <Button onClick={syncVisualToCode}>Sincronizar → Código</Button>
            <Button onClick={saveFile} disabled={!selectedFile}>
              Guardar
            </Button>
          </div>
        </div>
      </div>

      <div className="flex h-[calc(100vh-80px)]">
        {/* Sidebar - Lista de archivos */}
        <div className="w-64 bg-white border-r p-4 overflow-auto">
          <h2 className="font-semibold mb-4">Categorías</h2>
          {categories.map(category => (
            <div key={category} className="mb-4">
              <button
                onClick={() => setSelectedCategory(category)}
                className="font-medium text-sm"
              >
                {category}
              </button>
              {/* Listar archivos de la categoría */}
            </div>
          ))}
        </div>

        {/* Main Content */}
        <div className="flex-1 flex">
          {viewMode === 'code' || viewMode === 'split' ? (
            <div className={viewMode === 'split' ? 'w-1/2 border-r' : 'w-full'}>
              <MonacoEditor
                height="100%"
                language="drools"
                value={rawContent}
                onChange={(value) => setRawContent(value || '')}
                theme="vs-dark"
                options={{
                  minimap: { enabled: false },
                  fontSize: 14,
                  wordWrap: 'on',
                }}
              />
            </div>
          ) : null}

          {viewMode === 'visual' || viewMode === 'split' ? (
            <div className={viewMode === 'split' ? 'w-1/2 p-4 overflow-auto' : 'w-full p-4'}>
              {ruleFile ? (
                <div className="space-y-4">
                  {/* Información del archivo */}
                  <Card>
                    <CardHeader>
                      <CardTitle>Información del Archivo</CardTitle>
                    </CardHeader>
                    <CardContent>
                      <p><strong>Package:</strong> {ruleFile.package}</p>
                      <p><strong>Imports:</strong> {ruleFile.imports.join(', ')}</p>
                      <p><strong>Reglas:</strong> {ruleFile.rules.length}</p>
                    </CardContent>
                  </Card>

                  {/* Lista de reglas */}
                  {ruleFile.rules.map(rule => (
                    <Card
                      key={rule.id}
                      className={selectedRule?.id === rule.id ? 'border-primary' : ''}
                      onClick={() => setSelectedRule(rule)}
                    >
                      <CardHeader>
                        <CardTitle className="text-lg">{rule.name}</CardTitle>
                        <p className="text-sm text-muted-foreground">
                          Salience: {rule.salience}
                        </p>
                      </CardHeader>
                      <CardContent>
                        {/* Mostrar condiciones y acciones de forma visual */}
                        <div className="space-y-2">
                          <div>
                            <strong>When:</strong>
                            {rule.when.map((condition, idx) => (
                              <div key={idx} className="ml-4 text-sm">
                                {condition.factVariable}: {condition.factType}
                                {condition.conditions.map((cond, cIdx) => (
                                  <div key={cIdx} className="ml-4">
                                    {cond.field} {cond.operator} {cond.value}
                                  </div>
                                ))}
                              </div>
                            ))}
                          </div>
                          <div>
                            <strong>Then:</strong>
                            {rule.then.map((action, idx) => (
                              <div key={idx} className="ml-4 text-sm">
                                {action.type}: {action.target}.{action.method}({action.value})
                              </div>
                            ))}
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              ) : (
                <p>Seleccione un archivo para editar</p>
              )}
            </div>
          ) : null}
        </div>
      </div>
    </div>
  )
}
```

#### 3.2 Crear Editor de Regla Individual

**Archivo:** `app/(app)/drools-editor/components/RuleEditor.tsx`

```typescript
interface RuleEditorProps {
  rule: DroolsRule
  onChange: (rule: DroolsRule) => void
}

export function RuleEditor({ rule, onChange }: RuleEditorProps) {
  // Editor visual para editar una regla individual
  // - Nombre de la regla
  // - Salience
  // - Condiciones (agregar/eliminar/modificar)
  // - Acciones (agregar/eliminar/modificar)
  // - Documentación
}
```

---

### FASE 4: Asistente de Creación de Reglas (1 día)

#### 4.1 Crear Wizard de Creación

**Archivo:** `app/(app)/drools-editor/components/RuleWizard.tsx`

```typescript
export function RuleWizard({ onComplete }: { onComplete: (rule: DroolsRule) => void }) {
  const [step, setStep] = useState(1)
  const [rule, setRule] = useState<Partial<DroolsRule>>({
    name: '',
    salience: 100,
    when: [],
    then: [],
  })

  // Paso 1: Información básica
  // - Nombre de la regla
  // - Salience
  // - Documentación

  // Paso 2: Seleccionar Fact
  // - Lista de Facts disponibles
  // - Variable name ($fact)

  // Paso 3: Agregar Condiciones
  // - Campo del Fact
  // - Operador (>=, <=, ==, !=, etc.)
  // - Valor
  // - Operador lógico (&&, ||)

  // Paso 4: Agregar Acciones
  // - Tipo de acción (setField, callMethod, log, update)
  // - Método o campo
  // - Valor

  // Paso 5: Revisar y Crear
  // - Vista previa de la regla
  // - Generar código DRL
  // - Crear regla
}
```

---

### FASE 5: Probador de Reglas (0.5 días)

#### 5.1 Crear Componente de Pruebas

**Archivo:** `app/(app)/drools-editor/components/RuleTester.tsx`

```typescript
export function RuleTester({ ruleContent }: { ruleContent: string }) {
  const [factData, setFactData] = useState<Record<string, any>>({})
  const [testResult, setTestResult] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  const runTest = async () => {
    setLoading(true)
    try {
      const response = await fetch('/api/drools/rules/test', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ruleContent,
          factData,
        }),
      })

      const result = await response.json()
      setTestResult(result)
    } catch (error) {
      console.error('Error testing rule:', error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Probar Regla</CardTitle>
      </CardHeader>
      <CardContent>
        {/* Formulario para ingresar datos del Fact */}
        {/* Botón "Ejecutar Prueba" */}
        {/* Mostrar resultados */}
      </CardContent>
    </Card>
  )
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Fase 1: Parser
- [ ] Crear `droolsParser.ts` con función `parseDroolsFile()`
- [ ] Parsear package, imports, globals
- [ ] Parsear reglas individuales
- [ ] Parsear bloque when (condiciones)
- [ ] Parsear bloque then (acciones)
- [ ] Crear función `generateDroolsFile()` para generar código
- [ ] Probar con archivo real (ej: `bias-fairness.drl`)

### Fase 2: Backend
- [ ] Crear `DroolsRulesController` con endpoints
- [ ] Crear `DroolsRulesService` con lógica
- [ ] Implementar `listRules()` - escanear resources/rules/
- [ ] Implementar `loadRule()` - leer archivo .drl
- [ ] Implementar `saveRule()` - escribir archivo .drl
- [ ] Implementar `validateRule()` - validar con Drools
- [ ] Implementar `testRule()` - probar regla con datos
- [ ] Crear DTOs
- [ ] Probar endpoints

### Fase 3: Editor Visual
- [ ] Crear página `drools-editor/page.tsx`
- [ ] Integrar Monaco Editor para código
- [ ] Crear vista visual de reglas
- [ ] Implementar modo split (código + visual)
- [ ] Crear componente `RuleEditor` para editar regla individual
- [ ] Sincronizar código ↔ visual
- [ ] Implementar guardado con validación

### Fase 4: Asistente
- [ ] Crear componente `RuleWizard`
- [ ] Paso 1: Información básica
- [ ] Paso 2: Seleccionar Fact
- [ ] Paso 3: Agregar condiciones
- [ ] Paso 4: Agregar acciones
- [ ] Paso 5: Revisar y crear
- [ ] Generar código DRL desde wizard

### Fase 5: Probador
- [ ] Crear componente `RuleTester`
- [ ] Formulario dinámico para datos del Fact
- [ ] Llamar a API de prueba
- [ ] Mostrar resultados (reglas ejecutadas, cambios en fact)

---

## 🧪 PRUEBAS

### Casos de Prueba

1. **Cargar Archivo Existente**
   - Cargar `bias-fairness.drl`
   - Verificar que se parsea correctamente
   - Verificar que se muestra en vista visual

2. **Editar Regla**
   - Modificar salience de una regla
   - Agregar nueva condición
   - Modificar acción
   - Verificar sincronización código ↔ visual

3. **Crear Nueva Regla**
   - Usar wizard para crear regla
   - Verificar que se genera código correcto
   - Agregar a archivo existente

4. **Validar Regla**
   - Intentar guardar regla con error de sintaxis
   - Verificar que se muestran errores
   - Corregir errores y guardar exitosamente

5. **Probar Regla**
   - Crear datos de ejemplo para Fact
   - Ejecutar prueba
   - Verificar que reglas se ejecutan correctamente
   - Verificar cambios en Fact

---

## 📚 REFERENCIAS

### Documentación Drools
- [Drools Documentation](https://docs.drools.org/)
- [Drools Rule Language](https://docs.drools.org/latest/drools-docs/html_single/#_drools_rule_language_drl_rules)

### Sintaxis Drools
- **Package:** `package com.example.drools;`
- **Imports:** `import com.example.Fact;`
- **Globals:** `global Logger logger;`
- **Rules:** `rule "Name" when ... then ... end`
- **Salience:** `salience 100` (mayor número = mayor prioridad)
- **Conditions:** `$fact : FactClass(field >= value)`
- **Actions:** `$fact.setField(value); update($fact);`

### Ejemplos de Código
- Ver archivos en `codeflowx.govern.workflow.lib/src/main/resources/rules/`
- `bias-fairness.drl` - Ejemplo completo
- `agent-scoring.drl` - Múltiples reglas
- `dataset-quality-scoring.drl` - Reglas complejas

---

## ⚠️ NOTAS IMPORTANTES

1. **Validación:**
   - Siempre validar sintaxis antes de guardar
   - Usar `KieHelper` de Drools para validación
   - Mostrar errores de compilación al usuario

2. **Facts:**
   - Los Facts son clases Java
   - Necesitan estar disponibles en el classpath
   - Documentar Facts disponibles para el usuario

3. **Sincronización:**
   - Mantener sincronizado código y vista visual
   - Permitir editar en ambos modos
   - Advertir si hay cambios no guardados

4. **Backup:**
   - Crear backup antes de guardar cambios
   - Permitir revertir cambios

5. **Testing:**
   - Permitir probar reglas sin guardar
   - Usar datos de ejemplo realistas
   - Mostrar qué reglas se ejecutaron

---

**Última actualización:** Enero 2025
**Mantenido por:** Equipo de Desarrollo
**Contacto:** `dev@codeflowx.internal`
