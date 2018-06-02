package com.works.cobranca.repository;

//Repository – Repositórios do Spring Data JPA responsáveis pelo acesso a dados.

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.works.cobranca.model.Titulo;

public interface Titulos extends JpaRepository<Titulo, Long> {

	public List<Titulo> findByDescricaoContaining(String descricao);	
	public List<Titulo> findAllByOrderByDataVencimentoAsc();	
	public Page<Titulo> findByDescricaoContainingOrderByDataVencimentoAsc(String descricao, Pageable pageable);
	
}