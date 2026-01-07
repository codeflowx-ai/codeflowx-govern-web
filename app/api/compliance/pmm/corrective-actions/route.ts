import { NextResponse } from "next/server";
import {
  mockCorrectiveActions,
  type CorrectiveAction,
} from "@/app/(app)/governance/data/mockPMM";
import { mockIncidents } from "@/app/(app)/governance/data/mockPMM";

export async function GET(request: Request) {
  try {
    // Mock: Listar todas las acciones correctoras
    // En producción, esto consultaría la base de datos con filtros
    const { searchParams } = new URL(request.url);
    const status = searchParams.get("status");
    const incidentId = searchParams.get("incidentId");
    const search = searchParams.get("search");

    let filtered = [...mockCorrectiveActions];

    if (status && status !== "all") {
      filtered = filtered.filter((action) => action.status === status);
    }

    if (incidentId) {
      filtered = filtered.filter(
        (action) => action.incidentId.toString() === incidentId
      );
    }

    if (search) {
      filtered = filtered.filter(
        (action) =>
          action.description.toLowerCase().includes(search.toLowerCase()) ||
          action.incidentDescription.toLowerCase().includes(search.toLowerCase())
      );
    }

    return NextResponse.json({
      success: true,
      data: filtered,
      total: filtered.length,
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error fetching corrective actions" },
      { status: 500 }
    );
  }
}

export async function POST(request: Request) {
  try {
    // Mock: Crear una nueva acción correctora
    // En producción, esto insertaría en la base de datos
    const body = await request.json();
    const { incidentId, description, plannedDate } = body;

    // Validar datos requeridos
    if (!incidentId || !description || !plannedDate) {
      return NextResponse.json(
        { success: false, error: "Missing required fields" },
        { status: 400 }
      );
    }

    // Buscar el incidente asociado
    const incident = mockIncidents.find(
      (i) => i.id.toString() === incidentId.toString()
    );

    if (!incident) {
      return NextResponse.json(
        { success: false, error: "Incident not found" },
        { status: 404 }
      );
    }

    // Crear nueva acción correctora
    const newAction: CorrectiveAction = {
      id: mockCorrectiveActions.length + 1,
      incidentId: parseInt(incidentId),
      incidentDescription: incident.description,
      description,
      status: "PLANNED",
      effectiveness: null,
      plannedDate,
      completedDate: null,
    };

    // En producción, se guardaría en la BD
    // await correctiveActionService.create(newAction);

    return NextResponse.json({
      success: true,
      data: newAction,
      message: "Corrective action created successfully",
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error creating corrective action" },
      { status: 500 }
    );
  }
}

export async function PATCH(request: Request) {
  try {
    // Mock: Actualizar una acción correctora
    // En producción, esto actualizaría en la base de datos
    const body = await request.json();
    const { id, status, effectiveness } = body;

    if (!id) {
      return NextResponse.json(
        { success: false, error: "Action ID is required" },
        { status: 400 }
      );
    }

    // Buscar la acción
    const actionIndex = mockCorrectiveActions.findIndex((a) => a.id === id);

    if (actionIndex === -1) {
      return NextResponse.json(
        { success: false, error: "Action not found" },
        { status: 404 }
      );
    }

    // Actualizar la acción
    const updatedAction: CorrectiveAction = {
      ...mockCorrectiveActions[actionIndex],
      ...(status && { status: status as CorrectiveAction["status"] }),
      ...(effectiveness !== undefined && { effectiveness }),
      ...(status === "COMPLETED" && {
        completedDate: new Date().toISOString().split("T")[0],
      }),
    };

    // En producción, se actualizaría en la BD
    // await correctiveActionService.update(id, updatedAction);

    return NextResponse.json({
      success: true,
      data: updatedAction,
      message: "Corrective action updated successfully",
    });
  } catch (error) {
    return NextResponse.json(
      { success: false, error: "Error updating corrective action" },
      { status: 500 }
    );
  }
}
