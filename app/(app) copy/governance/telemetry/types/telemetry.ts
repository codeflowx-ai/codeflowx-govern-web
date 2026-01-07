/**
 * Tipos TypeScript para los DTOs de telemetría
 */

export interface TelemetryKpisResponseDto {
  totalEvents: number;
  totalCostUsd: number;
  totalTokens: number;
  avgLatencyMs: number;
  componentCount: number;
  startTime: string;
  endTime: string;
}

export interface TelemetryEventDto {
  id: number;
  uuid: string;
  timestamp: string;
  componentUuid: string;
  agentExternalId?: string;
  eventType: string;
  severity: string; // INFO, WARN, ERROR, CRITICAL
  traceId?: string;
  runId?: string;
  metrics?: string; // JSON string
  payload?: string; // JSON string
  sourceTool?: string;
  biasChecked?: boolean;
  toxicityChecked?: boolean;
  piiDetected?: boolean;
  secretDetected?: boolean;
  analysisResults?: string; // JSON string
  // Campos de categorización de compliance y seguridad
  complianceStatus?: string; // PASS, WARNING, REVIEW_REQUIRED, VIOLATION, CRITICAL_VIOLATION
  riskLevel?: string; // LOW, MEDIUM, HIGH, CRITICAL
  complianceCategory?: string; // GDPR, CCPA, HIPAA, OWASP, PCI_DSS, SOC2, SECURITY, LEGAL
  issueTags?: string[]; // Array de tags para filtrado: ["pii_exposure", "secret_leak", "sql_injection", "gdpr_violation", etc.]
}

export interface TelemetryEventsResponseDto {
  events: TelemetryEventDto[];
  total: number;
  page: number;
  size: number;
}

export interface ComponentStatisticsDto {
  componentUuid: string;
  totalEvents: number;
  avgLatencyMs: number;
  totalTokens: number;
  totalCostUsd: number;
}

export interface ComponentStatisticsListResponseDto {
  statistics: ComponentStatisticsDto[];
  total: number;
}

export interface TelemetryKpisResponse {
  success: boolean;
  data?: TelemetryKpisResponseDto;
  error?: string;
}

export interface TelemetryEventsResponse {
  success: boolean;
  data?: TelemetryEventsResponseDto;
  error?: string;
}

export interface ComponentStatisticsResponse {
  success: boolean;
  data?: ComponentStatisticsListResponseDto;
  error?: string;
}
