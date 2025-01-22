package com.sixa.cqrsbankingapp.service.auth;

import com.sixa.cqrsbankingapp.domain.model.Client;
import com.sixa.cqrsbankingapp.web.dto.LoginRequestDto;
import com.sixa.cqrsbankingapp.web.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto request);

    void register (Client client);

}
