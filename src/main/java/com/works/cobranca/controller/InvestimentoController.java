package com.works.cobranca.controller;

import java.util.Arrays;
import java.util.List;

//Controller – Camada intermediária entre a view e a camada de serviços.


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.works.cobranca.model.Investimento;
import com.works.cobranca.model.TipoInvestimento;
import com.works.cobranca.repository.filter.InvestimentoFilter;
import com.works.cobranca.service.CadastroInvestimentoService;


@Controller
@RequestMapping("/investimentos")
public class InvestimentoController {
	
	private static final String CADASTRO_VIEW = "cadastro/CadastroInvestimento";
	
	@Autowired
	private CadastroInvestimentoService cadastroInvestimentoService;

	@RequestMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Investimento());
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Investimento investimento, Errors errors, RedirectAttributes attributes) {		
		
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
		
		try {
			cadastroInvestimentoService.salvar(investimento);
			attributes.addFlashAttribute("mensagem", "Investimento salvo com sucesso!");
			return "redirect:/investimentos/novo";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	@RequestMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") InvestimentoFilter filtro) {
		List<Investimento> todosinvestimentos = cadastroInvestimentoService.filtrar(filtro);
		
		ModelAndView mv = new ModelAndView("pesquisa/PesquisaInvestimentos");
		mv.addObject("investimentos", todosinvestimentos);
		return mv;
	}
	
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Investimento investimento) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW); 
		mv.addObject(investimento);
		return mv;
	}
	
	@RequestMapping(value="{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		cadastroInvestimentoService.excluir(codigo);		
		attributes.addFlashAttribute("mensagem", "Investimento excluído com sucesso!");
		return "redirect:/investimentos";
	}
	
	@ModelAttribute("todosTipoInvestimento")
	public List<TipoInvestimento> todosTipoInvestimento() {
		return Arrays.asList(TipoInvestimento.values());
	}
}