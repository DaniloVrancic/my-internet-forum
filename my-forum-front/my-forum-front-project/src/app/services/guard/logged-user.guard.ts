import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../user.service';

export const loggedUserGuard: CanActivateFn = (route, state) => {

  const userService = inject(UserService);
  const router = inject(Router);

  let currentlyLoggedUser = userService.getCurrentUser();

  if(currentlyLoggedUser != null){
    return true;
  }
  else{
    router.navigate(['']);
    return false;
  }
};
