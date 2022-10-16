import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime } from 'rxjs';
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
  pagerClientes: Pager = new Pager();

  valorMaximoLinhasGrid = 3;
  totalElementos = 0;
  termoPesquisaCheckin = '';
  showLoader: boolean;
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

    this.buscarNovamente();
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
    this.showBuscaPessoas = false;
    this.pessoaNaoSelecionada = false;
  }

  buscarNovamente() {
    if (this.checkinSubscriber) {
      this.checkinSubscriber.unsubscribe();
    }
    this.checkinForm.get('hospede')?.setValue({
      id: new FormControl(),
      nome: '',
      documento: '',
      telefone: '',
    });
    this.checkinForm.get('hospede')?.enable();
    this.pessoaNaoSelecionada = true;
    this.showBuscaPessoas = false;
    this.checkinSubscriber = this.checkinForm
      .get('hospede.nome')?.valueChanges
      .pipe(
        debounceTime(500)
      )
      .subscribe(termoPesquisa => {
        this.showBuscaPessoas = false;
        this.termoPesquisa = ('' + termoPesquisa).toUpperCase();
        setTimeout(() => {
          this.showBuscaPessoas = true;
        }, 50);
      });
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
        this.buscarNovamente();
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

  buscaCheckin(termoPesquisaCheckin: string) {
    this.termoPesquisaCheckin = termoPesquisaCheckin;

    this.showLoader = true;
    this.request.buscaTotalCheckinPaginado(0, new Date().toISOString().split('.')[0],
      new Date().toISOString().split('.')[0], this.termoPesquisaCheckin)
      .subscribe((retornoTotalAtendimentos: number) => {        
        this.totalElementos = Number(retornoTotalAtendimentos).valueOf();
        if (this.totalElementos > 0) {
          this.pagerClientes.totalPages = 1;
          this.setPageofClientes(1);
        } else {
          this.showLoader = false;
          this.checkins = [];
        }
      });
  }

  setPageofClientes(page: number) {
    if (page < 1 || page > this.pagerClientes.totalPages) {
      return;
    }
    // obter a paginacao através do serviço
    this.pagerClientes = this.paginacaoServico.getPager(this.totalElementos, page, this.valorMaximoLinhasGrid);

    
    // obtem a pagina atual dos itens
    this.showLoader = true;
    this.request.buscarCheckin(0, new Date().toISOString().split('.')[0], new Date().toISOString().split('.')[0], this.termoPesquisaCheckin,
      (this.pagerClientes.currentPage - 1), this.valorMaximoLinhasGrid)
      .subscribe((checkins: Checkin[]) => {
        this.showLoader = false;
        
        this.checkins = checkins;
      });
  }



  fecharCheckout() {
    this.showCheckout = false;
    this.checkinForm.reset();
    this.buscarNovamente();
    this.buscaCheckin('' + this.pesquisaCheckinForm.value.tipoPesquisa);
  }

}
