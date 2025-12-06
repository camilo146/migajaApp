package com.migaja.service;

import com.migaja.dto.RatingRequest;
import com.migaja.model.Rating;
import com.migaja.model.Story;
import com.migaja.model.User;
import com.migaja.repository.RatingRepository;
import com.migaja.repository.StoryRepository;
import com.migaja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void rateStory(Long storyId, RatingRequest request) {
        User currentUser = getCurrentUser();

        if (ratingRepository.existsByUserIdAndStoryId(currentUser.getId(), storyId)) {
            throw new RuntimeException("Ya has calificado esta historia.");
        }

        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        Rating rating = Rating.builder()
                .user(currentUser)
                .story(story)
                .stars(request.getStars())
                .build();

        ratingRepository.save(rating);

        updateStoryRating(story);
    }

    private void updateStoryRating(Story story) {
        // Force refresh of ratings collection from DB if needed, but here we rely on
        // the fact that
        // the rating we just saved is in the DB. However, the 'story' object might have
        // an outdated 'ratings' collection
        // if it was fetched before the new rating was saved.
        // A better approach is to calculate average using a query.

        Double avgRating = ratingRepository.calculateAverageRating(story.getId());
        Integer count = ratingRepository.countByStoryId(story.getId());

        story.setAverageRating(avgRating != null ? avgRating : 0.0);
        story.setRatingCount(count != null ? count : 0);
        storyRepository.save(story);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
