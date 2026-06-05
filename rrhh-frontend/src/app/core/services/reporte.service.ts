import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DataGrafico } from '../models/reporte.model';
import { environment } from '../../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ReporteService {

    private http = inject(HttpClient);
    private url = `${environment.HOST}/reportes`;

    getEmpleadosByDepartament() {
        return this.http.get<DataGrafico[]>(`${this.url}/empleados-por-departamento`);
    }

    getSalaryByJobTitle() {
        return this.http.get<DataGrafico[]>(`${this.url}/sueldos-por-cargo`);
    }
}