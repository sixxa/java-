package com.sixa.giveawayapp.common.security;

import com.sixa.giveawayapp.model.User;
import com.sixa.giveawayapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Integer userId = Integer.valueOf(username);  // Assuming 'username' is actually the userId passed as string

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

            return new org.springframework.security.core.userdetails.User(
                    user.getId().toString(), // Store ID as the username in the authentication
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        }
    }
