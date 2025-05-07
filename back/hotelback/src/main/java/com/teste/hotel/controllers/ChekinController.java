package com.teste.hotel.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.teste.hotel.commun.Util;
import com.teste.hotel.domain.Checkin;
import com.teste.hotel.dto.BuscarChekinDto;
import com.teste.hotel.dto.CheckinDTO;
import com.teste.hotel.repository.ChekinRepository;
import com.teste.hotel.services.ChekinService;

import jakarta.validation.Valid;

@RestController
public class ChekinController {

	private final Logger log = LoggerFactory.getLogger(ChekinController.class);

	@Autowired
	private ChekinRepository _checkinRepository;

	private final ChekinService checkinService;

	public ChekinController(ChekinService checkinService) {
		this.checkinService = checkinService;
	}

	@RequestMapping(value = "/chekin", method = RequestMethod.GET)
	public List<Checkin> Get() {
		return _checkinRepository.findAll();
	}

	@RequestMapping(value = "/chekin/{id}", method = RequestMethod.GET)
	public ResponseEntity<Checkin> GetById(@PathVariable(value = "id") long id) {
		Optional<Checkin> pessoa = _checkinRepository.findById(id);
		if (pessoa.isPresent())
			return new ResponseEntity<Checkin>(pessoa.get(), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/buscaTotalCheckinPaginado")
	public ResponseEntity<Long> buscaTotalCheckinPaginado(@RequestBody BuscarChekinDto buscarChekinDto) {

		Long checkins = Long.valueOf("0");

		switch (buscarChekinDto.getTipoPesquisa()) {
			case "DATASAIDAISNULL":
				checkins = _checkinRepository.dataSaidaIsNullPaginado();
				break;
			case "DATASAIDANOTNULL":
				checkins = _checkinRepository.dataSaidaNotNullPaginado();
				break;

			default:
				break;
		}

		if (checkins == 0) {
			return new ResponseEntity<Long>(checkins, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Long>(checkins, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/buscarChekin", method = RequestMethod.POST)
	public ResponseEntity<List<Checkin>> buscarChekin(@RequestBody BuscarChekinDto buscarChekinDto) {
		PageRequest pageRequest = PageRequest.of(
				buscarChekinDto.getInicio(),
				buscarChekinDto.getQuantidade(),
				Sort.Direction.DESC,
				"dataEntrada");
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
				checkins = checkinService.dataSaidaIsNull(pageRequest);
				break;
			case "DATASAIDANOTNULL":
				checkins = _checkinRepository.dataSaidaNotNull(pageRequest).getContent();
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
	public ResponseEntity<Checkin> Post(@Valid @RequestBody CheckinDTO checkin) {
		log.debug(" checkin save {}", checkin);
		try {
			return new ResponseEntity<>(checkinService.save(checkin), HttpStatus.OK);
		} catch (Exception e) {
			log.error(" checkin save error: {}", e);
			checkin.setMsgDetalhe(new Util().getCauseMessage(e.getCause()));
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/chekin/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Checkin> Put(@PathVariable(value = "id") long id, @Valid @RequestBody Checkin novo) {
		Optional<Checkin> oldPessoa = _checkinRepository.findById(id);
		if (oldPessoa.isPresent()) {
			Checkin antigo = oldPessoa.get();
			antigo.setDataEntrada(novo.getDataEntrada());
			antigo.setDataSaida(novo.getDataSaida());
			antigo.setHospede(novo.getHospede());
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
