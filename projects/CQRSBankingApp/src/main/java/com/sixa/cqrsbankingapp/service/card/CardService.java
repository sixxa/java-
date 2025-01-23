package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.service.CommandService;
import com.sixa.cqrsbankingapp.service.QueryService;
import java.util.UUID;

public interface CardService extends QueryService<Card>, CommandService<Card> {

    void createByClientId(UUID clientId);

    boolean existsByNumberAndDate(String number, String date);
}
