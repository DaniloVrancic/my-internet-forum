import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';
import { VerifyCodeRequest } from '../../verify-code-page/verify-code-request-interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUrl = environment.apiBaseUrl + '/users';

  constructor(private http: HttpClient) { }

  findAll(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  findUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  registerUser(request: any): Observable<User> {
    return this.http.post<any>(`${this.baseUrl}/register`, request);
  }

  addUser(request: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/add`, request);
  }

  verifyUser(request: VerifyCodeRequest): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/verify`, request);
  }

  updateUser(userData: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/update`, userData);
  }

  changeTypeUser(request: any): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/change-type`, request);
  }

  changeStatusUser(request: any): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/change-status`, request);
  }

  deleteUserById(id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/delete/${id}`);
  }

  deleteUserByUsername(username: string): Observable<string> {
    return this.http.delete<string>(`${this.baseUrl}/delete-username/${username}`);
  }

  public setCurrentUser(user: User | null)
  {
    //UserService.currentUser = user;
    if(user == null)
    {
      if(sessionStorage.getItem(environment.userKeyString) != null)
      {
        sessionStorage.removeItem(environment.userKeyString);
      }
    }
    else
    {
      sessionStorage.setItem(environment.userKeyString, JSON.stringify(user));
    }
  }

  public getCurrentUser() : User | null
  {
    return JSON.parse(sessionStorage.getItem(environment.userKeyString) as string);
  }
}