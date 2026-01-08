package com.codeflowx.govern.nocode.dtos.compliance;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTOs para HITL Supervision
 *
 * Ubicación: codeflowx.govern.nocode.dtos/src/main/java/com/codeflowx/govern/nocode/dtos/compliance/
 */

/**
 * DTO para el dashboard de HITL
 */
@Data
public class HitlDashboardDto {
    private HitlMetricsDto metrics;
    private List<HitlInterventionDto> pendingInterventions;
    private List<HitlDecisionDto> recentDecisions;
    private List<HitlSupervisionConfigDto> supervisionConfig;
}

/**
 * DTO para métricas HITL
 */
@Data
public class HitlMetricsDto {
    private Double averageResponseTime; // horas
    private Double approvalRate; // 0-1
    private Double slaCompliance; // 0-1
    private Integer pendingInterventions;
    private Integer totalInterventions;
    private Map<String, Integer> interventionsByType;
    private Map<String, Integer> interventionsByStatus;
}

/**
 * DTO para intervención HITL
 */
@Data
public class HitlInterventionDto {
    private Long id;
    private String type; // AGENT_APPROVAL, MODEL_DEPLOYMENT, PROMPT_REVIEW, HUMAN_OVERRIDE
    private String entityType; // Agent, Model, Prompt
    private Long entityId;
    private String entityName;
    private String status; // PENDING, IN_REVIEW, APPROVED, REJECTED
    private LocalDateTime createdAt;
    private LocalDateTime slaDeadline;
    private Integer slaHours;
    private Double timeRemaining; // horas
    private String urgency; // LOW, MEDIUM, HIGH, CRITICAL
}

/**
 * DTO para decisión HITL
 */
@Data
public class HitlDecisionDto {
    private Long id;
    private String type; // AGENT_APPROVAL, MODEL_DEPLOYMENT, PROMPT_REVIEW, HUMAN_OVERRIDE
    private String entityType; // Agent, Model, Prompt
    private Long entityId;
    private String entityName;
    private String decision; // APPROVED, REJECTED, MODIFIED
    private String decisionReason;
    private Double responseTime; // horas
    private LocalDateTime decisionDate;
    private String userId;
}

/**
 * Request DTO para registrar decisión
 */
@Data
public class HitlDecisionRequest {
    private Long interventionId;
    private String decision; // APPROVED, REJECTED, MODIFIED
    private String reason;
    private String userId;
}

/**
 * DTO para configuración de supervisión HITL
 */
@Data
public class HitlSupervisionConfigDto {
    private String type; // PRE_DEPLOYMENT, IN_LOOP, POST_DEPLOYMENT, OVERRIDE
    private Boolean enabled;
    private Integer slaHours;
    private List<String> requiredRoles;
    private Boolean autoEscalation;
    private Integer escalationHours;
}

