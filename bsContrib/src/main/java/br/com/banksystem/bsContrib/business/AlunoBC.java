package br.com.banksystem.bsContrib.business;

import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.AlunoDAO;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroGenerico;
import br.gov.frameworkdemoiselle.stereotype.BusinessController;

@BusinessController
public class AlunoBC extends GenericBC<Aluno, Long, AlunoDAO> {

	private static final long serialVersionUID = -6236657683899707758L;

	@Override
	protected void adicionarParametrosConsulta(FiltroGenerico<Aluno> filtro) {
		// TODO Auto-generated method stub

	}

}
