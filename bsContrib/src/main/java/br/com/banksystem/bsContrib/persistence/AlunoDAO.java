package br.com.banksystem.bsContrib.persistence;

import br.com.banksystem.bsContrib.domain.Aluno;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;

@PersistenceController
public class AlunoDAO extends GenericDAO<Aluno, Long> {

	private static final long serialVersionUID = -5732614036453027426L;

}
