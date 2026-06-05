import { Component, OnInit, signal, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { DatePipe, DecimalPipe } from '@angular/common';
import { EmpleadoService } from '../../../../core/services/empleado.service';
import { EmpleadoDetalladoResponse } from '../../../../core/models/empleado-detallado.model';

@Component({
  selector: 'app-empleado-detalle',
  standalone: true,
  imports: [
    MatDialogModule, MatButtonModule, MatIconModule, MatSnackBarModule,
    MatProgressSpinnerModule, DatePipe, DecimalPipe, MatChipsModule
  ],
  templateUrl: './empleado-detalle.html'
})
export class EmpleadoDetalle implements OnInit {
  private empleadoService = inject(EmpleadoService);
  private snackBar = inject(MatSnackBar);

  public data = inject<{ id: number }>(MAT_DIALOG_DATA);

  empleado = signal<EmpleadoDetalladoResponse | null>(null);

  ngOnInit(): void {
    this.empleadoService.getDetalle(this.data.id).subscribe({
      next: (res) => this.empleado.set(res),
      error: () => this.snackBar.open('Error al cargar detalle', 'Cerrar', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom'
      })
    });
  }

  verPdf() {
    const url = this.empleado()?.urlCurriculum;

    if (url) {
      window.open(url, '_blank');
    } else {
      this.snackBar.open('No hay currículum disponible para este empleado.', 'Cerrar', {
        duration: 3000,
        horizontalPosition: 'center',
        verticalPosition: 'bottom',
      });
    }
  }
}