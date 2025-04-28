package com.sixa.giveawayapp.service.impl;

import com.sixa.giveawayapp.DTO.request.FilterItemRequest;
import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemDetailResponse;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.mapper.ItemMapper;
import com.sixa.giveawayapp.model.Category;
import com.sixa.giveawayapp.model.Item;
import com.sixa.giveawayapp.model.Location;
import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponse createItem(ItemRequest itemRequest, Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get or return a message if the category doesn't exist
        Category category = categoryRepository.findByNameIgnoreCase(itemRequest.getCategory())
                .orElseThrow(() -> new RuntimeException("Category '" + itemRequest.getCategory() + "' does not exist"));

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
        item.setCategory(category);
        item.setAddress(itemRequest.getAddress());

        Item savedItem = itemRepository.save(item);

        if (savedItem.getImages() != null) {
            savedItem.getImages().forEach(image -> image.setItem(savedItem));
        }

        return itemMapper.toItemResponse(savedItem);
    }

    @Override
    public ItemResponse editItem(Integer id, ItemRequest itemRequest, Integer userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to edit this item");
        }

        if (itemRequest.getName() != null && !itemRequest.getName().trim().isEmpty()) {
            item.setName(itemRequest.getName());
        }
        if (itemRequest.getDescription() != null && !itemRequest.getDescription().trim().isEmpty()) {
            item.setDescription(itemRequest.getDescription());
        }
        if (itemRequest.getPrice() != null) {
            item.setPrice(itemRequest.getPrice());
        }
        if (itemRequest.getAddress() != null && !itemRequest.getAddress().trim().isEmpty()) {
            item.setAddress(itemRequest.getAddress());
        }

        if (itemRequest.getCountry() != null && itemRequest.getCity() != null &&
                !itemRequest.getCountry().trim().isEmpty() && !itemRequest.getCity().trim().isEmpty()) {
            Location location = locationRepository.findByCountryAndCity(
                    itemRequest.getCountry(), itemRequest.getCity()
            ).orElseGet(() -> {
                Location newLocation = new Location();
                newLocation.setCountry(itemRequest.getCountry());
                newLocation.setCity(itemRequest.getCity());
                return locationRepository.save(newLocation);
            });
            item.setLocation(location);
        }

        // Get or return a message if the category doesn't exist
        if (itemRequest.getCategory() != null && !itemRequest.getCategory().trim().isEmpty()) {
            Category category = categoryRepository.findByNameIgnoreCase(itemRequest.getCategory())
                    .orElseThrow(() -> new RuntimeException("Category '" + itemRequest.getCategory() + "' does not exist"));
            item.setCategory(category);
        }

        if (itemRequest.getImages() != null && !itemRequest.getImages().isEmpty()) {
            item.setImages(itemMapper.stringListToImageList(itemRequest.getImages()));
        }

        Item updatedItem = itemRepository.save(item);

        return itemMapper.toItemResponse(updatedItem);
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

    @Override
    public ItemDetailResponse getItemById(Integer itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return itemMapper.toItemDetailResponse(item);
    }

}
