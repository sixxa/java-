package com.sixa.virtualguide.Service;

import com.sixa.virtualguide.model.Account;
import com.sixa.virtualguide.repo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepo accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the user account using the email (which is the username)
        Optional<Account> account = accountRepo.findByEmail(username);
        if (account.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println(account.get().getRole().name());
        // Return a Spring Security User object with account details
        return User.builder()
                .username(account.get().getEmail())
                .password(account.get().getPassword())  // The hashed password stored in the database
                .authorities(new SimpleGrantedAuthority(account.get().getRole().name()))
                .build();
    }
}

