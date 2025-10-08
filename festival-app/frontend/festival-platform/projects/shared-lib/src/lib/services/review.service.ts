import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Review, CreateReviewRequest, UpdateReviewRequest } from '../models/review.model';
import { environment } from '../config/environment';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private readonly apiUrl = `${environment.reviewApiUrl}/api/reviews`;

  constructor(private http: HttpClient) {}

  /**
   * Get all reviews
   */
  getReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.apiUrl);
  }

  /**
   * Get review by ID
   */
  getReview(id: string): Observable<Review> {
    return this.http.get<Review>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new review
   */
  createReview(review: CreateReviewRequest): Observable<Review> {
    return this.http.post<Review>(this.apiUrl, review);
  }

  /**
   * Update existing review
   */
  updateReview(id: string, review: UpdateReviewRequest): Observable<Review> {
    return this.http.put<Review>(`${this.apiUrl}/${id}`, review);
  }

  /**
   * Delete review
   */
  deleteReview(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get reviews by subject (DJ, Performance, or Event)
   */
  getReviewsBySubject(subjectId: string, subjectType?: 'DJ' | 'PERFORMANCE' | 'EVENT'): Observable<Review[]> {
    let params = new HttpParams().set('subjectId', subjectId);
    if (subjectType) {
      params = params.set('subjectType', subjectType);
    }
    return this.http.get<Review[]>(`${this.apiUrl}/subject`, { params });
  }

  /**
   * Get reviews by DJ
   */
  getReviewsByDJ(djId: string): Observable<Review[]> {
    return this.getReviewsBySubject(djId, 'DJ');
  }

  /**
   * Get reviews by performance
   */
  getReviewsByPerformance(performanceId: string): Observable<Review[]> {
    return this.getReviewsBySubject(performanceId, 'PERFORMANCE');
  }

  /**
   * Get reviews by rating
   */
  getReviewsByRating(rating: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/rating/${rating}`);
  }

  /**
   * Get reviews with minimum rating
   */
  getReviewsWithMinRating(minRating: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/min-rating/${minRating}`);
  }

  /**
   * Get reviews by reviewer
   */
  getReviewsByReviewer(reviewerName: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/reviewer/${reviewerName}`);
  }

  /**
   * Get recent reviews
   */
  getRecentReviews(days: number = 7): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/recent/${days}`);
  }

  /**
   * Get review statistics for a DJ
   */
  getDJReviewStats(djId: string): Observable<{
    totalReviews: number;
    averageRating: number;
    ratingDistribution: { [key: number]: number };
  }> {
    return this.http.get<any>(`${this.apiUrl}/stats/dj/${djId}`);
  }

  /**
   * Get review statistics for a performance
   */
  getPerformanceReviewStats(performanceId: string): Observable<{
    totalReviews: number;
    averageRating: number;
    ratingDistribution: { [key: number]: number };
  }> {
    return this.http.get<any>(`${this.apiUrl}/stats/performance/${performanceId}`);
  }

  /**
   * Search reviews with query parameters
   */
  searchReviews(params: {
    djId?: string;
    performanceId?: string;
    reviewerName?: string;
    minRating?: number;
    maxRating?: number;
    startDate?: string;
    endDate?: string;
  }): Observable<Review[]> {
    let httpParams = new HttpParams();
    
    if (params.djId) {
      httpParams = httpParams.set('djId', params.djId);
    }
    if (params.performanceId) {
      httpParams = httpParams.set('performanceId', params.performanceId);
    }
    if (params.reviewerName) {
      httpParams = httpParams.set('reviewerName', params.reviewerName);
    }
    if (params.minRating !== undefined) {
      httpParams = httpParams.set('minRating', params.minRating.toString());
    }
    if (params.maxRating !== undefined) {
      httpParams = httpParams.set('maxRating', params.maxRating.toString());
    }
    if (params.startDate) {
      httpParams = httpParams.set('startDate', params.startDate);
    }
    if (params.endDate) {
      httpParams = httpParams.set('endDate', params.endDate);
    }

    return this.http.get<Review[]>(this.apiUrl, { params: httpParams });
  }
}