import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { DashboardService } from '../../../../core/services/dashboard.service';
import { ChartData, DashboardResponse } from '../../../../core/models/dashboard.model';
import { MatProgressSpinner, MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatListModule } from "@angular/material/list";
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from "@angular/material/toolbar";

@Component({
  selector: 'app-dashboard-page',
  standalone: true,
  imports: [CommonModule, NgxChartsModule, MatCardModule, MatIconModule, MatProgressSpinner, MatGridListModule, MatListModule, MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, MatGridListModule, MatDividerModule, MatProgressSpinnerModule, MatToolbarModule],
  templateUrl: './dashboard.page.html'
})
export class DashboardPage implements OnInit {

  private dashboardService = inject(DashboardService);

  data = signal<DashboardResponse | null>(null);

  colorScheme: any = { domain: ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'] };

  ngOnInit() {
    this.dashboardService.getData().subscribe(res => {
      this.data.set(res);
      console.log(this.data());
    });
  }

  mapToChart(items: ChartData[] | undefined) {
    return (items || []).map(item => ({ name: item.label, value: item.value }));
  }

  totalProyectosActivos(proyectos: ChartData[]): number {
    return proyectos
      .filter(p => p.label === 'ACTIVO' || p.label === 'PLANIFICACION')
      .reduce((acc, curr) => acc + curr.value, 0);
  }
}