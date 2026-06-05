import { Component, inject } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatCardModule } from '@angular/material/card';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { UsuarioService } from '../../../../../core/services/usuario.service';
import { UsuarioResponse } from '../../../../../core/models/usuario.model';
import { UsuarioForm } from '../form/usuario-form';

@Component({
  selector: 'app-usuario-list',
  standalone: true,
  imports: [
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatCardModule,
    MatDialogModule
  ],
  templateUrl: './usuario-list.html'
})
export class UsuarioList {
  private usuarioService = inject(UsuarioService);
  private dialog = inject(MatDialog);

  // Leemos el Signal del servicio (Estado reactivo)
  public usuarios = this.usuarioService.usuarios;
  // Definición de columnas para el mat-table
  public displayedColumns: string[] = ['id', 'email', 'nombre', 'roles', 'estado', 'acciones'];

  toggleEstado(usuario: UsuarioResponse) {
    const nuevoEstado = !usuario.enabled;
    this.usuarioService.cambiarEstado(usuario.id, nuevoEstado).subscribe();
  }

  editar(usuario: UsuarioResponse) {
    this.dialog.open(UsuarioForm, {
      width: '500px',
      disableClose: true,
      data: usuario
    });
  }
}