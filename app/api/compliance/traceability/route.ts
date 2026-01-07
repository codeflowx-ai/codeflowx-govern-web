import { NextRequest, NextResponse } from "next/server";
import { config } from "@/app/config/environment";
import {
  mockTraceability,
  mockEntityTraceability,
  mockProjectTraceability,
  mockAgentTraceability,
  type EntityTraceability,
} from "@/app/(app)/governance/data/mockTraceability";

export interface TraceabilitySearchCriteria {
  entityType?: string;
  entityId?: number;
  startDate?: string;
  endDate?: string;
  userId?: string;
  searchText?: string;
}

/**
 * GET /api/compliance/traceability
 *
 * Obtiene trazabilidad general o específica de una entidad.
 *
 * En modo MOCK: Retorna datos mock desde mockTraceability
 * En modo PRODUCCIÓN: Llama al microservicio de backend
 *
 * Microservicio backend esperado: codeflowx-governance-traceability-service
 * Endpoint backend: GET /api/v1/traceability?entityType={type}&entityId={id}
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const entityType = searchParams.get("entityType");
    const entityId = searchParams.get("entityId");

    // Modo MOCK: Retornar datos mock
    if (config.useMock) {
      // Si se especifica entidad, retornar trazabilidad específica
      if (entityType && entityId) {
        const id = parseInt(entityId);
        let traceabilityData: EntityTraceability | null = null;

        switch (entityType.toUpperCase()) {
          case "MODEL":
            traceabilityData = { ...mockEntityTraceability, entityId: id };
            break;
          case "PROJECT":
            traceabilityData = { ...mockProjectTraceability, entityId: id };
            break;
          case "AGENT":
            traceabilityData = { ...mockAgentTraceability, entityId: id };
            break;
          default:
            traceabilityData = { ...mockEntityTraceability, entityId: id };
        }

        return NextResponse.json({
          success: true,
          data: traceabilityData,
        });
      }

      // Retornar trazabilidad general
      return NextResponse.json({
        success: true,
        data: mockTraceability,
      });
    }

    // Modo PRODUCCIÓN: Llamar al microservicio de backend
    const backendUrl = new URL(`${config.backendUrl}/api/v1/traceability`);
    if (entityType) {
      backendUrl.searchParams.set("entityType", entityType);
    }
    if (entityId) {
      backendUrl.searchParams.set("entityId", entityId);
    }

    const authHeader = request.headers.get("authorization");

    const response = await fetch(backendUrl.toString(), {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        ...(authHeader && { Authorization: authHeader }),
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
    console.error("Error fetching traceability:", error);

    // Fallback a mock si está configurado
    if (config.fallbackToMock) {
      return NextResponse.json({
        success: true,
        data: mockTraceability,
      });
    }

    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error fetching traceability data",
      },
      { status: 500 }
    );
  }
}

/**
 * POST /api/compliance/traceability
 *
 * Busca trazabilidad con criterios de filtrado.
 *
 * En modo MOCK: Filtra datos mock según criterios
 * En modo PRODUCCIÓN: Llama al microservicio de backend
 *
 * Microservicio backend esperado: codeflowx-governance-traceability-service
 * Endpoint backend: POST /api/v1/traceability/search
 */
export async function POST(request: NextRequest) {
  try {
    const criteria: TraceabilitySearchCriteria = await request.json();

    // Modo MOCK: Filtrar trazabilidad según criterios
    if (config.useMock) {
      let filteredData = mockTraceability;

      // Aplicar filtros si es necesario
      if (criteria.entityType) {
        filteredData = {
          ...filteredData,
          logs: filteredData.logs.filter(
            (log) => log.entityType === criteria.entityType
          ),
        };
      }

      if (criteria.entityId) {
        filteredData = {
          ...filteredData,
          logs: filteredData.logs.filter(
            (log) => log.entityId === criteria.entityId
          ),
        };
      }

      if (criteria.startDate || criteria.endDate) {
        filteredData = {
          ...filteredData,
          logs: filteredData.logs.filter((log) => {
            const logDate = new Date(log.timestamp);
            if (criteria.startDate && logDate < new Date(criteria.startDate)) {
              return false;
            }
            if (criteria.endDate && logDate > new Date(criteria.endDate)) {
              return false;
            }
            return true;
          }),
        };
      }

      if (criteria.userId) {
        filteredData = {
          ...filteredData,
          logs: filteredData.logs.filter((log) =>
            log.userId.toLowerCase().includes(criteria.userId!.toLowerCase())
          ),
        };
      }

      return NextResponse.json({
        success: true,
        data: filteredData,
      });
    }

    // Modo PRODUCCIÓN: Llamar al microservicio de backend
    const backendUrl = `${config.backendUrl}/api/v1/traceability/search`;
    const authHeader = request.headers.get("authorization");

    const response = await fetch(backendUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(authHeader && { Authorization: authHeader }),
      },
      body: JSON.stringify(criteria),
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
    console.error("Error searching traceability:", error);

    // Fallback a mock si está configurado
    if (config.fallbackToMock) {
      return NextResponse.json({
        success: true,
        data: mockTraceability,
      });
    }

    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error searching traceability data",
      },
      { status: 500 }
    );
  }
}
