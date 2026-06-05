import { Component, DestroyRef, inject, OnInit, signal, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

// Servicios y Modelos
import { UsuarioService } from '../../../../../core/services/usuario.service';
import { EmpleadoService } from '../../../../../core/services/empleado.service';
import { UsuarioResponse, UsuarioRequest } from '../../../../../core/models/usuario.model';
import { EmpleadoResponse } from '../../../../../core/models/empleado.model';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule
  ],
  templateUrl: './usuario-form.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UsuarioForm implements OnInit {
  private fb = inject(FormBuilder);
  private dialogRef = inject(MatDialogRef<UsuarioForm>);
  private cdr = inject(ChangeDetectorRef);
  private destroyRef = inject(DestroyRef);
  private usuarioService = inject(UsuarioService);
  private empleadoService = inject(EmpleadoService);

  public data: UsuarioResponse | null = inject(MAT_DIALOG_DATA);
  public form: FormGroup | null = null;
  public listaEmpleados = signal<EmpleadoResponse[]>([]);

  public rolesDisponibles = [
    { id: 1, nombre: 'ADMIN' },
    { id: 2, nombre: 'EXECUTIVE' },
    { id: 3, nombre: 'HR_BP' },
    { id: 4, nombre: 'USER' }
  ];

  ngOnInit(): void {
    this.cargarEmpleados();
  }

  save(): void {
    if (!this.form || this.form.invalid) return;

    const request: UsuarioRequest = {
      email: this.form.value.email,
      password: this.form.value.password || undefined,
      idEmpleado: this.form.getRawValue().idEmpleado,
      idsRoles: this.form.value.idsRoles
    };

    this.usuarioService.save(request, this.data?.id).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => console.error('Error al guardar usuario', err)
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  // --- Helpers Methods ---
  private cargarEmpleados(): void {
    this.empleadoService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (emps) => {
          this.listaEmpleados.set(emps);
          this.cdr.markForCheck();
          if (this.data) {
            this.cargarUsuarioCompleto();
          } else {
            this.initForm();
          }
        },
        error: (err) => console.error('Error al cargar empleados', err)
      });
  }

  private cargarUsuarioCompleto(): void {
    if (!this.data?.id) return;

    this.usuarioService.getById(this.data.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (usuarioCompleto) => {
          this.data = usuarioCompleto;
          this.initForm();
          this.cdr.markForCheck();
        },
        error: (err) => console.error('Error al cargar usuario', err)
      });
  }

  private initForm(): void {
    this.form = this.fb.group({
      idEmpleado: [null, this.data ? [] : [Validators.required]],
      email: [this.data?.email || '', [Validators.required, Validators.email]],
      password: ['', this.data ? [] : [Validators.required, Validators.minLength(6)]],
      idsRoles: [[], [Validators.required]]
    });

    if (!this.form) return;

    this.form.get('idEmpleado')?.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((idEmpleado) => {
        if (!idEmpleado || !this.form) return;

        const empleado = this.listaEmpleados().find(e => e.id === idEmpleado);
        const nombreCompleto = `${empleado?.nombre} ${empleado?.apellido}`;
        const existe = this.usuarioService.usuarios().some(u => u.nombreCompleto === nombreCompleto);

        const control = this.form!.get('idEmpleado');
        if (existe) {
          control?.setErrors({ empleadoDuplicado: true });
        } else {
          control?.updateValueAndValidity({ emitEvent: false });
        }
      });

    if (this.data) {
      const idEmpleadoAsignado = this.obtenerIdEmpleadoAsignado();
      this.form.patchValue({
        idEmpleado: idEmpleadoAsignado
      });
      this.form.get('idEmpleado')?.disable();
    }
  }

  private obtenerIdEmpleadoAsignado(): number | null {
    if (!this.data) return null;

    if (this.data.idEmpleado) {
      return this.data.idEmpleado;
    }

    const nombreObjetivo = this.normalizarTexto(this.data.nombreCompleto);
    const empleado = this.listaEmpleados().find((item) => {
      const nombreEmpleado = this.normalizarTexto(`${item.nombre} ${item.apellido}`);
      return nombreEmpleado === nombreObjetivo;
    });

    return empleado?.id ?? null;
  }

  private normalizarTexto(texto: string): string {
    return texto.trim().replace(/\s+/g, ' ').toLowerCase();
  }
}