import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, healthStatus: "HEALTHY", status: "ACTIVE", score: 95, lastCheck: "2024-01-15 10:30" },
      { id: 2, healthStatus: "WARNING", status: "PENDING", score: 72, lastCheck: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

