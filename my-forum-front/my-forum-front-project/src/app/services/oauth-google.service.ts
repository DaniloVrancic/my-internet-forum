import { Injectable, inject } from '@angular/core';
import { Router, response } from 'express';
import { environment } from '../../environments/environment';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Token } from '../../interfaces/token';

@Injectable({
  providedIn: 'root'
})
export class OauthGoogleService {

  token: string = "";
  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient){}

  get(url: string): any {
    return this.http.get(this.baseUrl + url)
  }

  getToken(code: string): Observable<boolean>{
    return this.http.get<Token>(this.baseUrl + "/auth/oauth2/callback?code=" + code, {observe: "response"})
    .pipe(map((response: HttpResponse<Token>) => {
      if(response.status == 200 && response.body !== null) {
        this.token = response.body.token;
        console.log(this.token);
        return true;
      }
      else{
        return false;
      }
    }));
  }


}
