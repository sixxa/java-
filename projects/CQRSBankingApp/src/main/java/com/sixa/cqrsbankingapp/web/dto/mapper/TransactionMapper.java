package com.sixa.cqrsbankingapp.web.dto.mapper;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import com.sixa.cqrsbankingapp.web.dto.TransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper extends Mappable<Transaction, TransactionDto> {
}
