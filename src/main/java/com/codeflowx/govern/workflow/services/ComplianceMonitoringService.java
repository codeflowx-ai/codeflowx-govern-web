package com.codeflowx.govern.workflow.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.enartframework.orm.exception.DaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codeflowx.govern.entity.agents.Agent;
import com.codeflowx.govern.entity.governance.ComplianceAssessment;
import com.codeflowx.govern.entity.models.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import codeflowx.nocode.persist.BusinessService;
import codeflowx.nocode.persist.PageParams;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para monitorización continua de compliance en sistemas de IA
 * Coordina checks programados en múltiples entidades (Agents, Models, RAGs, Prompts)
 */
@Slf4j
@Service
public class ComplianceMonitoringService {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ComplianceCheckService complianceCheckService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Ejecuta compliance check programado en todos los sistemas activos
     * 
     * @return Map con resultados agregados del check
     */
    public Map<String, Object> executeScheduledCompliance() {
        log.info("Iniciando compliance check programado");
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> systemResults = new ArrayList<>();
        
        int totalChecked = 0;
        int compliant = 0;
        int nonCompliant = 0;
        int criticalIssues = 0;

        try {
            // 1. Check all active Agents
            log.info("Checking compliance for Agents...");
            List<Agent> agents = getActiveAgents();
            for (Agent agent : agents) {
                Map<String, Object> checkResult = checkSystemCompliance("AGENT", agent.getIdxagent());
                systemResults.add(checkResult);
                totalChecked++;
                
                if ((Boolean) checkResult.get("compliant")) {
                    compliant++;
                } else {
                    nonCompliant++;
                    Integer issues = (Integer) checkResult.get("criticalIssuesCount");
                    if (issues != null && issues > 0) {
                        criticalIssues += issues;
                    }
                }
            }
            
            // 2. Check all active Models
            log.info("Checking compliance for Models...");
            List<Model> models = getActiveModels();
            for (Model model : models) {
                Map<String, Object> checkResult = checkSystemCompliance("MODEL", model.getIdxmodel());
                systemResults.add(checkResult);
                totalChecked++;
                
                if ((Boolean) checkResult.get("compliant")) {
                    compliant++;
                } else {
                    nonCompliant++;
                    Integer issues = (Integer) checkResult.get("criticalIssuesCount");
                    if (issues != null && issues > 0) {
                        criticalIssues += issues;
                    }
                }
            }
            
            // 3. TODO: Add RAGs and Prompts when implemented
            
            // 4. Agregar resultados
            result.put("totalSystemsChecked", totalChecked);
            result.put("compliantSystems", compliant);
            result.put("nonCompliantSystems", nonCompliant);
            result.put("criticalIssues", criticalIssues);
            result.put("complianceRate", totalChecked > 0 ? (compliant * 100.0 / totalChecked) : 100.0);
            result.put("systemResults", systemResults);
            result.put("timestamp", System.currentTimeMillis());
            result.put("success", true);
            
            log.info("Compliance check completado - Total: {}, Compliant: {}, Non-Compliant: {}, Critical: {}",
                     totalChecked, compliant, nonCompliant, criticalIssues);
            
        } catch (Exception e) {
            log.error("Error ejecutando compliance check programado", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * Ejecuta compliance check en un sistema específico
     * 
     * @param entityType Tipo de entidad (AGENT, MODEL, RAG, PROMPT)
     * @param entityId ID de la entidad
     * @return Map con resultado del check
     */
    public Map<String, Object> checkSystemCompliance(String entityType, Long entityId) {
        log.info("Checking compliance for {} ID: {}", entityType, entityId);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. Ejecutar compliance check usando ComplianceCheckService
            ComplianceCheckService.ComplianceCheckResult checkResult;
            
            if ("AGENT".equals(entityType)) {
                checkResult = complianceCheckService.checkAgentCompliance(entityId);
            } else if ("MODEL".equals(entityType)) {
                checkResult = complianceCheckService.checkModelCompliance(entityId);
            } else {
                throw new IllegalArgumentException("Entity type not supported: " + entityType);
            }
            
            // 2. Crear registro de ComplianceAssessment en BBDD
            ComplianceAssessment assessment = new ComplianceAssessment();
            
            // Campos obligatorios
            assessment.setAssessmentname(entityType + " Compliance - ID: " + entityId);
            assessment.setComplianceframework("AI_GOVERNANCE");
            assessment.setStatus(checkResult.getStatus()); // COMPLIANT, NON_COMPLIANT, etc.
            assessment.setAssessmentdate(Timestamp.valueOf(LocalDateTime.now()));
            assessment.setAssessorname("SYSTEM_SCHEDULED");
            assessment.setAssessoremail("system@codeflowx.com");
            assessment.setCreatedat(Timestamp.valueOf(LocalDateTime.now()));
            assessment.setUpdatedat(Timestamp.valueOf(LocalDateTime.now()));
            
            // Campos opcionales
            String description = String.format("Compliance assessment for %s (ID: %d)\n", entityType, entityId);
            if (checkResult.getViolations() != null && !checkResult.getViolations().isEmpty()) {
                description += String.format("Violations: %d\n", checkResult.getViolations().size());
                for (ComplianceCheckService.ComplianceViolation v : checkResult.getViolations()) {
                    description += String.format("  - [%s] %s\n", v.getSeverity(), v.getDescription());
                }
            }
            if (checkResult.getAnalysis() != null && checkResult.getAnalysis().getRecommendations() != null) {
                description += "\nRecommendations:\n";
                for (String rec : checkResult.getAnalysis().getRecommendations()) {
                    description += "  - " + rec + "\n";
                }
            }
            assessment.setDescription(description);
            
            // Scores - calcular porcentaje de compliance
            int compliancePercentage = calculateCompliancePercentage(checkResult);
            assessment.setOverallscore(new java.math.BigDecimal(compliancePercentage));
            assessment.setCompliancepercentage(new java.math.BigDecimal(compliancePercentage));
            
            businessService.save(assessment);
            
            // 3. Preparar resultado
            result.put("entityType", entityType);
            result.put("entityId", entityId);
            result.put("assessmentId", assessment.getIdxcomplianceassessment());
            result.put("compliant", "COMPLIANT".equals(checkResult.getStatus()));
            result.put("complianceScore", compliancePercentage);
            result.put("criticalIssuesCount", countCriticalViolations(checkResult));
            result.put("findings", checkResult.getViolations());
            result.put("success", true);
            
        } catch (DaoException e) {
            log.error("Error de BBDD en compliance check para {} ID: {}", entityType, entityId, e);
            result.put("success", false);
            result.put("error", e.getMessage());
        } catch (Exception e) {
            log.error("Error en compliance check para {} ID: {}", entityType, entityId, e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * Cuenta violaciones críticas
     */
    private int countCriticalViolations(ComplianceCheckService.ComplianceCheckResult checkResult) {
        if (checkResult.getViolations() == null) {
            return 0;
        }
        
        return (int) checkResult.getViolations().stream()
            .filter(v -> "CRITICAL".equals(v.getSeverity()))
            .count();
    }

    /**
     * Calcula porcentaje de compliance (0-100)
     * COMPLIANT = 100, PARTIAL_COMPLIANCE = 70, NON_COMPLIANT = 30, NEEDS_REVIEW = 50
     */
    private int calculateCompliancePercentage(ComplianceCheckService.ComplianceCheckResult checkResult) {
        String status = checkResult.getStatus();
        
        if ("COMPLIANT".equals(status)) {
            return 100;
        } else if ("PARTIAL_COMPLIANCE".equals(status)) {
            return 70;
        } else if ("NEEDS_REVIEW".equals(status)) {
            return 50;
        } else if ("NON_COMPLIANT".equals(status)) {
            // Si hay violaciones, reducir más
            int violations = checkResult.getViolations() != null ? checkResult.getViolations().size() : 0;
            return Math.max(10, 30 - (violations * 5)); // Mínimo 10%
        }
        
        return 50; // Default
    }

    /**
     * Obtiene todos los agentes activos
     */
    private List<Agent> getActiveAgents() {
        try {
            String sql = "SELECT * FROM AGTAGENTS LIMIT 1000";
            List<Agent> agents = businessService.findByParams(Agent.class, sql, null);
            return agents != null ? agents : Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error obteniendo agentes activos", e);
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene todos los modelos activos
     */
    private List<Model> getActiveModels() {
        try {
            String sql = "SELECT * FROM MODMODELS LIMIT 1000";
            List<Model> models = businessService.findByParams(Model.class, sql, null);
            return models != null ? models : Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error obteniendo modelos activos", e);
            return Collections.emptyList();
        }
    }

    /**
     * Convierte objeto a JSON string
     */
    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error convirtiendo a JSON", e);
            return "{}";
        }
    }
}


