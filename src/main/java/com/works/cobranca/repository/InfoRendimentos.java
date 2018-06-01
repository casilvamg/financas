package com.works.cobranca.repository;

//Repository – Repositórios do Spring Data JPA responsáveis pelo acesso a dados.

import org.springframework.data.jpa.repository.JpaRepository;

import com.works.cobranca.model.InfoRendimento;
import com.works.cobranca.model.Investimento;

public interface InfoRendimentos extends JpaRepository<InfoRendimento, Long> {

	public InfoRendimento findByInvestimento(Investimento investimento);
		
}