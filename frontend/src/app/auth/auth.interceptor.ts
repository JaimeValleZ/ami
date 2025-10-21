import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse
} from '@angular/common/http';
import { AuthService } from './auth.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getToken();

    let cloned = req;
    if (token) {
      cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        // 🟡 Si el backend responde con 401, significa token expirado o inválido
        if (error.status === 401) {
          this.auth.logout();
          Swal.fire({
            icon: 'info',
            title: 'Sesión expirada',
            text: 'Tu sesión ha caducado. Por favor, inicia sesión nuevamente.',
            confirmButtonColor: '#2563eb'
          });
        }
        return throwError(() => error);
      })
    );
  }
}
