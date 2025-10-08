import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { DJ, DjService, ConfirmDialogComponent, LoadingSpinnerComponent, ErrorMessageComponent, RatingComponent } from 'shared-lib';

@Component({
  selector: 'app-dj-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatChipsModule,
    MatDialogModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent,
    RatingComponent
  ],
  templateUrl: './dj-list.component.html',
  styleUrl: './dj-list.component.scss'
})
export class DjListComponent implements OnInit {
  djs: DJ[] = [];
  loading = false;
  error: any = null;
  searchTerm = '';
  selectedGenre = '';
  
  genres = ['Electronic', 'House', 'Techno', 'Trance', 'Drum & Bass', 'Dubstep'];
  displayedColumns: string[] = ['name', 'email', 'genre', 'actions'];

  constructor(
    private djService: DjService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.loadDJs();
  }

  loadDJs() {
    this.loading = true;
    this.error = null;
    
    this.djService.getDJs().subscribe({
      next: (djs) => {
        this.djs = djs;
        this.loading = false;
      },
      error: (error) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  onSearch() {
    if (this.searchTerm.trim()) {
      this.loading = true;
      this.djService.getDJsByName(this.searchTerm).subscribe({
        next: (djs) => {
          this.djs = djs;
          this.loading = false;
        },
        error: (error) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadDJs();
    }
  }

  onGenreFilter() {
    if (this.selectedGenre) {
      this.loading = true;
      this.djService.getDJsByGenre(this.selectedGenre).subscribe({
        next: (djs) => {
          this.djs = djs;
          this.loading = false;
        },
        error: (error) => {
          this.error = error;
          this.loading = false;
        }
      });
    } else {
      this.loadDJs();
    }
  }

  onClearFilters() {
    this.searchTerm = '';
    this.selectedGenre = '';
    this.loadDJs();
  }

  onDeleteDJ(dj: DJ) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Delete DJ',
        message: `Are you sure you want to delete ${dj.name}? This action cannot be undone.`,
        confirmText: 'Delete',
        cancelText: 'Cancel',
        type: 'danger'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.deleteDJ(dj.id);
      }
    });
  }

  private deleteDJ(id: string) {
    this.djService.deleteDJ(id).subscribe({
      next: () => {
        this.djs = this.djs.filter(dj => dj.id !== id);
      },
      error: (error) => {
        this.error = error;
      }
    });
  }

  onRetry() {
    this.loadDJs();
  }
}
