import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Topic } from '../../interfaces/topic';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private baseUrl = environment.apiBaseUrl + '/topics';

  constructor(private http: HttpClient) { }

  findAllTopics(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.baseUrl);
  }

  findTopicById(id: number): Observable<Topic> {
    return this.http.get<Topic>(`${this.baseUrl}/${id}`);
  }

  findAllTopicsByNameContains(namePart: string): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.baseUrl}/contains/${namePart}`);
  }

  findAllTopicsByNameIs(name: string): Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.baseUrl}/is/${name}`);
  }
}
