import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';

import { Review, ReviewService } from 'shared-lib';

@Component({
  selector: 'app-review-list',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatPaginatorModule,
    MatSelectModule,
    MatFormFieldModule,
    FormsModule
  ],
  template: `
    <div class="container">
      <div class="header">
        <h1>Festival Reviews</h1>
        <p>Browse and manage all festival reviews</p>
      </div>

      <!-- Filters -->
      <div class="filters">
        <mat-form-field appearance="outline">
          <mat-label>Filter by Type</mat-label>
          <mat-select [(value)]="selectedSubjectType" (selectionChange)="applyFilters()">
            <mat-option value="">All Types</mat-option>
            <mat-option value="DJ">DJ Reviews</mat-option>
            <mat-option value="PERFORMANCE">Performance Reviews</mat-option>
            <mat-option value="EVENT">Event Reviews</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="outline">
          <mat-label>Minimum Rating</mat-label>
          <mat-select [(value)]="selectedMinRating" (selectionChange)="applyFilters()">
            <mat-option [value]="null">Any Rating</mat-option>
            <mat-option [value]="5">5 Stars</mat-option>
            <mat-option [value]="4">4+ Stars</mat-option>
            <mat-option [value]="3">3+ Stars</mat-option>
            <mat-option [value]="2">2+ Stars</mat-option>
            <mat-option [value]="1">1+ Stars</mat-option>
          </mat-select>
        </mat-form-field>

        <button mat-raised-button color="primary" (click)="loadReviews()">
          <mat-icon>refresh</mat-icon>
          Refresh
        </button>
      </div>

      <!-- Loading State -->
      <div *ngIf="loading" class="loading">
        <mat-spinner></mat-spinner>
        <p>Loading reviews...</p>
      </div>

      <!-- Reviews Grid -->
      <div *ngIf="!loading && paginatedReviews.length > 0" class="reviews-grid">
        <mat-card *ngFor="let review of paginatedReviews" class="review-card">
          <mat-card-header>
            <mat-card-title class="review-header">
              <span class="reviewer-name">{{ review.reviewerName }}</span>
              <mat-chip-set>
                <mat-chip [style.background-color]="getSubjectTypeColor(review.subjectType)">
                  {{ review.subjectType }}
                </mat-chip>
              </mat-chip-set>
            </mat-card-title>
            <mat-card-subtitle>
              Subject ID: {{ review.subjectId }} • {{ formatDate(review.createdAt) }}
            </mat-card-subtitle>
          </mat-card-header>

          <mat-card-content>
            <div class="rating">
              <span class="stars">
                <mat-icon 
                  *ngFor="let star of getStarArray(review.rating)"
                  [class.filled]="star"
                  class="star">
                  {{ star ? 'star' : 'star_border' }}
                </mat-icon>
              </span>
              <span class="rating-value">{{ review.rating }}/5</span>
            </div>
            <p class="comment">{{ review.comment }}</p>
          </mat-card-content>

          <mat-card-actions>
            <button mat-button color="primary">
              <mat-icon>edit</mat-icon>
              Edit
            </button>
            <button mat-button color="warn" (click)="deleteReview(review.id)">
              <mat-icon>delete</mat-icon>
              Delete
            </button>
          </mat-card-actions>
        </mat-card>
      </div>

      <!-- Empty State -->
      <div *ngIf="!loading && paginatedReviews.length === 0" class="empty-state">
        <mat-icon class="empty-icon">rate_review</mat-icon>
        <h2>No Reviews Found</h2>
        <p>No reviews match your current filters. Try adjusting your search criteria.</p>
        <button mat-raised-button color="primary" (click)="clearFilters()">
          Clear Filters
        </button>
      </div>

      <!-- Pagination -->
      <mat-paginator 
        *ngIf="!loading && filteredReviews.length > 0"
        [length]="filteredReviews.length"
        [pageSize]="pageSize"
        [pageSizeOptions]="pageSizeOptions"
        (page)="onPageChange($event)"
        showFirstLastButtons>
      </mat-paginator>

      <!-- Stats -->
      <div *ngIf="!loading && reviews.length > 0" class="stats">
        <mat-card>
          <mat-card-content>
            <h3>Review Statistics</h3>
            <div class="stats-grid">
              <div class="stat">
                <span class="stat-label">Total Reviews</span>
                <span class="stat-value">{{ reviews.length }}</span>
              </div>
              <div class="stat">
                <span class="stat-label">Average Rating</span>
                <span class="stat-value">{{ getAverageRating() | number:'1.1-1' }}/5</span>
              </div>
              <div class="stat">
                <span class="stat-label">DJ Reviews</span>
                <span class="stat-value">{{ getCountByType('DJ') }}</span>
              </div>
              <div class="stat">
                <span class="stat-label">Performance Reviews</span>
                <span class="stat-value">{{ getCountByType('PERFORMANCE') }}</span>
              </div>
              <div class="stat">
                <span class="stat-label">Event Reviews</span>
                <span class="stat-value">{{ getCountByType('EVENT') }}</span>
              </div>
            </div>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }

    .header {
      text-align: center;
      margin-bottom: 30px;
    }

    .header h1 {
      color: #1976d2;
      margin-bottom: 10px;
    }

    .filters {
      display: flex;
      gap: 20px;
      margin-bottom: 30px;
      flex-wrap: wrap;
      align-items: end;
    }

    .filters mat-form-field {
      min-width: 200px;
    }

    .loading {
      text-align: center;
      padding: 40px;
    }

    .loading mat-spinner {
      margin: 0 auto 20px auto;
    }

    .reviews-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }

    .review-card {
      height: fit-content;
    }

    .review-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
    }

    .reviewer-name {
      font-weight: bold;
      color: #1976d2;
    }

    .rating {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 15px;
    }

    .stars {
      display: flex;
      gap: 2px;
    }

    .star {
      font-size: 18px;
      color: #ffc107;
    }

    .star:not(.filled) {
      color: #e0e0e0;
    }

    .rating-value {
      font-weight: bold;
      color: #666;
    }

    .comment {
      line-height: 1.5;
      color: #444;
      margin: 0;
    }

    .empty-state {
      text-align: center;
      padding: 60px 20px;
      color: #666;
    }

    .empty-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #ccc;
      margin-bottom: 20px;
    }

    .stats {
      margin-top: 30px;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      gap: 20px;
      margin-top: 15px;
    }

    .stat {
      text-align: center;
      padding: 10px;
      border-radius: 8px;
      background-color: #f5f5f5;
    }

    .stat-label {
      display: block;
      font-size: 12px;
      color: #666;
      margin-bottom: 5px;
    }

    .stat-value {
      display: block;
      font-size: 24px;
      font-weight: bold;
      color: #1976d2;
    }

    mat-paginator {
      margin-top: 20px;
    }

    @media (max-width: 768px) {
      .reviews-grid {
        grid-template-columns: 1fr;
      }
      
      .filters {
        flex-direction: column;
        align-items: stretch;
      }
      
      .filters mat-form-field {
        min-width: auto;
      }
    }
  `]
})
export class ReviewListComponent implements OnInit {
  private reviewService = inject(ReviewService);
  private snackBar = inject(MatSnackBar);

  reviews: Review[] = [];
  filteredReviews: Review[] = [];
  paginatedReviews: Review[] = [];
  loading = false;

  // Filters
  selectedSubjectType = '';
  selectedMinRating: number | null = null;

  // Pagination
  pageSize = 10;
  pageSizeOptions = [5, 10, 25, 50];
  currentPage = 0;

  ngOnInit() {
    this.loadReviews();
  }

  loadReviews() {
    this.loading = true;
    this.reviewService.getReviews().subscribe({
      next: (reviews) => {
        this.reviews = reviews;
        this.applyFilters();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading reviews:', error);
        this.snackBar.open('Error loading reviews', 'Close', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  applyFilters() {
    let filtered = [...this.reviews];

    if (this.selectedSubjectType) {
      filtered = filtered.filter(review => review.subjectType === this.selectedSubjectType);
    }

    if (this.selectedMinRating !== null) {
      filtered = filtered.filter(review => review.rating >= this.selectedMinRating!);
    }

    this.filteredReviews = filtered;
    this.currentPage = 0;
    this.updatePaginatedReviews();
  }

  clearFilters() {
    this.selectedSubjectType = '';
    this.selectedMinRating = null;
    this.applyFilters();
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.updatePaginatedReviews();
  }

  updatePaginatedReviews() {
    const startIndex = this.currentPage * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    this.paginatedReviews = this.filteredReviews.slice(startIndex, endIndex);
  }

  deleteReview(reviewId: string) {
    if (confirm('Are you sure you want to delete this review?')) {
      this.reviewService.deleteReview(reviewId).subscribe({
        next: () => {
          this.snackBar.open('Review deleted successfully', 'Close', { duration: 3000 });
          this.loadReviews();
        },
        error: (error) => {
          console.error('Error deleting review:', error);
          this.snackBar.open('Error deleting review', 'Close', { duration: 3000 });
        }
      });
    }
  }

  getStarArray(rating: number): boolean[] {
    return Array(5).fill(0).map((_, index) => index < rating);
  }

  getSubjectTypeColor(type: string): string {
    const colors = {
      'DJ': '#2196F3',
      'PERFORMANCE': '#4CAF50', 
      'EVENT': '#FF9800'
    };
    return colors[type as keyof typeof colors] || '#9E9E9E';
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString();
  }

  getAverageRating(): number {
    if (this.reviews.length === 0) return 0;
    const sum = this.reviews.reduce((acc, review) => acc + review.rating, 0);
    return sum / this.reviews.length;
  }

  getCountByType(type: string): number {
    return this.reviews.filter(review => review.subjectType === type).length;
  }
}