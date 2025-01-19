package com.sixa.virtualguide.dto;

import com.sixa.virtualguide.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateModel {

    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String bio;
    private List<String> picturePaths;
    private List<String> languages;
    private Location location;
    private int pricePerHour;
}
