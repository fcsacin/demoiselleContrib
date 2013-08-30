package br.com.banksystem.bsContrib.business.filtro;

import br.com.banksystem.bsContrib.domain.Aluno;
import br.com.banksystem.bsContrib.persistence.filtro.FiltroCriteriaGenerico;

public class FiltroAluno extends FiltroCriteriaGenerico<Aluno> {

	private static final long serialVersionUID = -1824091228624292167L;

	public static final String NOME = "nome";

	public static final String ALTURA = "altura";

	public static final String NASCIMENTO = "nascimento";

	public static final String FILHOS = "filhos";

	private Integer alturaInicial;

	private Integer alturaFinal;

	public Integer getAlturaFinal() {
		return alturaFinal;
	}

	public Integer getAlturaInicial() {
		return alturaInicial;
	}

	public void setAlturaFinal(Integer alturaFinal) {
		this.alturaFinal = alturaFinal;
	}

	public void setAlturaInicial(Integer alturaInicial) {
		this.alturaInicial = alturaInicial;
	}

}
