import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `<div class="container"><h1>Panel de Administración</h1></div>`
})
export class AdminDashboardComponent {}
