package com.sixa.cqrsbankingapp.repository;

import com.sixa.cqrsbankingapp.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
