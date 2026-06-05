import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { ProyectoService } from '../../../../core/services/proyecto.service';
import { ProyectoResponse } from '../../../../core/models/proyecto.model';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ProyectoForm } from '../../components/form/proyecto-form';
import { ProyectoEquipo } from '../../components/equipo/proyecto-equipo';
import { switchMap, filter } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-proyecto-page',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatButtonModule, MatIconModule, MatCardModule, MatDialogModule],
  templateUrl: './proyecto.page.html'
})
export class ProyectoPage {
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  public proyectoService = inject(ProyectoService);

  public displayedColumns: string[] = ['id', 'nombre', 'descripcion', 'estado', 'acciones'];

  abrirModal(proyecto?: ProyectoResponse) {
    const dialogRef = this.dialog.open(ProyectoForm, {
      data: { proyecto },
      width: '500px'
    });

    dialogRef.afterClosed().pipe(
      filter(result => !!result), // Solo continuamos si el usuario hizo clic en "Guardar" y no en "Cancelar"
      switchMap(result => this.proyectoService.save(result, proyecto?.id)) // Guardamos el proyecto (nuevo o editado)
    ).subscribe({
      next: () => {
        this.snackBar.open(`Proyecto ${proyecto ? 'actualizado' : 'creado'} con éxito`, 'Cerrar', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'bottom'
        });
      },
      error: (err) => {
        console.error('Error al guardar el proyecto', err);
        this.snackBar.open('Hubo un error al guardar. Intenta nuevamente.', 'Cerrar', {
          duration: 3000,
          panelClass: ['error-snackbar']
        });
      }
    });
  }

  verEquipo(id: number) {
    this.proyectoService.getDetalle(id).subscribe({
      next: (detalle) => {
        this.dialog.open(ProyectoEquipo, {
          width: '400px',
          data: detalle
        });
      },
      error: (err) => {
        console.error('Error al obtener el equipo', err);
      }
    });
  }

  eliminar(id: number) {
    if (confirm('¿Estás seguro de eliminar este proyecto del sistema?')) {
      this.proyectoService.delete(id).subscribe({
        next: () => {
          alert('Proyecto eliminado con éxito');
        },
        error: (err) => {
          console.error('Error al eliminar el proyecto', err);
          alert('Hubo un error al eliminar el proyecto. Por favor, intenta nuevamente.');
        }
      });
    }
  }
}