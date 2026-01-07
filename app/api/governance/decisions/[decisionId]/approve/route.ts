import { NextResponse } from "next/server";

export const dynamic = "force-dynamic";

export async function POST(
  request: Request,
  { params }: { params: { decisionId: string } }
) {
  let reason: string | undefined;
  try {
    const body = await request.json();
    reason = body?.reason;
  } catch {
    // ignore
  }

  return NextResponse.json({
    success: true,
    decisionId: params.decisionId,
    action: "approve",
    reason,
  });
}
