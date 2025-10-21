import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidebarPacientesComponent } from './sidebar-pacientes.component';

describe('SidebarPacientesComponent', () => {
  let component: SidebarPacientesComponent;
  let fixture: ComponentFixture<SidebarPacientesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SidebarPacientesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SidebarPacientesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
