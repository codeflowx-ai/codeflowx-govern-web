import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import { mockQmsData } from "@/app/(app)/governance/data/mockQms";

/**
 * GET /api/governance/compliance/qms
 * Obtiene los datos del QMS para un proyecto
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
      // Retornar mock data
      return NextResponse.json({
        success: true,
        data: {
          ...mockQmsData,
          projectId: parseInt(projectId),
        },
      });
    }

    // Llamada real al backend
    // El backend debería tener un endpoint: GET /web/api/v1/compliance/qms?projectId={projectId}
    const response = await fetch(
      `${GATEWAY_WEB_BASE}/compliance/qms?projectId=${projectId}`,
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
    console.error("Error in GET /api/governance/compliance/qms:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error loading QMS data",
      },
      { status: 500 }
    );
  }
}

/**
 * POST /api/governance/compliance/qms
 * Calcula o actualiza el score QMS para un proyecto
 *
 * Body:
 * - projectId: ID del proyecto (requerido)
 * - action: "calculate" | "update" (opcional, default: "calculate")
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { projectId, action = "calculate" } = body;

    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "projectId is required" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Simular cálculo de score
      await new Promise((resolve) => setTimeout(resolve, 1000));

      return NextResponse.json({
        success: true,
        message: action === "calculate"
          ? "QMS score calculated successfully"
          : "QMS updated successfully",
        data: {
          ...mockQmsData,
          projectId: parseInt(projectId),
        },
      });
    }

    // Llamada real al backend
    const endpoint = action === "calculate"
      ? `${GATEWAY_WEB_BASE}/compliance/qms/calculate`
      : `${GATEWAY_WEB_BASE}/compliance/qms`;

    const response = await fetch(endpoint, {
      method: action === "calculate" ? "POST" : "PUT",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
      },
      body: JSON.stringify({ projectId }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({
      success: true,
      message: action === "calculate"
        ? "QMS score calculated successfully"
        : "QMS updated successfully",
      data,
    });
  } catch (error) {
    console.error("Error in POST /api/governance/compliance/qms:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error processing QMS request",
      },
      { status: 500 }
    );
  }
}



