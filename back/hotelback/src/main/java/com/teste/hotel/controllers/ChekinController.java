package com.teste.hotel.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teste.hotel.dto.BuscarChekinDto;
import com.teste.hotel.entities.Checkin;
import com.teste.hotel.repository.ChekinRepository;

@RestController
public class ChekinController {
	@Autowired
	private ChekinRepository _checkinRepository;

	@RequestMapping(value = "/chekin", method = RequestMethod.GET)
	public List<Checkin> Get() {
		return _checkinRepository.findAll();
	}

	@RequestMapping(value = "/chekin/{id}", method = RequestMethod.GET)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Checkin> GetById(@PathVariable(value = "id") long id) {
		Optional<Checkin> pessoa = _checkinRepository.findById(id);
		if (pessoa.isPresent())
			return new ResponseEntity<Checkin>(pessoa.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/buscarChekin", method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<List<Checkin>> buscarChekin(@RequestBody BuscarChekinDto buscarChekinDto) {

		List<Checkin> checkins = new ArrayList<>();

		switch (buscarChekinDto.getTipoPesquisa()) {
		case "MAIORQ":
			checkins = _checkinRepository.findAllDataEntradaDepois(buscarChekinDto.getDataEntrada());
			break;
		case "MENORQ":
			checkins = _checkinRepository.findAllDataEntradaAntes(buscarChekinDto.getDataEntrada());
			break;
		case "ENTRE":
			checkins = _checkinRepository.findAllByDataEntradaBetween(buscarChekinDto.getDataEntrada(),
					buscarChekinDto.getDataSaida());
			break;
		case "DATASAIDAISNULL":
			checkins = _checkinRepository.dataSaidaIsNull();
			break;
		case "DATASAIDANOTNULL":
			checkins = _checkinRepository.dataSaidaNotNull();
			break;

		default:
			break;
		}
		if (checkins.isEmpty()) {
			return new ResponseEntity<List<Checkin>>(checkins, HttpStatus.NOT_FOUND);
		} else {
			try {
				new Checkin().calcularValorPago(checkins);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return new ResponseEntity<List<Checkin>>(checkins, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/chekin", method = RequestMethod.POST)
	@CrossOrigin(origins = "http://localhost:4200")
	public Checkin Post(@Valid @RequestBody Checkin checkin) {
		return _checkinRepository.save(checkin);
	}

	@RequestMapping(value = "/chekin/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Checkin> Put(@PathVariable(value = "id") long id, @Valid @RequestBody Checkin novo) {
		Optional<Checkin> oldPessoa = _checkinRepository.findById(id);
		if (oldPessoa.isPresent()) {
			Checkin antigo = oldPessoa.get();
			antigo.setDataEntrada(novo.getDataEntrada());
			antigo.setDataSaida(novo.getDataSaida());
			antigo.setPessoa(novo.getPessoa());
			antigo.setAdicionalVeiculo(false);
			_checkinRepository.save(antigo);
			return new ResponseEntity<Checkin>(antigo, HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = "/chekin/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id) {
		Optional<Checkin> pessoa = _checkinRepository.findById(id);
		if (pessoa.isPresent()) {
			_checkinRepository.delete(pessoa.get());
			return new ResponseEntity<>(HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
