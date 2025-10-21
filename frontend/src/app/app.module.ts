import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgendarCitaComponent } from './pages/pacientes/agendar-cita/agendar-cita.component';
import { VerCitasComponent } from './pages/pacientes/ver-citas/ver-citas.component';
import { SignupPacientesComponent } from './pages/pacientes/signup-pacientes/signup-pacientes.component';
import { CancelarCitaComponent } from './pages/cancelar-cita/cancelar-cita.component';
import { RegistrarMedicoComponent } from './pages/medicos/registrar-medico/registrar-medico.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { DashboardPacientesComponent } from './pages/pacientes/dashboard-pacientes/dashboard-pacientes.component';
import { AuthInterceptor } from './auth/auth.interceptor';
import { SidebarPacientesComponent } from './pages/pacientes/sidebar-pacientes/sidebar-pacientes.component';
import { LayoutComponent } from './pages/pacientes/layout/layout.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupPacientesComponent,
    AgendarCitaComponent,
    VerCitasComponent,
    SignupPacientesComponent,
    CancelarCitaComponent,
    RegistrarMedicoComponent,
    DashboardPacientesComponent,
    SidebarPacientesComponent,
    LayoutComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    provideClientHydration(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
