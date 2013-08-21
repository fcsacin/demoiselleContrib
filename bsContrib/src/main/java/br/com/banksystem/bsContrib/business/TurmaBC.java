package br.com.banksystem.bsContrib.business;

import br.com.banksystem.bsContrib.domain.Turma;
import br.com.banksystem.bsContrib.persistence.TurmaDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;

@BusinessController
public class TurmaBC extends GenericBC<Turma, Long, TurmaDAO> {

	private static final long serialVersionUID = 5631622807561514186L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Turma> filtro) {
		// TODO Auto-generated method stub

	}

}
