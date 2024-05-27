import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../../interfaces/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private baseUrl = environment.apiBaseUrl + '/comment';

  constructor(private http: HttpClient) { }

  findAllComments(): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.baseUrl);
  }

  findAllByPostId(post_id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.baseUrl}/forum_post/${post_id}`);
  }

  findAllApprovedByPostId(post_id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.baseUrl}/find_approved/${post_id}`);
  }

  findAllByUserId(user_id: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.baseUrl}/user/${user_id}`);
  }

  addComment(commentRequest: any): Observable<Comment> {
    return this.http.post<any>(`${this.baseUrl}/add`, commentRequest);
  }

  editComment(commentRequest: any): Observable<Comment> {
    return this.http.put<Comment>(`${this.baseUrl}/edit`, commentRequest);
  }

  updateComment(commentRequest: any): Observable<Comment> {
    return this.http.put<Comment>(`${this.baseUrl}/update`, commentRequest);
  }

  changeStatus(commentId: number, status: string): Observable<Comment> {
    return this.http.put<Comment>(`${this.baseUrl}/change_status/${commentId}`, null, {
      params: {
        status: status
      }
    });
  }

  deleteComment(commentId: number): Observable<Comment> {
    return this.http.delete<Comment>(`${this.baseUrl}/delete/${commentId}`);
  }
}

