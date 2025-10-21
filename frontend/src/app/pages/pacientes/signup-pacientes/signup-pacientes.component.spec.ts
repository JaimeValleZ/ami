import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupPacientesComponent } from './signup-pacientes.component';

describe('SignupPacientesComponent', () => {
  let component: SignupPacientesComponent;
  let fixture: ComponentFixture<SignupPacientesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SignupPacientesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SignupPacientesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
