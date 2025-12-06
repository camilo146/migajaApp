package com.migaja.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.migaja.model.Tournament;
import com.migaja.repository.TournamentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TournamentInitializer {

    private final TournamentRepository tournamentRepository;

    @Bean
    public CommandLineRunner initTournaments() {
        return args -> {
            log.info("Checking for tournaments...");

            if (tournamentRepository.count() == 0) {
                log.info("No tournaments found. Creating default tournaments...");

                Tournament t1 = Tournament.builder()
                        .title("Torneo de los Migajeros")
                        .description(
                                "Primer gran torneo de historias de desamor. La mejor historia gana $100.000! Inscripción: $10.000. Comparte tu historia y deja que la comunidad vote.")
                        .startDate(LocalDateTime.now())
                        .endDate(LocalDateTime.now().plusDays(30))
                        .votingStartDate(LocalDateTime.now().plusDays(15))
                        .votingEndDate(LocalDateTime.now().plusDays(28))
                        .entryFee(new BigDecimal("10000.00"))
                        .prizeAmount(new BigDecimal("100000.00"))
                        .status(Tournament.TournamentStatus.REGISTRATION_OPEN)
                        .createdAt(LocalDateTime.now())
                        .build();

                tournamentRepository.save(t1);

                log.info("Default tournaments created successfully.");
            } else {
                log.info("Tournaments already exist.");
            }
        };
    }
}
