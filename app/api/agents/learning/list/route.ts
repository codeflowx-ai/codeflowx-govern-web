import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, learningType: "SUPERVISED", status: "ACTIVE", progress: 75, lastUpdate: "2024-01-15" },
      { id: 2, learningType: "REINFORCEMENT", status: "PENDING", progress: 45, lastUpdate: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

