export interface DJ {
  id: string;
  name: string;
  genre: string;
  email: string;
}

export interface CreateDJRequest {
  name: string;
  genre: string;
  email: string;
}

export interface UpdateDJRequest {
  name?: string;
  genre?: string;
  email?: string;
}

export const DJ_GENRES = [
  'Electronic',
  'House',
  'Techno',
  'Ambient',
  'Trance',
  'Drum & Bass',
  'Dubstep',
  'Deep House',
  'Progressive',
  'Minimal'
] as const;

export type DJGenre = typeof DJ_GENRES[number];