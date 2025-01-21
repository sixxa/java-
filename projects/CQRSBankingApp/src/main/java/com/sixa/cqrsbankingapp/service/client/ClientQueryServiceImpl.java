package com.sixa.cqrsbankingapp.service.client;

import com.sixa.cqrsbankingapp.domain.exception.ResourceNotFoundException;
import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientQueryServiceImpl implements ClientQueryService {

    private final ClientRepository repository;

    @Override
    public Client getById(UUID id) {
        return repository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }
}
