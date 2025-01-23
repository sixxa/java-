package com.sixa.cqrsbankingapp.web.controller;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.client.ClientService;
import com.sixa.cqrsbankingapp.web.dto.AccountDto;
import com.sixa.cqrsbankingapp.web.dto.CardDto;
import com.sixa.cqrsbankingapp.web.dto.ClientDto;
import com.sixa.cqrsbankingapp.web.dto.mapper.AccountMapper;
import com.sixa.cqrsbankingapp.web.dto.mapper.CardMapper;
import com.sixa.cqrsbankingapp.web.dto.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientMapper clientMapper;
    private final CardMapper cardMapper;
    private final AccountMapper accountMapper;

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable final UUID id) {
        Client client = clientService.getById(id);
        return clientMapper.toDto(client);
    }

    @GetMapping("/{id}/cards")
    public List<CardDto> getCardsById(@PathVariable final UUID id) {
        Client client = clientService.getById(id);
        return cardMapper.toDto(client.getCards());
    }

    @GetMapping("/{id}/account")
    public AccountDto getAccountById(@PathVariable final UUID id) {
        Client client = clientService.getById(id);
        return accountMapper.toDto(client.getAccount());
    }
}
