import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PacienteService } from '../../../services/pacientes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-signup-pacientes',
  templateUrl: './signup-pacientes.component.html',
  styleUrls: ['./signup-pacientes.component.css']
})
export class SignupPacientesComponent implements OnInit {
  form!: FormGroup;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private pacienteService: PacienteService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', Validators.required],
      documento: ['', Validators.required],
      password: ['', Validators.required],
      direccion: this.fb.group({
        calle: ['', Validators.required],
        numero: [''],
        complemento: [''],
        barrio: ['', Validators.required],
        codigo_postal: ['', Validators.required],
        ciudad: ['', Validators.required],
        estado: ['', Validators.required]
      })
    });
  }

  registrarPaciente() {
    if (this.form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Por favor completa todos los campos obligatorios.',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    this.isLoading = true;
    const data = this.form.value;

    this.pacienteService.registrarPaciente(data).subscribe({
      next: (res) => {
        this.isLoading = false;
        Swal.fire({
          icon: 'success',
          title: '¡Registro exitoso!',
          text: 'El paciente fue registrado correctamente.',
          confirmButtonColor: '#3085d6'
        });
        this.form.reset();
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error al registrar paciente:', err);

        Swal.fire({
          icon: 'error',
          title: 'Error al registrar',
          text:
            err.error?.mensaje ||
            'No se pudo completar el registro. Intenta nuevamente.',
          confirmButtonColor: '#d33'
        });
      }
    });
  }
}
