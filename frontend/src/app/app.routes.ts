import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'stories',
    loadComponent: () => import('./features/stories/story-list/story-list.component').then(m => m.StoryListComponent)
  },
  {
    path: 'stories/new',
    canActivate: [authGuard],
    loadComponent: () => import('./features/stories/story-form/story-form.component').then(m => m.StoryFormComponent)
  },
  {
    path: 'stories/edit/:id',
    canActivate: [authGuard],
    loadComponent: () => import('./features/stories/story-form/story-form.component').then(m => m.StoryFormComponent)
  },
  {
    path: 'stories/:id',
    loadComponent: () => import('./features/stories/story-detail/story-detail.component').then(m => m.StoryDetailComponent)
  },
  {
    path: 'create-story',
    canActivate: [authGuard],
    loadComponent: () => import('./features/stories/story-form/story-form.component').then(m => m.StoryFormComponent)
  },
  {
    path: 'my-stories',
    canActivate: [authGuard],
    loadComponent: () => import('./features/stories/my-stories/my-stories.component').then(m => m.MyStoriesComponent)
  },
  {
    path: 'tournaments',
    loadComponent: () => import('./features/tournaments/tournament-list.component').then(m => m.TournamentListComponent)
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    loadComponent: () => import('./features/admin/admin-dashboard.component').then(m => m.AdminDashboardComponent)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
