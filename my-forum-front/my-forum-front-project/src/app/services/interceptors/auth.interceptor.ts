import { HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { UserService } from '../user.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const userService = inject(UserService);
  const token = userService.getJwtToken();

  if (token) {
    const cloned = req.clone({
      headers: req.headers.set(
        'Authorization', `Bearer ${token}`)
      
    });
    return next(cloned);
  } else {
    return next(req);
  }

};
