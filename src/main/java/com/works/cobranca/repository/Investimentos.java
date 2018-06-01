package com.works.cobranca.repository;

//Repository – Repositórios do Spring Data JPA responsáveis pelo acesso a dados.

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.works.cobranca.model.Investimento;

public interface Investimentos extends JpaRepository<Investimento, Long> {

	public List<Investimento> findByNomeContaining(String nome);
	public Investimento findByNome(String nome);
	
}