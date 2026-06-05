import { inject, Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { UsuarioResponse, UsuarioRequest } from '../models/usuario.model';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private http = inject(HttpClient);
  private url = `${environment.HOST}/usuarios`;

  // Signal para manejar el estado de la lista de usuarios
  private _usuarios = signal<UsuarioResponse[]>([]);
  public usuarios = this._usuarios.asReadonly();

  constructor() {
    this.load();
  }

  // Carga la lista completa y actualiza el signal
  load() {
    return this.http.get<UsuarioResponse[]>(this.url).pipe(
      tap((response) => {
        console.log('Usuarios cargados:', response);
        this._usuarios.set(response);
      })
    ).subscribe();
  }

  // Obtener un usuario por ID
  getById(id: number): Observable<UsuarioResponse> {
    return this.http.get<UsuarioResponse>(`${this.url}/${id}`);
  }

  // Guardar (Crear o Editar)
  // Nota: Siguiendo tu patrón de ProyectoService para consistencia
  save(request: UsuarioRequest, id?: number): Observable<UsuarioResponse> {
    const isEdition = !!id;
    return isEdition
      ? this.http.put<UsuarioResponse>(`${this.url}/${id}`, request).pipe(tap(() => this.load()))
      : this.http.post<UsuarioResponse>(this.url, request).pipe(tap(() => this.load()));
  }

  // Cambio de estado (El PATCH que probamos en Postman)
  cambiarEstado(id: number, nuevoEstado: boolean): Observable<void> {
    const params = new HttpParams().set('nuevoEstado', nuevoEstado);
    
    return this.http.patch<void>(`${this.url}/${id}/estado`, null, { params }).pipe(
      tap(() => this.load()) // Recargamos para que la tabla refleje el cambio de 'enabled'
    );
  }

  // Eliminar usuario
  delete(id: number) {
    return this.http.delete(`${this.url}/${id}`).pipe(
      tap(() => this.load())
    );
  }
}