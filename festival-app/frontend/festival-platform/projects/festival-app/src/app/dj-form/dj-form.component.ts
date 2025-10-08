import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DJ, DjService, CreateDJRequest, UpdateDJRequest, LoadingSpinnerComponent, ErrorMessageComponent } from 'shared-lib';

@Component({
  selector: 'app-dj-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatChipsModule,
    MatIconModule,
    MatSnackBarModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent
  ],
  templateUrl: './dj-form.component.html',
  styleUrl: './dj-form.component.scss'
})
export class DjFormComponent implements OnInit {
  djForm: FormGroup;
  isEditMode = false;
  djId?: string;
  loading = false;
  error: any = null;
  
  availableGenres = ['Electronic', 'House', 'Techno', 'Trance', 'Drum & Bass', 'Dubstep'];
  selectedGenre = '';

  constructor(
    private fb: FormBuilder,
    private djService: DjService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.djForm = this.createForm();
  }

  ngOnInit() {
    this.djId = this.route.snapshot.paramMap.get('id') || undefined;
    this.isEditMode = !!this.djId;
    
    if (this.isEditMode) {
      this.loadDJ();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      genre: ['', [Validators.required]]
    });
  }

  private loadDJ() {
    if (!this.djId) return;
    
    this.loading = true;
    this.djService.getDJ(this.djId).subscribe({
      next: (dj) => {
        this.selectedGenre = dj.genre;
        this.djForm.patchValue({
          name: dj.name,
          email: dj.email,
          genre: dj.genre
        });
        this.loading = false;
      },
      error: (error) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  onGenreChange(genre: string) {
    this.selectedGenre = genre;
    this.djForm.patchValue({ genre: genre });
  }

  onSubmit() {
    if (this.djForm.valid) {
      this.loading = true;
      const formValue = this.djForm.value;
      
      if (this.isEditMode) {
        this.updateDJ(formValue);
      } else {
        this.createDJ(formValue);
      }
    } else {
      this.markFormGroupTouched();
    }
  }

  private createDJ(formValue: any) {
    const request: CreateDJRequest = {
      name: formValue.name,
      email: formValue.email,
      genre: formValue.genre
    };

    this.djService.createDJ(request).subscribe({
      next: (dj) => {
        this.loading = false;
        this.snackBar.open('DJ created successfully!', 'Close', { duration: 3000 });
        this.router.navigate(['/djs']);
      },
      error: (error) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  private updateDJ(formValue: any) {
    if (!this.djId) return;
    
    const request: UpdateDJRequest = {
      name: formValue.name,
      email: formValue.email,
      genre: formValue.genre
    };

    this.djService.updateDJ(this.djId, request).subscribe({
      next: (dj) => {
        this.loading = false;
        this.snackBar.open('DJ updated successfully!', 'Close', { duration: 3000 });
        this.router.navigate(['/djs']);
      },
      error: (error) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  private markFormGroupTouched() {
    Object.keys(this.djForm.controls).forEach(key => {
      const control = this.djForm.get(key);
      control?.markAsTouched();
    });
  }

  onCancel() {
    this.router.navigate(['/djs']);
  }

  getErrorMessage(fieldName: string): string {
    const control = this.djForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    if (control?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    if (control?.hasError('minlength')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${control.errors?.['minlength'].requiredLength} characters`;
    }
    return '';
  }

  onRetry() {
    if (this.isEditMode) {
      this.loadDJ();
    } else {
      this.error = null;
    }
  }
}
