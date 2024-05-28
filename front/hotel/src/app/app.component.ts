import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuComponent } from './componentes/menu/menu.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  title = 'hotel';  
  @ViewChild('menuComponente') menuComponente: MenuComponent;

}
