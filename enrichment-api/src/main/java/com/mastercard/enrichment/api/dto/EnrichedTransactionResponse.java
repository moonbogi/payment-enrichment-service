package com.mastercard.enrichment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for enriched transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrichedTransactionResponse {
    
    private String transactionId;
    private String merchantId;
    private String merchantName;
    private BigDecimal amount;
    private String currency;
    private Instant timestamp;
    
    // Enrichment data
    private MerchantCategoryInfo merchantCategory;
    private GeolocationInfo geolocation;
    private NormalizedDataInfo normalizedData;
    private Instant enrichedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MerchantCategoryInfo {
        private String categoryCode;
        private String categoryName;
        private String industry;
        private String riskLevel;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeolocationInfo {
        private String country;
        private String countryCode;
        private String city;
        private String region;
        private Double latitude;
        private Double longitude;
        private String timezone;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NormalizedDataInfo {
        private String normalizedMerchantName;
        private String standardizedAddress;
        private String formattedAmount;
        private String isoCountryCode;
    }
}
