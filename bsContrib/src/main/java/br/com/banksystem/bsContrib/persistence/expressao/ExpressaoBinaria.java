package br.com.banksystem.bsContrib.persistence.expressao;

import br.com.banksystem.bsContrib.persistence.filtro.Operator;

public abstract class ExpressaoBinaria implements IExpressao {

	private static final long serialVersionUID = -4437214943671939621L;

	private Operator operadorExpressao;

	private Operator operadorInterno;

	public Operator getOperadorExpressao() {
		return operadorExpressao;
	}

	public Operator getOperadorInterno() {
		return operadorInterno;
	}

	public abstract <T extends IExpressao> T getPrimeiraExpressao();

	public abstract <T extends IExpressao> T getSegundaExpressao();

	public void setOperadorExpressao(Operator operadorExpressao) {
		this.operadorExpressao = operadorExpressao;
	}

	public void setOperadorInterno(Operator operadorInterno) {
		this.operadorInterno = operadorInterno;
	}

}
