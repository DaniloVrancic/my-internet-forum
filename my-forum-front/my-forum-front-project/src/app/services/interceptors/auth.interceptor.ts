import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable, catchError, throwError } from 'rxjs';
import { UserService } from '../user.service';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const userService = inject(UserService);
  const router = inject(Router);
  const token = userService.getJwtToken();

  if (token) {
    const cloned = req.clone({
      headers: req.headers.set(
        'Authorization', `Bearer ${token}`)
      
    });
    return next(cloned)
    .pipe( //Handling expired token
      catchError((error) => {
        if (error.status === 401) {
          // Token expired, delete token and redirect
            userService.deleteJwtToken();
            router.navigate(['']);
        }
        throw (error);
      })
    );
  } else {
    return next(req);
  }

};
