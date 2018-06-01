package com.works.cobranca.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.NumberFormat;

@Entity
public class InfoRendimento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// bi-directional many-to-one association to Site
	@ManyToOne
	@JoinColumn(name = "codigo")
	private Investimento investimento;
	
	@NotNull(message = "Rendimento Bruto é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal rendBruto;
	
	@NotNull(message = "IR é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal IR;
	
	@NotNull(message = "Saldo Bruto é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal saldoBruto;
	
	@NotNull(message = "Valor Resgate é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorResgate;
	
	@NotNull(message = "Rendimento Porcentagem é obrigatório")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal rendPerc;
	
	@Transient
	private String previsaoResgate;

	public String getPrevisaoResgate() {
		DecimalFormat decFormat = new java.text.DecimalFormat("#,###,##0.00");
		BigDecimal ret = null;
				
		if (this.getInvestimento().getTipo().getDescricao().equalsIgnoreCase("Fundo de Investimento")) {
			ret= this.getRendBruto().subtract(this.getRendBruto().multiply(new BigDecimal(17.5)).divide(new BigDecimal(100)));
			return decFormat.format(ret);
		}
		else {
			ret = this.getRendBruto();
			return decFormat.format(ret);	
		}
	}

	public void setPrevisaoResgate(String previsaoResgate) {
		this.previsaoResgate = previsaoResgate;
	}

	public BigDecimal getRendPerc() {
		return rendPerc;
	}

	public void setRendPerc(BigDecimal rendPerc) {
		this.rendPerc = rendPerc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Investimento getInvestimento() {
		return investimento;
	}

	public void setInvestimento(Investimento investimento) {
		this.investimento = investimento;
	}

	public BigDecimal getRendBruto() {
		return rendBruto;
	}

	public void setRendBruto(BigDecimal rendBruto) {
		this.rendBruto = rendBruto;
	}

	public BigDecimal getIR() {
		return IR;
	}

	public void setIR(BigDecimal iR) {
		IR = iR;
	}

	public BigDecimal getSaldoBruto() {
		return saldoBruto;
	}

	public void setSaldoBruto(BigDecimal saldoBruto) {
		this.saldoBruto = saldoBruto;
	}

	public BigDecimal getValorResgate() {
		return valorResgate;
	}

	public void setValorResgate(BigDecimal valorResgate) {
		this.valorResgate = valorResgate;
	}
}
