package com.works.cobranca.service;

//Service – Classes de serviço e/ou negócio.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.works.cobranca.model.Investimento;
import com.works.cobranca.model.Rendimento;
import com.works.cobranca.repository.Rendimentos;

@Service
public class CadastroRendimentoService {

	@Autowired
	private Rendimentos rendimentos;
	
	public void salvar(Rendimento rendimento) {		
		
		try {
			rendimentos.save(rendimento);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	public void excluir(Long codigo) {
		rendimentos.delete(codigo);
	}
	
	public List<Rendimento> listarTodos() {
		return rendimentos.findAll(); 
	}
	
	public List<Rendimento> listarTodosPorInvestimento(Investimento investimento) {
		return rendimentos.findByInvestimento(investimento);
	}
}
