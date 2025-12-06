package com.migaja.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.migaja.model.Story;
import com.migaja.model.User;
import com.migaja.repository.StoryRepository;
import com.migaja.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

        private final UserRepository userRepository;
        private final StoryRepository storyRepository;
        private final PasswordEncoder passwordEncoder;
        private final java.util.Random random = new java.util.Random();

        @Bean
        public CommandLineRunner seedData() {
                return args -> {
                        // Seeding disabled by request
                        log.info("Data seeding is disabled.");
                };
        }

        private User createUser(String username, String email, String fullName, String bio) {
                return User.builder()
                                .username(username)
                                .email(email)
                                .password(passwordEncoder.encode("password123"))
                                .fullName(fullName)
                                .bio(bio)
                                .role(User.Role.USER)
                                .active(true)
                                .createdAt(LocalDateTime.now())
                                .build();
        }

        private void createStory(User author, String title, String content, Story.StoryStatus status) {
                Story story = Story.builder()
                                .title(title)
                                .content(content)
                                .author(author)
                                .status(status)
                                .relationshipStartDate(LocalDateTime.now().minusYears(random.nextInt(5) + 1))
                                .relationshipEndDate(LocalDateTime.now().minusMonths(random.nextInt(10) + 1))
                                .city("Bogotá")
                                .municipality("Bogotá")
                                .department("Cundinamarca")
                                .country("Colombia")
                                .relationshipDurationDays(random.nextInt(1000) + 30)
                                .viewCount(random.nextInt(500))
                                .averageRating(1.0 + (random.nextDouble() * 4.0))
                                .ratingCount(random.nextInt(50))
                                .createdAt(LocalDateTime.now().minusDays(random.nextInt(365)))
                                .build();

                if (status == Story.StoryStatus.PUBLISHED) {
                        story.setPublishedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                }

                storyRepository.save(story);
        }
}
