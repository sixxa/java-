package com.sixa.giveawayapp.service.impl;

import com.sixa.giveawayapp.DTO.request.LoginRequest;
import com.sixa.giveawayapp.DTO.request.RegisterRequest;
import com.sixa.giveawayapp.mapper.UserMapper;
import com.sixa.giveawayapp.model.Role;
import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.UserRepository;
import com.sixa.giveawayapp.common.security.JwtUtil;
import com.sixa.giveawayapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = userMapper.registerRequestToUser(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);

        newUser.setRole(Role.USER);

        userRepository.save(newUser);
    }

    public String login(LoginRequest request) {
        String usernameOrEmail = request.getUsernameOrEmail();
        User user = findUserByUsernameOrEmail(usernameOrEmail);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }

    @Override
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            return userRepository.findByEmail(usernameOrEmail);
        } else {
            return userRepository.findByUsername(usernameOrEmail);
        }
    }
}
