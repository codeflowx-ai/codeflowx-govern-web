import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

const USE_MOCK_LOCAL = process.env.USE_MOCK === "true" || process.env.NODE_ENV === "development";

// Mock data para proyectos con declaraciones de conformidad
const mockProjects = [
  {
    projectId: 1,
    projectName: "Healthcare Diagnostics AI",
    description: "Sistema de diagnóstico médico con IA",
    totalDeclarations: 2,
    signedDeclarations: 1,
    draftDeclarations: 1,
    latestDeclarationDate: "2025-01-15T00:00:00Z",
    latestDeclarationStatus: "SIGNED",
    latestDeclarationVersion: "v1.2",
    hasDeclarations: true,
  },
  {
    projectId: 2,
    projectName: "Financial Fraud Detection",
    description: "Sistema de detección de fraude financiero",
    totalDeclarations: 1,
    signedDeclarations: 0,
    draftDeclarations: 1,
    latestDeclarationDate: "2025-01-14T00:00:00Z",
    latestDeclarationStatus: "DRAFT",
    latestDeclarationVersion: "v1.0",
    hasDeclarations: true,
  },
  {
    projectId: 3,
    projectName: "Manufacturing Quality Control",
    description: "Control de calidad en manufactura",
    totalDeclarations: 0,
    signedDeclarations: 0,
    draftDeclarations: 0,
    hasDeclarations: false,
  },
];

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const page = parseInt(searchParams.get("page") || "0");
    const size = parseInt(searchParams.get("size") || "20");
    const hasDeclarations = searchParams.get("hasDeclarations");
    const search = searchParams.get("search");

    if (USE_MOCK || USE_MOCK_LOCAL) {
      // Filtrar por hasDeclarations
      let filtered = mockProjects;
      if (hasDeclarations && hasDeclarations !== "all") {
        const hasDecls = hasDeclarations === "true";
        filtered = filtered.filter((p) => p.hasDeclarations === hasDecls);
      }

      // Filtrar por búsqueda
      if (search) {
        const searchLower = search.toLowerCase();
        filtered = filtered.filter(
          (p) =>
            p.projectName.toLowerCase().includes(searchLower) ||
            (p.description && p.description.toLowerCase().includes(searchLower))
        );
      }

      // Calcular estadísticas
      const totalProjects = filtered.length;
      const projectsWithDeclarations = filtered.filter((p) => p.hasDeclarations).length;
      const totalDeclarations = filtered.reduce((sum, p) => sum + p.totalDeclarations, 0);
      const totalSigned = filtered.reduce((sum, p) => sum + p.signedDeclarations, 0);
      const totalDrafts = filtered.reduce((sum, p) => sum + p.draftDeclarations, 0);

      // Paginación
      const start = page * size;
      const end = start + size;
      const paginatedProjects = filtered.slice(start, end);
      const totalPages = Math.ceil(filtered.length / size);

      return NextResponse.json({
        success: true,
        data: {
          projects: paginatedProjects,
          pagination: {
            page,
            size,
            totalElements: filtered.length,
            totalPages,
          },
          statistics: {
            totalProjects,
            projectsWithDeclarations,
            totalDeclarations,
            totalSigned,
            totalDrafts,
          },
        },
      });
    }

    // Llamada real al backend
    const params = new URLSearchParams();
    params.append("page", String(page));
    params.append("size", String(size));
    if (hasDeclarations && hasDeclarations !== "all") {
      params.append("hasDeclarations", hasDeclarations);
    }
    if (search) params.append("search", search);

    const response = await fetch(
      `${BFF_BASE_URL}/api/v1/conformity-declaration/projects?${params.toString()}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(`Backend responded with status ${response.status}`);
    }

    const data = await response.json();

    return NextResponse.json({
      success: true,
      data,
    });
  } catch (error) {
    console.error("Error fetching conformity declaration projects:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Failed to fetch projects",
      },
      { status: 500 }
    );
  }
}
