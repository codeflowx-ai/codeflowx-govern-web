import { NextResponse } from "next/server";
import { NextRequest } from "next/server";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Generar UUID e ID para la nueva aprobación
    const newApproval = {
      id: Date.now(), // ID temporal basado en timestamp
      uuid: crypto.randomUUID(),
      agentUuid: body.agentUuid,
      approvalType: body.approvalType,
      approvalStatus: body.approvalStatus || "PENDING",
      requestReason: body.requestReason,
      requestDetails: body.requestDetails,
      approvalCriteria: body.approvalCriteria,
      createdAt: new Date().toISOString().split("T")[0],
      lastModified: new Date().toISOString().split("T")[0],
    };

    // En producción, aquí se guardaría en la base de datos
    // Por ahora, retornamos la aprobación creada
    return NextResponse.json(newApproval, { status: 201 });
  } catch (error) {
    console.error("Error creating approval:", error);
    return NextResponse.json(
      { message: "Error al crear la aprobación" },
      { status: 500 }
    );
  }
}
