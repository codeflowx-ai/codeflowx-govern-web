import { NextRequest, NextResponse } from 'next/server';
import { USE_MOCK } from '@/app/config/mock';

export async function GET(request: NextRequest) {
  if (USE_MOCK) {
    const mockData = await import('@/app/(app)/governance/data/mockConformityAssessment');
    return NextResponse.json(mockData.mockConformityAssessments);
  }

  const gatewayUrl = process.env.NEXT_PUBLIC_GATEWAY_WEB_URL || 'http://localhost:8080';
  const response = await fetch(`${gatewayUrl}/web/api/v1/compliance/compliance-assessments`, {
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    return NextResponse.json(
      { error: 'Error al obtener evaluaciones' },
      { status: response.status }
    );
  }

  const assessments = await response.json();

  // Mapear DTOs del backend al formato del frontend
  const mappedAssessments = assessments.map((assessment: any) => ({
    id: assessment.idxcomplianceassessment,
    projectId: assessment.idxproject,
    projectName: assessment.projectName || `Project ${assessment.idxproject}`, // TODO: Obtener del Project Service
    assessmentDate: assessment.comassessmentdate || assessment.comcreatedat,
    assessmentType: assessment.comassessmenttype || 'FULL',
    step1Score: 1.0, // Step 1 siempre es 1.0 si pasa la validación
    step2QmsScore: assessment.comstep2qmsscore ? parseFloat(assessment.comstep2qmsscore.toString()) : null,
    step3DocScore: assessment.comstep3docscore ? parseFloat(assessment.comstep3docscore.toString()) : null,
    step4ConsistencyScore: assessment.comstep4consistencyscore ? parseFloat(assessment.comstep4consistencyscore.toString()) : null,
    overallScore: assessment.comoverallscore ? parseFloat(assessment.comoverallscore.toString()) : null,
    readyForCertification: assessment.comreadyforcertification || false,
    annexViCompliant: assessment.comannexvicompliant || false,
    certificateId: assessment.comcertificateid || null,
    status: determineStatus(assessment),
    createdBy: `User ${assessment.idxuser}`,
    createdAt: assessment.comcreatedat || new Date().toISOString(),
    updatedBy: assessment.comupdatedby ? `User ${assessment.comupdatedby}` : undefined,
    updatedAt: assessment.comupdatedat || undefined,
  }));

  return NextResponse.json(mappedAssessments);
}

/**
 * Determina el status de la evaluación basado en los scores y flags
 */
function determineStatus(assessment: any): "PENDING" | "IN_PROGRESS" | "COMPLETED" | "REJECTED" {
  // Si tiene certificado, está completada
  if (assessment.comcertificateid) {
    return 'COMPLETED';
  }

  // Si está listo para certificación, está completada
  if (assessment.comreadyforcertification) {
    return 'COMPLETED';
  }

  // Si tiene overall score, está en progreso o completada
  if (assessment.comoverallscore) {
    return 'COMPLETED';
  }

  // Si tiene algún score de step, está en progreso
  if (assessment.comstep2qmsscore || assessment.comstep3docscore || assessment.comstep4consistencyscore) {
    return 'IN_PROGRESS';
  }

  // Si no tiene scores, está pendiente
  return 'PENDING';
}
