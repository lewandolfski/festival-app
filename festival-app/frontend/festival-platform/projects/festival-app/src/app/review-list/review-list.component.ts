import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Review, ReviewService, DJ, DjService, Performance, PerformanceService, ConfirmDialogComponent, LoadingSpinnerComponent, ErrorMessageComponent } from 'shared-lib';

@Component({
  selector: 'app-review-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatChipsModule,
    MatDialogModule,
    MatTooltipModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent
  ],
  templateUrl: './review-list.component.html',
  styleUrl: './review-list.component.scss'
})
export class ReviewListComponent implements OnInit {
  reviews: Review[] = [];
  djs: Map<string, DJ> = new Map();
  performances: Map<string, Performance> = new Map();
  loading = false;
  error: any = null;
  searchTerm = '';
  selectedType = '';
  selectedRating = '';

  subjectTypes = ['DJ', 'PERFORMANCE', 'EVENT'];
  ratings = [5, 4, 3, 2, 1];

  constructor(
    private reviewService: ReviewService,
    private djService: DjService,
    private performanceService: PerformanceService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadReviews();
    this.loadDJs();
    this.loadPerformances();
  }

  loadReviews() {
    this.loading = true;
    this.error = null;
    
    this.reviewService.getReviews().subscribe({
      next: (reviews: Review[]) => {
        this.reviews = reviews;
        this.loading = false;
      },
      error: (error: any) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  loadDJs() {
    this.djService.getDJs().subscribe({
      next: (djs: DJ[]) => {
        djs.forEach(dj => this.djs.set(dj.id, dj));
      },
      error: (error: any) => {
        console.error('Failed to load DJs', error);
      }
    });
  }

  loadPerformances() {
    this.performanceService.getPerformances().subscribe({
      next: (performances: Performance[]) => {
        performances.forEach(p => this.performances.set(p.id, p));
      },
      error: (error: any) => {
        console.error('Failed to load performances', error);
      }
    });
  }

  getSubjectName(review: Review): string {
    if (review.subjectType === 'DJ') {
      return this.djs.get(review.subjectId)?.name || 'Unknown DJ';
    } else if (review.subjectType === 'PERFORMANCE') {
      return this.performances.get(review.subjectId)?.title || 'Unknown Performance';
    }
    return 'Event';
  }

  onSearch() {
    if (this.searchTerm.trim()) {
      this.loading = true;
      this.reviewService.getReviews().subscribe({
        next: (reviews: Review[]) => {
          this.reviews = reviews.filter(r => 
            r.reviewerName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
            r.comment.toLowerCase().includes(this.searchTerm.toLowerCase())
          );
          this.loading = false;
        },
        error: (error: any) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadReviews();
    }
  }

  onTypeFilter() {
    if (this.selectedType) {
      this.loading = true;
      this.reviewService.getReviews().subscribe({
        next: (reviews: Review[]) => {
          this.reviews = reviews.filter(r => r.subjectType === this.selectedType);
          this.loading = false;
        },
        error: (error: any) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadReviews();
    }
  }

  onRatingFilter() {
    if (this.selectedRating) {
      this.loading = true;
      this.reviewService.getReviewsByRating(parseInt(this.selectedRating)).subscribe({
        next: (reviews: Review[]) => {
          this.reviews = reviews;
          this.loading = false;
        },
        error: (error: any) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadReviews();
    }
  }

  onClearFilters() {
    this.searchTerm = '';
    this.selectedType = '';
    this.selectedRating = '';
    this.loadReviews();
  }

  onDeleteReview(review: Review) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Delete Review',
        message: `Are you sure you want to delete this review by ${review.reviewerName}?`,
        confirmText: 'Delete',
        cancelText: 'Cancel',
        type: 'danger'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteReview(review.id);
      }
    });
  }

  private deleteReview(id: string) {
    this.reviewService.deleteReview(id).subscribe({
      next: () => {
        this.reviews = this.reviews.filter(r => r.id !== id);
      },
      error: (error: any) => {
        this.error = error;
      }
    });
  }

  onRetry() {
    this.loadReviews();
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
      month: 'short', 
      day: 'numeric', 
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getStarArray(rating: number): number[] {
    return Array(5).fill(0).map((_, i) => i < rating ? 1 : 0);
  }
}
