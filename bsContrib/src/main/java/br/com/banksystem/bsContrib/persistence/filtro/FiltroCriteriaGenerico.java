package br.com.banksystem.bsContrib.persistence.filtro;

import java.util.ArrayList;
import java.util.List;
import br.com.banksystem.bsContrib.persistence.expressao.IExpressao;

public abstract class FiltroCriteriaGenerico<T> extends FiltroGenerico<T> {

	private static final long serialVersionUID = 552236894609088416L;

	private List<IExpressao> expressoes;

	public FiltroCriteriaGenerico() {
		super();
	}

	public void addExpressao(final IExpressao expressao) {
		if (getExpressoes() == null) {
			setExpressoes(new ArrayList<IExpressao>());
		}
		getExpressoes().add(expressao);
	}

	public List<IExpressao> getExpressoes() {
		return expressoes;
	}

	@Override
	public void limpar() {
		super.limpar();
		if (expressoes != null) {
			expressoes.clear();
		}
	}

	public void setExpressoes(final List<IExpressao> expressoes) {
		this.expressoes = expressoes;
	};

}
