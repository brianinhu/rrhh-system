import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Cargo } from '../models/cargo.model';

@Injectable({
  providedIn: 'root'
})
export class CargoService {
  private http = inject(HttpClient);
  private url = `${environment.HOST}/cargos`;

  getCargos(): Observable<Cargo[]> {
    return this.http.get<Cargo[]>(this.url);
  }
}