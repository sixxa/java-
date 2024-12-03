package com.sixa.virtualguide.controller;

import com.sixa.virtualguide.Service.AccountService;
import com.sixa.virtualguide.dto.LoginRequest;
import com.sixa.virtualguide.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1")
public class LogRegController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/reg")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            // Check if email is already taken
            if (accountService.isEmailTaken(account.getEmail())) {
                return ResponseEntity.badRequest().body("Email is already in use.");
            } // Register the user
            Account savedAccount = accountService.registerUser(account);
            return ResponseEntity.ok("Account registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Account account = accountService.findByEmail(loginRequest.getEmail());
            if (account == null) {
                return ResponseEntity.badRequest().body("Account not found.");
            }

            // Compare the entered password with the stored hashed password
            if (passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                return ResponseEntity.ok("Login successful!");
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

}

