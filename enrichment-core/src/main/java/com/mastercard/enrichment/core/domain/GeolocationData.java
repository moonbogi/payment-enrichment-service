package com.mastercard.enrichment.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Geolocation enrichment data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeolocationData {
    
    private String country;
    private String countryCode;
    private String city;
    private String region;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String timezone;
}
