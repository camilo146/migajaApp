import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TournamentService } from '../../core/services/tournament.service';
import { StoryService } from '../../core/services/story.service';
import { NotificationService } from '../../core/services/notification.service';
import { Tournament, TournamentEntry, Story } from '../../core/models/models';

@Component({
  selector: 'app-tournament-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './tournament-list.component.html',
  styleUrls: ['./tournament-list.component.css']
})
export class TournamentListComponent implements OnInit {
  private tournamentService = inject(TournamentService);
  private storyService = inject(StoryService);
  private notificationService = inject(NotificationService);
  private router = inject(Router);

  tournaments: Tournament[] = [];
  myEntries: TournamentEntry[] = [];
  selectedTournament: Tournament | null = null;
  selectedStoryId: number = 0;
  myStories: Story[] = [];
  showRegistrationModal: boolean = false;
  showPaymentModal: boolean = false;
  
  ngOnInit() {
    this.loadTournaments();
    this.loadMyEntries();
  }

  loadTournaments() {
    this.tournamentService.getAllTournaments().subscribe({
      next: (data) => this.tournaments = data,
      error: (err) => console.error('Error loading tournaments', err)
    });
  }

  loadMyEntries() {
    this.tournamentService.getMyEntries().subscribe({
      next: (data) => this.myEntries = data,
      error: (err) => console.error('Error loading entries', err)
    });
  }

  openRegistrationModal(tournament: Tournament) {
    this.selectedTournament = tournament;
    this.showRegistrationModal = true;
    this.loadMyStories();
  }

  loadMyStories() {
    this.storyService.getMyStories(0, 100).subscribe({
      next: (page) => {
        // Filter stories that are PUBLISHED and not already registered (simplified check)
        this.myStories = page.content.filter(s => s.status === 'PUBLISHED');
      },
      error: (err) => this.notificationService.error('Error loading stories')
    });
  }

  closeModal() {
    this.showRegistrationModal = false;
    this.showPaymentModal = false;
    this.selectedTournament = null;
    this.selectedStoryId = 0;
  }

  proceedToPayment() {
    if (!this.selectedTournament || !this.selectedStoryId) return;
    this.showRegistrationModal = false;
    this.showPaymentModal = true;
  }

  confirmPayment() {
    if (!this.selectedTournament || !this.selectedStoryId) return;

    const request = {
      storyId: this.selectedStoryId,
      paymentTransactionId: 'NEQUI-PENDING-' + Date.now()
    };

    this.tournamentService.registerForTournament(this.selectedTournament.id, request).subscribe({
      next: () => {
        this.notificationService.success('¡Inscripción solicitada! Esperando verificación.');
        this.closeModal();
        this.loadTournaments();
        this.loadMyEntries();
      },
      error: (err) => {
        this.notificationService.error('Error al inscribir historia: ' + (err.error?.message || 'Error desconocido'));
      }
    });
  }
  
  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'UPCOMING': return 'badge-upcoming';
      case 'REGISTRATION_OPEN': return 'badge-open';
      case 'VOTING': return 'badge-voting';
      case 'ENDED': return 'badge-ended';
      case 'CANCELLED': return 'badge-cancelled';
      default: return 'badge-default';
    }
  }
  
  getStatusText(status: string): string {
    switch (status) {
      case 'UPCOMING': return 'Próximamente';
      case 'REGISTRATION_OPEN': return 'Registro Abierto';
      case 'VOTING': return 'Votación Activa';
      case 'ENDED': return 'Finalizado';
      case 'CANCELLED': return 'Cancelado';
      default: return status;
    }
  }
  
  canRegister(tournament: Tournament): boolean {
    return tournament.status === 'REGISTRATION_OPEN';
  }
  
  canVote(tournament: Tournament): boolean {
    return tournament.status === 'VOTING';
  }
  
  viewRankings(tournamentId: number) {
    this.router.navigate(['/tournaments', tournamentId, 'rankings']);
  }
}
