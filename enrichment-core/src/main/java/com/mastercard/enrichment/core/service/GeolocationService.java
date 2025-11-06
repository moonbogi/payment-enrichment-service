package com.mastercard.enrichment.core.service;

import com.mastercard.enrichment.core.domain.GeolocationData;

import java.util.Optional;

/**
 * Service for geolocation enrichment
 */
public interface GeolocationService {
    
    /**
     * Get geolocation data by coordinates
     */
    Optional<GeolocationData> getGeolocationByCoordinates(Double latitude, Double longitude);
    
    /**
     * Get geolocation data by address
     */
    Optional<GeolocationData> getGeolocationByAddress(String address, String country);
    
    /**
     * Get geolocation data by IP address
     */
    Optional<GeolocationData> getGeolocationByIp(String ipAddress);
}
