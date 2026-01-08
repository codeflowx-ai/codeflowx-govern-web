import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from "@/app/config/mock";

/**
 * GET /api/compliance/fria/assessments
 *
 * Lista todas las evaluaciones FRIA con filtros opcionales
 *
 * Backend: codeflowx-governance-fria-service
 * Endpoint: GET /web/api/v1/compliance/fria/assessments
 */
export async function GET(request: NextRequest) {
  try {
    if (USE_MOCK) {
      // Mock: Listar todas las evaluaciones FRIA
    const mockAssessments = [
      {
        id: 1,
        projectId: 1001,
        projectName: "AI Credit Scoring System",
        createdAt: "2025-12-01T10:00:00Z",
        status: "COMPLETED",
        completenessScore: 0.95,
        finalRisk: 0.42,
        riskLevel: "medium",
      },
      {
        id: 2,
        projectId: 1002,
        projectName: "Facial Recognition System",
        createdAt: "2025-12-02T14:30:00Z",
        status: "DRAFT",
        completenessScore: 0.60,
        finalRisk: null,
        riskLevel: null,
      },
      {
        id: 3,
        projectId: 1003,
        projectName: "Automated Hiring System",
        createdAt: "2025-12-03T09:15:00Z",
        status: "NOTIFIED",
        completenessScore: 1.0,
        finalRisk: 0.85,
        riskLevel: "high",
      },
    ];

    // Obtener parámetros de query para filtros
    const { searchParams } = new URL(request.url);
    const status = searchParams.get("status");
    const search = searchParams.get("search");

    let filtered = mockAssessments;

    if (status && status !== "all") {
      filtered = filtered.filter((a) => a.status === status);
    }

    if (search) {
      filtered = filtered.filter(
        (a) =>
          a.projectName.toLowerCase().includes(search.toLowerCase()) ||
          a.projectId.toString().includes(search)
      );
    }

      return NextResponse.json({
        success: true,
        data: filtered,
        total: filtered.length,
      });
    }

    // Llamada real al backend
    const searchParams = request.nextUrl.searchParams;
    const queryString = searchParams.toString();
    const url = `${GATEWAY_WEB_BASE}/compliance/fria/assessments${queryString ? `?${queryString}` : ""}`;

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error fetching assessments:", error);
    return NextResponse.json(
      { success: false, error: "Error fetching assessments" },
      { status: 500 }
    );
  }
}
