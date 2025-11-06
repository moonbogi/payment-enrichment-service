package com.mastercard.enrichment.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main application entry point
 */
@SpringBootApplication(scanBasePackages = "com.mastercard.enrichment")
@EnableAsync
public class EnrichmentServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EnrichmentServiceApplication.class, args);
    }
}
