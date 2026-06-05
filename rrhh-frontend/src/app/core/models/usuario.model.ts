export interface UsuarioResponse {
  id: number;
  email: string;
  enabled: boolean;
  nombreCompleto: string;
  idEmpleado?: number;
  nombresRoles: string[];
}

export interface UsuarioRequest {
  email: string;
  password?: string; 
  idEmpleado: number;
  idsRoles: number[];
}