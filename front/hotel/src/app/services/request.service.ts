import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of, throwError } from 'rxjs';
import { Checkin } from '../entities/checkin';
import { Pessoa } from '../entities/pessoa';

@Injectable({
  providedIn: 'root'
})
export class RequestService {



  API_URL = `http://localhost:8080/`;

  constructor(private http: HttpClient) { }


  salvarCheckin(checkin: Checkin) {
    return this.http
      .post<any>(`${this.API_URL}chekin`, checkin)
      .pipe(
        catchError(this.handleErrorPesquisa<Checkin>('salvarCheckin', new Checkin()))
      )
  }

  salvarPessoa(pessoa: Pessoa) {
    return this.http
      .post<any>(`${this.API_URL}hospede`, pessoa)
      .pipe(
        catchError(this.handleErrorPesquisa<Pessoa>('salvarPessoa', new Pessoa()))
      )
  }


  buscarPessoas(termoPesquisa: string) {
    return this.http
      .get<any>(`${this.API_URL}hospede/${termoPesquisa}`)
      .pipe(
        catchError(this.handleErrorPesquisa<Pessoa[]>('buscarPessoas', []))
      )
  }

  buscarCheckin(id: number, dataSaida: string, dataEntrada: string,
    tipoPesquisa: string, inicio: number, quantidade: number) {
    return this.http
      .post<any>(`${this.API_URL}buscarChekin`,
        {
          id,
          dataEntrada,
          dataSaida,
          tipoPesquisa,
          inicio,
          quantidade
        }
      )
      .pipe(
        catchError(this.handleErrorPesquisa<Checkin[]>('buscarCheckin', []))
      )
  }

  buscaTotalCheckinPaginado(id: number, dataSaida: string, dataEntrada: string,
    tipoPesquisa: string) {


    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http
      .post<number>(
        `${this.API_URL}buscaTotalCheckinPaginado`,
        {
          id,
          dataEntrada,
          dataSaida,
          tipoPesquisa
        },
        {
          headers 
        }
      )
      .pipe(
        catchError(this.handleErrorPesquisa<number>('buscaTotalCheckinPaginado', 0))
      )
  }

  private handleErrorPesquisa<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {      
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

 
}
