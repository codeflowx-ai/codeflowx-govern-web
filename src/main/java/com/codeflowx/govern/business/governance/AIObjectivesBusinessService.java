package com.codeflowx.govern.business.governance;

import com.codeflowx.govern.entity.governance.AIObjective;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.enartframework.nocode.datamodel.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de negocio para gestión de objetivos IA (ISO/IEC 42001 Clause 6.2).
 */
@Service
@Slf4j
public class AIObjectivesBusinessService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_ACHIEVED = "ACHIEVED";
    private static final String STATUS_REVISED = "REVISED";
    private static final String STATUS_DISCONTINUED = "DISCONTINUED";

    @Autowired
    private DAO dao;

    @PostConstruct
    void validateSeed() {
        try {
            Long total = dao.findBySQL(Long.class, "SELECT COUNT(1) FROM AIMOBJECTIVES");
            log.info("AIMOBJECTIVES rows existentes: {}", total);
        } catch (Exception e) {
            log.warn("No fue posible leer AIMOBJECTIVES: {}", e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<AIObjective> findAllObjectives() {
        String sql = "SELECT * FROM AIMOBJECTIVES ORDER BY AIOCATEGORY, AIOMETRIC";
        return Optional.ofNullable(dao.findListBySQL(AIObjective.class, sql))
            .orElse(Collections.emptyList());
    }

    @Transactional
    public AIObjective createObjective(AIObjective objective) {
        Objects.requireNonNull(objective, "objective es obligatorio");
        if (objective.getAiodescription() == null || objective.getAiodescription().isBlank()) {
            throw new IllegalArgumentException("El campo descripción es obligatorio");
        }
        if (objective.getAiocategory() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        if (objective.getAiometric() == null) {
            throw new IllegalArgumentException("La métrica es obligatoria");
        }
        if (objective.getAiotargetvalue() == null) {
            throw new IllegalArgumentException("El valor objetivo es obligatorio");
        }
        if (objective.getAiostatus() == null) {
            objective.setAiostatus(STATUS_ACTIVE);
        }
        if (objective.getAioresponsible() == null) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        if (objective.getAioreviewfrequency() == null) {
            throw new IllegalArgumentException("La frecuencia de revisión es obligatoria");
        }
        if (objective.getAiocurrentvalue() == null) {
            objective.setAiocurrentvalue(BigDecimal.ZERO);
        }

        dao.insert(objective);
        log.info("Objetivo IA creado id={} metric={}", objective.getIdxaiobjective(), objective.getAiometric());
        return objective;
    }

    @Transactional
    public AIObjective updateObjective(AIObjective updatedData) {
        if (updatedData == null || updatedData.getIdxaiobjective() == null) {
            throw new IllegalArgumentException("Se requiere un objetivo con ID para actualizar");
        }
        AIObjective current = dao.findById(AIObjective.class, updatedData.getIdxaiobjective());
        if (current == null) {
            throw new IllegalArgumentException("Objetivo no encontrado: " + updatedData.getIdxaiobjective());
        }
        if (updatedData.getAiodescription() == null || updatedData.getAiodescription().isBlank()) {
            throw new IllegalArgumentException("El campo descripción es obligatorio");
        }
        if (updatedData.getAiocategory() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        if (updatedData.getAiometric() == null) {
            throw new IllegalArgumentException("La métrica es obligatoria");
        }
        if (updatedData.getAiotargetvalue() == null) {
            throw new IllegalArgumentException("El valor objetivo es obligatorio");
        }
        if (updatedData.getAioresponsible() == null) {
            throw new IllegalArgumentException("El responsable es obligatorio");
        }
        if (updatedData.getAioreviewfrequency() == null) {
            throw new IllegalArgumentException("La frecuencia de revisión es obligatoria");
        }

        if (updatedData.getAiocurrentvalue() == null) {
            updatedData.setAiocurrentvalue(BigDecimal.ZERO);
        }

        current.setAiodescription(updatedData.getAiodescription());
        current.setAiocategory(updatedData.getAiocategory());
        current.setAiometric(updatedData.getAiometric());
        current.setAiotargetvalue(updatedData.getAiotargetvalue());
        current.setAiocurrentvalue(updatedData.getAiocurrentvalue());
        current.setAiostatus(updatedData.getAiostatus());
        current.setAioresponsible(updatedData.getAioresponsible());
        current.setAioreviewfrequency(updatedData.getAioreviewfrequency());
        current.setAiolastreviewed(updatedData.getAiolastreviewed());
        current.setAionextreview(updatedData.getAionextreview());
        current.setAiorelatedpolicy(updatedData.getAiorelatedpolicy());
        current.setAioupdatedat(Timestamp.from(Instant.now()));

        dao.update(current);
        return current;
    }

    @Transactional
    public AIObjective updateProgress(Long objectiveId, BigDecimal currentValue) {
        AIObjective objective = dao.findById(AIObjective.class, objectiveId);
        if (objective == null) {
            throw new IllegalArgumentException("Objetivo no encontrado: " + objectiveId);
        }
        if (currentValue == null) {
            throw new IllegalArgumentException("currentValue es obligatorio");
        }
        objective.setAiocurrentvalue(currentValue);
        objective.setAiolastreviewed(Timestamp.from(Instant.now()));

        if (objective.getAiotargetvalue() != null) {
            if (currentValue.compareTo(objective.getAiotargetvalue()) >= 0) {
                objective.setAiostatus(STATUS_ACHIEVED);
            } else if (!STATUS_REVISED.equals(objective.getAiostatus())
                    && !STATUS_DISCONTINUED.equals(objective.getAiostatus())) {
                objective.setAiostatus(STATUS_ACTIVE);
            }
        }

        dao.update(objective);
        return objective;
    }

    @Transactional
    public void deleteObjective(Long objectiveId) {
        if (objectiveId == null) {
            throw new IllegalArgumentException("objectiveId es obligatorio");
        }
        AIObjective objective = dao.findById(AIObjective.class, objectiveId);
        if (objective == null) {
            throw new IllegalArgumentException("Objetivo no encontrado: " + objectiveId);
        }
        dao.delete(objective);
        log.info("Objetivo IA eliminado id={}", objectiveId);
    }

    @Transactional
    public AIObjective reviewObjective(Long objectiveId, String decision, Timestamp nextReview) {
        AIObjective objective = dao.findById(AIObjective.class, objectiveId);
        if (objective == null) {
            throw new IllegalArgumentException("Objetivo no encontrado: " + objectiveId);
        }
        if (decision == null || decision.isBlank()) {
            throw new IllegalArgumentException("decision es obligatoria");
        }
        String normalizedDecision = decision.trim().toUpperCase();
        if (!normalizedDecision.equals(STATUS_ACTIVE)
            && !normalizedDecision.equals(STATUS_ACHIEVED)
            && !normalizedDecision.equals(STATUS_REVISED)
            && !normalizedDecision.equals(STATUS_DISCONTINUED)) {
            throw new IllegalArgumentException("Estado inválido: " + decision);
        }

        objective.setAiostatus(normalizedDecision);
        objective.setAiolastreviewed(Timestamp.from(Instant.now()));
        objective.setAionextreview(nextReview);
        dao.update(objective);
        return objective;
    }

    @Transactional(readOnly = true)
    public AIObjectivesReport generateObjectivesReport() {
        List<AIObjective> objectives = findAllObjectives();
        if (objectives.isEmpty()) {
            return AIObjectivesReport.empty();
        }

        long achieved = objectives.stream()
            .filter(obj -> STATUS_ACHIEVED.equalsIgnoreCase(obj.getAiostatus()))
            .count();

        BigDecimal avgProgress = objectives.stream()
            .filter(obj -> obj.getAiotargetvalue() != null && obj.getAiotargetvalue().compareTo(BigDecimal.ZERO) > 0)
            .map(obj -> {
                BigDecimal current = Optional.ofNullable(obj.getAiocurrentvalue()).orElse(BigDecimal.ZERO);
                return current.divide(obj.getAiotargetvalue(), 4, RoundingMode.HALF_UP);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(objectives.size()), 4, RoundingMode.HALF_UP);

        Map<String, Long> byCategory = objectives.stream()
            .collect(Collectors.groupingBy(AIObjective::getAiocategory, Collectors.counting()));

        List<AIObjective> alerts = objectives.stream()
            .filter(obj -> {
                if (obj.getAiotargetvalue() == null || obj.getAiotargetvalue().compareTo(BigDecimal.ZERO) == 0) {
                    return false;
                }
                BigDecimal current = Optional.ofNullable(obj.getAiocurrentvalue()).orElse(BigDecimal.ZERO);
                BigDecimal progress = current.divide(obj.getAiotargetvalue(), 4, RoundingMode.HALF_UP);
                return progress.compareTo(new BigDecimal("0.8")) < 0;
            })
            .sorted(Comparator.comparing(AIObjective::getAiocategory))
            .collect(Collectors.toList());

        return AIObjectivesReport.builder()
            .totalObjectives(objectives.size())
            .achievedObjectives((int) achieved)
            .activeObjectives((int) objectives.stream().filter(obj -> STATUS_ACTIVE.equalsIgnoreCase(obj.getAiostatus())).count())
            .revisedObjectives((int) objectives.stream().filter(obj -> STATUS_REVISED.equalsIgnoreCase(obj.getAiostatus())).count())
            .discontinuedObjectives((int) objectives.stream().filter(obj -> STATUS_DISCONTINUED.equalsIgnoreCase(obj.getAiostatus())).count())
            .averageProgress(avgProgress.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP))
            .objectivesByCategory(byCategory)
            .objectives(objectives)
            .objectivesBelowThreshold(alerts)
            .build();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class AIObjectivesReport {
        private final int totalObjectives;
        private final int achievedObjectives;
        private final int activeObjectives;
        private final int revisedObjectives;
        private final int discontinuedObjectives;
        private final BigDecimal averageProgress;
        private final Map<String, Long> objectivesByCategory;
        private final List<AIObjective> objectives;
        private final List<AIObjective> objectivesBelowThreshold;

        public static AIObjectivesReport empty() {
            return AIObjectivesReport.builder()
                .totalObjectives(0)
                .achievedObjectives(0)
                .activeObjectives(0)
                .revisedObjectives(0)
                .discontinuedObjectives(0)
                .averageProgress(BigDecimal.ZERO)
                .objectivesByCategory(Collections.emptyMap())
                .objectives(Collections.emptyList())
                .objectivesBelowThreshold(Collections.emptyList())
                .build();
        }
    }
}


