package com.works.cobranca.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
//Service – Classes de serviço e/ou negócio.
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.works.cobranca.model.SituacaoTitulo;
import com.works.cobranca.model.StatusTitulo;
import com.works.cobranca.model.Titulo;
import com.works.cobranca.repository.Titulos;
import com.works.cobranca.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private Titulos titulos;
	
	@Autowired
    private JavaMailSender mailSender;
	
	public void salvar(Titulo titulo) {
		try {
			titulos.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
	}

	public void excluir(Long codigo) {
		titulos.delete(codigo);
	}

	public String receber(Long codigo) {
		Titulo titulo = titulos.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		titulos.save(titulo);		
		return StatusTitulo.RECEBIDO.getDescricao();
	}
	
	public List<Titulo> listarTodos() {
		return titulos.findAll(); 
	}
	
	/*public List<Titulo> filtrar(TituloFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		//return titulos.findByDescricaoContaining(descricao); 
		return titulos.findByDescricaoContainingOrderByDataVencimentoAsc(descricao);
	}*/
	
	public Page<Titulo> filtrar(TituloFilter filtro, Pageable pageable) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return titulos.findByDescricaoContainingOrderByDataVencimentoAsc(descricao, pageable);
	}

	public List<Titulo> findByDataVencimentoAsc() {
		return titulos.findAllByOrderByDataVencimentoAsc();
	}
	
	public BigDecimal findByValorPorSituacao(SituacaoTitulo situacao) {
		return titulos.sumValorPorSituacao(situacao);
	}
	
	public BigDecimal findByValorPorSituacaoAndStatus(SituacaoTitulo situacao, StatusTitulo status) {
		return titulos.sumValorPorSituacaoAndStatus(situacao, status);
	}
	
	public boolean enviarEmail(Long codigo) {
										
		Titulo titulo = titulos.findOne(codigo);	
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = formato.format(titulo.getDataVencimento());
		
		
		String msg = "Caro(a). Por Favor verificar o seguinte título: "  + titulo.getDescricao() + " que"
				+ " venceu no dia " + dataFormatada + ". Caso a situação já tenha sido normalizada, por favor, "
						+ "desconsiderar esse aviso.";
		
		SimpleMailMessage message = new SimpleMailMessage();

        message.setText(msg);
        message.setSubject("Conta em atraso!!!");
        message.setTo(titulo.getEmail());

        try {
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }	
		
        /*try {
            Email email = new SimpleEmail();
            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthentication("casilvamgdev@gmail.com", "caca$$9999AB");
            email.setStartTLSEnabled(true);           
            email.setFrom("casilvamgdev@gmail.com");
            email.setSubject("Débito Vencido ");
            email.setMsg(msg);
            email.addTo(titulo.getEmail());
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }*/
	}	
}