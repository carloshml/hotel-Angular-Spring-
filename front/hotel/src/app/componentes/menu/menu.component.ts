import { AfterViewInit, Component, ElementRef, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { Pessoa } from 'src/app/entities/pessoa';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, AfterViewInit {


  @ViewChild('componentMenu') componentMenu: ElementRef;
  @Output() componentMenuEmitter: EventEmitter<ElementRef> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    this.componentMenuEmitter.emit(this.componentMenu);
  }

}
