import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, rollbackType: "VERSION", status: "COMPLETED", version: "v2.1.0", rolledBackAt: "2024-01-15 10:30" },
      { id: 2, rollbackType: "CONFIG", status: "PENDING", version: "v2.0.5", rolledBackAt: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

