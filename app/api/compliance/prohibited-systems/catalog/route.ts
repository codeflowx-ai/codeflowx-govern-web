import { NextRequest, NextResponse } from "next/server";

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
    const category = searchParams.get("category");
    const active = searchParams.get("active");
    const search = searchParams.get("search");

    let catalog = [...mockCatalog];

    if (category && category !== "ALL") {
      catalog = catalog.filter((item) => item.category === category);
    }

    if (active === "true") {
      catalog = catalog.filter((item) => item.active);
    } else if (active === "false") {
      catalog = catalog.filter((item) => !item.active);
    }

    if (search) {
      const searchLower = search.toLowerCase();
      catalog = catalog.filter(
        (item) =>
          item.name.toLowerCase().includes(searchLower) ||
          item.description.toLowerCase().includes(searchLower) ||
          item.keywords.some((kw) => kw.toLowerCase().includes(searchLower))
      );
    }

    return NextResponse.json({
      success: true,
      catalog,
    });
  } catch (error) {
    console.error("Error in GET /api/compliance/prohibited-systems/catalog:", error);
    return NextResponse.json(
      { success: false, error: "Error loading catalog" },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Simular creación de sistema prohibido
    const newSystem = {
      id: mockCatalog.length + 1,
      ...body,
      active: body.active !== undefined ? body.active : true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    return NextResponse.json({
      success: true,
      system: newSystem,
    });
  } catch (error) {
    console.error("Error in POST /api/compliance/prohibited-systems/catalog:", error);
    return NextResponse.json(
      { success: false, error: "Error creating system" },
      { status: 500 }
    );
  }
}

export async function PUT(request: NextRequest) {
  try {
    const body = await request.json();
    const { id, ...updates } = body;

    // Simular actualización
    const system = mockCatalog.find((s) => s.id === id);
    if (!system) {
      return NextResponse.json(
        { success: false, error: "System not found" },
        { status: 404 }
      );
    }

    const updatedSystem = {
      ...system,
      ...updates,
      updatedAt: new Date().toISOString(),
    };

    return NextResponse.json({
      success: true,
      system: updatedSystem,
    });
  } catch (error) {
    console.error("Error in PUT /api/compliance/prohibited-systems/catalog:", error);
    return NextResponse.json(
      { success: false, error: "Error updating system" },
      { status: 500 }
    );
  }
}

export async function DELETE(request: NextRequest) {
  try {
    const searchParams = request.nextUrl.searchParams;
    const id = searchParams.get("id");

    if (!id) {
      return NextResponse.json(
        { success: false, error: "ID is required" },
        { status: 400 }
      );
    }

    // Simular eliminación
    const system = mockCatalog.find((s) => s.id === parseInt(id));
    if (!system) {
      return NextResponse.json(
        { success: false, error: "System not found" },
        { status: 404 }
      );
    }

    return NextResponse.json({
      success: true,
      message: "System deleted successfully",
    });
  } catch (error) {
    console.error("Error in DELETE /api/compliance/prohibited-systems/catalog:", error);
    return NextResponse.json(
      { success: false, error: "Error deleting system" },
      { status: 500 }
    );
  }
}


