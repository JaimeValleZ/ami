import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const required = route.data['roles'] as string[] | undefined;
    if (!required || required.length === 0) return true;
    const roles = this.auth.getRoles();
    const ok = required.some(r => roles.includes(r));
    if (!ok) {
      // opcional: mostrar alerta
      import('sweetalert2').then(Swal => {
        Swal.default.fire('Acceso denegado', 'No tienes permisos para ver esta página', 'error');
      });
      this.router.navigateByUrl('/');
    }
    return ok;
  }
}
