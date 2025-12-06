package com.migaja.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.migaja.dto.TournamentEntryResponse;
import com.migaja.dto.TournamentRegistrationRequest;
import com.migaja.dto.TournamentRequest;
import com.migaja.dto.TournamentResponse;
import com.migaja.model.Story;
import com.migaja.model.Tournament;
import com.migaja.model.TournamentEntry;
import com.migaja.model.User;
import com.migaja.repository.CommentRepository;
import com.migaja.repository.StoryRepository;
import com.migaja.repository.TournamentEntryRepository;
import com.migaja.repository.TournamentRepository;
import com.migaja.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentEntryRepository entryRepository;
    private final StoryRepository storyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<TournamentResponse> getAllTournaments() {
        return tournamentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public TournamentResponse getTournament(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        return convertToResponse(tournament);
    }

    @Transactional
    public TournamentResponse createTournament(TournamentRequest request) {
        Tournament tournament = Tournament.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .votingStartDate(request.getVotingStartDate())
                .votingEndDate(request.getVotingEndDate())
                .entryFee(request.getEntryFee())
                .prizeAmount(request.getPrizeAmount())
                .status(Tournament.TournamentStatus.UPCOMING)
                .createdAt(LocalDateTime.now())
                .build();

        tournament = tournamentRepository.save(tournament);
        return convertToResponse(tournament);
    }

    @Transactional
    public TournamentResponse updateTournament(Long id, TournamentRequest request) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        tournament.setTitle(request.getTitle());
        tournament.setDescription(request.getDescription());
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setVotingStartDate(request.getVotingStartDate());
        tournament.setVotingEndDate(request.getVotingEndDate());
        tournament.setEntryFee(request.getEntryFee());
        tournament.setPrizeAmount(request.getPrizeAmount());

        tournament = tournamentRepository.save(tournament);
        return convertToResponse(tournament);
    }

    @Transactional
    public void registerForTournament(Long tournamentId, TournamentRegistrationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        if (tournament.getStatus() != Tournament.TournamentStatus.REGISTRATION_OPEN) {
            throw new RuntimeException("Registration is not open for this tournament");
        }

        Story story = storyRepository.findById(request.getStoryId())
                .orElseThrow(() -> new RuntimeException("Story not found"));

        if (!story.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("You can only register your own stories");
        }

        if (story.getStatus() != Story.StoryStatus.PUBLISHED) {
            throw new RuntimeException("Only published stories can be registered");
        }

        // Check if already registered
        boolean alreadyRegistered = entryRepository.existsByTournamentIdAndStoryId(tournamentId, request.getStoryId());
        if (alreadyRegistered) {
            throw new RuntimeException("This story is already registered for this tournament");
        }

        TournamentEntry entry = TournamentEntry.builder()
                .tournament(tournament)
                .story(story)
                .registeredAt(LocalDateTime.now())
                .status(TournamentEntry.EntryStatus.PENDING)
                .paymentConfirmed(true) // Simulando pago confirmado
                .paymentTransactionId(request.getPaymentTransactionId())
                .voteCount(0)
                .averageRating(story.getAverageRating())
                .score(0.0)
                .category(TournamentEntry.EntryCategory.NORMAL)
                .build();

        // Calculate initial score and category
        updateEntryScore(entry);

        entryRepository.save(entry);

        // Update prize amount
        // Base prize (e.g. 100,000) + (Entries * 2000)
        // The prize only starts increasing after 50 entries.
        int totalEntries = entryRepository.countByTournamentId(tournamentId);
        if (totalEntries > 50) {
            BigDecimal currentPrize = tournament.getPrizeAmount();
            tournament.setPrizeAmount(currentPrize.add(new BigDecimal("2000")));
            tournamentRepository.save(tournament);
        }
    }

    public List<TournamentEntryResponse> getMyEntries() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return entryRepository.findByStoryAuthorId(user.getId()).stream()
                .map(this::convertToEntryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void voteForEntry(Long tournamentId, Long entryId) {
        TournamentEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getTournament().getId().equals(tournamentId)) {
            throw new RuntimeException("Entry does not belong to this tournament");
        }

        if (entry.getTournament().getStatus() != Tournament.TournamentStatus.VOTING) {
            throw new RuntimeException("Voting is not open for this tournament");
        }

        entry.setVoteCount(entry.getVoteCount() + 1);
        entryRepository.save(entry);
    }

    public List<TournamentEntryResponse> getRankings(Long tournamentId) {
        return entryRepository.findByTournamentIdOrderByVoteCountDescAverageRatingDesc(tournamentId).stream()
                .map(this::convertToEntryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void calculateScores(Long tournamentId) {
        List<TournamentEntry> entries = entryRepository.findByTournamentId(tournamentId);

        for (TournamentEntry entry : entries) {
            updateEntryScore(entry);
        }
        entryRepository.saveAll(entries);
    }

    private void updateEntryScore(TournamentEntry entry) {
        Story story = entry.getStory();
        long viewCount = story.getViewCount();
        double avgRating = story.getAverageRating();
        int commentCount = commentRepository.countByStoryId(story.getId());

        double score = viewCount + (commentCount * 5) + (avgRating * 10);
        entry.setScore(score);

        // Categorization
        if (score > 1000) {
            entry.setCategory(TournamentEntry.EntryCategory.ARRASTRADO);
        } else if (score > 500) {
            entry.setCategory(TournamentEntry.EntryCategory.MIGAJERO);
        } else {
            entry.setCategory(TournamentEntry.EntryCategory.NORMAL);
        }
    }

    @Transactional
    public void publishWinner(Long tournamentId) {
        calculateScores(tournamentId); // Ensure scores are up to date

        List<TournamentEntry> entries = entryRepository.findByTournamentId(tournamentId);
        if (entries.isEmpty())
            return;

        TournamentEntry winner = entries.stream()
                .max(Comparator.comparing(TournamentEntry::getScore))
                .orElseThrow(() -> new RuntimeException("No entries found"));

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        tournament.setWinner(winner);
        tournament.setStatus(Tournament.TournamentStatus.ENDED);
        tournamentRepository.save(tournament);
    }

    @Transactional
    public void resetTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));

        // Clear winner
        tournament.setWinner(null);

        // Reset status
        tournament.setStatus(Tournament.TournamentStatus.UPCOMING);

        // Delete all entries
        entryRepository.deleteAll(tournament.getEntries());
        tournament.getEntries().clear();

        tournamentRepository.save(tournament);
    }

    public List<TournamentEntryResponse> getLeaderboard(Long tournamentId) {
        return entryRepository.findByTournamentId(tournamentId).stream()
                .sorted(Comparator.comparing(TournamentEntry::getScore).reversed())
                .map(this::convertToEntryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void verifyEntry(Long entryId) {
        TournamentEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setStatus(TournamentEntry.EntryStatus.APPROVED);
        entry.setPaymentConfirmed(true);
        entryRepository.save(entry);

        Story story = entry.getStory();
        story.setVerified(true);
        storyRepository.save(story);
    }

    @Transactional
    public void openRegistration(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.setStatus(Tournament.TournamentStatus.REGISTRATION_OPEN);
        tournamentRepository.save(tournament);
    }

    @Transactional
    public void startVoting(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.setStatus(Tournament.TournamentStatus.VOTING);
        tournamentRepository.save(tournament);
    }

    @Transactional
    public void endTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        tournament.setStatus(Tournament.TournamentStatus.ENDED);
        tournamentRepository.save(tournament);
    }

    public List<TournamentEntryResponse> getPendingEntries() {
        return entryRepository.findByStatus(TournamentEntry.EntryStatus.PENDING).stream()
                .map(this::convertToEntryResponse)
                .collect(Collectors.toList());
    }

    private TournamentResponse convertToResponse(Tournament tournament) {
        int entryCount = entryRepository.countByTournamentId(tournament.getId());

        TournamentEntryResponse winnerResponse = null;
        if (tournament.getWinner() != null) {
            winnerResponse = convertToEntryResponse(tournament.getWinner());
        }

        return TournamentResponse.builder()
                .id(tournament.getId())
                .title(tournament.getTitle())
                .description(tournament.getDescription())
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .votingStartDate(tournament.getVotingStartDate())
                .votingEndDate(tournament.getVotingEndDate())
                .entryFee(tournament.getEntryFee())
                .prizeAmount(tournament.getPrizeAmount())
                .status(tournament.getStatus())
                .entryCount(entryCount)
                .winner(winnerResponse)
                .build();
    }

    private TournamentEntryResponse convertToEntryResponse(TournamentEntry entry) {
        // Ensure score and category are up to date for display
        updateEntryScore(entry);

        return TournamentEntryResponse.builder()
                .id(entry.getId())
                .storyId(entry.getStory().getId())
                .storyTitle(entry.getStory().getTitle())
                .voteCount(entry.getVoteCount())
                .averageRating(entry.getAverageRating())
                .score(entry.getScore())
                .category(entry.getCategory().toString())
                .status(entry.getStatus().toString())
                .registeredAt(entry.getRegisteredAt())
                .build();
    }
}
