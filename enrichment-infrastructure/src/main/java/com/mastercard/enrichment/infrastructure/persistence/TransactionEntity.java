package com.mastercard.enrichment.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DynamoDB entity for transaction persistence
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class TransactionEntity {
    
    private String transactionId;
    private String merchantId;
    private String merchantName;
    private BigDecimal amount;
    private String currency;
    private Long timestamp;
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;
    private String enrichmentStatus;
    private Long enrichedAt;
    
    @DynamoDbPartitionKey
    @DynamoDbAttribute("transactionId")
    public String getTransactionId() {
        return transactionId;
    }
    
    @DynamoDbAttribute("merchantId")
    public String getMerchantId() {
        return merchantId;
    }
    
    @DynamoDbAttribute("merchantName")
    public String getMerchantName() {
        return merchantName;
    }
    
    @DynamoDbAttribute("amount")
    public BigDecimal getAmount() {
        return amount;
    }
    
    @DynamoDbAttribute("currency")
    public String getCurrency() {
        return currency;
    }
    
    @DynamoDbAttribute("timestamp")
    public Long getTimestamp() {
        return timestamp;
    }
    
    @DynamoDbAttribute("country")
    public String getCountry() {
        return country;
    }
    
    @DynamoDbAttribute("city")
    public String getCity() {
        return city;
    }
    
    @DynamoDbAttribute("latitude")
    public Double getLatitude() {
        return latitude;
    }
    
    @DynamoDbAttribute("longitude")
    public Double getLongitude() {
        return longitude;
    }
    
    @DynamoDbAttribute("enrichmentStatus")
    public String getEnrichmentStatus() {
        return enrichmentStatus;
    }
    
    @DynamoDbAttribute("enrichedAt")
    public Long getEnrichedAt() {
        return enrichedAt;
    }
}
