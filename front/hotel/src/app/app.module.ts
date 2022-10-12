import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { ReactiveFormsModule } from '@angular/forms';
import { PessoaCadastroComponent } from './pessoa-cadastro/pessoa-cadastro.component';
import { HttpClientModule } from '@angular/common/http';
import { RequestService } from './services/request.service';
import { BuscaPessoasComponent } from './componentes/busca-pessoas/busca-pessoas.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    PessoaCadastroComponent,
    BuscaPessoasComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    RequestService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }