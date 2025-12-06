import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tournament-list',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="container"><h1>Torneos</h1></div>`
})
export class TournamentListComponent {}
