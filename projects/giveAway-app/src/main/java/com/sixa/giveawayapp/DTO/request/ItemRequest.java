package com.sixa.giveawayapp.DTO.request;

import lombok.Data;

import java.util.List;

@Data
public class ItemRequest {
    private String name;
    private String description;
    private Integer price;
    private String category;
    private String country;
    private String city;
    private String address;
    private List<String> images;
}
