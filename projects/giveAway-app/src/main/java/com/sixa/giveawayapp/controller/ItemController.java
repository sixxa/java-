package com.sixa.giveawayapp.controller;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemDetailResponse;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.service.ItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<ItemResponse>> getAllItems(
            FilterItemRequest filterItemRequest,
             @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ItemResponse> items = itemService.getFilteredItems(filterItemRequest, page, size);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{itemId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ItemDetailResponse> getItemById(@PathVariable Integer itemId) {
        ItemDetailResponse itemDetail = itemService.getItemById(itemId);
        return ResponseEntity.ok(itemDetail);
    }

}
