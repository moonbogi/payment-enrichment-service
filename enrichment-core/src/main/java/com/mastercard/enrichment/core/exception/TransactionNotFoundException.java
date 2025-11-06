package com.mastercard.enrichment.core.exception;

/**
 * Exception thrown when transaction is not found
 */
public class TransactionNotFoundException extends EnrichmentException {
    
    public TransactionNotFoundException(String transactionId) {
        super("Transaction not found: " + transactionId);
    }
}
