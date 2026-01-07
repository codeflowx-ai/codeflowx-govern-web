import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, complianceType: "GDPR", status: "COMPLIANT", lastCheck: "2024-01-15", score: 95 },
      { id: 2, complianceType: "HIPAA", status: "PENDING", lastCheck: "2024-01-14", score: 78 },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
