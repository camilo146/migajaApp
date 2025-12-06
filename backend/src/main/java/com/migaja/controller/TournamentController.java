package com.migaja.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.migaja.dto.TournamentEntryResponse;
import com.migaja.dto.TournamentRegistrationRequest;
import com.migaja.dto.TournamentRequest;
import com.migaja.dto.TournamentResponse;
import com.migaja.service.TournamentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping
    public ResponseEntity<List<TournamentResponse>> getAllTournaments() {
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentResponse> getTournament(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournament(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentResponse> createTournament(@RequestBody TournamentRequest request) {
        return ResponseEntity.ok(tournamentService.createTournament(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TournamentResponse> updateTournament(
            @PathVariable Long id,
            @RequestBody TournamentRequest request) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, request));
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> registerForTournament(
            @PathVariable Long id,
            @RequestBody TournamentRegistrationRequest request) {
        tournamentService.registerForTournament(id, request);
        return ResponseEntity.ok("Registered successfully");
    }

    @GetMapping("/my-entries")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TournamentEntryResponse>> getMyEntries() {
        return ResponseEntity.ok(tournamentService.getMyEntries());
    }

    @PostMapping("/{tournamentId}/entries/{entryId}/vote")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> voteForEntry(
            @PathVariable Long tournamentId,
            @PathVariable Long entryId) {
        tournamentService.voteForEntry(tournamentId, entryId);
        return ResponseEntity.ok("Vote registered");
    }

    @GetMapping("/{id}/rankings")
    public ResponseEntity<List<TournamentEntryResponse>> getRankings(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getRankings(id));
    }

    @GetMapping("/{id}/leaderboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TournamentEntryResponse>> getLeaderboard(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getLeaderboard(id));
    }

    @PostMapping("/{id}/open-registration")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> openRegistration(@PathVariable Long id) {
        tournamentService.openRegistration(id);
        return ResponseEntity.ok("Registration opened");
    }

    @PostMapping("/{id}/start-voting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> startVoting(@PathVariable Long id) {
        tournamentService.startVoting(id);
        return ResponseEntity.ok("Voting started");
    }

    @PostMapping("/{id}/end")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> endTournament(@PathVariable Long id) {
        tournamentService.endTournament(id);
        return ResponseEntity.ok("Tournament ended");
    }

    @PostMapping("/{id}/calculate-scores")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> calculateScores(@PathVariable Long id) {
        tournamentService.calculateScores(id);
        return ResponseEntity.ok("Scores calculated");
    }

    @PostMapping("/{id}/publish-winner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> publishWinner(@PathVariable Long id) {
        tournamentService.publishWinner(id);
        return ResponseEntity.ok("Winner published");
    }

    @PostMapping("/{id}/reset")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> resetTournament(@PathVariable Long id) {
        tournamentService.resetTournament(id);
        return ResponseEntity.ok("Tournament reset successfully");
    }

    @PostMapping("/entries/{entryId}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> verifyEntry(@PathVariable Long entryId) {
        tournamentService.verifyEntry(entryId);
        return ResponseEntity.ok("Entry verified");
    }

    @GetMapping("/entries/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TournamentEntryResponse>> getPendingEntries() {
        return ResponseEntity.ok(tournamentService.getPendingEntries());
    }
}
