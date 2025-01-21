package com.sixa.cqrsbankingapp.repository;

import com.sixa.cqrsbankingapp.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}
