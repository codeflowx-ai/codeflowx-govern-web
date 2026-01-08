import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, BFF_BASE_URL } from "@/app/config/mock";
import { mockImmutableLogs } from "@/app/(app)/governance/data/mockImmutableLogs";

export const dynamic = "force-dynamic";

/**
 * GET /api/compliance/immutable-logs/[id]
 *
 * Obtiene el detalle completo de un log inmutable con su hash chain
 */
export async function GET(
  _request: NextRequest,
  { params }: { params: Promise<{ id: string }> | { id: string } }
) {
  try {
    const resolvedParams = await Promise.resolve(params);
    const id = parseInt(resolvedParams.id);

    if (isNaN(id)) {
      return NextResponse.json({ error: "Invalid log ID" }, { status: 400 });
    }

    if (USE_MOCK) {
      const log = mockImmutableLogs.find((l) => l.id === id);
      if (!log) return NextResponse.json({ error: "Log not found" }, { status: 404 });

      const chainLogs = mockImmutableLogs
        .filter((l) => l.entityType === log.entityType && l.entityId === log.entityId)
        .sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());

      const currentIndex = chainLogs.findIndex((l) => l.id === id);
      const previousLog = currentIndex > 0 ? chainLogs[currentIndex - 1] : null;
      const nextLog = currentIndex < chainLogs.length - 1 ? chainLogs[currentIndex + 1] : null;

      return NextResponse.json({
        log,
        chain: chainLogs,
        previousLog,
        nextLog,
        chainLength: chainLogs.length,
        currentPosition: currentIndex + 1,
      });
    }

    const response = await fetch(`${BFF_BASE_URL}/api/v1/immutable-logs/${id}`, {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });

    if (!response.ok) {
      if (response.status === 404) {
        return NextResponse.json({ error: "Log not found" }, { status: 404 });
      }
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error fetching log:", error);
    return NextResponse.json({ error: "Error fetching log" }, { status: 500 });
  }
}
