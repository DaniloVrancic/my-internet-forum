import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../../interfaces/user';
import { VerifyCodeRequest } from '../../verify-code-page/verify-code-request-interface';
import { environment } from '../../environments/environment';
import { UserPrivilegeUpdateRequest } from '../../interfaces/requests/user-privilege-update-request';
import { UserWithToken } from '../../interfaces/user-with-token';
import { PermissionService } from './permission.service';
import { response } from 'express';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private baseUserUrl = environment.apiBaseUrl + '/users';
  private baseAuthUrl = environment.apiBaseUrl + '/auth';
  private currentUser: User | null;
  private currentForumerTopicPermissions: string[] = [];
  permissionService = inject(PermissionService);

  constructor(private http: HttpClient) { 
    this.currentUser = null;
  }

  findAll(): Observable<any> {
    return this.http.get<any>(this.baseUserUrl);
  }

  findUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUserUrl}/${id}`);
  }

  registerUser(request: any): Observable<User> {
    return this.http.post<any>(`${this.baseAuthUrl}/register`, request);
  }

  loginUser(request: any): Observable<UserWithToken> {
    return this.http.post<UserWithToken>(`${this.baseAuthUrl}/login`, request)
  }

  addUser(request: any): Observable<any> {
    return this.http.post<any>(`${this.baseUserUrl}/add`, request);
  }

  verifyUser(request: VerifyCodeRequest): Observable<UserWithToken> {
    return this.http.post<UserWithToken>(`${this.baseAuthUrl}/verify`, request);
  }

  updateUser(userData: any): Observable<any> {
    return this.http.put<any>(`${this.baseUserUrl}/update`, userData);
  }

  changePrivilege(request: UserPrivilegeUpdateRequest): Observable<User> {
    return this.http.put<User>(`${this.baseUserUrl}/change-privileges`, request);
  }


  deleteUserById(id: number): Observable<string> {
    return this.http.delete<string>(`${this.baseUserUrl}/delete/${id}`);
  }

  deleteUserByUsername(username: string): Observable<string> {
    return this.http.delete<string>(`${this.baseUserUrl}/delete-username/${username}`);
  }

  resendVerificationCodeForUserId(id: number): Observable<string> {
    return this.http.post<string>(`${this.baseAuthUrl}/resend_code/${id}`, null);
  }

  resendVerificationCodeForUser(user: User): Observable<string> {
    return this.http.post<any>(`${this.baseAuthUrl}/resend_code/${user.id}`, null);
  }

  public setCurrentUser(user: User | null)
  {
    if(user == null)
    {
      this.removeCurrentUser();
    }
    else
    {
      localStorage.setItem(environment.userKeyString, JSON.stringify(user));
      this.currentUser = user; //Makes sure to always update the current user to the latest set User.
    }
  }

  public removeCurrentUser()
  {
    if(localStorage.getItem(environment.userKeyString) != null)
      {
        localStorage.removeItem(environment.userKeyString);
        this.currentUser = null;
      }
  }

  public getCurrentUser() : User | null
  {
    if(this.currentUser == null)
      {
        return JSON.parse(localStorage.getItem(environment.userKeyString) as string);
      }
    else
      return this.currentUser;
  }

  public setJwtToken(token : string){
    localStorage.setItem(environment.tokenStorageKey, JSON.stringify(token));
  }

  public getJwtToken(): string{
    return JSON.parse(localStorage.getItem(environment.tokenStorageKey) as string);
  }

  public deleteJwtToken(){
    localStorage.removeItem(environment.tokenStorageKey);
  }

  public fetchTopicPermissionsForCurrentUser(topicId: number){
        
        this.permissionService.findPermissionsForUserAndTopic({userId: this.getCurrentUser()?.id as number, topicId: topicId}).subscribe({
          next: (response: string[]) => {this.currentForumerTopicPermissions = response;
                                        sessionStorage.setItem(environment.currentForumerTopicPermissions, JSON.stringify(response));
          },
          error: errorObj => {console.error(errorObj)}
        });
  }

  public deleteFetchedPermissions(){
    sessionStorage.removeItem(environment.currentForumerTopicPermissions);
    this.currentForumerTopicPermissions = [];
  }

  public getFetchedPermissions(): string[]{
    if(this.currentForumerTopicPermissions != null){
      return this.currentForumerTopicPermissions;
    }
    else if(sessionStorage.getItem(environment.currentForumerTopicPermissions) != null){
      this.currentForumerTopicPermissions = JSON.parse(sessionStorage.getItem(environment.currentForumerTopicPermissions) as string)
      return (this.currentForumerTopicPermissions);
    }
    else{
      return [];
    }
  }
}