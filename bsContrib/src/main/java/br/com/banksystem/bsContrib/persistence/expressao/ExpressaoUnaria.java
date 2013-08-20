package br.com.banksystem.bsContrib.persistence.expressao;

import br.com.banksystem.bsContrib.persistence.filtro.Operator;

public abstract class ExpressaoUnaria implements IExpressao {

	private static final long serialVersionUID = 6986722265154247510L;

	private Operator operador;

	public Operator getOperador() {
		return operador;
	}

	public abstract ValorExpressao getValorExpressao();

	public void setOperador(Operator operador) {
		this.operador = operador;
	}

}
