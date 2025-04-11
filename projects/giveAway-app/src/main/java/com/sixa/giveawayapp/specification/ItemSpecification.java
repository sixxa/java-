package com.sixa.giveawayapp.specification;

import com.sixa.giveawayapp.model.Item;
import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    public static Specification<Item> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Item> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Item> hasMinPrice(Integer minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Item> hasMaxPrice(Integer maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Item> hasLocationCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("location").get("country"), country);
    }

    public static Specification<Item> hasLocationCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("location").get("city"), city);
    }

    public static Specification<Item> hasAddress(String address) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address"), address);
    }

    public static Specification<Item> isForGiveaway() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("price"), 0);
    }

    public static Specification<Item> isNotForGiveaway() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("price"), 0);
    }
}
