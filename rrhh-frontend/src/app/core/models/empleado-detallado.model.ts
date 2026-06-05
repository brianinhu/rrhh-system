export interface Link {
  href: string;
}

export interface HateoasLinks {
  self: Link;
  [key: string]: Link; // Permite otros links como 'lista-completa'
}

export interface EmpleadoDetalladoResponse {
  id: number;
  nombre: string;
  apellido: string;
  sueldo: number;
  cargoNombre: string;
  cargoDescripcion: string;
  departamentoNombre: string;
  departamentoDescripcion: string;
  idCargo: number;
  idDepartamento: number;
  idsProyectos: number[];
  urlCurriculum: string;
  nivelEstudios: string;
  especialidad: string;
  fechaRegistro: string;
  nombresProyectos: string[];
  _links: HateoasLinks;
}