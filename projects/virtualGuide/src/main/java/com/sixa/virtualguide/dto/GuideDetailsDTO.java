package com.sixa.virtualguide.dto;

import com.sixa.virtualguide.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GuideDetailsDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private List<String> languages;
    private Location location;
    private Integer pricePerHour;
    private List<String> picturePaths;
}
