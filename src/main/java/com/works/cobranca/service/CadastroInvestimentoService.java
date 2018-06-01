package com.works.cobranca.service;

//Service – Classes de serviço e/ou negócio.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.works.cobranca.model.Investimento;
import com.works.cobranca.repository.Investimentos;
import com.works.cobranca.repository.filter.InvestimentoFilter;

@Service
public class CadastroInvestimentoService {

	@Autowired
	private Investimentos investimentos;
	
	public void salvar(Investimento investimento) {
		try {
			investimentos.save(investimento);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	public void excluir(Long codigo) {
		investimentos.delete(codigo);
	}
	
	public List<Investimento> listarTodos() {
		return investimentos.findAll(); 
	}
	
	public Investimento buscarByNome(String nome) {
		return investimentos.findByNome(nome);
	}
	
	public List<Investimento> filtrar(InvestimentoFilter filtro) {
		String descricao = filtro.getNome() == null ? "%" : filtro.getNome();
		return investimentos.findByNomeContaining(descricao);
	}
}