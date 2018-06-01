package com.works.cobranca.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

//Controller – Camada intermediária entre a view e a camada de serviços.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.works.cobranca.model.InfoRendimento;
import com.works.cobranca.model.Investimento;
import com.works.cobranca.model.Rendimento;
import com.works.cobranca.service.CadastroInvestimentoService;
import com.works.cobranca.service.CadastroRendimentoService;
import com.works.cobranca.service.InfoRendimentoService;


@Controller
@RequestMapping("/rendimentos")
public class RendimentoController {
	
	@NumberFormat(pattern = "#,##0.00")
	BigDecimal somaIR = new BigDecimal("0.0"); 
	
	private static final String CADASTRO_VIEW = "cadastro/CadastroRendimento";
	
	@Autowired
	private CadastroRendimentoService cadastroRendimentoService;
	
	@Autowired
	private InfoRendimentoService infoRendimentoService;
	
	@Autowired
	private CadastroInvestimentoService cadastroInvestimentoService;

	@RequestMapping("/novo/{id}")
	public ModelAndView novo(@PathVariable("id") Investimento investimento) {	
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		Rendimento rendimento = new Rendimento();
		rendimento.setInvestimento(investimento);
		mv.addObject(rendimento);
		return mv;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String salvar(@Validated Rendimento rendimento, Errors errors, RedirectAttributes attributes) {
		
		Investimento investimento = cadastroInvestimentoService.buscarByNome(rendimento.getInvestimento().getNome());
		rendimento.setInvestimento(investimento);
		
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
								
		try {
			cadastroRendimentoService.salvar(rendimento);
			attributes.addFlashAttribute("mensagem", "Rendimento salvo com sucesso!");
			return "redirect:/rendimentos/" + investimento.getCodigo();
			//return "redirect:/investimentos";
		} catch (IllegalArgumentException e) {
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	

	@RequestMapping("/editar/{id}")
	public ModelAndView edicao(@PathVariable("id") Rendimento rendimento) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW); 
		mv.addObject(rendimento);
		return mv;
	}
	
	@RequestMapping("{id}")
	public ModelAndView pesquisar(@PathVariable("id") Investimento investimento) {		
		DecimalFormat decFormat = new java.text.DecimalFormat("#,###,##0.00");
		
		InfoRendimento infoRendimento = new InfoRendimento();
		
		List<Rendimento> todosRendimentos = cadastroRendimentoService.listarTodosPorInvestimento(investimento);	
		BigDecimal somaRendimento = new BigDecimal("0.0");
		BigDecimal somaIR = new BigDecimal("0.0");
		BigDecimal somaRedimentoPerc = new BigDecimal("0.0"); 
		BigDecimal somaRedimentoLiq = new BigDecimal("0.0"); 
		String nomeInvestimento = "";

			for (Rendimento rend : todosRendimentos) {
				somaRendimento = rend.getJuros().add(somaRendimento);
				somaIR = rend.getIR().add(somaIR);
				somaRedimentoPerc = rend.getRendimentoPct().add(somaRedimentoPerc);
				nomeInvestimento = rend.getInvestimento().getNome();
			}
		
		
		ModelAndView mv = new ModelAndView("pesquisa/PesquisaRendimentos");
		mv.addObject("rendimentos", todosRendimentos);
		mv.addObject("nomeInvestimento", nomeInvestimento);
		mv.addObject("codigo", investimento.getCodigo());
		
		mv.addObject("somaRendimentos", decFormat.format(somaRendimento));
		mv.addObject("somaIR", decFormat.format(somaIR));
		
		infoRendimento.setIR(somaIR);
		infoRendimento.setRendBruto(somaRendimento);
					
		mv.addObject("somaRedimentoPerc", decFormat.format(somaRedimentoPerc));
		somaRedimentoLiq = somaRendimento.subtract(somaIR); 
		mv.addObject("somaRendimentoLiq", decFormat.format(somaRedimentoLiq));
		infoRendimento.setSaldoBruto(somaRedimentoLiq);
		infoRendimento.setRendPerc(somaRedimentoPerc);
		infoRendimento.setInvestimento(investimento);
		infoRendimento.setValorResgate(somaRedimentoLiq);
		
		while (infoRendimentoService.buscarPorInvestimento(investimento) != null) {
			InfoRendimento info = infoRendimentoService.buscarPorInvestimento(investimento);
			infoRendimentoService.excluir(info);
		}
		
		if (infoRendimentoService.buscarPorInvestimento(investimento) == null) {
			infoRendimentoService.salvar(infoRendimento);
		}
	
		return mv;
	}
	
	@ModelAttribute("todosInvestimentos")
	public List<Investimento> todosInvestimentos() {
		return cadastroInvestimentoService.listarTodos();
	}
}