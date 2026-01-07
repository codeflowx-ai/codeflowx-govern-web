import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

/**
 * POST /api/compliance/eu-registration/submit
 *
 * Envía el registro a la Base de Datos UE según Art. 49 y Anexo VIII
 *
 * Backend: codeflowx-governance-eu-registration-service
 * Endpoint: POST /api/v1/eu-registrations/{registrationId}/submit
 * Business Service: EuRegistrationBusinessService.submitToEuDatabase()
 *
 * Nota: El método submitToEuDatabase() genera el payload JSON según Anexo VIII
 * y llama al microservicio Python para registro en EU Database (actualmente comentado)
 */
export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { registrationId } = body;

    if (!registrationId) {
      return NextResponse.json(
        { success: false, error: "Missing required field: registrationId" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {
      // Mock: Simulate submission to EU Database
      await new Promise((resolve) => setTimeout(resolve, 1500)); // Simulate API delay

      // Mock: Generate EU Registration ID
      const euRegistrationId = `EU-REG-2025-${String(Math.floor(Math.random() * 1000000)).padStart(6, "0")}`;

      return NextResponse.json({
        success: true,
        registrationId,
        status: "SUBMITTED",
        euRegistrationId,
        submittedAt: new Date().toISOString(),
        message: "Registration submitted successfully to EU Database",
      });
    }

    // Llamada real al backend
    const response = await fetch(`${BFF_BASE_URL}/api/v1/eu-registrations/${registrationId}/submit`, {
      method: "POST",
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
    console.error("Error submitting EU registration:", error);
    return NextResponse.json(
      {
        success: false,
        error: "Failed to submit registration to EU Database",
        status: "ERROR",
      },
      { status: 500 }
    );
  }
}
