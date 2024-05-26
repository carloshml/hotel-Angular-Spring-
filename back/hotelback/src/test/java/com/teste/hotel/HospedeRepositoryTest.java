package com.teste.hotel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.teste.hotel.entities.Hospede;
import com.teste.hotel.repository.PessoaRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
class HospedeRepositoryTest {

	@Autowired
	PessoaRepository pessoaRepository;

	@Autowired
	EntityManager entityManager;

	@Test
	@DisplayName("Should return an hospede sucessfully ")
	void findHospedeByDocumentoSucess() {
		String documento = "668.738.670-04";
		Hospede hospede = new Hospede("Usuario Teste", "668.738.670-04", "6399156868", "obs. teste");
		createHospede(hospede);
		Optional<Hospede> result = this.pessoaRepository.findByDocumento(documento);
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("Should get a error when  return an  hospede  ")
	void findHospedeByDocumentoError() {
		String documento = "205.881.120-89";		 
		Optional<Hospede> result = this.pessoaRepository.findByDocumento(documento);
		assertThat(result.isEmpty()).isTrue();
	}

	Hospede createHospede(Hospede hospede) {
		this.entityManager.persist(hospede);
		return hospede;
	}

}
