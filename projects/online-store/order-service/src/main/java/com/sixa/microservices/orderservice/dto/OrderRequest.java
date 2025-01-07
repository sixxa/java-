package com.sixa.microservices.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNUmber, String skuCode, BigDecimal price, Integer quantity, UserDetails userDetails) {

    public record UserDetails(String email, String firstName, String lastName) {}
}
