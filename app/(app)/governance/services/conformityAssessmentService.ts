/**
 * Servicio para integración con backend de Evaluaciones de Conformidad
 * Art. 43 EU AI Act - Evaluación de conformidad según Anexo VI
 *
 * Backend Services:
 * - ComplianceAssessmentBusinessService
 * - ComplianceAssessmentService
 * - ComplianceDashboardService
 */

export interface ConformityAssessment {
  id: number;
  projectId: number;
  projectName?: string;
  assessmentDate: string;
  assessmentType: string;
  step1Score: number | null;
  step2QmsScore: number | null;
  step3DocScore: number | null;
  step4ConsistencyScore: number | null;
  overallScore: number | null;
  readyForCertification: boolean;
  annexViCompliant: boolean;
  certificateId: string | null;
  status: "PENDING" | "IN_PROGRESS" | "COMPLETED" | "REJECTED";
  createdBy: string;
  createdAt: string;
  updatedBy?: string;
  updatedAt?: string;
}

export interface ConformityMetrics {
  total: number;
  completed: number;
  readyForCertification: number;
  averageScore: number;
  inProgress: number;
  pending: number;
}

export interface CreateAssessmentRequest {
  projectId: number;
  assessmentType: string;
  executedBy: string;
}

export interface ExecuteStepRequest {
  executedBy: string;
}

class ConformityAssessmentService {
  private baseUrl = '/api/governance/compliance';

  /**
   * Obtiene todas las evaluaciones de conformidad
   * Backend: ComplianceAssessmentService.findAll()
   */
  async getAll(): Promise<ConformityAssessment[]> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          // TODO: Añadir Authorization header cuando esté disponible
          // 'Authorization': `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch assessments: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessments(data);
    } catch (error) {
      console.error('Error fetching assessments:', error);
      throw error;
    }
  }

  /**
   * Obtiene una evaluación por ID
   * Backend: ComplianceAssessmentService.findById(id)
   */
  async getById(id: number): Promise<ConformityAssessment> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments/${id}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch assessment ${id}: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessment(data);
    } catch (error) {
      console.error(`Error fetching assessment ${id}:`, error);
      throw error;
    }
  }

  /**
   * Crea una nueva evaluación de conformidad
   * Backend: ComplianceAssessmentBusinessService.createAssessment()
   *
   * Validaciones backend:
   * - Proyecto debe ser de alto riesgo
   * - Proyecto debe tener FRIA completada
   * - Proyecto debe tener clasificación Anexo III
   */
  async create(request: CreateAssessmentRequest): Promise<ConformityAssessment> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to create assessment: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessment(data);
    } catch (error) {
      console.error('Error creating assessment:', error);
      throw error;
    }
  }

  /**
   * Ejecuta Step 2: Verificación QMS (Art. 17)
   * Backend: ComplianceAssessmentBusinessService.executeStep2QmsCheck()
   *
   * Calcula el score QMS basado en 13 módulos del sistema de gestión de calidad
   */
  async executeStep2(assessmentId: number, executedBy: string): Promise<ConformityAssessment> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/execute-step2`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ executedBy }),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to execute step 2: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessment(data);
    } catch (error) {
      console.error(`Error executing step 2 for assessment ${assessmentId}:`, error);
      throw error;
    }
  }

  /**
   * Ejecuta Step 3: Revisión Documentación Técnica (Art. 11, Anexo IV)
   * Backend: ComplianceAssessmentBusinessService.executeStep3DocReview()
   *
   * Calcula el score de documentación basado en 11 secciones del Anexo IV
   */
  async executeStep3(assessmentId: number, executedBy: string): Promise<ConformityAssessment> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/execute-step3`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ executedBy }),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to execute step 3: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessment(data);
    } catch (error) {
      console.error(`Error executing step 3 for assessment ${assessmentId}:`, error);
      throw error;
    }
  }

  /**
   * Calcula el score overall y determina si está listo para certificación
   * Backend: ComplianceAssessmentBusinessService.calculateOverallScore()
   *
   * Fórmula: overallScore = (step2QmsScore + step3DocScore + step4ConsistencyScore) / 3
   * readyForCertification = overallScore >= 0.80
   */
  async calculateOverallScore(assessmentId: number): Promise<ConformityAssessment> {
    try {
      const response = await fetch(`${this.baseUrl}/assessments/${assessmentId}/calculate-overall`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Failed to calculate overall score: ${response.statusText}`);
      }

      const data = await response.json();
      return this.mapAssessment(data);
    } catch (error) {
      console.error(`Error calculating overall score for assessment ${assessmentId}:`, error);
      throw error;
    }
  }

  /**
   * Obtiene las métricas del dashboard
   * Backend: ComplianceDashboardService.getConformityMetrics()
   */
  async getMetrics(): Promise<ConformityMetrics> {
    try {
      const response = await fetch(`${this.baseUrl}/dashboard/metrics`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch metrics: ${response.statusText}`);
      }

      const data = await response.json();
      return {
        total: data.total || 0,
        completed: data.completed || 0,
        readyForCertification: data.readyForCertification || 0,
        averageScore: data.averageScore || 0,
        inProgress: data.inProgress || 0,
        pending: data.pending || 0,
      };
    } catch (error) {
      console.error('Error fetching metrics:', error);
      throw error;
    }
  }

  /**
   * Mapea una evaluación del formato backend al formato frontend
   * Conversión de nombres de campos y tipos de datos
   */
  private mapAssessment(backendData: any): ConformityAssessment {
    // Determinar status basado en los scores
    let status: "PENDING" | "IN_PROGRESS" | "COMPLETED" | "REJECTED" = "PENDING";
    if (backendData.overallScore !== null && backendData.overallScore !== undefined) {
      status = "COMPLETED";
    } else if (
      backendData.step2QmsScore !== null ||
      backendData.step3DocScore !== null
    ) {
      status = "IN_PROGRESS";
    }

    return {
      id: backendData.id || backendData.idxcomplianceassessment,
      projectId: backendData.projectId || backendData.idxproject,
      projectName: backendData.projectName,
      assessmentDate: backendData.assessmentDate || backendData.comassessmentdate,
      assessmentType: backendData.assessmentType || backendData.comassessmenttype || "FULL",
      step1Score: this.mapScore(backendData.step1Score || backendData.comstep1score),
      step2QmsScore: this.mapScore(backendData.step2QmsScore || backendData.comstep2qmsscore),
      step3DocScore: this.mapScore(backendData.step3DocScore || backendData.comstep3docscore),
      step4ConsistencyScore: this.mapScore(backendData.step4ConsistencyScore || backendData.comstep4consistencyscore),
      overallScore: this.mapScore(backendData.overallScore || backendData.comoverallscore),
      readyForCertification: backendData.readyForCertification ?? backendData.comreadyforcertification ?? false,
      annexViCompliant: backendData.annexViCompliant ?? backendData.comannexvicompliant ?? false,
      certificateId: backendData.certificateId || backendData.comcertificateid || null,
      status: backendData.status || status,
      createdBy: backendData.createdBy || backendData.comcreatedby || "",
      createdAt: backendData.createdAt || backendData.comcreatedat || new Date().toISOString(),
      updatedBy: backendData.updatedBy || backendData.comupdatedby,
      updatedAt: backendData.updatedAt || backendData.comupdatedat,
    };
  }

  /**
   * Mapea múltiples evaluaciones
   */
  private mapAssessments(backendData: any[]): ConformityAssessment[] {
    return backendData.map(item => this.mapAssessment(item));
  }

  /**
   * Convierte BigDecimal del backend (0.00 - 1.00) a number
   * Maneja null/undefined
   */
  private mapScore(score: any): number | null {
    if (score === null || score === undefined) return null;
    const num = typeof score === 'string' ? parseFloat(score) : score;
    return isNaN(num) ? null : num;
  }
}

export const conformityAssessmentService = new ConformityAssessmentService();



