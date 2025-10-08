import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'lib-error-message',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule],
  template: `
    <mat-card class="error-card" *ngIf="error">
      <mat-card-content>
        <div class="error-content">
          <mat-icon color="warn">error</mat-icon>
          <div class="error-text">
            <h4>{{ title }}</h4>
            <p>{{ message }}</p>
            <button mat-button color="primary" *ngIf="showRetry" (click)="onRetry()">
              Retry
            </button>
          </div>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .error-card {
      margin: 16px 0;
      border-left: 4px solid #f44336;
    }
    
    .error-content {
      display: flex;
      align-items: flex-start;
      gap: 16px;
    }
    
    .error-text h4 {
      margin: 0 0 8px 0;
      color: #f44336;
    }
    
    .error-text p {
      margin: 0 0 16px 0;
      color: rgba(0, 0, 0, 0.7);
    }
    
    mat-icon {
      margin-top: 4px;
    }
  `]
})
export class ErrorMessageComponent {
  @Input() error: any;
  @Input() title: string = 'Error';
  @Input() message: string = 'An error occurred. Please try again.';
  @Input() showRetry: boolean = true;
  @Input() retryCallback?: () => void;

  onRetry() {
    if (this.retryCallback) {
      this.retryCallback();
    }
  }
}