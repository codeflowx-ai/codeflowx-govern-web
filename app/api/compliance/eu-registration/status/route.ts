import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

/**
 * GET /api/compliance/eu-registration/status
 *
 * Obtiene el listado de todos los registros UE con sus estados
 *
 * Backend: codeflowx-governance-eu-registration-service
 * Endpoint: GET /api/v1/eu-registrations/status?projectId={projectId}&status={status}&type={type}
 * Business Service: EuRegistrationService.findAll() con filtros
 */
export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");
    const status = searchParams.get("status");
    const type = searchParams.get("type");

    if (USE_MOCK) {
      // Mock: Return list of registrations
      const mockRegistrations = [
        {
          id: 1,
          projectId: 1001,
          projectName: "AI Credit Scoring System",
          registrationType: "STANDARD",
          status: "REGISTERED",
          submittedAt: "2025-12-01T10:00:00Z",
          euRegistrationId: "EU-REG-2025-001234",
          response: {
            success: true,
            registrationId: "EU-REG-2025-001234",
            registeredAt: "2025-12-01T10:05:00Z",
          },
        },
        {
          id: 2,
          projectId: 1002,
          projectName: "Facial Recognition System",
          registrationType: "SENSITIVE",
          status: "PENDING",
          submittedAt: null,
          euRegistrationId: null,
          response: null,
        },
        {
          id: 3,
          projectId: 1003,
          projectName: "HR Recruitment AI",
          registrationType: "STANDARD",
          status: "SUBMITTED",
          submittedAt: "2025-12-05T14:30:00Z",
          euRegistrationId: null,
          response: null,
        },
        {
          id: 4,
          projectId: 1004,
          projectName: "Medical Diagnosis Assistant",
          registrationType: "SENSITIVE",
          status: "REJECTED",
          submittedAt: "2025-11-20T09:15:00Z",
          euRegistrationId: null,
          response: {
            success: false,
            error: "Missing required documentation",
          },
        },
      ];

      // Aplicar filtros en mock
      let filtered = mockRegistrations;
      if (projectId) {
        filtered = filtered.filter((r) => r.projectId === parseInt(projectId));
      }
      if (status && status !== "ALL") {
        filtered = filtered.filter((r) => r.status === status);
      }
      if (type && type !== "ALL") {
        filtered = filtered.filter((r) => r.registrationType === type);
      }

      return NextResponse.json({
        success: true,
        data: filtered,
        total: filtered.length,
      });
    }

    // Construir query params para backend
    const queryParams = new URLSearchParams();
    if (projectId) queryParams.append("projectId", projectId);
    if (status && status !== "ALL") queryParams.append("status", status);
    if (type && type !== "ALL") queryParams.append("type", type);

    const queryString = queryParams.toString();
    const url = `${BFF_BASE_URL}/api/v1/eu-registrations/status${queryString ? `?${queryString}` : ""}`;

    // Llamada real al backend
    const response = await fetch(url, {
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
    console.error("Error fetching EU registration status:", error);
    return NextResponse.json(
      { success: false, error: "Failed to fetch registration status" },
      { status: 500 }
    );
  }
}



