import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const search = searchParams.get("search") || "";
      const status = searchParams.get("status") || "";
      const type = searchParams.get("type") || "";
      const licenseType = searchParams.get("licenseType") || "";

      // Mock data
      const mockModels = [
        {
          id: 1,
          modelName: "GPT-4",
          modelId: "gpt-4",
          displayName: "GPT-4",
          description: "Large language model",
          version: "1.2.3",
          type: "TEXT",
          framework: "OpenAI",
          status: "ACTIVE",
        },
        {
          id: 2,
          modelName: "CodeFlowX-CodeAdapter",
          modelId: "codeflowx-code-adapter-v1",
          displayName: "CodeFlowX Code Adapter",
          description: "Modelo propio con adapter especializado para generación y análisis de código",
          version: "2.1.0",
          type: "TEXT",
          framework: "Custom",
          status: "ACTIVE",
        },
        {
          id: 3,
          modelName: "Mistral-Large",
          modelId: "mistral-large",
          displayName: "Mistral Large",
          description: "Modelo de asistencia avanzada con capacidades de razonamiento",
          version: "1.0.0",
          type: "TEXT",
          framework: "Mistral",
          status: "ACTIVE",
        },
        {
          id: 4,
          modelName: "DeepSeek-Coder",
          modelId: "deepseek-coder",
          displayName: "DeepSeek Coder",
          description: "Modelo especializado en programación y asistencia técnica",
          version: "1.5.2",
          type: "TEXT",
          framework: "DeepSeek",
          status: "ACTIVE",
        },
        {
          id: 5,
          modelName: "CodeFlowX-Asistente",
          modelId: "codeflowx-asistente-v2",
          displayName: "CodeFlowX Asistente",
          description: "Modelo propio adaptado para asistencia técnica y soporte",
          version: "3.0.1",
          type: "TEXT",
          framework: "Custom",
          status: "PENDING",
        },
        {
          id: 6,
          modelName: "Claude-3-Opus",
          modelId: "claude-3-opus",
          displayName: "Claude 3 Opus",
          description: "Modelo avanzado para tareas complejas",
          version: "3.0.0",
          type: "TEXT",
          framework: "Anthropic",
          status: "ACTIVE",
        },
        {
          id: 7,
          modelName: "CodeFlowX-Embeddings",
          modelId: "codeflowx-embeddings",
          displayName: "CodeFlowX Embeddings",
          description: "Modelo propio para generación de embeddings de código",
          version: "1.8.5",
          type: "EMBEDDING",
          framework: "Custom",
          status: "ACTIVE",
        },
        {
          id: 8,
          modelName: "Gemini-Pro",
          modelId: "gemini-pro",
          displayName: "Gemini Pro",
          description: "Modelo multiuso de Google",
          version: "1.0.0",
          type: "TEXT",
          framework: "Google",
          status: "INACTIVE",
        },
      ];

      let filteredModels = [...mockModels];

      // Aplicar filtros
      if (search) {
        const searchLower = search.toLowerCase();
        filteredModels = filteredModels.filter(
          (model) =>
            model.modelName.toLowerCase().includes(searchLower) ||
            model.displayName.toLowerCase().includes(searchLower) ||
            model.description?.toLowerCase().includes(searchLower)
        );
      }

      if (status && status !== "ALL") {
        filteredModels = filteredModels.filter((model) => model.status === status);
      }

      if (type && type !== "ALL") {
        filteredModels = filteredModels.filter((model) => model.type === type);
      }

      if (licenseType && licenseType !== "ALL") {
        // Mock: todos tienen licenseType por defecto
        filteredModels = filteredModels;
      }

      return NextResponse.json(filteredModels);
    } catch (error) {
      console.error("Error loading mock models:", error);
      return NextResponse.json(
        { error: "Error loading models" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(
    `${BFF_BASE_URL}/api/v1/bff/compliance/models${queryString ? `?${queryString}` : ""}`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al obtener modelos" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}

export async function POST(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const body = await request.json();
      const newModel = {
        id: Math.floor(Math.random() * 10000),
        ...body,
        createdAt: new Date().toISOString(),
      };
      return NextResponse.json(newModel, { status: 201 });
    } catch (error) {
      console.error("Error creating mock model:", error);
      return NextResponse.json(
        { error: "Error creating model" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const body = await request.json();
  const response = await fetch(`${BFF_BASE_URL}/api/v1/bff/compliance/models`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al crear modelo" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data, { status: 201 });
}
