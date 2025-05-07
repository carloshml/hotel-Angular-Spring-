package com.teste.hotel.services.mapper;

import org.mapstruct.Mapper;

import com.teste.hotel.domain.Checkin;
import com.teste.hotel.dto.CheckinDTO;

@Mapper(componentModel = "spring")
public interface CheckinMapper extends EntityMapper<CheckinDTO, Checkin> {
}