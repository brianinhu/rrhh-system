import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { CambioPasswordRequest, EmailRequest, GenericResponse, MenuResponse, UserProfileResponse } from '../models/auth.model';
import { firstValueFrom, tap } from 'rxjs';

import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);
  private url = `${environment.HOST}/auth`;
  private keycloak = inject(Keycloak);

  private readonly _currentUser = signal<UserProfileResponse | null>(null);
  private readonly _userMenu = signal<MenuResponse[]>([]);
  private readonly _isReady = signal(false);
  private readonly _isAuthenticated = signal(false);

  readonly currentUser = this._currentUser.asReadonly();
  readonly userMenu = this._userMenu.asReadonly();
  readonly isReady = this._isReady.asReadonly();
  readonly isAuthenticated = this._isAuthenticated.asReadonly();

  async login() {
    await this.keycloak.login();
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  hasRole(role: string): boolean {
    const tokenParsed = this.keycloak.tokenParsed as any;
    const realmRoles: string[] = tokenParsed?.realm_access?.roles || [];
    const customRolesLower: string[] = tokenParsed?.['roles'] || [];
    const customRolesUpper: string[] = tokenParsed?.['ROLES'] || [];
    const roles = [...new Set([...realmRoles, ...customRolesLower, ...customRolesUpper])];

    console.log('🔍 AuthService viendo roles en el token:', roles);
    console.log('🔍 Comparando contra:', role);

    return roles.includes(role);
  }

  async initializeAuth(): Promise<void> {
    const authenticated = !!this.keycloak.authenticated;
    console.log('[AuthService] keycloak.authenticated:', this.keycloak.authenticated);
    console.log('[AuthService] Token presente:', !!this.keycloak.token);

    this._isAuthenticated.set(authenticated);

    if (authenticated) {
      try {
        await firstValueFrom(this.fetchUserProfile());
        console.log('[AuthService] Perfil y Menús sincronizados correctamente.');
      } catch (error) {
        console.error('[AuthService] Error en la sincronización inicial del perfil:', error);
      }
    } else {
      console.log('[AuthService] No autenticado — omitiendo carga de perfil.');
    }

    this._isReady.set(true);
    console.log('[AuthService] Inicializado. authenticated:', authenticated, '| isReady: true');
  }

  fetchUserProfile() {
    return this.http.get<UserProfileResponse>(`${this.url}/perfil`)
      .pipe(
        tap(perfil => {
          console.log('[AuthService] Perfil recibido del backend:', perfil);
          this._currentUser.set(perfil);
          this._userMenu.set(perfil.menus);
        })
      );
  }

  async logout() {
    this._currentUser.set(null);
    this._userMenu.set([]);
    this._isReady.set(false);
    this._isAuthenticated.set(false);
    await this.keycloak.logout({
      redirectUri: window.location.origin + '/login'
    });
  }

  enviarCorreoRecuperacion(correo: string) {
    const body: EmailRequest = { email: correo };
    return this.http.post<GenericResponse>(`${this.url}/enviar-recuperacion`, body);
  }

  verificarToken(token: string) {
    return this.http.get<GenericResponse>(`${this.url}/verificar-token/${token}`);
  }

  cambiarPassword(request: CambioPasswordRequest) {
    return this.http.post<GenericResponse>(`${this.url}/cambiar-password`, request);
  }
}
