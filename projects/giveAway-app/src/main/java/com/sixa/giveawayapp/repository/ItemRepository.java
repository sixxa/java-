package com.sixa.giveawayapp.repository;

import com.sixa.giveawayapp.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByUserId(Integer userId);

    List<Item> findByCategory(String category);
}
