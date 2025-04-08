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
        // Check if the username or email already exists
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // Map RegisterRequest DTO to User entity using MapStruct
        User newUser = userMapper.registerRequestToUser(request);

        // Encode the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);

        // Set default role (you can adjust this depending on your requirements)
        newUser.setRole(Role.USER); // Assuming Role is an enum, adjust as needed

        // Save the new user to the database
        userRepository.save(newUser);
    }

    public String login(LoginRequest request) {
        // Check if the provided value is email or username
        String usernameOrEmail = request.getUsernameOrEmail();
        User user = findUserByUsernameOrEmail(usernameOrEmail);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        // Check if the password is correct
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        // Generate JWT token
        return jwtUtil.generateToken(user.getUsername(), user.getRole());  // Adjust role extraction as necessary
    }

    @Override
    public User findUserByUsernameOrEmail(String usernameOrEmail) {
        if (usernameOrEmail.contains("@")) {
            // It's an email, search by email
            return userRepository.findByEmail(usernameOrEmail);  // Make sure you have an email field in your User model
        } else {
            // It's a username, search by username
            return userRepository.findByUsername(usernameOrEmail);
        }
    }
}
