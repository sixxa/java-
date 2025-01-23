package com.sixa.cqrsbankingapp.service.transaction;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionCommandService commandService;
    private final TransactionQueryService queryService;

    @Override
    public void create(Transaction object) {
        commandService.create(object);
    }

    @Override
    public Transaction getById(UUID id) {
        return queryService.getById(id);
    }
}
