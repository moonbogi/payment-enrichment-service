package com.mastercard.enrichment.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Complete enriched transaction with all additional data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedTransaction {
    
    private Transaction transaction;
    private MerchantCategory merchantCategory;
    private GeolocationData geolocation;
    private NormalizedData normalizedData;
    private Instant enrichedAt;
    
    /**
     * Normalized and standardized transaction data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedData {
        private String normalizedMerchantName;
        private String standardizedAddress;
        private String formattedAmount;
        private String isoCountryCode;
    }
}
