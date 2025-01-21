package com.sixa.cqrsbankingapp.service.transaction;

import com.sixa.cqrsbankingapp.domain.exception.ResourceNotFoundException;
import com.sixa.cqrsbankingapp.domain.model.Transaction;
import com.sixa.cqrsbankingapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionQueryServiceImpl implements TransactionQueryService {

    private final TransactionRepository repository;

    @Override
    public Transaction getById(UUID id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }
}
