package com.migaja.repository;

import com.migaja.model.Tournament;
import com.migaja.model.Tournament.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStatusOrderByStartDateDesc(TournamentStatus status);

    List<Tournament> findAllByOrderByStartDateDesc();
}
