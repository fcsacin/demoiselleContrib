package br.com.banksystem.bsContrib.persistence.expressao.implementacao;

import br.com.banksystem.bsContrib.persistence.expressao.ExpressaoBinaria;
import br.com.banksystem.bsContrib.persistence.expressao.IExpressao;
import br.com.banksystem.bsContrib.persistence.filtro.Operator;

public class ExpressaoImpl extends ExpressaoBinaria {

	private static final long serialVersionUID = 1L;

	private IExpressao primeiraExpressao;

	private IExpressao segundaExpressao;

	public ExpressaoImpl(IExpressao primeiraoExpressao, IExpressao segundaExpressao) {
		this(Operator.AND, Operator.AND, primeiraoExpressao, segundaExpressao);
	}

	public ExpressaoImpl(Operator operadorInterno, IExpressao primeiraoExpressao, IExpressao segundaExpressao) {
		this(Operator.AND, operadorInterno, primeiraoExpressao, segundaExpressao);
	}

	public ExpressaoImpl(Operator operadorExpressao, Operator operadorInterno, IExpressao primeiraoExpressao, IExpressao segundaExpressao) {
		super();
		setOperadorExpressao(operadorExpressao);
		setOperadorInterno(operadorInterno);
		setPrimeiraExpressao(primeiraoExpressao);
		setSegundaExpressao(segundaExpressao);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IExpressao getPrimeiraExpressao() {
		return primeiraExpressao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IExpressao getSegundaExpressao() {
		return segundaExpressao;
	}

	public void setPrimeiraExpressao(IExpressao primeiraExpressao) {
		this.primeiraExpressao = primeiraExpressao;
	}

	public void setSegundaExpressao(IExpressao segundaExpressao) {
		this.segundaExpressao = segundaExpressao;
	}

}
