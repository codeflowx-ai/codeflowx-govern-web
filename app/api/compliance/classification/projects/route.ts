import { NextRequest, NextResponse } from "next/server";
import { mockClassifiedProjects } from "@/app/(app)/governance/data/mockClassification";

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const category = searchParams.get("category");
    const status = searchParams.get("status");
    const search = searchParams.get("search");

    let filteredProjects = [...mockClassifiedProjects];

    // Filtrar por categoría
    if (category && category !== "all") {
      filteredProjects = filteredProjects.filter((p) => p.category === category);
    }

    // Filtrar por estado
    if (status && status !== "all") {
      filteredProjects = filteredProjects.filter((p) => p.status === status);
    }

    // Filtrar por búsqueda
    if (search) {
      const query = search.toLowerCase();
      filteredProjects = filteredProjects.filter(
        (p) =>
          p.name.toLowerCase().includes(query) ||
          p.description.toLowerCase().includes(query) ||
          p.category.toLowerCase().includes(query) ||
          p.classifiedBy.toLowerCase().includes(query)
      );
    }

    return NextResponse.json({
      success: true,
      projects: filteredProjects,
      total: filteredProjects.length,
    });
  } catch (error) {
    console.error("Error fetching classified projects:", error);
    return NextResponse.json(
      { success: false, error: "Error fetching classified projects" },
      { status: 500 }
    );
  }
}

