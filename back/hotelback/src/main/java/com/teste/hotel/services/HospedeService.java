package com.teste.hotel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.teste.hotel.domain.Hospede;
import com.teste.hotel.repository.PessoaRepository;

@Service
public class HospedeService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Hospede> findAll() {
		return pessoaRepository.findAll();
	}

	public Optional<Hospede> findById(long id) {
		return pessoaRepository.findById(id);
	}

	public Optional<List<Hospede>> findAllByTermoPesquisa(String termoPesquisa, PageRequest pageRequest) {
		return pessoaRepository.findAllByTermoPesquisa(termoPesquisa.toUpperCase(), pageRequest);
	}

	public void delete(Hospede hospede) {
		pessoaRepository.delete(hospede);
	}

	public Hospede save(Hospede pessoa) {
		return pessoaRepository.save(pessoa);

	}

}
