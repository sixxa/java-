package com.sixa.cqrsbankingapp.repository;

import com.sixa.cqrsbankingapp.events.AbstractEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<AbstractEvent, Long> {
}
