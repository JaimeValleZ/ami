import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface DatosReservaConsulta {
  idMedico?: number;
  idPaciente: number;
  fecha: string;
  especialidad?: Especialidad;
}

export enum Especialidad {
  ORTOPEDIA = 'ORTOPEDIA',
  CARDIOLOGIA = 'CARDIOLOGIA',
  GINECOLOGIA = 'GINECOLOGIA',
  DERMATOLOGIA = 'DERMATOLOGIA'
}

export enum MotivoCancelamiento {
  PACIENTE_DESISTIO = 'PACIENTE_DESISTIO',
  MEDICO_CANCELA = 'MEDICO_CANCELO',
  OTROS = 'OTROS'
}


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


export class ConsultaService {
private baseUrl = `${environment.apiUrl}/consultas`;

  constructor(private http: HttpClient) {}

  reservarCita(datos: DatosReservaConsulta): Observable<any> {
    return this.http.post(this.baseUrl, datos);
  }

   /** Listar consultas paginadas por paciente */
  listarConsultas(idPaciente: number, page: number = 0, size: number = 10): Observable<any> {
    return this.http.get(`${this.baseUrl}/consultas/${idPaciente}?page=${page}&size=${size}`);
  }

  cancelarCita(idConsulta: number, motivo: MotivoCancelamiento): Observable<any> {
  const body = {
    idConsulta,
    motivo
  };
  return this.http.delete(`${this.baseUrl}`, { body });
}

}
