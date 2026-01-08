import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const searchParams = request.nextUrl.searchParams;
      const search = searchParams.get("search") || "";
      const type = searchParams.get("type") || "";
      const score = searchParams.get("score") || "";

      // Mock data
      const mockAnalyses = [
        {
          id: 1,
          modelId: 1,
          modelName: "GPT-4 Fine-tuned Classification",
          modelType: "TEXT",
          explainabilityScore: 75,
          isExplainable: true,
          explainabilityMethod: "SHAP",
          featureImportance: {
            feature_1: 0.25,
            feature_2: 0.18,
            feature_3: 0.15,
          },
          analysisDate: "2024-01-15T10:30:00",
          analyst: "analyst@example.com",
          explanationQuality: "GOOD",
          limitations: "Algunas características tienen baja importancia pero son críticas en casos edge",
          recommendations: "Mejorar documentación de características y agregar ejemplos de explicación",
          status: "REVIEWED",
        },
        {
          id: 2,
          modelId: 2,
          modelName: "Neural Network Credit Scoring",
          modelType: "TEXT",
          explainabilityScore: 45,
          isExplainable: false,
          explainabilityMethod: "LIME",
          analysisDate: "2024-01-14T14:20:00",
          analyst: "analyst@example.com",
          explanationQuality: "POOR",
          limitations: "Modelo de caja negra con baja interpretabilidad",
          recommendations: "Considerar modelos más interpretables o usar técnicas de post-hoc explicabilidad avanzadas",
          status: "PENDING",
        },
        {
          id: 3,
          modelId: 3,
          modelName: "Decision Tree Classifier",
          modelType: "TEXT",
          explainabilityScore: 95,
          isExplainable: true,
          explainabilityMethod: "Tree-based",
          featureImportance: {
            age: 0.35,
            income: 0.28,
            education: 0.20,
          },
          analysisDate: "2024-01-13T09:15:00",
          analyst: "analyst@example.com",
          explanationQuality: "EXCELLENT",
          recommendations: "Modelo altamente explicable, mantener estructura",
          status: "APPROVED",
        },
        {
          id: 4,
          modelId: 4,
          modelName: "ResNet Image Classification",
          modelType: "IMAGE",
          explainabilityScore: 60,
          isExplainable: true,
          explainabilityMethod: "Integrated Gradients",
          analysisDate: "2024-01-12T16:45:00",
          analyst: "analyst@example.com",
          explanationQuality: "FAIR",
          limitations: "Explicaciones a nivel de píxel pueden ser difíciles de interpretar",
          recommendations: "Agregar visualizaciones de heatmaps y documentación de interpretación",
          status: "REVIEWED",
        },
        {
          id: 5,
          modelId: 5,
          modelName: "BERT Sentiment Analysis",
          modelType: "TEXT",
          explainabilityScore: 70,
          isExplainable: true,
          explainabilityMethod: "Attention Weights",
          featureImportance: {
            tokens_positive: 0.22,
            tokens_negative: 0.19,
            context: 0.16,
          },
          analysisDate: "2024-01-11T11:30:00",
          analyst: "analyst@example.com",
          explanationQuality: "GOOD",
          recommendations: "Mejorar visualización de attention weights para usuarios finales",
          status: "APPROVED",
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

      if (score && score !== "ALL") {
        // Filtrar por rango de score
        const scoreNum = parseInt(score);
        filteredAnalyses = filteredAnalyses.filter(
          (analysis) => analysis.explainabilityScore >= scoreNum
        );
      }

      return NextResponse.json(filteredAnalyses);
    } catch (error) {
      console.error("Error loading mock explainability analyses:", error);
      return NextResponse.json(
        { error: "Error loading explainability analyses" },
        { status: 500 }
      );
    }
  }

  // Si no está en modo mock, llamar al backend real
  const searchParams = request.nextUrl.searchParams;
  const queryString = searchParams.toString();

  const response = await fetch(
    `${BFF_BASE_URL}/api/v1/bff/compliance/models/explainability${queryString ? `?${queryString}` : ""}`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    return NextResponse.json(
      { error: "Error al obtener análisis de explicabilidad" },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
