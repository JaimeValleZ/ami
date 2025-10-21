import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface Direccion {
  calle: string;
  numero: string;
  complemento: string;
  barrio: string;
  codigoPostal: string;
  ciudad: string;
  estado: string;
}

export interface PacienteRegistroDTO {
  nombre: string;
  email: string;
  telefono: string;
  documento: string;
  password: string;
  direccion: Direccion;
}

@Injectable({
  providedIn: 'root'
})
export class PacienteService {
  private apiUrl = `${environment.apiUrl}/paciente`;

  constructor(private http: HttpClient) {}

  registrarPaciente(paciente: PacienteRegistroDTO): Observable<any> {
    return this.http.post(this.apiUrl, paciente);
  }

  listarPacientes(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  detallarPaciente(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  actualizarPaciente(paciente: any): Observable<any> {
    return this.http.put(this.apiUrl, paciente);
  }

  bloquearPaciente(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
