package com.migaja.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.migaja.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByStoryIdOrderByCreatedAtDesc(Long storyId, Pageable pageable);

    int countByStoryId(Long storyId);

    void deleteByStoryId(Long storyId);
}
