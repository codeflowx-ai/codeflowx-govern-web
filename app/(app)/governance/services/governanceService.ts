import {
  AutoApprovalResponse,
  ComplianceCheckRequest,
  ComplianceChecksResponse,
  ComplianceResponse,
  DecisionsResponse,
  GovernanceMetricsResponse,
  ManualReviewRequest,
  PoliciesResponse,
  PolicyUpdateRequest,
  RiskAssessmentResponse
} from '../types/governance';

import {
  mockAutoApprovalData,
  mockComplianceChecks,
  mockComplianceData,
  mockDecisions,
  mockGovernanceMetrics,
  mockPolicies,
  mockRiskAssessmentData,
  simulateApiCall,
} from '../data/mockData';

// Usar API routes de Next.js en lugar de conectarse directamente al backend
const API_BASE_URL = '/api/governance';
// En el cliente, solo tenemos acceso a NEXT_PUBLIC_* variables
const IS_DEMO_MODE = process.env.NEXT_PUBLIC_USE_MOCK === 'true'
  || process.env.NEXT_PUBLIC_DEMO_MODE === 'true'
  || !process.env.NEXT_PUBLIC_API_URL;

class GovernanceService {
  private readonly baseUrl = API_BASE_URL;

  private buildUrl(path: string, params?: Record<string, string | undefined>): string {
    const url = new URL(path.startsWith('/') ? `${this.baseUrl}${path}` : `${this.baseUrl}/${path}`, 'http://localhost');
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value) url.searchParams.set(key, value);
      });
    }
    // removemos el origin fake
    return `${url.pathname}${url.search}`;
  }

  private isDemoMode(): boolean {
    return IS_DEMO_MODE;
  }

  // Governance Metrics
  async getMetrics(projectId?: string): Promise<GovernanceMetricsResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        metrics: mockGovernanceMetrics
      });
    }

    try {
      const url = this.buildUrl('/metrics', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) {
        // Si falla, usar mocks como fallback
        console.warn('Failed to fetch governance metrics, using mocks');
        return simulateApiCall({
          success: true,
          metrics: mockGovernanceMetrics
        });
      }
      return await response.json();
    } catch (error) {
      // Si falla, usar mocks como fallback
      console.warn('Error fetching governance metrics, using mocks:', error);
      return simulateApiCall({
        success: true,
        metrics: mockGovernanceMetrics
      });
    }
  }

  // Auto Approval Data
  async getAutoApprovalData(projectId?: string): Promise<AutoApprovalResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        data: mockAutoApprovalData
      });
    }

    try {
      const url = this.buildUrl('/auto-approval', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch auto approval data');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching auto approval data: ${error}`);
    }
  }

  // Compliance Data
  async getComplianceData(projectId?: string): Promise<ComplianceResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        data: mockComplianceData
      });
    }

    try {
      const url = this.buildUrl('/compliance/data', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch compliance data');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching compliance data: ${error}`);
    }
  }

  // Pending Decisions
  async getPendingDecisions(projectId?: string): Promise<DecisionsResponse> {
    if (this.isDemoMode()) {
      const pendingDecisions = mockDecisions.filter(d => d.status === 'pending' || d.status === 'human_review');
      return simulateApiCall({
        success: true,
        decisions: pendingDecisions
      });
    }

    try {
      const url = this.buildUrl('/decisions', { status: 'pending', project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch pending decisions');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching pending decisions: ${error}`);
    }
  }

  // All Decisions
  async getAllDecisions(projectId?: string): Promise<DecisionsResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        decisions: mockDecisions
      });
    }

    try {
      const url = this.buildUrl('/decisions', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch decisions');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching decisions: ${error}`);
    }
  }

  // Active Compliance Checks
  async getActiveComplianceChecks(projectId?: string): Promise<ComplianceChecksResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        checks: mockComplianceChecks
      });
    }

    try {
      const url = this.buildUrl('/compliance/active', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch active compliance checks');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching active compliance checks: ${error}`);
    }
  }

  // Risk Assessment
  async getRiskAssessment(projectId?: string): Promise<RiskAssessmentResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        data: mockRiskAssessmentData
      });
    }

    try {
      const url = this.buildUrl('/risk-assessment', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch risk assessment');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching risk assessment: ${error}`);
    }
  }

  // Policies
  async getPolicies(projectId?: string): Promise<PoliciesResponse> {
    if (this.isDemoMode()) {
      return simulateApiCall({
        success: true,
        policies: mockPolicies
      });
    }

    try {
      const url = this.buildUrl('/policies', { project_id: projectId });
      const response = await fetch(url);
      if (!response.ok) throw new Error('Failed to fetch policies');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching policies: ${error}`);
    }
  }

  // Manual Review
  async requireManualReview(request: ManualReviewRequest): Promise<{ success: boolean }> {
    if (this.isDemoMode()) {
      return simulateApiCall({ success: true });
    }

    try {
      const response = await fetch(`${this.baseUrl}/decisions/${request.decisionId}/manual-review`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
      });
      if (!response.ok) throw new Error('Failed to require manual review');
      return await response.json();
    } catch (error) {
      throw new Error(`Error requiring manual review: ${error}`);
    }
  }

  // Run Compliance Check
  async runComplianceCheck(request: ComplianceCheckRequest): Promise<{ success: boolean }> {
    if (this.isDemoMode()) {
      return simulateApiCall({ success: true });
    }

    try {
      const response = await fetch(`${this.baseUrl}/compliance/auto-check`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
      });
      if (!response.ok) throw new Error('Failed to run compliance check');
      return await response.json();
    } catch (error) {
      throw new Error(`Error running compliance check: ${error}`);
    }
  }

  // Update Policy
  async updatePolicy(request: PolicyUpdateRequest): Promise<{ success: boolean }> {
    if (this.isDemoMode()) {
      return simulateApiCall({ success: true });
    }

    try {
      const response = await fetch(`${this.baseUrl}/policies/${request.policyId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(request)
      });
      if (!response.ok) throw new Error('Failed to update policy');
      return await response.json();
    } catch (error) {
      throw new Error(`Error updating policy: ${error}`);
    }
  }

  // Approve Decision
  async approveDecision(decisionId: string, reason?: string): Promise<{ success: boolean }> {
    if (this.isDemoMode()) {
      return simulateApiCall({ success: true });
    }

    try {
      const response = await fetch(`${this.baseUrl}/decisions/${decisionId}/approve`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ reason })
      });
      if (!response.ok) throw new Error('Failed to approve decision');
      return await response.json();
    } catch (error) {
      throw new Error(`Error approving decision: ${error}`);
    }
  }

  // Reject Decision
  async rejectDecision(decisionId: string, reason: string): Promise<{ success: boolean }> {
    if (this.isDemoMode()) {
      return simulateApiCall({ success: true });
    }

    try {
      const response = await fetch(`${this.baseUrl}/decisions/${decisionId}/reject`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ reason })
      });
      if (!response.ok) throw new Error('Failed to reject decision');
      return await response.json();
    } catch (error) {
      throw new Error(`Error rejecting decision: ${error}`);
    }
  }
}

export const governanceService = new GovernanceService();
