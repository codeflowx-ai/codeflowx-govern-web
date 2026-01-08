import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import { mockQmsData } from "@/app/(app)/governance/data/mockQms";

/**
 * GET /api/governance/compliance/qms/gaps
 * Obtiene los gaps detectados del QMS para un proyecto
 *
 * Query params:
 * - projectId: ID del proyecto (requerido)
 */
export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const projectId = searchParams.get("projectId");

    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "projectId is required" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Retornar gaps del mock data
      return NextResponse.json({
        success: true,
        data: {
          gaps: mockQmsData.gaps,
          overallScore: mockQmsData.overallScore,
          projectId: parseInt(projectId),
        },
      });
    }

    // Llamada real al backend
    // El backend debería tener un endpoint: GET /web/api/v1/compliance/qms/gaps?projectId={projectId}
    const response = await fetch(
      `${GATEWAY_WEB_BASE}/compliance/qms/gaps?projectId=${projectId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          // TODO: Agregar headers de autenticación cuando esté disponible
        },
      }
    );

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({
      success: true,
      data,
    });
  } catch (error) {
    console.error("Error in GET /api/governance/compliance/qms/gaps:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error loading QMS gaps",
      },
      { status: 500 }
    );
  }
}



