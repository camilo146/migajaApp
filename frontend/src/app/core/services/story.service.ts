import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Story, StoryRequest, Page, RatingRequest, Comment, CommentRequest } from '../models/models';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StoryService {
  private readonly API_URL = `${environment.apiUrl}/stories`;
  private http = inject(HttpClient);
  
  getPublicStories(page: number = 0, size: number = 10, search?: string): Observable<Page<Story>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
      
    if (search) {
      params = params.set('search', search);
    }
    
    return this.http.get<Page<Story>>(`${this.API_URL}/public`, { params });
  }
  
  getStoryById(id: number): Observable<Story> {
    return this.http.get<Story>(`${this.API_URL}/${id}`);
  }
  
  getMyStories(page: number = 0, size: number = 10): Observable<Page<Story>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Story>>(`${this.API_URL}/my-stories`, { params });
  }
  
  createStory(story: StoryRequest): Observable<Story> {
    return this.http.post<Story>(this.API_URL, story);
  }
  
  updateStory(id: number, story: StoryRequest): Observable<Story> {
    return this.http.put<Story>(`${this.API_URL}/${id}`, story);
  }
  
  publishStory(id: number): Observable<Story> {
    return this.http.post<Story>(`${this.API_URL}/${id}/publish`, {});
  }

  submitStory(id: number): Observable<Story> {
    return this.http.post<Story>(`${this.API_URL}/${id}/submit`, {});
  }
  
  deleteStory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
  
  rateStory(id: number, rating: RatingRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/${id}/rate`, rating);
  }
  
  addComment(id: number, comment: CommentRequest): Observable<Comment> {
    return this.http.post<Comment>(`${this.API_URL}/${id}/comments`, comment);
  }
  
  getComments(id: number, page: number = 0, size: number = 10): Observable<Page<Comment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<Comment>>(`${this.API_URL}/${id}/comments`, { params });
  }
  
  deleteComment(commentId: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/comments/${commentId}`);
  }
}
