import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

/**
 * POST /api/compliance/eu-registration
 *
 * Guarda o actualiza una sección del formulario de registro UE
 *
 * Backend: codeflowx-governance-eu-registration-service
 * Endpoint: POST /api/v1/eu-registrations/section
 * Business Service: EuRegistrationBusinessService.updateSubmissionData()
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { projectId, section, data, registrationId } = body;

    if (!projectId || !section || !data) {
      return NextResponse.json(
        { success: false, error: "Missing required fields: projectId, section, data" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Mock: Simular guardado de sección
      return NextResponse.json({
        success: true,
        registrationId: registrationId || 2001,
        section,
        status: "DRAFT",
        message: `Section ${section} saved successfully`,
      });
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/eu-registrations/section`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        projectId,
        registrationId,
        section,
        data,
      }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error saving EU registration section:", error);
    return NextResponse.json(
      { success: false, error: "Failed to save section" },
      { status: 500 }
    );
  }
}

/**
 * GET /api/compliance/eu-registration
 *
 * Obtiene los datos de registro UE para un proyecto
 *
 * Backend: codeflowx-governance-eu-registration-service
 * Endpoint: GET /api/v1/eu-registrations?projectId={projectId}
 * Business Service: EuRegistrationBusinessService.getRegistrationByProjectId()
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");

    if (!projectId) {
      return NextResponse.json(
        { success: false, error: "Missing required parameter: projectId" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Mock: Return registration data
      return NextResponse.json({
        success: true,
        data: {
          registrationId: 2001,
          projectId: parseInt(projectId),
          projectName: "AI Credit Scoring System",
          status: "DRAFT",
          registrationType: "STANDARD",
          sectionA: {
            providerName: "ACME Corporation",
            providerAddress: "123 Main St, Madrid, Spain",
            providerCountry: "ES",
            taxId: "B12345678",
            contactEmail: "contact@acme.com",
            contactPhone: "+34 123 456 789",
            website: "https://www.acme.com",
            legalRepresentative: "John Doe",
            registrationNumber: "REG-12345",
            vatNumber: "ESB12345678",
            establishmentDate: "2020-01-15",
            mainActivity: "Financial Services",
          },
          sectionB: {
            systemName: "AI Credit Scoring System",
            intendedPurpose: "Automated credit scoring for loan applications",
            highRiskCategory: "A3_4",
            technicalDescription: "Machine learning model for credit risk assessment using gradient boosting algorithms",
            deploymentDate: "2024-01-01",
            systemVersion: "2.1.0",
            aiTechniques: "Gradient Boosting, Neural Networks",
            trainingData: "Historical loan data from 2015-2023",
            performanceMetrics: "Accuracy: 87%, AUC: 0.92",
          },
          sectionC: {
            conformityBasis: "Internal control procedure",
            notifiedBody: null,
            certificateId: null,
            certificateDate: null,
            conformityAssessment: "Self-assessment completed",
            additionalInfo: "System complies with GDPR and EU AI Act requirements",
          },
        },
      });
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/eu-registrations?projectId=${projectId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error fetching EU registration:", error);
    return NextResponse.json(
      { success: false, error: "Failed to fetch registration" },
      { status: 500 }
    );
  }
}
