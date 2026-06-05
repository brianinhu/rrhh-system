export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // Número de página actual (empieza en 0)
  first: boolean;
  last: boolean;
  empty: boolean;
}