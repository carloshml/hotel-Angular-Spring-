package com.teste.hotel.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Checkin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5400166771653598696L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	@JoinColumn(name = "id_hospede")
	Hospede pessoa;

	@Column(nullable = false)
	boolean adicionalVeiculo;

	@Column(nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataEntrada;

	@Column(nullable = true)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataSaida;

	private BigDecimal valorAPagar;

	public Checkin() {
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

	public Hospede getPessoa() {
		return pessoa;
	}

	public void setPessoa(Hospede pessoa) {
		this.pessoa = pessoa;
	}

	public boolean getAdicionalVeiculo() {
		return adicionalVeiculo;
	}

	public void setAdicionalVeiculo(boolean adicionalVeiculo) {
		this.adicionalVeiculo = adicionalVeiculo;
	}

	public LocalDateTime getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDateTime dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDateTime getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDateTime dataSaida) {
		this.dataSaida = dataSaida;
	}

	public void calcularValorPago(List<Checkin> checkins) {
		
		BigDecimal vlDiariaDiasDaSemana = BigDecimal.valueOf(120.0);
		BigDecimal vlDiariaFimDeSemana = BigDecimal.valueOf(150.0);

		BigDecimal vladicionalVeiculoDiasDaSemana = BigDecimal.valueOf(15.0);
		BigDecimal vladicionalVeiculoFimDeSemana = BigDecimal.valueOf(20.0);
		
		Long daysBetween = Long.valueOf("0");
		for (Checkin checkin : checkins) {
			if (checkin.getDataSaida() != null) {
				daysBetween = Duration.between(checkin.getDataEntrada(), checkin.getDataSaida()).toDays();
			} else {
				daysBetween = Duration.between(checkin.getDataEntrada(), LocalDateTime.now()).toDays();
			}
			List<LocalDateTime> diasNoHotel = new ArrayList<>();
			if(daysBetween>0) {
				  diasNoHotel = IntStream.iterate(0, i -> i + 1).limit(daysBetween)
						.mapToObj(i -> checkin.getDataEntrada().plusDays(i)).collect(Collectors.toList());	
			}
			
			 
			for (LocalDateTime checkin2 : diasNoHotel) {
			

				// • Uma diária no hotel de segunda à sexta custa R$120,00;
				// • Uma diária no hotel em finais de semana custa R$150,00;
				// • Caso a pessoa precise de uma vaga na garagem do hotel há um acréscimo
				// diário, sendo R$15,00 de segunda à sexta e R$20,00 nos finais de semana;
				// • Caso o horário da saída seja após às 16:30h deve ser cobrada uma diária
				// extra.
				
				
				if(finalDeSemana(checkin2)) {
					 
					checkin.setValorAPagar(checkin.getValorAPagar().add(vlDiariaFimDeSemana));
					if (checkin.getAdicionalVeiculo()) {
						checkin.setValorAPagar(checkin.getValorAPagar().add(vladicionalVeiculoFimDeSemana));
					}
				}else {
					 
					checkin.setValorAPagar(checkin.getValorAPagar().add(vlDiariaDiasDaSemana));
					if (checkin.getAdicionalVeiculo()) {
						checkin.setValorAPagar(checkin.getValorAPagar().add(vladicionalVeiculoDiasDaSemana));
					}
				}

				 
			}

			LocalDateTime ultimoDia = checkins.get(checkins.size() - 1).getDataSaida();
			if (ultimoDia == null) {
				ultimoDia = LocalDateTime.now();
			}
			
			 

			if (ultimoDia.getHour() >= 16 && ultimoDia.getMinute() > 30) {
				if(finalDeSemana(ultimoDia)) {				 
					checkin.setValorAPagar(checkin.getValorAPagar().add(vlDiariaFimDeSemana));					 
				}else {			 
					checkin.setValorAPagar(checkin.getValorAPagar().add(vlDiariaDiasDaSemana));				 
				}

			}
		}
	}

	private boolean finalDeSemana(LocalDateTime checkin2) {
		switch (checkin2.getDayOfWeek().toString()) {
		case "SUNDAY":
		case "SATURDAY":
			return  true ;
		default:
			return false ;
		}
	}

}
