import { ChangeDetectionStrategy, Component, computed, effect, inject, signal, untracked } from "@angular/core";
import { CommonModule } from "@angular/common";
import { HttpErrorResponse } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { finalize } from 'rxjs';

import { EmpleadoRequest, EmpleadoResponse } from "../../../../core/models/empleado.model";
import { EmpleadoDetalladoResponse } from "../../../../core/models/empleado-detallado.model";
import { EmpleadoService } from "../../../../core/services/empleado.service";

import { EmpleadoTable } from '../../ui/empleado-table';
import { EmpleadoForm } from '../../components/form/empleado-form';
import { BusyDialog } from '../../components/busy-dialog/busy-dialog';

import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule, MatLabel } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatDialog, MatDialogModule, MatDialogRef } from "@angular/material/dialog";
import { MatIconModule } from "@angular/material/icon";
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatGridListModule } from '@angular/material/grid-list';

import { EmpleadoDetalle } from "../../components/detalle/empleado-detalle";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatCardModule } from "@angular/material/card";
import { MatListModule } from "@angular/material/list";
import { ConfirmDialog } from "../../components/confirm-dialog/confirm-dialog";
import { MatChipsModule } from "@angular/material/chips";

type ApiErrorPayload = {
    detail?: string;
    message?: string;
};

@Component({
    selector: 'app-empleado-page',
    imports: [CommonModule, EmpleadoTable, FormsModule, MatFormFieldModule, MatLabel, MatInputModule, MatPaginatorModule, MatButtonModule, MatDialogModule, MatIconModule, MatSnackBarModule, MatProgressSpinnerModule, MatCardModule, MatGridListModule, MatListModule, MatChipsModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './empleado.page.html'
})
export class EmpleadoPage {

    private empleadoService = inject(EmpleadoService);
    private dialog = inject(MatDialog);
    private snackBar = inject(MatSnackBar);
    private busyDialogRef: MatDialogRef<BusyDialog> | null = null;

    // Signals de datos
    empleados = this.empleadoService.empleados;
    totalElements = this.empleadoService.totalElements;
    totalNomina = signal<number>(0);
    totalEmpleados = signal<number>(0);

    // Signals de estado
    searchTerm = signal('');
    pageSize = signal(4);
    currentPage = signal(0);
    isSaving = signal(false);
    isLoadingDetail = signal(false);
    isBusy = computed(() => this.isSaving() || this.isLoadingDetail());
    busyTitle = computed(() => this.isSaving() ? 'Guardando...' : 'Cargando detalle...');
    busyMessage = computed(() => this.isSaving() ? 'Guardando cambios' : 'Cargando datos del empleado');

    constructor() {
        effect(() => {
            const term = this.searchTerm();
            untracked(() => {
                this.currentPage.set(0);
                this.loadEmpleados(term);
            });
        });

        effect(() => {
            if (this.isBusy()) {
                this.openBusyDialog();
            } else {
                this.closeBusyDialog();
            }
        });

        this.updateNomina();
        this.updateTotalEmpleados();
    }

    loadEmpleados(term: string = this.searchTerm()) {
        this.empleadoService.load(
            this.currentPage(),
            this.pageSize(),
            term);
    }

    updateNomina() {
        this.empleadoService.getTotalNomina().subscribe({
            next: (total) => this.totalNomina.set(total),
            error: (err: HttpErrorResponse) => {
                console.error('Error al cargar el total de la nómina:', err);
                this.totalNomina.set(0);
            }
        });
    }

    updateTotalEmpleados() {
        this.empleadoService.getTotalEmpleados().subscribe({
            next: (total) => this.totalEmpleados.set(total),
            error: (err: HttpErrorResponse) => {
                console.error('Error al cargar el total de empleados:', err);
                this.totalEmpleados.set(0);
            }
        });
    }

    viewDetails(id: number) {
        this.dialog.open(EmpleadoDetalle, {
            data: { id },
            width: '600px',
            maxWidth: '95vw', // Para que sea responsivo en móviles
            autoFocus: false
        });
    }

    onPageChange(event: PageEvent) {
        this.currentPage.set(event.pageIndex);
        this.pageSize.set(event.pageSize);
        this.loadEmpleados();
    }

    openDialog(empleado: EmpleadoResponse | EmpleadoDetalladoResponse | null = null): void {
        const dialogRef = this.dialog.open(EmpleadoForm, {
            width: '500px',
            data: { empleado },
            autoFocus: true
        });

        dialogRef.afterClosed().subscribe((result: { request: EmpleadoRequest; file: File | null } | null) => {
            if (result) {
                const id = empleado?.id && empleado.id > 0 ? empleado.id : undefined;
                this.save(result.request, result.file, id);
            }
        });
    }

    // --- Métodos de acción --- //

    save(request: EmpleadoRequest, archivo?: File | null, id?: number) {
        this.isSaving.set(true);
        this.empleadoService.save(request, archivo || undefined, id).pipe(
            finalize(() => this.isSaving.set(false))
        ).subscribe({
            next: () => {
                this.snackBar.open(`Empleado ${id ? 'actualizado' : 'creado'} con éxito`, 'Cerrar', {
                    duration: 3000,
                    horizontalPosition: 'center',
                    verticalPosition: 'bottom'
                });
                this.refreshSummary();
            },
            error: (err: HttpErrorResponse) => {
                console.error('Error en la operación:', err);
                this.snackBar.open(this.getErrorMessage(err, 'Error al procesar el registro'), 'Cerrar', {
                    duration: 5000,
                    horizontalPosition: 'center',
                    verticalPosition: 'bottom'
                });
            }
        });
    }

    edit(empleado: EmpleadoResponse) {
        this.isLoadingDetail.set(true);

        this.empleadoService.getDetalle(empleado.id).pipe(
            finalize(() => this.isLoadingDetail.set(false))
        ).subscribe({
            next: (empleadoDetalle) => {
                this.openDialog(empleadoDetalle);
            },
            error: (err: HttpErrorResponse) => {
                console.error('Error al cargar el detalle del empleado:', err);
                this.snackBar.open('Error al cargar los detalles del empleado', 'Cerrar', {
                    duration: 3000,
                    horizontalPosition: 'center',
                    verticalPosition: 'bottom'
                });
            }
        });
    }

    delete(id: number) {
        const dialogRef = this.dialog.open(ConfirmDialog, {
            width: '360px',
            autoFocus: false,
            data: {
                title: 'Eliminar empleado',
                message: '¿Estás seguro de que deseas eliminar este empleado?',
                confirmText: 'Eliminar',
                cancelText: 'Cancelar'
            }
        });

        dialogRef.afterClosed().subscribe((confirmed: boolean | undefined) => {
            if (!confirmed) {
                return;
            }

            this.empleadoService.delete(id).subscribe({
                next: () => {
                    this.snackBar.open('Empleado eliminado con éxito', 'Cerrar', {
                        duration: 3000,
                        horizontalPosition: 'center',
                        verticalPosition: 'bottom'
                    });
                    this.refreshSummary();
                },
                error: (err: HttpErrorResponse) => {
                    console.error('Error al eliminar el empleado:', err);
                    this.snackBar.open('Error al eliminar el empleado', 'Cerrar', {
                        duration: 3000,
                        horizontalPosition: 'center',
                        verticalPosition: 'bottom'
                    });
                }
            });
        });
    }

    // --- Métodos auxiliares --- //

    private refreshSummary() {
        this.loadEmpleados();
        this.updateNomina();
        this.updateTotalEmpleados();
    }

    private getErrorMessage(error: HttpErrorResponse, fallback: string): string {
        const payload = error.error as ApiErrorPayload | null;
        const detail = payload?.detail || payload?.message;
        return detail ? `${fallback}: ${detail}` : fallback;
    }

    private openBusyDialog() {
        if (this.busyDialogRef) {
            return;
        }

        this.busyDialogRef = this.dialog.open(BusyDialog, {
            data: {
                title: this.busyTitle(),
                message: this.busyMessage()
            },
            width: '420px',
            disableClose: true,
            autoFocus: false
        });

        this.busyDialogRef.afterClosed().subscribe(() => {
            this.busyDialogRef = null;
        });
    }

    private closeBusyDialog() {
        this.busyDialogRef?.close();
        this.busyDialogRef = null;
    }
}