import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, communicationType: "CHAT", status: "ACTIVE", messages: 45, lastMessage: "2024-01-15 10:30" },
      { id: 2, communicationType: "EMAIL", status: "PENDING", messages: 12, lastMessage: "2024-01-15 11:00" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}

