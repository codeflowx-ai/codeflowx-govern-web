import { NextResponse } from "next/server";
import { NextRequest } from "next/server";
import { USE_MOCK } from "@/app/config/mock";

const GATEWAY_WEB_BASE = process.env.GATEWAY_WEB_BASE || 'http://localhost:8080';

// Mock data para agents
const mockAgents = [
  {
    id: 1,
    uuid: "550e8400-e29b-41d4-a716-446655440000",
    name: "Credit Scoring Agent",
    description: "Agent for credit risk assessment using machine learning models",
    domain: "Finance",
    version: "v2.1.0",
    status: "ACTIVE",
    createdAt: "2024-01-10",
    lastModified: "2024-01-15",
  },
  {
    id: 2,
    uuid: "550e8400-e29b-41d4-a716-446655440001",
    name: "Fraud Detection Agent",
    description: "Real-time fraud detection agent for transaction monitoring",
    domain: "Security",
    version: "v1.5.2",
    status: "ACTIVE",
    createdAt: "2024-01-08",
    lastModified: "2024-01-14",
  },
  {
    id: 3,
    uuid: "550e8400-e29b-41d4-a716-446655440002",
    name: "Customer Service Agent",
    description: "AI-powered customer service agent for handling inquiries",
    domain: "Customer Service",
    version: "v3.0.0",
    status: "ACTIVE",
    createdAt: "2024-01-12",
    lastModified: "2024-01-16",
  },
];

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const search = searchParams.get("search");
      const status = searchParams.get("status");

      let filteredAgents = [...mockAgents];

      // Filtrar por búsqueda
      if (search) {
        const searchLower = search.toLowerCase();
        filteredAgents = filteredAgents.filter(
          (agent) =>
            agent.name.toLowerCase().includes(searchLower) ||
            (agent.description && agent.description.toLowerCase().includes(searchLower))
        );
      }

      // Filtrar por estado
      if (status && status !== "all") {
        filteredAgents = filteredAgents.filter((agent) => agent.status === status);
      }

      return NextResponse.json({
        items: filteredAgents,
        total: filteredAgents.length,
      });
    } catch (error) {
      console.error("Error returning mock agents:", error);
      return NextResponse.json({ items: mockAgents, total: mockAgents.length });
    }
  }

  try {
    const response = await fetch(`${GATEWAY_WEB_BASE}/governance/agents/registry`, {
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
    console.error("Error fetching agents:", error);
    // Fallback a mocks si falla la conexión
    return NextResponse.json({ items: mockAgents, total: mockAgents.length });
  }
}

export async function POST(request: NextRequest) {
  let body: any;
  try {
    body = await request.json();

    // Llamar al BFF real
    const response = await fetch(`${GATEWAY_WEB_BASE}/governance/agents/registry`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({ message: "Error al crear el agente" }));
      return NextResponse.json(error, { status: response.status });
    }

    const data = await response.json();
    return NextResponse.json(data, { status: 201 });
  } catch (error) {
    console.error("Error creating agent:", error);
    // Fallback a mock data si el BFF no está disponible
    if (!body) {
      body = {};
    }
    const newAgent = {
      id: Date.now(),
      uuid: crypto.randomUUID(),
      name: body.name || "",
      description: body.description || "",
      domain: body.domain || "",
      version: body.version || "1.0.0",
      status: body.status || "DRAFT",
      createdAt: new Date().toISOString().split("T")[0],
      lastModified: new Date().toISOString().split("T")[0],
    };
    return NextResponse.json(newAgent, { status: 201 });
  }
}
