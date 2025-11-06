package com.mastercard.enrichment.infrastructure.service;

import com.mastercard.enrichment.core.domain.GeolocationData;
import com.mastercard.enrichment.core.service.GeolocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of GeolocationService with caching
 * In production, this would integrate with a real geolocation API
 */
@Slf4j
@Service
public class GeolocationServiceImpl implements GeolocationService {
    
    // Mock data for demonstration
    private static final Map<String, GeolocationData> CITY_DATA = new HashMap<>();
    
    static {
        CITY_DATA.put("Vancouver", GeolocationData.builder()
                .city("Vancouver")
                .country("Canada")
                .countryCode("CA")
                .region("British Columbia")
                .latitude(49.2827)
                .longitude(-123.1207)
                .timezone("America/Vancouver")
                .build());
        
        CITY_DATA.put("New York", GeolocationData.builder()
                .city("New York")
                .country("United States")
                .countryCode("US")
                .region("New York")
                .latitude(40.7128)
                .longitude(-74.0060)
                .timezone("America/New_York")
                .build());
        
        CITY_DATA.put("London", GeolocationData.builder()
                .city("London")
                .country("United Kingdom")
                .countryCode("GB")
                .region("England")
                .latitude(51.5074)
                .longitude(-0.1278)
                .timezone("Europe/London")
                .build());
        
        CITY_DATA.put("Budapest", GeolocationData.builder()
                .city("Budapest")
                .country("Hungary")
                .countryCode("HU")
                .region("Central Hungary")
                .latitude(47.4979)
                .longitude(19.0402)
                .timezone("Europe/Budapest")
                .build());
    }
    
    @Override
    @Cacheable(value = "geolocationByCoords", key = "#latitude + ',' + #longitude")
    public Optional<GeolocationData> getGeolocationByCoordinates(Double latitude, Double longitude) {
        log.debug("Getting geolocation for coordinates: {}, {}", latitude, longitude);
        
        // Simple proximity match with mock data
        for (GeolocationData data : CITY_DATA.values()) {
            if (isNearby(latitude, longitude, data.getLatitude(), data.getLongitude(), 50.0)) {
                return Optional.of(data);
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    @Cacheable(value = "geolocationByAddress", key = "#address + ',' + #country")
    public Optional<GeolocationData> getGeolocationByAddress(String address, String country) {
        log.debug("Getting geolocation for address: {}, {}", address, country);
        
        // Simple keyword matching with mock data
        for (Map.Entry<String, GeolocationData> entry : CITY_DATA.entrySet()) {
            if (address.toLowerCase().contains(entry.getKey().toLowerCase())) {
                return Optional.of(entry.getValue());
            }
        }
        
        return Optional.empty();
    }
    
    @Override
    @Cacheable(value = "geolocationByIp", key = "#ipAddress")
    public Optional<GeolocationData> getGeolocationByIp(String ipAddress) {
        log.debug("Getting geolocation for IP: {}", ipAddress);
        // Would integrate with IP geolocation service in production
        return Optional.empty();
    }
    
    /**
     * Check if two coordinates are within a certain distance (in km)
     */
    private boolean isNearby(Double lat1, Double lon1, Double lat2, Double lon2, double maxDistance) {
        double distance = calculateDistance(lat1, lon1, lat2, lon2);
        return distance <= maxDistance;
    }
    
    /**
     * Calculate distance between two coordinates using Haversine formula
     */
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}
