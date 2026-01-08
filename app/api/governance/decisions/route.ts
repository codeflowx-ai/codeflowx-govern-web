import { NextResponse } from "next/server";

import { mockDecisions } from "@/app/(app)/governance/data/mockData";

export const dynamic = "force-dynamic";

export async function GET(request: Request) {
  const url = new URL(request.url);
  const status = url.searchParams.get("status");

  // Nota: para la UI de Auto Approval, "pendientes" incluye también los que están en revisión humana
  const decisions =
    status === "pending"
      ? mockDecisions.filter(
          (d) => d.status === "pending" || d.status === "human_review"
        )
      : mockDecisions;

  return NextResponse.json({
    success: true,
    decisions,
  });
}
