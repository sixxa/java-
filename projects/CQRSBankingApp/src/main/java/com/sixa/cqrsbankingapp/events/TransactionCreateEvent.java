package com.sixa.cqrsbankingapp.events;

import com.sixa.cqrsbankingapp.domain.aggregate.Aggregate;
import com.sixa.cqrsbankingapp.domain.model.Transaction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TransactionCreateEvent extends AbstractEvent{

    public TransactionCreateEvent(Transaction payload) {
        super(null,EventType.TRANSACTION_CREATE, payload);
    }

    @Override
    public void apply(Aggregate aggregate) {

    }

}
