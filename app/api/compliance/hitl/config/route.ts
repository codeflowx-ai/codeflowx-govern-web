import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import { mockHitlData } from "@/app/(app)/governance/data/mockHitl";

/**
 * GET /api/compliance/hitl/config
 *
 * Obtiene la configuración de supervisión HITL.
 *
 * Query params:
 * - projectId: Filtrar por proyecto (opcional)
 *
 * Microservicio backend esperado: codeflowx-governance-hitl-service
 * Endpoint backend: GET /web/api/v1/compliance/hitl/config?projectId={projectId}
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");

    // Modo MOCK: Retornar configuración mock
    if (USE_MOCK) {
      const config = mockHitlData.supervisionConfig;
      return NextResponse.json({
        success: true,
        data: config,
      });
    }

    // Modo PRODUCCIÓN: Llamar al BFF que enruta al microservicio
    const backendUrl = new URL(`${GATEWAY_WEB_BASE}/compliance/hitl/config`);
    if (projectId) {
      backendUrl.searchParams.set("projectId", projectId);
    }

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
    console.error("Error fetching HITL configuration:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error fetching HITL configuration"
      },
      { status: 500 }
    );
  }
}

/**
 * POST /api/compliance/hitl/config
 *
 * Actualiza la configuración de supervisión HITL.
 *
 * Body:
 * - type: Tipo de supervisión (PRE_DEPLOYMENT, IN_LOOP, POST_DEPLOYMENT, OVERRIDE)
 * - enabled: Habilitado/deshabilitado
 * - slaHours: SLA en horas
 * - requiredRoles: Roles requeridos
 * - autoEscalation: Escalación automática
 * - escalationHours: Horas para escalación
 *
 * Microservicio backend esperado: codeflowx-governance-hitl-service
 * Endpoint backend: POST /web/api/v1/compliance/hitl/config
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { type, enabled, slaHours, requiredRoles, autoEscalation, escalationHours } = body;

    // Validaciones
    if (!type) {
      return NextResponse.json(
        { success: false, error: "Missing required field: type" },
        { status: 400 }
      );
    }

    // Modo MOCK: Simular actualización de configuración
    if (USE_MOCK) {
      const config = mockHitlData.supervisionConfig.find((c) => c.type === type);

      if (!config) {
        return NextResponse.json(
          { success: false, error: "Configuration type not found" },
          { status: 404 }
        );
      }

      // Actualizar configuración
      config.enabled = enabled ?? config.enabled;
      config.slaHours = slaHours ?? config.slaHours;
      config.requiredRoles = requiredRoles ?? config.requiredRoles;
      config.autoEscalation = autoEscalation ?? config.autoEscalation;
      config.escalationHours = escalationHours ?? config.escalationHours;

      return NextResponse.json({
        success: true,
        data: config,
      });
    }

    // Modo PRODUCCIÓN: Llamar al BFF que enruta al microservicio
    const response = await fetch(`${GATEWAY_WEB_BASE}/compliance/hitl/config`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
      },
      body: JSON.stringify({
        type,
        enabled,
        slaHours,
        requiredRoles,
        autoEscalation,
        escalationHours,
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
    console.error("Error updating HITL configuration:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error updating HITL configuration"
      },
      { status: 500 }
    );
  }
}
