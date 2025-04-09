package com.sixa.giveawayapp.service;

import com.sixa.giveawayapp.DTO.request.LoginRequest;
import com.sixa.giveawayapp.DTO.request.RegisterRequest;
import com.sixa.giveawayapp.model.User;

import java.util.Optional;

public interface AuthService {

    String login(LoginRequest loginRequest);

    void register(RegisterRequest signupRequest);

    Optional<User> findUserByUsernameOrEmail(String usernameOrEmail);
}
