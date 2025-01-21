package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private CardCommandService commandService;
    private CardQueryService queryService;


    @Override
    public void create(Card object) {
        commandService.create(object);
    }

    @Override
    public Card getById(UUID id) {
        return queryService.getById(id);
    }
}
