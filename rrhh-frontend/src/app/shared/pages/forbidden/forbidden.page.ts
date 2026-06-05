import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [RouterLink, MatButtonModule, MatIconModule],
  template: `
    <div class="flex flex-col items-center justify-center h-[80vh] text-center px-4">
      <div class="bg-red-50 p-6 rounded-full mb-6">
        <mat-icon class="text-red-600 !w-16 !h-16 text-6xl">lock_person</mat-icon>
      </div>
      
      <h1 class="text-4xl font-bold text-gray-900 mb-2">Acceso Restringido</h1>
      <p class="text-gray-500 max-w-md mb-8">
        Lo sentimos, no tienes los permisos necesarios para acceder a esta sección. 
        Si crees que esto es un error, contacta al administrador del sistema.
      </p>

      <button mat-flat-button color="primary" routerLink="/dashboard" class="!rounded-xl !py-6 !px-8">
        <mat-icon>home</mat-icon>
        Volver al Dashboard principal
      </button>
    </div>
  `
})
export class ForbiddenPage {}