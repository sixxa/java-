package com.sixa.virtualguide.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
    @Data
    public class Location {
        private String city;
        private String country;
    }

