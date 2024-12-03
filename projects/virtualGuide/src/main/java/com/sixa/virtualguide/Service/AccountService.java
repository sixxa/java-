package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.model.Account;
import com.sixa.virtualguide.model.Guide;
import com.sixa.virtualguide.model.Role;
import com.sixa.virtualguide.model.User;
import com.sixa.virtualguide.repo.AccountRepo;
import com.sixa.virtualguide.repo.GuideRepo;
import com.sixa.virtualguide.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserRepo userRepo;  // For User repository

    @Autowired
    private GuideRepo guideRepo;  // For Guide repository

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account registerUser(Account account) {
        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);

        // Save the account to the database
        Account savedAccount = accountRepo.save(account);

        // Handle User or Guide creation based on role
        if (account.getRole() == Role.USER) {
            User user = new User();
            user.setEmail(account.getEmail());
            user.setPassword(account.getPassword());  // Assume password is already hashed
            user.setRole(account.getRole());
            // Set any other user-specific properties
            userRepo.save(user);
        } else if (account.getRole() == Role.GUIDE) {
            Guide guide = new Guide();
            guide.setEmail(account.getEmail());
            guide.setPassword(account.getPassword());  // Assume password is already hashed
            guide.setRole(account.getRole());
            // Set any other guide-specific properties
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

    public boolean verifyPassword(String rawPassword, String storedPassword) {
        // Since you're using password hashing, we compare hashed passwords
        return passwordEncoder.matches(rawPassword, storedPassword);
    }
}
