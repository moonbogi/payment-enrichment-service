package com.mastercard.enrichment.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Merchant category information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantCategory {
    
    private String merchantId;
    private String categoryCode;
    private String categoryName;
    private String industry;
    private RiskLevel riskLevel;
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
