package com.teste.hotel.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

@Entity
public class Hospede implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 248500367154437579L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	@Size(min = 1, message = "Nome deve ser informado")
	private String nome;

	@Column(unique = true,  nullable = false)
	private String documento;

	@Column(nullable = false)
	private String telefone;

	private String msgDetalhe;
	
	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getMsgDetalhe() {
		return msgDetalhe;
	}

	public void setMsgDetalhe(String msgDetalhe) {
		this.msgDetalhe = msgDetalhe;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
