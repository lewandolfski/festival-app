import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DJ, CreateDJRequest, UpdateDJRequest } from '../models/dj.model';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root'
})
export class DjService {
  private readonly apiUrl = `${environment.festivalApiUrl}/api/djs`;

  constructor(private http: HttpClient) {}

  /**
   * Get all DJs
   */
  getDJs(): Observable<DJ[]> {
    return this.http.get<DJ[]>(this.apiUrl);
  }

  /**
   * Get DJ by ID
   */
  getDJ(id: string): Observable<DJ> {
    return this.http.get<DJ>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new DJ
   */
  createDJ(dj: CreateDJRequest): Observable<DJ> {
    return this.http.post<DJ>(this.apiUrl, dj);
  }

  /**
   * Update existing DJ
   */
  updateDJ(id: string, dj: UpdateDJRequest): Observable<DJ> {
    return this.http.put<DJ>(`${this.apiUrl}/${id}`, dj);
  }

  /**
   * Delete DJ
   */
  deleteDJ(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Search DJs by genre
   */
  getDJsByGenre(genre: string): Observable<DJ[]> {
    return this.http.get<DJ[]>(`${this.apiUrl}/genre/${genre}`);
  }

  /**
   * Search DJs by name
   */
  getDJsByName(name: string): Observable<DJ[]> {
    return this.http.get<DJ[]>(`${this.apiUrl}/name/${name}`);
  }

  /**
   * Get DJs with long names (>6 characters)
   */
  getDJsWithLongNames(): Observable<DJ[]> {
    return this.http.get<DJ[]>(`${this.apiUrl}/long-names`);
  }

  /**
   * Search DJs with query parameters
   */
  searchDJs(params: { name?: string; genre?: string; email?: string }): Observable<DJ[]> {
    let httpParams = new HttpParams();
    
    if (params.name) {
      httpParams = httpParams.set('name', params.name);
    }
    if (params.genre) {
      httpParams = httpParams.set('genre', params.genre);
    }
    if (params.email) {
      httpParams = httpParams.set('email', params.email);
    }

    return this.http.get<DJ[]>(this.apiUrl, { params: httpParams });
  }
}