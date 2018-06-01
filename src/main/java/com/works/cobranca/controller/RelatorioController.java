package com.works.cobranca.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.works.cobranca.repository.Titulos;

@Controller
@RequestMapping("relatorio")
public class RelatorioController {
	
	@Autowired
    private Titulos titulosService;
	
	@Autowired 
	private ApplicationContext   appContext;   
		
	    @RequestMapping(method = RequestMethod.GET)
		public ModelAndView report() {

	        JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:/reports/titulos2.jrxml");
	        view.setApplicationContext(appContext);

	        Map<String, Object> params = new HashMap<>();
	        params.put("datasource", titulosService.findAll());

	        return new ModelAndView(view, params);
	    }
	    
	    @RequestMapping(value="/finalizado/pagar", method = RequestMethod.GET)
	    //@RequestMapping(method = RequestMethod.GET)
		public ModelAndView report_finalizado_pagar() {

	        JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:/reports/titulo_finalizado_pagar.jrxml");
	        view.setApplicationContext(appContext);

	        Map<String, Object> params = new HashMap<>();
	        params.put("datasource", titulosService.findAll());

	        return new ModelAndView(view, params);
	    }
	    
	    @RequestMapping(value="/finalizado/receber", method = RequestMethod.GET)
		public ModelAndView report_finalizado_receber() {

	        JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:/reports/titulo_finalizado_receber.jrxml");
	        view.setApplicationContext(appContext);

	        Map<String, Object> params = new HashMap<>();
	        params.put("datasource", titulosService.findAll());

	        return new ModelAndView(view, params);
	    }
	    
	    @RequestMapping(value="/pendente/pagar", method = RequestMethod.GET)
	    //@RequestMapping(method = RequestMethod.GET)
		public ModelAndView report_pendente_pagar() {

	        JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:/reports/titulo_pendente_pagar.jrxml");
	        view.setApplicationContext(appContext);

	        Map<String, Object> params = new HashMap<>();
	        params.put("datasource", titulosService.findAll());

	        return new ModelAndView(view, params);
	    }
	    
	    @RequestMapping(value="/pendente/receber", method = RequestMethod.GET)
		public ModelAndView report_pendente_receber() {

	        JasperReportsPdfView view = new JasperReportsPdfView();
	        view.setUrl("classpath:/reports/titulo_pendente_receber.jrxml");
	        view.setApplicationContext(appContext);

	        Map<String, Object> params = new HashMap<>();
	        params.put("datasource", titulosService.findAll());

	        return new ModelAndView(view, params);
	    }
	}