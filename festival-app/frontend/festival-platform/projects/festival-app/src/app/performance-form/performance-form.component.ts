import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Performance, PerformanceService, CreatePerformanceRequest, UpdatePerformanceRequest, DJ, DjService, LoadingSpinnerComponent, ErrorMessageComponent } from 'shared-lib';

@Component({
  selector: 'app-performance-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    LoadingSpinnerComponent,
    ErrorMessageComponent
  ],
  templateUrl: './performance-form.component.html',
  styleUrl: './performance-form.component.scss'
})
export class PerformanceFormComponent implements OnInit {
  performanceForm: FormGroup;
  isEditMode = false;
  performanceId?: string;
  loading = false;
  error: any = null;
  djs: DJ[] = [];

  constructor(
    private fb: FormBuilder,
    private performanceService: PerformanceService,
    private djService: DjService,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.performanceForm = this.createForm();
  }

  ngOnInit() {
    this.loadDJs();
    this.performanceId = this.route.snapshot.paramMap.get('id') || undefined;
    this.isEditMode = !!this.performanceId;
    
    if (this.isEditMode) {
      this.loadPerformance();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      startTime: ['', [Validators.required]],
      endTime: ['', [Validators.required]],
      djId: ['', [Validators.required]]
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

  private loadPerformance() {
    if (!this.performanceId) return;
    
    this.loading = true;
    this.performanceService.getPerformance(this.performanceId).subscribe({
      next: (performance: Performance) => {
        this.performanceForm.patchValue({
          title: performance.title,
          description: performance.description,
          startTime: performance.startTime,
          endTime: performance.endTime,
          djId: performance.djId
        });
        this.loading = false;
      },
      error: (error: any) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  onSubmit() {
    if (this.performanceForm.valid) {
      this.loading = true;
      const formValue = this.performanceForm.value;
      
      if (this.isEditMode) {
        this.updatePerformance(formValue);
      } else {
        this.createPerformance(formValue);
      }
    } else {
      this.markFormGroupTouched();
    }
  }

  private createPerformance(formValue: any) {
    const request: CreatePerformanceRequest = {
      title: formValue.title,
      description: formValue.description,
      startTime: new Date(formValue.startTime).toISOString(),
      endTime: new Date(formValue.endTime).toISOString(),
      djId: formValue.djId
    };

    this.performanceService.createPerformance(request).subscribe({
      next: (performance: Performance) => {
        this.loading = false;
        this.snackBar.open('Performance created successfully!', 'Close', { duration: 3000 });
        this.router.navigate(['/performances']);
      },
      error: (error: any) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  private updatePerformance(formValue: any) {
    if (!this.performanceId) return;
    
    const request: UpdatePerformanceRequest = {
      title: formValue.title,
      description: formValue.description,
      startTime: new Date(formValue.startTime).toISOString(),
      endTime: new Date(formValue.endTime).toISOString(),
      djId: formValue.djId
    };

    this.performanceService.updatePerformance(this.performanceId, request).subscribe({
      next: (performance: Performance) => {
        this.loading = false;
        this.snackBar.open('Performance updated successfully!', 'Close', { duration: 3000 });
        this.router.navigate(['/performances']);
      },
      error: (error: any) => {
        this.error = error;
        this.loading = false;
      }
    });
  }

  private markFormGroupTouched() {
    Object.keys(this.performanceForm.controls).forEach(key => {
      const control = this.performanceForm.get(key);
      control?.markAsTouched();
    });
  }

  onCancel() {
    this.router.navigate(['/performances']);
  }

  getErrorMessage(fieldName: string): string {
    const control = this.performanceForm.get(fieldName);
    if (control?.hasError('required')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
    }
    if (control?.hasError('minlength')) {
      return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${control.errors?.['minlength'].requiredLength} characters`;
    }
    return '';
  }

  onRetry() {
    if (this.isEditMode) {
      this.loadPerformance();
    } else {
      this.error = null;
    }
  }
}
