package com.sixa.cqrsbankingapp.service.card;

import com.sixa.cqrsbankingapp.domain.exception.ResourceNotFoundException;
import com.sixa.cqrsbankingapp.domain.model.Card;
import com.sixa.cqrsbankingapp.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardQueryServiceImpl implements CardQueryService {

    private final CardRepository repository;

    @Override
    public Card getById(UUID id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public boolean existsByNumberAndDate(String number, String date) {
        return repository.existsByNumberAndDate(number, date);
    }
}
