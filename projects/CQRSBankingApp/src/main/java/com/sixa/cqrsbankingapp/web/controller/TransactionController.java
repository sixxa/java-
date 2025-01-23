package com.sixa.cqrsbankingapp.web.controller;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import com.sixa.cqrsbankingapp.service.card.CardService;
import com.sixa.cqrsbankingapp.service.transaction.TransactionService;
import com.sixa.cqrsbankingapp.web.dto.OnCreate;
import com.sixa.cqrsbankingapp.web.dto.TransactionDto;
import com.sixa.cqrsbankingapp.web.dto.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CardService cardService;
    private final TransactionMapper transactionMapper;

    @PostMapping()
    public void create(@RequestBody @Validated(OnCreate.class) TransactionDto dto) {
        if(!cardService.existsByNumberAndDate(
                dto.getTo().getNumber(),
                dto.getTo().getDate()
        )){
            throw new IllegalStateException("card does not exist");
        }
        Transaction transaction = transactionMapper.fromDto(dto);
        transactionService.create(transaction);
    }

    @GetMapping("/{id}/")
    public TransactionDto getById(@PathVariable UUID id) {
        Transaction transaction = transactionService.getById(id);
        return transactionMapper.toDto(transaction);
    }
}
