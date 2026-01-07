import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, versionNumber: "v2.1.0", status: "ACTIVE", releaseDate: "2024-01-15", changes: 45 },
      { id: 2, versionNumber: "v2.2.0", status: "PENDING", releaseDate: "2024-01-20", changes: 32 },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

