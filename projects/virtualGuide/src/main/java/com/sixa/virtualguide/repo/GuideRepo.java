package com.sixa.virtualguide.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sixa.virtualguide.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface GuideRepo extends JpaRepository<Guide, Integer> {
    Optional<Guide> findByEmail(String email);

    @Query("SELECT g FROM Guide g WHERE " +
            "(:keyword IS NULL OR LOWER(g.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(g.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(g.bio) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(g.location.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            " LOWER(g.location.country) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:minPrice IS NULL OR g.pricePerHour >= :minPrice) " +
            "AND (:maxPrice IS NULL OR g.pricePerHour <= :maxPrice)")
    Page<Guide> findGuidesWithPaging(@Param("keyword") String keyword,
                                     @Param("minPrice") Integer minPrice,
                                     @Param("maxPrice") Integer maxPrice,
                                     Pageable pageable);


    Page<Guide> findAllByOrderByPricePerHourAsc(Pageable pageable);

    Page<Guide> findAllByOrderByPricePerHourDesc(Pageable pageable);


    // In GuideRepo
    void deleteById(Integer id);  // To delete a guide by ID

}
