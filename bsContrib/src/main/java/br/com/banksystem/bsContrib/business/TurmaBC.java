package br.com.banksystem.bsContrib.business;

import br.com.banksystem.bsContrib.domain.Turma;
import br.com.banksystem.bsContrib.persistence.TurmaDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.com.banksystem.bsContrib.util.TipoTurma;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class TurmaBC extends GenericBC<Turma, Long, TurmaDAO> {

	private static final long serialVersionUID = 5631622807561514186L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Turma> filtro) {
		// TODO Auto-generated method stub

	}

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Turma("T1", TipoTurma.NORMAL));
			insert(new Turma("T2", TipoTurma.NORMAL));
			insert(new Turma("T3", TipoTurma.NORMAL));
			insert(new Turma("TS1", TipoTurma.SUPLETIVO));
			insert(new Turma("TS2", TipoTurma.SUPLETIVO));
			insert(new Turma("TS3", TipoTurma.SUPLETIVO));
		}
	}

}
