package com.sixa.cqrsbankingapp.web.controller;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.service.auth.AuthService;
import com.sixa.cqrsbankingapp.web.dto.ClientDto;
import com.sixa.cqrsbankingapp.web.dto.LoginRequestDto;
import com.sixa.cqrsbankingapp.web.dto.LoginResponseDto;
import com.sixa.cqrsbankingapp.web.dto.OnCreate;
import com.sixa.cqrsbankingapp.web.dto.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ClientMapper clientMapper;

    @PostMapping("/register")
    public void register(@RequestBody @Validated(OnCreate.class) final ClientDto dto) {
        Client client = clientMapper.fromDto(dto);
        authService.register(client);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Validated final LoginRequestDto dto) {
        return authService.login(dto);
    }
}
