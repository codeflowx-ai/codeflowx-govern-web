import type { Project } from "./_mock";

const STORAGE_KEY = "codeflowx.projects.v1";

function safeParse(json: string | null): unknown {
  if (!json) return null;
  try {
    return JSON.parse(json);
  } catch {
    return null;
  }
}

export function getLocalProjects(): Project[] {
  if (typeof window === "undefined") return [];
  const raw = safeParse(window.localStorage.getItem(STORAGE_KEY));
  if (!Array.isArray(raw)) return [];
  // Trust pero verifica lo mínimo
  return raw.filter((p: any) => p && typeof p.id === "number") as Project[];
}

export function saveLocalProjects(projects: Project[]) {
  if (typeof window === "undefined") return;
  window.localStorage.setItem(STORAGE_KEY, JSON.stringify(projects));
}

export function upsertLocalProject(project: Project) {
  const current = getLocalProjects();
  const next = [...current.filter((p) => p.id !== project.id), project].sort(
    (a, b) => a.id - b.id
  );
  saveLocalProjects(next);
}
