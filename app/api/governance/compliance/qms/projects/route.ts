import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK } from "@/app/config/mock";

// Mock data para proyectos con QMS
const mockProjects = [
  {
    projectId: 1,
    projectName: "AI Credit Scoring System",
    description: "Sistema de evaluación crediticia basado en IA",
    overallScore: 0.82,
    complianceStatus: "PARTIAL",
    totalModules: 13,
    compliantModules: 10,
    gapsCount: 3,
    lastUpdated: "2025-12-15T14:30:00Z",
    createdAt: "2025-01-01T10:00:00Z",
  },
  {
    projectId: 2,
    projectName: "Medical Diagnosis Assistant",
    description: "Asistente de diagnóstico médico con IA",
    overallScore: 0.91,
    complianceStatus: "COMPLIANT",
    totalModules: 13,
    compliantModules: 13,
    gapsCount: 0,
    lastUpdated: "2025-12-14T16:20:00Z",
    createdAt: "2025-02-15T09:00:00Z",
  },
  {
    projectId: 3,
    projectName: "Autonomous Vehicle Control",
    description: "Sistema de control para vehículos autónomos",
    overallScore: 0.68,
    complianceStatus: "NON_COMPLIANT",
    totalModules: 13,
    compliantModules: 6,
    gapsCount: 7,
    lastUpdated: "2025-12-13T11:45:00Z",
    createdAt: "2025-03-01T08:00:00Z",
  },
];

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const page = parseInt(searchParams.get("page") || "0");
    const size = parseInt(searchParams.get("size") || "20");
    const status = searchParams.get("status");
    const search = searchParams.get("search");

    if (USE_MOCK) {
      // Filtrar por status
      let filtered = mockProjects;
      if (status && status !== "all") {
        filtered = filtered.filter((p) => p.complianceStatus === status);
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
      const totalCompliant = filtered.filter((p) => p.complianceStatus === "COMPLIANT").length;
      const totalPartial = filtered.filter((p) => p.complianceStatus === "PARTIAL").length;
      const totalNonCompliant = filtered.filter((p) => p.complianceStatus === "NON_COMPLIANT").length;
      const averageScore =
        filtered.reduce((sum, p) => sum + p.overallScore, 0) / (filtered.length || 1);

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
            totalCompliant,
            totalPartial,
            totalNonCompliant,
            averageScore,
          },
        },
      });
    }

    // Llamada real al backend
    const bffBaseUrl = process.env.GATEWAY_WEB_BASE || "http://localhost:8080";
    const params = new URLSearchParams();
    params.append("page", String(page));
    params.append("size", String(size));
    if (status) params.append("status", status);
    if (search) params.append("search", search);

    const response = await fetch(`${bffBaseUrl}/web/api/v1/compliance/qms/projects?${params.toString()}`);

    if (!response.ok) {
      throw new Error(`Backend responded with status ${response.status}`);
    }

    const data = await response.json();

    return NextResponse.json({
      success: true,
      data,
    });
  } catch (error) {
    console.error("Error fetching QMS projects:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Failed to fetch QMS projects",
      },
      { status: 500 }
    );
  }
}
