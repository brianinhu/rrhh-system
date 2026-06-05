import { Component, inject, OnInit, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../data-access/auth.service';

import { CambioPasswordRequest } from '../../models/auth.model';

type ApiErrorPayload = {
  detail?: string;
  message?: string;
};

@Component({
  selector: 'app-restablecer-page',
  imports: [ReactiveFormsModule, CommonModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatIconModule, MatProgressBarModule, MatProgressSpinnerModule],
  templateUrl: './restablecer.page.html'
})
export class RestablecerPage implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);

  isLoading = signal(false);
  message = signal('');
  isError = signal(false);

  token: string = '';
  tokenExpired = signal(false);
  isValidatingToken = signal(true);

  hidePassword = signal(true);
  hideConfirmPassword = signal(true);

  restablecerForm = this.fb.nonNullable.group({
    password: ['', [Validators.required, Validators.minLength(3)]],
    confirmPassword: ['', Validators.required]
  });

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.token = params['token'];
      if (this.token) {
        this.validarTokenAlInicio();
      } else {
        this.setTokenValidationState(false);
      }
    });
  }

  validarTokenAlInicio() {
    this.authService.verificarToken(this.token).subscribe({
      next: () => this.setTokenValidationState(true),
      error: () => this.setTokenValidationState(false)
    });
  }

  private setTokenValidationState(valid: boolean) {
    this.isValidatingToken.set(false);
    this.tokenExpired.set(!valid);
    if (valid) {
      this.restablecerForm.enable();
    } else {
      this.restablecerForm.disable();
    }
  }

  cambiar() {
    if (this.restablecerForm.invalid || !this.token) return;

    if (this.restablecerForm.getRawValue().password !== this.restablecerForm.getRawValue().confirmPassword) {
      this.message.set('Las contraseñas no coinciden.');
      this.isError.set(true);
      return;
    }

    this.isLoading.set(true);
    this.message.set('');
    this.isError.set(false);

    this.tokenExpired.set(false);

    const request: CambioPasswordRequest = {
      token: this.token,
      password: this.restablecerForm.getRawValue().password,
      confirmPassword: this.restablecerForm.getRawValue().confirmPassword
    };

    this.authService.cambiarPassword(request).subscribe({
      next: (response) => {
        this.isLoading.set(false);
        this.message.set(response.message);
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 5000);
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.message.set(this.getErrorMessage(error, 'No se pudo restablecer la contraseña'));
        this.isError.set(true);
      }
    });
  }

  private getErrorMessage(error: HttpErrorResponse, fallback: string): string {
    const payload = error.error as ApiErrorPayload | null;
    return payload?.detail || payload?.message || fallback;
  }
}
