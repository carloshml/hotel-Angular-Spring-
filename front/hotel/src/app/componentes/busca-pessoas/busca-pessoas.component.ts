import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Pessoa } from 'src/app/entities/pessoa';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-busca-pessoas',
  templateUrl: './busca-pessoas.component.html',
  styleUrls: ['./busca-pessoas.component.scss']
})
export class BuscaPessoasComponent implements OnInit {


  @Input() termoPesquisa : string; 
  @Output() fecharBuscaPessoasEmitter: EventEmitter<Pessoa> = new EventEmitter();
  pessoas: Pessoa[];

  constructor(private request: RequestService) { }

  ngOnInit(): void {
    this.request.buscarPessoas('' + this.termoPesquisa)
    .subscribe((pessoas : Pessoa[]) => {
      console.log(' pessoas ::   ', pessoas);
      this.pessoas = pessoas;
    });
  }

  fechar(item : Pessoa){
    this.fecharBuscaPessoasEmitter.emit(item);
  }

}
