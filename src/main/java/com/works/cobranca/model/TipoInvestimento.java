package com.works.cobranca.model;

public enum TipoInvestimento {

	POUPANÇA("Poupança"),
	FUNDOS("Fundo de Investimento"),
	PREVIDENCIA("Previdência Privada"),
	CDB("CDB"), 
	AÇÃO("Ação"),
	DEBENTURE("Debênture"),
	TESOURO("Tesouro Direto"),
	TITULOS("Títulos Públicos"),
	LETRAS_("LCI e LCA");
	
	private String descricao;
	
	TipoInvestimento(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}