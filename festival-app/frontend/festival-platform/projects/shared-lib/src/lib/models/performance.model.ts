export interface Performance {
  id: string;
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  djId: string;
  durationInHours: number;
}

export interface CreatePerformanceRequest {
  title: string;
  description: string;
  startTime: string;
  endTime: string;
  djId: string;
}

export interface UpdatePerformanceRequest {
  title?: string;
  description?: string;
  startTime?: string;
  endTime?: string;
  djId?: string;
}

export interface PerformanceWithDJ extends Performance {
  djName: string;
  djGenre: string;
}