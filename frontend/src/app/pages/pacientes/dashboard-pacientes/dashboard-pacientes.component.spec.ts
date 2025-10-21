import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardPacientesComponent } from './dashboard-pacientes.component';

describe('DashboardPacientesComponent', () => {
  let component: DashboardPacientesComponent;
  let fixture: ComponentFixture<DashboardPacientesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashboardPacientesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashboardPacientesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
