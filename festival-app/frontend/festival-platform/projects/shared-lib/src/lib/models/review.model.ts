export interface Review {
  id: string;
  subjectId: string;
  subjectType: 'DJ' | 'PERFORMANCE' | 'EVENT';
  reviewerName: string;
  rating: number;
  comment: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateReviewRequest {
  subjectId: string;
  subjectType: 'DJ' | 'PERFORMANCE' | 'EVENT';
  reviewerName: string;
  rating: number;
  comment: string;
}

export interface UpdateReviewRequest {
  rating?: number;
  comment?: string;
}

export interface ReviewStats {
  averageRating: number;
  totalReviews: number;
  ratingDistribution: {
    [key: number]: number; // rating (1-5) -> count
  };
}

export interface ReviewSummary {
  subjectId: string;
  subjectName: string;
  subjectType: 'DJ' | 'PERFORMANCE' | 'EVENT';
  averageRating: number;
  reviewCount: number;
  latestReviews: Review[];
}