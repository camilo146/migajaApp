package com.migaja.controller;

import com.migaja.dto.CommentRequest;
import com.migaja.dto.CommentResponse;
import com.migaja.dto.RatingRequest;
import com.migaja.dto.StoryRequest;
import com.migaja.dto.StoryResponse;
import com.migaja.service.CommentService;
import com.migaja.service.RatingService;
import com.migaja.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final RatingService ratingService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<StoryResponse> createStory(@Valid @RequestBody StoryRequest request) {
        return ResponseEntity.ok(storyService.createStory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoryResponse> updateStory(@PathVariable Long id,
            @Valid @RequestBody StoryRequest request) {
        return ResponseEntity.ok(storyService.updateStory(id, request));
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<StoryResponse> publishStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.publishStory(id));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<StoryResponse> submitStory(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.submitStory(id));
    }

    @GetMapping("/public")
    public ResponseEntity<Page<StoryResponse>> getPublishedStories(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(storyService.getPublishedStories(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoryResponse> getStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(storyService.getStoryById(id));
    }

    @GetMapping("/my-stories")
    public ResponseEntity<Page<StoryResponse>> getMyStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(storyService.getMyStories(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<Void> rateStory(@PathVariable Long id,
            @Valid @RequestBody RatingRequest request) {
        ratingService.rateStory(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.addComment(id, request));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<CommentResponse>> getStoryComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.getStoryComments(id, pageable));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
