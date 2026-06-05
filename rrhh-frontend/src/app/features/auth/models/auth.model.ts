export interface LoginRequest {
  email: string;
  password: string;
}

export interface MenuResponse {
  nombre: string;
  icono: string;
  url: string;
}

export interface UserProfileResponse {
  email: string;
  nombreCompleto: string;
  roles: string[];
  menus: MenuResponse[];
}

export interface EmailRequest {
  email: string;
}

export interface CambioPasswordRequest {
  token: string;
  password: string;
  confirmPassword: string;
}

export interface GenericResponse {
  message: string;
}