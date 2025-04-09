package com.sixa.giveawayapp.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class ItemRequest {
    private String name;
    private String description;
    private Integer price;  // Price 0 for giveaway
    private String category;
    private String country;  // Country field added
    private String city;     // City field added
    private String address;
    private List<String> images;
}
