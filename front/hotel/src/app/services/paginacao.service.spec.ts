import { TestBed } from '@angular/core/testing';

import { PaginacaoService } from './paginacao.service';

describe('PaginacaoService', () => {
  let service: PaginacaoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaginacaoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
