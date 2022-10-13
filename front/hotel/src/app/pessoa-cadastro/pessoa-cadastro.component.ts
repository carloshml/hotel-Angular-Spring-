import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Pessoa } from '../entities/pessoa';
import { RequestService } from '../services/request.service';

@Component({
  selector: 'app-pessoa-cadastro',
  templateUrl: './pessoa-cadastro.component.html',
  styleUrls: ['./pessoa-cadastro.component.scss']
})
export class PessoaCadastroComponent implements OnInit {

  pessoaForm = new FormGroup(
    {
      id: new FormControl(),
      nome: new FormControl(''),
      documento: new FormControl(''),
      telefone: new FormControl(''),
    }
  );

  constructor(private router: Router, private request: RequestService) { }

  ngOnInit(): void {
  }


  salvar() {

    let pessoa = this.pessoaForm.getRawValue() as Pessoa;
    pessoa.nome = pessoa.nome.toUpperCase();

    this.request.salvarPessoa(pessoa)
    .subscribe((pessoa: Pessoa) => {
      console.log('Checkin salvo   :: ', pessoa);  
      this.router.navigate(['']);
    });
  }

}
