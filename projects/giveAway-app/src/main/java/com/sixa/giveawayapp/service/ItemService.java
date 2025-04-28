package com.sixa.giveawayapp.service;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemDetailResponse;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import org.springframework.data.domain.Page;

public interface ItemService {

    ItemResponse createItem(ItemRequest itemRequest, Integer userId);
    ItemResponse editItem(Integer id, ItemRequest itemRequest, Integer userId);
    Page<ItemResponse> getFilteredItems(FilterItemRequest filterItemRequest, int page, int size);
    ItemDetailResponse getItemById(Integer itemId);
    void deleteItem(Integer itemId, Integer userId);

}
