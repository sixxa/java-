package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.model.Account;
import com.sixa.virtualguide.model.Guide;
import com.sixa.virtualguide.model.Role;
import com.sixa.virtualguide.model.User;
import com.sixa.virtualguide.repo.AccountRepo;
import com.sixa.virtualguide.repo.GuideRepo;
import com.sixa.virtualguide.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserRepo userRepo;  // For User repository

    @Autowired
    private GuideRepo guideRepo;  // For Guide repository

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public Account register(Account account) {
        // Hash the password before saving
        String hashedPassword = encoder.encode(account.getPassword());
        account.setPassword(hashedPassword);

        // Save the account to the database
        Account savedAccount = accountRepo.save(account);

        // Handle User or Guide creation based on role
        if (account.getRole() == Role.ROLE_USER) {
            User user = new User();
            user.setEmail(account.getEmail());
            user.setPassword(account.getPassword());  // Assume password is already hashed
            user.setRole(account.getRole());
            user.setAccount(savedAccount);  // Set the account reference for User
            userRepo.save(user);
        } else if (account.getRole() == Role.ROLE_GUIDE) {
            Guide guide = new Guide();
            guide.setEmail(account.getEmail());
            guide.setPassword(account.getPassword());  // Assume password is already hashed
            guide.setRole(account.getRole());
            guide.setAccount(savedAccount);  // Set the account reference for Guide
            guideRepo.save(guide);
        }

        return savedAccount;
    }

    public boolean isEmailTaken(String email) {
        Optional<Account> existingAccount = accountRepo.findByEmail(email);
        return existingAccount.isPresent();
    }

    public Account findByEmail(String email) {
        return accountRepo.findByEmail(email).orElse(null); // Return null if not found
    }

    public String generatePasswordResetToken(Account account) {
        String token = UUID.randomUUID().toString();
        // Save the token and set an expiration (e.g., 1 hour) in the Account model
        account.setPasswordResetToken(token);
        account.setTokenExpiration(new Date(System.currentTimeMillis() + 3600000)); // 1 hour expiration
        accountRepo.save(account);
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        Account account = accountRepo.findByPasswordResetToken(token);
        if (account != null && account.getTokenExpiration().after(new Date())) {
            // Token is valid
            return true;
        }
        return false;
    }

    public void updatePassword(String email, String newPassword) {
        try {
            Optional<Account> account = accountRepo.findByEmail(email);

            // Check if the account is present
            if (account.isPresent()) {
                // Get the actual Account object from the Optional
                Account accountEntity = account.get();

                // Ensure the password is encoded
                accountEntity.setPassword(encoder.encode(newPassword));

                // Save the updated account entity
                Account savedAccount = accountRepo.save(accountEntity);

                // Handle User or Guide creation based on role
                if (accountEntity.getRole() == Role.ROLE_USER) {
                    Optional<User> user = userRepo.findByEmail(accountEntity.getEmail());
                    if (user.isPresent()) {
                        user.get().setPassword(accountEntity.getPassword());  // Assume password is already hashed
                        userRepo.save(user.get());  // Pass the entity (user.get())
                    }
                } else if (accountEntity.getRole() == Role.ROLE_GUIDE) {
                    Optional<Guide> guide = guideRepo.findByEmail(accountEntity.getEmail());
                    if (guide.isPresent()) {
                        guide.get().setPassword(accountEntity.getPassword());  // Assume password is already hashed
                        guideRepo.save(guide.get());  // Pass the entity (guide.get())
                    }
                }
            } else {
                // Handle case where account is not found
                throw new AccountNotFoundException("Account not found.");
            }
        } catch (AccountNotFoundException e) {
            // Log the exception or take appropriate action
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        // Delete the account
        accountRepo.delete(account);
    }
}
