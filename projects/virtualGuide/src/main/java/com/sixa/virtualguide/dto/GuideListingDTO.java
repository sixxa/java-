package com.sixa.virtualguide.dto;

import com.sixa.virtualguide.model.Location;
import java.util.List;

public class GuideListingDTO {
    private int guideId;
    private List<String> picturePaths;
    private String firstName;
    private String lastName;
    private String bio;
    private Integer pricePerHour;
    private Location location;

    // Explicit Constructor for JPQL Projection
    public GuideListingDTO(int guideId, List<String> picturePaths, String firstName,
                           String lastName, String bio, Integer pricePerHour, Location location) {
        this.guideId = guideId;
        this.picturePaths = picturePaths;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.pricePerHour = pricePerHour;
        this.location = location;
    }

    // Getters
    public int getGuideId() {
        return guideId;
    }

    public List<String> getPicturePaths() {
        return picturePaths;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBio() {
        return bio;
    }

    public Integer getPricePerHour() {
        return pricePerHour;
    }

    public Location getLocation() {
        return location;
    }
}
