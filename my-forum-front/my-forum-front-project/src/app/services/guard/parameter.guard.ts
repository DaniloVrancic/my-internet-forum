import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const parameterGuard: CanActivateFn = (route, state) => {

  let router = inject(Router);
  
  const hasParameter = !!route.queryParams['code'];
  if (!hasParameter) {
    router.navigate(['/start-page']);
    return false;
  }
  return true;
};
