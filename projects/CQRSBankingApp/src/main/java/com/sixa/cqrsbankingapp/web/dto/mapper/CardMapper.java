package com.sixa.cqrsbankingapp.web.dto.mapper;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.web.dto.CardDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper extends Mappable<Card, CardDto> {
}
