import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Pessoa } from 'src/app/entities/pessoa';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-busca-pessoas',
  templateUrl: './busca-pessoas.component.html',
  styleUrls: ['./busca-pessoas.component.scss']
})
export class BuscaPessoasComponent implements OnInit, OnDestroy {



  @Input() label = '';
  @Output() fecharBuscaPessoasEmitter: EventEmitter<any> = new EventEmitter();
  pessoas: Pessoa[];
  showLoader: boolean;
  showBuscaPessoas: boolean;
  pessoaNaoSelecionada = true;

  hospedeForm: FormGroup<{
    id: FormControl<any>;
    nome: FormControl<string>;
    documento: FormControl<string>;
    telefone: FormControl<string>;

  }> = new FormGroup<any>({
    id: new FormControl<any>('', {
      nonNullable: false,
      validators: [],
    }),
    nome: new FormControl<string>('', {
      nonNullable: false,
      validators: [],
    }),
    documento: new FormControl<string>('', {
      nonNullable: false,
      validators: [],
    }),
    telefone: new FormControl<string>('', {
      nonNullable: false,
      validators: [],
    }),
  }
  );


  constructor(private request: RequestService) { }

  ngOnInit(): void {
    this.showLoader = true;
    this.hospedeForm
      .controls.nome
      .valueChanges
      .subscribe(() => {
        this.request.buscarPessoas(this.hospedeForm.controls.nome.value)
          .subscribe((pessoas: Pessoa[]) => {
            this.showBuscaPessoas = true;
            this.showLoader = false;
            this.pessoas = pessoas;
          });
      });

    this.request.buscarPessoas(this.hospedeForm.controls.nome.value)
      .subscribe((pessoas: Pessoa[]) => {
        this.showLoader = false;
        this.pessoas = pessoas;
      });
  }

  ngOnDestroy() {
    this.showLoader = false;
  }

  fechar(item: Pessoa) {
    this.showBuscaPessoas = false;
    this.pessoaNaoSelecionada = false;
    delete item.msgDetalhe;
    this.hospedeForm.get('nome')?.setValue(item.nome);
    this.hospedeForm.disable();
    this.fecharBuscaPessoasEmitter.emit(item);
  }

  checkinSubscriber!: any;

  buscar() {
    this.hospedeForm.enable();
    if (this.checkinSubscriber) {
      this.checkinSubscriber.unsubscribe();
    }

    this.fecharBuscaPessoasEmitter.emit({
      id: new FormControl(),
      nome: '',
      documento: '',
      telefone: '',
    });
    this.pessoaNaoSelecionada = true;
    this.showBuscaPessoas = false;
    this.checkinSubscriber = this.hospedeForm
      .get('nome')?.valueChanges
      .pipe(
        debounceTime(500)
      )
      .subscribe(termoPesquisa => {
        this.showBuscaPessoas = false;
        setTimeout(() => {
          this.showBuscaPessoas = true;
        }, 50);
      });
  }

}
