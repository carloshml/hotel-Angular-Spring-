package com.teste.hotel.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teste.hotel.entities.Checkin;

@Repository
public interface ChekinRepository extends JpaRepository<Checkin, Long> {

	List<Checkin> findAllByDataEntrada(LocalDateTime dataEntrada);

	@Query("select a from Checkin a where a.dataEntrada < :d1")
	List<Checkin> findAllDataEntradaAntes(@Param("d1") LocalDateTime d1);
	
	@Query("select a from Checkin a where a.dataEntrada > :d1")
	List<Checkin> findAllDataEntradaDepois(@Param("d1") LocalDateTime d1);
	
	List<Checkin> findAllByDataEntradaBetween(LocalDateTime d1, LocalDateTime d2);
	
	@Query("select a from Checkin a where a.dataSaida = :d1")
	List<Checkin> findAllDataSaidaAnterior(@Param("d1") LocalDateTime d1);
	
	List<Checkin> dataSaidaIsNull();
	
	List<Checkin> dataSaidaNotNull();
}
