import { Pessoa } from "./pessoa";

export class Checkin {
    id: number;
    hospede: Pessoa;
    adicionalVeiculo: true;
    dataEntrada: string | Date;
    dataSaida: string;
    valorAPagar: number;

    constructor() { }
}