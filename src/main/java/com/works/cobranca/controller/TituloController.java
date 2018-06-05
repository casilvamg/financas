package com.works.cobranca.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

//Controller – Camada intermediária entre a view e a camada de serviços.
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.works.cobranca.util.PageWrapper;
import com.works.cobranca.model.SituacaoTitulo;
import com.works.cobranca.model.StatusTitulo;
import com.works.cobranca.model.Titulo;
import com.works.cobranca.repository.filter.TituloFilter;
import com.works.cobranca.service.CadastroTituloService;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	private static final String CADASTRO_VIEW = "cadastro/CadastroTitulo";
	
	@Autowired
	private CadastroTituloService cadastroTituloService;
	
	@Autowired 
	private ApplicationContext   appContext;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
		
		try {
			cadastroTituloService.salvar(titulo);
			attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
			return "redirect:/titulos/novo";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro, Pageable pageable) {
		
		DecimalFormat decFormat = new java.text.DecimalFormat("#,###,##0.00");
		BigDecimal calcFaltantePagar = new BigDecimal("0.0"); 
		BigDecimal calcFaltanteReceber = new BigDecimal("0.0"); 
				
		Page<Titulo> todosTitulos = cadastroTituloService.filtrar(filtro, pageable);		
		PageWrapper<Titulo> page = new PageWrapper<Titulo>(todosTitulos, "/titulos");
		
		calcFaltantePagar = cadastroTituloService.findByValorPorSituacao(SituacaoTitulo.PAGAR).subtract(cadastroTituloService.findByValorPorSituacaoAndStatus(SituacaoTitulo.PAGAR, StatusTitulo.RECEBIDO)); 
		calcFaltanteReceber = cadastroTituloService.findByValorPorSituacao(SituacaoTitulo.RECEBER).subtract(cadastroTituloService.findByValorPorSituacaoAndStatus(SituacaoTitulo.RECEBER, StatusTitulo.RECEBIDO)); 

		ModelAndView mv = new ModelAndView("pesquisa/PesquisaTitulos");
		mv.addObject("titulos", todosTitulos);
		mv.addObject("previstoReceber", decFormat.format(cadastroTituloService.findByValorPorSituacao(SituacaoTitulo.RECEBER)));
		mv.addObject("previstoPagar", decFormat.format(cadastroTituloService.findByValorPorSituacao(SituacaoTitulo.PAGAR)));
		mv.addObject("realizadoReceber", decFormat.format(cadastroTituloService.findByValorPorSituacaoAndStatus(SituacaoTitulo.RECEBER, StatusTitulo.RECEBIDO)));
		mv.addObject("realizadoPagar", decFormat.format(cadastroTituloService.findByValorPorSituacaoAndStatus(SituacaoTitulo.PAGAR, StatusTitulo.RECEBIDO)));
		mv.addObject("calcFaltantePagar", decFormat.format(calcFaltantePagar));
		mv.addObject("calcFaltanteReceber", decFormat.format(calcFaltanteReceber));	
		mv.addObject("page", page);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW); 
		mv.addObject(titulo);
		return mv;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		cadastroTituloService.excluir(codigo);		
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return "redirect:/titulos";
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.PUT)
	public String enviarEmail(@PathVariable Long codigo, RedirectAttributes attributes) {
		boolean result = cadastroTituloService.enviarEmail(codigo);		
		
		if (result == true) {
			attributes.addFlashAttribute("mensagem", "Cobrança enviada com sucesso!");
			return "redirect:/titulos";
		}
		else {
		    attributes.addFlashAttribute("mensagem", "Falha no envio!");
			return "redirect:/titulos";
		}
	}
	
	@RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return cadastroTituloService.receber(codigo);
	}
	
	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}
	
	@ModelAttribute("todosSituacaoTitulo")
	public List<SituacaoTitulo> todosSituacaoTitulo() {
		return Arrays.asList(SituacaoTitulo.values());
	}
	
	//@RequestMapping(method = RequestMethod.GET, value="relatorio")
	@RequestMapping(value="relatorio", method = RequestMethod.GET)
	//@RequestMapping("/relatorio")
	public ModelAndView report() {

        JasperReportsPdfView view = new JasperReportsPdfView();
        view.setUrl("classpath:/reports/titulos2.jrxml");
        view.setApplicationContext(appContext);

        Map<String, Object> params = new HashMap<>();
        params.put("datasource", cadastroTituloService.listarTodos());

        return new ModelAndView(view, params);
    }
}