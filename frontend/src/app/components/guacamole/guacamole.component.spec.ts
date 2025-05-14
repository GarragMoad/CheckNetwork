import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GuacamoleComponent } from './guacamole.component';

describe('GuacamoleComponent', () => {
  let component: GuacamoleComponent;
  let fixture: ComponentFixture<GuacamoleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GuacamoleComponent]
    });
    fixture = TestBed.createComponent(GuacamoleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
