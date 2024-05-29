import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ForumPost } from '../../interfaces/forum-post';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ModeratorPageService {

  private baseForumUrl = environment.apiBaseUrl + '/forum_post';
  private baseCommentUrl = environment.apiBaseUrl + '/comment';

  constructor(public http: HttpClient) { }

  findAllForumPosts(): Observable<ForumPost[]> {
    return this.http.get<ForumPost[]>(this.baseForumUrl);
  }

  findAllPendingForumPosts(): Observable<ForumPost[]>{
    return this.http.get<ForumPost[]>(this.baseForumUrl + "/status_pending");
  }

  findAllComments(): Observable<Comment[]>{
    return this.http.get<Comment[]>(this.baseCommentUrl);
  }

  findAllPendingComments(): Observable<Comment[]>{
    return this.http.get<Comment[]>(this.baseCommentUrl + "/status_pending");
  }

  
}
