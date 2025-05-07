package com.teste.hotel.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teste.hotel.domain.Checkin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Repository
public interface ChekinRepository extends JpaRepository<Checkin, Long> {

	List<Checkin> findAllByDataEntrada(ZonedDateTime dataEntrada);

	@Query("select a from Checkin a where a.dataEntrada < :d1")
	List<Checkin> findAllDataEntradaAntes(@Param("d1") ZonedDateTime d1);

	@Query("select a from Checkin a where a.dataEntrada > :d1")
	List<Checkin> findAllDataEntradaDepois(@Param("d1") ZonedDateTime d1);

	List<Checkin> findAllByDataEntradaBetween(ZonedDateTime d1, ZonedDateTime d2);

	@Query("select a from Checkin a where a.dataSaida = :d1")
	List<Checkin> findAllDataSaidaAnterior(@Param("d1") ZonedDateTime d1);

	Page<Checkin> dataSaidaIsNull(PageRequest pageRequest);

	@Query("select count(*) from Checkin a where a.dataSaida IS NULL ")
	Long dataSaidaIsNullPaginado();

	Page<Checkin> dataSaidaNotNull(PageRequest pageRequeste);

	@Query("select count(*) from Checkin a where a.dataSaida IS NOT NULL ")
	Long dataSaidaNotNullPaginado();
}
