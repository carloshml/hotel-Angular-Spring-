import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Checkin } from 'src/app/entities/checkin';
import { RequestService } from 'src/app/services/request.service';

@Component({
  selector: 'app-modal-checkout',
  templateUrl: './modal-checkout.component.html',
  styleUrls: ['./modal-checkout.component.scss']
})
export class ModalCheckoutComponent implements OnInit, OnDestroy {



  @Input() checkin: Checkin;
  @Output() fecharCheckoutEmitter: EventEmitter<boolean> = new EventEmitter();
  total = 0;
  showLoader: boolean;
  checkins: Checkin[];
  mensagem: string;

  constructor(private request: RequestService, private router: Router) { }

  ngOnInit(): void {
    this.request.buscarCheckin(this.checkin.id, new Date().toISOString().split('.')[0], new Date().toISOString().split('.')[0],
      'BY_ID', 0, 4)
      .subscribe((checkins: Checkin[]) => {
        this.showLoader = false;
      
        this.checkins = checkins;
        this.total = this.checkins[0].valorAPagar;
        this.checkin = this.checkins[0];
      });
  }

  ngOnDestroy() {

  }


  checkout() {
    const tzoffset = new Date().getTimezoneOffset() * 60000; //offset in milliseconds
    const localISOTime = (new Date(Date.now() - tzoffset)).toISOString().split('.')[0];
    this.checkin.dataSaida = localISOTime;


    this.showLoader = true;
    this.request.salvarCheckin(this.checkin)
      .subscribe((checkins: Checkin) => {
        this.showLoader = false;

        if (checkins.dataSaida) {
                  
          this.fecharCheckoutEmitter.emit(true);
        } else {
          this.mensagem = 'Chekin Não foi salvo!';
          setTimeout(() => {
            this.mensagem = '';
          }, 3000);
        }
      });
  }


  cancelar() {
    this.fecharCheckoutEmitter.emit(true);
  }

}
