package com.works.cobranca.repository;

import java.util.List;



// Repository – Repositórios do Spring Data JPA responsáveis pelo acesso a dados.
// Padrão repository contém apenas código de persistencia, não lógica de negócio. 
// Essa padrão gera operações CRUD e consultas customizadas baseadas na interface e assinatura dos métodos.
// Servir de abstração para persistir os dados.


import org.springframework.data.jpa.repository.JpaRepository;

import com.works.cobranca.model.Investimento;
import com.works.cobranca.model.Rendimento;

public interface Rendimentos extends JpaRepository<Rendimento, Long> {

	public List<Rendimento> findByInvestimento(Investimento investimento);
	
}