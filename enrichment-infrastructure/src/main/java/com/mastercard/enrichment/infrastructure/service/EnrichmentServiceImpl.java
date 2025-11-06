package com.mastercard.enrichment.infrastructure.service;

import com.mastercard.enrichment.core.domain.*;
import com.mastercard.enrichment.core.repository.TransactionRepository;
import com.mastercard.enrichment.core.service.EnrichmentService;
import com.mastercard.enrichment.core.service.GeolocationService;
import com.mastercard.enrichment.core.service.MerchantCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of EnrichmentService with caching and async support
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichmentServiceImpl implements EnrichmentService {
    
    private final MerchantCategoryService merchantCategoryService;
    private final GeolocationService geolocationService;
    private final TransactionRepository transactionRepository;
    
    @Override
    @Cacheable(value = "enrichedTransactions", key = "#transaction.transactionId")
    public EnrichedTransaction enrichTransaction(Transaction transaction) {
        log.info("Enriching transaction: {}", transaction.getTransactionId());
        
        transaction.setEnrichmentStatus(EnrichmentStatus.IN_PROGRESS);
        
        try {
            // Enrich with merchant category
            MerchantCategory merchantCategory = merchantCategoryService
                    .categorizeMerchant(transaction.getMerchantId(), transaction.getMerchantName());
            
            // Enrich with geolocation
            GeolocationData geolocation = null;
            if (transaction.getLatitude() != null && transaction.getLongitude() != null) {
                geolocation = geolocationService
                        .getGeolocationByCoordinates(transaction.getLatitude(), transaction.getLongitude())
                        .orElse(null);
            } else if (transaction.getCountry() != null) {
                String address = (transaction.getCity() != null ? transaction.getCity() + ", " : "") 
                        + transaction.getCountry();
                geolocation = geolocationService
                        .getGeolocationByAddress(address, transaction.getCountry())
                        .orElse(null);
            }
            
            // Create normalized data
            EnrichedTransaction.NormalizedData normalizedData = createNormalizedData(
                    transaction, merchantCategory, geolocation);
            
            transaction.setEnrichmentStatus(EnrichmentStatus.COMPLETED);
            transaction.setEnrichedAt(Instant.now());
            
            // Save enriched transaction
            transactionRepository.save(transaction);
            
            return EnrichedTransaction.builder()
                    .transaction(transaction)
                    .merchantCategory(merchantCategory)
                    .geolocation(geolocation)
                    .normalizedData(normalizedData)
                    .enrichedAt(Instant.now())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error enriching transaction: {}", transaction.getTransactionId(), e);
            transaction.setEnrichmentStatus(EnrichmentStatus.FAILED);
            transactionRepository.save(transaction);
            throw e;
        }
    }
    
    @Override
    @Async
    public CompletableFuture<EnrichedTransaction> enrichTransactionAsync(Transaction transaction) {
        log.info("Starting async enrichment for transaction: {}", transaction.getTransactionId());
        EnrichedTransaction result = enrichTransaction(transaction);
        return CompletableFuture.completedFuture(result);
    }
    
    @Override
    public List<EnrichedTransaction> enrichTransactions(List<Transaction> transactions) {
        log.info("Batch enriching {} transactions", transactions.size());
        return transactions.stream()
                .map(this::enrichTransaction)
                .collect(Collectors.toList());
    }
    
    @Override
    public String getEnrichmentStatus(String transactionId) {
        return transactionRepository.findById(transactionId)
                .map(txn -> txn.getEnrichmentStatus() != null ? 
                        txn.getEnrichmentStatus().name() : "UNKNOWN")
                .orElse("NOT_FOUND");
    }
    
    private EnrichedTransaction.NormalizedData createNormalizedData(
            Transaction transaction, 
            MerchantCategory category, 
            GeolocationData geolocation) {
        
        String normalizedMerchantName = normalizeMerchantName(transaction.getMerchantName());
        String standardizedAddress = geolocation != null ? 
                formatAddress(geolocation) : null;
        String formattedAmount = String.format("%.2f %s", 
                transaction.getAmount(), transaction.getCurrency());
        String isoCountryCode = geolocation != null ? geolocation.getCountryCode() : null;
        
        return EnrichedTransaction.NormalizedData.builder()
                .normalizedMerchantName(normalizedMerchantName)
                .standardizedAddress(standardizedAddress)
                .formattedAmount(formattedAmount)
                .isoCountryCode(isoCountryCode)
                .build();
    }
    
    private String normalizeMerchantName(String merchantName) {
        if (merchantName == null) return null;
        // Simple normalization: trim, uppercase, remove special chars
        return merchantName.trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9\\s]", "");
    }
    
    private String formatAddress(GeolocationData geolocation) {
        StringBuilder address = new StringBuilder();
        if (geolocation.getCity() != null) {
            address.append(geolocation.getCity()).append(", ");
        }
        if (geolocation.getRegion() != null) {
            address.append(geolocation.getRegion()).append(", ");
        }
        if (geolocation.getCountry() != null) {
            address.append(geolocation.getCountry());
        }
        return address.toString();
    }
}
