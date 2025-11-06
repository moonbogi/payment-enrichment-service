package com.mastercard.enrichment.core.service;

import com.mastercard.enrichment.core.domain.EnrichedTransaction;
import com.mastercard.enrichment.core.domain.Transaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Core service interface for transaction enrichment
 */
public interface EnrichmentService {
    
    /**
     * Enrich a single transaction with all available data
     */
    EnrichedTransaction enrichTransaction(Transaction transaction);
    
    /**
     * Enrich a single transaction asynchronously
     */
    CompletableFuture<EnrichedTransaction> enrichTransactionAsync(Transaction transaction);
    
    /**
     * Batch enrich multiple transactions
     */
    List<EnrichedTransaction> enrichTransactions(List<Transaction> transactions);
    
    /**
     * Get enrichment status for a transaction
     */
    String getEnrichmentStatus(String transactionId);
}
