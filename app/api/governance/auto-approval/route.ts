import { NextResponse } from "next/server";

import { mockAutoApprovalData } from "@/app/(app)/governance/data/mockData";

export const dynamic = "force-dynamic";

export async function GET() {
  return NextResponse.json({
    success: true,
    data: mockAutoApprovalData,
  });
}
