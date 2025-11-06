package com.mastercard.enrichment.infrastructure.persistence;

import com.mastercard.enrichment.core.domain.EnrichmentStatus;
import com.mastercard.enrichment.core.domain.Transaction;
import com.mastercard.enrichment.core.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DynamoDB implementation of TransactionRepository
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DynamoDbTransactionRepository implements TransactionRepository {
    
    private final DynamoDbEnhancedClient dynamoDbClient;
    private static final String TABLE_NAME = "Transactions";
    
    private DynamoDbTable<TransactionEntity> getTable() {
        return dynamoDbClient.table(TABLE_NAME, TableSchema.fromBean(TransactionEntity.class));
    }
    
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Saving transaction: {}", transaction.getTransactionId());
        TransactionEntity entity = toEntity(transaction);
        getTable().putItem(entity);
        return transaction;
    }
    
    @Override
    public Optional<Transaction> findById(String transactionId) {
        log.debug("Finding transaction by ID: {}", transactionId);
        Key key = Key.builder()
                .partitionValue(transactionId)
                .build();
        
        TransactionEntity entity = getTable().getItem(key);
        return Optional.ofNullable(entity).map(this::toDomain);
    }
    
    @Override
    public List<Transaction> findByMerchantId(String merchantId) {
        log.debug("Finding transactions by merchant ID: {}", merchantId);
        // Note: This requires a GSI on merchantId in production
        return getTable().scan().items().stream()
                .filter(entity -> merchantId.equals(entity.getMerchantId()))
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void delete(String transactionId) {
        log.debug("Deleting transaction: {}", transactionId);
        Key key = Key.builder()
                .partitionValue(transactionId)
                .build();
        getTable().deleteItem(key);
    }
    
    private TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .transactionId(transaction.getTransactionId())
                .merchantId(transaction.getMerchantId())
                .merchantName(transaction.getMerchantName())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .timestamp(transaction.getTimestamp() != null ? 
                        transaction.getTimestamp().getEpochSecond() : null)
                .country(transaction.getCountry())
                .city(transaction.getCity())
                .latitude(transaction.getLatitude())
                .longitude(transaction.getLongitude())
                .enrichmentStatus(transaction.getEnrichmentStatus() != null ? 
                        transaction.getEnrichmentStatus().name() : null)
                .enrichedAt(transaction.getEnrichedAt() != null ? 
                        transaction.getEnrichedAt().getEpochSecond() : null)
                .build();
    }
    
    private Transaction toDomain(TransactionEntity entity) {
        return Transaction.builder()
                .transactionId(entity.getTransactionId())
                .merchantId(entity.getMerchantId())
                .merchantName(entity.getMerchantName())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .timestamp(entity.getTimestamp() != null ? 
                        Instant.ofEpochSecond(entity.getTimestamp()) : null)
                .country(entity.getCountry())
                .city(entity.getCity())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .enrichmentStatus(entity.getEnrichmentStatus() != null ? 
                        EnrichmentStatus.valueOf(entity.getEnrichmentStatus()) : null)
                .enrichedAt(entity.getEnrichedAt() != null ? 
                        Instant.ofEpochSecond(entity.getEnrichedAt()) : null)
                .build();
    }
}
