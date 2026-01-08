/**
 * TypeScript types para Compliance Assessment
 *
 * Alineados con los DTOs del BFF:
 * - com.codeflowx.govern.nocode.dtos.compliance.ComplianceAssessmentDto
 * - com.codeflowx.governance.dtos.compliance.ComplianceAssessmentDto (core-services)
 */

import { z } from 'zod';

/**
 * Schema Zod para ComplianceAssessmentDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.ComplianceAssessmentDto
 */
export const ComplianceAssessmentSchema = z.object({
  idxcomplianceassessment: z.number().nullable().optional(),
  iduuid: z.string().nullable().optional(),
  idxproject: z.number().nullable().optional(),
  idxuser: z.number().nullable().optional(),
  comassessmenttype: z.enum(['SELF_ASSESSMENT', 'NOTIFIED_BODY']).nullable(),
  comassessmentdate: z.string().datetime().nullable(), // ISO 8601 datetime from LocalDateTime
  comannexvicompliant: z.boolean().nullable(),
  comstep2qmsscore: z.number().nullable().optional(), // BigDecimal as number (0.00 - 1.00)
  comstep3docscore: z.number().nullable().optional(), // BigDecimal as number (0.00 - 1.00)
  comstep4consistencyscore: z.number().nullable().optional(), // BigDecimal as number (0.00 - 1.00)
  comoverallscore: z.number().nullable().optional(), // BigDecimal as number (promedio de steps 2, 3, 4)
  comgaps: z.string().nullable().optional(), // JSON string
  comresults: z.string().nullable().optional(), // JSON string
  comreadyforcertification: z.boolean().nullable().optional(),
  comcertificateid: z.string().max(100).nullable().optional(),
  comreporturl: z.string().max(500).nullable().optional(),
  comcreatedat: z.string().datetime().nullable(), // ISO 8601 datetime from LocalDateTime (NOT NULL)
  comupdatedat: z.string().datetime().nullable().optional(), // ISO 8601 datetime from LocalDateTime
});

/**
 * Type TypeScript para ComplianceAssessment
 */
export type ComplianceAssessment = z.infer<typeof ComplianceAssessmentSchema>;

/**
 * Schema Zod para CreateComplianceAssessmentRequestDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.CreateComplianceAssessmentRequestDto
 */
export const CreateComplianceAssessmentRequestSchema = z.object({
  projectId: z.number().positive(), // @NotNull
  assessmentType: z.enum(['SELF_ASSESSMENT', 'NOTIFIED_BODY']), // @NotNull, @Size(max = 30)
  executedBy: z.string().max(100).optional().nullable(), // @Size(max = 100)
});

/**
 * Type TypeScript para CreateComplianceAssessmentRequest
 */
export type CreateComplianceAssessmentRequest = z.infer<typeof CreateComplianceAssessmentRequestSchema>;

/**
 * Schema Zod para StepExecutionRequestDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.StepExecutionRequestDto
 */
export const StepExecutionRequestSchema = z.object({
  executedBy: z.string().max(100).optional().nullable(), // @Size(max = 100)
});

/**
 * Type TypeScript para StepExecutionRequest
 */
export type StepExecutionRequest = z.infer<typeof StepExecutionRequestSchema>;

/**
 * Schema Zod para StepExecutionResponseDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.StepExecutionResponseDto
 */
export const StepExecutionResponseSchema = z.object({
  status: z.string(), // Ejemplo: "success"
  message: z.string(), // Ejemplo: "Step 2 executed"
});

/**
 * Type TypeScript para StepExecutionResponse
 */
export type StepExecutionResponse = z.infer<typeof StepExecutionResponseSchema>;

/**
 * Schema Zod para CalculateOverallScoreResponseDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.CalculateOverallScoreResponseDto
 */
export const CalculateOverallScoreResponseSchema = z.object({
  overallScore: z.number().nullable(), // BigDecimal as number
  readyForCertification: z.boolean().nullable(),
});

/**
 * Type TypeScript para CalculateOverallScoreResponse
 */
export type CalculateOverallScoreResponse = z.infer<typeof CalculateOverallScoreResponseSchema>;

/**
 * Schema Zod para ConformityAssessmentMetricsDto
 * Alineado con el DTO del BFF: com.codeflowx.govern.nocode.dtos.compliance.ConformityAssessmentMetricsDto
 */
export const ConformityAssessmentMetricsSchema = z.object({
  total: z.number().int(), // int en Java
  completed: z.number().int(), // int en Java
  readyForCertification: z.number().int(), // int en Java
  averageScore: z.number(), // double en Java
  inProgress: z.number().int(), // int en Java
  pending: z.number().int(), // int en Java
});

/**
 * Type TypeScript para ConformityAssessmentMetrics
 */
export type ConformityAssessmentMetrics = z.infer<typeof ConformityAssessmentMetricsSchema>;

/**
 * Schema Zod para ScoreDistributionDto
 */
export const ScoreDistributionSchema = z.object({
  distribution: z.record(z.string(), z.number().int()),
});

/**
 * Type TypeScript para ScoreDistribution
 */
export type ScoreDistribution = z.infer<typeof ScoreDistributionSchema>;
