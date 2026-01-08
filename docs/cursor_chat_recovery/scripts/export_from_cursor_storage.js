/* eslint-disable no-console */
/**
 * Exporta artefactos recuperables localmente de Cursor:
 * - state.vscdb (SQLite): composer.composerData, aiService.prompts, aiService.generations
 * - checkpoints metadata/diffs indexados por workspaceId
 *
 * Uso:
 *   node export_from_cursor_storage.js --vscdb <path> --checkpoints <dir> --workspaceId <id> --out <dir>
 */

const fs = require("fs");
const path = require("path");

function parseArgs(argv) {
  const out = {};
  for (let i = 2; i < argv.length; i++) {
    const a = argv[i];
    if (!a.startsWith("--")) continue;
    const k = a.slice(2);
    const v = argv[i + 1];
    out[k] = v;
    i++;
  }
  return out;
}

function ensureDir(dir) {
  fs.mkdirSync(dir, { recursive: true });
}

function writeJson(filePath, value) {
  fs.writeFileSync(filePath, JSON.stringify(value, null, 2), "utf8");
}

function safeReadJson(filePath) {
  try {
    return JSON.parse(fs.readFileSync(filePath, "utf8"));
  } catch {
    return null;
  }
}

function listDirs(dir) {
  try {
    return fs
      .readdirSync(dir, { withFileTypes: true })
      .filter((d) => d.isDirectory())
      .map((d) => d.name);
  } catch {
    return [];
  }
}

async function exportVscdb({ vscdbPath, outDir }) {
  // sql.js se instala por el wrapper PS en un temp node project.
  // Ojo: Node resuelve módulos relativos al directorio del *archivo* (este script),
  // no al cwd. Por eso intentamos también cargarlo desde el node_modules del cwd.
  function requireSqlJs() {
    try {
      return require("sql.js");
    } catch (e1) {
      const candidates = [
        path.join(process.cwd(), "node_modules", "sql.js"),
        path.join(process.cwd(), "node_modules", "sql.js", "dist", "sql-wasm.js"),
      ];
      for (const c of candidates) {
        try {
          return require(c);
        } catch {
          // continue
        }
      }
      throw e1;
    }
  }

  const initSqlJs = requireSqlJs();
  const SQL = await initSqlJs();
  const db = new SQL.Database(fs.readFileSync(vscdbPath));

  function getItem(key) {
    const res = db.exec(
      `SELECT key, CAST(value AS TEXT) AS text, length(value) AS len FROM ItemTable WHERE key='${key}' LIMIT 1`,
    );
    if (!res[0] || !res[0].values || res[0].values.length === 0) return null;
    const row = res[0].values[0];
    return { key: row[0], text: row[1], len: row[2] };
  }

  const keys = [
    "composer.composerData",
    "aiService.prompts",
    "aiService.generations",
    "workbench.backgroundComposer.workspacePersistentData",
  ];

  const raw = {};
  for (const k of keys) {
    const item = getItem(k);
    raw[k] = item ? { len: item.len, text: item.text } : null;
  }

  // Parse JSON when possible
  const parsed = {};
  for (const k of keys) {
    const t = raw[k]?.text;
    if (!t) {
      parsed[k] = null;
      continue;
    }
    try {
      parsed[k] = JSON.parse(t);
    } catch {
      parsed[k] = { _rawText: t };
    }
  }

  writeJson(path.join(outDir, "vscdb.keys.raw.json"), raw);
  writeJson(path.join(outDir, "vscdb.keys.parsed.json"), parsed);

  const composers = parsed["composer.composerData"]?.allComposers ?? [];
  writeJson(path.join(outDir, "composers.index.json"), {
    count: composers.length,
    composers,
  });

  console.log(`OK vscdb: exportados ${keys.length} keys; composers=${composers.length}`);
}

function exportCheckpointsIndex({ checkpointsDir, workspaceId, outDir }) {
  const ids = listDirs(checkpointsDir);
  const rows = [];

  for (const id of ids) {
    const metaPath = path.join(checkpointsDir, id, "metadata.json");
    const meta = safeReadJson(metaPath);
    if (!meta) continue;
    if (workspaceId && meta.workspaceId !== workspaceId) continue;

    rows.push({
      checkpointId: id,
      agentRequestId: meta.agentRequestId ?? null,
      workspaceId: meta.workspaceId ?? null,
      startTrackingDateUnixMilliseconds: meta.startTrackingDateUnixMilliseconds ?? null,
      fileSizeBytes: meta.fileSizeBytes ?? null,
      requestFiles: meta.requestFiles ?? [],
      metaPath,
      diffsDir: path.join(checkpointsDir, id, "diffs"),
    });
  }

  rows.sort(
    (a, b) =>
      (b.startTrackingDateUnixMilliseconds ?? 0) - (a.startTrackingDateUnixMilliseconds ?? 0),
  );

  writeJson(path.join(outDir, "checkpoints.index.json"), {
    workspaceId,
    totalMatched: rows.length,
    checkpoints: rows,
  });

  console.log(`OK checkpoints: matched=${rows.length}`);
}

async function main() {
  const args = parseArgs(process.argv);
  const vscdbPath = args.vscdb;
  const checkpointsDir = args.checkpoints;
  const workspaceId = args.workspaceId;
  const outDir = args.out;

  if (!vscdbPath || !checkpointsDir || !workspaceId || !outDir) {
    console.error(
      "Faltan argumentos. Uso: --vscdb <path> --checkpoints <dir> --workspaceId <id> --out <dir>",
    );
    process.exit(2);
  }

  ensureDir(outDir);

  await exportVscdb({ vscdbPath, outDir });
  exportCheckpointsIndex({ checkpointsDir, workspaceId, outDir });
}

main().catch((e) => {
  console.error(e);
  process.exit(1);
});
