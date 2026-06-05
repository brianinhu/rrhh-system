import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Departamento } from '../models/departamento.model';

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {
  private http = inject(HttpClient);
  private url = `${environment.HOST}/departamentos`;

  getDepartamentos(): Observable<Departamento[]> {
    return this.http.get<Departamento[]>(this.url);
  }
}