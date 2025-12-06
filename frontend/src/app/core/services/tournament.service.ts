import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Tournament, TournamentEntry, TournamentRegistrationRequest, TournamentRequest } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/tournaments`;

  getAllTournaments(): Observable<Tournament[]> {
    return this.http.get<Tournament[]>(this.apiUrl);
  }

  getTournament(id: number): Observable<Tournament> {
    return this.http.get<Tournament>(`${this.apiUrl}/${id}`);
  }

  createTournament(request: TournamentRequest): Observable<Tournament> {
    return this.http.post<Tournament>(this.apiUrl, request);
  }

  updateTournament(id: number, request: TournamentRequest): Observable<Tournament> {
    return this.http.put<Tournament>(`${this.apiUrl}/${id}`, request);
  }

  registerForTournament(id: number, request: TournamentRegistrationRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/register`, request, { responseType: 'text' });
  }

  getMyEntries(): Observable<TournamentEntry[]> {
    return this.http.get<TournamentEntry[]>(`${this.apiUrl}/my-entries`);
  }

  getRankings(id: number): Observable<TournamentEntry[]> {
    return this.http.get<TournamentEntry[]>(`${this.apiUrl}/${id}/rankings`);
  }

  getLeaderboard(id: number): Observable<TournamentEntry[]> {
    return this.http.get<TournamentEntry[]>(`${this.apiUrl}/${id}/leaderboard`);
  }

  calculateScores(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/calculate-scores`, {}, { responseType: 'text' });
  }

  publishWinner(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/publish-winner`, {}, { responseType: 'text' });
  }

  resetTournament(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/reset`, {}, { responseType: 'text' });
  }

  verifyEntry(entryId: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/entries/${entryId}/verify`, {}, { responseType: 'text' });
  }

  openRegistration(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/open-registration`, {}, { responseType: 'text' });
  }

  startVoting(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/start-voting`, {}, { responseType: 'text' });
  }

  endTournament(id: number): Observable<string> {
    return this.http.post(`${this.apiUrl}/${id}/end`, {}, { responseType: 'text' });
  }

  getPendingEntries(): Observable<TournamentEntry[]> {
    return this.http.get<TournamentEntry[]>(`${this.apiUrl}/entries/pending`);
  }
}
