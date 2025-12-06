package com.migaja.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentEntryResponse {
    private Long id;
    private Long storyId;
    private String storyTitle;
    private Integer voteCount;
    private Double averageRating;
    private Double score;
    private String category;
    private String status;
    private LocalDateTime registeredAt;
}
