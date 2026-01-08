const fs = require("fs");
const path = require("path");

function requireSqlJs() {
  try {
    return require("sql.js");
  } catch (e1) {
    const work = path.join(process.env.TEMP || "/tmp", "cursor-chat-recovery-sqljs");
    const candidates = [
      path.join(work, "node_modules", "sql.js"),
      path.join(process.cwd(), "node_modules", "sql.js"),
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

async function extractTranscripts(vscdbPath, outDir) {
  const initSqlJs = requireSqlJs();
  const SQL = await initSqlJs();
  const db = new SQL.Database(fs.readFileSync(vscdbPath));

  // Buscar todas las claves de composerChatViewPane
  const res = db.exec(
    `SELECT key, CAST(value AS TEXT) AS text, length(value) AS len
     FROM ItemTable
     WHERE key LIKE 'workbench.panel.composerChatViewPane.%'
     ORDER BY len DESC`
  );

  if (!res[0] || !res[0].values) {
    console.log("No se encontraron claves de composerChatViewPane");
    return;
  }

  const transcripts = [];

  for (const row of res[0].values) {
    const key = row[0];
    const text = row[1];
    const len = row[2];

    try {
      const parsed = JSON.parse(text);
      transcripts.push({
        key,
        size: len,
        data: parsed
      });
    } catch (e) {
      transcripts.push({
        key,
        size: len,
        error: "No se pudo parsear como JSON",
        raw: text.substring(0, 200)
      });
    }
  }

  const outputFile = path.join(outDir, "composerChatViewPanes.json");
  fs.writeFileSync(outputFile, JSON.stringify(transcripts, null, 2), "utf8");

  console.log(`Extraidas ${transcripts.length} claves de composerChatViewPane`);
  console.log(`Guardado en: ${outputFile}`);

  // Mostrar un ejemplo
  if (transcripts.length > 0) {
    console.log("\n=== Ejemplo de la primera clave ===");
    console.log(JSON.stringify(transcripts[0], null, 2).substring(0, 1000));
  }
}

const vscdbPath = process.argv[2] || "C:\\Users\\Manuel\\AppData\\Roaming\\Cursor\\User\\workspaceStorage\\339b1d28ceef130c67a12d8146b259d9\\state.vscdb";
const outDir = process.argv[3] || "C:\\Users\\Manuel\\Documents\\git\\codeflowx-studio\\docs\\cursor_chat_recovery\\output_all_workspaces\\339b1d28ceef130c67a12d8146b259d9";

extractTranscripts(vscdbPath, outDir).catch(console.error);
