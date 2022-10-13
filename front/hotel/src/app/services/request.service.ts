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
      .post<any>(`${this.API_URL}chekin/`, checkin)
      .pipe(
        catchError(this.handleErrorPesquisa<Checkin[]>('buscarCheckin', []))
      )
  }

  salvarPessoa(pessoa: Pessoa) {
    return this.http
      .post<any>(`${this.API_URL}pessoa/`, pessoa)
      .pipe(
        catchError(this.handleErrorPesquisa<Checkin[]>('buscarCheckin', []))
      )
  }


  buscarPessoaById(id: number) {
    return this.http
      .get<any>(`${this.API_URL}pessoaById/${id}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  buscarPessoas(termoPesquisa: string) {
    return this.http
      .get<any>(`${this.API_URL}pessoa/${termoPesquisa}`)
      .pipe(
        catchError(this.handleError)
      )
  }

  buscarCheckin(id: number, dataSaida: string, dataEntrada: string,
    tipoPesquisa: string, inicio: number, quantidade: number) {
    return this.http
      .post<any>(`${this.API_URL}buscarChekin/`,
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
        `${this.API_URL}buscaTotalCheckinPaginado/`,
        {
          id,
          dataEntrada,
          dataSaida,
          tipoPesquisa
        },
        {
          headers,
          params: { responseType: 'text' }
        }
      )
      .pipe(
        catchError(this.handleErrorPesquisa<number>('buscaTotalCheckinPaginado', 0))
      )
  }

  private handleErrorPesquisa<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.log(error);
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else if (error.status === 404 || error.status === 400) {
      // A client-side or network error occurred. Handle it accordingly.
      console.log('Nao encontrado ::', error.error);
      return error.error;
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
