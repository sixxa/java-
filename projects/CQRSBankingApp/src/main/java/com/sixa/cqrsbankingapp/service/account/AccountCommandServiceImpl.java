package com.sixa.cqrsbankingapp.service.account;

import com.sixa.cqrsbankingapp.domain.model.Account;
import com.sixa.cqrsbankingapp.events.AccountCreateEvent;
import com.sixa.cqrsbankingapp.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCommandServiceImpl implements AccountCommandService {

    private final EventService eventService;

    @Override
    public void create(Account object) {
        AccountCreateEvent event = new AccountCreateEvent(object);
        eventService.create(event);
    }
}
