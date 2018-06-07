package com.works.cobranca;

import com.works.cobranca.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;

/*
 *  A interface AuditorAware é usada para obter informações do usuário do Spring
 *  Security para campos createdby e modifiedby.
 * 
 */

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	    public String getCurrentAuditor() { 
	    	return SecurityUtils.getCurrentLogin();
    }
}
