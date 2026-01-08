import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, monitoringType: "PERFORMANCE", status: "ACTIVE", metrics: 156, lastCheck: "2024-01-15 10:30" },
      { id: 2, monitoringType: "HEALTH", status: "PENDING", metrics: 89, lastCheck: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

