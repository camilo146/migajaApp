package com.migaja.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRegistrationRequest {
    private Long tournamentId;
    private Long storyId;
    private String paymentTransactionId;
}
