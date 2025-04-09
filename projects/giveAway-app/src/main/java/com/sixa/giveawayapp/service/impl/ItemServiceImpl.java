package com.sixa.giveawayapp.service.impl;

import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.mapper.ItemMapper;
import com.sixa.giveawayapp.model.Item;
import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.ItemRepository;
import com.sixa.giveawayapp.repository.UserRepository;
import com.sixa.giveawayapp.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemMapper.toItem(itemRequest);
        item.setUser(user);
        item.setCountry(itemRequest.getCountry());
        item.setCity(itemRequest.getCity());
        item.setAddress(itemRequest.getAddress());

        // Save first to get item ID for images (if needed)
        Item savedItem = itemRepository.save(item);

        // Set `item` for each image
        if (savedItem.getImages() != null) {
            savedItem.getImages().forEach(image -> image.setItem(savedItem));
        }

        return itemMapper.toItemResponse(savedItem);
    }

    @Override
    public List<ItemResponse> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(itemMapper::toItemResponse)
                .toList();
    }

}