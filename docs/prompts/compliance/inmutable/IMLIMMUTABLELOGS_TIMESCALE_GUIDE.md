# IMLIMMUTABLELOGS · Hash Chains + Timescale Hypertable

## 1. Qué guarda esta tabla

`IMLIMMUTABLELOGS` es la bitácora oficial para todo evento relevante del dominio Govern. Cada fila almacena:

- `IMLACTION`, `IMLENTITYTYPE`, `IMLENTITYID`, `IMLUSERID` + snapshot `IMLDATA`.
- `IMLTIMESTAMP` y `IMLTIMESTAMPEPOCH` para ordenar cronológicamente.
- `IMLPREVIOUSHASH` / `IMLCURRENTHASH`: hash SHA-256 encadenado a la fila anterior (estilo blockchain).
- Datos de contexto (`IMLIPADDRESS`, `IMLUSERAGENT`, `IMLUSERNAME`, etc.) y flags de verificación (`IMLVERIFIED`, `IMLINTEGRITYSTATUS`).

## 2. Cómo se asegura la inmutabilidad

1. **Trigger `trg_iml_hash_chain`**
   - Se ejecuta antes de cada `INSERT`.
   - Busca el hash del registro anterior (`IMLCURRENTHASH` del último `IDXIMMUTABLELOG`).
   - Si no existe, usa el bloque génesis `GENESIS_BLOCK_CODEFLOWX_GOVERN`.
   - Calcula `IMLCURRENTHASH = SHA256(prev_hash || datos del evento)` y setea `IMLPREVIOUSHASH`.

2. **Triggers `trg_iml_no_update` y `trg_iml_no_delete`**
   - Rechazan cualquier `UPDATE` o `DELETE` con un `RAISE EXCEPTION` (Art. 19).
   - Se documenta el intento indicando la operación y el ID afectado.

3. **Campos auxiliares**
   - `IMLTIMESTAMP` se rellena en `@PrePersist` para garantizar consistencia.
   - `IMLDATA` contiene una foto del estado original (JSON) que permite reconstruir el evento.

## 3. Por qué usamos una hypertable (Timescale)

Los logs crecen de forma lineal y son consultados por ventanas temporales. Para optimizar:

- **Hypertable** sobre `IMLTIMESTAMP` (columna de partición) + `IDXIMMUTABLELOG` como segment key.
- **Retención**: política automática que conserva 10 años de historial (`add_retention_policy`).
- **Compresión**: `ALTER TABLE ... SET (timescaledb.compress, ...)` + `add_compression_policy` tras 90 días, reduciendo almacenamiento ~10x.
- **Índices**:
  - PK `(IMLTIMESTAMP, IDXIMMUTABLELOG)` requerido por Timescale.
  - Índices únicos adicionales (`IMLTIMESTAMP` + `iduuid` / `IMLCURRENTHASH`).
  - `idx_iml_entity_timestamp` (GIST) para filtrar por entidad + rango temporal.

En resumen, Timescale nos da retención automática, compresión y consultas rápidas sin perder la semántica de hash-chain.

## 4. Backup / Restore de los schemas Timescale

### 4.1 Estrategia recomendada

1. **`pg_dump` + `--schema`**
   - Respaldar todos los schemas relevantes (`public`, `timescaledb_information`, etc.).
   - Comando típico:
     ```bash
     pg_dump \
       --format=custom \
       --file=/backups/codeflowx_$(date +%Y%m%d).dump \
       --schema=public \
       --schema=timescaledb_information \
       --schema=timescaledb_experimental \
       <connection-string>
     ```
   - Incluye metadatos de la hypertable (políticas, compresión, etc.).

2. **Snapshot físico (opcional)**
   - Para entornos HA se recomienda `pg_basebackup` o snapshots a nivel disco si el volumen es grande.
   - Asegurar que el directorio `pg_wal` se respalde junto al data dir.

3. **Restauración**
   - `pg_restore --clean --if-exists --create --dbname=postgres backup.dump`.
   - Verificar que la extensión timescaledb está instalada antes de restaurar (`CREATE EXTENSION IF NOT EXISTS timescaledb;`).

### 4.2 Procedimiento específico para hypertables

Timescale recomienda no desconectarse de `pg_dump`:

- `pg_dump` captura definición + datos comprimidos sin pasos extra.
- Si se usa compresión, la restauración descomprime automáticamente.
- Para backups incrementales se puede usar el comando `timescaledb-backup` (si está habilitado) o rely en PITR con WAL.

### 4.3 Validaciones post-backup

1. Restaurar en un entorno temporal y ejecutar:
   ```sql
   SELECT * FROM timescaledb_information.hypertables WHERE hypertable_name = 'imlimmutablelogs';
   ```
2. Correr `SELECT verify_audit_chain()` para garantizar que la cadena hash se mantiene intacta.
3. Confirmar que las políticas siguen activas:
   ```sql
   SELECT * FROM timescaledb_information.jobs WHERE hypertable_name = 'imlimmutablelogs';
   ```

## 5. Checklist operativo

- [ ] Extensión `timescaledb` instalada (`SHOW shared_preload_libraries`).
- [ ] Script `2025_11_16_prompts14_extensions.sql` ejecutado (crea triggers + hypertable).
- [ ] Backups automáticos diarios (`pg_dump` o snapshot).
- [ ] Restauraciones periódicas de prueba con verificación de hash-chain.
- [ ] Documentar cualquier mantenimiento (vacuum, retención) en `AUDITORIA_ENTIDADES.md`.

Con este flujo garantizamos trazabilidad criptográfica (hash chain) y observabilidad de largo plazo (Timescale) cumpliendo Art. 19 y requisitos de auditoría. Continuous backups + verificación de `verify_audit_chain()` son clave para detectar manipulaciones incluso en entornos restaurados. ***
