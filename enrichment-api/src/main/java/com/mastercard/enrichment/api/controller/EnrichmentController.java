package com.mastercard.enrichment.api.controller;

import com.mastercard.enrichment.api.dto.EnrichedTransactionResponse;
import com.mastercard.enrichment.api.dto.TransactionRequest;
import com.mastercard.enrichment.api.mapper.TransactionMapper;
import com.mastercard.enrichment.core.domain.EnrichedTransaction;
import com.mastercard.enrichment.core.domain.Transaction;
import com.mastercard.enrichment.core.service.EnrichmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * REST controller for transaction enrichment operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/enrichment")
@RequiredArgsConstructor
@Tag(name = "Transaction Enrichment", description = "APIs for enriching transaction data")
public class EnrichmentController {
    
    private final EnrichmentService enrichmentService;
    private final TransactionMapper transactionMapper;
    
    @PostMapping("/transactions")
    @Operation(summary = "Enrich a single transaction", 
               description = "Enriches transaction with merchant category, geolocation, and normalized data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transaction enriched successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EnrichedTransactionResponse> enrichTransaction(
            @Valid @RequestBody TransactionRequest request) {
        
        log.info("Enriching transaction: {}", request.getTransactionId());
        
        Transaction transaction = transactionMapper.toTransaction(request);
        transaction.setTimestamp(Instant.now());
        
        EnrichedTransaction enriched = enrichmentService.enrichTransaction(transaction);
        EnrichedTransactionResponse response = transactionMapper.toResponse(enriched);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/transactions/async")
    @Operation(summary = "Enrich a transaction asynchronously",
               description = "Starts asynchronous enrichment process and returns immediately")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Enrichment started"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<String> enrichTransactionAsync(
            @Valid @RequestBody TransactionRequest request) {
        
        log.info("Starting async enrichment for transaction: {}", request.getTransactionId());
        
        Transaction transaction = transactionMapper.toTransaction(request);
        transaction.setTimestamp(Instant.now());
        
        CompletableFuture<EnrichedTransaction> future = 
                enrichmentService.enrichTransactionAsync(transaction);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Enrichment started for transaction: " + request.getTransactionId());
    }
    
    @PostMapping("/transactions/batch")
    @Operation(summary = "Batch enrich multiple transactions",
               description = "Enriches multiple transactions in a single request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Batch enrichment completed"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<List<EnrichedTransactionResponse>> enrichTransactionsBatch(
            @Valid @RequestBody List<TransactionRequest> requests) {
        
        log.info("Batch enriching {} transactions", requests.size());
        
        List<Transaction> transactions = requests.stream()
                .map(req -> {
                    Transaction txn = transactionMapper.toTransaction(req);
                    txn.setTimestamp(Instant.now());
                    return txn;
                })
                .collect(Collectors.toList());
        
        List<EnrichedTransaction> enriched = enrichmentService.enrichTransactions(transactions);
        List<EnrichedTransactionResponse> responses = enriched.stream()
                .map(transactionMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/transactions/{transactionId}/status")
    @Operation(summary = "Get enrichment status",
               description = "Returns the current enrichment status of a transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<String> getEnrichmentStatus(@PathVariable String transactionId) {
        log.info("Getting enrichment status for transaction: {}", transactionId);
        String status = enrichmentService.getEnrichmentStatus(transactionId);
        return ResponseEntity.ok(status);
    }
}
