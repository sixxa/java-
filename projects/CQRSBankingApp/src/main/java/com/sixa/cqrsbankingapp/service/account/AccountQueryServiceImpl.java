package com.sixa.cqrsbankingapp.service.account;

import com.sixa.cqrsbankingapp.domain.exception.ResourceNotFoundException;
import com.sixa.cqrsbankingapp.domain.model.Account;
import com.sixa.cqrsbankingapp.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository repository;


    @Override
    public Account getById(UUID id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }
}
