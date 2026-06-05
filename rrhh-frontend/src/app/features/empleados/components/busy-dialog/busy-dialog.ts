import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBar } from '@angular/material/progress-bar';

type BusyDialogData = {
    title: string;
    message: string;
};

@Component({
    selector: 'app-busy-dialog',
    standalone: true,
    imports: [MatDialogModule, MatProgressSpinnerModule, MatProgressBar],
    template: `
        <h2 mat-dialog-title>{{ data.title }}</h2>
        <mat-dialog-content>
            <mat-progress-bar mode="indeterminate"></mat-progress-bar>
            <p>{{ data.message }}</p>
        </mat-dialog-content>
    `
})
export class BusyDialog {
    public data = inject<BusyDialogData>(MAT_DIALOG_DATA);
}