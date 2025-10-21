import { Component } from '@angular/core';

@Component({
  selector: 'app-ver-citas',
  templateUrl: './ver-citas.component.html',
  styleUrl: './ver-citas.component.css'
})
export class VerCitasComponent {
  citasProximas = [
    {
      icon: '',
      titulo: 'Consulta de Seguimiento',
      doctor: 'Dr. Ricardo García - Cardiología',
      fecha: '15 de Julio, 10:00 AM',
    },
    {
      icon: '',
      titulo: 'Revisión Anual',
      doctor: 'Dra. Sofía Martínez - Dermatología',
      fecha: '22 de Julio, 2:30 PM',
    }
  ];

  citasPasadas = [
    {
      icon: '',
      titulo: 'Vacunación',
      doctor: 'Dr. Carlos López - Pediatría',
      fecha: '8 de Junio, 11:00 AM',
    },
    {
      icon: '',
      titulo: 'Control de Rutina',
      doctor: 'Dra. Laura Pérez - Ginecología',
      fecha: '15 de Mayo, 9:00 AM',
    }
  ];
}
