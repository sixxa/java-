package com.sixa.springbootpr1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class Product {
    private int prodId;
    private String prodName;
    int price;

    public Product() {}
}
