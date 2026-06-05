import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ProyectoResponse, ProyectoRequest } from '../../../../core/models/proyecto.model';
import { EmpleadoService } from '../../../../core/services/empleado.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { ProyectoService } from '../../../../core/services/proyecto.service';

@Component({
    selector: 'app-proyecto-form',
    standalone: true,
    imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatIconModule],
    templateUrl: './proyecto-form.html'
})
export class ProyectoForm implements OnInit {
    private cdr = inject(ChangeDetectorRef);
    private dialogRef = inject(MatDialogRef<ProyectoForm>);
    private empleadoService = inject(EmpleadoService);
    private proyectoService = inject(ProyectoService);

    public data = inject<{ proyecto: ProyectoResponse | null }>(MAT_DIALOG_DATA);
    public empleados = toSignal(this.empleadoService.getAll(), { initialValue: [] });

    // Modelo para el formulario
    public proyectoRequest: ProyectoRequest = {
        nombre: '',
        descripcion: '',
        estado: 'PLANIFICACION',
        idsEmpleados: []
    };

    // Opciones para el select de estados
    public estados = ['PLANIFICACION', 'ACTIVO', 'PAUSADO', 'FINALIZADO'];

    ngOnInit(): void {
        if (this.data?.proyecto) {
            this.proyectoRequest = {
                nombre: this.data.proyecto.nombre,
                descripcion: this.data.proyecto.descripcion,
                estado: this.data.proyecto.estado,
                idsEmpleados: []
            };

            this.proyectoService.getDetalle(this.data.proyecto.id).subscribe(detalle => {
                this.proyectoRequest.idsEmpleados = detalle.idsEmpleados || [];
                this.cdr.detectChanges(); // Forzar la detección de cambios
            });
        }
    }

    guardar() {
        if (!this.proyectoRequest.nombre) return;
        this.dialogRef.close(this.proyectoRequest);
    }

    cancelar() {
        this.dialogRef.close(null);
    }
}