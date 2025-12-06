import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { StoryService } from '../../../core/services/story.service';
import { AuthService } from '../../../core/services/auth.service';
import { TournamentService } from '../../../core/services/tournament.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Story, Tournament, AuthResponse } from '../../../core/models/models';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-story-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './story-list.component.html',
  styleUrl: './story-list.component.scss'
})
export class StoryListComponent implements OnInit {
  stories: Story[] = [];
  currentPage = 0;
  totalPages = 0;
  isLoading = false;
  searchTerm = '';
  private searchSubject = new Subject<string>();
  
  currentUser: AuthResponse | null = null;
  activeTournaments: Tournament[] = [];
  registeredStoryStatus: Map<number, string> = new Map();
  
  // Modal properties
  showRegistrationModal = false;
  selectedTournament: Tournament | null = null;
  selectedStoryId: number | null = null;
  nequiNumber = '3132233304';
  transactionId = '';
  isSubmitting = false;

  constructor(
    private storyService: StoryService,
    private authService: AuthService,
    private tournamentService: TournamentService,
    private notificationService: NotificationService
  ) {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      this.searchTerm = term;
      this.currentPage = 0;
      this.loadStories();
    });
    
    this.currentUser = this.authService.getCurrentUser();
  }

  ngOnInit() {
    this.loadStories();
    this.loadActiveTournaments();
    if (this.currentUser) {
      this.loadMyEntries();
    }
  }

  loadMyEntries() {
    this.tournamentService.getMyEntries().subscribe({
      next: (entries) => {
        this.registeredStoryStatus = new Map(entries.map(e => [e.storyId, e.status]));
      },
      error: (err) => console.error('Error loading entries', err)
    });
  }

  onSearch(term: string) {
    this.searchSubject.next(term);
  }

  loadStories() {
    this.isLoading = true;
    this.storyService.getPublicStories(this.currentPage, 12, this.searchTerm).subscribe({
      next: (response) => {
        this.stories = response.content;
        this.totalPages = response.totalPages;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading stories:', error);
        this.isLoading = false;
      }
    });
  }
  
  loadActiveTournaments() {
    this.tournamentService.getAllTournaments().subscribe({
      next: (tournaments) => {
        this.activeTournaments = tournaments.filter(t => t.status === 'REGISTRATION_OPEN');
      },
      error: (err) => console.error('Error loading tournaments', err)
    });
  }

  nextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadStories();
    }
  }

  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadStories();
    }
  }

  getStarArray(rating: number): number[] {
    return Array(5).fill(0).map((_, i) => i < Math.round(rating) ? 1 : 0);
  }
  
  // Registration Logic
  openRegistrationModal(storyId: number) {
    if (this.activeTournaments.length === 0) {
      this.notificationService.info('No hay torneos activos en este momento.');
      return;
    }
    
    // If there's only one active tournament, select it automatically
    // Otherwise, we might need a selector, but for now let's assume the first one or most relevant
    this.selectedTournament = this.activeTournaments[0]; 
    this.selectedStoryId = storyId;
    this.showRegistrationModal = true;
    this.transactionId = '';
  }
  
  closeModal() {
    this.showRegistrationModal = false;
    this.selectedTournament = null;
    this.selectedStoryId = null;
    this.transactionId = '';
  }
  
  confirmRegistration() {
    if (!this.selectedTournament || !this.selectedStoryId || !this.transactionId) return;
    
    this.isSubmitting = true;
    this.tournamentService.registerForTournament(
      this.selectedTournament.id, 
      {
        storyId: this.selectedStoryId,
        paymentTransactionId: this.transactionId
      }
    ).subscribe({
      next: () => {
        this.notificationService.success('¡Inscripción enviada! Tu historia está pendiente de verificación.');
        if (this.selectedStoryId) {
          this.registeredStoryStatus.set(this.selectedStoryId, 'PENDING');
        }
        this.closeModal();
        this.isSubmitting = false;
      },
      error: (err) => {
        console.error('Error registering:', err);
        this.notificationService.error('Error al inscribirse: ' + (err.error?.message || 'Intente nuevamente'));
        this.isSubmitting = false;
      }
    });
  }
}
