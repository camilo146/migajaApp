package com.migaja.controller;

import com.migaja.dto.StoryResponse;
import com.migaja.model.Story;
import com.migaja.model.User;
import com.migaja.service.StoryService;
import com.migaja.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final StoryService storyService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/stories")
    public ResponseEntity<Page<StoryResponse>> getAllStories(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (status != null) {
            try {
                Story.StoryStatus storyStatus = Story.StoryStatus.valueOf(status);
                return ResponseEntity.ok(storyService.getStoriesByStatus(storyStatus, pageable));
            } catch (IllegalArgumentException e) {
                // If status is invalid, return all stories or handle error
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.ok(storyService.getAllStories(pageable));
    }
}
