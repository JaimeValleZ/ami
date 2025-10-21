import { Component } from '@angular/core';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar-pacientes',
  templateUrl: './sidebar-pacientes.component.html',
  styleUrl: './sidebar-pacientes.component.css'
})
export class SidebarPacientesComponent {

  userName: string | null = null;
  userRole: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const userInfo = this.authService.getUserInfo();
    this.userName = userInfo.nombre;
    this.userRole = userInfo.roles[0] || 'Usuario';
  }

  logout(): void {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: 'Tu sesión actual se cerrará.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, salir',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6b7280'
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout();
        this.router.navigate(['/login']);
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'Sesión cerrada con éxito',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true,
        });
      }
    });
  }

}
