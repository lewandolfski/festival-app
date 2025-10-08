import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSliderModule } from '@angular/material/slider';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Review, ReviewService, CreateReviewRequest, DJ, DjService, Performance, PerformanceService, LoadingSpinnerComponent, ErrorMessageComponent } from 'shared-lib';

@Component({
  selector: 'app-review-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatSliderModule,
    MatSnackBarModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent
  ],
  templateUrl: './review-form.component.html',
  styleUrl: './review-form.component.scss'
})
export class ReviewFormComponent implements OnInit {
  reviewForm: FormGroup;
  loading = false;
  error: any = null;
  
  djs: DJ[] = [];
  performances: Performance[] = [];
  subjectTypes = ['DJ', 'PERFORMANCE'];
  selectedSubjectType = '';

  constructor(
    private fb: FormBuilder,
    private reviewService: ReviewService,
    private djService: DjService,
    private performanceService: PerformanceService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.reviewForm = this.createForm();
  }

  ngOnInit() {
    this.loadDJs();
    this.loadPerformances();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      subjectType: ['', [Validators.required]],
      subjectId: ['', [Validators.required]],
      reviewerName: ['', [Validators.required, Validators.minLength(2)]],
      rating: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  private loadDJs() {
    this.djService.getDJs().subscribe({
      next: (djs: DJ[]) => {
        this.djs = djs;
      },
      error: (error: any) => {
        console.error('Failed to load DJs', error);
      }
    });
  }

  private loadPerformances() {
    this.performanceService.getPerformances().subscribe({
      next: (performances: Performance[]) => {
        this.performances = performances;
      },
      error: (error: any) => {
        console.error('Failed to load performances', error);
      }
    });
  }

  onSubjectTypeChange(type: string) {
    this.selectedSubjectType = type;
    this.reviewForm.patchValue({ subjectId: '' });
  }

  getSubjectOptions(): any[] {
    if (this.selectedSubjectType === 'DJ') {
      return this.djs;
    } else if (this.selectedSubjectType === 'PERFORMANCE') {
      return this.performances;
    }
    return [];
  }

  getSubjectLabel(subject: any): string {
    if (this.selectedSubjectType === 'DJ') {
      return subject.name;
    } else if (this.selectedSubjectType === 'PERFORMANCE') {
      return subject.title;
    }
    return '';
  }

  onSubmit() {
    if (this.reviewForm.valid) {
      this.loading = true;
      const formValue = this.reviewForm.value;
      
      const request: CreateReviewRequest = {
        subjectId: formValue.subjectId,
        subjectType: formValue.subjectType,
        reviewerName: formValue.reviewerName,
        rating: parseInt(formValue.rating),
        comment: formValue.comment
      };

      console.log('Submitting review:', request);

      this.reviewService.createReview(request).subscribe({
        next: (review: Review) => {
          this.loading = false;
          this.snackBar.open('Review submitted successfully!', 'Close', { duration: 3000 });
          this.router.navigate(['/reviews']);
        },
        error: (error: any) => {
          console.error('Review submission error:', error);
          this.error = error;
          this.loading = false;
          this.snackBar.open('Failed to submit review. Please try again.', 'Close', { duration: 5000 });
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched() {
    Object.keys(this.reviewForm.controls).forEach(key => {
      const control = this.reviewForm.get(key);
      control?.markAsTouched();
    });
  }

  onCancel() {
    this.router.navigate(['/reviews']);
  }

  getErrorMessage(fieldName: string): string {
    const control = this.reviewForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    if (control?.hasError('minlength')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${control.errors?.['minlength'].requiredLength} characters`;
    }
    if (control?.hasError('min')) {
      return `Rating must be at least ${control.errors?.['min'].min}`;
    }
    if (control?.hasError('max')) {
      return `Rating must be at most ${control.errors?.['max'].max}`;
    }
    return '';
  }

  onRetry() {
    this.error = null;
  }

  getStarArray(): number[] {
    return [1, 2, 3, 4, 5];
  }
}
