package com.sixa.giveawayapp.service;

import com.sixa.giveawayapp.DTO.request.LoginRequest;
import com.sixa.giveawayapp.DTO.request.RegisterRequest;
import com.sixa.giveawayapp.model.User;

public interface AuthService {

    String login(LoginRequest loginRequest);

    void register(RegisterRequest signupRequest);

    User findUserByUsernameOrEmail(String usernameOrEmail);
}
