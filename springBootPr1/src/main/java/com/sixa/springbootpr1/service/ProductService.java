package com.sixa.springbootpr1.service;

import com.sixa.springbootpr1.model.Product;
import com.sixa.springbootpr1.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepo repo;

    //List<Product> products =new ArrayList<>(
    //        Arrays.asList(new Product(101, "Iphone", 5000), new Product(102, "Canon camera", 70000), new Product(103, "shure mic", 10000)));

    public List<Product> getProducts() {
        return repo.findAll();
    }


    public Product getProductByID(int prodId) {
        return repo.findById(prodId).orElse(null);
    }

    public void addProduct(Product prod) {
        repo.save(prod);
    }

    public void updateProduct(Product prod) {
        repo.save(prod);
    }

    public void deleteProduct(int prodId) {
        repo.deleteById(prodId);
    }
}
