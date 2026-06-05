import { inject } from '@angular/core';
import { CanActivateChildFn, CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../features/auth/data-access/auth.service';

export const authGuard: CanActivateFn & CanActivateChildFn = async (route): Promise<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('[AuthGuard] Ejecutando para ruta:', route.routeConfig?.path, '| data:', route.data);

  // Esperar a que AuthService esté listo (polling, sin toObservable)
  if (!authService.isReady()) {
    console.log('[AuthGuard] Esperando a que el servicio se sincronice...');
    await waitForReady(authService);
  }

  console.log('[AuthGuard] isReady:', authService.isReady(), 'isAuthenticated:', authService.isAuthenticated());

  if (!authService.isAuthenticated()) {
    console.warn('[AuthGuard] Acceso denegado: Sesión no encontrada. Redirigiendo al login de Keycloak.');
    await authService.login(); // Redirige al login de Keycloak
    return false; // No es necesario redirigir manualmente, Keycloak se encargará de eso
  }

  const allowedRoles = route.data['roles'] as string[];
  if (!allowedRoles || allowedRoles.length === 0) {
    return true;
  }

  const hasRequiredRole = allowedRoles.some(role => authService.hasRole(role));
  console.log('[AuthGuard] Ruta:', route.routeConfig?.path, '| Roles requeridos:', allowedRoles, '| Acceso:', hasRequiredRole ? 'CONCEDIDO' : 'DENEGADO');

  if (hasRequiredRole) {
    return true;
  }

  console.warn('[AuthGuard] Redirigiendo a forbidden. Roles del usuario:', allowedRoles);
  console.warn('[AuthGuard] hasRole ADMIN:', authService.hasRole('ADMIN'));
  console.warn('[AuthGuard] tokenParsed:', (authService as any).keycloak?.tokenParsed);

  return router.parseUrl('/forbidden');
};

function waitForReady(authService: AuthService): Promise<void> {
  return new Promise<void>(resolve => {
    if (authService.isReady()) {
      resolve();
      return;
    }
    const id = setInterval(() => {
      if (authService.isReady()) {
        clearInterval(id);
        resolve();
      }
    }, 50);
  });
}
