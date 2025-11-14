package com.codeflowx.govern.business.governance;

import com.codeflowx.govern.entity.governance.AICCompetence;
import com.codeflowx.govern.entity.governance.AITTrainingRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.enartframework.nocode.datamodel.dao.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio de negocio para competencias y awareness (ISO/IEC 42001 Clauses 7.2/7.3).
 */
@Service
@Slf4j
public class AICompetenceBusinessService {

    private static final String DEFAULT_AWARENESS_LEVEL = "NONE";
    private static final List<String> AWARENESS_LEVELS = List.of("NONE", "BASIC", "INTERMEDIATE", "ADVANCED");

    @Autowired
    private DAO dao;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    void logCounts() {
        try {
            Long count = dao.findBySQL(Long.class, "SELECT COUNT(1) FROM AICCOMPETENCE");
            log.info("ISO 42001 competence records: {}", count);
        } catch (Exception ex) {
            log.warn("No fue posible contar AICCOMPETENCE: {}", ex.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // CRUD Básico
    // -------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public List<AICCompetence> findAllCompetences() {
        String sql = "SELECT * FROM AICCOMPETENCE ORDER BY AICROLE, AICPERSONNAME";
        return Optional.ofNullable(dao.findListBySQL(AICCompetence.class, sql))
            .orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<AITTrainingRecord> findTrainingRecords(Long competenceId) {
        String sql = "SELECT * FROM AITTRAININGRECORD WHERE AITPERSONID = ? ORDER BY AITTRAININGDATE DESC";
        return Optional.ofNullable(dao.findListBySQL(AITTrainingRecord.class, sql, competenceId))
            .orElse(Collections.emptyList());
    }

    // -------------------------------------------------------------------------
    // Gap Analysis
    // -------------------------------------------------------------------------
    @Transactional
    public GapAnalysisResult assessCompetenceGap(Long competenceId) {
        AICCompetence competence = requireCompetence(competenceId);

        Set<String> required = toSet(competence.getAicrequiredcompetencies());
        Set<String> current = toSet(competence.getAiccurrentcompetencies());

        LinkedHashSet<String> missing = new LinkedHashSet<>(required);
        missing.removeAll(current);

        LinkedHashSet<String> extras = new LinkedHashSet<>(current);
        extras.removeAll(required);

        ObjectNode analysisNode = objectMapper.createObjectNode();
        analysisNode.set("missing", toArrayNode(missing));
        analysisNode.set("extras", toArrayNode(extras));

        competence.setAicgapanalysis(writeValueAsString(analysisNode));
        competence.setAicupdatedat(Timestamp.from(Instant.now()));
        dao.update(competence);

        return GapAnalysisResult.builder()
            .competence(competence)
            .missing(List.copyOf(missing))
            .extra(List.copyOf(extras))
            .build();
    }

    // -------------------------------------------------------------------------
    // Plan de formación
    // -------------------------------------------------------------------------
    @Transactional
    public AICCompetence createTrainingPlan(Long competenceId, List<String> gaps, String owner, Timestamp targetDate) {
        AICCompetence competence = requireCompetence(competenceId);
        List<String> missing = gaps != null ? gaps : Collections.emptyList();

        ObjectNode planNode = objectMapper.createObjectNode();
        planNode.put("generatedAt", Instant.now().toString());
        planNode.put("owner", owner != null ? owner : competence.getAicpersonname());
        if (targetDate != null) {
            planNode.put("targetDate", targetDate.toInstant().toString());
        }
        planNode.set("gaps", toArrayNode(new LinkedHashSet<>(missing)));

        ArrayNode actions = objectMapper.createArrayNode();
        for (String gap : missing) {
            ObjectNode action = objectMapper.createObjectNode();
            action.put("competency", gap);
            action.put("status", "PLANNED");
            action.put("expectedOutcome", "Cerrar brecha en " + gap);
            actions.add(action);
        }
        planNode.set("actions", actions);

        competence.setAictrainingplan(writeValueAsString(planNode));
        competence.setAicupdatedat(Timestamp.from(Instant.now()));
        dao.update(competence);
        return competence;
    }

    // -------------------------------------------------------------------------
    // Registro formación
    // -------------------------------------------------------------------------
    @Transactional
    public AITTrainingRecord recordTraining(Long competenceId, AITTrainingRecord record) {
        Objects.requireNonNull(record, "trainingRecord es obligatorio");
        AICCompetence competence = requireCompetence(competenceId);

        if (record.getAittrainingdate() == null) {
            record.setAittrainingdate(Timestamp.from(Instant.now()));
        }
        record.setCompetence(competence);
        record.setAitcreatedat(Timestamp.from(Instant.now()));
        record.setAitupdatedat(record.getAitcreatedat());

        dao.insert(record);

        // Actualizar awareness
        competence.setAiclastawarenesstrained(record.getAittrainingdate());
        if ("AWARENESS".equalsIgnoreCase(record.getAittrainingtype())) {
            increaseAwarenessLevel(competence);
        }
        dao.update(competence);

        return record;
    }

    // -------------------------------------------------------------------------
    // Reporte
    // -------------------------------------------------------------------------
    @Transactional(readOnly = true)
    public CompetenceDashboard generateCompetenceReport() {
        List<AICCompetence> competences = findAllCompetences();
        if (competences.isEmpty()) {
            return CompetenceDashboard.empty();
        }

        Map<String, Long> byRole = competences.stream()
            .collect(Collectors.groupingBy(AICCompetence::getAicrole, Collectors.counting()));

        Map<String, Long> byAwareness = competences.stream()
            .collect(Collectors.groupingBy(c -> Optional.ofNullable(c.getAicawarenesslevel()).orElse(DEFAULT_AWARENESS_LEVEL),
                Collectors.counting()));

        Map<String, Long> missingCounts = competences.stream()
            .map(c -> toSetFromJsonArray(fetchNodeArray(c.getAicgapanalysis(), "missing")))
            .filter(set -> !set.isEmpty())
            .flatMap(Set::stream)
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        long totalTrainings = competences.stream()
            .map(c -> findTrainingRecords(c.getIdxaiccompetence()))
            .mapToLong(List::size)
            .sum();

        return CompetenceDashboard.builder()
            .totalPeople(competences.size())
            .byRole(byRole)
            .byAwareness(byAwareness)
            .openGaps(missingCounts)
            .trainingSessions(totalTrainings)
            .build();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    private AICCompetence requireCompetence(Long competenceId) {
        if (competenceId == null) {
            throw new IllegalArgumentException("competenceId es obligatorio");
        }
        AICCompetence competence = dao.findById(AICCompetence.class, competenceId);
        if (competence == null) {
            throw new IllegalArgumentException("Competence no encontrada: " + competenceId);
        }
        if (competence.getAicawarenesslevel() == null) {
            competence.setAicawarenesslevel(DEFAULT_AWARENESS_LEVEL);
        }
        return competence;
    }

    private Set<String> toSet(String json) {
        if (StringUtils.isBlank(json)) {
            return new LinkedHashSet<>();
        }
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node.isArray()) {
                LinkedHashSet<String> result = new LinkedHashSet<>();
                node.forEach(item -> {
                    if (item != null && item.isTextual()) {
                        result.add(item.asText());
                    }
                });
                return result;
            }
            return new LinkedHashSet<>();
        } catch (JsonProcessingException e) {
            log.warn("No se pudo parsear JSON de competencias: {}", e.getMessage());
            return new LinkedHashSet<>();
        }
    }

    private Set<String> toSetFromJsonArray(ArrayNode node) {
        if (node == null) {
            return Collections.emptySet();
        }
        LinkedHashSet<String> result = new LinkedHashSet<>();
        node.forEach(item -> {
            if (item != null && item.isTextual()) {
                result.add(item.asText());
            }
        });
        return result;
    }

    private ArrayNode toArrayNode(Set<String> values) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        values.forEach(arrayNode::add);
        return arrayNode;
    }

    private ArrayNode fetchNodeArray(String json, String field) {
        if (StringUtils.isBlank(json)) {
            return objectMapper.createArrayNode();
        }
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode array = node.get(field);
            if (array instanceof ArrayNode) {
                return (ArrayNode) array;
            }
        } catch (JsonProcessingException e) {
            log.debug("JSON inválido en gapanalysis: {}", e.getMessage());
        }
        return objectMapper.createArrayNode();
    }

    private String writeValueAsString(JsonNode node) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar JSON", e);
        }
    }

    private void increaseAwarenessLevel(AICCompetence competence) {
        String current = Optional.ofNullable(competence.getAicawarenesslevel()).orElse(DEFAULT_AWARENESS_LEVEL);
        int idx = AWARENESS_LEVELS.indexOf(current.toUpperCase());
        if (idx < 0) {
            competence.setAicawarenesslevel("BASIC");
        } else if (idx < AWARENESS_LEVELS.size() - 1) {
            competence.setAicawarenesslevel(AWARENESS_LEVELS.get(idx + 1));
        }
    }

    // -------------------------------------------------------------------------
    // DTOs
    // -------------------------------------------------------------------------
    @Getter
    @Builder
    @AllArgsConstructor
    public static class GapAnalysisResult {
        private final AICCompetence competence;
        private final List<String> missing;
        private final List<String> extra;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CompetenceDashboard {
        private final int totalPeople;
        private final Map<String, Long> byRole;
        private final Map<String, Long> byAwareness;
        private final Map<String, Long> openGaps;
        private final long trainingSessions;

        public static CompetenceDashboard empty() {
        return CompetenceDashboard.builder()
            .totalPeople(0)
            .byRole(Collections.emptyMap())
            .byAwareness(Collections.emptyMap())
            .openGaps(Collections.emptyMap())
            .trainingSessions(0)
            .build();
        }
    }
}


