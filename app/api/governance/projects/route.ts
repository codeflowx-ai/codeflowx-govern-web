import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BACKEND_BASE_URL } from "@/app/config/mock";

type ProjectListItem = {
  idxproject: number;
  name: string;
};

const mockProjects: ProjectListItem[] = [
  { idxproject: 1, name: "Customer Service AI" },
  { idxproject: 2, name: "Content AI" },
  { idxproject: 3, name: "Analytics AI" },
  { idxproject: 4, name: "Translation AI" },
  { idxproject: 5, name: "Dev Tools AI" },
];

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const search = (searchParams.get("search") || "").trim().toLowerCase();

    if (USE_MOCK) {
      const projects = search
        ? mockProjects.filter((p) => p.name.toLowerCase().includes(search))
        : mockProjects;

      return NextResponse.json({
        success: true,
        data: {
          projects,
          total: projects.length,
        },
      });
    }

    // Llamada real al backend (gateway/BFF)
    // Nota: este endpoint debe existir en tu gateway/bff. Si el path real difiere, ajústalo aquí.
    const url = new URL(`${BACKEND_BASE_URL}/api/v1/projects`);
    if (search) url.searchParams.set("search", search);
    url.searchParams.set("page", "0");
    url.searchParams.set("size", "1000");

    const response = await fetch(url.toString(), {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });

    if (!response.ok) {
      return NextResponse.json(
        { success: false, error: `Error listando proyectos: ${response.status}` },
        { status: 502 }
      );
    }

    const data = await response.json();

    // Normalización defensiva: soportar {items:[...]} o {projects:[...]} o lista directa
    const rawItems = Array.isArray(data)
      ? data
      : (data?.items ?? data?.projects ?? []);

    const projects: ProjectListItem[] = rawItems
      .map((p: any) => ({
        idxproject: Number(p.idxproject ?? p.projectId ?? p.id),
        name: String(p.name ?? p.projectName ?? p.projectname ?? ""),
      }))
      .filter((p: ProjectListItem) => Number.isFinite(p.idxproject) && p.name);

    return NextResponse.json({
      success: true,
      data: {
        projects,
        total: projects.length,
      },
    });
  } catch (error) {
    console.error("Error fetching projects:", error);
    return NextResponse.json(
      { success: false, error: "Error interno al listar proyectos" },
      { status: 500 }
    );
  }
}
