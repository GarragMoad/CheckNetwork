import { TestBed } from '@angular/core/testing';

import { SnmpStatusService } from './snmp-status.service';

describe('SnmpStatusService', () => {
  let service: SnmpStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SnmpStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
