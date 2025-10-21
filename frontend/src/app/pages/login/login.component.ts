import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  userRole = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onLogin() {
    if (!this.email || !this.password || !this.userRole) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Por favor, completa todos los campos antes de continuar.',
        confirmButtonColor: '#0ea5e9'
      });
      return;
    }

    this.authService.login(this.email, this.password).subscribe({
      next: () => {
        const roles = this.authService.getRoles();
        const rol = roles[0]?.toUpperCase() || this.userRole.toUpperCase();

        // 🔁 Redirige según el rol sin mostrar aún el toast
        if (rol === 'PACIENTE') {
          this.router.navigate(['/paciente/dashboard']);
        } else if (rol === 'ADMINISTRADOR') {
          this.router.navigate(['/admin/dashboard']);
        } else if (rol === 'MEDICO') {
          this.router.navigate(['/medico/dashboard']);
        }

      },
      error: (err) => {
        console.error('Error al iniciar sesión:', err);
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'error',
          title: 'Correo o contraseña incorrectos',
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: true
        });
      }
    });
  }
}
