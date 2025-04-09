package com.sixa.giveawayapp.service;

import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.model.Item;

import java.util.List;

public interface ItemService {

    ItemResponse createItem(ItemRequest itemRequest, Integer userId);
    List<ItemResponse> getAllItems();
}
