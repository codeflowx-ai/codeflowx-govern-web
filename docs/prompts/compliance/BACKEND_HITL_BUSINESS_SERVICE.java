package com.codeflowx.govern.business.compliance;

import com.codeflowx.govern.entity.compliance.HitlSupervision;
import com.codeflowx.govern.entity.compliance.HitlDecision;
import com.codeflowx.govern.repository.compliance.HitlSupervisionRepository;
import com.codeflowx.govern.repository.compliance.HitlDecisionRepository;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDashboardDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlInterventionDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDecisionDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlDecisionRequest;
import com.codeflowx.govern.nocode.dtos.compliance.HitlSupervisionConfigDto;
import com.codeflowx.govern.nocode.dtos.compliance.HitlMetricsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de Negocio para HITL Supervision (Art. 14 EU AI Act)
 *
 * Ubicación: codeflowx.govern.business/src/main/java/com/codeflowx/govern/business/compliance/
 *
 * Responsabilidades:
 * - Configurar supervisión HITL
 * - Obtener intervenciones pendientes
 * - Registrar decisiones humanas
 * - Calcular métricas HITL
 * - Gestionar configuración de supervisión
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HitlSupervisionBusinessService {

    private final HitlSupervisionRepository supervisionRepository;
    private final HitlDecisionRepository decisionRepository;

    /**
     * Obtiene el dashboard completo de HITL con métricas y datos consolidados
     */
    public HitlDashboardDto getDashboard(Long projectId) {
        log.info("Retrieving HITL dashboard for projectId: {}", projectId);

        try {
            // Obtener supervisiones
            List<HitlSupervision> supervisions = projectId != null
                ? supervisionRepository.findByProjectId(projectId)
                : supervisionRepository.findAll();

            // Obtener decisiones
            List<HitlDecision> allDecisions = decisionRepository.findAll();
            if (projectId != null) {
                // Filtrar decisiones por proyecto (a través de supervisiones)
                Set<Long> supervisionIds = supervisions.stream()
                    .map(HitlSupervision::getIdxhitlsupervision)
                    .collect(Collectors.toSet());
                allDecisions = allDecisions.stream()
                    .filter(d -> supervisionIds.contains(d.getIdxhitlsupervision()))
                    .collect(Collectors.toList());
            }

            // Calcular métricas
            HitlMetricsDto metrics = calculateMetrics(allDecisions, supervisions);

            // Obtener intervenciones pendientes
            List<HitlInterventionDto> pendingInterventions = getPendingInterventions(supervisions, allDecisions);

            // Obtener decisiones recientes
            List<HitlDecisionDto> recentDecisions = getRecentDecisions(allDecisions, 10);

            // Obtener configuración
            List<HitlSupervisionConfigDto> supervisionConfig = getSupervisionConfig(supervisions);

            // Construir DTO
            HitlDashboardDto dashboard = new HitlDashboardDto();
            dashboard.setMetrics(metrics);
            dashboard.setPendingInterventions(pendingInterventions);
            dashboard.setRecentDecisions(recentDecisions);
            dashboard.setSupervisionConfig(supervisionConfig);

            return dashboard;
        } catch (Exception e) {
            log.error("Error retrieving HITL dashboard", e);
            throw new RuntimeException("Error retrieving HITL dashboard", e);
        }
    }

    /**
     * Obtiene intervenciones HITL con filtros opcionales
     */
    public List<HitlInterventionDto> getInterventions(Long projectId, String status, String type) {
        log.info("Retrieving HITL interventions - projectId: {}, status: {}, type: {}",
            projectId, status, type);

        try {
            // Obtener supervisiones
            List<HitlSupervision> supervisions = projectId != null
                ? supervisionRepository.findByProjectId(projectId)
                : supervisionRepository.findAll();

            // Obtener decisiones
            List<HitlDecision> allDecisions = decisionRepository.findAll();

            // Filtrar por proyecto si es necesario
            if (projectId != null) {
                Set<Long> supervisionIds = supervisions.stream()
                    .map(HitlSupervision::getIdxhitlsupervision)
                    .collect(Collectors.toSet());
                allDecisions = allDecisions.stream()
                    .filter(d -> supervisionIds.contains(d.getIdxhitlsupervision()))
                    .collect(Collectors.toList());
            }

            // Obtener intervenciones pendientes
            List<HitlInterventionDto> interventions = getPendingInterventions(supervisions, allDecisions);

            // Aplicar filtros
            if (status != null && !status.equals("ALL")) {
                interventions = interventions.stream()
                    .filter(i -> i.getStatus().equals(status))
                    .collect(Collectors.toList());
            }

            if (type != null && !type.equals("ALL")) {
                interventions = interventions.stream()
                    .filter(i -> i.getEntityType().equals(type))
                    .collect(Collectors.toList());
            }

            return interventions;
        } catch (Exception e) {
            log.error("Error retrieving HITL interventions", e);
            throw new RuntimeException("Error retrieving HITL interventions", e);
        }
    }

    /**
     * Registra una decisión humana para una intervención HITL
     */
    @Transactional
    public HitlDecisionDto recordDecision(HitlDecisionRequest request) {
        log.info("Recording HITL decision for interventionId: {}", request.getInterventionId());

        try {
            // Buscar la supervisión relacionada
            // Nota: En una implementación real, necesitarías una relación más clara
            // entre intervenciones y supervisiones. Por ahora, asumimos que el interventionId
            // corresponde a un ID de supervisión o entidad.

            // Crear nueva decisión
            HitlDecision decision = new HitlDecision();
            decision.setIdxhitlsupervision(request.getInterventionId()); // Ajustar según modelo real
            decision.setHitlentitytype(determineEntityType(request.getInterventionId()));
            decision.setIdxentity(request.getInterventionId());
            decision.setHitldecision(request.getDecision());
            decision.setHitldecisionreason(request.getReason());
            decision.setHitldecisiondate(new Timestamp(System.currentTimeMillis()));
            decision.setHitluserid(request.getUserId() != null ? request.getUserId() : "system");

            // Calcular tiempo de respuesta
            // Nota: Necesitarías obtener la fecha de creación de la intervención
            // Por ahora, usamos un cálculo simplificado
            HitlSupervision supervision = supervisionRepository.findById(request.getInterventionId())
                .orElseThrow(() -> new RuntimeException("Supervision not found"));

            long minutes = ChronoUnit.MINUTES.between(
                supervision.getHitlcreatedat().toLocalDateTime(),
                LocalDateTime.now()
            );
            decision.setHitlresponsetime((int) minutes);

            // Generar UUID
            decision.setIduuid(UUID.randomUUID().toString());

            // Guardar decisión
            HitlDecision savedDecision = decisionRepository.save(decision);

            // Convertir a DTO
            return toDecisionDto(savedDecision);
        } catch (Exception e) {
            log.error("Error recording HITL decision", e);
            throw new RuntimeException("Error recording HITL decision", e);
        }
    }

    /**
     * Obtiene la configuración de supervisión HITL
     */
    public List<HitlSupervisionConfigDto> getSupervisionConfig(Long projectId) {
        log.info("Retrieving HITL supervision config for projectId: {}", projectId);

        try {
            List<HitlSupervision> supervisions = projectId != null
                ? supervisionRepository.findByProjectId(projectId)
                : supervisionRepository.findAll();

            return getSupervisionConfig(supervisions);
        } catch (Exception e) {
            log.error("Error retrieving HITL supervision config", e);
            throw new RuntimeException("Error retrieving HITL supervision config", e);
        }
    }

    /**
     * Actualiza la configuración de supervisión HITL
     */
    @Transactional
    public HitlSupervisionConfigDto updateSupervisionConfig(HitlSupervisionConfigDto configDto) {
        log.info("Updating HITL supervision config for type: {}", configDto.getType());

        try {
            // Buscar supervisión por tipo
            List<HitlSupervision> supervisions = supervisionRepository.findAll();
            HitlSupervision supervision = supervisions.stream()
                .filter(s -> s.getHitlsupervisiontype().equals(configDto.getType()))
                .findFirst()
                .orElse(null);

            if (supervision == null) {
                // Crear nueva supervisión si no existe
                supervision = new HitlSupervision();
                supervision.setHitlsupervisiontype(configDto.getType());
                supervision.setIduuid(UUID.randomUUID().toString());
                supervision.setHitlcreatedat(new Timestamp(System.currentTimeMillis()));
            }

            // Actualizar configuración
            supervision.setHitlsla(configDto.getSlaHours());
            // Nota: enabled, requiredRoles, autoEscalation, escalationHours
            // deberían almacenarse en HITLCONFIGURATION (JSONB)
            // Por simplicidad, asumimos que se actualiza el JSONB

            HitlSupervision saved = supervisionRepository.save(supervision);

            return toConfigDto(saved);
        } catch (Exception e) {
            log.error("Error updating HITL supervision config", e);
            throw new RuntimeException("Error updating HITL supervision config", e);
        }
    }

    // ========== MÉTODOS PRIVADOS ==========

    /**
     * Calcula métricas HITL
     */
    private HitlMetricsDto calculateMetrics(List<HitlDecision> decisions, List<HitlSupervision> supervisions) {
        HitlMetricsDto metrics = new HitlMetricsDto();

        if (decisions.isEmpty()) {
            metrics.setAverageResponseTime(0.0);
            metrics.setApprovalRate(0.0);
            metrics.setSlaCompliance(0.0);
            metrics.setPendingInterventions(0);
            metrics.setTotalInterventions(0);
            metrics.setInterventionsByType(new HashMap<>());
            metrics.setInterventionsByStatus(new HashMap<>());
            return metrics;
        }

        // Tiempo promedio de respuesta (en horas)
        double avgResponseTime = decisions.stream()
            .filter(d -> d.getHitlresponsetime() != null)
            .mapToInt(HitlDecision::getHitlresponsetime)
            .average()
            .orElse(0.0) / 60.0; // Convertir minutos a horas

        // Tasa de aprobación
        long approved = decisions.stream()
            .filter(d -> "APPROVED".equals(d.getHitldecision()))
            .count();
        double approvalRate = (double) approved / decisions.size();

        // SLA cumplimiento
        // Obtener SLA promedio de supervisiones
        int avgSlaHours = supervisions.stream()
            .filter(s -> s.getHitlsla() != null)
            .mapToInt(HitlSupervision::getHitlsla)
            .average()
            .mapToInt(d -> (int) d)
            .orElse(4); // Default 4 horas

        long slaCompliant = decisions.stream()
            .filter(d -> d.getHitlresponsetime() != null)
            .filter(d -> d.getHitlresponsetime() <= (avgSlaHours * 60))
            .count();
        double slaCompliance = (double) slaCompliant / decisions.size();

        // Intervenciones pendientes (supervisiones sin decisión)
        Set<Long> decidedSupervisionIds = decisions.stream()
            .map(HitlDecision::getIdxhitlsupervision)
            .collect(Collectors.toSet());
        long pendingInterventions = supervisions.stream()
            .filter(s -> !decidedSupervisionIds.contains(s.getIdxhitlsupervision()))
            .count();

        // Intervenciones por tipo
        Map<String, Integer> byType = decisions.stream()
            .collect(Collectors.groupingBy(
                HitlDecision::getHitlentitytype,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));

        // Intervenciones por estado
        Map<String, Integer> byStatus = decisions.stream()
            .collect(Collectors.groupingBy(
                HitlDecision::getHitldecision,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));

        metrics.setAverageResponseTime(avgResponseTime);
        metrics.setApprovalRate(approvalRate);
        metrics.setSlaCompliance(slaCompliance);
        metrics.setPendingInterventions((int) pendingInterventions);
        metrics.setTotalInterventions(decisions.size());
        metrics.setInterventionsByType(byType);
        metrics.setInterventionsByStatus(byStatus);

        return metrics;
    }

    /**
     * Obtiene intervenciones pendientes
     */
    private List<HitlInterventionDto> getPendingInterventions(
            List<HitlSupervision> supervisions,
            List<HitlDecision> decisions) {

        Set<Long> decidedSupervisionIds = decisions.stream()
            .map(HitlDecision::getIdxhitlsupervision)
            .collect(Collectors.toSet());

        return supervisions.stream()
            .filter(s -> !decidedSupervisionIds.contains(s.getIdxhitlsupervision()))
            .map(this::toInterventionDto)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene decisiones recientes
     */
    private List<HitlDecisionDto> getRecentDecisions(List<HitlDecision> decisions, int limit) {
        return decisions.stream()
            .sorted(Comparator.comparing(HitlDecision::getHitldecisiondate).reversed())
            .limit(limit)
            .map(this::toDecisionDto)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene configuración de supervisión
     */
    private List<HitlSupervisionConfigDto> getSupervisionConfig(List<HitlSupervision> supervisions) {
        return supervisions.stream()
            .map(this::toConfigDto)
            .collect(Collectors.toList());
    }

    /**
     * Convierte entidad a DTO de intervención
     */
    private HitlInterventionDto toInterventionDto(HitlSupervision supervision) {
        HitlInterventionDto dto = new HitlInterventionDto();
        dto.setId(supervision.getIdxhitlsupervision());
        dto.setType(supervision.getHitlsupervisiontype());
        // Nota: entityType, entityId, entityName deberían venir de la relación
        // Por ahora, usamos valores por defecto
        dto.setEntityType("AGENT"); // Ajustar según modelo real
        dto.setEntityId(supervision.getIdxproject());
        dto.setEntityName("Entity " + supervision.getIdxproject());
        dto.setStatus("PENDING");
        dto.setCreatedAt(supervision.getHitlcreatedat().toLocalDateTime());

        // Calcular SLA deadline
        LocalDateTime deadline = supervision.getHitlcreatedat().toLocalDateTime()
            .plusHours(supervision.getHitlsla() != null ? supervision.getHitlsla() : 4);
        dto.setSlaDeadline(deadline);
        dto.setSlaHours(supervision.getHitlsla() != null ? supervision.getHitlsla() : 4);

        // Calcular tiempo restante
        long hoursRemaining = ChronoUnit.HOURS.between(LocalDateTime.now(), deadline);
        dto.setTimeRemaining((double) hoursRemaining);

        // Determinar urgencia
        if (hoursRemaining < 1) {
            dto.setUrgency("CRITICAL");
        } else if (hoursRemaining < 2) {
            dto.setUrgency("HIGH");
        } else if (hoursRemaining < 4) {
            dto.setUrgency("MEDIUM");
        } else {
            dto.setUrgency("LOW");
        }

        return dto;
    }

    /**
     * Convierte entidad a DTO de decisión
     */
    private HitlDecisionDto toDecisionDto(HitlDecision decision) {
        HitlDecisionDto dto = new HitlDecisionDto();
        dto.setId(decision.getIdxhitldecision());
        dto.setType(decision.getHitlentitytype());
        dto.setEntityType(decision.getHitlentitytype());
        dto.setEntityId(decision.getIdxentity());
        dto.setEntityName("Entity " + decision.getIdxentity());
        dto.setDecision(decision.getHitldecision());
        dto.setDecisionReason(decision.getHitldecisionreason());
        dto.setResponseTime(decision.getHitlresponsetime() != null
            ? decision.getHitlresponsetime() / 60.0
            : 0.0);
        dto.setDecisionDate(decision.getHitldecisiondate().toLocalDateTime());
        dto.setUserId(decision.getHitluserid());
        return dto;
    }

    /**
     * Convierte entidad a DTO de configuración
     */
    private HitlSupervisionConfigDto toConfigDto(HitlSupervision supervision) {
        HitlSupervisionConfigDto dto = new HitlSupervisionConfigDto();
        dto.setType(supervision.getHitlsupervisiontype());
        // Nota: enabled, requiredRoles, autoEscalation, escalationHours
        // deberían parsearse del JSONB HITLCONFIGURATION
        // Por simplicidad, usamos valores por defecto
        dto.setEnabled(true);
        dto.setSlaHours(supervision.getHitlsla() != null ? supervision.getHitlsla() : 4);
        dto.setRequiredRoles(Arrays.asList("governance-admin", "compliance-officer"));
        dto.setAutoEscalation(true);
        dto.setEscalationHours(12);
        return dto;
    }

    /**
     * Determina el tipo de entidad basado en el ID
     * Nota: Esto debería venir de la relación real en la base de datos
     */
    private String determineEntityType(Long interventionId) {
        // Lógica simplificada - en producción debería consultar la relación real
        return "AGENT";
    }
}

