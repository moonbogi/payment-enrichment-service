package com.mastercard.enrichment.infrastructure.service;

import com.mastercard.enrichment.core.domain.MerchantCategory;
import com.mastercard.enrichment.core.service.MerchantCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of MerchantCategoryService with caching
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCategoryServiceImpl implements MerchantCategoryService {
    
    // In-memory storage for demo purposes (would be DynamoDB in production)
    private final Map<String, MerchantCategory> categoryStore = new HashMap<>();
    
    @Override
    @Cacheable(value = "merchantCategories", key = "#merchantId")
    public Optional<MerchantCategory> getCategoryByMerchantId(String merchantId) {
        log.debug("Getting category for merchant: {}", merchantId);
        return Optional.ofNullable(categoryStore.get(merchantId));
    }
    
    @Override
    public MerchantCategory categorizeMerchant(String merchantId, String merchantName) {
        log.info("Categorizing merchant: {} - {}", merchantId, merchantName);
        
        // Check if already categorized
        Optional<MerchantCategory> existing = getCategoryByMerchantId(merchantId);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Simple categorization logic based on merchant name keywords
        MerchantCategory category = determineCategoryFromName(merchantId, merchantName);
        categoryStore.put(merchantId, category);
        
        return category;
    }
    
    @Override
    public void updateCategory(MerchantCategory category) {
        log.info("Updating category for merchant: {}", category.getMerchantId());
        categoryStore.put(category.getMerchantId(), category);
    }
    
    private MerchantCategory determineCategoryFromName(String merchantId, String merchantName) {
        String nameLower = merchantName.toLowerCase();
        
        // E-commerce
        if (nameLower.contains("amazon") || nameLower.contains("ebay") || nameLower.contains("shop")) {
            return buildCategory(merchantId, "5999", "E-Commerce", "Retail", MerchantCategory.RiskLevel.LOW);
        }
        
        // Restaurants
        if (nameLower.contains("restaurant") || nameLower.contains("cafe") || nameLower.contains("food")) {
            return buildCategory(merchantId, "5812", "Restaurant", "Food & Beverage", MerchantCategory.RiskLevel.LOW);
        }
        
        // Gas Stations
        if (nameLower.contains("gas") || nameLower.contains("fuel") || nameLower.contains("petrol")) {
            return buildCategory(merchantId, "5541", "Gas Station", "Automotive", MerchantCategory.RiskLevel.LOW);
        }
        
        // Hotels
        if (nameLower.contains("hotel") || nameLower.contains("inn") || nameLower.contains("resort")) {
            return buildCategory(merchantId, "7011", "Hotel", "Lodging", MerchantCategory.RiskLevel.MEDIUM);
        }
        
        // Airlines
        if (nameLower.contains("airline") || nameLower.contains("airways") || nameLower.contains("air")) {
            return buildCategory(merchantId, "4511", "Airline", "Transportation", MerchantCategory.RiskLevel.MEDIUM);
        }
        
        // Gambling/Casino
        if (nameLower.contains("casino") || nameLower.contains("gambling") || nameLower.contains("betting")) {
            return buildCategory(merchantId, "7995", "Gambling", "Entertainment", MerchantCategory.RiskLevel.HIGH);
        }
        
        // Cryptocurrency
        if (nameLower.contains("crypto") || nameLower.contains("bitcoin") || nameLower.contains("blockchain")) {
            return buildCategory(merchantId, "6051", "Cryptocurrency", "Financial Services", MerchantCategory.RiskLevel.CRITICAL);
        }
        
        // Default category
        return buildCategory(merchantId, "5999", "Miscellaneous", "General Retail", MerchantCategory.RiskLevel.LOW);
    }
    
    private MerchantCategory buildCategory(String merchantId, String code, String name, 
                                          String industry, MerchantCategory.RiskLevel risk) {
        return MerchantCategory.builder()
                .merchantId(merchantId)
                .categoryCode(code)
                .categoryName(name)
                .industry(industry)
                .riskLevel(risk)
                .build();
    }
}
