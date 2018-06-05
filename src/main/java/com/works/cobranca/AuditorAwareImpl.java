package com.works.cobranca;

import org.springframework.data.domain.AuditorAware;

import com.works.cobranca.security.SecurityUtils;


/*
 *  A interface AuditorAware é usada para obter informações do usuário do Spring
 *  Security para campos createdby e modifiedby.
 * 
 */

public class AuditorAwareImpl implements AuditorAware<String>{

	    @Override
	    public String getCurrentAuditor() {		 
	    	return SecurityUtils.getCurrentLogin();
	}
}
