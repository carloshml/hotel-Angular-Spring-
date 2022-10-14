import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Pessoa } from 'src/app/entities/pessoa';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-busca-pessoas',
  templateUrl: './busca-pessoas.component.html',
  styleUrls: ['./busca-pessoas.component.scss']
})
export class BuscaPessoasComponent implements OnInit, OnDestroy {


  @Input() termoPesquisa : string; 
  @Output() fecharBuscaPessoasEmitter: EventEmitter<Pessoa> = new EventEmitter();
  pessoas: Pessoa[];
  showLoader: boolean;

  constructor(private request: RequestService) { }

  ngOnInit(): void {

    this.showLoader = true;
    this.request.buscarPessoas('' + this.termoPesquisa)
    .subscribe((pessoas : Pessoa[]) => {
      this.showLoader = false;     
      this.pessoas = pessoas;
    });
  }

  ngOnDestroy(){
    this.showLoader = false;
  }

  fechar(item : Pessoa){
    this.fecharBuscaPessoasEmitter.emit(item);
  }

}
