import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { StoryService } from '../../../core/services/story.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Story } from '../../../core/models/models';
import { ConfirmationModalComponent } from '../../../shared/components/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-my-stories',
  standalone: true,
  imports: [CommonModule, RouterLink, ConfirmationModalComponent],
  templateUrl: './my-stories.component.html',
  styleUrl: './my-stories.component.scss'
})
export class MyStoriesComponent implements OnInit {
  stories: Story[] = [];
  currentPage = 0;
  totalPages = 0;
  isLoading = false;
  
  // Modal State
  isModalOpen = false;
  modalTitle = '';
  modalMessage = '';
  pendingAction: (() => void) | null = null;
  
  private storyService = inject(StoryService);
  private notificationService = inject(NotificationService);

  ngOnInit() {
    this.loadMyStories();
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

  loadMyStories() {
    this.isLoading = true;
    this.storyService.getMyStories(this.currentPage, 10).subscribe({
      next: (response) => {
        this.stories = response.content;
        this.totalPages = response.totalPages;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading my stories:', error);
        this.notificationService.error('Error al cargar tus historias');
        this.isLoading = false;
      }
    });
  }

  submitStory(id: number) {
    this.storyService.submitStory(id).subscribe({
      next: () => {
        this.notificationService.success('Historia enviada a revisión exitosamente');
        this.loadMyStories();
      },
      error: (error) => {
        console.error('Error submitting story:', error);
        this.notificationService.error('Error al enviar la historia');
      }
    });
  }

  deleteStory(id: number) {
    this.openConfirmation(
      'Eliminar Historia',
      '¿Estás seguro de eliminar esta historia?',
      () => {
        this.storyService.deleteStory(id).subscribe({
          next: () => {
            this.notificationService.success('Historia eliminada');
            this.loadMyStories();
          },
          error: (error) => {
            console.error('Error deleting story:', error);
            this.notificationService.error('Error al eliminar la historia');
          }
        });
      }
    );
  }
}
