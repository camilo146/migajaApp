package com.migaja.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.migaja.model.TournamentEntry;

@Repository
public interface TournamentEntryRepository extends JpaRepository<TournamentEntry, Long> {
    List<TournamentEntry> findByTournamentIdOrderByAverageRatingDescVoteCountDesc(Long tournamentId);

    List<TournamentEntry> findByTournamentIdOrderByVoteCountDescAverageRatingDesc(Long tournamentId);

    List<TournamentEntry> findByTournamentId(Long tournamentId);

    List<TournamentEntry> findByStoryAuthorId(Long authorId);

    List<TournamentEntry> findByStatus(TournamentEntry.EntryStatus status);

    Optional<TournamentEntry> findByTournamentIdAndStoryId(Long tournamentId, Long storyId);

    boolean existsByTournamentIdAndStoryId(Long tournamentId, Long storyId);

    int countByTournamentId(Long tournamentId);

    void deleteByStoryId(Long storyId);
}
