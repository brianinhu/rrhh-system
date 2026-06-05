import { Component, input, output } from '@angular/core';
import { EmpleadoResponse } from '../../../core/models/empleado.model';
import { EmpleadoCard } from './empleado-card';

@Component({
  selector: 'app-empleado-table',
  imports: [EmpleadoCard],
  template: `
      <div class="grid grid-cols-2 gap-4">

        @for (emp of empleados(); track emp.id) {
        <app-empleado-card [empleado]="emp" (eliminarEmpleado)="eliminarEmpleado.emit($event)"
            (editarEmpleado)="editarEmpleado.emit($event)" (viewDetails)="verDetalle.emit($event)"></app-empleado-card>
        }

        @if (empleados().length === 0) {
        <p>No hay empleados registrados.</p>
        }

      </div>
  `
})
export class EmpleadoTable {
  empleados = input.required<EmpleadoResponse[]>();

  editarEmpleado = output<EmpleadoResponse>();
  eliminarEmpleado = output<number>();
  verDetalle = output<number>();
}
