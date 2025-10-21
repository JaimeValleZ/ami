import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { ConsultaService, MotivoCancelamiento } from '../../../services/consulta.service';
import { MedicosService, Medico } from '../../../services/medicos.service';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-dashboard-pacientes',
  templateUrl: './dashboard-pacientes.component.html',
  styleUrls: ['./dashboard-pacientes.component.css']
})
export class DashboardPacientesComponent implements OnInit {

  consultas: any[] = [];
  cargando = true;

  constructor(
    public authService: AuthService,
    private consultaService: ConsultaService,
    private medicoService: MedicosService
  ) {}

  ngOnInit(): void {
    //this.mostrarBienvenida();
    this.cargarConsultas();
  }

  /** 💬 Mostrar bienvenida 
  private mostrarBienvenida(): void {
    const nombre = this.authService.getNombreFromToken() || 'usuario';
    const roles = this.authService.getRoles();
    const rol = roles[0]?.toUpperCase();

    Swal.fire({
      toast: true,
      position: 'top-end',
      icon: 'success',
      title: `¡Bienvenido, ${nombre}! (${rol})`,
      showConfirmButton: false,
      timer: 2000,
      timerProgressBar: true,
      background: '#f0f9ff',
      color: '#0369a1'
    });
  }*/

  /** 📋 Cargar consultas del paciente */
  private cargarConsultas(): void {
    const idPaciente = this.authService.getIdFromToken();
    if (!idPaciente) return;

    this.consultaService.listarConsultas(idPaciente).subscribe({
      next: (res: any) => {
        const consultas = res.content;

        const peticiones = consultas.map((consulta: any) =>
          this.medicoService.obtenerMedicoPorId(consulta.idMedico)
        );

        forkJoin<Medico[]>(peticiones).subscribe({
          next: (medicos: Medico[]) => {
            this.consultas = consultas.map((consulta: any, i: number) => ({
              ...consulta,
              medico: medicos[i]
            }));
            this.cargando = false;
          },
          error: (err) => {
            console.error('Error al obtener médicos:', err);
            this.cargando = false;
          }
        });
      },
      error: (err) => {
        console.error('Error al obtener consultas:', err);
        this.cargando = false;
      }
    });
  }

  /** ❌ Cancelar cita con selección de motivo */
  cancelarCita(idConsulta: number): void {
      if (!idConsulta) {
    Swal.fire('Error', 'Id de la cita inválido', 'error');
    return;
  }

    Swal.fire({
      title: 'Cancelar cita',
      text: '¿Estás seguro que deseas cancelar esta cita?',
      icon: 'warning',
      input: 'select',
      inputOptions: {
        PACIENTE_DESISTIO: 'El paciente desistió',
        MEDICO_CANCELO: 'El médico canceló',
        OTROS: 'Otros'
      },
      inputPlaceholder: 'Selecciona un motivo',
      showCancelButton: true,
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No, volver',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      preConfirm: (motivo) => {
        if (!motivo) {
          Swal.showValidationMessage('Debes seleccionar un motivo');
        }
        return motivo;
      }
    }).then((result) => {
      if (result.isConfirmed && result.value) {
        const motivo = result.value as MotivoCancelamiento;
        this.consultaService.cancelarCita(idConsulta, motivo).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Cita cancelada',
              text: 'Tu cita ha sido cancelada correctamente.',
              confirmButtonColor: '#2563eb'
            });
            this.cargarConsultas();
          },
          error: (err) => {
            Swal.fire('Error', err?.error?.mensaje || 'No se pudo cancelar la cita.', 'error');
          }
        });
      }
    });
  }
}
