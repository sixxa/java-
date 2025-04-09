package com.sixa.giveawayapp.service.impl;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.mapper.ItemMapper;
import com.sixa.giveawayapp.model.Item;
import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.ItemRepository;
import com.sixa.giveawayapp.repository.UserRepository;
import com.sixa.giveawayapp.service.ItemService;
import com.sixa.giveawayapp.specification.ItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemResponse> getAllItems(FilterItemRequest request) {
        Specification<Item> spec = Specification.where(null);

        if (request.getName() != null) {
            spec = spec.and(ItemSpecification.hasName(request.getName()));
        }

        if (request.getCategory() != null) {
            spec = spec.and(ItemSpecification.hasCategory(request.getCategory()));
        }

        if (request.getForGiveaway() != null) {
            if (request.getForGiveaway()) {
                spec = spec.and(ItemSpecification.isForGiveaway());
            } else {
                spec = spec.and(ItemSpecification.isNotForGiveaway());
            }
        }

// Apply price filters regardless of the giveaway flag
        if (request.getMinPrice() != null) {
            spec = spec.and(ItemSpecification.hasMinPrice(request.getMinPrice()));
        }
        if (request.getMaxPrice() != null) {
            spec = spec.and(ItemSpecification.hasMaxPrice(request.getMaxPrice()));
        }


        if (request.getCountry() != null) {
            spec = spec.and(ItemSpecification.hasCountry(request.getCountry()));
        }

        if (request.getCity() != null) {
            spec = spec.and(ItemSpecification.hasCity(request.getCity()));
        }

        if (request.getAddress() != null) {
            spec = spec.and(ItemSpecification.hasAddress(request.getAddress()));
        }

        List<Item> items = itemRepository.findAll(spec);
        return items.stream().map(itemMapper::toItemResponse).toList();
    }



}