package com.sixa.springbootpr1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
@Entity
public class Product {
    @Id
    private int prodId;
    private String prodName;
    int price;

    public Product() {}
}
