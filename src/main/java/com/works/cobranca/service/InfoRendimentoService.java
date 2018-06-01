package com.works.cobranca.service;

//Service – Classes de serviço e/ou negócio.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.works.cobranca.model.InfoRendimento;
import com.works.cobranca.model.Investimento;
import com.works.cobranca.repository.InfoRendimentos;

@Service
public class InfoRendimentoService {

	@Autowired
	private InfoRendimentos infoRendimentos;
	
	public void salvar(InfoRendimento infoRendimento) {
		try {
			infoRendimentos.save(infoRendimento);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Salvar InfoRendimento falhou");
		}
	}

	public void excluir(InfoRendimento infoRendimento) {
		infoRendimentos.delete(infoRendimento);
	}
	
	public InfoRendimento buscarPorInvestimento(Investimento investimento) {
		return infoRendimentos.findByInvestimento(investimento);
	}
	
	
	//public List<InfoRendimentos> listarTodosPorInvestimento(Investimento investimento) {
		//return  infoRendimentos.
	//}
}
