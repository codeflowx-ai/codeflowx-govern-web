import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";
import { mockHitlData } from "@/app/(app)/governance/data/mockHitl";

/**
 * GET /api/compliance/hitl/interventions
 *
 * Obtiene las intervenciones HITL pendientes con filtros opcionales.
 *
 * Query params:
 * - projectId: Filtrar por proyecto
 * - status: Filtrar por estado (PENDING, IN_REVIEW, APPROVED, REJECTED)
 * - type: Filtrar por tipo de entidad (Agent, Model, Prompt)
 *
 * Microservicio backend esperado: codeflowx-governance-hitl-service
 * Endpoint backend: GET /api/v1/hitl/interventions?projectId={projectId}&status={status}&type={type}
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");
    const status = searchParams.get("status");
    const type = searchParams.get("type");

    // Modo MOCK: Retornar datos mock filtrados
    if (USE_MOCK) {
      let interventions = [...mockHitlData.pendingInterventions];

      // Filtrar por proyecto si se especifica
      if (projectId) {
        interventions = interventions.filter(
          (i) => i.entityId.toString().includes(projectId)
        );
      }

      // Filtrar por estado
      if (status && status !== "ALL") {
        interventions = interventions.filter((i) => i.status === status);
      }

      // Filtrar por tipo
      if (type && type !== "ALL") {
        interventions = interventions.filter((i) => i.entityType === type);
      }

      return NextResponse.json({
        success: true,
        data: interventions,
      });
    }

    // Modo PRODUCCIÓN: Llamar al BFF que enruta al microservicio
    const backendUrl = new URL(`${BFF_BASE_URL}/api/v1/hitl/interventions`);
    if (projectId) backendUrl.searchParams.set("projectId", projectId);
    if (status) backendUrl.searchParams.set("status", status);
    if (type) backendUrl.searchParams.set("type", type);

    const response = await fetch(backendUrl.toString(), {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
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
    console.error("Error fetching HITL interventions:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error fetching HITL interventions"
      },
      { status: 500 }
    );
  }
}

/**
 * POST /api/compliance/hitl/interventions
 *
 * Registra una decisión humana para una intervención HITL.
 *
 * Body:
 * - interventionId: ID de la intervención
 * - decision: Decisión (APPROVED, REJECTED, MODIFIED)
 * - reason: Razón de la decisión
 * - userId: ID del usuario que toma la decisión
 *
 * Microservicio backend esperado: codeflowx-governance-hitl-service
 * Endpoint backend: POST /api/v1/hitl/interventions
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { interventionId, decision, reason, userId } = body;

    // Validaciones
    if (!interventionId || !decision || !reason) {
      return NextResponse.json(
        { success: false, error: "Missing required fields: interventionId, decision, reason" },
        { status: 400 }
      );
    }

    // Modo MOCK: Simular registro de decisión
    if (USE_MOCK) {
      const intervention = mockHitlData.pendingInterventions.find(
        (i) => i.id === interventionId
      );

      if (!intervention) {
        return NextResponse.json(
          { success: false, error: "Intervention not found" },
          { status: 404 }
        );
      }

      // Calcular tiempo de respuesta
      const createdAt = new Date(intervention.createdAt);
      const now = new Date();
      const responseTimeHours =
        (now.getTime() - createdAt.getTime()) / (1000 * 60 * 60);

      // Crear decisión
      const newDecision = {
        id: mockHitlData.recentDecisions.length + 1,
        type: intervention.type,
        entityType: intervention.entityType,
        entityId: intervention.entityId,
        entityName: intervention.entityName,
        decision: decision as "APPROVED" | "REJECTED" | "MODIFIED",
        decisionReason: reason,
        responseTime: responseTimeHours,
        decisionDate: now.toISOString(),
        userId: userId || "current-user",
      };

      return NextResponse.json({
        success: true,
        data: newDecision,
      });
    }

    // Modo PRODUCCIÓN: Llamar al BFF que enruta al microservicio
    const response = await fetch(`${BFF_BASE_URL}/api/v1/hitl/interventions`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
      },
      body: JSON.stringify({
        interventionId,
        decision,
        reason,
        userId: userId || "current-user",
      }),
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
    console.error("Error recording HITL decision:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error recording HITL decision"
      },
      { status: 500 }
    );
  }
}
