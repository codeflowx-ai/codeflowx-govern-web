import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, executionType: "AUTOMATED", status: "COMPLETED", duration: 125, startedAt: "2024-01-15 10:30" },
      { id: 2, executionType: "MANUAL", status: "RUNNING", duration: 45, startedAt: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

