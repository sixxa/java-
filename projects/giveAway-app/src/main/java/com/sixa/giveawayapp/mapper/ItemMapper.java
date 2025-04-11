package com.sixa.giveawayapp.mapper;

import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.DTO.SimpleUserDTO;
import com.sixa.giveawayapp.model.Category;
import com.sixa.giveawayapp.model.Image;
import com.sixa.giveawayapp.model.Item;
import com.sixa.giveawayapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(source = "images", target = "images", qualifiedByName = "stringListToImageList")
    @Mapping(source = "category", target = "category", qualifiedByName = "categoryNameToCategory")  // Map category name to Category entity
    Item toItem(ItemRequest itemRequest);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "images", target = "images", qualifiedByName = "imageListToStringList")
    @Mapping(source = "location.country", target = "country")
    @Mapping(source = "location.city", target = "city")
    @Mapping(source = "category", target = "category", qualifiedByName = "categoryToCategoryName")  // Map Category entity to category name
    ItemResponse toItemResponse(Item item);

    SimpleUserDTO toSimpleUserDTO(User user);

    @Named("stringListToImageList")
    default List<Image> stringListToImageList(List<String> imagePaths) {
        if (imagePaths == null) {
            return null;
        }

        return imagePaths.stream().map(path -> {
            Image image = new Image();
            image.setImagePath(path);
            return image;
        }).collect(Collectors.toList());
    }

    @Named("imageListToStringList")
    default List<String> imageListToStringList(List<Image> images) {
        if (images == null) {
            return null;
        }

        return images.stream()
                .map(Image::getImagePath)
                .collect(Collectors.toList());
    }

    // Method to convert category name (String) to Category entity
    @Named("categoryNameToCategory")
    default Category categoryNameToCategory(String categoryName) {
        if (categoryName == null) {
            return null;
        }

        // Assuming you have a method to fetch Category from the database (or other sources)
        Category category = new Category();
        category.setName(categoryName); // Set category name (modify as needed for actual category fetching logic)
        return category;
    }

    // Method to convert Category entity to category name (String)
    @Named("categoryToCategoryName")
    default String categoryToCategoryName(Category category) {
        if (category == null) {
            return null;
        }

        return category.getName();  // Assuming category has a 'name' field
    }
}
