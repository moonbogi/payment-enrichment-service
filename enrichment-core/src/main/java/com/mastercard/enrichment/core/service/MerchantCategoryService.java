package com.mastercard.enrichment.core.service;

import com.mastercard.enrichment.core.domain.MerchantCategory;

import java.util.Optional;

/**
 * Service for merchant categorization
 */
public interface MerchantCategoryService {
    
    /**
     * Get merchant category by merchant ID
     */
    Optional<MerchantCategory> getCategoryByMerchantId(String merchantId);
    
    /**
     * Categorize merchant based on name and other attributes
     */
    MerchantCategory categorizeMerchant(String merchantId, String merchantName);
    
    /**
     * Update merchant category
     */
    void updateCategory(MerchantCategory category);
}
