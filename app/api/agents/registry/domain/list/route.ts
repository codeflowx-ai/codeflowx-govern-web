import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, domainName: "Customer Service", status: "ACTIVE", agents: 5, createdAt: "2024-01-15" },
      { id: 2, domainName: "Sales", status: "PENDING", agents: 3, createdAt: "2024-01-16" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

