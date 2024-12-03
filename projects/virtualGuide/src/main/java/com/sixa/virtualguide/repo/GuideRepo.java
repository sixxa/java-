package com.sixa.virtualguide.repo;

import com.sixa.virtualguide.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepo extends JpaRepository<Guide, Integer> {
}
