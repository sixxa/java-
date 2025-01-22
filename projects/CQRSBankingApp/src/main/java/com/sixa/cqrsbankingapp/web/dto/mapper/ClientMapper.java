package com.sixa.cqrsbankingapp.web.dto.mapper;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.web.dto.ClientDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper extends Mappable<Client, ClientDto> {
}
