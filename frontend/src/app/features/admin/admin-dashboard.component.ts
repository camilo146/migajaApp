import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { NotificationService } from '../../core/services/notification.service';
import { TournamentService } from '../../core/services/tournament.service';
import { Tournament, TournamentEntry } from '../../core/models/models';
import { ConfirmationModalComponent } from '../../shared/components/confirmation-modal/confirmation-modal.component';

interface DashboardStats {
  totalUsers: number;
  totalStories: number;
  totalPublishedStories: number;
  totalComments: number;
  totalRatings: number;
  averageRating: number;
}

interface StoryModeration {
  id: number;
  title: string;
  author: {
    username: string;
    fullName: string;
    avatarUrl: string;
  };
  status: string;
  createdAt: Date;
  viewCount: number;
  ratingCount: number;
  averageRating: number;
}

interface UserManagement {
  id: number;
  username: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: string;
  active: boolean;
  createdAt: Date;
}

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, ConfirmationModalComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  stats: DashboardStats = {
    totalUsers: 0,
    totalStories: 0,
    totalPublishedStories: 0,
    totalComments: 0,
    totalRatings: 0,
    averageRating: 0
  };
  
  // Modal State
  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  pendingAction: (() => void) | null = null;

  pendingStories: StoryModeration[] = [];
  allStories: StoryModeration[] = [];
  users: UserManagement[] = [];
  tournaments: Tournament[] = [];
  selectedTournamentId: number | null = null;
  leaderboard: TournamentEntry[] = [];
  pendingEntries: TournamentEntry[] = [];
  
  activeTab: 'stats' | 'stories' | 'users' | 'tournaments' = 'stats';
  
  private http = inject(HttpClient);
  private notificationService = inject(NotificationService);
  private tournamentService = inject(TournamentService);
  
  ngOnInit() {
    this.loadStats();
    this.loadPendingStories();
    this.loadAllStories();
    this.loadUsers();
    this.loadTournaments();
    this.loadPendingEntries();
  }

  // Modal Helpers
  openConfirmation(title: string, message: string, action: () => void) {
    this.modalTitle = title;
    this.modalMessage = message;
    this.pendingAction = action;
    this.isModalOpen = true;
  }

  onConfirmAction() {
    if (this.pendingAction) {
      this.pendingAction();
    }
    this.closeModal();
  }

  closeModal() {
    this.isModalOpen = false;
    this.pendingAction = null;
  }

  loadPendingEntries() {
    this.tournamentService.getPendingEntries().subscribe({
      next: (data) => this.pendingEntries = data,
      error: (err) => console.error('Error loading pending entries', err)
    });
  }

  verifyEntry(entryId: number) {
    this.openConfirmation(
      'Verificar Inscripción',
      '¿Estás seguro de verificar esta inscripción?',
      () => {
        this.tournamentService.verifyEntry(entryId).subscribe({
          next: () => {
            this.notificationService.success('Inscripción verificada correctamente');
            this.loadPendingEntries();
            this.loadTournaments();
          },
          error: (err) => this.notificationService.error('Error al verificar inscripción')
        });
      }
    );
  }

  openRegistration(id: number) {
    this.openConfirmation(
      'Abrir Inscripciones',
      '¿Abrir inscripciones para este torneo?',
      () => {
        this.tournamentService.openRegistration(id).subscribe({
          next: () => {
            this.notificationService.success('Inscripciones abiertas');
            this.loadTournaments();
          },
          error: (err) => this.notificationService.error('Error al abrir inscripciones')
        });
      }
    );
  }

  startVoting(id: number) {
    this.openConfirmation(
      'Iniciar Votación',
      '¿Iniciar fase de votación?',
      () => {
        this.tournamentService.startVoting(id).subscribe({
          next: () => {
            this.notificationService.success('Votación iniciada');
            this.loadTournaments();
          },
          error: (err) => this.notificationService.error('Error al iniciar votación')
        });
      }
    );
  }

  endTournament(id: number) {
    this.openConfirmation(
      'Finalizar Torneo',
      '¿Finalizar torneo?',
      () => {
        this.tournamentService.endTournament(id).subscribe({
          next: () => {
            this.notificationService.success('Torneo finalizado');
            this.loadTournaments();
          },
          error: (err) => this.notificationService.error('Error al finalizar torneo')
        });
      }
    );
  }

  loadTournaments() {
    this.tournamentService.getAllTournaments().subscribe({
      next: (data) => this.tournaments = data,
      error: (err) => this.notificationService.error('Error loading tournaments')
    });
  }

  viewLeaderboard(tournamentId: number) {
    this.selectedTournamentId = tournamentId;
    this.tournamentService.getLeaderboard(tournamentId).subscribe({
      next: (data) => this.leaderboard = data,
      error: (err) => this.notificationService.error('Error loading leaderboard')
    });
  }

  calculateScores(tournamentId: number) {
    this.tournamentService.calculateScores(tournamentId).subscribe({
      next: () => {
        this.notificationService.success('Scores calculated');
        this.viewLeaderboard(tournamentId);
      },
      error: (err) => this.notificationService.error('Error calculating scores')
    });
  }

  publishWinner(tournamentId: number) {
    this.openConfirmation(
      'Publicar Ganador',
      'Are you sure you want to publish the winner? This action cannot be undone.',
      () => {
        this.tournamentService.publishWinner(tournamentId).subscribe({
          next: () => {
            this.notificationService.success('Winner published successfully');
            this.loadTournaments();
            this.viewLeaderboard(tournamentId);
          },
          error: (err) => this.notificationService.error('Error publishing winner')
        });
      }
    );
  }

  resetTournament(tournamentId: number) {
    this.openConfirmation(
      'Reiniciar Torneo',
      '¿Estás seguro de que quieres reiniciar el torneo? Se eliminarán todas las inscripciones y el ganador, pero los puntajes de las historias se mantendrán.',
      () => {
        this.tournamentService.resetTournament(tournamentId).subscribe({
          next: () => {
            this.notificationService.success('Torneo reiniciado exitosamente');
            this.loadTournaments();
            this.leaderboard = [];
            this.selectedTournamentId = null;
          },
          error: (err) => this.notificationService.error('Error al reiniciar el torneo')
        });
      }
    );
  }
  
  loadStats() {
    // Simular carga de estadísticas - en producción vendría del backend
    this.http.get<any[]>(`${environment.apiUrl}/admin/users`).subscribe(users => {
      this.stats.totalUsers = users.length;
    });
    
    this.http.get<any>(`${environment.apiUrl}/admin/stories`).subscribe(response => {
      this.stats.totalStories = response.totalElements || 0;
      this.stats.totalPublishedStories = response.content?.filter((s: any) => s.status === 'PUBLISHED').length || 0;
    });
  }
  
  loadPendingStories() {
    this.http.get<any>(`${environment.apiUrl}/admin/stories?status=MODERATED`).subscribe(response => {
      this.pendingStories = response.content || [];
    });
  }
  
  loadAllStories() {
    this.http.get<any>(`${environment.apiUrl}/admin/stories?size=50`).subscribe(response => {
      this.allStories = response.content || [];
    });
  }
  
  loadUsers() {
    this.http.get<UserManagement[]>(`${environment.apiUrl}/admin/users`).subscribe(users => {
      this.users = users;
    });
  }

  approveStory(storyId: number) {
    this.http.post(`${environment.apiUrl}/stories/${storyId}/publish`, {}).subscribe({
      next: () => {
        this.notificationService.success('Historia aprobada y publicada');
        this.loadPendingStories();
        this.loadAllStories();
      },
      error: (err) => {
        console.error(err);
        this.notificationService.error('Error al aprobar la historia');
      }
    });
  }
  
  deleteStory(storyId: number) {
    this.openConfirmation(
      'Eliminar Historia',
      '¿Estás seguro de eliminar esta historia?',
      () => {
        this.http.delete(`${environment.apiUrl}/stories/${storyId}`).subscribe({
          next: () => {
            this.notificationService.success('Historia eliminada');
            this.loadPendingStories();
            this.loadAllStories();
          },
          error: (err) => {
            console.error(err);
            this.notificationService.error('Error al eliminar la historia');
          }
        });
      }
    );
  }
  
  toggleUserStatus(userId: number, currentStatus: boolean) {
    // Endpoint para activar/desactivar usuario
    const action = currentStatus ? 'desactivar' : 'activar';
    this.openConfirmation(
      `${action.charAt(0).toUpperCase() + action.slice(1)} Usuario`,
      `¿Estás seguro de ${action} este usuario?`,
      () => {
        this.http.patch(`${environment.apiUrl}/users/${userId}/toggle-status`, {}).subscribe({
          next: () => {
            this.notificationService.success(`Usuario ${action}do`);
            this.loadUsers();
          },
          error: (err) => {
            console.error(err);
            this.notificationService.error(`Error al ${action} el usuario`);
          }
        });
      }
    );
  }
  
  changeUserRole(userId: number, currentRole: string) {
    const newRole = currentRole === 'USER' ? 'ADMIN' : 'USER';
    this.openConfirmation(
      'Cambiar Rol',
      `¿Cambiar rol a ${newRole}?`,
      () => {
        this.http.patch(`${environment.apiUrl}/users/${userId}/role`, { role: newRole }).subscribe({
          next: () => {
            this.notificationService.success('Rol actualizado');
            this.loadUsers();
          },
          error: (err) => {
            console.error(err);
            this.notificationService.error('Error al actualizar el rol');
          }
        });
      }
    );
  }
  
  setActiveTab(tab: 'stats' | 'stories' | 'users' | 'tournaments') {
    this.activeTab = tab;
  }
}
