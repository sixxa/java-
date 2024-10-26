package com.sixa.springbootpr1.repository;

import com.sixa.springbootpr1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface ProductRepo extends JpaRepository<Product, Integer>{

}
