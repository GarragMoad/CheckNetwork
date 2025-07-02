import { TestBed } from '@angular/core/testing';

import { GrafanaServiceJson } from './grafana.service';

describe('GrafanaService', () => {
  let service: GrafanaServiceJson;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GrafanaServiceJson);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
