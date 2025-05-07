package com.teste.hotel.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BuscarChekinDto {

	private Long id;
	 
	private ZonedDateTime dataSaida;
	 
	private ZonedDateTime dataEntrada;
	
	private String  tipoPesquisa;
	
	private int inicio;
	
	private int quantidade;

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getTipoPesquisa() {
		return tipoPesquisa;
	}

	public void setTipoPesquisa(String tipoPesquisa) {
		this.tipoPesquisa = tipoPesquisa;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZonedDateTime getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(ZonedDateTime dataSaida) {
		this.dataSaida = dataSaida;
	}

	public ZonedDateTime getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(ZonedDateTime dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

}
