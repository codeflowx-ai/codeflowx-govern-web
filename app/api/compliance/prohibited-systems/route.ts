import { NextRequest, NextResponse } from "next/server";

// Mock data para proyectos verificados
const mockProjects = [
  {
    id: 1,
    name: "AI Credit Scoring System",
    status: "PROHIBITED",
    detectedSystems: [
      {
        id: 1,
        name: "Social Scoring by Public Authorities",
        category: "Art. 5.1.c",
        detectedAt: "2025-12-01T10:30:00Z",
        confidence: 0.95,
      },
    ],
    blocked: true,
  },
  {
    id: 2,
    name: "Facial Recognition System",
    status: "WARNING",
    detectedSystems: [
      {
        id: 2,
        name: "Real-time Remote Biometric Identification",
        category: "Art. 5.1.d",
        detectedAt: "2025-12-01T11:00:00Z",
        confidence: 0.75,
      },
    ],
    blocked: false,
  },
  {
    id: 3,
    name: "Customer Analytics Platform",
    status: "CLEAN",
    detectedSystems: [],
    blocked: false,
  },
];

// Mock data para catálogo
const mockCatalog = [
  {
    id: 1,
    name: "Social Scoring by Public Authorities",
    category: "ART_5_1_C",
    description: "AI systems for social scoring by public authorities",
    keywords: ["social scoring", "public authority", "citizen rating", "behavioral scoring"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 2,
    name: "Real-time Remote Biometric Identification",
    category: "ART_5_1_D",
    description: "Real-time remote biometric identification in publicly accessible spaces",
    keywords: ["biometric", "facial recognition", "real-time", "public space", "surveillance"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 3,
    name: "Subliminal Manipulation",
    category: "ART_5_1_A",
    description: "AI systems using subliminal techniques to distort behavior",
    keywords: ["subliminal", "manipulation", "behavioral distortion"],
    active: true,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
  {
    id: 4,
    name: "Vulnerability Exploitation",
    category: "ART_5_1_B",
    description: "AI systems exploiting vulnerabilities of specific groups",
    keywords: ["vulnerability", "exploitation", "targeted groups"],
    active: false,
    createdAt: "2025-01-15T00:00:00Z",
    updatedAt: "2025-12-01T00:00:00Z",
  },
];

export async function GET(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const type = searchParams.get("type") || "projects";

    if (type === "catalog") {
      const active = searchParams.get("active");
      let catalog = [...mockCatalog];

      if (active === "true") {
        catalog = catalog.filter((item) => item.active);
      }

      return NextResponse.json({
        success: true,
        catalog,
      });
    }

    // Retornar proyectos verificados
    return NextResponse.json({
      success: true,
      projects: mockProjects,
      metrics: {
        activeProhibited: mockProjects.filter((p) => p.status === "PROHIBITED").length,
        blockedDeployments: mockProjects.filter((p) => p.blocked).length,
        totalDetections: mockProjects.filter((p) => p.detectedSystems.length > 0).length,
      },
    });
  } catch (error) {
    console.error("Error in GET /api/compliance/prohibited-systems:", error);
    return NextResponse.json(
      { success: false, error: "Error loading prohibited systems data" },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { action, projectId } = body;

    if (action === "verify") {
      // Simular verificación de proyecto
      const project = mockProjects.find((p) => p.id === projectId);
      if (!project) {
        return NextResponse.json(
          { success: false, error: "Project not found" },
          { status: 404 }
        );
      }

      // Simular detección
      const detected = project.status === "PROHIBITED" || project.status === "WARNING";

      return NextResponse.json({
        success: true,
        detected,
        systems: project.detectedSystems,
      });
    }

    if (action === "block") {
      // Simular bloqueo de despliegue
      const project = mockProjects.find((p) => p.id === projectId);
      if (!project) {
        return NextResponse.json(
          { success: false, error: "Project not found" },
          { status: 404 }
        );
      }

      project.blocked = true;
      project.status = "BLOCKED";

      return NextResponse.json({
        success: true,
        project,
      });
    }

    return NextResponse.json(
      { success: false, error: "Invalid action" },
      { status: 400 }
    );
  } catch (error) {
    console.error("Error in POST /api/compliance/prohibited-systems:", error);
    return NextResponse.json(
      { success: false, error: "Error processing request" },
      { status: 500 }
    );
  }
}


