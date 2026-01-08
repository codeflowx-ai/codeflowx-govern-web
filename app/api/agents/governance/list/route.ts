import { NextResponse } from "next/server";
import { USE_MOCK } from "@/app/config/mock";

export async function GET() {
  if (USE_MOCK) {
    const mockData = {
      items: [
        {
          id: 1,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          policyType: "SECURITY",
          policyName: "Política de Seguridad de Datos Sensibles",
          status: "ACTIVE",
          enforcementLevel: "MANDATORY",
          priority: "HIGH",
          effectiveFrom: "2024-01-15",
          effectiveUntil: "2025-01-15"
        },
        {
          id: 2,
          agentUuid: "550e8400-e29b-41d4-a716-446655440001",
          agentName: "Fraud Detection Agent",
          policyType: "DATA_PRIVACY",
          policyName: "Política de Privacidad de Información Personal",
          status: "ACTIVE",
          enforcementLevel: "MANDATORY",
          priority: "HIGH",
          effectiveFrom: "2024-01-10",
          effectiveUntil: undefined
        },
        {
          id: 3,
          agentUuid: "550e8400-e29b-41d4-a716-446655440000",
          agentName: "Credit Scoring Agent",
          policyType: "ETHICAL",
          policyName: "Política Ética de Decisiones Crediticias",
          status: "DRAFT",
          enforcementLevel: "RECOMMENDED",
          priority: "MEDIUM",
          effectiveFrom: "2024-02-01",
          effectiveUntil: undefined
        },
        {
          id: 4,
          agentUuid: "550e8400-e29b-41d4-a716-446655440002",
          agentName: "Customer Service Agent",
          policyType: "OPERATIONAL",
          policyName: "Política Operacional de Tiempos de Respuesta",
          status: "ACTIVE",
          enforcementLevel: "RECOMMENDED",
          priority: "MEDIUM",
          effectiveFrom: "2024-01-20",
          effectiveUntil: undefined
        },
      ],
      total: 4,
      active: 3,
      pending: 1,
    };

    return NextResponse.json(mockData);
  }

  // Si no está en modo mock, llamar al backend real
  const bffUrl = process.env.BFF_BASE_URL || "http://localhost:8084";
  try {
    const response = await fetch(`${bffUrl}/api/v1/agents/governance/list`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`Backend responded with status ${response.status}`);
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error fetching governance list:", error);
    // Fallback a mocks si falla la conexión
    return NextResponse.json({
      items: [],
      total: 0,
      active: 0,
      pending: 0,
    });
  }
}
