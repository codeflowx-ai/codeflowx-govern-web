package com.codeflowx.govern.business.compliance;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.entity.compliance.ComplianceAssessment;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * BusinessService para Compliance Assessments según EU AI Act Anexo VI (Art. 43)
 * 
 * Gestiona evaluaciones de conformidad con patrón EnArt (DAO, NO Repository)
 */
@Service
@Slf4j
public class ComplianceAssessmentBusinessService {
    
    @Autowired
    private DAO dao;
    
    @Autowired
    private QualityManagementSystemBusinessService qmsBusinessService;
    
    @Autowired
    private TechnicalDocumentationBusinessService docBusinessService;
    
    /**
     * Crea nueva evaluación de conformidad
     */
    public ComplianceAssessment createAssessment(Long projectId, String assessmentType) {
        log.info("Creating compliance assessment for project: {}, type: {}", projectId, assessmentType);
        
        ComplianceAssessment assessment = new ComplianceAssessment();
        assessment.setIdxproject(projectId);
        assessment.setComassessmenttype(assessmentType);
        assessment.setComassessmentdate(new Timestamp(System.currentTimeMillis()));
        assessment.setComannexvicompliant(false);
        assessment.setComreadyforcertification(false);
        assessment.setComcreatedat(new Timestamp(System.currentTimeMillis()));
        assessment.setIduuid(UUID.randomUUID().toString());
        
        dao.insert(assessment);
        log.info("Compliance assessment created with ID: {}", assessment.getIdxcomplianceassessment());
        
        return assessment;
    }
    
    /**
     * Ejecuta step 2 - QMS compliance check
     */
    public void executeStep2QmsCheck(Long assessmentId) {
        log.info("Executing Step 2 QMS check for assessment: {}", assessmentId);
        
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment not found: " + assessmentId);
        }
        
        BigDecimal qmsScore = qmsBusinessService.calculateQmsComplianceScore(assessment.getIdxproject());
        assessment.setComstep2qmsscore(qmsScore);
        assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(assessment);
        log.info("Step 2 QMS check completed with score: {}", qmsScore);
    }
    
    /**
     * Ejecuta step 3 - Technical doc review
     */
    public void executeStep3DocReview(Long assessmentId) {
        log.info("Executing Step 3 Doc review for assessment: {}", assessmentId);
        
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment not found: " + assessmentId);
        }
        
        BigDecimal docScore = docBusinessService.calculateDocumentationScore(assessment.getIdxproject());
        assessment.setComstep3docscore(docScore);
        assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(assessment);
        log.info("Step 3 Doc review completed with score: {}", docScore);
    }
    
    /**
     * Ejecuta step 4 - Process consistency check
     */
    public void executeStep4ConsistencyCheck(Long assessmentId) {
        log.info("Executing Step 4 Consistency check for assessment: {}", assessmentId);
        
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment not found: " + assessmentId);
        }
        
        // Implementar lógica de consistency check
        BigDecimal consistencyScore = calculateConsistencyScore(assessment);
        assessment.setComstep4consistencyscore(consistencyScore);
        assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(assessment);
        log.info("Step 4 Consistency check completed with score: {}", consistencyScore);
    }
    
    /**
     * Calcula score overall (promedio de los 3 steps)
     */
    public BigDecimal calculateOverallScore(Long assessmentId) {
        log.info("Calculating overall score for assessment: {}", assessmentId);
        
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        if (assessment == null) {
            throw new IllegalArgumentException("Assessment not found: " + assessmentId);
        }
        
        if (assessment.getComstep2qmsscore() == null || 
            assessment.getComstep3docscore() == null || 
            assessment.getComstep4consistencyscore() == null) {
            log.warn("Cannot calculate overall score - some steps not completed");
            return null;
        }
        
        BigDecimal avg = assessment.getComstep2qmsscore()
            .add(assessment.getComstep3docscore())
            .add(assessment.getComstep4consistencyscore())
            .divide(new BigDecimal(3), 2, BigDecimal.ROUND_HALF_UP);
        
        assessment.setComoverallscore(avg);
        assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(assessment);
        log.info("Overall score calculated: {}", avg);
        
        return avg;
    }
    
    /**
     * Determina si ready for certification
     */
    public Boolean isReadyForCertification(Long assessmentId) {
        ComplianceAssessment assessment = dao.findById(ComplianceAssessment.class, assessmentId);
        if (assessment == null) {
            return false;
        }
        
        // Ready si: overall score >= 0.80 y Annex VI compliant
        BigDecimal overallScore = assessment.getComoverallscore();
        Boolean annexCompliant = assessment.getComannexvicompliant();
        
        boolean ready = overallScore != null && 
                       overallScore.compareTo(new BigDecimal("0.80")) >= 0 &&
                       Boolean.TRUE.equals(annexCompliant);
        
        assessment.setComreadyforcertification(ready);
        assessment.setComupdatedat(new Timestamp(System.currentTimeMillis()));
        dao.update(assessment);
        
        return ready;
    }
    
    /**
     * Obtiene latest assessment de proyecto
     */
    public ComplianceAssessment getLatestAssessment(Long projectId) {
        String query = "SELECT * FROM COMCOMPLIANCEASSESSMENTS WHERE IDXPROJECT = ? ORDER BY COMASSESSMENTDATE DESC LIMIT 1";
        return dao.findBySQL(ComplianceAssessment.class, query, projectId);
    }
    
    /**
     * Obtiene todos los assessments de un proyecto
     */
    public List<ComplianceAssessment> getAssessmentsByProject(Long projectId) {
        String query = "SELECT * FROM COMCOMPLIANCEASSESSMENTS WHERE IDXPROJECT = ? ORDER BY COMASSESSMENTDATE DESC";
        return dao.findListBySQL(ComplianceAssessment.class, query, projectId);
    }
    
    /**
     * Calcula consistency score (implementación simplificada)
     */
    private BigDecimal calculateConsistencyScore(ComplianceAssessment assessment) {
        // Implementar lógica real según requerimientos
        // Por ahora, retorna 0.85 como ejemplo
        return new BigDecimal("0.85");
    }
}


