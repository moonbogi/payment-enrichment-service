package com.mastercard.enrichment.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Core domain model representing a payment transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    
    private String transactionId;
    private String merchantId;
    private String merchantName;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    
    // Location data
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;
    
    // Enrichment status
    private EnrichmentStatus enrichmentStatus;
    private Instant enrichedAt;
}
