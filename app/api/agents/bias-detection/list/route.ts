import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, detectionType: "GENDER", severity: "HIGH", status: "ACTIVE", description: "Gender bias detected", detectedAt: "2024-01-15" },
      { id: 2, detectionType: "RACIAL", severity: "MEDIUM", status: "PENDING", description: "Racial bias detected", detectedAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
