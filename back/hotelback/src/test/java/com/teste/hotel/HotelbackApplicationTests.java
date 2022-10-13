package com.teste.hotel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.teste.hotel.entities.Checkin;

@SpringBootTest
class HotelbackApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testaValorZerado() {

		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		checkin.setDataEntrada(LocalDateTime.now());
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(checkins.get(0).getValorAPagar(), BigDecimal.ZERO);
	}

	@Test
	public void testaValorDiariaDiaDaSemanaSemVeiculo() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 5, 20, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 6, 00, 00, 00);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals( BigDecimal.valueOf(120.0), checkins.get(0).getValorAPagar());
	}
	
	@Test
	public void testaValorDiariaFimDeSemanaSemVeiculo() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 15, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 16, 12, 30, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(150.0), checkins.get(0).getValorAPagar() );
	}
	
	@Test
	public void testaValorDiariaDiaDaSemanaComVeiculo() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		checkin.setAdicionalVeiculo(true);
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 5, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 6, 12, 30, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(135.0), checkins.get(0).getValorAPagar() );
	}
	
	@Test
	public void testaValorDiariaFimDeSemanaComVeiculo() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		checkin.setAdicionalVeiculo(true);
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 15, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 16, 12, 30, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(170.0), checkins.get(0).getValorAPagar() );
	}
	
	@Test
	public void testaValorDiaria2MistoDiasSemVeiculo() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 14, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 16, 12, 30, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(270.0), checkins.get(0).getValorAPagar() );
	}
	/**
	 * Contendo um dia na semana e um dia no fim de semana saida depois da 16:30
	 * */
	@Test
	public void testaValorDiaria2DiasMistoSemVeiculoDepois16_30() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 14, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 16, 16, 31, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(420.0), checkins.get(0).getValorAPagar() );
	}
	
	/**
	 * Contendo um dia na semana e um dia no fim de semana saida depois da 16:30
	 * */
	@Test
	public void testaValorDiaria2SemanaSemVeiculoDepois16_30() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 12, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 14, 16, 31, 40);

		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(360.0), checkins.get(0).getValorAPagar() );
	}
	
	/**
	 * Contendo 2 dias na semana  saida depois da 16:30 e usando carro
	 * */
	@Test
	public void testaValorDiaria2SemanaComVeiculoDepois16_30() {
		List<Checkin> checkins = new ArrayList<>();
		Checkin checkin = new Checkin();
		LocalDateTime entrada = LocalDateTime.of(2022, Month.OCTOBER, 12, 8, 30, 40);
		LocalDateTime saida = LocalDateTime.of(2022, Month.OCTOBER, 14, 16, 31, 40);
		checkin.setAdicionalVeiculo(true);
		checkin.setDataEntrada(entrada);
		checkin.setDataSaida(saida);
		checkins.add(checkin);
		new Checkin().calcularValorPago(checkins);
		assertEquals(BigDecimal.valueOf(390.0), checkins.get(0).getValorAPagar() );
	}
	

}
