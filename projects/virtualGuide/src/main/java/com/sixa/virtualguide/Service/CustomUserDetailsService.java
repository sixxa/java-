package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.Service.AccountService;
import com.sixa.virtualguide.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user account using the email (which is the username)
        Account account = accountService.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Return a Spring Security User object with account details
        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())  // The hashed password stored in the database
                .roles("USER")  // Add roles dynamically if needed (e.g., "USER", "ADMIN")
                .build();
    }
}

