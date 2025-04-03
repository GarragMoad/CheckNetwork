import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NetworkScannerComponent } from './network-scanner.component';

describe('NetworkScannerComponent', () => {
  let component: NetworkScannerComponent;
  let fixture: ComponentFixture<NetworkScannerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NetworkScannerComponent]
    });
    fixture = TestBed.createComponent(NetworkScannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
