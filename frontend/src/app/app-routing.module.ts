import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { SignupPacientesComponent } from './pages/pacientes/signup-pacientes/signup-pacientes.component';
import { DashboardPacientesComponent } from './pages/pacientes/dashboard-pacientes/dashboard-pacientes.component';
import { AgendarCitaComponent } from './pages/pacientes/agendar-cita/agendar-cita.component';
import { VerCitasComponent } from './pages/pacientes/ver-citas/ver-citas.component';
import { CancelarCitaComponent } from './pages/cancelar-cita/cancelar-cita.component';
import { RegistrarMedicoComponent } from './pages/medicos/registrar-medico/registrar-medico.component';
import { RoleGuard } from './auth/role.guard';
import { AuthGuard } from './auth/auth.guard';
import { LayoutComponent } from './pages/pacientes/layout/layout.component';

const routes: Routes = [
  // 🔹 Rutas públicas
  { path: 'login', component: LoginComponent },
  { path: 'registrar-paciente', component: SignupPacientesComponent },

  // 🔹 Rutas de PACIENTE con layout
  {
    path: 'paciente',
    component: LayoutComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['PACIENTE'] },
    children: [
      { path: 'dashboard', component: DashboardPacientesComponent },
      { path: 'agendar-cita', component: AgendarCitaComponent },
      { path: 'ver-citas', component: VerCitasComponent },
      { path: 'cancelar-cita', component: CancelarCitaComponent },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },

  // 🔹 Rutas de ADMIN (más adelante puedes crear layout para este rol)
  {
    path: 'registrar-medico',
    component: RegistrarMedicoComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['ADMINISTRADOR'] }
  },

  // 🔹 Ruta por defecto y 404
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
