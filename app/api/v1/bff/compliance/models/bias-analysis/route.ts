import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const search = searchParams.get("search") || "";
      const type = searchParams.get("type") || "";
      const biasLevel = searchParams.get("biasLevel") || "";

      // Mock data
      const mockAnalyses = [
        {
          id: 1,
          modelId: 1,
          modelName: "GPT-4 Fine-tuned Classification",
          modelType: "TEXT",
          biasScore: 45,
          detectedBias: true,
          biasLevel: "HIGH",
          affectedGroups: ["Género", "Edad"],
          analysisDate: "2024-01-15T10:30:00",
          analyst: "analyst@example.com",
          analysisMethod: "Statistical Parity",
          recommendations: "Ajustar dataset de entrenamiento para incluir más representación balanceada",
          status: "REVIEWED",
        },
        {
          id: 2,
          modelId: 2,
          modelName: "Claude Vision OCR",
          modelType: "MULTIMODAL",
          biasScore: 85,
          detectedBias: false,
          biasLevel: "NONE",
          affectedGroups: [],
          analysisDate: "2024-01-14T14:20:00",
          analyst: "analyst@example.com",
          analysisMethod: "Equalized Odds",
          status: "RESOLVED",
        },
        {
          id: 3,
          modelId: 3,
          modelName: "Resume Screening Model",
          modelType: "TEXT",
          biasScore: 60,
          detectedBias: true,
          biasLevel: "MEDIUM",
          affectedGroups: ["Género", "Etnia"],
          analysisDate: "2024-01-13T09:15:00",
          analyst: "analyst@example.com",
          analysisMethod: "Demographic Parity",
          recommendations: "Implementar técnicas de debiasing y re-entrenar modelo",
          status: "PENDING",
        },
        {
          id: 4,
          modelId: 4,
          modelName: "Credit Scoring Model",
          modelType: "TEXT",
          biasScore: 35,
          detectedBias: true,
          biasLevel: "HIGH",
          affectedGroups: ["Etnia", "Ubicación geográfica"],
          analysisDate: "2024-01-12T16:45:00",
          analyst: "analyst@example.com",
          analysisMethod: "Calibrated Fairness",
          recommendations: "Revisar variables de entrada y eliminar proxies de características protegidas",
          status: "REVIEWED",
        },
        {
          id: 5,
          modelId: 5,
          modelName: "Image Classification Model",
          modelType: "IMAGE",
          biasScore: 92,
          detectedBias: false,
          biasLevel: "NONE",
          affectedGroups: [],
          analysisDate: "2024-01-11T11:30:00",
          analyst: "analyst@example.com",
          analysisMethod: "Fairness Through Awareness",
          status: "RESOLVED",
        },
      ];

      let filteredAnalyses = [...mockAnalyses];

      // Aplicar filtros
      if (search) {
        const searchLower = search.toLowerCase();
        filteredAnalyses = filteredAnalyses.filter(
          (analysis) =>
            analysis.modelName.toLowerCase().includes(searchLower) ||
            analysis.analyst.toLowerCase().includes(searchLower)
        );
      }

      if (type && type !== "ALL") {
        filteredAnalyses = filteredAnalyses.filter(
          (analysis) => analysis.modelType === type
        );
      }

      if (biasLevel && biasLevel !== "ALL") {
        filteredAnalyses = filteredAnalyses.filter(
          (analysis) => analysis.biasLevel === biasLevel
        );
      }

      return NextResponse.json(filteredAnalyses);
    } catch (error) {
      console.error("Error loading mock bias analyses:", error);
      return NextResponse.json(
        { error: "Error loading bias analyses" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(
    `${GATEWAY_WEB_BASE}/api/v1/bff/compliance/models/bias-analysis${queryString ? `?${queryString}` : ""}`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al obtener análisis de sesgo" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
