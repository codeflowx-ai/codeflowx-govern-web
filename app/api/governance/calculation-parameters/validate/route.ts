/**
 * API Route para validar parámetros
 */

import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080';

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    // Si está en modo mock, simular validación
    if (USE_MOCK) {
      const errors: string[] = [];

      // Validación básica para EDM Metrics
      if (body.microservice === 'board-governance' && body.parameterKey === 'edm_metrics') {
        if (body.parameters?.weights) {
          const { evaluate, direct, monitor } = body.parameters.weights;
          if (evaluate !== undefined && (evaluate < 0 || evaluate > 1)) {
            errors.push('weights.evaluate debe estar entre 0.0 y 1.0');
          }
          if (direct !== undefined && (direct < 0 || direct > 1)) {
            errors.push('weights.direct debe estar entre 0.0 y 1.0');
          }
          if (monitor !== undefined && (monitor < 0 || monitor > 1)) {
            errors.push('weights.monitor debe estar entre 0.0 y 1.0');
          }
        }

        if (body.parameters?.thresholds) {
          const { excellent, healthy, moderate } = body.parameters.thresholds;
          if (excellent !== undefined && healthy !== undefined && excellent < healthy) {
            errors.push('thresholds.excellent debe ser >= thresholds.healthy');
          }
          if (healthy !== undefined && moderate !== undefined && healthy < moderate) {
            errors.push('thresholds.healthy debe ser >= thresholds.moderate');
          }
        }
      }

      return NextResponse.json({
        valid: errors.length === 0,
        errors
      });
    }

    // Si no está en modo mock, llamar al backend real
    const url = `${API_BASE_URL}/api/governance/calculation-parameters/validate`;

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    });

    if (!response.ok) {
      return NextResponse.json(
        { error: 'Error al validar parámetros' },
        { status: response.status }
      );
    }

    const data = await response.json();
    return NextResponse.json(data);
  } catch (error) {
    console.error('Error validating parameters:', error);
    return NextResponse.json(
      { error: 'Error al validar parámetros' },
      { status: 500 }
    );
  }
}
