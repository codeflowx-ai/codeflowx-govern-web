import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, transparencyType: "DECISION", status: "ACTIVE", score: 88, lastAudit: "2024-01-15" },
      { id: 2, transparencyType: "PROCESS", status: "PENDING", score: 75, lastAudit: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

