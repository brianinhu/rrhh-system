import { ChangeDetectionStrategy, Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { EmpleadoResponse, EmpleadoRequest } from '../../../../core/models/empleado.model';
import { EmpleadoDetalladoResponse } from '../../../../core/models/empleado-detallado.model';
import { Cargo } from '../../../../core/models/cargo.model';
import { Departamento } from '../../../../core/models/departamento.model';
import { ProyectoResponse } from '../../../../core/models/proyecto.model';

import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';

import { CargoService } from '../../../../core/services/cargo.service';
import { DepartamentoService } from '../../../../core/services/departamento.service';
import { ProyectoService } from '../../../../core/services/proyecto.service';

type EmpleadoFormDialogData = {
  empleado: EmpleadoResponse | EmpleadoDetalladoResponse | null;
};

type EmpleadoFormCloseResult = {
  request: EmpleadoRequest;
  file: File | null;
};

type EntityWithId = {
  id: number;
};

@Component({
  selector: 'app-empleado-form',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatCardModule, MatSelectModule, MatDialogModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './empleado-form.html'
})
export class EmpleadoForm implements OnInit {

  private cargoService = inject(CargoService);
  private departamentoService = inject(DepartamentoService);
  public proyectoService = inject(ProyectoService);
  private dialogRef = inject(MatDialogRef<EmpleadoForm, EmpleadoFormCloseResult | null>);

  public data = inject<EmpleadoFormDialogData>(MAT_DIALOG_DATA);

  cargos: Cargo[] = [];
  departamentos: Departamento[] = [];
  idsProyectosSeleccionados: number[] = [];
  archivoSeleccionado: File | null = null;
  nombreArchivo: string = '';

  nivelEstudios: string = '';
  especialidad: string = '';

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement | null;
    const file = input?.files?.[0];
    if (file) {
      this.archivoSeleccionado = file;
      this.nombreArchivo = file.name;
    }
  }

  nuevoEmpleado: EmpleadoResponse = {
    id: 0,
    nombre: '',
    apellido: '',
    cargo: { id: 0, nombre: '', descripcion: '' },
    sueldo: 0,
    departamento: { id: 0, nombre: '', descripcion: '' },
    proyectos: []
  };

  ngOnInit(): void {
    if (this.data?.empleado) {
      const empleado = this.data.empleado;

      if (this.isEmpleadoDetallado(empleado)) {
        this.nuevoEmpleado = {
          id: empleado.id,
          nombre: empleado.nombre,
          apellido: empleado.apellido,
          sueldo: empleado.sueldo,
          cargo: {
            id: empleado.idCargo,
            nombre: empleado.cargoNombre,
            descripcion: empleado.cargoDescripcion
          },
          departamento: {
            id: empleado.idDepartamento,
            nombre: empleado.departamentoNombre,
            descripcion: empleado.departamentoDescripcion
          },
          proyectos: []
        };
        this.nivelEstudios = empleado.nivelEstudios || '';
        this.especialidad = empleado.especialidad || '';
        this.nombreArchivo = empleado.urlCurriculum ? 'Currículum cargado en S3' : '';
        this.idsProyectosSeleccionados = empleado.idsProyectos || [];
      } else {
        this.nuevoEmpleado = empleado;
        this.nivelEstudios = empleado.curriculum?.nivelEstudios || '';
        this.especialidad = empleado.curriculum?.especialidad || '';
        this.nombreArchivo = empleado.curriculum?.urlArchivo ? 'Currículum cargado en S3' : '';
        this.idsProyectosSeleccionados = (empleado.proyectos || []).map((p: ProyectoResponse) => p.id);
      }
    }

    this.cargoService.getCargos().subscribe(data => this.cargos = data);
    this.departamentoService.getDepartamentos().subscribe(data => this.departamentos = data);
  }

  compareFn(obj1: EntityWithId | null, obj2: EntityWithId | null): boolean {
    return obj1 && obj2 ? obj1.id === obj2.id : obj1 === obj2;
  }

  agregarEmpleado() {
    if (this.nuevoEmpleado.nombre.length === 0) return;

    const request: EmpleadoRequest = {
      nombre: this.nuevoEmpleado.nombre,
      apellido: this.nuevoEmpleado.apellido,
      idCargo: this.nuevoEmpleado.cargo.id,
      sueldo: this.nuevoEmpleado.sueldo,
      idDepartamento: this.nuevoEmpleado.departamento.id,
      idsProyectos: this.idsProyectosSeleccionados,
      nivelEstudios: this.nivelEstudios,
      especialidad: this.especialidad
    };

    this.dialogRef.close({ request: request, file: this.archivoSeleccionado });
  }

  cancelar() {
    this.dialogRef.close(null);
  }

  private isEmpleadoDetallado(
    empleado: EmpleadoResponse | EmpleadoDetalladoResponse
  ): empleado is EmpleadoDetalladoResponse {
    return 'idCargo' in empleado && 'idDepartamento' in empleado;
  }
}
