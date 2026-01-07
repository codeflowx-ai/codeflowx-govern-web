import { NextResponse } from "next/server";

export async function GET() {
  // Mock data
  const mockData = {
    items: [
      { id: 1, alertType: "TYPE1", alertCategory: "SECURITY", severity: "HIGH", status: "ACTIVE", title: "Security Alert 1" },
      { id: 2, alertType: "TYPE2", alertCategory: "PERFORMANCE", severity: "MEDIUM", status: "PENDING", title: "Performance Alert 1" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
