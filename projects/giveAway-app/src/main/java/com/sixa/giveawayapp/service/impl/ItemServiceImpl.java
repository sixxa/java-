package com.sixa.giveawayapp.service.impl;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.mapper.ItemMapper;
import com.sixa.giveawayapp.model.Item;
import com.sixa.giveawayapp.model.Location;
import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.ItemRepository;
import com.sixa.giveawayapp.repository.LocationRepository;
import com.sixa.giveawayapp.repository.UserRepository;
import com.sixa.giveawayapp.service.ItemService;
import com.sixa.giveawayapp.specification.ItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get or create location
        Location location = locationRepository.findByCountryAndCity(
                itemRequest.getCountry(), itemRequest.getCity()
        ).orElseGet(() -> {
            Location newLocation = new Location();
            newLocation.setCountry(itemRequest.getCountry());
            newLocation.setCity(itemRequest.getCity());
            return locationRepository.save(newLocation);
        });

        Item item = itemMapper.toItem(itemRequest);
        item.setUser(user);
        item.setLocation(location);
        item.setAddress(itemRequest.getAddress());

        // Save first to get item ID for images
        Item savedItem = itemRepository.save(item);

        if (savedItem.getImages() != null) {
            savedItem.getImages().forEach(image -> image.setItem(savedItem));
        }

        return itemMapper.toItemResponse(savedItem);
    }

    @Override
    public Page<ItemResponse> getFilteredItems(FilterItemRequest request, int page, int size) {
        Specification<Item> spec = Specification.where(null);

        if (request.getName() != null) {
            spec = spec.and(ItemSpecification.hasName(request.getName()));
        }

        if (request.getCategory() != null) {
            spec = spec.and(ItemSpecification.hasCategory(request.getCategory()));
        }

        if (request.getForGiveaway() != null) {
            spec = spec.and(request.getForGiveaway()
                    ? ItemSpecification.isForGiveaway()
                    : ItemSpecification.isNotForGiveaway());
        }

        if (request.getMinPrice() != null) {
            spec = spec.and(ItemSpecification.hasMinPrice(request.getMinPrice()));
        }

        if (request.getMaxPrice() != null) {
            spec = spec.and(ItemSpecification.hasMaxPrice(request.getMaxPrice()));
        }

        // Use nested location filters
        if (request.getCountry() != null) {
            spec = spec.and(ItemSpecification.hasLocationCountry(request.getCountry()));
        }

        if (request.getCity() != null) {
            spec = spec.and(ItemSpecification.hasLocationCity(request.getCity()));
        }

        if (request.getAddress() != null) {
            spec = spec.and(ItemSpecification.hasAddress(request.getAddress()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> itemPage = itemRepository.findAll(spec, pageable);

        return itemPage.map(itemMapper::toItemResponse);
    }
}