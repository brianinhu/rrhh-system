import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { ProyectoDetalleResponse } from '../../../../core/models/proyecto.model';

@Component({
  selector: 'app-proyecto-equipo',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  templateUrl: './proyecto-equipo.html'
})
export class ProyectoEquipo {
  // Inyectamos la data que nos pasará el componente padre
  public data = inject<ProyectoDetalleResponse>(MAT_DIALOG_DATA);
}