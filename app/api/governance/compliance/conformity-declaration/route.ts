import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

const USE_MOCK_LOCAL = process.env.USE_MOCK === "true" || process.env.NODE_ENV === "development";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    if (USE_MOCK || USE_MOCK_LOCAL) {
      // Simular creación de declaración
      const newDeclaration = {
        idxDeclaration: Math.floor(Math.random() * 1000),
        assessmentId: body.assessmentId,
        providerName: body.providerName || "Provider Name",
        aiSystemName: body.aiSystemName || "AI System Name",
        status: "DRAFT",
        createdAt: new Date().toISOString(),
      };

      return NextResponse.json({
        success: true,
        data: newDeclaration,
      });
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/conformity-declaration`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();

    return NextResponse.json({
      success: true,
      data,
    });
  } catch (error) {
    console.error("Error creating conformity declaration:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Failed to create declaration",
      },
      { status: 500 }
    );
  }
}
