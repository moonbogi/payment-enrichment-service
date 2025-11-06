package com.mastercard.enrichment.core.repository;

import com.mastercard.enrichment.core.domain.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for transaction persistence
 */
public interface TransactionRepository {
    
    /**
     * Save a transaction
     */
    Transaction save(Transaction transaction);
    
    /**
     * Find transaction by ID
     */
    Optional<Transaction> findById(String transactionId);
    
    /**
     * Find transactions by merchant ID
     */
    List<Transaction> findByMerchantId(String merchantId);
    
    /**
     * Delete transaction
     */
    void delete(String transactionId);
}
