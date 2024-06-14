import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../user.service';

export const authGuard: CanActivateFn = (route, state) => {

  let userService = inject(UserService);
  let router = inject(Router);

  let localToken = userService.getJwtToken();


  if(localToken != null){
    return true;
  }
  else{
    router.navigate(['']);
    return false;
  }
};
