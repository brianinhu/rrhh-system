import { inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ProyectoResponse, ProyectoRequest, ProyectoDetalleResponse } from '../models/proyecto.model';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {
  private http = inject(HttpClient);
  private url = `${environment.HOST}/proyectos`;

  private _proyectos = signal<ProyectoResponse[]>([]);
  public proyectos = this._proyectos.asReadonly();

  constructor() {
    this.load();
  }

  load() {
    return this.http.get<ProyectoResponse[]>(this.url).pipe(
      tap((response) => {
        this._proyectos.set(response);
      })
    ).subscribe();
  }

  getDetalle(id: number): Observable<ProyectoDetalleResponse> {
    return this.http.get<ProyectoDetalleResponse>(`${this.url}/${id}`);
  }

  save(request: ProyectoRequest, id?: number): Observable<ProyectoResponse> {
    const isEdition = !!id;
    return isEdition
      ? this.http.put<ProyectoResponse>(`${this.url}/${id}`, request).pipe(tap(() => this.load()))
      : this.http.post<ProyectoResponse>(this.url, request).pipe(tap(() => this.load()));
  }

  delete(id: number) {
    return this.http.delete(`${this.url}/${id}`).pipe(
      tap(() => this.load())
    );
  }
}