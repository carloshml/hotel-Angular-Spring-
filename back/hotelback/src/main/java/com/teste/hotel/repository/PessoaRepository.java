package com.teste.hotel.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teste.hotel.domain.Hospede;

 

@Repository
public interface PessoaRepository extends JpaRepository<Hospede, Long> {
	
	@Query("select a from Hospede a where a.nome like :termoPesquisa  OR  a.documento like :termoPesquisa  OR  a.telefone like :termoPesquisa   ")
	Optional<List<Hospede>> findAllByTermoPesquisa(String termoPesquisa, PageRequest pageRequest);
	
	Optional<Hospede> findByDocumento(String documento);
}

