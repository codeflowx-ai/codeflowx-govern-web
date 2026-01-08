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

async function searchTranscripts(vscdbPath) {
  const initSqlJs = requireSqlJs();
  const SQL = await initSqlJs();
  const db = new SQL.Database(fs.readFileSync(vscdbPath));

  // Buscar todas las claves que puedan contener mensajes/transcripts
  const patterns = [
    "%composer%",
    "%message%",
    "%chat%",
    "%conversation%",
    "%transcript%",
    "%assistant%",
    "%response%",
    "%thread%",
    "%history%",
  ];

  const allKeys = new Set();

  for (const pattern of patterns) {
    const res = db.exec(
      `SELECT DISTINCT key FROM ItemTable WHERE key LIKE '${pattern}'`
    );
    if (res[0] && res[0].values) {
      res[0].values.forEach((row) => {
        allKeys.add(row[0]);
      });
    }
  }

  // También obtener todas las claves para ver qué hay
  const allRes = db.exec("SELECT key, length(value) as len FROM ItemTable ORDER BY len DESC LIMIT 100");

  console.log("=== Claves encontradas relacionadas con conversaciones ===");
  Array.from(allKeys).sort().forEach((key) => {
    console.log(`- ${key}`);
  });

  console.log("\n=== Top 100 claves por tamaño ===");
  if (allRes[0] && allRes[0].values) {
    allRes[0].values.forEach((row) => {
      console.log(`${row[0]} (${row[1]} bytes)`);
    });
  }

  // Buscar claves que contengan IDs de compositors específicos
  const composerKeys = db.exec(
    `SELECT key FROM ItemTable WHERE key LIKE 'composer.%' OR key LIKE '%.composer.%'`
  );

  console.log("\n=== Claves relacionadas con composer ===");
  if (composerKeys[0] && composerKeys[0].values) {
    composerKeys[0].values.forEach((row) => {
      console.log(`- ${row[0]}`);
    });
  }
}

const vscdbPath = process.argv[2] || "C:\\Users\\Manuel\\AppData\\Roaming\\Cursor\\User\\workspaceStorage\\339b1d28ceef130c67a12d8146b259d9\\state.vscdb";

searchTranscripts(vscdbPath).catch(console.error);
