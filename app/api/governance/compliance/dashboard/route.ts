import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    try {
      const mockData = await import('@/mocks/governance/compliance/dashboard.json');
      return NextResponse.json(mockData.default || mockData);
    } catch (error) {
      console.error('Error loading mock data:', error);
      // Fallback a datos hardcodeados si falla la importación
      return NextResponse.json({
        completionRate: 78.5,
        completedAssessments: 47,
        totalAssessments: 60,
        friaApprovalRate: 85.2,
        approvedFria: 23,
        totalFria: 27,
        euSubmissionRate: 72.0,
        submittedEuRegistrations: 18,
        totalEuRegistrations: 25,
        averageComplianceScore: 82.3,
        complianceStatus: "Cumplimiento Parcial",
        pendingAssessments: 13,
        pendingFria: 4,
        pendingEuRegistrations: 7,
      });
    }
  }

  const bffUrl = process.env.NEXT_PUBLIC_BFF_COMPLIANCE_URL || 'http://localhost:8083';
  const response = await fetch(`${bffUrl}/api/v1/compliance/dashboard/metrics`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener métricas del dashboard' },
      { status: response.status }
    );
  }

  const data = await response.json();
  return NextResponse.json(data);
}
