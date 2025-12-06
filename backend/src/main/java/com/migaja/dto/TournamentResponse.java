package com.migaja.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.migaja.model.Tournament;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime votingStartDate;
    private LocalDateTime votingEndDate;
    private BigDecimal entryFee;
    private BigDecimal prizeAmount;
    private Tournament.TournamentStatus status;
    private Integer entryCount;
    private TournamentEntryResponse winner;
}
