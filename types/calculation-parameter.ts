/**
 * Tipos TypeScript para gestión de parámetros de cálculo
 */

export interface CalculationParameter {
  id: number;
  uuid: string;
  microservice: string;
  organizationId: string | null;
  organizationName?: string;
  parameterKey: string;
  parameters: Record<string, any>;  // JSON object
  version: number;
  description?: string;
  appliesToEndpoint?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}

export interface CalculationParameterRequest {
  microservice: string;
  organizationId?: string | null;
  parameterKey: string;
  parameters: Record<string, any>;
  description?: string;
  appliesToEndpoint?: string;
  active?: boolean;
}

export interface ParameterHierarchy {
  global: CalculationParameter | null;
  organizations: CalculationParameter[];
}

export interface ValidationResult {
  valid: boolean;
  errors: string[];
}

export interface MicroserviceOption {
  value: string;
  label: string;
  parameterKeys: string[];
}

export const MICROSERVICES: MicroserviceOption[] = [
  {
    value: 'board-governance',
    label: 'Board Governance Calculator',
    parameterKeys: ['edm_metrics', 'health_score', 'executive_summary']
  },
  {
    value: 'llm-evaluation',
    label: 'LLM Evaluation',
    parameterKeys: ['quality_metrics', 'cost_efficiency_metrics', 'consistency_metrics']
  },
  {
    value: 'prompt-governance',
    label: 'Prompt Governance',
    parameterKeys: ['effectiveness_metrics', 'quality_thresholds']
  },
  {
    value: 'agent-monitoring',
    label: 'Agent Monitoring',
    parameterKeys: ['ab_test_metrics', 'performance_metrics']
  },
  {
    value: 'bias-detection',
    label: 'Bias Detection',
    parameterKeys: ['bias_metrics', 'fairness_thresholds']
  },
  {
    value: 'rag-evaluation',
    label: 'RAG Evaluation',
    parameterKeys: ['rag_quality_metrics', 'retrieval_metrics']
  }
];
