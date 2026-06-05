import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../features/auth/data-access/auth.service';

export const publicGuard: CanActivateFn = async (): Promise<boolean | UrlTree> => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Esperar a que AuthService esté listo (polling, sin toObservable)
  if (!authService.isReady()) {
    console.log('[PublicGuard] Esperando sincronización inicial...');
    await waitForReady(authService);
  }

  console.log('[PublicGuard] isReady:', authService.isReady(), 'isAuthenticated:', authService.isAuthenticated());

  if (authService.isAuthenticated()) {
    console.log('[PublicGuard] Ya autenticado, redirigiendo a /dashboard');
    return router.parseUrl('/dashboard');
  }

  return true;
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
