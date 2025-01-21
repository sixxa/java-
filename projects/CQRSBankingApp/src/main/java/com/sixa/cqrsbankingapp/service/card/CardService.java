package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.service.CommandService;
import com.sixa.cqrsbankingapp.service.QueryService;

public interface CardService extends QueryService<Card>, CommandService<Card> {
}
