package com.works.cobranca.repository;

import java.math.BigDecimal;

//Repository – Repositórios do Spring Data JPA responsáveis pelo acesso a dados.

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.works.cobranca.model.SituacaoTitulo;
import com.works.cobranca.model.StatusTitulo;
import com.works.cobranca.model.Titulo;

public interface Titulos extends JpaRepository<Titulo, Long> {

	public List<Titulo> findByDescricaoContaining(String descricao);	
	public List<Titulo> findAllByOrderByDataVencimentoAsc();	
	public Page<Titulo> findByDescricaoContainingOrderByDataVencimentoAsc(String descricao, Pageable pageable);
	
	@Query("select sum(valor) from Titulo where situacao = :situacao")
    public BigDecimal sumValorPorSituacao(@Param("situacao") SituacaoTitulo situacao);
	
	@Query("select sum(valor) from Titulo where situacao = :situacao and status = :status")
    public BigDecimal sumValorPorSituacaoAndStatus(@Param("situacao") SituacaoTitulo situacao, @Param("status") StatusTitulo status);
	
}