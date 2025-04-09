package com.sixa.giveawayapp.DTO.request;

import lombok.Data;

@Data
public class FilterItemRequest {

    private String name;
    private String category;
    private Integer minPrice;
    private Integer maxPrice;
    private Boolean ForGiveaway;
    private String country;
    private String city;
    private String address;
}
