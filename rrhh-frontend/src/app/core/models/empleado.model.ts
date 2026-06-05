import { Cargo } from "./cargo.model";
import { Departamento } from "./departamento.model";
import { ProyectoResponse } from "./proyecto.model";

// Este modelo representa la estructura de un empleado tal como lo maneja el backend y lo que necesitamos en el frontend para mostrar la información básica en la tabla y manejar las relaciones con cargo, departamento y proyectos. Para detalles adicionales (como nivel de estudios, especialidad, etc.) usaremos el modelo EmpleadoDetalladoResponse.
export interface EmpleadoResponse {
  id: number;
  nombre: string;
  apellido: string;
  cargo: Cargo;
  sueldo: number;
  departamento: Departamento;
  proyectos?: ProyectoResponse[];
  curriculum?: {
    nivelEstudios: string;
    especialidad: string;
    urlArchivo?: string;
  };
}

// Este es el modelo que usaremos para enviar datos al backend al crear o editar un empleado
export interface EmpleadoRequest {
  nombre: string;
  apellido: string;
  sueldo: number;
  idCargo: number;
  idDepartamento: number;
  nivelEstudios: string;
  especialidad: string;
  idsProyectos: number[];
}