import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

type ConfirmDialogData = {
    title: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
};

@Component({
    selector: 'app-confirm-dialog',
    imports: [MatDialogModule, MatButtonModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
        <h2 mat-dialog-title>{{ data.title }}</h2>

        <mat-dialog-content>
            <p>{{ data.message }}</p>
        </mat-dialog-content>

        <mat-dialog-actions align="end">
            <button mat-button (click)="close(false)">{{ data.cancelText || 'Cancelar' }}</button>
            <button mat-flat-button color="warn" (click)="close(true)">
                {{ data.confirmText || 'Eliminar' }}
            </button>
        </mat-dialog-actions>
    `
})
export class ConfirmDialog {
    private dialogRef = inject(MatDialogRef<ConfirmDialog, boolean>);
    public data = inject<ConfirmDialogData>(MAT_DIALOG_DATA);

    close(result: boolean) {
        this.dialogRef.close(result);
    }
}
