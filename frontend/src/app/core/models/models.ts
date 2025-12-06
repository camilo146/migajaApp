export interface User {
  id: number;
  username: string;
  email: string;
  fullName: string;
  phoneNumber?: string;
  bio?: string;
  avatarUrl?: string;
  role: 'USER' | 'ADMIN';
  active: boolean;
  createdAt: Date;
}

export interface Story {
  id: number;
  title: string;
  content: string;
  author: Author;
  relationshipStartDate: Date;
  relationshipEndDate?: Date;
  city?: string;
  municipality?: string;
  department?: string;
  country?: string;
  relationshipDurationDays?: number;
  status: 'DRAFT' | 'PUBLISHED' | 'DELETED' | 'MODERATED';
  photos: Photo[];
  viewCount: number;
  averageRating: number;
  ratingCount: number;
  createdAt: Date;
  publishedAt?: Date;
  verified?: boolean;
}

export interface Author {
  id: number;
  username: string;
  fullName: string;
  avatarUrl?: string;
}

export interface Photo {
  id: number;
  url: string;
  caption?: string;
  photoDate?: Date;
  displayOrder: number;
}

export interface Comment {
  id: number;
  content: string;
  user: {
    id: number;
    username: string;
    fullName: string;
    avatarUrl?: string;
  };
  createdAt: Date;
}

export interface AuthResponse {
  id: number;
  token: string;
  username: string;
  email: string;
  fullName: string;
  role: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

export interface StoryRequest {
  title: string;
  content: string;
  relationshipStartDate: Date;
  relationshipEndDate?: Date;
  city?: string;
  municipality?: string;
  department?: string;
  country?: string;
  relationshipDurationDays?: number;
}

export interface RatingRequest {
  stars: number;
}

export interface CommentRequest {
  content: string;
}

export interface Tournament {
  id: number;
  title: string;
  description: string;
  entryFee: number;
  prizeAmount: number;
  startDate: Date;
  endDate: Date;
  votingStartDate: Date;
  votingEndDate: Date;
  status: 'UPCOMING' | 'REGISTRATION_OPEN' | 'VOTING' | 'ENDED' | 'CANCELLED';
  entryCount?: number;
  winner?: TournamentEntry;
}

export interface TournamentEntry {
  id: number;
  storyId: number;
  storyTitle: string;
  voteCount: number;
  averageRating: number;
  score?: number;
  category?: 'NORMAL' | 'MIGAJERO' | 'ARRASTRADO';
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'DISQUALIFIED';
  registeredAt: Date;
}

export interface TournamentRequest {
  title: string;
  description: string;
  entryFee: number;
  prizeAmount: number;
  startDate: Date;
  endDate: Date;
  votingStartDate: Date;
  votingEndDate: Date;
}

export interface TournamentRegistrationRequest {
  storyId: number;
  paymentTransactionId: string;
}

export interface Tournament {
  id: number;
  title: string;
  description: string;
  entryFee: number;
  prizeAmount: number;
  startDate: Date;
  endDate: Date;
  votingStartDate: Date;
  votingEndDate: Date;
  status: 'UPCOMING' | 'REGISTRATION_OPEN' | 'VOTING' | 'ENDED' | 'CANCELLED';
  createdAt: Date;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
