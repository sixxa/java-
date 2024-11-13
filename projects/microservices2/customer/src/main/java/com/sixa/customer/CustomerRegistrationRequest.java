package com.sixa.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email) {
}
