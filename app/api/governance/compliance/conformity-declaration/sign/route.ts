import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';

const USE_MOCK_LOCAL = process.env.USE_MOCK === "true" || process.env.NODE_ENV === "development";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    if (USE_MOCK || USE_MOCK_LOCAL) {
      // Simular firma de declaración
      const signedDeclaration = {
        idxDeclaration: body.declarationId,
        status: "SIGNED",
        signedBy: body.signedBy || "User",
        signatureDate: new Date().toISOString(),
      };

      return NextResponse.json({
        success: true,
        data: signedDeclaration,
      });
    }

    // Llamada real al backend
    const response = await fetch(`${GATEWAY_WEB_BASE}/compliance/conformity-declaration/sign`, {
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
    console.error("Error signing conformity declaration:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Failed to sign declaration",
      },
      { status: 500 }
    );
  }
}
