import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { VerifyCodeRequest } from './verify-code-request-interface';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class VerifyService {
  private baseUrl = '/users'; // Assuming your backend is served from the same host


  constructor(private http: HttpClient) { }

  verifyUser(request: VerifyCodeRequest): Observable<User> {
    return this.http.post<any>(`${this.baseUrl}/verify`, request);
  }
}
