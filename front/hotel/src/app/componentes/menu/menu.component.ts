import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, AfterViewInit {


  @ViewChild('componentMenu') componentMenu: ElementRef;
  @ViewChild('componentMenuWrapper') componentMenuWrapper: ElementRef;
  showItemMenu: boolean;

  constructor() { }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
  }

  totgleMenu() {

    if (Array.from(this.componentMenu.nativeElement.classList).includes('menu-in')) {
      this.componentMenu.nativeElement.classList.remove('menu-in');
      this.componentMenu.nativeElement.classList.add('menu-out');
      this.showItemMenu =  false;
      setTimeout(() => {
        this.componentMenuWrapper.nativeElement.classList.add('hidden');
     
      }, 500);
    } else { 
      
      this.componentMenuWrapper.nativeElement.classList.remove('hidden');
      this.componentMenu.nativeElement.classList.remove('menu-out');
      this.componentMenu.nativeElement.classList.add('menu-in');
      setTimeout(() => {
        this.showItemMenu =  true;
      }, 500);
    
    }
  }

}
