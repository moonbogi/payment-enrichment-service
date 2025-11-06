package com.mastercard.enrichment.infrastructure.service;

import com.mastercard.enrichment.core.domain.*;
import com.mastercard.enrichment.core.repository.TransactionRepository;
import com.mastercard.enrichment.core.service.GeolocationService;
import com.mastercard.enrichment.core.service.MerchantCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrichmentServiceImplTest {
    
    @Mock
    private MerchantCategoryService merchantCategoryService;
    
    @Mock
    private GeolocationService geolocationService;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @InjectMocks
    private EnrichmentServiceImpl enrichmentService;
    
    private Transaction transaction;
    private MerchantCategory merchantCategory;
    private GeolocationData geolocationData;
    
    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .transactionId("txn-123")
                .merchantId("merch-456")
                .merchantName("Test Restaurant")
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .country("USA")
                .city("New York")
                .latitude(40.7128)
                .longitude(-74.0060)
                .timestamp(Instant.now())
                .build();
        
        merchantCategory = MerchantCategory.builder()
                .merchantId("merch-456")
                .categoryCode("5812")
                .categoryName("Restaurant")
                .industry("Food & Beverage")
                .riskLevel(MerchantCategory.RiskLevel.LOW)
                .build();
        
        geolocationData = GeolocationData.builder()
                .country("United States")
                .countryCode("US")
                .city("New York")
                .region("New York")
                .latitude(40.7128)
                .longitude(-74.0060)
                .timezone("America/New_York")
                .build();
    }
    
    @Test
    void enrichTransaction_ShouldEnrichWithAllData() {
        // Given
        when(merchantCategoryService.categorizeMerchant(anyString(), anyString()))
                .thenReturn(merchantCategory);
        when(geolocationService.getGeolocationByCoordinates(any(), any()))
                .thenReturn(Optional.of(geolocationData));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);
        
        // When
        EnrichedTransaction result = enrichmentService.enrichTransaction(transaction);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTransaction().getTransactionId()).isEqualTo("txn-123");
        assertThat(result.getMerchantCategory()).isNotNull();
        assertThat(result.getMerchantCategory().getCategoryName()).isEqualTo("Restaurant");
        assertThat(result.getGeolocation()).isNotNull();
        assertThat(result.getGeolocation().getCity()).isEqualTo("New York");
        assertThat(result.getNormalizedData()).isNotNull();
        
        verify(merchantCategoryService, times(1)).categorizeMerchant(anyString(), anyString());
        verify(geolocationService, times(1)).getGeolocationByCoordinates(any(), any());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void enrichTransaction_WithoutCoordinates_ShouldUseAddress() {
        // Given
        transaction.setLatitude(null);
        transaction.setLongitude(null);
        
        when(merchantCategoryService.categorizeMerchant(anyString(), anyString()))
                .thenReturn(merchantCategory);
        when(geolocationService.getGeolocationByAddress(anyString(), anyString()))
                .thenReturn(Optional.of(geolocationData));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);
        
        // When
        EnrichedTransaction result = enrichmentService.enrichTransaction(transaction);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getGeolocation()).isNotNull();
        
        verify(geolocationService, times(1)).getGeolocationByAddress(anyString(), anyString());
        verify(geolocationService, never()).getGeolocationByCoordinates(any(), any());
    }
    
    @Test
    void getEnrichmentStatus_ShouldReturnStatus() {
        // Given
        transaction.setEnrichmentStatus(EnrichmentStatus.COMPLETED);
        when(transactionRepository.findById("txn-123"))
                .thenReturn(Optional.of(transaction));
        
        // When
        String status = enrichmentService.getEnrichmentStatus("txn-123");
        
        // Then
        assertThat(status).isEqualTo("COMPLETED");
        
        verify(transactionRepository, times(1)).findById("txn-123");
    }
    
    @Test
    void getEnrichmentStatus_TransactionNotFound_ShouldReturnNotFound() {
        // Given
        when(transactionRepository.findById("txn-999"))
                .thenReturn(Optional.empty());
        
        // When
        String status = enrichmentService.getEnrichmentStatus("txn-999");
        
        // Then
        assertThat(status).isEqualTo("NOT_FOUND");
    }
}
