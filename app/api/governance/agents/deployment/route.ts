import { NextResponse } from "next/server";
import { NextRequest } from "next/server";

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Generar UUID para el nuevo deployment
    const newDeployment = {
      id: crypto.randomUUID(),
      uuid: crypto.randomUUID(),
      agentUuid: body.agentUuid,
      agentId: body.agentId,
      deploymentType: body.deploymentType,
      status: body.status || "PENDING",
      environment: body.environment,
      version: body.version || "1.0.0",
      configuration: body.configuration,
      notes: body.notes,
      agentUrl: body.agentUrl,
      deployedAt: new Date().toISOString(),
      deployedBy: "current-user@example.com",
      createdAt: new Date().toISOString().split("T")[0],
      lastModified: new Date().toISOString().split("T")[0],
    };

    // En producción, aquí se guardaría en la base de datos
    // Por ahora, retornamos el deployment creado
    return NextResponse.json(newDeployment, { status: 201 });
  } catch (error) {
    console.error("Error creating deployment:", error);
    return NextResponse.json(
      { message: "Error al crear el deployment" },
      { status: 500 }
    );
  }
}
