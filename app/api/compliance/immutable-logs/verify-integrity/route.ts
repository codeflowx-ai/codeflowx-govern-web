import { NextRequest, NextResponse } from "next/server";
import { USE_MOCK, GATEWAY_WEB_BASE } from '@/app/config/mock';
import {
  mockImmutableLogs,
  mockIntegrityVerification,
  mockIntegrityVerificationBroken,
  type IntegrityVerificationResult
} from "@/app/(app)/governance/data/mockImmutableLogs";

/**
 * POST /api/compliance/immutable-logs/verify-integrity
 *
 * Verifica la integridad de la hash chain en un rango de logs
 *
 * Backend: codeflowx-governance-immutable-logs-service
 * Endpoint: POST /web/api/v1/compliance/immutable-logs/verify-integrity
 */
export async function POST(request: NextRequest) {
  try {
    const { startId, endId } = await request.json();

    if (!startId || !endId) {
      return NextResponse.json(
        { error: "startId and endId are required" },
        { status: 400 }
      );
    }

    if (USE_MOCK) {

    // Filtrar logs en el rango
    const logsInRange = mockImmutableLogs.filter(
      log => log.id >= startId && log.id <= endId
    );

    if (logsInRange.length === 0) {
      return NextResponse.json(
        { error: "No logs found in the specified range" },
        { status: 404 }
      );
    }

    // Simular verificación de integridad
    // En producción, esto calcularía los hashes y verificaría la cadena
    const totalLogs = logsInRange.length;
    const verifiedLogs = logsInRange.filter(log => log.integrityVerified).length;
    const integrityScore = verifiedLogs / totalLogs;

    // Determinar estado
    let status: "INTEGRITY_OK" | "INTEGRITY_BROKEN" | "INTEGRITY_PARTIAL";
    if (integrityScore === 1.0) {
      status = "INTEGRITY_OK";
    } else if (integrityScore >= 0.9) {
      status = "INTEGRITY_PARTIAL";
    } else {
      status = "INTEGRITY_BROKEN";
    }

    const result: IntegrityVerificationResult = {
      startId,
      endId,
      totalLogs,
      verifiedLogs,
      integrityScore: Math.round(integrityScore * 100) / 100,
      status
    };

    // Si hay problemas, añadir información de cadenas rotas
    if (status !== "INTEGRITY_OK") {
      const brokenLogs = logsInRange.filter(log => !log.integrityVerified);
      result.brokenChains = brokenLogs.map(log => ({
        logId: log.id,
        expectedHash: log.hash,
        actualHash: log.hash // En producción, esto sería diferente si está roto
      }));
    }

      return NextResponse.json(result);
    }

    // Llamada real al backend
    const response = await fetch(`${GATEWAY_WEB_BASE}/compliance/immutable-logs/verify-integrity`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ startId, endId }),
    });

    if (!response.ok) {
      throw new Error(`Backend error: ${response.statusText}`);
    }

    const result = await response.json();
    return NextResponse.json(result);
  } catch (error) {
    console.error("Error verifying integrity:", error);
    return NextResponse.json(
      { error: "Error verifying integrity" },
      { status: 500 }
    );
  }
}
