import { NextResponse } from "next/server";
import { mockIncidents, type Incident } from "@/app/(app)/governance/data/mockPMM";

export async function GET(request: Request) {
  try {
    // Mock: Listar todos los incidentes
    // En producción, esto consultaría la base de datos con filtros
    const { searchParams } = new URL(request.url);
    const severity = searchParams.get("severity");
    const status = searchParams.get("status");
    const projectId = searchParams.get("projectId");
    const search = searchParams.get("search");

    let filtered = [...mockIncidents];

    if (severity && severity !== "all") {
      filtered = filtered.filter((incident) => incident.severity === severity);
    }

    if (status && status !== "all") {
      filtered = filtered.filter((incident) => incident.status === status);
    }

    if (projectId) {
      filtered = filtered.filter(
        (incident) => incident.projectId.toString() === projectId
      );
    }

    if (search) {
      filtered = filtered.filter(
        (incident) =>
          incident.description.toLowerCase().includes(search.toLowerCase()) ||
          incident.projectName.toLowerCase().includes(search.toLowerCase())
      );
    }

    return NextResponse.json({
      success: true,
      data: filtered,
      total: filtered.length,
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error fetching incidents" },
      { status: 500 }
    );
  }
}

export async function POST(request: Request) {
  try {
    // Mock: Crear un nuevo incidente
    // En producción, esto insertaría en la base de datos
    const body = await request.json();
    const { projectId, severity, description, impact } = body;

    // Validar datos requeridos
    if (!projectId || !severity || !description) {
      return NextResponse.json(
        { success: false, error: "Missing required fields" },
        { status: 400 }
      );
    }

    // Crear nuevo incidente
    const newIncident: Incident = {
      id: mockIncidents.length + 1,
      projectId: parseInt(projectId),
      projectName: `Project ${projectId}`, // En producción, se obtendría de la BD
      severity: severity as Incident["severity"],
      description,
      reportedAt: new Date().toISOString(),
      authorityNotified: severity === "HIGH" || severity === "CRITICAL",
      authorityNotifiedAt:
        severity === "HIGH" || severity === "CRITICAL"
          ? new Date().toISOString()
          : null,
      status: "OPEN",
      rootCauseAnalysis: null,
    };

    // En producción, se guardaría en la BD y se notificaría a autoridades si es necesario
    // if (newIncident.authorityNotified) {
    //   await notifyAuthority(newIncident);
    // }

    return NextResponse.json({
      success: true,
      data: newIncident,
      message:
        severity === "HIGH" || severity === "CRITICAL"
          ? "Incident created and authorities notified (Art. 20.1)"
          : "Incident created successfully",
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error creating incident" },
      { status: 500 }
    );
  }
}
