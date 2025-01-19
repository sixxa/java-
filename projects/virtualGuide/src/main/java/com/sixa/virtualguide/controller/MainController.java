package com.sixa.virtualguide.controller;

import com.sixa.virtualguide.Service.GuideService;
import com.sixa.virtualguide.dto.GuideDetailsDTO;
import com.sixa.virtualguide.dto.GuideListingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/main")
public class MainController {

    @Autowired
    private GuideService guideService;

    @GetMapping("/get_listings")
    public ResponseEntity<Page<GuideListingDTO>> getGuideListings(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "order", required = false, defaultValue = "asc") String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Page<GuideListingDTO> guideListings;

        if (keyword != null || minPrice != null || maxPrice != null) {
            // If search parameters are provided, use search logic with paging
            guideListings = guideService.searchGuidesWithPaging(keyword, minPrice, maxPrice, page, size);
        } else if (order != null) {
            // Default pagination for ordered guides
            guideListings = guideService.getGuideListingsOrderedByPriceWithPaging(order, page, size);
        } else {
            // Default: fetch all listings with paging
            guideListings = guideService.getAllGuideListingsWithPaging(page, size);
        }

        if (guideListings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(guideListings, HttpStatus.OK);
    }


    @GetMapping("get_listings/{guideId}")
    public ResponseEntity<GuideDetailsDTO> getGuideDetails(@PathVariable int guideId) {
        try {
            GuideDetailsDTO guideDetails = guideService.getGuideDetailsById(guideId);
            return ResponseEntity.ok(guideDetails);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null); // Return 404 if guide not found
        }
    }

}
