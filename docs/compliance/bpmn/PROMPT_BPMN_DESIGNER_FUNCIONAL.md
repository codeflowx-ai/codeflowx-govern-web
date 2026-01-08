# PROMPT: Implementar Diseñador BPMN Funcional con Carga/Edición de Procesos

**Fecha:** Enero 2025
**Objetivo:** Hacer funcional el diseñador BPMN para cargar, visualizar y editar procesos BPMN reales desde `codeflowx.govern.workflow.lib`
**Prioridad:** 🟡 MEDIA
**Estimación:** 5-7 días

---

## 📋 CONTEXTO

### Estado Actual

**Procesos BPMN:**
- **Ubicación:** `nocode-service/codeflowx.govern.workflow.lib/src/main/resources/processes/`
- **Total:** 42 archivos BPMN (`.bpmn` y `.bpmn20.xml`)
- **Formato:** XML BPMN 2.0 estándar compatible con Flowable 7.0.1
- **Organización:**
  - `aios/` - 15 procesos
  - `audit/` - 6 procesos
  - `compliance/` - 11 procesos
  - `metrics/` - 10 procesos

**Diseñador BPMN:**
- **Ubicación:** `codeflowx-studio/backup/app-app/bpmn-designer/page.tsx`
- **Estado:** Prototipo visual básico (drag & drop, sin persistencia)
- **Limitaciones:**
  - ❌ No carga archivos BPMN XML existentes
  - ❌ No exporta a XML BPMN
  - ❌ No integración con backend/Flowable
  - ❌ Botones Export/Save sin implementación

### Stack Tecnológico

- **Frontend:** Next.js 14, React, TypeScript
- **Backend:** Spring Boot, Flowable 7.0.1
- **Formato BPMN:** XML BPMN 2.0 (namespace Activiti/Flowable)
- **Librería BPMN:** `codeflowx.govern.workflow.lib` (JAR compartido)

---

## 🎯 OBJETIVOS

1. **Cargar procesos BPMN existentes** desde el backend y visualizarlos en el diseñador
2. **Editar procesos BPMN** visualmente (agregar/eliminar/modificar elementos)
3. **Exportar procesos editados** a XML BPMN 2.0 compatible con Flowable
4. **Guardar procesos** en el backend con validación
5. **Validar sintaxis BPMN** antes de guardar

---

## 📐 ARQUITECTURA PROPUESTA

### Componentes Necesarios

```
Frontend (Next.js)
├── BPMN Designer (page.tsx)
│   ├── Parser XML → Modelo Interno
│   ├── Renderer Visual (SVG/Canvas)
│   ├── Editor de Propiedades
│   └── Generador XML → Export
├── API Routes
│   ├── /api/bpmn/processes (listar)
│   ├── /api/bpmn/processes/[id] (cargar)
│   └── /api/bpmn/processes/[id] (guardar)
│
Backend (Spring Boot)
├── BPMNProcessController
│   ├── GET /api/bpmn/processes
│   ├── GET /api/bpmn/processes/{id}
│   ├── POST /api/bpmn/processes/{id}
│   └── POST /api/bpmn/processes/{id}/validate
└── BPMNProcessService
    ├── listProcesses()
    ├── loadProcess(id)
    ├── saveProcess(id, xml)
    └── validateProcess(xml)
```

---

## 🔧 IMPLEMENTACIÓN PASO A PASO

### FASE 1: Parser XML BPMN → Modelo Interno (2 días)

#### 1.1 Instalar Librería de Parsing BPMN

**Opción A: bpmn-js (Recomendado)**
```bash
npm install bpmn-js bpmn-moddle
```

**Opción B: Parser XML Manual**
```bash
npm install xml2js @types/xml2js
```

#### 1.2 Crear Utilidades de Parsing

**Archivo:** `app/(app)/bpmn-designer/utils/bpmnParser.ts`

```typescript
import { BPMNElement, BPMNConnection } from '../types'

interface BPMNProcess {
  id: string
  name: string
  elements: BPMNElement[]
  connections: BPMNConnection[]
  namespace: string
  isExecutable: boolean
}

/**
 * Parsea XML BPMN 2.0 a modelo interno
 */
export function parseBPMNXML(xmlString: string): BPMNProcess {
  // 1. Parsear XML
  // 2. Extraer elementos (startEvent, endEvent, userTask, serviceTask, gateway, etc.)
  // 3. Extraer conexiones (sequenceFlow)
  // 4. Mapear a BPMNElement[] y BPMNConnection[]
  // 5. Extraer propiedades (formKey, delegate, candidateGroups, etc.)
  // 6. Retornar BPMNProcess
}

/**
 * Mapea elemento XML BPMN a BPMNElement
 */
function mapXMLElementToBPMNElement(xmlElement: any, di: any): BPMNElement {
  // Mapear según tipo:
  // - startEvent → type: 'start'
  // - endEvent → type: 'end'
  // - userTask → type: 'task', subtype: 'user'
  // - serviceTask → type: 'task', subtype: 'service'
  // - exclusiveGateway → type: 'gateway', subtype: 'exclusive'
  // - parallelGateway → type: 'gateway', subtype: 'parallel'
  // Extraer posición desde BPMNDiagram
  // Extraer propiedades desde atributos activiti:*
}

/**
 * Mapea sequenceFlow XML a BPMNConnection
 */
function mapXMLSequenceFlowToConnection(xmlFlow: any): BPMNConnection {
  // Extraer source, target, conditionExpression
  // Mapear a BPMNConnection
}
```

**Elementos BPMN a Mapear:**
- `startEvent` → `{ type: 'start', subtype: 'timer'|'message'|'none' }`
- `endEvent` → `{ type: 'end', subtype: 'message'|'none' }`
- `userTask` → `{ type: 'task', subtype: 'user' }`
- `serviceTask` → `{ type: 'task', subtype: 'service' }`
- `scriptTask` → `{ type: 'task', subtype: 'script' }`
- `businessRuleTask` → `{ type: 'task', subtype: 'businessRule' }`
- `exclusiveGateway` → `{ type: 'gateway', subtype: 'exclusive' }`
- `parallelGateway` → `{ type: 'gateway', subtype: 'parallel' }`
- `inclusiveGateway` → `{ type: 'gateway', subtype: 'inclusive' }`
- `sequenceFlow` → `BPMNConnection`

**Propiedades a Extraer:**
- `activiti:formKey` → `properties.formKey`
- `activiti:class` → `properties.delegateClass`
- `activiti:candidateGroups` → `properties.candidateGroups`
- `activiti:priority` → `properties.priority`
- `name` → `name`
- `documentation` → `description`
- Posición desde `bpmndi:BPMNShape`

#### 1.3 Actualizar Tipos TypeScript

**Archivo:** `app/(app)/bpmn-designer/types.ts`

```typescript
export interface BPMNElement {
  id: string
  type: 'start' | 'end' | 'task' | 'gateway' | 'event' | 'subprocess'
  subtype?: string
  name: string
  description?: string
  x: number
  y: number
  width: number
  height: number
  properties: {
    formKey?: string
    delegateClass?: string
    candidateGroups?: string[]
    priority?: number
    assignee?: string
    dueDate?: string
    documentation?: string
    script?: string
    scriptFormat?: string
    conditionExpression?: string
    [key: string]: any
  }
  connections: {
    incoming: string[]
    outgoing: string[]
  }
}

export interface BPMNConnection {
  id: string
  type: 'sequence' | 'message' | 'association'
  source: string
  target: string
  label?: string
  conditionExpression?: string
  properties: Record<string, any>
}
```

---

### FASE 2: API Backend para Cargar/Guardar (1 día)

#### 2.1 Crear Controller Spring Boot

**Archivo:** `codeflowx.govern.workflow.engine/src/main/java/.../BPMNProcessController.java`

```java
@RestController
@RequestMapping("/api/bpmn/processes")
@RequiredArgsConstructor
public class BPMNProcessController {

    private final BPMNProcessService bpmnProcessService;

    /**
     * Lista todos los procesos BPMN disponibles
     */
    @GetMapping
    public ResponseEntity<List<BPMNProcessInfo>> listProcesses() {
        return ResponseEntity.ok(bpmnProcessService.listProcesses());
    }

    /**
     * Carga un proceso BPMN por ID
     */
    @GetMapping("/{processId}")
    public ResponseEntity<String> loadProcess(
        @PathVariable String processId
    ) {
        String xml = bpmnProcessService.loadProcess(processId);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_XML)
            .body(xml);
    }

    /**
     * Guarda un proceso BPMN editado
     */
    @PostMapping("/{processId}")
    public ResponseEntity<BPMNSaveResponse> saveProcess(
        @PathVariable String processId,
        @RequestBody BPMNSaveRequest request
    ) {
        // Validar XML antes de guardar
        ValidationResult validation = bpmnProcessService.validateProcess(request.getXml());
        if (!validation.isValid()) {
            return ResponseEntity.badRequest()
                .body(BPMNSaveResponse.error(validation.getErrors()));
        }

        // Guardar proceso
        bpmnProcessService.saveProcess(processId, request.getXml());

        return ResponseEntity.ok(BPMNSaveResponse.success(processId));
    }

    /**
     * Valida sintaxis BPMN sin guardar
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidationResult> validateProcess(
        @RequestBody BPMNValidateRequest request
    ) {
        ValidationResult result = bpmnProcessService.validateProcess(request.getXml());
        return ResponseEntity.ok(result);
    }
}
```

#### 2.2 Crear Service

**Archivo:** `BPMNProcessService.java`

```java
@Service
@RequiredArgsConstructor
public class BPMNProcessService {

    private final ProcessEngine processEngine;
    private final ResourceLoader resourceLoader;

    /**
     * Lista procesos desde resources/processes/
     */
    public List<BPMNProcessInfo> listProcesses() {
        // Escanear src/main/resources/processes/
        // Retornar lista con: id, name, category, path
    }

    /**
     * Carga proceso desde resources
     */
    public String loadProcess(String processId) {
        // Buscar archivo .bpmn o .bpmn20.xml
        // Leer contenido XML
        // Retornar como String
    }

    /**
     * Guarda proceso editado
     */
    public void saveProcess(String processId, String xml) {
        // Validar XML
        // Determinar ruta del archivo original
        // Escribir XML al archivo
        // Opcional: Redesplegar proceso en Flowable
    }

    /**
     * Valida sintaxis BPMN usando Flowable
     */
    public ValidationResult validateProcess(String xml) {
        try {
            // Usar BpmnXMLConverter de Flowable
            BpmnModel bpmnModel = new BpmnXMLConverter()
                .convertToBpmnModel(new StringInputStream(xml));

            // Validar modelo
            ProcessValidator validator = ProcessValidatorFactory.create();
            List<ValidationError> errors = validator.validate(bpmnModel);

            return ValidationResult.fromErrors(errors);
        } catch (Exception e) {
            return ValidationResult.error(e.getMessage());
        }
    }
}
```

#### 2.3 Crear DTOs

```java
@Data
public class BPMNProcessInfo {
    private String id;
    private String name;
    private String category; // aios, compliance, audit, metrics
    private String path;
    private String version;
}

@Data
public class BPMNSaveRequest {
    private String xml;
    private String comment; // Opcional: comentario del cambio
}

@Data
public class BPMNSaveResponse {
    private boolean success;
    private String processId;
    private List<String> errors;

    public static BPMNSaveResponse success(String processId) {
        BPMNSaveResponse response = new BPMNSaveResponse();
        response.setSuccess(true);
        response.setProcessId(processId);
        return response;
    }

    public static BPMNSaveResponse error(List<String> errors) {
        BPMNSaveResponse response = new BPMNSaveResponse();
        response.setSuccess(false);
        response.setErrors(errors);
        return response;
    }
}

@Data
public class ValidationResult {
    private boolean valid;
    private List<String> errors;
}
```

---

### FASE 3: API Routes Next.js (1 día)

#### 3.1 Crear API Routes

**Archivo:** `app/api/bpmn/processes/route.ts`

```typescript
import { NextRequest, NextResponse } from 'next/server'

const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080'

export async function GET(request: NextRequest) {
  try {
    const response = await fetch(`${BACKEND_URL}/api/bpmn/processes`, {
      headers: {
        'Authorization': request.headers.get('Authorization') || '',
      },
    })

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Failed to fetch processes' },
        { status: response.status }
      )
    }

    const processes = await response.json()
    return NextResponse.json(processes)
  } catch (error) {
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}
```

**Archivo:** `app/api/bpmn/processes/[id]/route.ts`

```typescript
export async function GET(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const response = await fetch(
      `${BACKEND_URL}/api/bpmn/processes/${params.id}`,
      {
        headers: {
          'Authorization': request.headers.get('Authorization') || '',
        },
      }
    )

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Failed to load process' },
        { status: response.status }
      )
    }

    const xml = await response.text()
    return new NextResponse(xml, {
      headers: {
        'Content-Type': 'application/xml',
      },
    })
  } catch (error) {
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}

export async function POST(
  request: NextRequest,
  { params }: { params: { id: string } }
) {
  try {
    const body = await request.json()

    const response = await fetch(
      `${BACKEND_URL}/api/bpmn/processes/${params.id}`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': request.headers.get('Authorization') || '',
        },
        body: JSON.stringify(body),
      }
    )

    const result = await response.json()

    if (!response.ok) {
      return NextResponse.json(result, { status: response.status })
    }

    return NextResponse.json(result)
  } catch (error) {
    return NextResponse.json(
      { error: 'Internal server error' },
      { status: 500 }
    )
  }
}
```

---

### FASE 4: Integrar Carga en Diseñador (1 día)

#### 4.1 Agregar Selector de Procesos

**En `page.tsx`:**

```typescript
const [availableProcesses, setAvailableProcesses] = useState<BPMNProcessInfo[]>([])
const [selectedProcessId, setSelectedProcessId] = useState<string | null>(null)
const [loading, setLoading] = useState(false)

// Cargar lista de procesos al montar
useEffect(() => {
  fetch('/api/bpmn/processes')
    .then(res => res.json())
    .then(data => setAvailableProcesses(data))
    .catch(err => console.error('Error loading processes:', err))
}, [])

// Cargar proceso seleccionado
const loadProcess = async (processId: string) => {
  setLoading(true)
  try {
    const response = await fetch(`/api/bpmn/processes/${processId}`)
    const xmlString = await response.text()

    // Parsear XML a modelo interno
    const process = parseBPMNXML(xmlString)

    // Actualizar estado del diseñador
    setElements(process.elements)
    setConnections(process.connections)
    setProcessName(process.name)
    setSelectedProcessId(processId)
  } catch (error) {
    console.error('Error loading process:', error)
    alert('Error al cargar el proceso')
  } finally {
    setLoading(false)
  }
}
```

#### 4.2 Agregar UI para Seleccionar Proceso

```typescript
{/* Selector de Procesos */}
<div className="mb-3">
  <label className="form-label small">Cargar Proceso Existente</label>
  <select
    className="form-select form-select-sm"
    value={selectedProcessId || ''}
    onChange={(e) => {
      if (e.target.value) {
        loadProcess(e.target.value)
      }
    }}
    disabled={loading}
  >
    <option value="">-- Seleccionar proceso --</option>
    {availableProcesses.map(proc => (
      <option key={proc.id} value={proc.id}>
        {proc.name} ({proc.category})
      </option>
    ))}
  </select>
</div>
```

---

### FASE 5: Generador XML BPMN → Export (2 días)

#### 5.1 Crear Generador XML

**Archivo:** `app/(app)/bpmn-designer/utils/bpmnGenerator.ts`

```typescript
import { BPMNElement, BPMNConnection, BPMNProcess } from '../types'

/**
 * Genera XML BPMN 2.0 desde modelo interno
 */
export function generateBPMNXML(process: BPMNProcess): string {
  // 1. Construir estructura XML BPMN 2.0
  // 2. Agregar namespace y definiciones
  // 3. Agregar proceso con elementos
  // 4. Agregar BPMNDiagram con posiciones
  // 5. Retornar XML como string
}

/**
 * Genera elemento XML desde BPMNElement
 */
function generateXMLElement(element: BPMNElement): string {
  // Generar XML según tipo:
  // - startEvent → <startEvent id="..." name="...">
  // - userTask → <userTask id="..." activiti:formKey="..." activiti:candidateGroups="...">
  // - serviceTask → <serviceTask id="..." activiti:class="...">
  // - gateway → <exclusiveGateway id="...">
  // Incluir propiedades desde element.properties
  // Incluir documentation si existe
}

/**
 * Genera sequenceFlow XML desde BPMNConnection
 */
function generateXMLSequenceFlow(connection: BPMNConnection): string {
  // Generar <sequenceFlow id="..." sourceRef="..." targetRef="...">
  // Incluir conditionExpression si existe
}

/**
 * Genera BPMNDiagram con posiciones
 */
function generateBPMNDiagram(elements: BPMNElement[], connections: BPMNConnection[]): string {
  // Generar <bpmndi:BPMNDiagram>
  // Agregar <bpmndi:BPMNPlane>
  // Agregar <bpmndi:BPMNShape> para cada elemento con posición
  // Agregar <bpmndi:BPMNEdge> para cada conexión
}
```

#### 5.2 Implementar Export

**En `page.tsx`:**

```typescript
const handleExport = () => {
  const process: BPMNProcess = {
    id: selectedProcessId || 'new-process',
    name: processName,
    elements,
    connections,
    namespace: 'http://www.activiti.org/test',
    isExecutable: true,
  }

  const xml = generateBPMNXML(process)

  // Descargar como archivo
  const blob = new Blob([xml], { type: 'application/xml' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${process.id}.bpmn20.xml`
  a.click()
  URL.revokeObjectURL(url)
}
```

#### 5.3 Implementar Save

```typescript
const handleSave = async () => {
  if (!selectedProcessId) {
    alert('Debe cargar un proceso existente para guardar')
    return
  }

  setLoading(true)
  try {
    const process: BPMNProcess = {
      id: selectedProcessId,
      name: processName,
      elements,
      connections,
      namespace: 'http://www.activiti.org/test',
      isExecutable: true,
    }

    const xml = generateBPMNXML(process)

    // Validar antes de guardar
    const validationResponse = await fetch('/api/bpmn/processes/validate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ xml }),
    })

    const validation = await validationResponse.json()

    if (!validation.valid) {
      alert(`Errores de validación:\n${validation.errors.join('\n')}`)
      return
    }

    // Guardar proceso
    const saveResponse = await fetch(`/api/bpmn/processes/${selectedProcessId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ xml }),
    })

    const result = await saveResponse.json()

    if (result.success) {
      alert('Proceso guardado exitosamente')
    } else {
      alert(`Error al guardar: ${result.errors.join('\n')}`)
    }
  } catch (error) {
    console.error('Error saving process:', error)
    alert('Error al guardar el proceso')
  } finally {
    setLoading(false)
  }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTACIÓN

### Fase 1: Parser
- [ ] Instalar librería de parsing (bpmn-js o xml2js)
- [ ] Crear `bpmnParser.ts` con función `parseBPMNXML()`
- [ ] Mapear todos los tipos de elementos BPMN
- [ ] Extraer propiedades (formKey, delegate, candidateGroups, etc.)
- [ ] Extraer posiciones desde BPMNDiagram
- [ ] Mapear sequenceFlow a conexiones
- [ ] Probar con proceso real (ej: `model-approval-v1.bpmn`)

### Fase 2: Backend
- [ ] Crear `BPMNProcessController` con endpoints
- [ ] Crear `BPMNProcessService` con lógica de negocio
- [ ] Implementar `listProcesses()` - escanear resources/processes/
- [ ] Implementar `loadProcess(id)` - leer archivo XML
- [ ] Implementar `saveProcess(id, xml)` - escribir archivo XML
- [ ] Implementar `validateProcess(xml)` - validar con Flowable
- [ ] Crear DTOs (BPMNProcessInfo, BPMNSaveRequest, etc.)
- [ ] Probar endpoints con Postman/curl

### Fase 3: API Routes
- [ ] Crear `app/api/bpmn/processes/route.ts` (GET list)
- [ ] Crear `app/api/bpmn/processes/[id]/route.ts` (GET load, POST save)
- [ ] Crear `app/api/bpmn/processes/validate/route.ts` (POST validate)
- [ ] Manejar autenticación/authorization
- [ ] Manejar errores correctamente
- [ ] Probar con fetch desde frontend

### Fase 4: Integración Frontend
- [ ] Agregar selector de procesos en UI
- [ ] Implementar `loadProcess()` en diseñador
- [ ] Llamar a parser cuando se carga proceso
- [ ] Actualizar estado del diseñador con elementos/conexiones
- [ ] Mostrar loading state durante carga
- [ ] Manejar errores de carga

### Fase 5: Generador XML
- [ ] Crear `bpmnGenerator.ts` con función `generateBPMNXML()`
- [ ] Generar estructura XML BPMN 2.0 correcta
- [ ] Incluir namespaces (Activiti/Flowable)
- [ ] Generar elementos desde modelo interno
- [ ] Generar sequenceFlow desde conexiones
- [ ] Generar BPMNDiagram con posiciones
- [ ] Incluir propiedades (formKey, delegate, etc.)
- [ ] Implementar `handleExport()` - descargar XML
- [ ] Implementar `handleSave()` - guardar en backend
- [ ] Validar XML antes de guardar
- [ ] Probar export/import roundtrip (cargar → editar → guardar → cargar)

---

## 🧪 PRUEBAS

### Casos de Prueba

1. **Cargar Proceso Existente**
   - Cargar `model-approval-v1.bpmn`
   - Verificar que todos los elementos se muestran correctamente
   - Verificar que las conexiones se muestran
   - Verificar que las propiedades se cargan (formKey, delegate, etc.)

2. **Editar Proceso**
   - Agregar nuevo elemento (userTask)
   - Conectar elemento nuevo
   - Modificar propiedades de elemento existente
   - Eliminar elemento

3. **Exportar Proceso**
   - Exportar proceso editado
   - Verificar que el XML generado es válido
   - Verificar que Flowable puede parsear el XML

4. **Guardar Proceso**
   - Guardar proceso editado
   - Verificar que se guarda en el archivo correcto
   - Cargar proceso guardado y verificar cambios

5. **Validación**
   - Intentar guardar proceso con error (ej: elemento sin conexión)
   - Verificar que se muestran errores de validación
   - Corregir errores y guardar exitosamente

---

## 📚 REFERENCIAS

### Documentación BPMN 2.0
- [BPMN 2.0 Specification](https://www.omg.org/spec/BPMN/2.0/)
- [Flowable BPMN Reference](https://www.flowable.com/open-source/docs/bpmn/ch02-GettingStarted)

### Librerías
- **bpmn-js:** https://github.com/bpmn-io/bpmn-js (Editor visual completo)
- **bpmn-moddle:** https://github.com/bpmn-io/bpmn-moddle (Parser/generador)
- **xml2js:** https://github.com/Leonidas-from-XIV/node-xml2js (Parser XML genérico)

### Ejemplos de Código
- Proceso de ejemplo: `model-approval-v1.bpmn`
- Estructura XML BPMN: Ver archivos en `codeflowx.govern.workflow.lib/src/main/resources/processes/`

---

## ⚠️ NOTAS IMPORTANTES

1. **Compatibilidad Flowable:**
   - Usar namespace `http://activiti.org/bpmn` (compatible con Flowable)
   - Atributos `activiti:*` son compatibles con Flowable 7.0.1

2. **Validación:**
   - Siempre validar XML antes de guardar
   - Usar `BpmnXMLConverter` de Flowable para validación
   - Mostrar errores de validación al usuario

3. **Backup:**
   - Antes de guardar, crear backup del archivo original
   - Permitir revertir cambios si hay errores

4. **Versionado:**
   - Considerar versionado de procesos (Git o sistema propio)
   - Guardar historial de cambios

5. **Rendimiento:**
   - Procesos grandes pueden ser lentos de parsear
   - Considerar lazy loading de elementos
   - Optimizar renderizado visual

---

**Última actualización:** Enero 2025
**Mantenido por:** Equipo de Desarrollo
**Contacto:** `dev@codeflowx.internal`
