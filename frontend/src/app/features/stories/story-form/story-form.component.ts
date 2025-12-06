import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { StoryService } from '../../../core/services/story.service';

@Component({
  selector: 'app-story-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './story-form.component.html',
  styleUrl: './story-form.component.scss'
})
export class StoryFormComponent implements OnInit {
  storyForm: FormGroup;
  isEditMode = false;
  storyId?: number;
  isSubmitting = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private storyService: StoryService,
    public router: Router,
    private route: ActivatedRoute
  ) {
    this.storyForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(50)]],
      relationshipStartDate: ['', Validators.required],
      relationshipEndDate: [''],
      city: [''],
      municipality: [''],
      department: [''],
      country: ['Colombia'],
      relationshipDurationDays: ['']
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.isEditMode = true;
        this.storyId = +params['id'];
        this.loadStory();
      }
    });
  }

  loadStory() {
    if (this.storyId) {
      this.storyService.getStoryById(this.storyId).subscribe({
        next: (story) => {
          this.storyForm.patchValue({
            title: story.title,
            content: story.content,
            relationshipStartDate: new Date(story.relationshipStartDate).toISOString().split('T')[0],
            relationshipEndDate: story.relationshipEndDate ? new Date(story.relationshipEndDate).toISOString().split('T')[0] : '',
            city: story.city || '',
            municipality: story.municipality || '',
            department: story.department || '',
            country: story.country || 'Colombia',
            relationshipDurationDays: story.relationshipDurationDays || ''
          });
        },
        error: (error) => {
          this.errorMessage = 'Error al cargar la historia';
          console.error(error);
        }
      });
    }
  }

  calculateDuration() {
    const startDate = this.storyForm.get('relationshipStartDate')?.value;
    const endDate = this.storyForm.get('relationshipEndDate')?.value;
    
    if (startDate && endDate) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end.getTime() - start.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      this.storyForm.patchValue({ relationshipDurationDays: diffDays });
    }
  }

  onSubmit() {
    if (this.storyForm.valid) {
      this.isSubmitting = true;
      this.errorMessage = '';

      const formValue = this.storyForm.value;
      const storyData = {
        ...formValue,
        relationshipStartDate: new Date(formValue.relationshipStartDate),
        relationshipEndDate: formValue.relationshipEndDate ? new Date(formValue.relationshipEndDate) : undefined
      };

      const operation = this.isEditMode && this.storyId
        ? this.storyService.updateStory(this.storyId, storyData)
        : this.storyService.createStory(storyData);

      operation.subscribe({
        next: (response) => {
          this.router.navigate(['/my-stories']);
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Error al guardar la historia';
          this.isSubmitting = false;
        }
      });
    } else {
      this.errorMessage = 'Por favor completa todos los campos requeridos correctamente.';
      this.markFormGroupTouched(this.storyForm);
    }
  }

  private markFormGroupTouched(formGroup: FormGroup) {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }
}
