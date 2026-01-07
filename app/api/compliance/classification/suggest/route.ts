import { NextResponse } from "next/server";
import { mockAISuggestion } from "@/app/(app)/governance/data/mockClassification";

export async function POST(request: Request) {
  try {
    const { projectId } = await request.json();

    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "Project ID is required" },
        { status: 400 }
      );
    }

    // Simular procesamiento con IA (delay para simular análisis)
    await new Promise((resolve) => setTimeout(resolve, 2000));

    // Mock: Retornar sugerencia predefinida
    // En producción, aquí se haría una llamada real a un servicio de IA
    // que analizaría la descripción del proyecto y sugeriría categorías

    const suggestion = {
      ...mockAISuggestion,
      projectId,
      generatedAt: new Date().toISOString(),
    };

    return NextResponse.json({
      success: true,
      suggestion,
    });
  } catch (error) {
    console.error("Error in AI suggestion API:", error);
    return NextResponse.json(
      { success: false, error: "Internal server error" },
      { status: 500 }
    );
  }
}
