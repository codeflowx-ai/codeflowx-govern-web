import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import { mockQmsData } from "@/app/(app)/governance/data/mockQms";

/**
 * POST /api/governance/compliance/qms/review
 * Procesa una revisión de gaps QMS (aprobar o solicitar correcciones)
 *
 * Body:
 * - projectId: ID del proyecto (requerido)
 * - reviewerName: Nombre del revisor (requerido)
 * - reviewNotes: Notas de revisión (requerido)
 * - decision: "APPROVED" | "CORRECTIONS_REQUIRED" (requerido)
 * - taskId: ID de la tarea BPMN (opcional, para completar tarea)
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { projectId, reviewerName, reviewNotes, decision, taskId } = body;

    // Validaciones
    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "projectId is required" },
        { status: 400 }
      );
    }

    if (!reviewerName || !reviewNotes) {
      return NextResponse.json(
        { success: false, error: "reviewerName and reviewNotes are required" },
        { status: 400 }
      );
    }

    if (!decision || !["APPROVED", "CORRECTIONS_REQUIRED"].includes(decision)) {
      return NextResponse.json(
        { success: false, error: "decision must be APPROVED or CORRECTIONS_REQUIRED" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Simular procesamiento de revisión
      await new Promise((resolve) => setTimeout(resolve, 1500));

      return NextResponse.json({
        success: true,
        message: decision === "APPROVED"
          ? "QMS approved successfully"
          : "Corrections requested successfully",
        data: {
          projectId: parseInt(projectId),
          decision,
          reviewerName,
          reviewNotes,
          reviewedAt: new Date().toISOString(),
          // Si hay taskId, simular completar tarea BPMN
          taskCompleted: taskId ? true : false,
        },
      });
    }

    // Llamada real al backend
    // El backend debería tener un endpoint: POST /web/api/v1/compliance/qms/review
    const response = await fetch(`${GATEWAY_WEB_BASE}/compliance/qms/review`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        // TODO: Agregar headers de autenticación cuando esté disponible
      },
      body: JSON.stringify({
        projectId,
        reviewerName,
        reviewNotes,
        decision,
        taskId,
      }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json({
      success: true,
      message: decision === "APPROVED"
        ? "QMS approved successfully"
        : "Corrections requested successfully",
      data,
    });
  } catch (error) {
    console.error("Error in POST /api/governance/compliance/qms/review:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Error processing QMS review",
      },
      { status: 500 }
    );
  }
}



