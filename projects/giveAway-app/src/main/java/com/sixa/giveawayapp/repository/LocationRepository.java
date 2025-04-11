package com.sixa.giveawayapp.repository;

import com.sixa.giveawayapp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    Optional<Location> findByCountryAndCity(String country, String city);
}
