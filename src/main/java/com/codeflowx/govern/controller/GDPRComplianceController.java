package com.codeflowx.govern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codeflowx.govern.entity.compliance.DataExportRequest;
import com.codeflowx.govern.entity.compliance.DataSubjectRequest;
import com.codeflowx.govern.entity.compliance.PrivacyNotice;
import com.codeflowx.govern.workflow.services.compliance.DataPortabilityService;
import com.codeflowx.govern.workflow.services.compliance.GDPRPrivacyService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * REST Controller para GDPR compliance
 * 
 * Endpoints para:
 * - Generación de Privacy Notices (GDPR Art. 13-14)
 * - Gestión de Data Subject Requests (GDPR Art. 15-22)
 * - Data Portability (GDPR Art. 20)
 */
@Slf4j
@RestController
@RequestMapping("/api/compliance/gdpr")
public class GDPRComplianceController {

    @Autowired
    private GDPRPrivacyService gdprService;

    @Autowired
    private DataPortabilityService dataPortabilityService;

    /**
     * Genera un Privacy Notice según GDPR Art. 13-14
     * 
     * @param systemType Tipo de sistema (MODEL, AGENT, PROMPT, RAG, ALL)
     * @param language Idioma (es, en)
     * @return Privacy Notice generado
     */
    @PostMapping("/generate-privacy-notice")
    public ResponseEntity<PrivacyNotice> generatePrivacyNotice(
            @RequestParam String systemType,
            @RequestParam(defaultValue = "es") String language) {
        
        log.info("POST /api/compliance/gdpr/generate-privacy-notice - systemType={}, language={}", 
                 systemType, language);
        
        try {
            PrivacyNotice notice = gdprService.generatePrivacyNotice(systemType, language);
            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            log.error("Error generando privacy notice", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Publica un Privacy Notice (DRAFT -> PUBLISHED)
     */
    @PutMapping("/publish-privacy-notice/{noticeId}")
    public ResponseEntity<String> publishPrivacyNotice(@PathVariable Long noticeId) {
        log.info("PUT /api/compliance/gdpr/publish-privacy-notice/{}", noticeId);
        
        try {
            gdprService.publishPrivacyNotice(noticeId);
            return ResponseEntity.ok("Privacy Notice publicado correctamente");
        } catch (Exception e) {
            log.error("Error publicando privacy notice", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * Crea una solicitud de derechos del interesado (Data Subject Request)
     * 
     * @param dto Datos de la solicitud
     * @return Data Subject Request creada
     */
    @PostMapping("/data-subject-request")
    public ResponseEntity<DataSubjectRequest> submitDataSubjectRequest(
            @RequestBody DataSubjectRequestDTO dto) {
        
        log.info("POST /api/compliance/gdpr/data-subject-request - email={}, type={}", 
                 dto.getEmail(), dto.getRequestType());
        
        try {
            DataSubjectRequest request = gdprService.processDataSubjectRequest(
                dto.getEmail(),
                dto.getRequestType(),
                dto.getDescription()
            );
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            log.error("Error procesando data subject request", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Actualiza el estado de una Data Subject Request
     */
    @PutMapping("/data-subject-request/{requestId}/status")
    public ResponseEntity<String> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestParam String status) {
        
        log.info("PUT /api/compliance/gdpr/data-subject-request/{}/status - newStatus={}", 
                 requestId, status);
        
        try {
            gdprService.updateRequestStatus(requestId, status);
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (Exception e) {
            log.error("Error actualizando estado de request", e);
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    /**
     * Exporta datos personales de un usuario (GDPR Art. 20 - Data Portability)
     * 
     * @param email Email del usuario
     * @param format Formato de export (JSON, XML, CSV)
     * @return Data Export Request
     */
    @PostMapping("/export-personal-data")
    public ResponseEntity<DataExportRequest> exportPersonalData(
            @RequestParam String email,
            @RequestParam(defaultValue = "JSON") String format) {
        
        log.info("POST /api/compliance/gdpr/export-personal-data - email={}, format={}", email, format);
        
        try {
            DataExportRequest exportRequest = dataPortabilityService.exportPersonalData(email, format);
            return ResponseEntity.ok(exportRequest);
        } catch (Exception e) {
            log.error("Error exportando datos personales", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * DTO para Data Subject Request
     */
    @Data
    public static class DataSubjectRequestDTO {
        private String email;
        private String name;
        private String requestType; // ACCESS, RECTIFICATION, ERASURE, RESTRICTION, PORTABILITY, OBJECTION, EXPLANATION
        private String description;
    }
}

