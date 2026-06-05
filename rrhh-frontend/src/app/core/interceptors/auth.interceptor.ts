import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Clonamos la petición y le añadimos withCredentials a TODAS
  const authReq = req.clone({
    withCredentials: true
  });
  return next(authReq);
};