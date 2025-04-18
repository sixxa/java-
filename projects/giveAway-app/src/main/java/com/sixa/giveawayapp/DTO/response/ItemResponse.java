package com.sixa.giveawayapp.DTO.response;

import com.sixa.giveawayapp.DTO.SimpleUserDTO;
import lombok.Data;
import java.util.List;

@Data
public class ItemResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private String category;
    private String country;
    private String city;
    private SimpleUserDTO user;
    private List<String> images;
}
