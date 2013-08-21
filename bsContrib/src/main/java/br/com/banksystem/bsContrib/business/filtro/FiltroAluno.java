package br.com.banksystem.bsContrib.business.filtro;

import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroCriteriaGenerico;

public class FiltroAluno extends FiltroCriteriaGenerico<Aluno> {

	private static final long serialVersionUID = -1824091228624292167L;

	public static final String NOME = "nome";

	public static final String ALTURA = "altura";

	public static final String NASCIMENTO = "nascimento";

}
