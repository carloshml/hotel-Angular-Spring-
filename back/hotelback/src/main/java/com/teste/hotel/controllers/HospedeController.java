package com.teste.hotel.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.hotel.commun.Util;
import com.teste.hotel.entities.Hospede;
import com.teste.hotel.repository.PessoaRepository;

import jakarta.validation.Valid;

@RestController
public class HospedeController {
	@Autowired
	private PessoaRepository _pessoaRepository;

	@RequestMapping(value = "/hospede", method = RequestMethod.GET)
	public List<Hospede> Get() {
		return _pessoaRepository.findAll();
	}

	@RequestMapping(value = "/hospedeById/{id}", method = RequestMethod.GET)
	public ResponseEntity<Hospede> GetById(@PathVariable(value = "id") long id) {
		Optional<Hospede> pessoa = _pessoaRepository.findById(id);
		if (pessoa.isPresent())
			return new ResponseEntity<Hospede>(pessoa.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/hospede", method = RequestMethod.POST)
	public ResponseEntity<Hospede> Post(@Valid @RequestBody Hospede hospede) {
		hospede.setNome(hospede.getNome().toUpperCase());
		try {
			_pessoaRepository.save(hospede);
			return new ResponseEntity<Hospede>(hospede, HttpStatus.OK);
		} catch (Exception e) {
			hospede.setMsgDetalhe(new Util().getCauseMessage(e.getCause()));
			return new ResponseEntity<>(hospede, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/hospede/{termoPesquisa}", method = RequestMethod.GET)
	public ResponseEntity<List<Hospede>> buscarHospedes(@PathVariable(value = "termoPesquisa") String termoPesquisa) {

		PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "nome");
		List<Hospede> hospedes = _pessoaRepository.findAllDataSaidaAnterior("%" + termoPesquisa + "%", pageRequest);
		if (hospedes.isEmpty()) {
			return new ResponseEntity<List<Hospede>>(hospedes, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Hospede>>(hospedes, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/hospede/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Hospede> Put(@PathVariable(value = "id") long id, @Valid @RequestBody Hospede newPessoa) {
		Optional<Hospede> oldPessoa = _pessoaRepository.findById(id);
		if (oldPessoa.isPresent()) {
			Hospede pessoa = oldPessoa.get();
			pessoa.setNome(newPessoa.getNome());
			_pessoaRepository.save(pessoa);
			return new ResponseEntity<Hospede>(pessoa, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/hospede/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id) {
		Optional<Hospede> pessoa = _pessoaRepository.findById(id);
		if (pessoa.isPresent()) {
			_pessoaRepository.delete(pessoa.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
