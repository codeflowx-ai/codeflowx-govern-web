package com.codeflowx.govern.repository.compliance;

import com.codeflowx.govern.entity.compliance.HitlSupervision;
import com.codeflowx.govern.entity.compliance.HitlDecision;
import com.codeflowx.govern.repository.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorios para HITL Supervision
 *
 * Ubicación: codeflowx.govern.repository/src/main/java/com/codeflowx/govern/repository/compliance/
 */

/**
 * Repositorio para HitlSupervision
 * Tabla: GOVHITLSUPERVISIONS
 */
@Repository
public interface HitlSupervisionRepository extends GenericRepository<HitlSupervision, Long> {

    /**
     * Busca supervisiones por proyecto
     */
    @Query("SELECT h FROM HitlSupervision h WHERE h.idxproject = :projectId")
    List<HitlSupervision> findByProjectId(@Param("projectId") Long projectId);

    /**
     * Busca supervisiones por tipo
     */
    @Query("SELECT h FROM HitlSupervision h WHERE h.hitlsupervisiontype = :type")
    List<HitlSupervision> findByType(@Param("type") String type);

    /**
     * Busca supervisiones activas (con SLA no vencido)
     */
    @Query("SELECT h FROM HitlSupervision h WHERE h.hitlsla IS NOT NULL")
    List<HitlSupervision> findActiveSupervisions();
}

/**
 * Repositorio para HitlDecision
 * Tabla: GOVHITLDECISIONS
 */
@Repository
public interface HitlDecisionRepository extends GenericRepository<HitlDecision, Long> {

    /**
     * Busca decisiones por supervisión
     */
    @Query("SELECT d FROM HitlDecision d WHERE d.idxhitlsupervision = :supervisionId")
    List<HitlDecision> findBySupervisionId(@Param("supervisionId") Long supervisionId);

    /**
     * Busca decisiones por tipo de entidad
     */
    @Query("SELECT d FROM HitlDecision d WHERE d.hitlentitytype = :entityType")
    List<HitlDecision> findByEntityType(@Param("entityType") String entityType);

    /**
     * Busca decisiones por decisión (APPROVED, REJECTED, MODIFIED)
     */
    @Query("SELECT d FROM HitlDecision d WHERE d.hitldecision = :decision")
    List<HitlDecision> findByDecision(@Param("decision") String decision);

    /**
     * Busca decisiones por usuario
     */
    @Query("SELECT d FROM HitlDecision d WHERE d.hitluserid = :userId")
    List<HitlDecision> findByUserId(@Param("userId") String userId);

    /**
     * Busca decisiones recientes ordenadas por fecha
     */
    @Query("SELECT d FROM HitlDecision d ORDER BY d.hitldecisiondate DESC")
    List<HitlDecision> findRecentDecisions();
}

