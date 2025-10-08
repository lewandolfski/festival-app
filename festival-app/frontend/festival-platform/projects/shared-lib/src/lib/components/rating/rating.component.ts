import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'lib-rating',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  template: `
    <div class="rating-container">
      <div class="stars" [attr.aria-label]="'Rating: ' + rating + ' out of 5 stars'">
        <button 
          *ngFor="let star of stars; let i = index"
          mat-icon-button
          [disabled]="readonly"
          (click)="onStarClick(i + 1)"
          (mouseenter)="onStarHover(i + 1)"
          (mouseleave)="onMouseLeave()"
          class="star-button"
          [attr.aria-label]="'Rate ' + (i + 1) + ' star' + (i === 0 ? '' : 's')">
          <mat-icon [ngClass]="getStarClass(i + 1)">
            {{ getStarIcon(i + 1) }}
          </mat-icon>
        </button>
      </div>
      <span class="rating-text" *ngIf="showValue">
        {{ rating.toFixed(1) }} / 5
        <span *ngIf="reviewCount !== undefined" class="review-count">
          ({{ reviewCount }} review{{ reviewCount === 1 ? '' : 's' }})
        </span>
      </span>
    </div>
  `,
  styles: [`
    .rating-container {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    
    .stars {
      display: flex;
      align-items: center;
    }
    
    .star-button {
      padding: 4px;
      min-width: auto;
      width: 32px;
      height: 32px;
    }
    
    .star-button:not(:disabled) {
      cursor: pointer;
    }
    
    .star-filled {
      color: #ffd700;
    }
    
    .star-half {
      color: #ffd700;
    }
    
    .star-empty {
      color: #e0e0e0;
    }
    
    .star-hover {
      color: #ffed4e;
    }
    
    .rating-text {
      font-size: 14px;
      color: rgba(0, 0, 0, 0.7);
    }
    
    .review-count {
      color: rgba(0, 0, 0, 0.5);
    }
  `]
})
export class RatingComponent {
  @Input() rating: number = 0;
  @Input() reviewCount?: number;
  @Input() readonly: boolean = true;
  @Input() showValue: boolean = true;
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  
  @Output() ratingChange = new EventEmitter<number>();

  stars = Array(5).fill(0);
  hoverRating = 0;

  onStarClick(rating: number): void {
    if (!this.readonly) {
      this.rating = rating;
      this.ratingChange.emit(rating);
    }
  }

  onStarHover(rating: number): void {
    if (!this.readonly) {
      this.hoverRating = rating;
    }
  }

  onMouseLeave(): void {
    this.hoverRating = 0;
  }

  getStarIcon(position: number): string {
    const currentRating = this.hoverRating || this.rating;
    
    if (currentRating >= position) {
      return 'star';
    } else if (currentRating >= position - 0.5) {
      return 'star_half';
    } else {
      return 'star_border';
    }
  }

  getStarClass(position: number): string {
    const currentRating = this.hoverRating || this.rating;
    
    if (this.hoverRating && position <= this.hoverRating) {
      return 'star-hover';
    } else if (currentRating >= position) {
      return 'star-filled';
    } else if (currentRating >= position - 0.5) {
      return 'star-half';
    } else {
      return 'star-empty';
    }
  }
}