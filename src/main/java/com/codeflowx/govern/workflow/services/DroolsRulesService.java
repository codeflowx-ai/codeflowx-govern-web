package com.codeflowx.govern.workflow.services;

import java.util.List;

import javax.annotation.PostConstruct;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Servicio para ejecutar reglas Drools
 * Gestiona scoring, decisiones y clasificaciones sin hardcode
 */
@Slf4j
@Service
public class DroolsRulesService {

    private KieContainer kieContainer;

    @PostConstruct
    public void init() {
        try {
            log.info("🔧 Inicializando Drools Rules Engine...");
            
            KieServices kieServices = KieServices.Factory.get();
            kieContainer = kieServices.getKieClasspathContainer();
            
            log.info("✅ Drools Rules Engine inicializado correctamente");
            
        } catch (Exception e) {
            log.error("❌ Error inicializando Drools", e);
            throw new RuntimeException("Failed to initialize Drools: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta reglas para Agent Approval
     */
    public void executeAgentApprovalRules(Object fact) {
        executeRules("agent-approval-session", fact);
    }

    /**
     * Ejecuta reglas para Model Approval
     */
    public void executeModelApprovalRules(Object fact) {
        executeRules("model-approval-session", fact);
    }

    /**
     * Ejecuta reglas para LLM Evaluation
     */
    public void executeLlmEvaluationRules(Object fact) {
        executeRules("llm-evaluation-session", fact);
    }

    /**
     * Ejecuta reglas para Bias Detection
     */
    public void executeBiasDetectionRules(Object fact) {
        executeRules("bias-detection-session", fact);
    }

    /**
     * Ejecuta reglas para Drift Detection
     */
    public void executeDriftDetectionRules(Object fact) {
        executeRules("drift-detection-session", fact);
    }

    /**
     * Ejecuta reglas para Alert Classification
     */
    public void executeAlertClassificationRules(Object fact) {
        executeRules("alert-classification-session", fact);
    }

    /**
     * Ejecuta reglas para Dataset Quality Governance
     */
    public void executeDatasetQualityRules(Object fact) {
        executeRules("dataset-quality-session", fact);
    }

    /**
     * Método genérico para ejecutar cualquier sesión de reglas
     */
    public void executeRules(String sessionName, Object fact) {
        try {
            log.info("🔄 Ejecutando reglas Drools: session={}", sessionName);
            
            StatelessKieSession kieSession = kieContainer.newStatelessKieSession(sessionName);
            
            // Añadir logger como global
            kieSession.setGlobal("logger", log);
            
            // Ejecutar reglas
            kieSession.execute(fact);
            
            log.info("✅ Reglas ejecutadas correctamente");
            
        } catch (Exception e) {
            log.error("❌ Error ejecutando reglas Drools: {}", e.getMessage());
            throw new RuntimeException("Error executing Drools rules: " + e.getMessage(), e);
        }
    }

    /**
     * Ejecuta reglas con múltiples facts
     */
    public void executeRulesWithMultipleFacts(String sessionName, List<Object> facts) {
        try {
            log.info("🔄 Ejecutando reglas Drools con {} facts", facts.size());
            
            StatelessKieSession kieSession = kieContainer.newStatelessKieSession(sessionName);
            kieSession.setGlobal("logger", log);
            
            kieSession.execute(facts);
            
            log.info("✅ Reglas ejecutadas con múltiples facts");
            
        } catch (Exception e) {
            log.error("❌ Error ejecutando reglas: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage(), e);
        }
    }

    /**
     * Valida que las reglas se cargaron correctamente
     */
    public boolean validateRulesLoaded() {
        try {
            // Intentar obtener cada sesión
            String[] sessions = {
                "agent-approval-session",
                "model-approval-session",
                "llm-evaluation-session",
                "bias-detection-session",
                "drift-detection-session",
                "alert-classification-session"
            };
            
            for (String session : sessions) {
                kieContainer.newStatelessKieSession(session);
            }
            
            log.info("✅ Todas las sesiones Drools validadas correctamente");
            return true;
            
        } catch (Exception e) {
            log.error("❌ Error validando sesiones Drools: {}", e.getMessage());
            return false;
        }
    }
}


