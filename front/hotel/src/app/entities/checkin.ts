import { Pessoa } from "./pessoa";

export class Checkin {
    id: number;
    hospede: Pessoa;
    adicionalVeiculo: true;
    dataEntrada: string;
    dataSaida: string;
    valorAPagar: number;

    constructor(){}
}