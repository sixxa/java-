package com.sixa.microservices.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(Long id, String orderNUmber, String skuCode, BigDecimal price, Integer quantity) {
}
