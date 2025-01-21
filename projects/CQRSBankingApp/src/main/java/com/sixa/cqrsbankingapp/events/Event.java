package com.sixa.cqrsbankingapp.events;

import com.sixa.cqrsbankingapp.domain.aggregate.Aggregate;

public interface Event {

    void apply(Aggregate aggregate);
}
