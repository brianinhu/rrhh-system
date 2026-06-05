import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { publicGuard } from './core/guards/public.guard';

export const routes: Routes = [

    { path: 'recuperar-password', canActivate: [publicGuard], loadComponent: () => import('./features/auth/pages/recuperar/recuperar.page').then(m => m.RecuperarPage) },
    { path: 'restablecer-password/:token', canActivate: [publicGuard], loadComponent: () => import('./features/auth/pages/restablecer/restablecer.page').then(m => m.RestablecerPage) },

    {
        path: '',
        loadComponent: () => import('./shared/components/nav/nav').then(m => m.Nav),
        canActivate: [authGuard],
        canActivateChild: [authGuard],
        children: [
            {
                path: 'dashboard',
                loadComponent: () => import('./features/dashboard/pages/dashboard/dashboard.page').then(m => m.DashboardPage)
            },
            {
                path: 'empleados',
                data: { roles: ['ADMIN', 'HR_BP'] },
                loadComponent: () => import('./features/empleados/pages/empleado/empleado.page').then(m => m.EmpleadoPage)
            },
            {
                path: 'proyectos',
                data: { roles: ['ADMIN', 'EXECUTIVE', 'HR_BP'] },
                loadComponent: () => import('./features/proyectos/pages/proyecto/proyecto.page').then(m => m.ProyectoPage)
            },
            {
                path: 'seguridad',
                data: { roles: ['ADMIN'] },
                loadComponent: () => import('./features/seguridad/pages/usuario/usuario.page').then(m => m.UsuarioPage)
            },
            {
                path: 'organizacion',
                data: { roles: ['ADMIN', 'HR_BP', 'EXECUTIVE'] },
                loadComponent: () => import('./features/organizacion/pages/organizacion/organizacion.page').then(m => m.OrganizacionPage)
            },
            {
                path: 'reportes',
                data: { roles: ['ADMIN', 'EXECUTIVE', 'HR_BP'] },
                loadComponent: () => import('./features/reportes/pages/reporte/reporte.page').then(m => m.ReportePage)
            },
            {
                path: 'forbidden',
                loadComponent: () => import('./shared/pages/forbidden/forbidden.page').then(m => m.ForbiddenPage)
            },
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
    },

    { path: '**', redirectTo: 'forbidden' } // Redirige a la página de "Forbidden" para cualquier ruta no definida
    
];