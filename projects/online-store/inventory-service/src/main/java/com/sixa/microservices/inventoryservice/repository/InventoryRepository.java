package com.sixa.microservices.inventoryservice.repository;

import com.sixa.microservices.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, int quantity);
}
