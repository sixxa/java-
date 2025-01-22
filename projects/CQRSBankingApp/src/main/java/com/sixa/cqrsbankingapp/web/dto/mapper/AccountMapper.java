package com.sixa.cqrsbankingapp.web.dto.mapper;

import com.sixa.cqrsbankingapp.domain.model.Account;
import com.sixa.cqrsbankingapp.web.dto.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends Mappable<Account, AccountDto> {
}
