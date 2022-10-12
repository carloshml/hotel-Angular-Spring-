import { Pessoa } from "./pessoa";

export class Checkin {
    id: number;
    pessoa: Pessoa;
    adicionalVeiculo: true;
    dataEntrada: string;
    dataSaida: string;
    valorAPagar: number;

    constructor(){}
}