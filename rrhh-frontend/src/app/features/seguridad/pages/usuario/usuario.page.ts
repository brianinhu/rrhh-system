import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';

import { UsuarioList } from '../../components/usuario/list/usuario-list';
import { UsuarioForm } from '../../components/usuario/form/usuario-form';

@Component({
  selector: 'app-usuario-page',
  standalone: true,
  imports: [
    CommonModule, 
    MatButtonModule, 
    MatIconModule,
    MatDialogModule, 
    UsuarioList
  ],
  templateUrl: './usuario.page.html',
})
export class UsuarioPage {
  private dialog = inject(MatDialog);

  nuevoUsuario() {
    this.dialog.open(UsuarioForm, {
      width: '500px',
      disableClose: true
    });
  }
}