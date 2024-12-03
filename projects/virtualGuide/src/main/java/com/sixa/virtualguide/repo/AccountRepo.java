package com.sixa.virtualguide.repo;

import com.sixa.virtualguide.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
}
