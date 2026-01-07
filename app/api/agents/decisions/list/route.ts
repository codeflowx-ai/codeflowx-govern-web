import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, decisionType: "APPROVE", status: "EXECUTED", confidence: 95, timestamp: "2024-01-15 10:30" },
      { id: 2, decisionType: "REJECT", status: "PENDING", confidence: 78, timestamp: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
