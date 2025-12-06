package com.migaja.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.migaja.dto.StoryRequest;
import com.migaja.dto.StoryResponse;
import com.migaja.model.Story;
import com.migaja.model.StoryView;
import com.migaja.model.User;
import com.migaja.repository.CommentRepository;
import com.migaja.repository.PhotoRepository;
import com.migaja.repository.RatingRepository;
import com.migaja.repository.StoryRepository;
import com.migaja.repository.StoryViewRepository;
import com.migaja.repository.TournamentEntryRepository;
import com.migaja.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoryService {

    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final StoryViewRepository storyViewRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final PhotoRepository photoRepository;
    private final TournamentEntryRepository tournamentEntryRepository;

    @Transactional
    public StoryResponse createStory(StoryRequest request) {
        User author = getCurrentUser();

        Story story = Story.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(author)
                .relationshipStartDate(request.getRelationshipStartDate())
                .relationshipEndDate(request.getRelationshipEndDate())
                .city(request.getCity())
                .municipality(request.getMunicipality())
                .department(request.getDepartment())
                .country(request.getCountry())
                .relationshipDurationDays(request.getRelationshipDurationDays())
                .status(Story.StoryStatus.DRAFT)
                .viewCount(0)
                .averageRating(0.0)
                .ratingCount(0)
                .build();

        story = storyRepository.save(story);
        return convertToResponse(story);
    }

    @Transactional
    public StoryResponse updateStory(Long id, StoryRequest request) {
        User currentUser = getCurrentUser();
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        if (!story.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para editar esta historia");
        }

        story.setTitle(request.getTitle());
        story.setContent(request.getContent());
        story.setRelationshipStartDate(request.getRelationshipStartDate());
        story.setRelationshipEndDate(request.getRelationshipEndDate());
        story.setCity(request.getCity());
        story.setMunicipality(request.getMunicipality());
        story.setDepartment(request.getDepartment());
        story.setCountry(request.getCountry());
        story.setRelationshipDurationDays(request.getRelationshipDurationDays());

        story = storyRepository.save(story);
        return convertToResponse(story);
    }

    @Transactional
    public StoryResponse publishStory(Long id) {
        User currentUser = getCurrentUser();
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        if (currentUser.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Solo los administradores pueden publicar historias");
        }

        story.setStatus(Story.StoryStatus.PUBLISHED);
        story.setPublishedAt(LocalDateTime.now());

        story = storyRepository.save(story);
        return convertToResponse(story);
    }

    @Transactional
    public StoryResponse submitStory(Long id) {
        User currentUser = getCurrentUser();
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        if (!story.getAuthor().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para enviar esta historia");
        }

        story.setStatus(Story.StoryStatus.MODERATED);
        story = storyRepository.save(story);
        return convertToResponse(story);
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getPublishedStories(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return storyRepository.findByStatusAndSearch(
                    Story.StoryStatus.PUBLISHED, search, pageable)
                    .map(this::convertToResponse);
        }
        return storyRepository.findByStatus(Story.StoryStatus.PUBLISHED, pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public StoryResponse getStoryById(Long id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        getAuthenticatedUser().ifPresent(user -> {
            if (!storyViewRepository.existsByStoryIdAndUserId(id, user.getId())) {
                StoryView view = StoryView.builder()
                        .story(story)
                        .user(user)
                        .build();
                storyViewRepository.save(view);

                story.setViewCount(story.getViewCount() + 1);
                storyRepository.save(story);
            }
        });

        return convertToResponse(story);
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getMyStories(Pageable pageable) {
        User currentUser = getCurrentUser();
        return storyRepository.findByAuthorId(currentUser.getId(), pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getAllStories(Pageable pageable) {
        return storyRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Transactional(readOnly = true)
    public Page<StoryResponse> getStoriesByStatus(Story.StoryStatus status, Pageable pageable) {
        return storyRepository.findByStatus(status, pageable)
                .map(this::convertToResponse);
    }

    @Transactional
    public void deleteStory(Long id) {
        User currentUser = getCurrentUser();
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        if (!story.getAuthor().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new RuntimeException("No tienes permiso para eliminar esta historia");
        }

        // Manually delete related entities to avoid constraint violations
        commentRepository.deleteByStoryId(id);
        ratingRepository.deleteByStoryId(id);
        storyViewRepository.deleteByStoryId(id);
        tournamentEntryRepository.deleteByStoryId(id);
        photoRepository.deleteByStoryId(id);

        storyRepository.delete(story);
    }

    private StoryResponse convertToResponse(Story story) {
        return StoryResponse.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .author(StoryResponse.AuthorDTO.builder()
                        .id(story.getAuthor().getId())
                        .username(story.getAuthor().getUsername())
                        .fullName(story.getAuthor().getFullName())
                        .avatarUrl(story.getAuthor().getAvatarUrl())
                        .build())
                .relationshipStartDate(story.getRelationshipStartDate())
                .relationshipEndDate(story.getRelationshipEndDate())
                .city(story.getCity())
                .municipality(story.getMunicipality())
                .department(story.getDepartment())
                .country(story.getCountry())
                .relationshipDurationDays(story.getRelationshipDurationDays())
                .status(story.getStatus().name())
                .photos(story.getPhotos().stream()
                        .map(photo -> StoryResponse.PhotoDTO.builder()
                                .id(photo.getId())
                                .url(photo.getUrl())
                                .caption(photo.getCaption())
                                .photoDate(photo.getPhotoDate())
                                .displayOrder(photo.getDisplayOrder())
                                .build())
                        .collect(Collectors.toList()))
                .viewCount(story.getViewCount())
                .averageRating(story.getAverageRating())
                .ratingCount(story.getRatingCount())
                .createdAt(story.getCreatedAt())
                .publishedAt(story.getPublishedAt())
                .build();
    }

    private Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
