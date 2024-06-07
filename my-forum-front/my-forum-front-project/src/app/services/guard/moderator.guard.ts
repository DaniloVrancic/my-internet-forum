import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../user.service';

export const moderatorGuard: CanActivateFn = (route, state) => {

  let userService = inject(UserService);
  let router = inject(Router);

  let localToken = userService.getJwtToken();
  let localUser = userService.getCurrentUser();
  let userType = null;
  if(localUser != null){

    userType = localUser.type;

  }
  else{
    return false;
  }


  if(userType != null && (userType === "Administrator" || userType === "Moderator")){
    return true;
  }
  else{
    if(localToken != null)
    router.navigate(['main-page']);
    else
    router.navigate(['']);

    return false;
  }
};
