import { Component, input, output } from '@angular/core';
import { EmpleadoResponse as EmpleadoModel } from '../../../core/models/empleado.model';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-empleado-card',
  imports: [MatCardModule, MatButtonModule],
  template: `
      <mat-card>

        <mat-card-header>
          <mat-card-title>{{ empleado().nombre }} {{ empleado().apellido }}</mat-card-title>
          <mat-card-subtitle>{{ empleado().id }} | {{ empleado().cargo.nombre }}</mat-card-subtitle>
          <mat-card-subtitle>{{ empleado().departamento.nombre }}</mat-card-subtitle>
        </mat-card-header>

        <mat-card-content>
          <p [class]="getClaseSueldo()">Sueldo: S/ {{ empleado().sueldo }}</p>
        </mat-card-content>

        <mat-card-actions>
          <button mat-button color="primary" (click)="onDetalleClick()"> Detalles </button>
          <button mat-button (click)="editarEmpleado.emit(empleado())"> Editar </button>
          <button mat-button (click)="onEliminarClick()"> Eliminar </button>
        </mat-card-actions>

      </mat-card>
  `
})
export class EmpleadoCard {
  empleado = input.required<EmpleadoModel>();
  solicitarVacaciones = output<number>();
  eliminarEmpleado = output<number>();
  editarEmpleado = output<EmpleadoModel>();
  viewDetails = output<number>();

  onVacacionesClick() {
    this.solicitarVacaciones.emit(this.empleado().id);
  }

  onEliminarClick() {
    this.eliminarEmpleado.emit(this.empleado().id);
  }

  getClaseSueldo(): string {
    const sueldo = this.empleado().sueldo;
    if (sueldo > 3000) return 'sueldo-alto';
    if (sueldo < 1300) return 'sueldo-bajo';
    return 'sueldo-medio';
  }

  onDetalleClick() {
    this.viewDetails.emit(this.empleado().id);
  }
}
