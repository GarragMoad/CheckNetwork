import { TestBed } from '@angular/core/testing';

import { GuacamoleService } from './guacamole.service';

describe('GuacamoleService', () => {
  let service: GuacamoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GuacamoleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
