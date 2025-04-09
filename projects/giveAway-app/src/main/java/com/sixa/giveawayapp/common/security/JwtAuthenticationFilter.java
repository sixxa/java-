package com.sixa.giveawayapp.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                jwt = authHeader.substring(7);
                logger.debug("Processing JWT token: {}", jwt);

                if (!jwt.matches("^[\\x20-\\x7E]+$")) {
                    logger.warn("JWT token contains non-ASCII characters: {}", jwt);
                    filterChain.doFilter(request, response);
                    return;
                }

                userId = jwtUtil.extractUserId(jwt);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    logger.debug("Extracted userId from token: {}", userId);

                    request.setAttribute("userId", Integer.valueOf(userId)); // Make sure you convert userId to Integer here.

                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);  // Using loadUserByUsername now

                        if (jwtUtil.validateToken(jwt, userId)) {
                            String role = jwtUtil.extractRole(jwt);

                            List<GrantedAuthority> authorities = Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + role)
                            );

                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userId,
                                    null,
                                    authorities
                            );

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                            logger.debug("Authentication successful for user ID: {}", userId);
                        }
                    } catch (Exception e) {
                        logger.error("Error loading user details: " + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                logger.error("JWT authentication failed: " + e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }


}