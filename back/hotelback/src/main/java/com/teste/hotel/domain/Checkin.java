package com.teste.hotel.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "checkin")
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
	Hospede hospede;

	@Column(nullable = false)
	boolean adicionalVeiculo;

	@Column(nullable = false) 
	private ZonedDateTime dataEntrada;

	@Column(nullable = true) 
	private ZonedDateTime dataSaida;

	private BigDecimal valorAPagar;

	private String msgDetalhe;

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
		System.out.println("  get :: dataEntrada :::   " + dataEntrada);
		return dataEntrada;
	}

	public void setDataEntrada(ZonedDateTime dataEntrada) {
		System.out.println("   dataEntrada  "+ dataEntrada);
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

	public void calcularValorPago(List<Checkin> checkins) {

		BigDecimal vlDiariaDiasDaSemana = BigDecimal.valueOf(120.0);
		BigDecimal vlDiariaFimDeSemana = BigDecimal.valueOf(150.0);

		BigDecimal vladicionalVeiculoDiasDaSemana = BigDecimal.valueOf(15.0);
		BigDecimal vladicionalVeiculoFimDeSemana = BigDecimal.valueOf(20.0);

		Long daysBetween = Long.valueOf("0");
		for (Checkin checkin : checkins) {

			Checkin checkinCalcula = new Checkin();

			checkinCalcula.setDataEntrada(checkin.getDataEntrada());
			checkinCalcula.setDataSaida(checkin.getDataSaida());

			checkinCalcula.setDataEntrada(checkinCalcula.getDataEntrada().withHour(0).withMinute(0).withSecond(0));

			if (checkinCalcula.getDataSaida() != null) {
				daysBetween = Duration.between(checkinCalcula.getDataEntrada(), checkinCalcula.getDataSaida()).toDays();
			} else {
				daysBetween = Duration.between(checkinCalcula.getDataEntrada(), ZonedDateTime.now()).toDays();
			}
			List<ZonedDateTime> diasNoHotel = new ArrayList<>();
			if (daysBetween > 0) {
				diasNoHotel = IntStream.iterate(0, i -> i + 1).limit(daysBetween)
						.mapToObj(i -> checkinCalcula.getDataEntrada().plusDays(i)).collect(Collectors.toList());
			}

			for (ZonedDateTime checkin2 : diasNoHotel) {

				// • Uma diária no hotel de segunda à sexta custa R$120,00;
				// • Uma diária no hotel em finais de semana custa R$150,00;
				// • Caso a pessoa precise de uma vaga na garagem do hotel há um acréscimo
				// diário, sendo R$15,00 de segunda à sexta e R$20,00 nos finais de semana;
				// • Caso o horário da saída seja após às 16:30h deve ser cobrada uma diária
				// extra.

				if (finalDeSemana(checkin2)) {
					checkin.setValorAPagar(checkin.getValorAPagar().add(vlDiariaFimDeSemana));
					if (checkinCalcula.getAdicionalVeiculo()) {
						checkin.setValorAPagar(checkin.getValorAPagar().add(vladicionalVeiculoFimDeSemana));
					}
				} else {
					checkin.setValorAPagar(checkinCalcula.getValorAPagar().add(vlDiariaDiasDaSemana));
					if (checkinCalcula.getAdicionalVeiculo()) {
						checkin.setValorAPagar(checkin.getValorAPagar().add(vladicionalVeiculoDiasDaSemana));
					}
				}

			}

			ZonedDateTime ultimoDia = checkins.get(checkins.size() - 1).getDataSaida();
			if (ultimoDia == null) {
				ultimoDia = ZonedDateTime.now();
			}

			if (ultimoDia.getHour() >= 16 && ultimoDia.getMinute() > 30) {
				if (finalDeSemana(ultimoDia)) {
					checkinCalcula.setValorAPagar(checkinCalcula.getValorAPagar().add(vlDiariaFimDeSemana));
				} else {
					checkinCalcula.setValorAPagar(checkinCalcula.getValorAPagar().add(vlDiariaDiasDaSemana));
				}

			}
		}
	}

	private boolean finalDeSemana(ZonedDateTime checkin2) {
		switch (checkin2.getDayOfWeek().toString()) {
			case "SUNDAY":
			case "SATURDAY":
				return true;
			default:
				return false;
		}
	}

	@Override
	public String toString() {
		return "Checkin [id=" + id + ", hospede=" + hospede + ", adicionalVeiculo=" + adicionalVeiculo
				+ ", dataEntrada=" + dataEntrada + ", dataSaida=" + dataSaida + ", valorAPagar=" + valorAPagar
				+ ", msgDetalhe=" + msgDetalhe + "]";
	}

	

}
