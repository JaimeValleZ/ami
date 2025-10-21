import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
interface TokenResponse { token: string; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'ami_token';

  constructor(private http: HttpClient, private router: Router) {}

  /** Inicia sesión y guarda el token */
  login(email: string, password: string): Observable<void> {
    return this.http.post<TokenResponse>(`${environment.apiUrl}/login`, { email, password })
      .pipe(
        tap(res => this.setToken(res.token)),
        map(() => void 0)
      );
  }

  /** Cierra sesión y limpia token */
  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }

  /** Guarda el token */
  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  /** Obtiene el token */
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /** Verifica si hay sesión activa, y elimina token si expiró */
  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload: any = jwtDecode(token);
      const exp = payload.exp as number | undefined;

      if (exp && Date.now() >= exp * 1000) {
        this.logout(); // auto-logout si expiró
        return false;
      }

      return true;
    } catch {
      this.logout(); // auto-logout si token inválido
      return false;
    }
  }

  /** Decodifica el payload del token */
  private decodePayload(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload: any = jwtDecode(token);
      const exp = payload.exp as number | undefined;

      // Si expiró, eliminarlo automáticamente
      if (exp && Date.now() >= exp * 1000) {
        this.logout();
        return null;
      }

      return payload;
    } catch {
      this.logout();
      return null;
    }
  }

  /** Obtiene roles */
  getRoles(): string[] {
    const payload = this.decodePayload();
    if (!payload) return [];

    const roles = payload.roles || payload.authorities || [];
    return (roles as string[]).map(r => r.replace(/^ROLE_/, ''));
  }

  /** Obtiene email */
  getEmailFromToken(): string | null {
    const payload = this.decodePayload();
    return payload?.sub || payload?.email || null;
  }

  /** Obtiene nombre */
  getNombreFromToken(): string | null {
    const payload = this.decodePayload();
    return payload?.nombre || null;
  }

  /** Obtiene id */
  getIdFromToken(): number | null {
    const payload = this.decodePayload();
    return payload?.id || null;
  }

  /** Obtiene info resumida del usuario */
  getUserInfo(): { id: number; email: string | null; roles: string[]; nombre: string | null } {
    return {
      id: this.getIdFromToken() || 0,
      email: this.getEmailFromToken(),
      roles: this.getRoles(),
      nombre: this.getNombreFromToken()
    };
  }
}
