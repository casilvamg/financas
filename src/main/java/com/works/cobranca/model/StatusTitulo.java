package com.works.cobranca.model;

public enum StatusTitulo {

	PENDENTE("Pendente"),
	CANCELADO("Cancelado"),
	RECEBIDO("Finalizado");
	
	private String descricao;
	
	StatusTitulo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}