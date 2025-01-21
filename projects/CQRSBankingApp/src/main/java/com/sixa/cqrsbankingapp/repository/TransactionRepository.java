package com.sixa.cqrsbankingapp.repository;

import com.sixa.cqrsbankingapp.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}
