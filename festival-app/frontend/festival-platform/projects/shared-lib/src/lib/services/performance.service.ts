import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Performance, CreatePerformanceRequest, UpdatePerformanceRequest } from '../models/performance.model';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root'
})
export class PerformanceService {
  private readonly apiUrl = `${environment.festivalApiUrl}/api/performances`;

  constructor(private http: HttpClient) {}

  /**
   * Get all performances
   */
  getPerformances(): Observable<Performance[]> {
    return this.http.get<Performance[]>(this.apiUrl);
  }

  /**
   * Get performance by ID
   */
  getPerformance(id: string): Observable<Performance> {
    return this.http.get<Performance>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new performance
   */
  createPerformance(performance: CreatePerformanceRequest): Observable<Performance> {
    return this.http.post<Performance>(this.apiUrl, performance);
  }

  /**
   * Update existing performance
   */
  updatePerformance(id: string, performance: UpdatePerformanceRequest): Observable<Performance> {
    return this.http.put<Performance>(`${this.apiUrl}/${id}`, performance);
  }

  /**
   * Delete performance
   */
  deletePerformance(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get performances by DJ
   */
  getPerformancesByDJ(djId: string): Observable<Performance[]> {
    return this.http.get<Performance[]>(`${this.apiUrl}/dj/${djId}`);
  }

  /**
   * Get performances by stage
   */
  getPerformancesByStage(stage: string): Observable<Performance[]> {
    return this.http.get<Performance[]>(`${this.apiUrl}/stage/${stage}`);
  }

  /**
   * Get performances by date range
   */
  getPerformancesByDateRange(startDate: string, endDate: string): Observable<Performance[]> {
    let params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    
    return this.http.get<Performance[]>(`${this.apiUrl}/date-range`, { params });
  }

  /**
   * Get upcoming performances
   */
  getUpcomingPerformances(): Observable<Performance[]> {
    return this.http.get<Performance[]>(`${this.apiUrl}/upcoming`);
  }

  /**
   * Get performances by genre
   */
  getPerformancesByGenre(genre: string): Observable<Performance[]> {
    return this.http.get<Performance[]>(`${this.apiUrl}/genre/${genre}`);
  }

  /**
   * Search performances with query parameters
   */
  searchPerformances(params: { 
    stage?: string; 
    djId?: string; 
    startDate?: string; 
    endDate?: string;
    genre?: string;
  }): Observable<Performance[]> {
    let httpParams = new HttpParams();
    
    if (params.stage) {
      httpParams = httpParams.set('stage', params.stage);
    }
    if (params.djId) {
      httpParams = httpParams.set('djId', params.djId);
    }
    if (params.startDate) {
      httpParams = httpParams.set('startDate', params.startDate);
    }
    if (params.endDate) {
      httpParams = httpParams.set('endDate', params.endDate);
    }
    if (params.genre) {
      httpParams = httpParams.set('genre', params.genre);
    }

    return this.http.get<Performance[]>(this.apiUrl, { params: httpParams });
  }
}