package com.teste.hotel.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.teste.hotel.domain.Hospede;

public class CheckinDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5400166771653598696L;

    private long id;

    Hospede hospede;

    boolean adicionalVeiculo;

    private ZonedDateTime dataEntrada;

    private ZonedDateTime dataSaida;

    private BigDecimal valorAPagar;

    private String msgDetalhe;

    public CheckinDTO() {
        this.valorAPagar = BigDecimal.ZERO;
    }

    public BigDecimal getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Hospede getHospede() {
        return hospede;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public boolean getAdicionalVeiculo() {
        return adicionalVeiculo;
    }

    public void setAdicionalVeiculo(boolean adicionalVeiculo) {
        this.adicionalVeiculo = adicionalVeiculo;
    }

    public ZonedDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(ZonedDateTime dataEntrada) {
        System.out.println(" DTO  dataEntrada :::   " + dataEntrada);
        this.dataEntrada = dataEntrada;
    }

    public ZonedDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(ZonedDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getMsgDetalhe() {
        return msgDetalhe;
    }

    public void setMsgDetalhe(String msgDetalhe) {
        this.msgDetalhe = msgDetalhe;
    }

}
