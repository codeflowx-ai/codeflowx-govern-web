import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/app/(app)/governance/data/mockConformityAssessment');
    return NextResponse.json(mockData.mockConformityMetrics);
  }

  const bffUrl = process.env.NEXT_PUBLIC_BFF_COMPLIANCE_URL || 'http://localhost:8083';
  const response = await fetch(`${bffUrl}/api/v1/compliance-assessments/metrics`, {
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
