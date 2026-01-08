import { NextResponse } from "next/server";
import type { ClassificationRequest } from "@/app/(app)/governance/data/mockClassification";

export async function POST(request: Request) {
  try {
    const body: ClassificationRequest = await request.json();
    const { projectId, category, subcategories, justification, prohibitedUseChecked } = body;

    // Validaciones básicas
    if (!projectId || !category || !subcategories || subcategories.length === 0 || !justification) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Missing required fields",
            details: []
          }
        },
        { status: 400 }
      );
    }

    // Validar formato de categoría (III.1 a III.8)
    if (!/^III\.[1-8]$/.test(category)) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Invalid category code",
            details: [{ field: "category", message: "Category must be III.1 to III.8" }]
          }
        },
        { status: 400 }
      );
    }

    // Validar justificación (mínimo 100 caracteres)
    if (justification.length < 100) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Justification must be at least 100 characters",
            details: [{
              field: "justification",
              message: "Size must be between 100 and 5000",
              rejectedValue: justification.length
            }]
          }
        },
        { status: 400 }
      );
    }

    // Validar Art. 5 - Sistemas Prohibidos
    if (prohibitedUseChecked === false || prohibitedUseChecked === undefined) {
      return NextResponse.json(
        {
          success: false,
          error: {
            code: "VALIDATION_ERROR",
            message: "Must verify that system is not prohibited under Art. 5",
            details: [{ field: "prohibitedUseChecked", message: "Must be true" }]
          }
        },
        { status: 400 }
      );
    }

    // Simular procesamiento
    await new Promise((resolve) => setTimeout(resolve, 1000));

    // Obtener nombre de categoría (en producción, desde base de datos o servicio)
    const categoryNames: Record<string, string> = {
      "III.1": "Biometría y categorización biométrica",
      "III.2": "Gestión y operación de infraestructuras críticas",
      "III.3": "Educación y formación profesional",
      "III.4": "Empleo, gestión de trabajadores y acceso al autoempleo",
      "III.5": "Acceso a servicios privados esenciales y a servicios y prestaciones públicos esenciales",
      "III.6": "Garantía del cumplimiento del Derecho",
      "III.7": "Migración, asilo y gestión del control fronterizo",
      "III.8": "Administración de justicia y procesos democráticos",
    };

    const categoryName = categoryNames[category] || category;

    // Simular workflow BPMN
    const workflowInstanceId = `wf-instance-${Date.now()}`;

    // Mock: Simular clasificación exitosa según especificación
    const result = {
      success: true,
      message: "Project classified successfully as high-risk system",
      data: {
        projectId,
        isHighRisk: true,
        category,
        categoryName,
        subcategories,
        classificationDate: new Date().toISOString(),
        classifiedBy: "current-user", // En producción, obtener del contexto de autenticación JWT
        workflowTriggered: true,
        workflowInstanceId,
        euRegistrationRequired: true, // Sistemas de alto riesgo requieren registro EU
        nextSteps: [
          "Complete EU Database registration (Art. 49)",
          "Prepare conformity assessment documentation",
          "Implement risk management system",
        ],
      }
    };

    return NextResponse.json(result);
  } catch (error) {
    console.error("Error in classification API:", error);
    return NextResponse.json(
      {
        success: false,
        error: {
          code: "INTERNAL_SERVER_ERROR",
          message: "Internal server error",
          timestamp: new Date().toISOString()
        }
      },
      { status: 500 }
    );
  }
}
