package com.sixa.cqrsbankingapp.service.client;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.events.ClientCreateEvent;
import com.sixa.cqrsbankingapp.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientCommandServiceImpl implements ClientCommandService {

    private EventService eventService;

    @Override
    public void create(Client object) {
        ClientCreateEvent event = new ClientCreateEvent(object);
        eventService.create(event);
    }
}
