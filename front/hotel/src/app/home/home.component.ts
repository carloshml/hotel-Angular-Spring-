import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime } from 'rxjs';
import { Checkin } from '../entities/checkin';
import { Pessoa } from '../entities/pessoa';
import { RequestService } from '../services/request.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  checkinForm = new FormGroup({
    pessoa: new FormGroup(
      {
        id: new FormControl(),
        nome: new FormControl(''),
        documento: new FormControl(''),
        telefone: new FormControl(''),
      }
    ),
    adicionalVeiculo: new FormControl(false),
    dataEntrada: new FormControl('2022-10-26T11:01'),
    dataSaida: new FormControl(),
  });

  pesquisaCheckinForm = new FormGroup({
    tipoPesquisa: new FormControl('DATASAIDAISNULL')
  });

  checkins: Checkin[];
  termoPesquisa: string;
  showBuscaPessoas: boolean;
  pessoaNaoSelecionada = true;

  constructor(private router: Router, private request: RequestService) { }

  ngOnInit(): void {
    this.buscarCheckins('DATASAIDAISNULL');

    this.pesquisaCheckinForm
      .valueChanges
      .subscribe(tipoPesquisa => {

        this.buscarCheckins('' + tipoPesquisa.tipoPesquisa);

      });

    this.checkinForm
      .get('pessoa.nome')?.valueChanges
      .pipe(
        debounceTime(500)
      )
      .subscribe(termoPesquisa => {
        this.showBuscaPessoas = false;
        setTimeout(() => {
          this.showBuscaPessoas = true;
        }, 50);
        this.termoPesquisa = '' + termoPesquisa;
      });

  }


  fecharBuscaPessoas(event: Pessoa) {
    console.log(' fecharBuscaPessoas ::   ', event);
    this.checkinForm.get('pessoa')?.setValue(event);
    this.checkinForm.get('pessoa')?.disable();
    this.showBuscaPessoas = false;
    this.pessoaNaoSelecionada = false;
  }

  buscarNovamente() {
    this.checkinForm.get('pessoa')?.enable();
    this.pessoaNaoSelecionada = true;
  }

  async buscarCheckins(termoPesquisa: string) {
    this.request.buscarCheckin(0, new Date().toISOString().split('.')[0], new Date().toISOString().split('.')[0], termoPesquisa)
      .subscribe((checkins: Checkin[]) => {

        console.log(' checkins   ::   ', checkins);
 
        this.checkins = checkins;
      });
  }


  salvar() {

    let checkin = this.checkinForm.getRawValue() as Checkin;

    checkin.dataEntrada ? checkin.dataEntrada += ':00' : undefined;
    checkin.dataSaida ? checkin.dataSaida += ':00' : undefined;

    console.log(checkin);
    console.log(JSON.stringify(checkin));


    this.request.salvarCheckin(checkin)
      .subscribe((checkins: Checkin[]) => {
        console.log('Checkin salvo   :: ', checkins);
        this.checkinForm.reset();
        this.buscarCheckins('' + this.pesquisaCheckinForm.value.tipoPesquisa);
      });
  }

  incluirPessoa() {
    this.router.navigate(['./pessoa-cadastro']);
  }

  checkout(item: Checkin) {
    console.log(' item :: ', item);
  }

  voltar() {

  }

  proximo() {

  }

}
