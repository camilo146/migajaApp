package com.migaja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponse {
    private Long id;
    private String title;
    private String content;
    private AuthorDTO author;
    private LocalDateTime relationshipStartDate;
    private LocalDateTime relationshipEndDate;
    private String city;
    private String municipality;
    private String department;
    private String country;
    private Integer relationshipDurationDays;
    private String status;
    private List<PhotoDTO> photos;
    private Integer viewCount;
    private Double averageRating;
    private Integer ratingCount;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDTO {
        private Long id;
        private String username;
        private String fullName;
        private String avatarUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoDTO {
        private Long id;
        private String url;
        private String caption;
        private LocalDateTime photoDate;
        private Integer displayOrder;
    }
}
