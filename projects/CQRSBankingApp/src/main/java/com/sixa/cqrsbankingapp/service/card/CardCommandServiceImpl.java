package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.events.CardCreateEvent;
import com.sixa.cqrsbankingapp.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardCommandServiceImpl implements CardCommandService {

    private final EventService eventService;

    @Override
    public void create(Card object) {
        CardCreateEvent event = new CardCreateEvent(object);
        eventService.create(event);
    }
}
