import { NextResponse } from "next/server";

export async function GET() {
  // Mock data para desarrollo
  const mockData = {
    items: [
      {
        idxpromptvalidation: 1,
        prmvalidationtype: "ACTIVE",
        prmvalidationresult: "PASSED",
        prmvalidationscore: 9.5,
        prmvalidationdetails: "Validación exitosa",
        prmissuesfound: 0,
      },
      {
        idxpromptvalidation: 2,
        prmvalidationtype: "INACTIVE",
        prmvalidationresult: "FAILED",
        prmvalidationscore: 5.2,
        prmvalidationdetails: "Errores encontrados",
        prmissuesfound: 3,
      },
      {
        idxpromptvalidation: 3,
        prmvalidationtype: "ACTIVE",
        prmvalidationresult: "PASSED",
        prmvalidationscore: 8.8,
        prmvalidationdetails: "Validación correcta",
        prmissuesfound: 0,
      },
      {
        idxpromptvalidation: 4,
        prmvalidationtype: "PENDING",
        prmvalidationresult: "PENDING",
        prmvalidationscore: 0,
        prmvalidationdetails: "En proceso",
        prmissuesfound: 0,
      },
      {
        idxpromptvalidation: 5,
        prmvalidationtype: "ACTIVE",
        prmvalidationresult: "PASSED",
        prmvalidationscore: 9.8,
        prmvalidationdetails: "Excelente validación",
        prmissuesfound: 0,
      },
    ],
    metrics: {
      total: 5,
      active: 3,
      pending: 1,
      inactive: 1,
    },
    total: 5,
    page: 1,
    pageSize: 10,
  };

  return NextResponse.json(mockData);
}
