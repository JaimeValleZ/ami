import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Especialidad } from './consulta.service';

export interface Medico {
  id: number;
  nombre: string;
  email: string;
  telefono: string;
  especialidad: Especialidad;
}

@Injectable({
  providedIn: 'root'
})
export class MedicosService {

  private baseUrl = `${environment.apiUrl}/medicos`;

  constructor(private http: HttpClient) {}

  /** 📋 Lista todos los médicos (paginación opcional) */
  listarMedicos(): Observable<any> {
    return this.http.get(`${this.baseUrl}?size=100`);
  }

  /** 🔍 Obtiene el detalle de un médico por su ID */
  obtenerMedicoPorId(id: number): Observable<Medico> {
    return this.http.get<Medico>(`${this.baseUrl}/${id}`);
  }
}
