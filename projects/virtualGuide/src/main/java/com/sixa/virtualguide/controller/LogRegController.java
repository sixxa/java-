package com.sixa.virtualguide.controller;

import com.sixa.virtualguide.Service.AccountService;
import com.sixa.virtualguide.Service.EmailService;
import com.sixa.virtualguide.Service.JWTService;
import com.sixa.virtualguide.dto.LoginRequestModel;
import com.sixa.virtualguide.dto.ResetPasswordRequest;
import com.sixa.virtualguide.model.Account;
import com.sixa.virtualguide.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController()
@RequestMapping("/api/v1/auth")
public class LogRegController {

    @Autowired
    private AccountService accountService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/reg")
    public ResponseEntity<?> registerUser(@RequestBody Account account) {
        try {
            // Check if email is already taken
            if (accountService.isEmailTaken(account.getEmail())) {
                return ResponseEntity.badRequest().body("Email is already in use.");
            } // Register the user
            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok("Account registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/log")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestModel loginRequest) {
        try {
            // Authenticate the user using email and password
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // If authentication is successful, generate a token
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Get the Account object by email
                Account account = accountService.findByEmail(loginRequest.getEmail());
                if (account == null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account not found.");
                }

                // Retrieve the Role from the Account object
                Role role = account.getRole();  // Assuming `getRole()` returns a Role enum

                // Generate the token with email and role
                String token = jwtService.generateToken(loginRequest.getEmail(), role);

                return ResponseEntity.ok(Map.of("token", token)); // Return token in JSON format
            }
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password.");
        }

        // Return failure response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        Account account = accountService.findByEmail(email);
        if (account != null) {
            // Generate token and send reset link
            String token = accountService.generatePasswordResetToken(account);
            String resetLink = "http://localhost:8080/api/v1/auth/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not found.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isValid = accountService.validatePasswordResetToken(request.getToken());
        if (isValid) {
            accountService.updatePassword(request.getEmail(), request.getNewPassword());
            return ResponseEntity.ok("Password has been successfully reset.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
    }

}

