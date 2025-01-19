package com.sixa.virtualguide.repo;

import com.sixa.virtualguide.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    // In GuideRepo
    void deleteById(Integer id);  // To delete a guide by ID

}
