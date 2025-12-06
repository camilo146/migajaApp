package com.migaja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.migaja.model.Story;
import com.migaja.model.Story.StoryStatus;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {
    Page<Story> findByStatus(StoryStatus status, Pageable pageable);

    Page<Story> findByAuthorIdAndStatus(Long authorId, StoryStatus status, Pageable pageable);

    Page<Story> findByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.status = 'PUBLISHED' ORDER BY s.averageRating DESC, s.ratingCount DESC")
    Page<Story> findTopRatedStories(Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.status = 'PUBLISHED' ORDER BY s.createdAt DESC")
    Page<Story> findRecentStories(Pageable pageable);

    @Query("SELECT s FROM Story s WHERE s.status = :status AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.author.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.author.username) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Story> findByStatusAndSearch(StoryStatus status, String search, Pageable pageable);
}
