package com.migaja.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.migaja.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByUserIdAndStoryId(Long userId, Long storyId);

    boolean existsByUserIdAndStoryId(Long userId, Long storyId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.story.id = :storyId")
    Double calculateAverageRating(@Param("storyId") Long storyId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.story.id = :storyId")
    Integer countByStoryId(@Param("storyId") Long storyId);

    void deleteByStoryId(Long storyId);
}
