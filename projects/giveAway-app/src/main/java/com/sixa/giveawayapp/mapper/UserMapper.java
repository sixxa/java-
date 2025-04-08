package com.sixa.giveawayapp.mapper;

import com.sixa.giveawayapp.DTO.request.RegisterRequest;
import com.sixa.giveawayapp.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerRequestToUser(RegisterRequest registerRequest);

}
