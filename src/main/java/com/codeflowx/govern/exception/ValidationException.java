package com.codeflowx.govern.exception;

/**
 * Excepción para validaciones de negocio en compliance
 * Usada para bloquear operaciones que no cumplen requisitos del EU AI Act
 */
public class ValidationException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

