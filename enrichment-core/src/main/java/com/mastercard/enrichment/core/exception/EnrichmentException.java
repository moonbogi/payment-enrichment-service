package com.mastercard.enrichment.core.exception;

/**
 * Base exception for enrichment service
 */
public class EnrichmentException extends RuntimeException {
    
    public EnrichmentException(String message) {
        super(message);
    }
    
    public EnrichmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
