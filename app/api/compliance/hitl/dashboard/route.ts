import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";
import { mockHitlData } from "@/app/(app)/governance/data/mockHitl";

/**
 * GET /api/compliance/hitl/dashboard
 *
 * Obtiene los datos del dashboard de HITL Supervision.
 *
 * En modo MOCK: Retorna datos mock desde mockHitlData
 * En modo PRODUCCIÓN: Llama al microservicio de backend
 *
 * Microservicio backend esperado: codeflowx-governance-hitl-service
 * Endpoint backend: GET /api/v1/hitl/dashboard?projectId={projectId}
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");

    // Modo MOCK: Retornar datos mock
    if (USE_MOCK) {
      let data = mockHitlData;

      // Filtrar por proyecto si se especifica
      if (projectId) {
        data = {
          ...data,
          pendingInterventions: data.pendingInterventions.filter(
            (i) => i.entityId.toString().includes(projectId)
          ),
          recentDecisions: data.recentDecisions.filter(
            (d) => d.entityId.toString().includes(projectId)
          ),
        };
      }

      return NextResponse.json({
        success: true,
        data: data,
      });
    }

    // Modo PRODUCCIÓN: Llamar al BFF que enruta al microservicio
    const backendUrl = new URL(`${BFF_BASE_URL}/api/v1/hitl/dashboard`);
    if (projectId) {
      backendUrl.searchParams.set("projectId", projectId);
    }

    const response = await fetch(backendUrl.toString(), {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
        // "Authorization": `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({
      success: true,
      data: data,
    });
  } catch (error) {
    console.error("Error fetching HITL dashboard data:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error fetching HITL dashboard data"
      },
      { status: 500 }
    );
  }
}
