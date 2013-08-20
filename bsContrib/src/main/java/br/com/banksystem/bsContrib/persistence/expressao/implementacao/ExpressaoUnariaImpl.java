package br.com.banksystem.bsContrib.persistence.expressao.implementacao;

import br.com.banksystem.bsContrib.persistence.expressao.ExpressaoUnaria;
import br.com.banksystem.bsContrib.persistence.expressao.TipoExpressao;
import br.com.banksystem.bsContrib.persistence.expressao.ValorExpressao;
import br.com.banksystem.bsContrib.persistence.filtro.Operator;

public class ExpressaoUnariaImpl extends ExpressaoUnaria {

	private static final long serialVersionUID = 1L;

	private ValorExpressaoImpl valorExpressao;

	public ExpressaoUnariaImpl(final Operator operador, final TipoExpressao tipoParametro, final String campo, final Object... valores) {
		super();
		setOperador(operador);
		setValorExpressao(new ValorExpressaoImpl(tipoParametro, campo, valores));
	}

	public ExpressaoUnariaImpl(final String campo, final Object valor) {
		this(Operator.AND, TipoExpressao.EQUALS, campo, valor);
	}

	public ExpressaoUnariaImpl(final TipoExpressao tipoParametro, final String campo, final Object... valores) {
		this(Operator.AND, tipoParametro, campo, valores);
	}

	@Override
	public ValorExpressao getValorExpressao() {
		return valorExpressao;
	}

	public void setValorExpressao(ValorExpressaoImpl valorExpressao) {
		this.valorExpressao = valorExpressao;
	}
}
