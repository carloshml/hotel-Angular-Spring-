package com.teste.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teste.hotel.entities.Hospede;

@Repository
public interface PessoaRepository extends JpaRepository<Hospede, Long> {
	
	@Query("select a from Hospede a where a.nome like :termoPesquisa  OR  a.documento like :termoPesquisa  OR  a.telefone like :termoPesquisa   ")
	List<Hospede> findAllDataSaidaAnterior(@Param("termoPesquisa") String termoPesquisa);
}

