package com.sixa.cqrsbankingapp.service.auth;

import com.sixa.cqrsbankingapp.domain.exception.ResourceAlreadyExistsException;
import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.client.ClientService;
import com.sixa.cqrsbankingapp.web.dto.LoginRequestDto;
import com.sixa.cqrsbankingapp.web.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientService clientService;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        return null;
    }

    @Override
    public void register(Client client) {
        if (clientService.existsByUsername(client.getUsername())){
            throw new ResourceAlreadyExistsException();
        }
        client.setPassword(client.getPassword());
        clientService.create(client);
    }
}
