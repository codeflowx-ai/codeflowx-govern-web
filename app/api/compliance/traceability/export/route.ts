import { NextRequest, NextResponse } from "next/server";
import { config } from "@/app/config/environment";
import {
  mockEntityTraceability,
  mockProjectTraceability,
  mockAgentTraceability,
  type EntityTraceability,
} from "@/app/(app)/governance/data/mockTraceability";

export interface ExportRequest {
  entityType: string;
  entityId: number;
  format: "JSON" | "PDF";
}

/**
 * POST /api/compliance/traceability/export
 *
 * Exporta evidencias de trazabilidad en formato JSON o PDF.
 *
 * En modo MOCK: Retorna datos mock o simula exportación
 * En modo PRODUCCIÓN: Llama al microservicio de backend
 *
 * Microservicio backend esperado: codeflowx-governance-traceability-service
 * Endpoint backend: POST /web/api/v1/compliance/traceability/export
 */
export async function POST(request: NextRequest) {
  try {
    const body: ExportRequest = await request.json();
    const { entityType, entityId, format } = body;

    if (!entityType || !entityId || !format) {
      return NextResponse.json(
        { success: false, error: "Missing required fields: entityType, entityId, format" },
        { status: 400 }
      );
    }

    // Modo MOCK: Retornar datos mock o simular exportación
    if (config.useMock) {
      let traceabilityData: EntityTraceability | null = null;

      switch (entityType.toUpperCase()) {
        case "MODEL":
          traceabilityData = { ...mockEntityTraceability, entityId };
          break;
        case "PROJECT":
          traceabilityData = { ...mockProjectTraceability, entityId };
          break;
        case "AGENT":
          traceabilityData = { ...mockAgentTraceability, entityId };
          break;
        default:
          return NextResponse.json(
            { success: false, error: "Invalid entity type" },
            { status: 400 }
          );
      }

      if (format === "JSON") {
        // Retornar JSON directamente
        return NextResponse.json({
          success: true,
          data: traceabilityData,
          format: "JSON",
          exportedAt: new Date().toISOString(),
        });
      } else if (format === "PDF") {
        // En producción, aquí se generaría el PDF
        // Por ahora, retornamos un mensaje indicando que se generaría el PDF
        return NextResponse.json({
          success: true,
          message: "PDF generation would be implemented here",
          format: "PDF",
          exportedAt: new Date().toISOString(),
          // En producción, esto retornaría el PDF como blob
          // return new NextResponse(pdfBuffer, {
          //   headers: {
          //     "Content-Type": "application/pdf",
          //     "Content-Disposition": `attachment; filename="traceability-${entityType}-${entityId}.pdf"`,
          //   },
          // });
        });
      }

      return NextResponse.json(
        { success: false, error: "Invalid export format" },
        { status: 400 }
      );
    }

    // Modo PRODUCCIÓN: Llamar al microservicio de backend
    const backendUrl = `${config.backendUrl}/web/api/v1/compliance/traceability/export`;
    const authHeader = request.headers.get("authorization");

    const response = await fetch(backendUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(authHeader && { Authorization: authHeader }),
      },
      body: JSON.stringify({ entityType, entityId, format }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    // Si es PDF, retornar el blob directamente
    if (format === "PDF") {
      const pdfBuffer = await response.arrayBuffer();
      return new NextResponse(pdfBuffer, {
        headers: {
          "Content-Type": "application/pdf",
          "Content-Disposition": `attachment; filename="traceability-${entityType}-${entityId}.pdf"`,
        },
      });
    }

    // Si es JSON, retornar JSON
    const data = await response.json();
    return NextResponse.json({
      success: true,
      data: data,
      format: "JSON",
      exportedAt: new Date().toISOString(),
    });
  } catch (error) {
    console.error("Error exporting traceability:", error);

    // Fallback a mock si está configurado
    if (config.fallbackToMock) {
      const { entityType, entityId, format } = await request.json();
      let traceabilityData: EntityTraceability | null = null;

      switch (entityType?.toUpperCase()) {
        case "MODEL":
          traceabilityData = { ...mockEntityTraceability, entityId };
          break;
        case "PROJECT":
          traceabilityData = { ...mockProjectTraceability, entityId };
          break;
        case "AGENT":
          traceabilityData = { ...mockAgentTraceability, entityId };
          break;
      }

      if (traceabilityData && format === "JSON") {
        return NextResponse.json({
          success: true,
          data: traceabilityData,
          format: "JSON",
          exportedAt: new Date().toISOString(),
        });
      }
    }

    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error exporting traceability data",
      },
      { status: 500 }
    );
  }
}
