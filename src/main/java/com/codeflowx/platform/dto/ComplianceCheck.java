package com.codeflowx.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un check individual del compliance checklist EU AI Act
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceCheck {
    
    /**
     * Identificador único del check
     */
    private String id;
    
    /**
     * Nombre del check
     */
    private String name;
    
    /**
     * Descripción detallada del check
     */
    private String description;
    
    /**
     * Si el check ha sido completado/passed
     */
    private boolean passed;
    
    /**
     * Fecha en que se completó (si passed = true)
     */
    private String completedDate;
    
    /**
     * Información adicional o link a detalles
     */
    private String details;
    
    /**
     * Prioridad del check (REQUIRED, RECOMMENDED, OPTIONAL)
     */
    private String priority;
}



