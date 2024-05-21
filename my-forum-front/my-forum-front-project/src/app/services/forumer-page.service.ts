import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ForumPost } from '../../interfaces/forum-post';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ForumerPageService {

  selectedTopicId: number | null;

  private baseUrl = environment.apiBaseUrl + '/forum_post';

  constructor(private http: HttpClient) { 
    this.selectedTopicId = null;
  }

  findAllForumPosts(): Observable<ForumPost[]> {
    return this.http.get<ForumPost[]>(this.baseUrl);
  }

  findAllByTopicId(topic_id: number): Observable<ForumPost[]>{
    return this.http.get<ForumPost[]>(`${this.baseUrl}/topic/${topic_id}`);
  }

  findAllByUserId(user_id: number): Observable<ForumPost[]>{
    return this.http.get<ForumPost[]>(`${this.baseUrl}/user/${user_id}`);
  }

  addForumPost(forumPostRequest: any): Observable<ForumPost> {
    return this.http.post<any>(`${this.baseUrl}/add`, forumPostRequest);
  }

  editForumPost(forumPostRequest: any): Observable<ForumPost> {
    return this.http.put<ForumPost>(`${this.baseUrl}/update`, forumPostRequest);
  }

  changeStatus(postId: number, status: string): Observable<ForumPost> {
    return this.http.put<ForumPost>(`${this.baseUrl}/change_status/${postId}`, null, {
      params: {
        status: status
      }
    });
  }

  deleteForumPost(postId: number): Observable<ForumPost> {
    return this.http.delete<ForumPost>(`${this.baseUrl}/delete/${postId}`);
  }

  getSelectedTopicId(){
    if(this.getSelectedTopicId == null || this.selectedTopicId as number < 0){
      return null;
    }
    else{
      return this.selectedTopicId;
    }
  }

  setSelectedTopicId(value: number | null){
    this.selectedTopicId = value;
  }


}
