package com.sixa.quizzapp.dao;

import com.sixa.quizzapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, Integer> {



}
