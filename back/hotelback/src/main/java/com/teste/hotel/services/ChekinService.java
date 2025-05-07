package com.teste.hotel.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.teste.hotel.domain.Checkin;
import com.teste.hotel.dto.CheckinDTO;
import com.teste.hotel.repository.ChekinRepository;
import com.teste.hotel.services.mapper.CheckinMapper;

@Service
public class ChekinService {

	private final Logger log = LoggerFactory.getLogger(ChekinService.class);

	private final ChekinRepository _checkinRepository;
	private final CheckinMapper checkinMapper;

	public ChekinService(ChekinRepository _checkinRepository, CheckinMapper checkinMapper) {
		this._checkinRepository = _checkinRepository;
		this.checkinMapper = checkinMapper;
	}

	public Checkin save(CheckinDTO chekin) {
		return _checkinRepository.save(checkinMapper.toEntity(chekin));
	}

	public List<Checkin> dataSaidaIsNull(PageRequest pageRequest) {
		var retorno = _checkinRepository.dataSaidaIsNull(pageRequest).getContent();
		log.debug(" dataSaidaIsNull retorno {}", log);
		return retorno;
	}

}
