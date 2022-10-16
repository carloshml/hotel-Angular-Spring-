import { Component, ElementRef } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'hotel';
  showMenu = false;
  menuComponente: HTMLDivElement;

  totgleMenu() {   
    if (this.showMenu) {
      this.menuComponente.classList.remove('menu-out');
      this.menuComponente.classList.add('menu-out');
      setTimeout(() => {
        this.showMenu = false;
      }, 450);
    } else {
      this.showMenu = true;
    }
  }

  receberMenuComponente(menuComponente: ElementRef) {
    this.menuComponente = menuComponente.nativeElement;    
  }
}
