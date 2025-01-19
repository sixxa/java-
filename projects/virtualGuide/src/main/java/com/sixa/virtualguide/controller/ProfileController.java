package com.sixa.virtualguide.controller;

import com.sixa.virtualguide.Service.AccountService;
import com.sixa.virtualguide.Service.GuideService;
import com.sixa.virtualguide.Service.JWTService;
import com.sixa.virtualguide.Service.UserService;
import com.sixa.virtualguide.dto.PasswordUpdateModel;
import com.sixa.virtualguide.dto.ProfileUpdateModel;
import com.sixa.virtualguide.model.Guide;
import com.sixa.virtualguide.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    @Autowired
    private JWTService jwtService;

    // Set the maximum allowed size for the Base64 string (e.g., 5MB image file becomes 6.5MB Base64)
    private static final long MAX_BASE64_SIZE = 8 * 1024 * 1024;  // 6MB limit for Base64 encoded string

//    // This endpoint is only accessible by users with 'ROLE_USER' or 'ROLE_ADMIN'
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")


    @Autowired
    private final UserService userService;
    @Autowired
    private final GuideService guideService;
    @Autowired
    private AccountService accountService;

    public ProfileController(UserService userService, GuideService guideService) {
        this.userService = userService;
        this.guideService = guideService;

    }

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(@RequestBody ProfileUpdateModel profileUpdateModel, @RequestHeader("Authorization") String token) {
        try {
            // Remove any leading/trailing whitespace from the token string
            String cleanToken = token.replace("Bearer ", "").trim();

            // Extract role from JWT
            String role = jwtService.extractRole(cleanToken);
            String email = jwtService.extractUserName(cleanToken);

            switch (role) {
                case "ROLE_USER":
                    // Check if the email exists for User
                    Optional<User> user = userService.getUserByEmail(email);
                    if (user.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                    }
                    updateUserFields(user.orElse(null), profileUpdateModel);
                    userService.saveUser(user.orElse(null));
                    return ResponseEntity.ok("User profile updated successfully");

                case "ROLE_GUIDE":
                    // Check if the email exists for Guide
                    Optional<Guide> guide = guideService.getGuideByEmail(email);
                    if (guide.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guide not found");
                    }
                    updateGuideFields(guide.orElse(null), profileUpdateModel);
                    guideService.saveGuide(guide.orElse(null));
                    return ResponseEntity.ok("Guide profile updated successfully");

                default:
                    return ResponseEntity.badRequest().body("Invalid role in JWT token");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    @PutMapping("/editpassword")
    public ResponseEntity<?> editPassword(@RequestBody PasswordUpdateModel passwordUpdateModel, @RequestHeader("Authorization") String token) {
        try {
            // Remove any leading/trailing whitespace from the token string
            String cleanToken = token.replace("Bearer ", "").trim();

            // Extract role from JWT
            String role = jwtService.extractRole(cleanToken);
            String email = jwtService.extractUserName(cleanToken);

            switch (role) {
                case "ROLE_USER":
                    // Check if the email exists for User
                    Optional<User> user = userService.getUserByEmail(email);
                    if (user.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                    }
                    updateUserPassword(user.orElse(null), passwordUpdateModel);
                    userService.saveUser(user.orElse(null));
                    return ResponseEntity.ok("User profile updated successfully");

                case "ROLE_GUIDE":
                    // Check if the email exists for Guide
                    Optional<Guide> guide = guideService.getGuideByEmail(email);
                    if (guide.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guide not found");
                    }
                    updateGuidePassword(guide.orElse(null), passwordUpdateModel);
                    guideService.saveGuide(guide.orElse(null));
                    return ResponseEntity.ok("Guide profile updated successfully");

                default:
                    return ResponseEntity.badRequest().body("Invalid role in JWT token");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(@RequestHeader("Authorization") String token) {
        try {
            // Remove any leading/trailing whitespace from the token string
            String cleanToken = token.replace("Bearer ", "").trim();

            // Extract role and email from JWT
            String role = jwtService.extractRole(cleanToken);
            String email = jwtService.extractUserName(cleanToken);

            switch (role) {
                case "ROLE_USER":
                    // Check if the email exists for User
                    Optional<User> user = userService.getUserByEmail(email);
                    if (user.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                    }
                    userService.deleteUser(user.orElse(null));
                    return ResponseEntity.ok("User deleted successfully");

                case "ROLE_GUIDE":
                    // Check if the email exists for Guide
                    Optional<Guide> guide = guideService.getGuideByEmail(email);
                    if (guide.isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Guide not found");
                    }
                    guideService.deleteGuide(guide.orElse(null));
                    return ResponseEntity.ok("Guide deleted successfully");

                default:
                    return ResponseEntity.badRequest().body("Invalid role in JWT token");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting profile: " + e.getMessage());
        }
    }



        private void updateUserFields(User user, ProfileUpdateModel dto) {
        userService.updateUserFields(user, dto); // Delegate to service
    }

    private void updateGuideFields(Guide guide, ProfileUpdateModel dto) {
        guideService.updateGuideFields(guide, dto); // Delegate to service
    }

    private void updateUserPassword(User user, PasswordUpdateModel dto) {
        userService.updateUserPassword(user, dto); // Delegate to service
    }

    private void updateGuidePassword(Guide guide, PasswordUpdateModel dto) {
        guideService.updateGuidePassword(guide, dto); // Delegate to service
    }


}
