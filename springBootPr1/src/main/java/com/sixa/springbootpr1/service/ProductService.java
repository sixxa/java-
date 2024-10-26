package com.sixa.springbootpr1.service;

import com.sixa.springbootpr1.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    List<Product> products = Arrays.asList(new Product(101, "Iphone", 5000), new Product(102, "Canon camera", 70000), new Product(103, "shure mic", 10000));

    public List<Product> getProducts() {
        return products;
    }


    public Product getProductByID(int prodId) {
        return products.stream()
                .filter(p -> p.getProdId() == prodId)
                .findFirst().orElse(new Product(100, "No Item", 0));
    }
}
