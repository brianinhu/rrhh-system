import { ApplicationConfig, inject, provideAppInitializer, provideZonelessChangeDetection, effect } from '@angular/core';
import { provideRouter, withViewTransitions } from '@angular/router';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { HttpHandlerFn, HttpInterceptorFn, HttpRequest, provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { environment } from '../environments/environment'

import {
  provideKeycloak,
  withAutoRefreshToken,
  AutoRefreshTokenService,
  UserActivityService,
  KEYCLOAK_EVENT_SIGNAL,
  KeycloakEventType
} from 'keycloak-angular';

import { AuthService } from './features/auth/data-access/auth.service';

const rrhhInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next: HttpHandlerFn) => {
  const authService = inject(AuthService);

  if (req.url.includes(environment.HOST) && authService.isAuthenticated()) {
    const token = authService.getToken();

    if (token) {
      const authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      return next(authReq);
    }
  }

  return next(req);
};

export const appConfig: ApplicationConfig = {
  providers: [
    provideZonelessChangeDetection(),
    provideRouter(routes, withViewTransitions()),
    provideAnimationsAsync(),

    provideKeycloak({
      config: {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      },
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false,
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html'
      },
      features: [
        withAutoRefreshToken(), // Mantiene la sesión viva automáticamente
      ]
    }),

    provideAppInitializer(() => new Promise<void>(resolve => {
      const eventSignal = inject(KEYCLOAK_EVENT_SIGNAL);
      const authService = inject(AuthService);

      effect(async () => {
        const event = eventSignal();
        if (
          event.type === KeycloakEventType.Ready ||
          event.type === KeycloakEventType.AuthError
        ) {
          await authService.initializeAuth();
          resolve();
        }
      });
    })),

    AutoRefreshTokenService,
    UserActivityService,

    provideHttpClient(
      withFetch(),
      withInterceptors([rrhhInterceptor])
    ),
  ]
};
