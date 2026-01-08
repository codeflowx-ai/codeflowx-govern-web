import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, collaborationType: "TEAM", status: "ACTIVE", agents: 4, createdAt: "2024-01-15" },
      { id: 2, collaborationType: "PAIR", status: "PENDING", agents: 2, createdAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

