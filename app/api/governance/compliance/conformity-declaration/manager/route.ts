import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

const USE_MOCK_LOCAL = process.env.USE_MOCK === "true" || process.env.NODE_ENV === "development";

// Mock data para el manager
const mockManagerData = {
  projectId: 1,
  projectName: "Healthcare AI Project",
  assessments: [
    {
      idxcomplianceassessment: 1,
      assessmentname: "AI System - Healthcare Diagnostics",
      idxproject: 1,
      comassessmenttype: "SELF_ASSESSMENT",
      comreadyforcertification: true,
    },
    {
      idxcomplianceassessment: 2,
      assessmentname: "AI System - Financial Fraud Detection",
      idxproject: 1,
      comassessmenttype: "SELF_ASSESSMENT",
      comreadyforcertification: false,
    },
  ],
  declarations: [
    {
      idxDeclaration: 1,
      projectId: 1,
      projectName: "Healthcare AI Project",
      system_name: "Healthcare Diagnostics AI",
      provider_name: "TechCorp Medical",
      declaration_date: "2025-01-15",
      status: "SIGNED",
      version: "v1.0",
    },
    {
      idxDeclaration: 2,
      projectId: 1,
      projectName: "Healthcare AI Project",
      system_name: "Healthcare Diagnostics AI",
      provider_name: "TechCorp Medical",
      declaration_date: "2025-01-20",
      status: "DRAFT",
      version: "v1.1",
    },
  ],
  preview: null,
};

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const projectId = searchParams.get("projectId");

    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "projectId is required" },
        { status: 400 }
      );
    }

    if (USE_MOCK || USE_MOCK_LOCAL) {
      // Retornar mock data
      return NextResponse.json({
        success: true,
        data: {
          ...mockManagerData,
          projectId: parseInt(projectId),
        },
      });
    }

    // Llamada real al backend
    const response = await fetch(
      `${BFF_BASE_URL}/api/v1/conformity-declaration/manager?projectId=${projectId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const data = await response.json();

    return NextResponse.json({
      success: true,
      data,
    });
  } catch (error) {
    console.error("Error fetching conformity declaration manager data:", error);
    return NextResponse.json(
      {
        success: false,
        error: error instanceof Error ? error.message : "Failed to fetch manager data",
      },
      { status: 500 }
    );
  }
}
