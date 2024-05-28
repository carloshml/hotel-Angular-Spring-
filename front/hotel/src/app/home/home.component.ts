import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Checkin } from '../entities/checkin';
import { Pessoa } from '../entities/pessoa';
import { Pager, PaginacaoService } from '../services/paginacao.service';
import { RequestService } from '../services/request.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  checkinForm = new FormGroup({
    hospede: new FormGroup(
      {
        id: new FormControl(),
        nome: new FormControl(''),
        documento: new FormControl(''),
        telefone: new FormControl(''),
      }
    ),
    adicionalVeiculo: new FormControl(false),
    dataEntrada: new FormControl(),
    dataSaida: new FormControl(''),
  });

  pesquisaCheckinForm = new FormGroup({
    tipoPesquisa: new FormControl('DATASAIDAISNULL')
  });

  checkins: Checkin[] = [];
  termoPesquisa: string;
  showBuscaPessoas: boolean;
  pessoaNaoSelecionada = true;
  mensagem: string;
  checkinSubscriber: any;

  paginacaoServico = new PaginacaoService();
  pager: Pager = new Pager();

  valorMaximoLinhasGrid = 3;
  totalElementos = 0;
  termoPesquisaCheckin = '';
  showLoader = false;
  checkinSelecionado: Checkin;
  showCheckout: boolean;
  constructor(private router: Router, private request: RequestService) { }

  ngOnInit(): void {
    const tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
    const localISOTime = (new Date(Date.now() - tzoffset)).toISOString().split('.')[0];
    this.checkinForm.get('dataEntrada')?.setValue(localISOTime);
    this.buscaCheckin('DATASAIDAISNULL');
    this.pesquisaCheckinForm
      .valueChanges
      .subscribe(tipoPesquisa => {
        this.termoPesquisaCheckin = '' + tipoPesquisa.tipoPesquisa;
        this.buscaCheckin(this.termoPesquisaCheckin);
      });   
  }

  atualizaData(check: string) {
    if (!this.checkinForm.get(check)?.value) {
      const tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
      const localISOTime = (new Date(Date.now() - tzoffset)).toISOString().split('.')[0];
      this.checkinForm.get(check)?.setValue(localISOTime);
    }
  }

  selecionarPessoa(event: Pessoa) {
    this.checkinForm.get('hospede')?.setValue(event);
    this.checkinForm.get('hospede')?.disable();
  }

  salvarCheckin() {
    let checkin = this.checkinForm.getRawValue() as Checkin;


    if (checkin.dataSaida && (new Date(checkin.dataEntrada) > new Date(checkin.dataSaida))) {
      this.mensagem = 'Atenção, Data de Entrada Maior que a Data Saida!';
      setTimeout(() => {
        this.mensagem = '';
      }, 3000);
      return;
    }
    if (!checkin.dataEntrada) {
      this.mensagem = 'Atenção, Data Entrada Deve Ser Selecionada!';
      setTimeout(() => {
        this.mensagem = '';
      }, 3000);
      return;
    }
    if (!checkin.hospede.nome) {
      this.mensagem = 'Atenção, Selecione uma Pessoa!';
      setTimeout(() => {
        this.mensagem = '';
      }, 3000);
      return;
    }

    this.showLoader = true;
    this.request.salvarCheckin(checkin)
      .subscribe((checkins: Checkin[]) => {
        this.showLoader = false;
        this.checkinForm.reset();
        this.buscaCheckin('' + this.pesquisaCheckinForm.value.tipoPesquisa);
      });
  }

  incluirPessoa() {
    this.router.navigate(['./pessoa-cadastro']);
  }

  checkout(item: Checkin) {
    this.checkinSelecionado = item;
    this.showCheckout = true;
  }

  async buscaCheckin(termoPesquisaCheckin: string) {
    this.termoPesquisaCheckin = termoPesquisaCheckin;
    this.showLoader = true;
    await this.request.buscaTotalCheckinPaginado(0, new Date().toISOString().split('.')[0],
      new Date().toISOString().split('.')[0], this.termoPesquisaCheckin)
      .subscribe((retornoTotalAtendimentos: number) => {
        this.totalElementos = Number(retornoTotalAtendimentos).valueOf();
        if (this.totalElementos > 0) {
          this.pager.totalPages = 1;
          this.setPageofClientes(1);
        } else {
          this.checkins = [];
        }
      });

    this.showLoader = false;
  }

  setPageofClientes(page: any) { 
    if (page < 1 || page > this.pager.totalPages) {
      return;
    }
    // obter a paginacao através do serviço
    this.pager = this.paginacaoServico.getPager(this.totalElementos, page, this.valorMaximoLinhasGrid);
    // obtem a pagina atual dos itens
    this.showLoader = true;
    this.request.buscarCheckin(0, new Date().toISOString().split('.')[0], new Date().toISOString().split('.')[0], this.termoPesquisaCheckin,
      (this.pager.currentPage - 1), this.valorMaximoLinhasGrid)
      .subscribe((checkins: Checkin[]) => {
        this.showLoader = false;
        this.checkins = checkins;
      });
  }
  
  fecharCheckout() {
    this.showCheckout = false;
    this.checkinForm.reset();
    this.buscaCheckin('' + this.pesquisaCheckinForm.value.tipoPesquisa);
  }

}
