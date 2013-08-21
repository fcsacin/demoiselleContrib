package br.com.banksystem.bsContrib.business;

import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.AlunoDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.lifecycle.Startup;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@BusinessController
public class AlunoBC extends GenericBC<Aluno, Long, AlunoDAO> {

	private static final long serialVersionUID = -6236657683899707758L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Aluno> filtro) {
		// TODO Auto-generated method stub

	}

	@Startup
	@Transactional
	public void load() {
		if (findAll().isEmpty()) {
			insert(new Aluno("Insert1", 20));
			insert(new Aluno("Insert2", 25));
			insert(new Aluno("Insert3", 28));
			insert(new Aluno("Insert4", 15));
			insert(new Aluno("Insert5", 18));
			insert(new Aluno("Insert6", 21));
		}
	}

}
