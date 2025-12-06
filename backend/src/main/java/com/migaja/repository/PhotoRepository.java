package com.migaja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.migaja.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByStoryIdOrderByDisplayOrderAsc(Long storyId);

    void deleteByStoryId(Long storyId);
}
