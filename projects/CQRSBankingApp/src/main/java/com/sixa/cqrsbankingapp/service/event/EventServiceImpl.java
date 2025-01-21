package com.sixa.cqrsbankingapp.service.event;

import com.sixa.cqrsbankingapp.events.AbstractEvent;
import com.sixa.cqrsbankingapp.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    @Override
    public void create(AbstractEvent event) {
        repository.save(event);
    }
}
