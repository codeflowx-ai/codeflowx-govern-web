package com.codeflowx.govern.business.logging;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.enartframework.nocode.datamodel.dao.DAO;
import lombok.extern.slf4j.Slf4j;
import com.codeflowx.govern.entity.logging.ImmutableLog;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * BusinessService para Immutable Logs con Hash Chains según Art. 19
 * 
 * CRÍTICO: Solo INSERT permitido, NUNCA UPDATE ni DELETE
 */
@Service
@Slf4j
public class ImmutableLoggingBusinessService {
    
    @Autowired
    private DAO dao;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Crea log entry inmutable
     * CRÍTICO: Solo INSERT, nunca UPDATE
     */
    public ImmutableLog createLogEntry(String entityType, Long entityId, String action, 
                                       Long userId, String userName, Map<String, Object> data) {
        log.info("Creating immutable log entry for entity: {}/{}, action: {}", entityType, entityId, action);
        
        // Obtener hash del log anterior
        ImmutableLog lastLog = getLastLog();
        String previousHash = lastLog != null ? lastLog.getImlcurrenthash() : 
            "0000000000000000000000000000000000000000000000000000000000000000";
        
        // Crear nuevo log
        ImmutableLog newLog = new ImmutableLog();
        newLog.setImlentitytype(entityType);
        newLog.setImlentityid(entityId);
        newLog.setImlaction(action);
        newLog.setImluserid(userId);
        newLog.setImlusername(userName);
        newLog.setImlprevioushash(previousHash);
        
        // Serializar data a JSON
        try {
            String dataJson = objectMapper.writeValueAsString(data);
            newLog.setImldata(dataJson);
        } catch (Exception e) {
            log.error("Error serializing data to JSON", e);
            throw new RuntimeException("Failed to serialize log data", e);
        }
        
        // Timestamps
        Timestamp now = new Timestamp(System.currentTimeMillis());
        newLog.setImltimestamp(now);
        newLog.setImltimestampepoch(System.currentTimeMillis());
        newLog.setImlcreatedat(now);
        newLog.setIduuid(UUID.randomUUID().toString());
        
        // Calcular current hash
        String currentHash = calculateHash(newLog);
        newLog.setImlcurrenthash(currentHash);
        newLog.setImlintegritystatus("UNVERIFIED");
        
        // Guardar (solo INSERT)
        dao.insert(newLog);
        log.info("Immutable log created with ID: {}, hash: {}", newLog.getIdximmutablelog(), currentHash);
        
        return newLog;
    }
    
    /**
     * Calcula SHA-256 hash del log
     */
    private String calculateHash(ImmutableLog log) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Concatenar campos en orden determinista
            String hashInput = log.getImlprevioushash() +
                               log.getImltimestampepoch() +
                               log.getImlentitytype() +
                               log.getImlentityid() +
                               log.getImlaction() +
                               log.getImluserid() +
                               log.getImldata();
            
            byte[] hashBytes = digest.digest(hashInput.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
    
    /**
     * Convierte bytes a hexadecimal
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Verifica integridad de hash chain
     */
    public LogIntegrityReport verifyIntegrity(Long startId, Long endId) {
        log.info("Verifying integrity of logs from {} to {}", startId, endId);
        
        String query = "SELECT * FROM IMLIMMUTABLELOGS WHERE IDXIMMUTABLELOG >= ? AND IDXIMMUTABLELOG <= ? ORDER BY IDXIMMUTABLELOG ASC";
        List<ImmutableLog> logs = dao.findListBySQL(ImmutableLog.class, query, startId, endId);
        
        LogIntegrityReport report = new LogIntegrityReport();
        report.setTotalLogsChecked(logs.size());
        report.setIntegrityValid(true);
        
        String expectedPreviousHash = null;
        for (ImmutableLog log : logs) {
            // Verificar hash actual
            String calculatedHash = calculateHash(log);
            if (!calculatedHash.equals(log.getImlcurrenthash())) {
                report.setIntegrityValid(false);
                report.addCorruptedLog(log.getIdximmutablelog(), "Current hash mismatch");
                log.error("Hash mismatch detected for log ID: {}", log.getIdximmutablelog());
            }
            
            // Verificar chain
            if (expectedPreviousHash != null && !log.getImlprevioushash().equals(expectedPreviousHash)) {
                report.setIntegrityValid(false);
                report.addCorruptedLog(log.getIdximmutablelog(), "Chain broken");
                log.error("Chain broken at log ID: {}", log.getIdximmutablelog());
            }
            
            expectedPreviousHash = log.getImlcurrenthash();
        }
        
        log.info("Integrity verification completed - Valid: {}, Total: {}", 
            report.isIntegrityValid(), report.getTotalLogsChecked());
        
        return report;
    }
    
    /**
     * Obtiene logs de una entidad con verificación
     */
    public List<ImmutableLog> getEntityLogsWithVerification(String entityType, Long entityId) {
        String query = "SELECT * FROM IMLIMMUTABLELOGS WHERE IMLENTITYTYPE = ? AND IMLENTITYID = ? ORDER BY IMLTIMESTAMPEPOCH ASC";
        List<ImmutableLog> logs = dao.findListBySQL(ImmutableLog.class, query, entityType, entityId);
        
        // Verificar integridad si hay más de 1 log
        if (logs.size() > 1) {
            Long firstId = logs.get(0).getIdximmutablelog();
            Long lastId = logs.get(logs.size() - 1).getIdximmutablelog();
            verifyIntegrity(firstId, lastId);
        }
        
        return logs;
    }
    
    /**
     * Obtiene último log (para hash chain)
     */
    private ImmutableLog getLastLog() {
        String query = "SELECT * FROM IMLIMMUTABLELOGS ORDER BY IDXIMMUTABLELOG DESC LIMIT 1";
        return dao.findBySQL(ImmutableLog.class, query);
    }
    
    /**
     * Clase para reporte de integridad
     */
    @Data
    public static class LogIntegrityReport {
        private int totalLogsChecked;
        private boolean integrityValid;
        private List<String> corruptedLogs = new java.util.ArrayList<>();
        
        public void addCorruptedLog(Long logId, String reason) {
            corruptedLogs.add("Log ID " + logId + ": " + reason);
        }
    }
}


