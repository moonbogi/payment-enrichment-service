package com.mastercard.enrichment.api.controller;

import com.mastercard.enrichment.api.dto.EnrichedTransactionResponse;
import com.mastercard.enrichment.api.dto.TransactionRequest;
import com.mastercard.enrichment.api.mapper.TransactionMapper;
import com.mastercard.enrichment.core.domain.*;
import com.mastercard.enrichment.core.service.EnrichmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrichmentControllerTest {
    
    @Mock
    private EnrichmentService enrichmentService;
    
    @Mock
    private TransactionMapper transactionMapper;
    
    @InjectMocks
    private EnrichmentController controller;
    
    private TransactionRequest request;
    private Transaction transaction;
    private EnrichedTransaction enrichedTransaction;
    private EnrichedTransactionResponse response;
    
    @BeforeEach
    void setUp() {
        request = TransactionRequest.builder()
                .transactionId("txn-123")
                .merchantId("merch-456")
                .merchantName("Test Restaurant")
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .country("USA")
                .city("New York")
                .build();
        
        transaction = Transaction.builder()
                .transactionId("txn-123")
                .merchantId("merch-456")
                .merchantName("Test Restaurant")
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .country("USA")
                .city("New York")
                .timestamp(Instant.now())
                .build();
        
        MerchantCategory category = MerchantCategory.builder()
                .merchantId("merch-456")
                .categoryCode("5812")
                .categoryName("Restaurant")
                .industry("Food & Beverage")
                .riskLevel(MerchantCategory.RiskLevel.LOW)
                .build();
        
        enrichedTransaction = EnrichedTransaction.builder()
                .transaction(transaction)
                .merchantCategory(category)
                .enrichedAt(Instant.now())
                .build();
        
        response = EnrichedTransactionResponse.builder()
                .transactionId("txn-123")
                .merchantId("merch-456")
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .build();
    }
    
    @Test
    void enrichTransaction_ShouldReturnEnrichedTransaction() {
        // Given
        when(transactionMapper.toTransaction(request)).thenReturn(transaction);
        when(enrichmentService.enrichTransaction(any(Transaction.class))).thenReturn(enrichedTransaction);
        when(transactionMapper.toResponse(enrichedTransaction)).thenReturn(response);
        
        // When
        ResponseEntity<EnrichedTransactionResponse> result = controller.enrichTransaction(request);
        
        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getTransactionId()).isEqualTo("txn-123");
        
        verify(enrichmentService, times(1)).enrichTransaction(any(Transaction.class));
    }
    
    @Test
    void enrichTransactionAsync_ShouldReturnAccepted() {
        // Given
        when(transactionMapper.toTransaction(request)).thenReturn(transaction);
        when(enrichmentService.enrichTransactionAsync(any(Transaction.class)))
                .thenReturn(CompletableFuture.completedFuture(enrichedTransaction));
        
        // When
        ResponseEntity<String> result = controller.enrichTransactionAsync(request);
        
        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertThat(result.getBody()).contains("txn-123");
        
        verify(enrichmentService, times(1)).enrichTransactionAsync(any(Transaction.class));
    }
    
    @Test
    void getEnrichmentStatus_ShouldReturnStatus() {
        // Given
        String transactionId = "txn-123";
        when(enrichmentService.getEnrichmentStatus(transactionId)).thenReturn("COMPLETED");
        
        // When
        ResponseEntity<String> result = controller.getEnrichmentStatus(transactionId);
        
        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("COMPLETED");
        
        verify(enrichmentService, times(1)).getEnrichmentStatus(transactionId);
    }
}
