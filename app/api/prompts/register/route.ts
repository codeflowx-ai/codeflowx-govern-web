import { NextResponse } from "next/server";

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const {
      prmversion,
      prmdescription,
      prmcontent,
      prmparameters,
      prmchanges,
      prmstatus,
    } = body;

    // Validación básica
    if (!prmversion || !prmdescription || !prmcontent || !prmparameters) {
      return NextResponse.json(
        { error: "Campos requeridos faltantes" },
        { status: 400 }
      );
    }

    // TODO: Implementar lógica real de guardado en base de datos
    // Por ahora, simulamos el guardado exitoso
    const newPrompt = {
      idxpromptversion: Math.floor(Math.random() * 10000) + 1, // ID temporal
      prmversion,
      prmdescription,
      prmcontent,
      prmparameters,
      prmchanges: prmchanges || "",
      prmstatus: prmstatus || "DRAFT",
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    // Simular delay de guardado
    await new Promise((resolve) => setTimeout(resolve, 500));

    return NextResponse.json(
      {
        success: true,
        message: "Prompt registrado exitosamente",
        data: newPrompt,
      },
      { status: 201 }
    );
  } catch (error) {
    console.error("Error al registrar prompt:", error);
    return NextResponse.json(
      { error: "Error interno del servidor" },
      { status: 500 }
    );
  }
}
