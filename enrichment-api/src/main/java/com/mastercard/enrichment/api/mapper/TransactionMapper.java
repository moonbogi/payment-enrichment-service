package com.mastercard.enrichment.api.mapper;

import com.mastercard.enrichment.api.dto.EnrichedTransactionResponse;
import com.mastercard.enrichment.api.dto.TransactionRequest;
import com.mastercard.enrichment.core.domain.EnrichedTransaction;
import com.mastercard.enrichment.core.domain.Transaction;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between DTOs and domain models
 */
@Component
public class TransactionMapper {
    
    public Transaction toTransaction(TransactionRequest request) {
        return Transaction.builder()
                .transactionId(request.getTransactionId())
                .merchantId(request.getMerchantId())
                .merchantName(request.getMerchantName())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .country(request.getCountry())
                .city(request.getCity())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
    }
    
    public EnrichedTransactionResponse toResponse(EnrichedTransaction enriched) {
        Transaction txn = enriched.getTransaction();
        
        EnrichedTransactionResponse.EnrichedTransactionResponseBuilder builder = 
                EnrichedTransactionResponse.builder()
                        .transactionId(txn.getTransactionId())
                        .merchantId(txn.getMerchantId())
                        .merchantName(txn.getMerchantName())
                        .amount(txn.getAmount())
                        .currency(txn.getCurrency())
                        .timestamp(txn.getTimestamp())
                        .enrichedAt(enriched.getEnrichedAt());
        
        // Map merchant category if present
        if (enriched.getMerchantCategory() != null) {
            builder.merchantCategory(EnrichedTransactionResponse.MerchantCategoryInfo.builder()
                    .categoryCode(enriched.getMerchantCategory().getCategoryCode())
                    .categoryName(enriched.getMerchantCategory().getCategoryName())
                    .industry(enriched.getMerchantCategory().getIndustry())
                    .riskLevel(enriched.getMerchantCategory().getRiskLevel() != null ? 
                            enriched.getMerchantCategory().getRiskLevel().name() : null)
                    .build());
        }
        
        // Map geolocation if present
        if (enriched.getGeolocation() != null) {
            builder.geolocation(EnrichedTransactionResponse.GeolocationInfo.builder()
                    .country(enriched.getGeolocation().getCountry())
                    .countryCode(enriched.getGeolocation().getCountryCode())
                    .city(enriched.getGeolocation().getCity())
                    .region(enriched.getGeolocation().getRegion())
                    .latitude(enriched.getGeolocation().getLatitude())
                    .longitude(enriched.getGeolocation().getLongitude())
                    .timezone(enriched.getGeolocation().getTimezone())
                    .build());
        }
        
        // Map normalized data if present
        if (enriched.getNormalizedData() != null) {
            builder.normalizedData(EnrichedTransactionResponse.NormalizedDataInfo.builder()
                    .normalizedMerchantName(enriched.getNormalizedData().getNormalizedMerchantName())
                    .standardizedAddress(enriched.getNormalizedData().getStandardizedAddress())
                    .formattedAmount(enriched.getNormalizedData().getFormattedAmount())
                    .isoCountryCode(enriched.getNormalizedData().getIsoCountryCode())
                    .build());
        }
        
        return builder.build();
    }
}
