package br.com.banksystem.bsContrib.infra;

import br.gov.frameworkdemoiselle.lifecycle.Startup;

public class ApplicationConfig {

	/**
	 * Forca que as conversoes de strings vazias de campos numero nao sejam para 0
	 */
	@Startup
	public void contextInitialized() {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
