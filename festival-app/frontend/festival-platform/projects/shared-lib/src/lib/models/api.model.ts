export interface ApiResponse<T> {
  data: T;
  success: boolean;
  message?: string;
  errors?: string[];
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  pageNumber: number;
  pageSize: number;
  first: boolean;
  last: boolean;
}

export interface ApiError {
  status: number;
  message: string;
  timestamp: string;
  path: string;
  validationErrors?: {
    [field: string]: string;
  };
}

export interface HealthStatus {
  status: 'UP' | 'DOWN';
  components?: {
    [key: string]: {
      status: 'UP' | 'DOWN';
      details?: any;
    };
  };
}