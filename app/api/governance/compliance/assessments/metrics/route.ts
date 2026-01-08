import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/app/(app)/governance/data/mockConformityAssessment');
    return NextResponse.json(mockData.mockConformityMetrics);
  }

  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_WEB_URL || 'http://localhost:8080';
  const response = await fetch(`${gatewayUrl}/web/api/v1/compliance/compliance-assessments/metrics`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener métricas de evaluaciones' },
      { status: response.status }
    );
  }

  const data = await response.json();

  // Mapear DTO del backend al formato del frontend
  const mappedMetrics = {
    total: data.total || 0,
    completed: data.completed || 0,
    readyForCertification: data.readyForCertification || 0,
    averageScore: data.averageScore || 0.0,
    inProgress: data.inProgress || 0,
    pending: data.pending || 0,
  };

  return NextResponse.json(mappedMetrics);
}
