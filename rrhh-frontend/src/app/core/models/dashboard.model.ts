export interface ChartData {
  label: string;
  value: number;
}

export interface EmpleadoDashboard {
  nombreCompleto: string;
  departamento: string;
  cargo: string;
  sueldo: number;
  fechaIngreso: string;
}

export interface DashboardResponse {
  totalEmpleadosActivos: number;
  distribucionDepartamento: ChartData[];
  distribucionCargo: ChartData[];
  topSueldos: EmpleadoDashboard[];
  proyectosStatus: ChartData[];
  ultimosEmpleados: EmpleadoDashboard[];
}