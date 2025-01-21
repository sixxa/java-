package com.sixa.cqrsbankingapp.service.transaction;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import com.sixa.cqrsbankingapp.events.TransactionCreateEvent;
import com.sixa.cqrsbankingapp.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionCommandServiceImpl implements TransactionCommandService{

    private EventService eventService;

    @Override
    public void create(Transaction object) {
        TransactionCreateEvent event = new TransactionCreateEvent(object);
        eventService.create(event);
    }
}
