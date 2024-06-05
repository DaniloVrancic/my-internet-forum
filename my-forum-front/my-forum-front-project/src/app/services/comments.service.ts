import { Injectable } from '@angular/core';
import  Comment from '../../interfaces/comment';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentRequest } from '../../interfaces/requests/comment-request';
import { UpdateCommentRequest } from '../../interfaces/requests/update-comment-request';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private baseUrl = environment.apiBaseUrl + '/comment';
  private jsonHeaders = new HttpHeaders({'Content-Type': 'application/json'});

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

  findByCommentId(comment_id: number): Observable<Comment>{
    return this.http.get<Comment>(`${this.baseUrl}/${comment_id}`);
  }

  addComment(commentRequest: CommentRequest): Observable<Comment> {
    return this.http.post<any>(`${this.baseUrl}/add`, commentRequest, {
      headers: this.jsonHeaders
    });
  }

  editComment(commentRequest: CommentRequest): Observable<Comment> {
    return this.http.put<any>(`${this.baseUrl}/edit`, commentRequest, {
      headers: this.jsonHeaders
    });
  }

  updateComment(commentRequest: UpdateCommentRequest): Observable<Comment> {
    return this.http.put<any>(`${this.baseUrl}/update`, commentRequest, {
      headers: this.jsonHeaders
    });
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

