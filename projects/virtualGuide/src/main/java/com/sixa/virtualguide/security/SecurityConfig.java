package com.sixa.virtualguide.security;

import com.sixa.virtualguide.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Define password encoder bean
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();  // Specify your custom UserDetailsService
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Disable CSRF (can be enabled if needed)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/**")
                        .permitAll()  // Allow registration and login without authentication
                        .anyRequest().authenticated()  // Require authentication for other requests
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}
