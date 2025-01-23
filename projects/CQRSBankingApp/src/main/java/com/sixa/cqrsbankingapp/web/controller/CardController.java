package com.sixa.cqrsbankingapp.web.controller;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.service.card.CardService;
import com.sixa.cqrsbankingapp.web.dto.CardDto;
import com.sixa.cqrsbankingapp.web.dto.TransactionDto;
import com.sixa.cqrsbankingapp.web.dto.mapper.CardMapper;
import com.sixa.cqrsbankingapp.web.dto.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;
    private final TransactionMapper transactionMapper;

    @PostMapping
    public void create() {
        UUID id = UUID.randomUUID();
        cardService.createByClientId(id);
    }

    @GetMapping("/{id}")
    public CardDto getById(@PathVariable UUID id) {
        Card card = cardService.getById(id);
        return cardMapper.toDto(card);
    }

    @GetMapping("/{id}/transactions")
    public List<TransactionDto> getTransactionsById(@PathVariable UUID id) {
        Card card = cardService.getById(id);
        return transactionMapper.toDto(card.getTransactions());
    }
}
