package com.works.cobranca.controller;

import com.works.cobranca.model.InfoRendimento;
import com.works.cobranca.model.Investimento;
import com.works.cobranca.service.CadastroRendimentoService;
import com.works.cobranca.service.InfoRendimentoService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;


@Controller
@RequestMapping("/info")
public class InfoRendimentoController {

    @Autowired
	private InfoRendimentoService infoRendimentoService;
	
	@Autowired
	private CadastroRendimentoService cadastroRendimentoService;
	
	@Autowired 
	private ApplicationContext   appContext;  
	
	@RequestMapping("{id}") 
	public ModelAndView pesquisar(@PathVariable("id") Investimento investimento) {
		InfoRendimento info = infoRendimentoService.buscarPorInvestimento(investimento);
			
		ModelAndView mv = new ModelAndView("InfoRendimento");
		mv.addObject("info", info);
		return mv;
	}
	
	@RequestMapping("/grafico/{id}")
	public ModelAndView visualizar_grafico(@PathVariable("id") Investimento investimento) {
		//Propriedades do Header da Response
		Properties header = new Properties();
				
		//Nome do arquivo caso o usuário de Ctrl+S (Salvar)
		//header.put("Content-Disposition", "inline; filename=rendimento_evolucao.pdf");
		header.put("Content-Disposition", "attachment; filename=rendimento_evolucao.pdf");
		JasperReportsPdfView view = new JasperReportsPdfView();
        view.setUrl("classpath:/reports/grafico_rend_br_prev.jrxml");
        view.setApplicationContext(appContext);
        view.setHeaders(header);

       Map<String, Object> params = new HashMap<>();
       params.put("datasource", cadastroRendimentoService.listarTodosPorInvestimento(investimento));

       return new ModelAndView(view, params); 
	}
}
