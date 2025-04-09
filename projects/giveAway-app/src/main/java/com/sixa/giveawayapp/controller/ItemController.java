package com.sixa.giveawayapp.controller;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.service.ItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest itemRequest,
                                                   @RequestAttribute("userId") Integer userId) {
        ItemResponse createdItem = itemService.createItem(itemRequest, userId);
        return ResponseEntity.ok(createdItem);
    }

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<ItemResponse>> getAllItems(FilterItemRequest filterItemRequest) {
        List<ItemResponse> items = itemService.getAllItems(filterItemRequest);
        return ResponseEntity.ok(items);
    }
}
