import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { StoryService } from '../../../core/services/story.service';
import { NotificationService } from '../../../core/services/notification.service';
import { Story, Comment } from '../../../core/models/models';

@Component({
  selector: 'app-story-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './story-detail.component.html',
  styleUrl: './story-detail.component.scss'
})
export class StoryDetailComponent implements OnInit {
  story?: Story;
  comments: Comment[] = [];
  commentForm: FormGroup;
  ratingForm: FormGroup;
  isLoading = true;

  private notificationService = inject(NotificationService);

  constructor(
    private route: ActivatedRoute,
    private storyService: StoryService,
    private fb: FormBuilder
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(5)]]
    });
    this.ratingForm = this.fb.group({
      stars: [0, [Validators.required, Validators.min(1), Validators.max(5)]]
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const id = +params['id'];
      this.loadStory(id);
      this.loadComments(id);
    });
  }

  loadStory(id: number) {
    this.storyService.getStoryById(id).subscribe({
      next: (story) => {
        this.story = story;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading story:', error);
        this.notificationService.error('Error al cargar la historia');
        this.isLoading = false;
      }
    });
  }

  loadComments(id: number) {
    this.storyService.getComments(id, 0, 50).subscribe({
      next: (response) => {
        this.comments = response.content;
      },
      error: (error) => {
        console.error('Error loading comments:', error);
      }
    });
  }

  submitRating() {
    if (this.story && this.ratingForm.valid) {
      this.storyService.rateStory(this.story.id, this.ratingForm.value).subscribe({
        next: () => {
          this.loadStory(this.story!.id);
          this.notificationService.success('¡Calificación enviada!');
        },
        error: (error) => {
          console.error('Error rating story:', error);
          this.notificationService.error('Error al enviar calificación');
        }
      });
    }
  }

  submitComment() {
    if (this.story && this.commentForm.valid) {
      this.storyService.addComment(this.story.id, this.commentForm.value).subscribe({
        next: (comment) => {
          this.comments.unshift(comment);
          this.commentForm.reset();
          this.notificationService.success('Comentario publicado');
        },
        error: (error) => {
          console.error('Error adding comment:', error);
          this.notificationService.error('Error al publicar comentario');
        }
      });
    }
  }

  getStarArray(rating: number): boolean[] {
    return Array(5).fill(false).map((_, i) => i < Math.round(rating));
  }

  setRating(stars: number) {
    this.ratingForm.patchValue({ stars });
  }
}
