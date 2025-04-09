package com.sixa.giveawayapp.mapper;

import com.sixa.giveawayapp.DTO.request.ItemRequest;
import com.sixa.giveawayapp.DTO.response.ItemResponse;
import com.sixa.giveawayapp.DTO.SimpleUserDTO;
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
    Item toItem(ItemRequest itemRequest);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "images", target = "images", qualifiedByName = "imageListToStringList")
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

}
