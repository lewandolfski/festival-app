import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

export interface ConfirmDialogData {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'warning' | 'danger' | 'info';
}

@Component({
  selector: 'lib-confirm-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <div class="dialog-content" [ngClass]="'dialog-' + (data.type || 'info')">
        <mat-icon *ngIf="data.type === 'warning'">warning</mat-icon>
        <mat-icon *ngIf="data.type === 'danger'">dangerous</mat-icon>
        <mat-icon *ngIf="data.type === 'info'">info</mat-icon>
        <p>{{ data.message }}</p>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">
        {{ data.cancelText || 'Cancel' }}
      </button>
      <button 
        mat-raised-button 
        [color]="getConfirmButtonColor()" 
        (click)="onConfirm()"
        cdkFocusInitial>
        {{ data.confirmText || 'Confirm' }}
      </button>
    </mat-dialog-actions>
  `,
  styles: [`
    .dialog-content {
      display: flex;
      align-items: center;
      gap: 16px;
      padding: 16px 0;
    }
    
    .dialog-content p {
      margin: 0;
    }
    
    .dialog-warning mat-icon {
      color: #ff9800;
    }
    
    .dialog-danger mat-icon {
      color: #f44336;
    }
    
    .dialog-info mat-icon {
      color: #2196f3;
    }
    
    mat-dialog-actions {
      padding: 16px 0 0 0;
    }
  `]
})
export class ConfirmDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData
  ) {}

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onConfirm(): void {
    this.dialogRef.close(true);
  }

  getConfirmButtonColor(): string {
    switch (this.data.type) {
      case 'danger':
        return 'warn';
      case 'warning':
        return 'accent';
      default:
        return 'primary';
    }
  }
}