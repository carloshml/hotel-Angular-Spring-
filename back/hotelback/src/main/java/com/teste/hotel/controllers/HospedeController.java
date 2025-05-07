package com.teste.hotel.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.hotel.commun.Util;
import com.teste.hotel.domain.Hospede;
import com.teste.hotel.services.HospedeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
public class HospedeController {
	@Autowired
	private HospedeService hospedeService;

	@GetMapping("/hospede/all")
	public List<Hospede> Get() {
		return hospedeService.findAll();
	}

	@GetMapping("/hospedeById/{id}")
	public ResponseEntity<Hospede> GetById(@PathVariable(value = "id") long id) {
		Optional<Hospede> pessoa = hospedeService.findById(id);
		if (pessoa.isPresent())
			return new ResponseEntity<Hospede>(pessoa.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/hospede")
	public ResponseEntity<Hospede> Post(@Valid @RequestBody Hospede hospede) {
		try {
			hospedeService.save(hospede);
			return new ResponseEntity<Hospede>(hospede, HttpStatus.OK);
		} catch (Exception e) {
			hospede.setMsgDetalhe(new Util().getCauseMessage(e.getCause()));
			return new ResponseEntity<>(hospede, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/hospede/{termoPesquisa}", method = RequestMethod.GET)
	public ResponseEntity<List<Hospede>> buscarHospedes(
			@NotNull @NotBlank @PathVariable(value = "termoPesquisa") String termoPesquisa) {

		PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.DESC, "nome");
		var hospedes = hospedeService.findAllByTermoPesquisa("%" + termoPesquisa + "%", pageRequest);
		if (hospedes.isEmpty()) {
			return new ResponseEntity(hospedes, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity(hospedes, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/hospede/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Hospede> Put(@PathVariable(value = "id") long id, @Valid @RequestBody Hospede newPessoa) {
		Optional<Hospede> oldPessoa = hospedeService.findById(id);
		if (oldPessoa.isPresent()) {
			Hospede pessoa = oldPessoa.get();
			pessoa.setNome(newPessoa.getNome());
			hospedeService.save(pessoa);
			return new ResponseEntity<Hospede>(pessoa, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/hospede/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id) {
		Optional<Hospede> pessoa = hospedeService.findById(id);
		if (pessoa.isPresent()) {
			hospedeService.delete(pessoa.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
