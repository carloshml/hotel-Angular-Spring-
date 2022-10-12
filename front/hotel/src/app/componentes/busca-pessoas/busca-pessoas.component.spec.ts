import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscaPessoasComponent } from './busca-pessoas.component';

describe('BuscaPessoasComponent', () => {
  let component: BuscaPessoasComponent;
  let fixture: ComponentFixture<BuscaPessoasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BuscaPessoasComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BuscaPessoasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
