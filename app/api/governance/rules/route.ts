import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK } from "@/app/config/mock";

const BACKEND_BASE_URL_RULES = process.env.NEXT_PUBLIC_BACKEND_URL || 'http://localhost:8080';

// Mock data para desarrollo
const mockRules = [
  {
    id: 1,
    uuid: "550e8400-e29b-41d4-a716-446655440001",
    name: "Risk Keywords Validation",
    description: "Palabras clave de riesgo para validación de justificaciones",
    type: "VALIDATION",
    category: "CLASSIFICATION",
    active: true,
    content: `riesgo
alto riesgo
vulnerable
crítico
critical
risk
high risk
vulnerable
critical
impacto
afecta
afecta significativamente
derechos fundamentales`,
    priority: 10,
    version: "1.0.0",
    author: "system",
    createdAt: "2025-01-01T00:00:00Z",
    updatedAt: "2025-01-01T00:00:00Z",
    createdBy: "system",
  },
  {
    id: 2,
    uuid: "550e8400-e29b-41d4-a716-446655440002",
    name: "Justification Length Rule",
    description: "Regla de validación para longitud mínima de justificaciones",
    type: "VALIDATION",
    category: "CLASSIFICATION",
    active: true,
    content: "minLength: 100",
    priority: 5,
    version: "1.0.0",
    author: "system",
    createdAt: "2025-01-01T00:00:00Z",
    updatedAt: "2025-01-01T00:00:00Z",
    createdBy: "system",
  },
];

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    return NextResponse.json({
      success: true,
      data: mockRules,
    });
  }

  try {
    const response = await fetch(`${BACKEND_BASE_URL_RULES}/web/api/v1/governance/rules`, {
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
    console.error("Error fetching rules:", error);
    return NextResponse.json(
      {
        success: false,
        error: "Error al obtener las reglas",
      },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  if (USE_MOCK) {
    const body = await request.json();
    const newRule = {
      id: mockRules.length + 1,
      uuid: `550e8400-e29b-41d4-a716-44665544000${mockRules.length + 1}`,
      ...body,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      createdBy: "current-user",
    };
    mockRules.push(newRule);
    return NextResponse.json({
      success: true,
      data: newRule,
    });
  }

  try {
    const body = await request.json();
    const response = await fetch(`${BACKEND_BASE_URL_RULES}/web/api/v1/governance/rules`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      const error = await response.json();
      return NextResponse.json(
        {
          success: false,
          error: error.message || "Error al crear la regla",
        },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error("Error creating rule:", error);
    return NextResponse.json(
      {
        success: false,
        error: "Error al crear la regla",
      },
      { status: 500 }
    );
  }
}
