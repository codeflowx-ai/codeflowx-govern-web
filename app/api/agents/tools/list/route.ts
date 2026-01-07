import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, toolName: "API Client", toolType: "EXTERNAL", status: "ACTIVE", version: "v1.2.0" },
      { id: 2, toolName: "Database Connector", toolType: "INTERNAL", status: "PENDING", version: "v2.0.1" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

