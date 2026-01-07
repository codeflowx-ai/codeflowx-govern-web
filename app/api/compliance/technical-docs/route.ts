import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";
import { mockModelsSummary } from "@/app/(app)/governance/data/mockTechnicalDocs";

/**
 * GET /api/compliance/technical-docs
 *
 * Lista todos los modelos con resumen de documentación técnica
 *
 * Backend: codeflowx-governance-technical-docs-service
 * Endpoint: GET /api/v1/technical-docs
 */
export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const filterModel = searchParams.get("filterModel");
    const filterScore = searchParams.get("filterScore");
    const filterCompleteness = searchParams.get("filterCompleteness");

    if (USE_MOCK) {
      let filtered = [...mockModelsSummary];

    if (filterModel) {
      filtered = filtered.filter((m) =>
        m.modelName.toLowerCase().includes(filterModel.toLowerCase())
      );
    }

    if (filterScore && filterScore !== "all") {
      const scoreThreshold =
        filterScore === "high" ? 0.8 : filterScore === "medium" ? 0.5 : 0;
      filtered = filtered.filter((m) => {
        if (filterScore === "high") return m.overallScore >= 0.8;
        if (filterScore === "medium")
          return m.overallScore >= 0.5 && m.overallScore < 0.8;
        if (filterScore === "low") return m.overallScore < 0.5;
        return true;
      });
    }

    if (filterCompleteness && filterCompleteness !== "all") {
      if (filterCompleteness === "complete") {
        filtered = filtered.filter((m) => m.isComplete);
      } else if (filterCompleteness === "incomplete") {
        filtered = filtered.filter((m) => !m.isComplete);
      }
    }

      return NextResponse.json({
        success: true,
        data: filtered,
        total: filtered.length,
      });
    }

    // Llamada real al backend
    const queryParams = new URLSearchParams();
    if (filterModel) queryParams.append("filterModel", filterModel);
    if (filterScore) queryParams.append("filterScore", filterScore);
    if (filterCompleteness) queryParams.append("filterCompleteness", filterCompleteness);

    const url = `${BFF_BASE_URL}/api/v1/technical-docs${queryParams.toString() ? `?${queryParams.toString()}` : ""}`;
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
    console.error("Error fetching technical docs:", error);
    return NextResponse.json(
      { success: false, error: "Failed to fetch technical documentation" },
      { status: 500 }
    );
  }
}
