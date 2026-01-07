import { NextRequest, NextResponse } from "next/server";

// Mock data para políticas
const mockPolicies = [
  {
    idxpolicy: 1,
    policyname: "Política de Seguridad de Datos",
    policytype: "SECURITY",
    enforcementlevel: "MANDATORY",
    version: 1,
    status: "ACTIVE",
    totalviolations: 2,
    avgcompliancescore: 85.5,
  },
  {
    idxpolicy: 2,
    policyname: "Política de Cumplimiento GDPR",
    policytype: "COMPLIANCE",
    enforcementlevel: "MANDATORY",
    version: 2,
    status: "ACTIVE",
    totalviolations: 0,
    avgcompliancescore: 92.3,
  },
  {
    idxpolicy: 3,
    policyname: "Política de Ética en IA",
    policytype: "ETHICS",
    enforcementlevel: "RECOMMENDED",
    version: 1,
    status: "DRAFT",
    totalviolations: 5,
    avgcompliancescore: 78.2,
  },
  {
    idxpolicy: 4,
    policyname: "Política de Privacidad de Usuarios",
    policytype: "PRIVACY",
    enforcementlevel: "MANDATORY",
    version: 1,
    status: "ACTIVE",
    totalviolations: 1,
    avgcompliancescore: 88.7,
  },
  {
    idxpolicy: 5,
    policyname: "Política Operacional de Monitoreo",
    policytype: "OPERATIONAL",
    enforcementlevel: "RECOMMENDED",
    version: 1,
    status: "ACTIVE",
    totalviolations: 0,
    avgcompliancescore: 95.0,
  },
];

const mockMetrics = {
  totalPolicies: 5,
  activePolicies: 4,
  totalViolations: 8,
  complianceScore: 87.9,
};

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const page = parseInt(searchParams.get("page") || "1");
    const size = parseInt(searchParams.get("size") || "20");
    const search = searchParams.get("search") || "";
    const type = searchParams.get("type");
    const status = searchParams.get("status");
    const enforcement = searchParams.get("enforcement");

    // Filtrar políticas
    let filtered = [...mockPolicies];

    if (search) {
      filtered = filtered.filter((p) =>
        p.policyname.toLowerCase().includes(search.toLowerCase())
      );
    }

    if (type && type !== "ALL") {
      filtered = filtered.filter((p) => p.policytype === type);
    }

    if (status && status !== "ALL") {
      filtered = filtered.filter((p) => p.status === status);
    }

    if (enforcement && enforcement !== "ALL") {
      filtered = filtered.filter((p) => p.enforcementlevel === enforcement);
    }

    // Paginación
    const start = (page - 1) * size;
    const end = start + size;
    const paginated = filtered.slice(start, end);
    const totalPages = Math.ceil(filtered.length / size);

    return NextResponse.json({
      success: true,
      policies: paginated,
      metrics: mockMetrics,
      totalPages,
      currentPage: page,
      totalItems: filtered.length,
    });
  } catch (error) {
    console.error("Error in GET /api/governance/policies:", error);
    return NextResponse.json(
      { success: false, error: "Error loading policies" },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Simular creación de política
    const newPolicy = {
      idxpolicy: mockPolicies.length + 1,
      ...body,
      version: 1,
      status: body.status || "DRAFT",
      totalviolations: 0,
      avgcompliancescore: 0,
    };

    return NextResponse.json({
      success: true,
      policy: newPolicy,
    });
  } catch (error) {
    console.error("Error in POST /api/governance/policies:", error);
    return NextResponse.json(
      { success: false, error: "Error creating policy" },
      { status: 500 }
    );
  }
}
