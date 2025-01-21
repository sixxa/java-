package com.sixa.cqrsbankingapp.service.event;

import com.sixa.cqrsbankingapp.events.AbstractEvent;

public interface EventService {

    void create(AbstractEvent event);
}
