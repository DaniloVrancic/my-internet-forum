import { CanActivateFn } from '@angular/router';

export const administratorGuard: CanActivateFn = (route, state) => {
  return true;
};
