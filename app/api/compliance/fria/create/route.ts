import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

/**
 * POST /api/compliance/fria/create
 *
 * Crea una nueva evaluación FRIA
 *
 * Backend: codeflowx-governance-fria-service
 * Endpoint: POST /api/v1/fria/create
 */
export async function POST(request: NextRequest) {
  try {
    const { projectId } = await request.json();

    if (USE_MOCK) {
      // Mock: Crear nueva evaluación FRIA
      const friaId = Math.floor(Math.random() * 10000) + 1;

      return NextResponse.json({
        success: true,
        friaId,
        status: "DRAFT",
      });
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/fria/create`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ projectId }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error creating FRIA:", error);
    return NextResponse.json(
      { success: false, error: "Error creating FRIA" },
      { status: 500 }
    );
  }
}
