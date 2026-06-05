export interface ProyectoResponse {
    id: number;
    nombre: string;
    descripcion: string;
    estado: string;
}

export interface ProyectoRequest {
    nombre: string;
    descripcion: string;
    estado: string;
    idsEmpleados: number[];
}

// Para cuando queramos ver qué empleados tiene asignados
export interface ProyectoDetalleResponse {
    id: number;
    nombre: string;
    descripcion: string;
    estado: string;
    idsEmpleados: number[];
    nombresEmpleados: string[];
}