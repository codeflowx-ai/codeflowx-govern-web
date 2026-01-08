import { NextResponse } from "next/server";

export async function GET() {
  const mockData = {
    items: [
      { id: 1, approvalType: "TYPE1", approvalStatus: "ACTIVE", requestReason: "Reason 1", requestDetails: "Details 1", approvalCriteria: "Criteria 1" },
      { id: 2, approvalType: "TYPE2", approvalStatus: "PENDING", requestReason: "Reason 2", requestDetails: "Details 2", approvalCriteria: "Criteria 2" },
    ],
    total: 2,
    active: 1,
    pending: 1,
  };

  return NextResponse.json(mockData);
}
