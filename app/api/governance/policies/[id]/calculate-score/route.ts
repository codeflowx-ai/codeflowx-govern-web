import { NextRequest, NextResponse } from "next/server";

export const dynamic = "force-dynamic";

export async function POST(_request: NextRequest, _ctx: { params: { id: string } }) {
  try {
    const scoreResult = {
      success: true,
      score: 87.5,
      level: "GOOD",
      metrics: {
        total_rules: 2,
        active_rules: 2,
        total_evaluations: 2,
        avg_confidence: 87.5,
      },
    };

    return NextResponse.json(scoreResult);
  } catch (error) {
    console.error("Error in POST /api/governance/policies/[id]/calculate-score:", error);
    return NextResponse.json({ success: false, error: "Error calculating score" }, { status: 500 });
  }
}
