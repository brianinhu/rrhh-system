import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable, tap } from 'rxjs';
import { EmpleadoResponse, EmpleadoRequest } from '../models/empleado.model';
import { Page } from '../models/page.model';
import { environment } from '../../../environments/environment';
import { EmpleadoDetalladoResponse } from '../models/empleado-detallado.model';

@Injectable({
    providedIn: 'root'
})
export class EmpleadoService {
    private http = inject(HttpClient);
    private url = `${environment.HOST}/empleados`;

    private _empleados = signal<EmpleadoResponse[]>([]);
    private _totalElements = signal<number>(0);


    public empleados = this._empleados.asReadonly();
    public totalElements = this._totalElements.asReadonly();

    constructor() {
        this.load();
    }

    getAll(): Observable<EmpleadoResponse[]> {
        return this.http.get<any>(this.url).pipe(
            map(response => response.content)
        );
    }

    getDetalle(id: number): Observable<EmpleadoDetalladoResponse> {
        return this.http.get<EmpleadoDetalladoResponse>(`${this.url}/${id}`);
    }

    getTotalNomina(): Observable<number> {
        return this.http.get<number>(`${this.url}/nomina-total`);
    }

    getTotalEmpleados(): Observable<number> {
        return this.http.get<number>(`${this.url}/total-count`);
    }

    load(page: number = 0, size: number = 4, term: string = '') {
        const url = term
            ? `${this.url}?page=${page}&size=${size}&term=${term}`
            : `${this.url}?page=${page}&size=${size}`;

        return this.http.get<Page<EmpleadoResponse>>(url).pipe(
            tap((response: Page<EmpleadoResponse>) => {
                this._totalElements.set(response.totalElements);
                this._empleados.set(response.content);
            })
        ).subscribe();
    }

    // El componente que invoca save() decide cuándo refrescar la lista para evitar
    // inconsistencias de paginación (por ejemplo, size=4 en la vista).
    save(request: EmpleadoRequest, file?: File, id?: number): Observable<EmpleadoResponse> {
        const isEdition = !!id;

        const formData = new FormData();
        formData.append('empleado', new Blob([JSON.stringify(request)], { type: 'application/json' }));

        if (file) {
            formData.append('curriculum', file);
        }

        return isEdition
            ? this.http.put<EmpleadoResponse>(`${this.url}/${id}`, formData)
            : this.http.post<EmpleadoResponse>(this.url, formData);
    }

    delete(id: number) {
        return this.http.delete(`${this.url}/${id}`);
    }

    asignarProyecto(idEmpleado: number, idProyecto: number): Observable<void> {
        return this.http.put<void>(`${this.url}/${idEmpleado}/proyectos/${idProyecto}`, {});
    }
}
