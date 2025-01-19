package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.dto.PasswordUpdateModel;
import com.sixa.virtualguide.dto.ProfileUpdateModel;
import com.sixa.virtualguide.model.User;
import com.sixa.virtualguide.repo.UserRepo;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AccountService accountService;

    public UserService(UserRepo userRepo, AccountService accountService) {
        this.userRepo = userRepo;
        this.accountService = accountService;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public void updateUserFields(User user, ProfileUpdateModel dto) {
        if (user == null || dto == null) {
            throw new IllegalArgumentException("Guide and updated data cannot be null");
        }

        // Check if email is being updated and if it's already in use
        if (dto.getEmail() != null && !dto.getEmail().isBlank() && !dto.getEmail().equals(user.getEmail())) {
            if (accountService.isEmailTaken(dto.getEmail())) {
                throw new IllegalArgumentException("Email is already in use");
            }
            user.getAccount().setEmail(dto.getEmail());
            user.setEmail(dto.getEmail());
        }

        // Update other fields only if they are not null
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getBio() != null) user.setBio(dto.getBio());
        if (dto.getLanguages() != null) user.setLanguages(dto.getLanguages());
        if (dto.getLocation() != null) user.setLocation(dto.getLocation());
        if (isValidPath(String.valueOf(dto.getPicturePaths())) && dto.getPicturePaths() != null && user.getPicturePaths().size() <= 5) user.setPicturePaths(dto.getPicturePaths());
    }

    private boolean isValidPath(String path) {
        return path != null && path.matches("[a-zA-Z0-9/._-]+");
    }

    public void updateUserPassword(User user, PasswordUpdateModel dto) {
        if (user == null || dto == null) {
            throw new IllegalArgumentException("User and updated data cannot be null");
        }

        // Update the password if provided and valid
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashedPassword = accountService.hashPassword(dto.getPassword());
            user.getAccount().setPassword(hashedPassword);
            user.setPassword(hashedPassword);
        }
    }

    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Ensure that the user has an associated account
        if (user.getAccount() != null) {
            // Explicitly delete the associated account
            accountService.deleteAccount(user.getAccount());
        }

        // Now delete the user
        userRepo.delete(user);
    }


}
