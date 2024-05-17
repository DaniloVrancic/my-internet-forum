import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Permission } from '../../interfaces/pemission';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  private baseUrl = environment.apiBaseUrl + '/permissions';

  constructor(private http: HttpClient) {}

  findAllPermissions(): Observable<Permission[]> {
    return this.http.get<Permission[]>(this.baseUrl);
  }

  findPermissionsByUserId(userId: number): Observable<Permission[]> {
    return this.http.get<Permission[]>(`${this.baseUrl}/${userId}`);
  }

  addPermission(permissionRequest: any): Observable<Permission> {
    return this.http.post<any>(`${this.baseUrl}/add`, permissionRequest);
  }

  deletePermissionById(permissionId: number): Observable<Permission> {
    return this.http.delete<any>(`${this.baseUrl}/delete/${permissionId}`);
  }

  deleteAllPermissionsForUserId(userId: number): Observable<Permission[]> {
    return this.http.delete<Permission[]>(`${this.baseUrl}/delete_by_user_id/${userId}`);
  }
}
