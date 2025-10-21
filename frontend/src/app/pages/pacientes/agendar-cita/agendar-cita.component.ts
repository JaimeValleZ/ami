import { Component, OnInit } from '@angular/core';
import { ConsultaService } from '../../../services/consulta.service';
import Swal from 'sweetalert2';
import { AuthService } from '../../../auth/auth.service';
import { MedicosService } from '../../../services/medicos.service';

@Component({
  selector: 'app-agendar-cita',
  templateUrl: './agendar-cita.component.html',
  styleUrls: ['./agendar-cita.component.css']
})
export class AgendarCitaComponent implements OnInit {

  pacienteNombre = '';
  pacienteEmail = '';
  idPaciente!: number;

  especialidades: string[] = ['CARDIOLOGIA', 'ORTOPEDIA', 'GINECOLOGIA', 'DERMATOLOGIA'];
  medicos: any[] = [];

  form = {
    especialidad: '',
    idMedico: '',
    fecha: '',
    hora: ''
  };

  constructor(
    private consultaService: ConsultaService,
    private medicoService: MedicosService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cargarDatosPaciente();
    this.cargarMedicos();
  }

  /** Carga datos del paciente desde AuthService */
  cargarDatosPaciente(): void {
    const user = this.authService.getUserInfo();
    this.pacienteNombre = user.nombre || '';
    this.pacienteEmail = user.email || '';
    // @ts-ignore
    this.idPaciente = (user as any).id || 0;
  }

  /** Carga lista de médicos desde el backend */
  cargarMedicos(): void {
    this.medicoService.listarMedicos().subscribe({
      next: (data: any) => {
        this.medicos = data.content || data;
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los médicos.', 'error');
      }
    });
  }

  /** Envía los datos al backend */
  onSubmit(): void {
    if (!this.form.fecha || !this.form.hora) {
      Swal.fire('Atención', 'Debe seleccionar una fecha y hora.', 'warning');
      return;
    }

    const fechaCompleta = `${this.form.fecha}T${this.form.hora}:00`;

    const datos: any = {
      idPaciente: this.idPaciente,
      idMedico: this.form.idMedico ? Number(this.form.idMedico) : undefined,
      especialidad: this.form.especialidad || undefined,
      fecha: fechaCompleta
    };

    Swal.fire({
      title: '¿Estás seguro?',
      text: '¿Deseas agendar esta cita médica?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, agendar cita',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#2563eb',
      cancelButtonColor: '#d33'
    }).then((result) => {
      if (result.isConfirmed) {
        this.consultaService.reservarCita(datos).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Cita agendada correctamente',
              html: `
              <p><strong>Fecha:</strong> ${new Date(fechaCompleta).toLocaleDateString()}</p>
              <p><strong>Hora:</strong> ${this.form.hora}</p>
            `,
              confirmButtonText: 'Entendido',
              confirmButtonColor: '#2563eb'
            });

            this.form = {
              especialidad: '',
              idMedico: '',
              fecha: '',
              hora: ''
            };
          },
          error: (err) => {
            let mensaje = 'Verifica los datos e intenta nuevamente.';

            if (err?.error) {
              if (typeof err.error === 'string') {
                // Caso: mensaje plano devuelto por RuntimeException
                mensaje = err.error;
              } else if (err.error.mensaje) {
                // Caso: backend devuelve un objeto con campo "mensaje"
                mensaje = err.error.mensaje;
              }
            }
            Swal.fire({
              icon: 'error',
              title: 'Error al agendar cita',
              text: mensaje || 'Verifica los datos e intenta nuevamente.'
            });
          }
        });
      }
    });
  }
}
