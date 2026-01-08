import { NextRequest, NextResponse } from "next/server";

export const dynamic = "force-dynamic";

// Mock data para una política completa
const mockPolicy = {
  idxpolicy: 1,
  name: "Política de Seguridad de Datos",
  version: 1,
  policytype: "SECURITY",
  category: "Seguridad de Información",
  enforcementlevel: "MANDATORY",
  description: "Política para garantizar la seguridad de los datos sensibles en el sistema.",
  effectivedate: "2025-01-01",
  expirationdate: "2026-01-01",
  status: "ACTIVE",
  metadata: '{"framework": "ISO 27001", "owner": "IT Security"}',
  createdby: 1,
  createdat: "2024-12-01T10:00:00Z",
  updatedat: "2025-01-15T14:30:00Z",
};

const mockRules = [
  {
    idxpolicyrule: 1,
    name: "Regla de Encriptación",
    conditiontext: "IF data.sensitivity == 'HIGH' THEN encrypt",
    actiontext: "ENCRYPT data",
    priority: 1,
    isactive: true,
  },
  {
    idxpolicyrule: 2,
    name: "Regla de Acceso",
    conditiontext: "IF user.role != 'ADMIN' THEN deny",
    actiontext: "DENY access",
    priority: 2,
    isactive: true,
  },
];

const mockEvaluations = [
  {
    idxpolicyevaluation: 1,
    evaluationdate: "2025-01-10T10:00:00Z",
    result: "PASS",
    compliancescore: 85,
    evaluatedby: "admin@example.com",
  },
  {
    idxpolicyevaluation: 2,
    evaluationdate: "2025-01-15T14:00:00Z",
    result: "PASS",
    compliancescore: 90,
    evaluatedby: "admin@example.com",
  },
];

const mockViolations = [
  {
    idxpolicyviolation: 1,
    violationtype: "ACCESS_VIOLATION",
    description: "Intento de acceso no autorizado a datos sensibles",
    severity: "HIGH",
    detectedat: "2025-01-12T08:30:00Z",
    status: "RESOLVED",
  },
  {
    idxpolicyviolation: 2,
    violationtype: "ENCRYPTION_VIOLATION",
    description: "Datos sensibles almacenados sin encriptación",
    severity: "CRITICAL",
    detectedat: "2025-01-13T11:20:00Z",
    status: "PENDING",
  },
];

const mockAssessments = [
  {
    idxcomplianceassessment: 1,
    assessmentdate: "2025-01-10T10:00:00Z",
    compliancearea: "Data Protection",
    compliancescore: 85,
    status: "PASSED",
    totalfindings: 2,
  },
  {
    idxcomplianceassessment: 2,
    assessmentdate: "2025-01-15T14:00:00Z",
    compliancearea: "Access Control",
    compliancescore: 90,
    status: "PASSED",
    totalfindings: 1,
  },
];

const mockStatistics = {
  totalRules: 2,
  totalEvaluations: 2,
  totalViolations: 2,
  currentPolicyScore: 87.5,
  averageComplianceScore: 87.5,
  lastEvaluationStatus: "PASS",
  lastEvaluationDate: "2025-01-15",
};

export async function GET(_request: NextRequest, { params }: { params: { id: string } }) {
  try {
    const { id } = params;

    if (id === "new") {
      return NextResponse.json({
        success: true,
        policy: {
          name: "",
          version: 1,
          policytype: "COMPLIANCE",
          category: "",
          enforcementlevel: "RECOMMENDED",
          description: "",
          effectivedate: "",
          expirationdate: "",
          status: "DRAFT",
          metadata: "",
        },
        rules: [],
        evaluations: [],
        violations: [],
        assessments: [],
        statistics: {
          totalRules: 0,
          totalEvaluations: 0,
          totalViolations: 0,
          currentPolicyScore: 0,
          averageComplianceScore: 0,
          lastEvaluationStatus: "NOT_EVALUATED",
          lastEvaluationDate: "",
        },
      });
    }

    return NextResponse.json({
      success: true,
      policy: { ...mockPolicy, idxpolicy: parseInt(id) || mockPolicy.idxpolicy },
      rules: mockRules,
      evaluations: mockEvaluations,
      violations: mockViolations,
      assessments: mockAssessments,
      statistics: mockStatistics,
    });
  } catch (error) {
    console.error("Error in GET /api/governance/policies/[id]:", error);
    return NextResponse.json({ success: false, error: "Error loading policy" }, { status: 500 });
  }
}

export async function PUT(request: NextRequest, { params }: { params: { id: string } }) {
  try {
    const { id } = params;
    const body = await request.json();

    const updatedPolicy = {
      ...mockPolicy,
      ...body,
      idxpolicy: parseInt(id),
      updatedat: new Date().toISOString(),
    };

    return NextResponse.json({
      success: true,
      policy: updatedPolicy,
    });
  } catch (error) {
    console.error("Error in PUT /api/governance/policies/[id]:", error);
    return NextResponse.json({ success: false, error: "Error updating policy" }, { status: 500 });
  }
}

export async function DELETE(_request: NextRequest, _ctx: { params: { id: string } }) {
  try {
    return NextResponse.json({
      success: true,
      message: "Policy deleted successfully",
    });
  } catch (error) {
    console.error("Error in DELETE /api/governance/policies/[id]:", error);
    return NextResponse.json({ success: false, error: "Error deleting policy" }, { status: 500 });
  }
}
