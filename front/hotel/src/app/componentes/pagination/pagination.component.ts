import { Component, EventEmitter, Input, Output, input, output } from '@angular/core';
import { Pager, PaginacaoService } from 'src/app/services/paginacao.service';

@Component({
  selector: 'app-pagination',  
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss'
})
export class PaginationComponent {

  @Output() paginationEmmiter = new EventEmitter<number>();
  @Input('pager') pager: Pager = new Pager();
  paginacaoServico: PaginacaoService;
  valorMaximoLinhasGrid = 3;
  totalElementos = 0;

  constructor( ){
  } 

  setPageofClientes(page: number) {  
    this.paginationEmmiter.emit(page);
  }

}
