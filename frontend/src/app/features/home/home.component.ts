import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { StoryService } from '../../core/services/story.service';
import { TournamentService } from '../../core/services/tournament.service';
import { Story, Tournament } from '../../core/models/models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  private authService = inject(AuthService);
  private storyService = inject(StoryService);
  private tournamentService = inject(TournamentService);
  
  featuredStories: Story[] = [];
  activeTournament: Tournament | null = null;
  isAuthenticated = this.authService.isAuthenticated();
  
  ngOnInit() {
    this.loadFeaturedStories();
    this.loadActiveTournament();
  }
  
  loadFeaturedStories() {
    this.storyService.getPublicStories(0, 6).subscribe({
      next: (page) => {
        this.featuredStories = page.content;
      },
      error: (err) => console.error('Error loading stories:', err)
    });
  }

  loadActiveTournament() {
    this.tournamentService.getAllTournaments().subscribe({
      next: (tournaments) => {
        // Find active tournament (Registration Open or Voting)
        this.activeTournament = tournaments.find(t => 
          t.status === 'REGISTRATION_OPEN' || t.status === 'VOTING' || t.status === 'ENDED'
        ) || null;
      },
      error: (err) => console.error('Error loading tournaments:', err)
    });
  }
}
