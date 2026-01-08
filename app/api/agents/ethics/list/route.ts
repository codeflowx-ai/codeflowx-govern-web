import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, assessmentType: "FAIRNESS", status: "PASSED", score: 92, assessedAt: "2024-01-15" },
      { id: 2, assessmentType: "TRANSPARENCY", status: "PENDING", score: 85, assessedAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
