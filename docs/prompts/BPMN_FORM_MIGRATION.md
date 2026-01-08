# Prompt para Migración Automática de Formularios BPMN

## Contexto

Estás migrando formularios BPMN de ZKoss (Java/ZUL) a Next.js 14 (React/TypeScript). El proyecto tiene ~500 pantallas y ~400 entidades, por lo que necesitamos un proceso automatizado y consistente.

## Objetivo

Migrar formularios BPMN (archivos `.zul`) a páginas Next.js siguiendo el patrón establecido, manteniendo:
- Estructura y funcionalidad del formulario original
- Internacionalización (español/inglés)
- Integración con el layout de la aplicación
- Validaciones y manejo de errores
- **NOTA: La integración con el backend queda pendiente** (usar mock data por ahora)

## Archivos de Referencia

### Ejemplo Migrado (Patrón a Seguir)
- **Next.js**: `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx`
- **Original ZKoss**: `suinsit.nova.web/src/main/webapp/console/bpmn/internal-audit-scheduling-form.zul`
- **ViewModel Java**: `suinsit.nova.web/src/main/java/com/codeflowx/govern/workflow/viewmodels/InternalAuditSchedulingViewModel.java`

### Archivos de Configuración
- **Módulos**: `app/config/modules.ts` (agregar entrada en `getMenuByModule` para "BPMN")
- **Traducciones**: `app/config/i18n.ts` (agregar claves en español e inglés)
- **Sidebar**: `components/layout/sidebar.tsx` (agregar icono si no existe)

## Estructura del Formulario Migrado

### 1. Ubicación del Archivo
```
app/(app)/bpmn/forms/{form-name}/page.tsx
```

**Ejemplo:**
- Formulario: `internal-audit-scheduling-form.zul`
- Ruta: `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx`

### 2. Estructura Base del Componente

```typescript
// app/(app)/bpmn/forms/{form-name}/page.tsx
// Migrado de: {original-form-name}.zul
'use client'

import { useTranslation } from '@/app/config/i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import DevelopmentBanner from '@/components/ui/development-banner'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { ArrowLeft, Calendar, Save } from 'lucide-react' // Reemplazar Calendar con el icono apropiado
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useState } from 'react'

function {FormName}FormContent() {
  const { t } = useTranslation()
  const params = useParams()
  const searchParams = useSearchParams()
  // El taskId viene de la URL: /bpmn/forms/{form-name}/[id] o /bpmn/forms/{form-name}?taskId=xxx
  const taskId = (params.id as string) || searchParams.get('taskId') || ''
  const projectIdParam = searchParams.get('projectId')

  // Estado del formulario (mapear campos del ViewModel)
  const [field1, setField1] = useState<string>('')
  const [field2, setField2] = useState<string>('')
  const [dateValue, setDateValue] = useState<string>('') // Agregar estado para fecha
  const [loading, setLoading] = useState(false)
  const [errors, setErrors] = useState<Record<string, string>>({})

  // Validación del formulario
  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {}

    // Agregar validaciones según el ViewModel
    if (!field1 || field1.trim() === '') {
      newErrors.field1 = t('{formKey}.errors.field1Required', 'Mensaje de error')
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  // Manejo del submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!validateForm()) {
      return
    }

    setLoading(true)
    try {
      // Mapear variables según el ViewModel (doSubmit)
      const variables = {
        variable1: field1,
        variable2: field2,
        // ... más variables según el ViewModel
        projectId: projectIdParam ? Number.parseInt(projectIdParam, 10) : null,
        // TODO: obtener usuario del contexto de autenticación cuando esté disponible
        submittedBy: 'current-user'
      }

      const response = await fetch(`/api/bpmn/tasks/${taskId}/complete`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ variables })
      })

      if (response.ok) {
        // Redirigir a la bandeja de tareas
        if (globalThis.window !== undefined) {
          globalThis.window.location.href = '/bpmn/task-inbox'
        }
      } else {
        const error = await response.json()
        alert(error.error || t('{formKey}.errors.submitError', 'Error al procesar el formulario'))
      }
    } catch (error) {
      console.error('Error procesando formulario:', error)
      alert(t('{formKey}.errors.submitError', 'Error al procesar el formulario'))
    } finally {
      setLoading(false)
    }
  }

  // Manejo del cancel
  const handleCancel = () => {
    if (globalThis.window !== undefined) {
      globalThis.window.location.href = '/bpmn/task-inbox'
    }
  }

  return (
    <div className="space-y-6">
      <DevelopmentBanner />

      {/* Header */}
      <div className="flex items-center gap-4">
        <Button variant="outline" size="sm" onClick={handleCancel}>
          <ArrowLeft size={16} className="mr-2" />
          {t('{formKey}.back', 'Volver')}
        </Button>
        <div>
          <h1 className="text-2xl font-bold flex items-center gap-2">
            <Calendar className="w-6 h-6 text-primary" /> {/* Reemplazar Calendar con el icono apropiado */}
            {t('{formKey}.title', 'Título del Formulario')}
          </h1>
          <p className="text-muted-foreground">
            {t('{formKey}.subtitle', 'Descripción del formulario')}
          </p>
        </div>
      </div>

      {/* Formulario */}
      <Card className="group hover:scale-[1.01] transition-all duration-700 bg-card/80 backdrop-blur-sm border-border shadow-lg">
        <CardHeader>
          <CardTitle>
            {t('{formKey}.formTitle', 'Datos del Formulario')}
          </CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Campos del formulario */}
            {/* Mapear cada campo del ZUL a un Input/Textarea/Select según corresponda */}

            {/* Ejemplo: Campo de texto */}
            <div className="space-y-2">
              <Label htmlFor="field1" className="font-semibold">
                {t('{formKey}.field1', 'Etiqueta del Campo')}
              </Label>
              <Input
                id="field1"
                type="text"
                value={field1}
                onChange={(e) => setField1(e.target.value)}
                placeholder={t('{formKey}.field1Placeholder', 'Placeholder')}
                className={errors.field1 ? 'border-destructive' : ''}
              />
              {errors.field1 && (
                <p className="text-sm text-destructive">{errors.field1}</p>
              )}
            </div>

            {/* Ejemplo: Textarea */}
            <div className="space-y-2">
              <Label htmlFor="field2" className="font-semibold">
                {t('{formKey}.field2', 'Etiqueta del Campo')}
              </Label>
              <textarea
                id="field2"
                value={field2}
                onChange={(e) => setField2(e.target.value)}
                rows={4}
                placeholder={t('{formKey}.field2Placeholder', 'Placeholder')}
                className={`w-full px-3 py-2 border rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 ${errors.field2 ? 'border-destructive' : 'border-gray-300 dark:border-gray-700'}`}
              />
              {errors.field2 && (
                <p className="text-sm text-destructive">{errors.field2}</p>
              )}
            </div>

            {/* Ejemplo: Date/DateTime */}
            <div className="space-y-2">
              <Label htmlFor="dateField" className="font-semibold">
                {t('{formKey}.date', 'Fecha')}
              </Label>
              <Input
                id="dateField"
                type="datetime-local"
                value={dateValue}
                onChange={(e) => setDateValue(e.target.value)}
                className={errors.dateField ? 'border-destructive' : ''}
              />
              {errors.dateField && (
                <p className="text-sm text-destructive">{errors.dateField}</p>
              )}
            </div>

            {/* Botones */}
            <div className="flex gap-2 justify-end pt-4 border-t">
              <Button type="button" variant="outline" onClick={handleCancel} disabled={loading}>
                {t('{formKey}.cancel', 'Cancelar')}
              </Button>
              <Button type="submit" disabled={loading}>
                <Save size={16} className="mr-2" />
                {loading
                  ? t('{formKey}.saving', 'Guardando...')
                  : t('{formKey}.submit', 'Enviar')}
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default function {FormName}Page() {
  return (
    <Suspense fallback={
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary" aria-label="Cargando...">
          <span className="sr-only">Cargando...</span>
        </div>
      </div>
    }>
      <{FormName}FormContent />
    </Suspense>
  )
}
```

## Pasos de Migración

### Paso 1: Analizar el Formulario ZUL Original

1. **Leer el archivo `.zul`**:
   - Identificar campos del formulario (`textbox`, `datebox`, `textarea`, `combobox`, etc.)
   - Identificar validaciones y restricciones
   - Identificar el título y descripción

2. **Leer el ViewModel Java asociado**:
   - Identificar propiedades (`@Getter @Setter`)
   - Identificar comandos (`@Command`)
   - Identificar validaciones en `doSubmit()`
   - Identificar variables que se pasan a `taskService.complete()`
   - Identificar valores por defecto (ej: `defaultDate()`)

### Paso 2: Crear la Página Next.js

1. **Crear el archivo** en `app/(app)/bpmn/forms/{form-name}/page.tsx`
2. **Mapear campos ZUL a React**:
   - `<textbox>` → `<Input>` o `<textarea>`
   - `<datebox>` → `<Input type="datetime-local">`
   - `<combobox>` → `<Select>` (si existe componente)
   - `<label>` → `<Label>`
   - `<button>` → `<Button>`

3. **Mapear estado del ViewModel**:
   - Cada propiedad del ViewModel → `useState`
   - Valores por defecto → inicialización de `useState`

4. **Mapear validaciones**:
   - Validaciones del `doSubmit()` → función `validateForm()`
   - Mensajes de error → claves de traducción

5. **Mapear comandos**:
   - `doSubmit()` → `handleSubmit()`
   - `doCancel()` → `handleCancel()`
   - Variables de `taskService.complete()` → objeto `variables` en el fetch

### Paso 3: Agregar Traducciones

En `app/config/i18n.ts`, agregar en la sección correspondiente:

```typescript
// Español (es)
{formKey}: {
  title: "Título del Formulario",
  subtitle: "Descripción del formulario",
  formTitle: "Datos del Formulario",
  field1: "Etiqueta del Campo 1",
  field1Placeholder: "Placeholder del campo 1",
  field2: "Etiqueta del Campo 2",
  field2Placeholder: "Placeholder del campo 2",
  date: "Fecha",
  back: "Volver",
  cancel: "Cancelar",
  submit: "Enviar",
  saving: "Guardando...",
  errors: {
    field1Required: "El campo 1 es requerido",
    field2Required: "El campo 2 es requerido",
    dateRequired: "La fecha es requerida",
    submitError: "Error al procesar el formulario",
  },
},

// Inglés (en)
{formKey}: {
  title: "Form Title",
  subtitle: "Form description",
  formTitle: "Form Data",
  field1: "Field 1 Label",
  field1Placeholder: "Field 1 placeholder",
  field2: "Field 2 Label",
  field2Placeholder: "Field 2 placeholder",
  date: "Date",
  back: "Back",
  cancel: "Cancel",
  submit: "Submit",
  saving: "Saving...",
  errors: {
    field1Required: "Field 1 is required",
    field2Required: "Field 2 is required",
    dateRequired: "Date is required",
    submitError: "Error processing the form",
  },
},
```

**Nota**: `{formKey}` debe ser un identificador único en camelCase (ej: `internalAuditScheduling`)

### Paso 4: Agregar al Menú del Sidebar

En `app/config/modules.ts`, en la función `getMenuByModule`, caso `"BPMN"`:

```typescript
case "BPMN":
  return [
    // ... entradas existentes ...
    {
      name: "{Display Name}",
      href: `/bpmn/forms/{form-name}`,
      icon: "{IconName}", // Icono de lucide-react (ej: "Calendar", "FileText", etc.)
      roles: [
        "admin",
        "developer",
        // ... roles según el ViewModel o requerimientos
      ],
      isDefault: false,
    },
  ];
```

**Nota**: Si el icono no existe en `sidebar.tsx`, agregarlo:
1. Importar en `components/layout/sidebar.tsx`
2. Agregar al objeto `iconComponents`

### Paso 5: Verificar y Probar

1. **Verificar linter**: `read_lints` en el archivo creado
2. **Verificar que aparece en el sidebar**: Seleccionar módulo BPMN y verificar que la opción aparece
3. **Probar navegación**: Hacer clic y verificar que carga correctamente
4. **Probar validaciones**: Intentar enviar sin completar campos requeridos
5. **Probar submit**: Completar y enviar (debe redirigir a `/bpmn/task-inbox`)

## Mapeo de Componentes ZUL → React

| ZUL | React/Next.js | Notas |
|-----|---------------|-------|
| `<textbox>` | `<Input>` | Para campos de texto simples |
| `<textbox multiline="true">` | `<textarea>` | Para campos de texto multilínea |
| `<datebox>` | `<Input type="datetime-local">` | Para fechas y horas |
| `<datebox format="yyyy-MM-dd">` | `<Input type="date">` | Solo fecha (sin hora) |
| `<combobox>` | `<Select>` | Si existe componente Select, sino usar Input con datalist |
| `<combobox model="@load(vm.options)">` | `<Select>` con `options` desde estado/API | Combobox con datos dinámicos |
| `<listbox>` | `<Select multiple>` o componente custom | Lista de selección múltiple |
| `<checkbox>` | `<input type="checkbox">` o componente Checkbox | Casillas de verificación |
| `<radiogroup>` | Radio buttons con `<input type="radio">` | Grupo de opciones radio |
| `<label>` | `<Label>` | Etiquetas de campos |
| `<button>` | `<Button>` | Botones de acción |
| `<groupbox>` | `<Card>` | Contenedores de secciones |
| `<tabbox>` | Componente Tabs (shadcn/ui) | Pestañas/tabs |
| `<grid>` | `<div className="space-y-6">` | Layout de formulario |
| `<row>` | `<div className="space-y-2">` o `<div className="grid grid-cols-2 gap-4">` | Fila de formulario |
| `<fileupload>` | `<Input type="file">` o componente FileUpload | Carga de archivos |

## Mapeo de Atributos ZUL → React

| ZUL | React | Notas |
|-----|------|-------|
| `value="@bind(vm.field)"` | `value={field} onChange={(e) => setField(e.target.value)}` | Binding bidireccional |
| `readonly="true"` | `readOnly` | Campo de solo lectura |
| `placeholder="..."` | `placeholder={t('...')}` | Placeholder internacionalizado |
| `onClick="@command('doSubmit')"` | `onSubmit={handleSubmit}` | Manejo de submit |
| `format="yyyy-MM-dd HH:mm"` | `type="datetime-local"` | Formato de fecha/hora |

## Validaciones Comunes

### Validación de Campo Requerido
```typescript
if (!field || field.trim() === '') {
  newErrors.field = t('{formKey}.errors.fieldRequired', 'El campo es requerido')
}
```

### Validación de Fecha
```typescript
if (!dateField) {
  newErrors.dateField = t('{formKey}.errors.dateRequired', 'La fecha es requerida')
}
```

### Validación de Email (si aplica)
```typescript
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
if (email && !emailRegex.test(email)) {
  newErrors.email = t('{formKey}.errors.invalidEmail', 'Email inválido')
}
```

## Valores por Defecto

Si el ViewModel tiene métodos como `defaultDate()`, mapearlos a la inicialización de `useState`:

```typescript
const [auditDate, setAuditDate] = useState<string>(() => {
  // Fecha por defecto: 14 días desde hoy a las 9:00
  const date = new Date()
  date.setDate(date.getDate() + 14)
  date.setHours(9, 0, 0, 0)
  return date.toISOString().slice(0, 16) // Formato YYYY-MM-DDTHH:mm
})
```

## Variables del TaskService

Mapear las variables que se pasan a `taskService.complete(taskId, variables)` en el ViewModel:

```typescript
const variables = {
  variable1: field1,
  variable2: field2,
  // ... más variables según el ViewModel
  projectId: projectIdParam ? Number.parseInt(projectIdParam, 10) : null,
  submittedBy: 'current-user' // TODO: obtener del contexto cuando esté disponible
}
```

## Notas Importantes

1. **Integración con Backend**: La integración real con el backend queda **PENDIENTE**. Por ahora:
   - Usar mock data en las API routes
   - El endpoint `/api/bpmn/tasks/${taskId}/complete` debe existir (ya existe para el ejemplo)

2. **Autenticación**: El usuario actual se obtiene de `localStorage` o contexto cuando esté disponible. Por ahora usar `'current-user'` como placeholder.

3. **Navegación**: Usar `globalThis.window.location.href` para navegación directa (más confiable que `router.push` en este contexto).

4. **Internacionalización**: Todas las cadenas de texto deben usar `t()` con claves anidadas.

5. **Errores**: Mostrar errores de validación debajo de cada campo y errores de submit con `alert()` (mejorar con toast notifications cuando esté disponible).

6. **Loading States**: Mostrar estado de carga durante el submit y deshabilitar botones.

## Checklist de Migración

- [ ] Archivo `.zul` leído y analizado
- [ ] ViewModel Java leído y analizado
- [ ] Página Next.js creada en `app/(app)/bpmn/forms/{form-name}/page.tsx`
- [ ] Campos mapeados correctamente
- [ ] Validaciones implementadas
- [ ] Traducciones agregadas (español e inglés)
- [ ] Entrada agregada al menú BPMN en `modules.ts`
- [ ] Icono agregado al sidebar si no existe
- [ ] Linter sin errores
- [ ] Probado en el navegador
- [ ] Navegación funciona correctamente
- [ ] Validaciones funcionan correctamente
- [ ] Submit redirige a `/bpmn/task-inbox`

## Ejemplo Completo

Ver el ejemplo migrado en:
- `app/(app)/bpmn/forms/internal-audit-scheduling/page.tsx`
- `app/config/i18n.ts` (buscar `internalAuditScheduling`)
- `app/config/modules.ts` (buscar `case "BPMN"`)

## Casos Especiales

### Combobox con Datos Dinámicos

Si el ViewModel carga opciones desde el backend:

```typescript
const [options, setOptions] = useState<Array<{value: string, label: string}>>([])
const [selectedOption, setSelectedOption] = useState<string>('')

useEffect(() => {
  // Cargar opciones desde API
  fetch(`/api/bpmn/forms/${formName}/options`)
    .then(res => res.json())
    .then(data => setOptions(data))
}, [])
```

### Formularios con Tabs/Secciones

Si el formulario ZUL usa `<tabbox>`, crear un componente con tabs:

```typescript
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

<Tabs defaultValue="section1">
  <TabsList>
    <TabsTrigger value="section1">{t('{formKey}.section1', 'Sección 1')}</TabsTrigger>
    <TabsTrigger value="section2">{t('{formKey}.section2', 'Sección 2')}</TabsTrigger>
  </TabsList>
  <TabsContent value="section1">
    {/* Campos de la sección 1 */}
  </TabsContent>
  <TabsContent value="section2">
    {/* Campos de la sección 2 */}
  </TabsContent>
</Tabs>
```

### Campos Condicionales

Si un campo aparece/desaparece según otro valor:

```typescript
const [showAdditionalField, setShowAdditionalField] = useState(false)

{showAdditionalField && (
  <div className="space-y-2">
    <Label>{t('{formKey}.additionalField', 'Campo Adicional')}</Label>
    <Input />
  </div>
)}
```

### Carga de Archivos

Si el formulario incluye `<fileupload>`:

```typescript
const [file, setFile] = useState<File | null>(null)

const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  if (e.target.files && e.target.files[0]) {
    setFile(e.target.files[0])
  }
}

// En el submit, incluir el archivo en FormData si es necesario
```

## Estructura de URL y Obtención de taskId

Los formularios BPMN pueden accederse de dos formas:

1. **Desde la bandeja de tareas**: `/bpmn/forms/{form-name}?taskId={taskId}&projectId={projectId}`
2. **Directo con ID en ruta**: `/bpmn/forms/{form-name}/[id]?projectId={projectId}`

El código del ejemplo maneja ambos casos. Si solo se usa query params, simplificar:

```typescript
const taskId = searchParams.get('taskId') || ''
```

## Siguientes Pasos (Pendientes)

1. **Integración con Backend**: Conectar con el backend real cuando esté disponible
2. **Autenticación**: Obtener usuario real del contexto de autenticación
3. **Toast Notifications**: Reemplazar `alert()` con notificaciones toast (usar `sonner` o similar)
4. **Mejoras de UX**: Agregar animaciones y feedback visual mejorado
5. **Testing**: Agregar tests unitarios y de integración
6. **Validación en Tiempo Real**: Agregar validación mientras el usuario escribe (opcional)
7. **Auto-guardado**: Implementar auto-guardado de borradores (opcional)
