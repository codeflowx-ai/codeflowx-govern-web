package com.codeflowx.govern.business.compliance;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.entity.compliance.FriaAssessment;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * BusinessService para FRIA - Fundamental Rights Impact Assessment según Art. 27
 * 
 * Gestiona evaluaciones de impacto en derechos fundamentales con patrón EnArt
 */
@Service
@Slf4j
public class FriaAssessmentBusinessService {
    
    @Autowired
    private DAO dao;
    
    /**
     * Crea nueva FRIA
     */
    public FriaAssessment createFria(Long projectId, Long deployerUserId) {
        log.info("Creating FRIA for project: {}, deployer: {}", projectId, deployerUserId);
        
        FriaAssessment fria = new FriaAssessment();
        fria.setIdxproject(projectId);
        fria.setIdxuser(deployerUserId);
        fria.setFriaart27compliant(false);
        fria.setFriaapproved(false);
        fria.setFrianotified(false);
        fria.setFriadpiaintegrated(false);
        fria.setFriahitlenabled(false);
        fria.setFriavulnerablegroupsincluded(false);
        fria.setFriacreatedat(new Timestamp(System.currentTimeMillis()));
        fria.setIduuid(UUID.randomUUID().toString());
        
        dao.insert(fria);
        log.info("FRIA created with ID: {}", fria.getIdxfriaassessment());
        
        return fria;
    }
    
    /**
     * Actualiza sección específica de FRIA
     */
    public void updateFriaSection(Long friaId, String section, Object data) {
        log.info("Updating FRIA section: {} for ID: {}", section, friaId);
        
        FriaAssessment fria = dao.findById(FriaAssessment.class, friaId);
        if (fria == null) {
            throw new IllegalArgumentException("FRIA not found: " + friaId);
        }
        
        // Actualizar sección específica según parámetros
        switch (section) {
            case "PROCESS_DESCRIPTION":
                fria.setFriaprocessdescription((String) data);
                break;
            case "USAGE_PERIOD":
                fria.setFriausageperiod((String) data);
                break;
            case "USAGE_FREQUENCY":
                fria.setFriausagefrequency((String) data);
                break;
            // ... más secciones según necesidad
        }
        
        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        dao.update(fria);
    }
    
    /**
     * Calcula completeness score (0.00 - 1.00)
     * Verifica que los 6 elementos mandatorios Art. 27.1 estén completos
     */
    public BigDecimal calculateCompletenessScore(Long friaId) {
        log.info("Calculating completeness score for FRIA: {}", friaId);
        
        FriaAssessment fria = dao.findById(FriaAssessment.class, friaId);
        if (fria == null) {
            throw new IllegalArgumentException("FRIA not found: " + friaId);
        }
        
        int completedElements = 0;
        int totalElements = 6; // Art. 27.1 tiene 6 elementos mandatorios
        
        // Art. 27.1.a - Process description
        if (fria.getFriaprocessdescription() != null && !fria.getFriaprocessdescription().isEmpty()) {
            completedElements++;
        }
        
        // Art. 27.1.b - Usage period/frequency
        if (fria.getFriausageperiod() != null && fria.getFriausagefrequency() != null) {
            completedElements++;
        }
        
        // Art. 27.1.c - Affected categories
        if (fria.getFriaaffectedcategories() != null) {
            completedElements++;
        }
        
        // Art. 27.1.d - Risks
        if (fria.getFriarisks() != null) {
            completedElements++;
        }
        
        // Art. 27.1.e - Human oversight
        if (fria.getFriahumanoversight() != null && !fria.getFriahumanoversight().isEmpty()) {
            completedElements++;
        }
        
        // Art. 27.1.f - Mitigation measures
        if (fria.getFriamiti gationmeasures() != null) {
            completedElements++;
        }
        
        BigDecimal score = new BigDecimal(completedElements)
            .divide(new BigDecimal(totalElements), 2, BigDecimal.ROUND_HALF_UP);
        
        fria.setFriacompletenesscore(score);
        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        dao.update(fria);
        
        log.info("Completeness score calculated: {} ({}/{})", score, completedElements, totalElements);
        return score;
    }
    
    /**
     * Envía notificación a autoridad (Art. 27.3)
     */
    public void submitToAuthority(Long friaId) {
        log.info("Submitting FRIA to authority: {}", friaId);
        
        FriaAssessment fria = dao.findById(FriaAssessment.class, friaId);
        if (fria == null) {
            throw new IllegalArgumentException("FRIA not found: " + friaId);
        }
        
        // Verificar que esté completa
        BigDecimal completenessScore = calculateCompletenessScore(friaId);
        if (completenessScore.compareTo(new BigDecimal("1.00")) < 0) {
            throw new IllegalStateException("FRIA is not complete. Completeness: " + completenessScore);
        }
        
        // Marcar como notificada
        fria.setFrianotified(true);
        fria.setFrianotificationdate(new Timestamp(System.currentTimeMillis()));
        fria.setFrianotificationid(UUID.randomUUID().toString()); // ID temporal, reemplazar con ID real de autoridad
        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(fria);
        log.info("FRIA submitted to authority with notification ID: {}", fria.getFrianotificationid());
    }
    
    /**
     * Integra con DPIA (Art. 27.4)
     */
    public void integrateWithDpia(Long friaId, String dpiaId) {
        log.info("Integrating FRIA {} with DPIA {}", friaId, dpiaId);
        
        FriaAssessment fria = dao.findById(FriaAssessment.class, friaId);
        if (fria == null) {
            throw new IllegalArgumentException("FRIA not found: " + friaId);
        }
        
        fria.setFriadpiaintegrated(true);
        fria.setFriadpiaid(dpiaId);
        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(fria);
        log.info("FRIA integrated with DPIA successfully");
    }
    
    /**
     * Aprueba FRIA
     */
    public void approveFria(Long friaId, Long approverUserId) {
        log.info("Approving FRIA {} by user {}", friaId, approverUserId);
        
        FriaAssessment fria = dao.findById(FriaAssessment.class, friaId);
        if (fria == null) {
            throw new IllegalArgumentException("FRIA not found: " + friaId);
        }
        
        fria.setFriaapproved(true);
        fria.setFriaapprovedby(approverUserId);
        fria.setFriaapprovaldate(new Timestamp(System.currentTimeMillis()));
        fria.setFriaupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(fria);
        log.info("FRIA approved successfully");
    }
    
    /**
     * Obtiene FRIAs de un proyecto
     */
    public List<FriaAssessment> getFriasByProject(Long projectId) {
        String query = "SELECT * FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS WHERE IDXPROJECT = ? ORDER BY FRIACREATEDAT DESC";
        return dao.findListBySQL(FriaAssessment.class, query, projectId);
    }
    
    /**
     * Obtiene latest FRIA de proyecto
     */
    public FriaAssessment getLatestFria(Long projectId) {
        String query = "SELECT * FROM FRIAFUNDAMENTALRIGHTSASSESSMENTS WHERE IDXPROJECT = ? ORDER BY FRIACREATEDAT DESC LIMIT 1";
        return dao.findBySQL(FriaAssessment.class, query, projectId);
    }
}


