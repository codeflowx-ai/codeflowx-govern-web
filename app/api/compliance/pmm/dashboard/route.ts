import { NextResponse } from "next/server";
import { mockPMMData } from "@/app/(app)/governance/data/mockPMM";

export async function GET(request: Request) {
  try {
    // Mock: Obtener datos del dashboard de Post-Market Monitoring
    // En producción, esto consultaría la base de datos
    const { searchParams } = new URL(request.url);
    const projectId = searchParams.get("projectId");

    let data = mockPMMData;

    // Filtrar por proyecto si se especifica
    if (projectId) {
      data = {
        ...data,
        systems: data.systems.filter((s) => s.projectId.toString() === projectId),
      };
    }

    return NextResponse.json({
      success: true,
      data: data,
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error fetching PMM dashboard data" },
      { status: 500 }
    );
  }
}
