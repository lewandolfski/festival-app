import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: '/djs', pathMatch: 'full' },
  { 
    path: 'djs', 
    loadComponent: () => import('./dj-list/dj-list.component').then(m => m.DjListComponent)
  },
  { 
    path: 'dj-form', 
    loadComponent: () => import('./dj-form/dj-form.component').then(m => m.DjFormComponent)
  },
  { 
    path: 'dj-form/:id', 
    loadComponent: () => import('./dj-form/dj-form.component').then(m => m.DjFormComponent)
  },
  { 
    path: 'performances', 
    loadComponent: () => import('./performance-list/performance-list.component').then(m => m.PerformanceListComponent)
  },
  { 
    path: 'performance-form', 
    loadComponent: () => import('./performance-form/performance-form.component').then(m => m.PerformanceFormComponent)
  },
  { 
    path: 'performance-form/:id', 
    loadComponent: () => import('./performance-form/performance-form.component').then(m => m.PerformanceFormComponent)
  },
  { 
    path: 'reviews', 
    loadComponent: () => import('./review-list/review-list.component').then(m => m.ReviewListComponent)
  },
  { 
    path: 'review-form', 
    loadComponent: () => import('./review-form/review-form.component').then(m => m.ReviewFormComponent)
  }
];
