import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../user.service';

export const administratorGuard: CanActivateFn = (route, state) => {
  let userService = inject(UserService);
  let router = inject(Router);

  let localToken = userService.getJwtToken(); //gets JWT token
  let localUser = userService.getCurrentUser(); //gets current user JSON and reads it
  let userType = null;

  if(localUser != null){

    userType = localUser.type;

  }
  else{
    return false;
  }


  if(userType != null && userType === "Administrator"){
    return true;
  }
  else{
      if(localToken != null)
        {
          router.navigate(['main-page']);
        }
      else{
        router.navigate(['']);
      }
      return false;
  }
};
