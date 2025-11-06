package com.mastercard.enrichment.core.domain;

/**
 * Enrichment status for tracking processing state
 */
public enum EnrichmentStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    PARTIALLY_ENRICHED
}
