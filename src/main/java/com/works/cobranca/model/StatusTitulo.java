package com.works.cobranca.model;

public enum StatusTitulo {

	PENDENTE("Pendente"),
	RECEBIDO("Finalizado");
	
	private String descricao;
	
	StatusTitulo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}