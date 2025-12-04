package com.codeflowx.govern.business.compliance;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.entity.compliance.EuRegistration;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BusinessService para EU Registrations según Art. 49 y Anexo VIII
 * 
 * Gestiona registro de sistemas IA en Base de Datos UE
 */
@Service
@Slf4j
public class EuRegistrationBusinessService {
    
    @Autowired
    private DAO dao;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Crea nuevo registro (draft)
     */
    public EuRegistration createRegistration(Long projectId, String registrationType) {
        log.info("Creating EU registration for project: {}, type: {}", projectId, registrationType);
        
        EuRegistration registration = new EuRegistration();
        registration.setIdxproject(projectId);
        registration.setRegregistrationtype(registrationType);
        registration.setRegstatus("DRAFT");
        registration.setRegattempts(0);
        registration.setRegissensitive(false);
        registration.setRegisnational(false);
        registration.setRegcreatedat(new Timestamp(System.currentTimeMillis()));
        registration.setIduuid(UUID.randomUUID().toString());
        
        // Inicializar submission data vacío
        registration.setRegsubmissiondata("{}");
        
        dao.insert(registration);
        log.info("EU registration created with ID: {}", registration.getIdxeuregistration());
        
        return registration;
    }
    
    /**
     * Actualiza submission data
     */
    public void updateSubmissionData(Long registrationId, Map<String, Object> submissionData) {
        log.info("Updating submission data for registration: {}", registrationId);
        
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        try {
            String dataJson = objectMapper.writeValueAsString(submissionData);
            registration.setRegsubmissiondata(dataJson);
            registration.setRegupdatedat(new Timestamp(System.currentTimeMillis()));
            
            dao.update(registration);
            log.info("Submission data updated successfully");
        } catch (Exception e) {
            log.error("Error serializing submission data", e);
            throw new RuntimeException("Failed to update submission data", e);
        }
    }
    
    /**
     * Envía a Base de Datos UE
     */
    public void submitToEuDatabase(Long registrationId) {
        log.info("Submitting registration to EU database: {}", registrationId);
        
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        // Validar que submission data esté completo
        if (registration.getRegsubmissiondata() == null || 
            registration.getRegsubmissiondata().equals("{}")) {
            throw new IllegalStateException("Submission data is not complete");
        }
        
        try {
            // AQUÍ: Integración real con API UE cuando esté disponible
            // Por ahora, simulamos el envío
            
            registration.setRegstatus("SUBMITTED");
            registration.setRegsubmissiondate(new Timestamp(System.currentTimeMillis()));
            registration.setRegattempts(registration.getRegattempts() + 1);
            registration.setReglastattemptat(new Timestamp(System.currentTimeMillis()));
            registration.setRegupdatedat(new Timestamp(System.currentTimeMillis()));
            
            // Simular respuesta exitosa
            registration.setRegeuregistrationid("EU-REG-" + UUID.randomUUID().toString().substring(0, 8));
            registration.setRegregistrationdate(new Timestamp(System.currentTimeMillis()));
            registration.setRegstatus("REGISTERED");
            
            dao.update(registration);
            log.info("Registration submitted successfully. EU ID: {}", registration.getRegeuregistrationid());
            
        } catch (Exception e) {
            log.error("Error submitting to EU database", e);
            
            // Marcar como error
            registration.setRegstatus("ERROR");
            registration.setRegerrormessage(e.getMessage());
            registration.setRegattempts(registration.getRegattempts() + 1);
            registration.setReglastattemptat(new Timestamp(System.currentTimeMillis()));
            registration.setRegupdatedat(new Timestamp(System.currentTimeMillis()));
            
            dao.update(registration);
            throw new RuntimeException("Failed to submit to EU database", e);
        }
    }
    
    /**
     * Verifica estado de submission
     */
    public String checkSubmissionStatus(Long registrationId) {
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        return registration.getRegstatus();
    }
    
    /**
     * Reintenta submission (con exponential backoff)
     */
    public void retrySubmission(Long registrationId) {
        log.info("Retrying submission for registration: {}", registrationId);
        
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        // Verificar número de intentos
        if (registration.getRegattempts() >= 5) {
            throw new IllegalStateException("Maximum retry attempts reached (5)");
        }
        
        // Calcular delay exponencial
        int delayMinutes = (int) Math.pow(2, registration.getRegattempts());
        log.info("Retry attempt {} with delay {} minutes", registration.getRegattempts() + 1, delayMinutes);
        
        // Reintentar submission
        submitToEuDatabase(registrationId);
    }
    
    /**
     * Marca como sistema sensible (Art. 49.4)
     */
    public void markAsSensitive(Long registrationId, boolean isSensitive) {
        log.info("Marking registration {} as sensitive: {}", registrationId, isSensitive);
        
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        registration.setRegissensitive(isSensitive);
        registration.setRegupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(registration);
    }
    
    /**
     * Configura registro nacional (Art. 49.5)
     */
    public void configureNationalRegistration(Long registrationId, String nationalRegistrationId) {
        log.info("Configuring national registration {} with ID: {}", registrationId, nationalRegistrationId);
        
        EuRegistration registration = dao.findById(EuRegistration.class, registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found: " + registrationId);
        }
        
        registration.setRegisnational(true);
        registration.setRegnationalregistrationid(nationalRegistrationId);
        registration.setRegupdatedat(new Timestamp(System.currentTimeMillis()));
        
        dao.update(registration);
    }
    
    /**
     * Obtiene registrations de un proyecto
     */
    public List<EuRegistration> getRegistrationsByProject(Long projectId) {
        String query = "SELECT * FROM REGEUREGISTRATIONS WHERE IDXPROJECT = ? ORDER BY REGCREATEDAT DESC";
        return dao.findListBySQL(EuRegistration.class, query, projectId);
    }
    
    /**
     * Obtiene latest registration de proyecto
     */
    public EuRegistration getLatestRegistration(Long projectId) {
        String query = "SELECT * FROM REGEUREGISTRATIONS WHERE IDXPROJECT = ? ORDER BY REGCREATEDAT DESC LIMIT 1";
        return dao.findBySQL(EuRegistration.class, query, projectId);
    }
}


