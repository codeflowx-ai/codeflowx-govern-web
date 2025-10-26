package com.codeflowx.govern.workflow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para el microservicio de governance
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "governance.service")
public class GovernanceServiceConfig {
    
    /**
     * URL del microservicio leka-server-governance
     * Default: http://localhost:8002
     */
    private String url = "http://localhost:8002";
    
    /**
     * Timeout en milisegundos
     * Default: 30 segundos
     */
    private int timeout = 30000;
    
    /**
     * Retry attempts en caso de error
     * Default: 2
     */
    private int retryAttempts = 2;
    
    /**
     * Usar fallback si el servicio no está disponible
     * Default: true
     */
    private boolean useFallback = true;
}

