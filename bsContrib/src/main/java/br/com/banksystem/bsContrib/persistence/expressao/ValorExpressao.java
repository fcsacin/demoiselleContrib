package br.com.banksystem.bsContrib.persistence.expressao;

import java.util.List;

public abstract class ValorExpressao implements FieldPath, IExpressao {

	private static final long serialVersionUID = -6720517013967102188L;

	@Override
	public abstract String getCampo();

	public abstract TipoExpressao getTipoParametro();

	public abstract Object getValor();

	public abstract List<Object> getValores();

}
