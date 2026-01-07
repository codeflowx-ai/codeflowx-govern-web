import { NextResponse } from "next/server";

export async function GET() {
  // Mock data para desarrollo
  const mockData = {
    items: [
      {
        idxpromptversion: 1,
        prmversion: "1.0.0",
        prmdescription: "Versión inicial",
        prmcontent: "Contenido del prompt v1.0.0",
        prmparameters: "temperature=0.7, max_tokens=1000",
        prmchanges: "Creación inicial",
      },
      {
        idxpromptversion: 2,
        prmversion: "1.1.0",
        prmdescription: "Mejoras en claridad",
        prmcontent: "Contenido del prompt v1.1.0",
        prmparameters: "temperature=0.7, max_tokens=1200",
        prmchanges: "Mejoras en redacción",
      },
      {
        idxpromptversion: 3,
        prmversion: "1.2.0",
        prmdescription: "Optimización de tokens",
        prmcontent: "Contenido del prompt v1.2.0",
        prmparameters: "temperature=0.8, max_tokens=1500",
        prmchanges: "Optimización de parámetros",
      },
      {
        idxpromptversion: 4,
        prmversion: "2.0.0",
        prmdescription: "Refactorización completa",
        prmcontent: "Contenido del prompt v2.0.0",
        prmparameters: "temperature=0.75, max_tokens=2000",
        prmchanges: "Refactorización mayor",
      },
      {
        idxpromptversion: 5,
        prmversion: "2.1.0",
        prmdescription: "Corrección de bugs",
        prmcontent: "Contenido del prompt v2.1.0",
        prmparameters: "temperature=0.75, max_tokens=2000",
        prmchanges: "Corrección de errores menores",
      },
    ],
    metrics: {
      total: 5,
      active: 2,
      pending: 0,
      inactive: 3,
    },
    total: 5,
    page: 1,
    pageSize: 10,
  };

  return NextResponse.json(mockData);
}
