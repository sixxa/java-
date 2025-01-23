package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardCommandService commandService;
    private final CardQueryService queryService;
    private final ClientService clientService;


    @Override
    public void create(Card object) {
        commandService.create(object);
    }

    @Override
    public void createByClientId(UUID clientId) {
        Client client = clientService.getById(clientId);
        Card card = new Card(client.getAccount());
        commandService.create(card);
    }

    @Override
    public Card getById(UUID id) {
        return queryService.getById(id);
    }

    @Override
    public boolean existsByNumberAndDate(String number, String date) {
        return queryService.existsByNumberAndDate(number, date);
    }
}
