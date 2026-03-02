import { TestBed } from '@angular/core/testing';

import { Modalservice } from './modalservice';

describe('Modalservice', () => {
  let service: Modalservice;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Modalservice);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
