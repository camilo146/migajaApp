package com.migaja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.migaja.model.StoryView;

@Repository
public interface StoryViewRepository extends JpaRepository<StoryView, Long> {
    boolean existsByStoryIdAndUserId(Long storyId, Long userId);

    void deleteByStoryId(Long storyId);
}
