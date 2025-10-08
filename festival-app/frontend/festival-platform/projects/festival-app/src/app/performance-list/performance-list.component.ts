import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Performance, PerformanceService, DJ, DjService, ConfirmDialogComponent, LoadingSpinnerComponent, ErrorMessageComponent } from 'shared-lib';

@Component({
  selector: 'app-performance-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatFormFieldModule,
    MatDialogModule,
    MatTooltipModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent
  ],
  templateUrl: './performance-list.component.html',
  styleUrl: './performance-list.component.scss'
})
export class PerformanceListComponent implements OnInit {
  performances: Performance[] = [];
  djs: Map<string, DJ> = new Map();
  loading = false;
  error: any = null;
  searchTerm = '';

  constructor(
    private performanceService: PerformanceService,
    private djService: DjService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadPerformances();
    this.loadDJs();
  }

  loadPerformances() {
    this.loading = true;
    this.error = null;
    
    this.performanceService.getPerformances().subscribe({
      next: (performances) => {
        this.performances = performances;
        this.loading = false;
      },
      error: (error) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  loadDJs() {
    this.djService.getDJs().subscribe({
      next: (djs) => {
        djs.forEach(dj => this.djs.set(dj.id, dj));
      },
      error: (error) => {
        console.error('Failed to load DJs', error);
      }
    });
  }

  getDJName(djId: string): string {
    return this.djs.get(djId)?.name || 'Unknown DJ';
  }

  getDJGenre(djId: string): string {
    return this.djs.get(djId)?.genre || 'Unknown';
  }

  onSearch() {
    if (this.searchTerm.trim()) {
      this.loading = true;
      // Filter performances locally by title
      this.performanceService.getPerformances().subscribe({
        next: (performances: Performance[]) => {
          this.performances = performances.filter(p => 
            p.title.toLowerCase().includes(this.searchTerm.toLowerCase())
          );
          this.loading = false;
        },
        error: (error: any) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadPerformances();
    }
  }

  onClearFilters() {
    this.searchTerm = '';
    this.loadPerformances();
  }

  onDeletePerformance(performance: Performance) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Delete Performance',
        message: `Are you sure you want to delete "${performance.title}"? This action cannot be undone.`,
        confirmText: 'Delete',
        cancelText: 'Cancel',
        type: 'danger'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deletePerformance(performance.id);
      }
    });
  }

  private deletePerformance(id: string) {
    this.performanceService.deletePerformance(id).subscribe({
      next: () => {
        this.performances = this.performances.filter(p => p.id !== id);
      },
      error: (error) => {
        this.error = error;
      }
    });
  }

  onRetry() {
    this.loadPerformances();
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

  formatTime(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-US', { 
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
