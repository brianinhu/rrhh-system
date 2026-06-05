import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatIconModule } from '@angular/material/icon';

import { AuthService } from '../../data-access/auth.service';

@Component({
  selector: 'app-login-page',
  imports: [ReactiveFormsModule, CommonModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatProgressBarModule, MatIconModule],
  templateUrl: './login.page.html'
})
export class LoginPage {
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);

  isLoading = signal(false);
  message = signal('');
  isError = signal(false);
  showPassword = signal(false);

  loginForm = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  async login() {
    this.isLoading.set(true);
    this.message.set('Redirigiendo a Keycloak...');
    
    try {
      await this.authService.login();
    } catch (err) {
      console.error('Error al intentar redirigir a Keycloak', err);
      this.isLoading.set(false);
      this.isError.set(true);
      this.message.set('No se pudo conectar con el servidor de identidad.');
    }
  }

  togglePasswordVisibility() {
    this.showPassword.set(!this.showPassword());
  }

  get emailError(): string {
    const control = this.loginForm.get('email');
    if (control?.hasError('required')) return 'El correo es requerido';
    if (control?.hasError('email')) return 'Correo inválido';
    return '';
  }

  get passwordError(): string {
    const control = this.loginForm.get('password');
    if (control?.hasError('required')) return 'La contraseña es requerida';
    if (control?.hasError('minlength')) return 'La contraseña debe tener al menos 6 caracteres';
    return '';
  }

  alertConfig = computed(() => {
    return {
      class: this.isError() ? 'text-red-700' : 'text-green-700',
      icon: this.isError() ? 'error' : 'check_circle'
    };
  });
}
