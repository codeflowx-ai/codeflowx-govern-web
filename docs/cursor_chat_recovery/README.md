## Recuperación local de chats de Agentes (Cursor)

Este workspace guarda **metadatos** de conversaciones y acciones del agente en almacenamiento local de Cursor (Windows):

- `state.vscdb` (SQLite): contiene, entre otros, `composer.composerData` (lista de conversaciones/composers), `aiService.prompts`, `aiService.generations`.
- `globalStorage/anysphere.cursor-retrieval/checkpoints/*`: contiene **checkpoints** y **diffs** asociados a ejecuciones (cambios de archivos), con `metadata.json`.

### Qué puedes recuperar (offline)

- **Lista de chats/conversaciones** (títulos, ids, fechas, modo) desde `composer.composerData`.
- **Historial de prompts** (lo que escribiste) desde `aiService.prompts`/`aiService.generations`.
- **Índice de checkpoints/diffs** que el agente ejecutó (qué archivos tocó y cuándo) desde `checkpoints/*/metadata.json` + `diffs/*`.

### Qué NO siempre se puede recuperar

- El **transcript completo** (mensajes del asistente) a veces **no se persiste localmente** como texto en `state.vscdb`.
  En esos casos, solo tendrás metadatos/prompt history y trazas de cambios (diffs).

### Cómo exportar

#### Exportar un workspace específico

Ejecuta el script:

```powershell
powershell -ExecutionPolicy Bypass -File docs/cursor_chat_recovery/scripts/export.ps1
```

Genera archivos en:

- `docs/cursor_chat_recovery/output/`

#### Exportar TODOS los workspaces (incluso cerrados)

Para recuperar chats/agentes de **todos los workspaces** que has usado (incluso si no están abiertos):

```powershell
.\docs\cursor_chat_recovery\scripts\export_all_workspaces.ps1
```

Este script:
- Busca automáticamente todos los workspaces con `state.vscdb` en `C:\Users\Manuel\AppData\Roaming\Cursor\User\workspaceStorage`
- Exporta cada uno a su propia carpeta en `docs/cursor_chat_recovery/output_all_workspaces/<workspace-id>/`
- Genera un resumen con estadísticas

#### Generar resumen consolidado

Después de exportar todos los workspaces, genera un resumen en markdown:

```powershell
.\docs\cursor_chat_recovery\scripts\create_summary.ps1
```

Esto crea `RESUMEN_COMPLETO.md` con:
- Lista de todos los workspaces exportados
- Total de conversaciones y checkpoints
- Lista completa de todas las conversaciones ordenadas por fecha

#### Exportación Diaria Automática

Para exportar **diariamente** todas las conversaciones a archivos markdown organizados por fechas:

```powershell
.\docs\cursor_chat_recovery\scripts\daily_export.ps1
```

Este script:
- Exporta **todos los workspaces** automáticamente
- Organiza los archivos por fecha en `C:\Users\Manuel\Documents\prompts\YYYY-MM-DD/`
- Genera un archivo markdown por cada conversación con todos los prompts y metadatos
- Crea un resumen diario con estadísticas

**Estructura de salida diaria:**
```
Documents/prompts/
  └── 2025-12-26/
      ├── resumen_2025-12-26.md
      └── workspace_<id>/
          ├── conversations/
          │   ├── Nombre Conversacion 1.md
          │   ├── Nombre Conversacion 2.md
          │   └── ...
          ├── composers.index.json
          ├── checkpoints.index.json
          └── vscdb.keys.parsed.json
```

**Instalar tarea programada (ejecución automática diaria):**

Para que se ejecute automáticamente cada día a las 2:00 AM:

```powershell
# Ejecutar PowerShell como Administrador
.\docs\cursor_chat_recovery\scripts\install_daily_task.ps1
```

**Nota importante:** Los transcripts completos de las respuestas del asistente no están disponibles localmente. Solo se pueden recuperar los prompts del usuario, metadatos y checkpoints. Cursor elimina los transcripts del servidor después de ~5 días.
