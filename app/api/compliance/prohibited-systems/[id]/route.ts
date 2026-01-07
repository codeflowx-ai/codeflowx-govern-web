import { NextRequest, NextResponse } from "next/server";

export const dynamic = "force-dynamic";

// Mock data para detección
const mockDetection = {
  id: 1,
  project: {
    id: 5001,
    name: "AI Credit Scoring System",
    description: "System for evaluating creditworthiness using AI",
    status: "ACTIVE",
    models: [
      { id: 1, name: "Credit Score Model v2.1", type: "Classification" },
      { id: 2, name: "Risk Assessment Model", type: "Regression" },
    ],
  },
  detectedSystem: {
    id: 1,
    name: "Social Scoring by Public Authorities",
    category: "Art. 5.1.c",
    detectedAt: "2025-12-01T10:30:00Z",
    confidence: 0.95,
  },
  matchedKeywords: ["social scoring", "public authority", "citizen rating"],
  evidence:
    "Project description and model names contain keywords matching prohibited system patterns. The system appears to evaluate citizens' behavior for general purpose scoring.",
  blocked: true,
  falsePositive: false,
};

export async function GET(_request: NextRequest, { params }: { params: { id: string } }) {
  try {
    const id = params.id;

    const detection = {
      ...mockDetection,
      id: parseInt(id),
    };

    return NextResponse.json({
      success: true,
      detection,
    });
  } catch (error) {
    console.error("Error in GET /api/compliance/prohibited-systems/[id]:", error);
    return NextResponse.json(
      { success: false, error: "Error loading detection" },
      { status: 500 }
    );
  }
}

export async function PUT(request: NextRequest, { params }: { params: { id: string } }) {
  try {
    const id = params.id;
    const body = await request.json();
    const { action } = body;

    if (action === "block") {
      const detection = {
        ...mockDetection,
        id: parseInt(id),
        blocked: true,
      };

      return NextResponse.json({
        success: true,
        detection,
      });
    }

    if (action === "mark-false-positive") {
      const detection = {
        ...mockDetection,
        id: parseInt(id),
        falsePositive: true,
        blocked: false,
      };

      return NextResponse.json({
        success: true,
        detection,
      });
    }

    return NextResponse.json({ success: false, error: "Invalid action" }, { status: 400 });
  } catch (error) {
    console.error("Error in PUT /api/compliance/prohibited-systems/[id]:", error);
    return NextResponse.json(
      { success: false, error: "Error updating detection" },
      { status: 500 }
    );
  }
}
